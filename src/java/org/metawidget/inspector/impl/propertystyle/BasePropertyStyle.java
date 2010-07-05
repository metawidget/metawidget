// Metawidget
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

package org.metawidget.inspector.impl.propertystyle;

import java.util.Collections;
import java.util.Map;
import java.util.regex.Pattern;

import org.metawidget.util.CollectionUtils;

/**
 * Convenience implementation for PropertyStyles.
 * <p>
 * Handles caching, excluding names and types, and unwrapping proxies.
 *
 * @author Richard Kennard
 */

public abstract class BasePropertyStyle
	implements PropertyStyle {

	//
	// Private members
	//

	/**
	 * Cache of property lookups.
	 * <p>
	 * Property lookups are potentially expensive, so we cache them. The cache itself is a member
	 * variable, not a static, because we rely on <code>BaseObjectInspector</code> and
	 * <code>ConfigReader</code> to only create one instance of <code>PropertyStyle</code> for all
	 * <code>Inspectors</code>.
	 * <p>
	 * This also stops problems with subclasses of <code>BasePropertyStyle</code> sharing the same
	 * static cache.
	 * <p>
	 * Note: the cache is unbounded, because the number of Classes in the system is fixed. This even
	 * applies to hot deployment products such as FakeReplace, because new Classes are replaced such
	 * that they <code>.equal()</code> their originals.
	 */

	private final Map<Class<?>, Map<String, Property>>	mPropertiesCache	= CollectionUtils.newHashMap();

	private Pattern										mExcludeBaseType;

	//
	// Constructor
	//

	protected BasePropertyStyle( BasePropertyStyleConfig config ) {

		mExcludeBaseType = config.getExcludeBaseType();
	}

	//
	// Public methods
	//

	public Map<String, Property> getProperties( Class<?> clazz ) {

		synchronized ( mPropertiesCache ) {
			Map<String, Property> properties = getCachedProperties( clazz );

			if ( properties == null ) {
				// If the class is not a proxy...

				if ( !isProxy( clazz ) ) {
					// ...inspect it normally

					properties = inspectProperties( clazz );
				} else {
					// ...otherwise, if the superclass is not just java.lang.Object...

					Class<?> superclass = clazz.getSuperclass();

					if ( !superclass.equals( Object.class ) ) {
						// ...inspect the superclass

						properties = getCachedProperties( superclass );

						if ( properties == null ) {
							properties = inspectProperties( superclass );
							cacheProperties( superclass, properties );
						}
					} else {
						// ...otherwise, inspect each interface and merge

						properties = inspectProperties( clazz.getInterfaces() );
					}
				}

				// Cache the result

				cacheProperties( clazz, properties );
			}

			return properties;
		}
	}

	/**
	 * SPI for tools such as <a href="http://code.google.com/p/fakereplace">FakeReplace</a> that
	 * need to clear the properties cache.
	 */

	// TODO: unit test

	public void clearCache() {

		synchronized ( mPropertiesCache ) {
			mPropertiesCache.clear();
		}
	}

	//
	// Protected methods
	//

	/**
	 * Whether to exclude the given property, of the given type, in the given class, when searching
	 * for properties.
	 * <p>
	 * This can be useful when the convention or base class define properties that are
	 * framework-specific, and should be filtered out from 'real' business model properties.
	 * <p>
	 * By default, calls <code>isExcludedReturnType</code> and <code>isExcludedName</code> and
	 * returns true if either of them return true. Returns false otherwise.
	 *
	 * @return true if the property should be excluded, false otherwise
	 */

	protected boolean isExcluded( Class<?> classToExclude, String propertyName, Class<?> propertyType ) {

		if ( isExcludedBaseType( classToExclude ) ) {
			return true;
		}

		if ( isExcludedReturnType( propertyType ) ) {
			return true;
		}

		if ( isExcludedName( propertyName ) ) {
			return true;
		}

		return false;
	}

	/**
	 * Whether to exclude the given base type when searching up the model inheritance chain.
	 * <p>
	 * This can be useful when the base types define properties that are framework-specific, and
	 * should be filtered out from 'real' business model properties.
	 * <p>
	 * By default, excludes any base types from
	 * <code>BasePropertyStyleConfig.setExcludeBaseType</code>.
	 *
	 * @return true if the property should be excluded, false otherwise
	 */

	protected boolean isExcludedBaseType( Class<?> classToExclude ) {

		String className = classToExclude.getName();

		if ( mExcludeBaseType != null && mExcludeBaseType.matcher( className ).matches() ) {
			return true;
		}

		return false;
	}

	/**
	 * Whether to exclude the given property name when searching for properties.
	 * <p>
	 * This can be useful when the convention defines properties that are framework-specific (eg.
	 * <code>getClass()</code>), and should be filtered out from 'real' business model properties.
	 * <p>
	 * By default, does not exclude any names.
	 *
	 * @return true if the property should be excluded, false otherwise
	 */

	protected boolean isExcludedName( String name ) {

		return false;
	}

	/**
	 * Whether to exclude the given property return type when searching for properties.
	 * <p>
	 * This can be useful when the convention or base class define properties that are
	 * framework-specific, and should be filtered out from 'real' business model properties.
	 * <p>
	 * By default, does not exclude any return types.
	 *
	 * @return true if the property should be excluded, false otherwise
	 */

	protected boolean isExcludedReturnType( Class<?> clazz ) {

		return false;
	}

	/**
	 * Returns true if the given class is a 'proxy' of its original self.
	 * <p>
	 * Proxied classes generally don't carry annotations, so it is important to traverse away from
	 * the proxied class back to the original class before inspection.
	 * <p>
	 * By default, returns true for classes with <code>_$$_javassist_</code> or
	 * <code>ByCGLIB$$</code> in their name.
	 */

	protected boolean isProxy( Class<?> clazz ) {

		// (don't use .getSimpleName or .contains, for J2SE 1.4 compatibility)

		String name = clazz.getName();

		if ( name.indexOf( "_$$_javassist_" ) != -1 ) {
			return true;
		}

		if ( name.indexOf( "ByCGLIB$$" ) != -1 ) {
			return true;
		}

		return false;
	}

	/**
	 * Inspect the given Classes and merge their results.
	 * <p>
	 * This version of <code>inspectProperties</code> is used when inspecting the interfaces of a
	 * proxied class. For each interface, it delegates to the single-class version of this method
	 * (ie. <code>inspectProperties( Class )</code>).
	 */

	protected Map<String, Property> inspectProperties( Class<?>[] classes ) {

		Map<String, Property> propertiesToReturn = CollectionUtils.newTreeMap();

		for ( Class<?> clazz : classes ) {
			Map<String, Property> properties = getCachedProperties( clazz );

			if ( properties == null ) {
				properties = inspectProperties( clazz );
				cacheProperties( clazz, properties );
			}

			propertiesToReturn.putAll( properties );
		}

		return propertiesToReturn;
	}

	/**
	 * @return the properties of the given class. Never null.
	 */

	protected abstract Map<String, Property> inspectProperties( Class<?> clazz );

	protected Map<String, Property> getCachedProperties( Class<?> clazz ) {

		return mPropertiesCache.get( clazz );
	}

	protected void cacheProperties( Class<?> clazz, Map<String, Property> properties ) {

		mPropertiesCache.put( clazz, Collections.unmodifiableMap( properties ) );
	}
}
