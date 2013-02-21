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

package org.metawidget.statically.spring.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.statically.StaticXmlStub;
import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.jsp.StaticJspUtils;
import org.metawidget.statically.spring.StaticSpringMetawidget;
import org.metawidget.statically.spring.widgetbuilder.FormHiddenTag;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

/**
 * WidgetProcessor that sets <code>HiddenTag.setDisabled( false )</code> on all
 * <code>HiddenTag</code>s, so that they POST back.
 * <p>
 * Note: passing values via hidden tags is a potential security risk: they can be modified by
 * malicious clients before being returned to the server.
 *
 * @author Ryan Bradley
 */

public class HiddenFieldProcessor
	implements WidgetProcessor<StaticXmlWidget, StaticSpringMetawidget> {

	//
	// Public methods
	//

	public StaticXmlWidget processWidget( StaticXmlWidget widget, String elementName, Map<String, String> attributes, StaticSpringMetawidget metawidget ) {

		// Not hidden?

		if ( !TRUE.equals( attributes.get( HIDDEN ) ) ) {
			return widget;
		}

		// Must process the widget's path again, as we are replacing a StaticXmlStub (containing the
		// path), with a FormHiddenTag.

		String name = attributes.get( NAME );
		String value = metawidget.getValue();

		if ( value != null ) {

			value = StaticJspUtils.unwrapExpression( value );

			// Take the LHS minus the first path (if any), as we assume that will
			// be supplied by the form

			int firstIndexOf = value.indexOf( StringUtils.SEPARATOR_DOT_CHAR );

			if ( firstIndexOf != -1 ) {
				name = value.substring( firstIndexOf + 1 ) + StringUtils.SEPARATOR_DOT + name;
			}
		}

		if ( !TRUE.equals( attributes.get( HIDDEN ) ) && "".equals( attributes.get( HIDDEN ) ) ) {
			return new StaticXmlStub();
		}

		FormHiddenTag hidden = new FormHiddenTag();
		hidden.putAttribute( "path", name );
		return hidden;
	}

}
