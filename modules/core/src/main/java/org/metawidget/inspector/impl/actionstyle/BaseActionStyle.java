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

package org.metawidget.inspector.impl.actionstyle;

import java.util.Collections;
import java.util.Map;

import org.metawidget.util.CollectionUtils;

/**
 * Convenience implementation for ActionStyles.
 * <p>
 * Handles caching, and unwrapping proxies.
 *
 * @author Richard Kennard
 */

public abstract class BaseActionStyle
	implements ActionStyle {

	//
	// Private members
	//

	/**
	 * Cache of Action lookups.
	 * <p>
	 * Action lookups are potentially expensive, so we cache them. The cache itself is a member
	 * variable, not a static, because we rely on <code>BaseObjectInspector</code> to only create
	 * one instance of <code>ActionStyle</code> for all <code>Inspectors</code>.
	 * <p>
	 * This also stops problems with subclasses of <code>BaseActionStyle</code> sharing the same
	 * static cache.
	 * <p>
	 * Note: the cache is unbounded, because the number of Classes in the system is fixed. This even
	 * applies to hot deployment products such as FakeReplace, because new Classes are replaced such
	 * that they <code>.equal()</code> their originals.
	 */

	private final Map<Class<?>, Map<String, Action>>	mActionCache	= CollectionUtils.newHashMap();

	//
	// Public methods
	//

	public Map<String, Action> getActions( Class<?> clazz ) {

		synchronized ( mActionCache ) {
			Map<String, Action> actions = getCachedActions( clazz );

			if ( actions == null ) {
				// If the class is not a proxy...

				if ( !isProxy( clazz ) ) {
					// ...inspect it normally

					actions = inspectActions( clazz );
				} else {
					// ...otherwise, if the superclass is not just java.lang.Object...

					Class<?> superclass = clazz.getSuperclass();

					if ( !superclass.equals( Object.class ) ) {
						// ...inspect the superclass

						actions = getCachedActions( superclass );

						if ( actions == null ) {
							actions = inspectActions( superclass );
							cacheActions( superclass, actions );
						}
					} else {
						// ...otherwise, inspect each interface and merge

						actions = inspectActions( clazz.getInterfaces() );
					}
				}

				// Cache the result

				cacheActions( clazz, actions );
			}

			return actions;
		}
	}

	/**
	 * SPI for tools such as <a href="http://code.google.com/p/fakereplace">FakeReplace</a> that
	 * need to clear the action cache.
	 * <p>
	 * This does not affect the immutability of the PropertyStyle, as its external behaviour is
	 * unchanged (it will just be a little slower the next time it is called, while it re-caches).
	 */

	public void clearCache() {

		synchronized ( mActionCache ) {
			mActionCache.clear();
		}
	}

	//
	// Protected methods
	//

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
	 * This version of <code>inspectActions</code> is used when inspecting the interfaces of a
	 * proxied class.
	 */

	protected Map<String, Action> inspectActions( Class<?>[] classes ) {

		Map<String, Action> actionsToReturn = CollectionUtils.newTreeMap();

		for ( Class<?> clazz : classes ) {
			Map<String, Action> actions = getCachedActions( clazz );

			if ( actions == null ) {
				actions = inspectActions( clazz );
				cacheActions( clazz, actions );
			}

			actionsToReturn.putAll( actions );
		}

		return actionsToReturn;
	}

	/**
	 * @return the properties of the given class. Never null.
	 */

	protected abstract Map<String, Action> inspectActions( Class<?> clazz );

	protected Map<String, Action> getCachedActions( Class<?> clazz ) {

		return mActionCache.get( clazz );
	}

	protected void cacheActions( Class<?> clazz, Map<String, Action> actions ) {

		mActionCache.put( clazz, Collections.unmodifiableMap( actions ) );
	}
}
