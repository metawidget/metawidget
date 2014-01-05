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

package org.metawidget.statically.html.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.statically.StaticWidget;
import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.html.StaticHtmlMetawidget;
import org.metawidget.statically.html.widgetbuilder.NameHolder;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

/**
 * WidgetProcessor that adds a 'name' attribute to a StaticXmlWidget.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class NameProcessor
	implements WidgetProcessor<StaticXmlWidget, StaticHtmlMetawidget> {

	//
	// Public methods
	//

	public StaticXmlWidget processWidget( StaticXmlWidget widget, String elementName, Map<String, String> attributes, StaticHtmlMetawidget metawidget ) {

		if ( widget instanceof NameHolder ) {

			String name = attributes.get( NAME );

			if ( metawidget.getId() != null ) {
				name = StringUtils.camelCase( metawidget.getId() + '-' + name, '-' );
			}

			((NameHolder) widget).setName( name );
		}

		// Do children too (this helps HiddenFieldProcessor)

		for( StaticWidget child : widget.getChildren() ) {

			processWidget( (StaticXmlWidget) child, elementName, attributes, metawidget );
		}

		return widget;
	}
}
