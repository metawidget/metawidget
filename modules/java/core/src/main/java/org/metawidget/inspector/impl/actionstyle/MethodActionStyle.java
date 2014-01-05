// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

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
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
