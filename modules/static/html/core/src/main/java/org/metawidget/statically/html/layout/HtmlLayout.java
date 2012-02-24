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

package org.metawidget.statically.html.layout;

import java.util.Map;

import org.metawidget.layout.iface.Layout;
import org.metawidget.statically.StaticWidget;
import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.html.StaticHtmlMetawidget;
import org.metawidget.statically.html.widgetbuilder.HtmlLabel;
import org.metawidget.statically.html.widgetbuilder.HtmlTag;

/**
 * @author Richard Kennard
 */

public abstract class HtmlLayout
	implements Layout<StaticXmlWidget, StaticXmlWidget, StaticHtmlMetawidget> {

	//
	// Protected methods
	//

	/**
	 * @param elementName
	 *            can be useful if the Layout needs to call a WidgetProcessor
	 * @return whether a label was written
	 */

	protected boolean layoutLabel( HtmlTag tag, StaticXmlWidget widgetNeedingLabel, String elementName, Map<String, String> attributes, StaticHtmlMetawidget metawidget ) {

		HtmlTag label = createLabel( widgetNeedingLabel, elementName, attributes, metawidget );
		tag.getChildren().add( label );
		return true;
	}

	/**
	 * @param elementName
	 *            can be useful if the Layout needs to call a WidgetProcessor
	 */

	protected HtmlTag createLabel( StaticXmlWidget widgetNeedingLabel, String elementName, Map<String, String> attributes, StaticHtmlMetawidget metawidget ) {

		HtmlLabel label = new HtmlLabel();
		String id = getWidgetId( widgetNeedingLabel );

		if ( id != null ) {
			label.putAttribute( "for", id );
		}

		String labelText = metawidget.getLabelString( attributes );
		if ( labelText != null && labelText.length() > 0 ) {
			label.setTextContent( labelText + ":" );
		}

		return label;
	}

	/**
	 * Gets the id attribute of the given widget, recursing into child widgets if necessary.
	 */

	protected String getWidgetId( StaticXmlWidget widget ) {

		String id = widget.getAttribute( "id" );

		if ( id != null ) {
			return id;
		}

		for ( StaticWidget child : widget.getChildren() ) {

			id = getWidgetId( (StaticXmlWidget) child );

			if ( id != null ) {
				return id;
			}
		}

		return null;
	}
}
