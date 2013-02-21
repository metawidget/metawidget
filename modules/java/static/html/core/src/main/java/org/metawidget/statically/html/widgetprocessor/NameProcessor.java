// Metawidget (licensed under LGPL)
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

import org.metawidget.statically.StaticWidget;
import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.html.StaticHtmlMetawidget;
import org.metawidget.statically.html.widgetbuilder.NameHolder;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

/**
 * WidgetProcessor that adds a 'name' attribute to a StaticXmlWidget.
 *
 * @author Richard Kennard
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
