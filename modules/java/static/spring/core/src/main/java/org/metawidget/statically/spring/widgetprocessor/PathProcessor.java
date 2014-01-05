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

package org.metawidget.statically.spring.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.jsp.StaticJspMetawidget;
import org.metawidget.statically.jsp.StaticJspUtils;
import org.metawidget.statically.jsp.widgetprocessor.StandardBindingProcessor;
import org.metawidget.statically.spring.widgetbuilder.SpringTag;
import org.metawidget.util.simple.StringUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class PathProcessor
	extends StandardBindingProcessor {

	//
	// Public methods
	//

	@Override
	public StaticXmlWidget processWidget( StaticXmlWidget widget, String elementName, Map<String, String> attributes, StaticJspMetawidget metawidget ) {

		if ( widget instanceof SpringTag ) {

			String name = attributes.get( NAME );
			String value = metawidget.getValue();

			if ( value != null ) {

				value = StaticJspUtils.unwrapExpression( value );

				// Take the LHS minus the first path (if any), as we assume that will
				// be supplied by the form

				int firstIndexOf = value.indexOf( StringUtils.SEPARATOR_DOT_CHAR );

				if ( firstIndexOf != -1 ) {
					name = value.substring( firstIndexOf + 1 ) + StringUtils.SEPARATOR_DOT_CHAR + name;
				}
			}

			widget.putAttribute( "path", name );
			return widget;
		}

		// Use superclass so that we add 'value' to 'c:out' (Spring doesn't have a native read-only
		// tag)

		return super.processWidget( widget, elementName, attributes, metawidget );
	}
}
