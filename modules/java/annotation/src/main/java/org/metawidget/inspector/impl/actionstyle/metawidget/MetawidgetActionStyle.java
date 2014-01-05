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

package org.metawidget.inspector.impl.actionstyle.metawidget;

import java.lang.reflect.Method;

import org.metawidget.inspector.annotation.UiAction;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.BaseTraitStyleConfig;
import org.metawidget.inspector.impl.actionstyle.MethodActionStyle;
import org.metawidget.util.ClassUtils;

/**
 * ActionStyle for Metawidget-style actions.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class MetawidgetActionStyle
	extends MethodActionStyle {

	//
	// Constructor
	//

	public MetawidgetActionStyle() {

		this( new BaseTraitStyleConfig() );
	}

	public MetawidgetActionStyle( BaseTraitStyleConfig config ) {

		super( config );
	}

	//
	// Protected methods
	//

	@Override
	protected boolean matchAction( Method method ) {

		UiAction action = ClassUtils.getOriginalAnnotation( method, UiAction.class );

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
