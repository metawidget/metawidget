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

package org.metawidget.gwt.client.ui.layout;

import java.util.Map;

import org.metawidget.gwt.client.ui.GwtMetawidget;
import org.metawidget.layout.iface.Layout;

import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Layout to simply output components one after another, with no labels and no structure.
 * <p>
 * This Layout is suited to rendering single components, or for rendering components whose layout
 * relies entirely on CSS.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class FlowLayout
	implements Layout<Widget, Panel, GwtMetawidget> {

	//
	// Public methods
	//

	public void layoutWidget( Widget widget, String elementName, Map<String, String> attributes, Panel container, GwtMetawidget metawidget ) {

		metawidget.add( widget );
	}
}
