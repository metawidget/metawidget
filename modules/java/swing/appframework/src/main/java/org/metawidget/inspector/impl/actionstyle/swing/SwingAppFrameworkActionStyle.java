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

package org.metawidget.inspector.impl.actionstyle.swing;

import java.awt.event.ActionEvent;
import java.lang.reflect.Method;

import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.BaseTraitStyleConfig;
import org.metawidget.inspector.impl.actionstyle.MethodActionStyle;

/**
 * ActionStyle for Swing AppFramework-style actions.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class SwingAppFrameworkActionStyle
	extends MethodActionStyle {

	//
	// Constructor
	//

	public SwingAppFrameworkActionStyle() {

		this( new BaseTraitStyleConfig() );
	}

	public SwingAppFrameworkActionStyle( BaseTraitStyleConfig config ) {

		super( config );
	}

	//
	// Protected methods
	//

	@Override
	protected boolean matchAction( Method method ) {

		// Exclude Swing AppFramework actions

		if ( method.getDeclaringClass().getPackage().getName().startsWith( "org.jdesktop" ) ) {
			return false;
		}

		org.jdesktop.application.Action action = method.getAnnotation( org.jdesktop.application.Action.class );

		if ( action == null ) {
			return false;
		}

		Class<?>[] parameterTypes = method.getParameterTypes();

		if ( parameterTypes.length > 1 ) {
			throw InspectorException.newException( "@Action " + method + " must not have more than one parameter" );
		}

		if ( parameterTypes.length == 1 && !parameterTypes[0].equals( ActionEvent.class ) ) {
			throw InspectorException.newException( "@Action " + method + " parameter must be a " + ActionEvent.class.getName() );
		}

		return true;
	}
}
