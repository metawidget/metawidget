// Metawidget
//
// This library is dual licensed under both LGPL and a commercial
// license.
//
// LGPL: this library is free software; you can redistribute it and/or
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
//
// Commercial License: See http://metawidget.org for details

package org.metawidget.statically.faces.component.widgetprocessor;

import java.util.Map;

import org.metawidget.statically.StaticXmlMetawidget;
import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.faces.StaticFacesUtils;
import org.metawidget.statically.faces.component.ValueHolder;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

/**
 * WidgetProcessor to set 'human readable' ids on a StaticXmlWidget.
 * <p>
 * Unlike <code>UIViewRoot.createUniqueId</code>, tries to make the id human readable, both for
 * debugging purposes and for when running unit tests (using, say, WebTest). Because the ids are
 * based off the value expression (or method expression) of the StaticXmlWidget, this
 * WidgetProcessor must come after <code>StandardBindingProcessor</code> (or equivalent).
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class ReadableIdProcessor
	implements WidgetProcessor<StaticXmlWidget, StaticXmlMetawidget> {

	//
	// Public methods
	//

	public StaticXmlWidget processWidget( StaticXmlWidget widget, String elementName, Map<String, String> attributes, StaticXmlMetawidget metawidget ) {

		if ( widget instanceof ValueHolder ) {

			// (do not overwrite existing, if any)

			if ( widget.getAttribute( "id" ) == null ) {

				ValueHolder valueWidget = (ValueHolder) widget;
				String valueExpression = valueWidget.getValue();

				if ( valueExpression != null && !"".equals( valueExpression )) {
					valueExpression = StaticFacesUtils.unwrapExpression( valueExpression );
					widget.putAttribute( "id", StringUtils.camelCase( valueExpression, StringUtils.SEPARATOR_DOT_CHAR ) );
				}
			}
		}

		return widget;
	}
}
