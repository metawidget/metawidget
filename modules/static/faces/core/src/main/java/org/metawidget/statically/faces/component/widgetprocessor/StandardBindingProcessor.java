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

package org.metawidget.statically.faces.component.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.statically.StaticWidget;
import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.faces.StaticFacesUtils;
import org.metawidget.statically.faces.component.html.StaticHtmlMetawidget;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

/**
 * @author Richard Kennard
 */

public class StandardBindingProcessor
	implements WidgetProcessor<StaticWidget, StaticHtmlMetawidget> {

	//
	// Public methods
	//

	public StaticWidget processWidget( StaticWidget widget, String elementName, Map<String, String> attributes, StaticHtmlMetawidget metawidget ) {

		if ( widget instanceof StaticXmlWidget ) {
			String valueExpression = metawidget.getValueExpression();
			valueExpression = StaticFacesUtils.unwrapExpression( valueExpression );
			valueExpression = StaticFacesUtils.wrapExpression( valueExpression + StringUtils.SEPARATOR_DOT_CHAR + attributes.get( NAME ));

			((StaticXmlWidget) widget).putAttribute( "value", valueExpression );
		}

		return widget;
	}
}
