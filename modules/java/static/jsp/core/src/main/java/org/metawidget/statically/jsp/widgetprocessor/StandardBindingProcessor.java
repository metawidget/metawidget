// Metawidget
//
// For historical reasons, this file is licensed under the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html).
//
// Most other files in Metawidget are licensed under both the
// LGPL/EPL and a commercial license. See http://metawidget.org
// for details.

package org.metawidget.statically.jsp.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.statically.StaticWidget;
import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.html.widgetbuilder.ValueHolder;
import org.metawidget.statically.jsp.StaticJspMetawidget;
import org.metawidget.statically.jsp.StaticJspUtils;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

/**
 * WidgetProcessor that binds the value of a JSP element.
 *
 * @author Ryan Bradley
 */

public class StandardBindingProcessor
	implements WidgetProcessor<StaticXmlWidget, StaticJspMetawidget> {

	//
	// Public methods
	//

	public StaticXmlWidget processWidget( StaticXmlWidget widget, String elementName, Map<String, String> attributes, StaticJspMetawidget metawidget ) {

		// (do not overwrite existing, if any)

		if ( widget instanceof ValueHolder ) {

			ValueHolder valueHolder = (ValueHolder) widget;

			if ( valueHolder.getValue() == null ) {

				String valueExpression = metawidget.getValue();

				if ( valueExpression != null ) {

					// If we are not at the top level, construct the binding.

					if ( !ENTITY.equals( elementName ) ) {
						valueExpression = StaticJspUtils.unwrapExpression( valueExpression );
						valueExpression += StringUtils.SEPARATOR_DOT_CHAR;

						// Attributes must be decapitalized for the EL to work.

						valueExpression += StringUtils.decapitalize( attributes.get( NAME ) );
						valueExpression = StaticJspUtils.wrapExpression( valueExpression );
					}

					valueHolder.setValue( valueExpression );
				}
			}
		}

		// Do children too (this helps HiddenFieldProcessor)

		for( StaticWidget child : widget.getChildren() ) {

			processWidget( (StaticXmlWidget) child, elementName, attributes, metawidget );
		}

		return widget;
	}
}
