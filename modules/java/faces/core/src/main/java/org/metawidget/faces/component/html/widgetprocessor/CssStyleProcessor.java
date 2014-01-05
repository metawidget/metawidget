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

package org.metawidget.faces.component.html.widgetprocessor;

import java.util.Map;

import javax.faces.component.UIComponent;

import org.metawidget.faces.FacesUtils;
import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.html.HtmlMetawidget;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

/**
 * WidgetProcessor that adds CSS styles to a UIComponent, based on the styles of the parent
 * Metawidget.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class CssStyleProcessor
	implements WidgetProcessor<UIComponent, UIMetawidget> {

	//
	// Public methods
	//

	public UIComponent processWidget( UIComponent component, String elementName, Map<String, String> attributes, UIMetawidget metawidget ) {

		// Note: this only applies the styles to UIStubs at the top-level. In practice, this seemed
		// to give more 'expected' behaviour than drilling into the UIStubs and applying the styles
		// to all their subcomponents too

		HtmlMetawidget htmlMetawidget = (HtmlMetawidget) metawidget;
		FacesUtils.setStyleAndStyleClass( component, htmlMetawidget.getStyle(), htmlMetawidget.getStyleClass() );

		return component;
	}
}
