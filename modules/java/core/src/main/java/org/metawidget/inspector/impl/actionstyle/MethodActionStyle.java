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
import java.util.Map;

import org.metawidget.inspector.impl.BaseTrait;
import org.metawidget.inspector.impl.BaseTraitStyleConfig;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * Convenience ActionStyle implementation for Method-based actions.
 *
 * @author Richard Kennard
 */

public abstract class MethodActionStyle
	extends BaseActionStyle {

	//
	// Constructor
	//

	protected MethodActionStyle( BaseTraitStyleConfig config ) {

		super( config );
	}

	//
	// Protected methods
	//

	/**
	 * @return the actions of the given class. Never null.
	 */

	@Override
	protected Map<String, Action> inspectActions( String type ) {

		// TreeMap so that returns alphabetically sorted actions

		Map<String, Action> actions = CollectionUtils.newTreeMap( StringUtils.CASE_INSENSITIVE_COMPARATOR );

		// For each action...

		Class<?> clazz = ClassUtils.niceForName( type );

		if ( clazz == null ) {
			return actions;
		}

		for ( Method method : clazz.getMethods() ) {
			// ...that is a match...

			if ( !matchAction( method ) ) {
				continue;
			}

			// ...that is not excluded...

			String methodName = method.getName();

			if ( isExcluded( ClassUtils.getOriginalDeclaringClass( method ), methodName, method.getReturnType() ) ) {
				continue;
			}

			// ...add it

			actions.put( methodName, new MethodAction( methodName, method ) );
		}

		return actions;
	}

	protected abstract boolean matchAction( Method method );

	//
	// Inner classes
	//

	/**
	 * Method-based action.
	 */

	protected static class MethodAction
		extends BaseTrait
		implements Action {

		//
		// Private methods
		//

		private Method	mMethod;

		//
		// Constructor
		//

		public MethodAction( String name, Method method ) {

			super( name );

			mMethod = method;

			if ( mMethod == null ) {
				throw new NullPointerException( "method" );
			}
		}

		//
		// Public methods
		//

		public <T extends Annotation> T getAnnotation( Class<T> annotation ) {

			return ClassUtils.getOriginalAnnotation( mMethod, annotation );
		}
	}
}
