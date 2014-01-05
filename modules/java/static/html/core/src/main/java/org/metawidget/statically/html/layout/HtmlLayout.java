// Metawidget
//
// For historical reasons, this file is licensed under the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html).
//
// Most other files in Metawidget are licensed under both the
// LGPL/EPL and a commercial license. See http://metawidget.org
// for details.

package org.metawidget.statically.html.layout;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.layout.iface.Layout;
import org.metawidget.statically.StaticWidget;
import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.html.StaticHtmlMetawidget;
import org.metawidget.statically.html.widgetbuilder.HtmlLabel;
import org.metawidget.statically.html.widgetbuilder.HtmlTag;
import org.metawidget.util.simple.StringUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 * @author Ryan Bradley
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

	    if ( TRUE.equals( attributes.get(HIDDEN) ) ) {
	        return false;
	    }

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
			label.setTextContent( labelText + StringUtils.SEPARATOR_COLON );
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
