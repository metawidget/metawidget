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

package org.metawidget.swing;

import java.awt.Component;

/**
 * Additional interface implemented by Swing WidgetBuilders.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public interface SwingValuePropertyProvider {

	//
	// Methods
	//

	/**
	 * Returns the property used to get/set the value of the component.
	 * <p>
	 * If the component is not known, returns <code>null</code>.
	 */

	String getValueProperty( Component component );
}
