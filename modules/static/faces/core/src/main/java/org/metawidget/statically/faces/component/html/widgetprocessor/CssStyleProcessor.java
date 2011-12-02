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

package org.metawidget.statically.faces.component.html.widgetprocessor;

import java.util.Map;

import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.faces.component.html.StaticHtmlMetawidget;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

/**
 * WidgetProcessor that adds CSS styles to a StaticXmlWidget, based on the styles of the parent
 * StaticHtmlMetawidget.
 *
 * @author Richard Kennard
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
