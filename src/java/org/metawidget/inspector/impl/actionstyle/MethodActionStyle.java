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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

import org.metawidget.util.CollectionUtils;

/**
 * Convenience ActionStyle implementation for Method-based actions.
 *
 * @author Richard Kennard
 */

public abstract class MethodActionStyle
	implements ActionStyle
{
	//
	//
	// Private members
	//
	//

	/**
	 * Cache of action lookups.
	 * <p>
	 * Action lookups are potentially expensive, so we cache them. The cache itself is a member
	 * variable, not a static, because we rely on <code>BaseObjectInspector</code> to only
	 * create one instance of <code>ActionStyle</code> for all <code>Inspectors</code>.
	 * <p>
	 * This also stops problems with subclasses of <code>MetawidgetActionStyle</code> sharing the
	 * same static cache.
	 */

	private Map<Class<?>, Map<String, Action>>	mActionCache	= CollectionUtils.newHashMap();

	//
	//
	// Public methods
	//
	//

	/**
	 * Returns actions sorted by name.
	 */

	public Map<String, Action> getActions( Class<?> clazz )
	{
		synchronized ( mActionCache )
		{
			Map<String, Action> actions = mActionCache.get( clazz );

			if ( actions == null )
			{
				actions = inspectActions( clazz );
				mActionCache.put( clazz, Collections.unmodifiableMap( actions ) );
			}

			return actions;
		}
	}

	//
	//
	// Protected methods
	//
	//

	/**
	 * @return the actions of the given class. Never null.
	 */

	protected abstract Map<String, Action> inspectActions( Class<?> clazz );

	//
	//
	// Inner classes
	//
	//

	/**
	 * Method-based action.
	 */

	protected static class MethodAction
		extends BaseAction
	{
		//
		//
		// Private methods
		//
		//

		private Method	mMethod;

		//
		//
		// Constructor
		//
		//

		public MethodAction( String name, Method method )
		{
			super( name );

			mMethod = method;

			if ( mMethod == null )
				throw new NullPointerException( "method" );
		}

		//
		//
		// Public methods
		//
		//

		public <T extends Annotation> T getAnnotation( Class<T> annotation )
		{
			return mMethod.getAnnotation( annotation );
		}
	}
}
