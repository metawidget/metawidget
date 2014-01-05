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

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.faces.StaticFacesUtils;
import org.metawidget.statically.faces.component.StaticUIMetawidget;
import org.metawidget.statically.faces.component.ValueHolder;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class StandardBindingProcessor
	implements WidgetProcessor<StaticXmlWidget, StaticUIMetawidget> {

	//
	// Public methods
	//

	public StaticXmlWidget processWidget( StaticXmlWidget widget, String elementName, Map<String, String> attributes, StaticUIMetawidget metawidget ) {

		if ( widget instanceof ValueHolder ) {

			ValueHolder valueHolder = (ValueHolder) widget;

			// (do not overwrite existing, if any)

			if ( valueHolder.getValue() == null ) {
				String valueExpression = metawidget.getValue();

				if ( valueExpression != null ) {

					// If we are not at the top level, construct the binding

					if ( !ENTITY.equals( elementName ) ) {
						valueExpression = StaticFacesUtils.unwrapExpression( valueExpression );
						valueExpression += StringUtils.SEPARATOR_DOT_CHAR;

						// attribute names *must* be decapitalized for EL to work, even if the
						// actual attribute name starts with an uppercase

						valueExpression += StringUtils.decapitalize( attributes.get( NAME ) );
						valueExpression = StaticFacesUtils.wrapExpression( valueExpression );
					}

					valueHolder.setValue( valueExpression );
				}
			}
		}

		return widget;
	}
}
