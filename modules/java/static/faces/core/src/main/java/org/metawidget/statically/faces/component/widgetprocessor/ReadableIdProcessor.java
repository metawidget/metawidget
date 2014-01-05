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
