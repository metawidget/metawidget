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

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.metawidget.statically.StaticXmlStub;
import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.jsp.StaticJspMetawidget;
import org.metawidget.statically.layout.SimpleLayout;
import org.metawidget.statically.spring.StaticSpringMetawidget;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.metawidget.widgetbuilder.iface.WidgetBuilderException;

/**
 * @author Richard Kennard
 * @author Ryan Bradley
 */

public class SpringWidgetBuilder
	implements WidgetBuilder<StaticXmlWidget, StaticSpringMetawidget> {

	//
	// Private statics
	//

	private final static String			MAX_LENGTH			= "maxlength";

	private static final List<Boolean>	LIST_BOOLEAN_VALUES	= CollectionUtils.unmodifiableList( Boolean.TRUE, Boolean.FALSE );

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

		if ( type == null ) {
			type = String.class.getName();
		}

		// Lookup the Class

		Class<?> clazz = ClassUtils.niceForName( type );

		// Support mandatory Booleans (can be rendered as a checkbox, even though they have a
		// Lookup)

		if ( Boolean.class.equals( clazz ) && TRUE.equals( REQUIRED ) ) {
			return new FormCheckboxTag();
		}

		// Spring Lookups

		String springLookup = attributes.get( SPRING_LOOKUP );

		if ( springLookup != null && !"".equals( springLookup ) ) {
			return createFormSelectTag( springLookup, attributes );
		}

		// String Lookups

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) ) {
			return createFormSelectTag( CollectionUtils.fromString( lookup ), CollectionUtils.fromString( attributes.get( LOOKUP_LABELS ) ), attributes );
		}

		if ( clazz != null ) {

			// Primitives

			if ( clazz.isPrimitive() ) {

				if ( boolean.class.equals( clazz ) ) {
					return new FormCheckboxTag();
				}

				if ( char.class.equals( clazz ) ) {
					attributes.put( MAXIMUM_LENGTH, "1" );
					return createFormInputTag( attributes );
				}

				return createFormInputTag( attributes );
			}

			// String

			if ( String.class.equals( clazz ) ) {
				if ( TRUE.equals( attributes.get( LARGE ) ) ) {
					return createFormTextareaTag( attributes );
				}

				if ( TRUE.equals( attributes.get( MASKED ) ) ) {
					FormPasswordTag passwordTag = new FormPasswordTag();
					passwordTag.putAttribute( MAX_LENGTH, attributes.get( MAXIMUM_LENGTH ) );
					return passwordTag;
				}

				return createFormInputTag( attributes );
			}

			// Character

			if ( Character.class.equals( clazz ) ) {
				attributes.put( MAXIMUM_LENGTH, "1" );
				return createFormInputTag( attributes );
			}

			// Dates

			if ( Date.class.equals( clazz ) ) {
				return createFormInputTag( attributes );
			}

			// Booleans (are tri-state)

			if ( Boolean.class.equals( clazz ) ) {
				return createFormSelectTag( LIST_BOOLEAN_VALUES, null, attributes );
			}

			// Numbers

			if ( Number.class.isAssignableFrom( clazz ) ) {
				return createFormInputTag( attributes );
			}

			// Collections will be handled by JspWidgetBuilder. As a niceity, we copy the Inspectors
			// from our own SpringMetawidget. But we mustn't copy anything else (such as
			// WidgetBuilders, Layouts etc) as they may be Spring-specific

			if ( Collection.class.isAssignableFrom( clazz ) ) {
				StaticJspMetawidget nestedMetawidget = new StaticJspMetawidget();
				nestedMetawidget.setInspector( metawidget.getInspector() );
				nestedMetawidget.setLayout( new SimpleLayout() );
				nestedMetawidget.setPath( metawidget.getPath() + StringUtils.SEPARATOR_FORWARD_SLASH_CHAR + attributes.get( NAME ) );
				return nestedMetawidget;
			}
		}

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) ) {
			return createFormInputTag( attributes );
		}

		// Not simple

		return null;
	}

	//
	// Private methods
	//

	private FormInputTag createFormInputTag( Map<String, String> attributes ) {

		FormInputTag input = new FormInputTag();
		input.putAttribute( MAX_LENGTH, attributes.get( MAXIMUM_LENGTH ) );

		return input;
	}

	private StaticXmlWidget createFormTextareaTag( Map<String, String> attributes ) {

		FormTextareaTag textarea = new FormTextareaTag();

		String rows = attributes.get( "rows" );

		if ( rows != null ) {
			textarea.putAttribute( "rows", rows );
		}

		String cols = attributes.get( "cols" );

		if ( cols != null ) {
			textarea.putAttribute( "cols", cols );
		}

		return textarea;
	}

	private FormSelectTag createFormSelectTag( String expression, Map<String, String> attributes ) {

		// Write the SELECT tag.

		FormSelectTag selectTag = new FormSelectTag();

		// Empty option

		if ( WidgetBuilderUtils.needsEmptyLookupItem( attributes ) ) {
			FormOptionTag emptyOption = new FormOptionTag();
			emptyOption.putAttribute( "value", "" );

			// Add the empty option to the SELECT tag

			selectTag.getChildren().add( emptyOption );
		}

		// Options tag

		FormOptionsTag optionsTag = new FormOptionsTag();
		optionsTag.putAttribute( "items", expression );

		String itemValue = attributes.get( SPRING_LOOKUP_ITEM_VALUE );

		if ( itemValue != null ) {
			optionsTag.putAttribute( "itemValue", itemValue );
		}

		String itemLabel = attributes.get( SPRING_LOOKUP_ITEM_LABEL );

		if ( itemLabel != null ) {
			optionsTag.putAttribute( "itemLabel", itemLabel );
		}

		// Add the <form:options> tag as a child of <form:select>

		selectTag.getChildren().add( optionsTag );

		return selectTag;
	}

	private FormSelectTag createFormSelectTag( List<?> values, List<String> labels, Map<String, String> attributes ) {

		// Write the SELECT tag.

		FormSelectTag selectTag = new FormSelectTag();

		String itemLabel = attributes.get( SPRING_LOOKUP_ITEM_LABEL );
		String itemValue = attributes.get( SPRING_LOOKUP_ITEM_VALUE );

		if ( itemLabel != null ) {
			selectTag.putAttribute( "itemLabel", itemLabel );
		}

		if ( itemValue != null ) {
			selectTag.putAttribute( "itemValue", itemValue );
		}

		// Check to see if labels are being used.

		if ( labels != null && !labels.isEmpty() && labels.size() != values.size() ) {
			throw WidgetBuilderException.newException( "Labels list must be same size as values list." );
		}

		// Empty option

		if ( WidgetBuilderUtils.needsEmptyLookupItem( attributes ) ) {
			FormOptionTag emptyOption = new FormOptionTag();
			emptyOption.putAttribute( "value", "" );

			// Add the empty option to the SELECT tag

			selectTag.getChildren().add( emptyOption );
		}

		// Add the options

		for ( int i = 0, length = values.size(); i < length; i++ ) {
			FormOptionTag optionTag = new FormOptionTag();

			optionTag.putAttribute( "value", values.get( i ).toString() );

			if ( labels != null && !labels.isEmpty() ) {

				optionTag.putAttribute( "label", labels.get( i ) );
			}

			// Add the option to the SELECT tag

			selectTag.getChildren().add( optionTag );
		}

		return selectTag;
	}

}
