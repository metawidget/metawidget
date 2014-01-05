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

package org.metawidget.statically.faces.component.html.widgetprocessor;

import java.util.Map;

import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.faces.component.html.StaticHtmlMetawidget;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

/**
 * WidgetProcessor that adds CSS styles to a StaticXmlWidget, based on the styles of the parent
 * StaticHtmlMetawidget.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class CssStyleProcessor
	implements WidgetProcessor<StaticXmlWidget, StaticHtmlMetawidget> {

	//
	// Public methods
	//

	public StaticXmlWidget processWidget( StaticXmlWidget widget, String elementName, Map<String, String> attributes, StaticHtmlMetawidget metawidget ) {

		// Note: this only applies the styles to UIStubs at the top-level. In practice, this seemed
		// to give more 'expected' behaviour than drilling into the UIStubs and applying the styles
		// to all their subcomponents too

		String style = metawidget.getStyle();

		if (style != null ) {
			String existingStyle = widget.getAttribute( "style" );

			if ( existingStyle == null || "".equals( existingStyle ) ) {
				widget.putAttribute( "style", style );
			} else {
				widget.putAttribute( "style", existingStyle + " " + style );
			}
		}

		String styleClass = metawidget.getStyleClass();

		if ( styleClass != null ) {
			String existingStyleClass = widget.getAttribute( "styleClass" );

			if ( existingStyleClass == null || "".equals( existingStyleClass ) ) {
				widget.putAttribute( "styleClass", styleClass );
			} else {
				widget.putAttribute( "styleClass", existingStyleClass + " " + styleClass );
			}
		}

		return widget;
	}
}
