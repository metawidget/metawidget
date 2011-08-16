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

package org.metawidget.inspector.impl;

import java.util.Collections;
import java.util.Map;
import java.util.regex.Pattern;

import org.metawidget.util.CollectionUtils;

/**
 * Convenience implementation for ActionStyles and PropertyStyles.
 * <p>
 * Handles caching and excluding names and types.
 *
 * @author Richard Kennard
 */

public abstract class BaseTraitStyle<T extends Trait> {

	//
	// Private members
	//

	/**
	 * Cache of trait lookups.
	 * <p>
	 * Lookups are potentially expensive, so we cache them. The cache itself is a member variable,
	 * not a static, because we rely on <code>BaseObjectInspector</code> and
	 * <code>ConfigReader</code> to only create one instance of <code>ActionStyle</code> and
	 * <code>PropertyStyle</code> for all <code>Inspectors</code>.
	 * <p>
	 * This also stops problems with subclasses of <code>BaseTraitStyle</code> sharing the same
	 * static cache.
	 * <p>
	 * Note: the cache is unbounded, because the number of Classes in the system is fixed. This even
	 * applies to hot deployment products such as FakeReplace, because new Classes are replaced such
	 * that they <code>.equal()</code> their originals.
	 */

	/* package private */final Map<Class<?>, Map<String, T>>	mCache;

	private Pattern												mExcludeBaseType;

	//
	// Constructor
	//

	protected BaseTraitStyle( BaseTraitStyleConfig config ) {

		if ( config.isCacheLookups() ) {
			mCache = CollectionUtils.newWeakHashMap();
		} else {
			mCache = null;
		}

		mExcludeBaseType = config.getExcludeBaseType();
	}

	//
	// Public methods
	//

	/**
	 * SPI for tools such as <a href="http://code.google.com/p/fakereplace">FakeReplace</a> that
	 * need to clear the cache.
	 * <p>
	 * This does not affect immutability, as our external behaviour is unchanged (we will just be a
	 * little slower the next time we are called, while we re-cache).
	 */

	public void clearCache() {

		if ( mCache == null ) {
			return;
		}

		synchronized ( mCache ) {
			mCache.clear();
		}
	}

	//
	// Protected methods
	//

	protected final Map<String, T> getTraits( Class<?> clazz ) {

		if ( mCache == null ) {
			return getUncachedTraits( clazz );
		}

		synchronized ( mCache ) {
			Map<String, T> traits = getCachedTraits( clazz );

			if ( traits == null ) {
				traits = getUncachedTraits( clazz );
				cacheTraits( clazz, traits );
			}

			return traits;
		}
	}

	protected final Map<String, T> getCachedTraits( Class<?> clazz ) {

		return mCache.get( clazz );
	}

	protected final void cacheTraits( Class<?> clazz, Map<String, T> traits ) {

		mCache.put( clazz, Collections.unmodifiableMap( traits ) );
	}

	protected abstract Map<String, T> getUncachedTraits( Class<?> clazz );

	/**
	 * Whether to exclude the given trait, of the given type, in the given class, when searching
	 * for traits.
	 * <p>
	 * This can be useful when the convention or base class define traits that are
	 * framework-specific, and should be filtered out from 'real' business model traits.
	 * <p>
	 * By default, calls <code>isExcludedReturnType</code> and <code>isExcludedName</code> and
	 * returns true if either of them return true. Returns false otherwise.
	 *
	 * @return true if the trait should be excluded, false otherwise
	 */

	protected final boolean isExcluded( Class<?> classToExclude, String name, Class<?> returnType ) {

		if ( isExcludedBaseType( classToExclude ) ) {
			return true;
		}

		if ( isExcludedReturnType( returnType ) ) {
			return true;
		}

		if ( isExcludedName( name ) ) {
			return true;
		}

		return false;
	}

	/**
	 * Whether to exclude the given base type when searching up the model inheritance chain.
	 * <p>
	 * This can be useful when the base types define traits that are framework-specific, and should
	 * be filtered out from 'real' business model traits.
	 * <p>
	 * By default, excludes any base types from <code>BaseTraitStyleConfig.setExcludeBaseType</code>.
	 *
	 * @return true if the trait should be excluded, false otherwise
	 */

	protected boolean isExcludedBaseType( Class<?> classToExclude ) {

		String className = classToExclude.getName();

		if ( mExcludeBaseType != null && mExcludeBaseType.matcher( className ).matches() ) {
			return true;
		}

		return false;
	}

	/**
	 * Whether to exclude the given name when searching for traits.
	 * <p>
	 * This can be useful when the convention defines traits that are framework-specific (eg.
	 * <code>getClass()</code>), and should be filtered out from 'real' business model traits.
	 * <p>
	 * By default, does not exclude any names.
	 *
	 * @param name
	 *            to consider for exclusion
	 * @return true if the trait should be excluded, false otherwise
	 */

	protected boolean isExcludedName( String name ) {

		return false;
	}

	/**
	 * Whether to exclude the given trait return type when searching for traits.
	 * <p>
	 * This can be useful when the convention or base class define traits that are
	 * framework-specific, and should be filtered out from 'real' business model traits.
	 * <p>
	 * By default, does not exclude any return types.
	 *
	 * @param clazz
	 *            return type to consider for exclusion
	 * @return true if the trait should be excluded, false otherwise
	 */

	protected boolean isExcludedReturnType( Class<?> clazz ) {

		return false;
	}
}
