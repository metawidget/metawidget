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

package org.metawidget.statically.jsp.html.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.statically.StaticXmlMetawidget;
import org.metawidget.statically.StaticXmlStub;
import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

/**
 * @author Richard Kennard
 */

public class HtmlWidgetBuilder
	implements WidgetBuilder<StaticXmlWidget, StaticXmlMetawidget> {

	//
	// Private statics
	//

	private final static String	MAX_LENGTH	= "maxLength";

	//
	// Public methods
	//

	public StaticXmlWidget buildWidget( String elementName, Map<String, String> attributes, StaticXmlMetawidget metawidget ) {

		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) ) {
			return new StaticXmlStub();
		}

		// Action

		if ( ACTION.equals( elementName ) ) {
			return new StaticXmlStub();
		}

		String type = WidgetBuilderUtils.getActualClassOrType( attributes );

		// If no type, fail gracefully with a text box

		if ( type == null ) {
			return createHtmlInputText( attributes );
		}

		// Lookup the Class

		Class<?> clazz = ClassUtils.niceForName( type );

		if ( clazz != null ) {

			// Primitives

			if ( clazz.isPrimitive() ) {
				return createHtmlInputText( attributes );
			}

			// String

			if ( String.class.equals( clazz ) ) {
				if ( TRUE.equals( attributes.get( LARGE ) ) ) {
					return new HtmlTag( "textarea" );
				}

				if ( TRUE.equals( attributes.get( MASKED ) ) ) {
					HtmlTag inputSecret = new HtmlTag( "input" );
					inputSecret.putAttribute( "type", "secret" );
					inputSecret.putAttribute( MAX_LENGTH, attributes.get( MAXIMUM_LENGTH ) );
					return inputSecret;
				}

				return createHtmlInputText( attributes );
			}
		}

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) ) {
			return createHtmlInputText( attributes );
		}

		// Not simple

		return null;
	}

	//
	// Private methods
	//

	private HtmlTag createHtmlInputText( Map<String, String> attributes ) {

		HtmlTag inputText = new HtmlTag( "input" );
		inputText.putAttribute( "type", "text" );
		inputText.putAttribute( MAX_LENGTH, attributes.get( MAXIMUM_LENGTH ) );

		return inputText;
	}
}
