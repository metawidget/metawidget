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
		}

        // HtmlOutput and c:out are good, nested Metawidgets are not

        if ( widget instanceof StaticHtmlMetawidget ) {
            return widget;
        }

        HtmlInput hidden = new HtmlInput();
        hidden.putAttribute( "type", "hidden" );

        String value = widget.toString();

        if ( !TRUE.equals( attributes.get( HIDDEN ) ) && "".equals( value ) ) {
            return new StaticXmlStub();
        }

        if ( widget instanceof StaticXmlStub ) {
            return hidden;          
        }

        widget.getChildren().add( hidden );

        return widget;
	}

}
