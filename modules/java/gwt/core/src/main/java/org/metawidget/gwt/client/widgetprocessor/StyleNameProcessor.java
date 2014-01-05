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

package org.metawidget.gwt.client.widgetprocessor;

import java.util.Map;

import org.metawidget.gwt.client.ui.GwtMetawidget;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

import com.google.gwt.user.client.ui.Widget;

/**
 * WidgetProcessor that calls <code>addStyleName</code> on a Widget, based on
 * the <code>getStyleName</code> of the parent Metawidget.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class StyleNameProcessor
	implements WidgetProcessor<Widget, GwtMetawidget> {

	//
	// Public methods
	//

	public Widget processWidget( Widget widget, String elementName, Map<String, String> attributes, GwtMetawidget metawidget ) {

		// Note: this only applies the styles to Stubs at the top-level. In practice, this seemed
		// to give more 'expected' behaviour than drilling into the Stubs and applying the styles
		// to all their subcomponents too

		String styleName = metawidget.getStyleName();

		if ( styleName != null && styleName.length() != 0 ) {
			widget.addStyleName( styleName );
		}

		return widget;
	}
}
