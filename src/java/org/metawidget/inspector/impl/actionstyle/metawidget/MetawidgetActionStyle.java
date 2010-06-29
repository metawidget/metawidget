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

package org.metawidget.inspector.impl.actionstyle.metawidget;

import java.lang.reflect.Method;

import org.metawidget.inspector.annotation.UiAction;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.actionstyle.MethodActionStyle;

/**
 * ActionStyle for Metawidget-style actions.
 *
 * @author Richard Kennard
 */

public class MetawidgetActionStyle
	extends MethodActionStyle {

	//
	// Protected methods
	//

	@Override
	protected boolean matchAction( Method method ) {

		UiAction action = method.getAnnotation( UiAction.class );

		if ( action == null ) {
			return false;
		}

		// Note: @UiAction must not take any parameters, because it is cross-platform. However the
		// action methods themselves may take, say, a java.awt.event.ActionEvent and they may
		// be annotated by something framework-specific like org.jdesktop.application.Action

		if ( method.getParameterTypes().length > 0 ) {
			throw InspectorException.newException( "@UiAction " + method + " must not take any parameters" );
		}

		return true;
	}
}
