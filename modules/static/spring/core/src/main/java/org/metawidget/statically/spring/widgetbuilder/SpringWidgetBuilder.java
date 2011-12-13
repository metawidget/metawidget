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

package org.metawidget.statically.spring.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.spring.SpringInspectionResultConstants.*;

import java.util.Map;

import org.metawidget.statically.StaticXmlStub;
import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.jsp.html.widgetbuilder.HtmlTag;
import org.metawidget.statically.spring.StaticSpringMetawidget;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

/**
 * @author Richard Kennard
 * @author Ryan Bradley
 */

public class SpringWidgetBuilder
	implements WidgetBuilder<StaticXmlWidget, StaticSpringMetawidget> {

	//
	// Private statics
	//

	private final static String	MAX_LENGTH	= "maxLength";

	//
	// Public methods
	//

	public StaticXmlWidget buildWidget( String elementName, Map<String, String> attributes, StaticSpringMetawidget metawidget ) {

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
		
        // Support mandatory Booleans (can be rendered as a checkbox, even though they have a
        // Lookup)
		
		if ( Boolean.class.equals( clazz ) && TRUE.equals( REQUIRED )) {
		    // Create a checkbox?
		    return createHtmlInputText( attributes );
		}
		
		// Spring Lookups
		
		String springLookup = attributes.get( SPRING_LOOKUP );
		
		if( springLookup != null & !"".equals( springLookup ) ) {
		    // Create select tag.
		}

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

	private StaticXmlWidget createHtmlInputText( Map<String, String> attributes ) {

		FormInputTag input = new FormInputTag();
		input.putAttribute( MAX_LENGTH, attributes.get( MAXIMUM_LENGTH ) );

		return input;
	}
	
}
