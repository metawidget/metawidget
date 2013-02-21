// Metawidget (licensed under LGPL)
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

package org.metawidget.inspector.impl.actionstyle.swing;

import java.awt.event.ActionEvent;
import java.lang.reflect.Method;

import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.BaseTraitStyleConfig;
import org.metawidget.inspector.impl.actionstyle.MethodActionStyle;

/**
 * ActionStyle for Swing AppFramework-style actions.
 *
 * @author Richard Kennard
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
