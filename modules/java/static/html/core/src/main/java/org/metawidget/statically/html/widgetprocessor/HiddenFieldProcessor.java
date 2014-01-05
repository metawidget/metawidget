// Metawidget
//
// For historical reasons, this file is licensed under the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html).
//
// Most other files in Metawidget are licensed under both the
// LGPL/EPL and a commercial license. See http://metawidget.org
// for details.

package org.metawidget.statically.html.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.statically.StaticXmlStub;
import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.html.StaticHtmlMetawidget;
import org.metawidget.statically.html.widgetbuilder.HtmlInput;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

/**
 * WidgetProcessor that adds HTML <code>&lt;input type="hidden"&gt;</code> tags to hidden and
 * read-only values, so that they POST back.
 * <p>
 * Note: passing values via hidden tags is a potential security risk: they can be modified by
 * malicious clients before being returned to the server.
 *
 * @author Ryan Bradley
 */

public class HiddenFieldProcessor
	implements WidgetProcessor<StaticXmlWidget, StaticHtmlMetawidget> {

	//
	// Public methods
	//

	public StaticXmlWidget processWidget( StaticXmlWidget widget, String elementName, Map<String, String> attributes, StaticHtmlMetawidget metawidget ) {

		// Not hidden?

		if ( !TRUE.equals( attributes.get( HIDDEN ) ) ) {

			// Not read-only either?

			if ( !TRUE.equals( attributes.get( READ_ONLY ))) {
				return widget;
			}

			// HtmlOutput and c:out are good, nested Metawidgets are not

			if ( widget instanceof StaticHtmlMetawidget ) {
				return widget;
			}

			HtmlInput hidden = new HtmlInput();
			hidden.putAttribute( "type", "hidden" );
			widget.getChildren().add( hidden );

			return widget;
		}

		String value = widget.toString();

		if ( !TRUE.equals( attributes.get( HIDDEN ) ) && "".equals( value ) ) {
			return new StaticXmlStub();
		}

		HtmlInput hidden = new HtmlInput();
		hidden.putAttribute( "type", "hidden" );

		return hidden;
	}

}
