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

package org.metawidget.jsp.tagext.html.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.jsp.JspInspectionResultConstants.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.tagext.Tag;

import org.metawidget.jsp.tagext.LiteralTag;
import org.metawidget.jsp.tagext.MetawidgetTag;
import org.metawidget.jsp.tagext.html.HtmlStubTag;
import org.metawidget.jsp.tagext.html.widgetprocessor.HiddenFieldProcessor;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.metawidget.widgetbuilder.iface.WidgetBuilderException;

/**
 * WidgetBuilder for 'plain' JSP environment (eg. just a servlet-based backend, no Struts/Spring
 * etc) that outputs HTML.
 * <p>
 * Creates native HTML tags, such as <code>&lt;input type="text"&gt;</code> and
 * <code>&lt;select&gt;</code>, to suit the inspected fields.
 * <p>
 * When used in a JSP 2.0 environment, automatically initializes tags using JSP EL.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class HtmlWidgetBuilder
	implements WidgetBuilder<Tag, MetawidgetTag> {

	//
	// Public methods
	//

	public Tag buildWidget( String elementName, Map<String, String> attributes, MetawidgetTag metawidget ) {

		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) ) {
			attributes.put( HiddenFieldProcessor.ATTRIBUTE_NEEDS_HIDDEN_FIELD, TRUE );
			return new HtmlStubTag();
		}

		// Action

		if ( ACTION.equals( elementName ) ) {
			return createSubmitTag( attributes, metawidget );
		}

		// Lookup the class

		Class<?> clazz = WidgetBuilderUtils.getActualClassOrType( attributes, String.class );

		// Support mandatory Booleans (can be rendered as a checkbox, even though they have a
		// Lookup)

		if ( Boolean.class.equals( clazz ) && TRUE.equals( attributes.get( REQUIRED ) ) ) {
			return createCheckboxTag( attributes, metawidget );
		}

		// Lookups

		String jspLookup = attributes.get( JSP_LOOKUP );

		if ( jspLookup != null && !"".equals( jspLookup ) ) {
			return createSelectTag( jspLookup, attributes, metawidget );
		}

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) ) {
			return createSelectTag( CollectionUtils.fromString( lookup ), CollectionUtils.fromString( attributes.get( LOOKUP_LABELS ) ), attributes, metawidget );
		}

		if ( clazz != null ) {
			// booleans

			if ( boolean.class.equals( clazz ) ) {
				return createCheckboxTag( attributes, metawidget );
			}

			// chars

			if ( char.class.equals( clazz ) ) {
				return createTextTag( attributes, metawidget );
			}

			// Other primitives

			if ( clazz.isPrimitive() ) {
				return createTextTag( "number", attributes, metawidget );
			}

			// String

			if ( String.class.equals( clazz ) ) {
				if ( TRUE.equals( attributes.get( LARGE ) ) ) {

					StringBuilder builder = new StringBuilder();
					builder.append( "<textarea" );
					builder.append( HtmlWidgetBuilderUtils.writeAttributes( attributes, metawidget ) );
					builder.append( ">" );
					builder.append( HtmlWidgetBuilderUtils.evaluateAsText( attributes, metawidget ) );
					builder.append( "</textarea>" );

					return new LiteralTag( builder.toString() );
				}

				if ( TRUE.equals( attributes.get( MASKED ) ) ) {
					return createTextTag( "password", attributes, metawidget );
				}

				return createTextTag( attributes, metawidget );
			}

			// Character

			if ( Character.class.equals( clazz ) ) {
				return createTextTag( attributes, metawidget );
			}

			// Dates

			if ( Date.class.equals( clazz ) ) {
				return createTextTag( attributes, metawidget );
			}

			// Numbers

			if ( Number.class.isAssignableFrom( clazz ) ) {
				return createTextTag( "number", attributes, metawidget );
			}

			// Collections

			if ( Collection.class.isAssignableFrom( clazz ) ) {
				return new HtmlStubTag();
			}
		}

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) ) {
			return createTextTag( attributes, metawidget );
		}

		// Not simple

		return null;
	}

	//
	// Private methods
	//

	private Tag createCheckboxTag( Map<String, String> attributes, MetawidgetTag metawidget ) {

		StringBuilder builder = new StringBuilder();
		builder.append( "<input type=\"checkbox\"" );
		builder.append( HtmlWidgetBuilderUtils.writeAttributes( attributes, metawidget ) );
		builder.append( writeCheckedAttribute( attributes, metawidget ) );
		builder.append( "/>" );

		return new LiteralTag( builder.toString() );
	}

	private Tag createTextTag( Map<String, String> attributes, MetawidgetTag metawidget ) {

		return createTextTag( "text", attributes, metawidget );
	}

	private Tag createTextTag( String textTag, Map<String, String> attributes, MetawidgetTag metawidget ) {

		StringBuilder builder = new StringBuilder();

		builder.append( "<input type=\"" );
		builder.append( textTag );
		builder.append( "\"" );
		builder.append( HtmlWidgetBuilderUtils.writeValueAttribute( attributes, metawidget ) );
		builder.append( HtmlWidgetBuilderUtils.writeAttributes( attributes, metawidget ) );

		// Maxlength

		Class<?> clazz = WidgetBuilderUtils.getActualClassOrType( attributes, null );

		if ( char.class.equals( clazz ) || Character.class.equals( clazz )) {
			builder.append( " maxlength=\"1\"" );
		} else {
			String maximumLength = attributes.get( MAXIMUM_LENGTH );

			if ( maximumLength != null && !"".equals( maximumLength ) ) {
				builder.append( " maxlength=\"" );
				builder.append( maximumLength );
				builder.append( "\"" );
			}
		}

		// minimumValue and maximumValue
		
		String minimumValue = attributes.get( MINIMUM_VALUE );

		if ( minimumValue != null && !"".equals( minimumValue ) ) {
			builder.append( " min=\"" );
			builder.append( minimumValue );
			builder.append( "\"" );
		}

		String maximumValue = attributes.get( MAXIMUM_VALUE );

		if ( maximumValue != null && !"".equals( maximumValue ) ) {
			builder.append( " max=\"" );
			builder.append( maximumValue );
			builder.append( "\"" );
		}

		// required

		if ( TRUE.equals( attributes.get( REQUIRED ))) {
			builder.append( " required" );
		}

		builder.append( "/>" );

		return new LiteralTag( builder.toString() );
	}

	private Tag createSubmitTag( Map<String, String> attributes, MetawidgetTag metawidget ) {

		StringBuilder builder = new StringBuilder();

		builder.append( "<input type=\"submit\" value=\"" );
		builder.append( metawidget.getLabelString( attributes ) );
		builder.append( "\"" );
		builder.append( HtmlWidgetBuilderUtils.writeAttributes( attributes, metawidget ) );
		builder.append( "/>" );

		return new LiteralTag( builder.toString() );
	}

	private String writeCheckedAttribute( Map<String, String> attributes, MetawidgetTag metawidget ) {
		
		Boolean result = (Boolean) HtmlWidgetBuilderUtils.evaluate( attributes, metawidget );

		if ( result != null && result.booleanValue() ) {
			return " checked";
		}

		return "";
	}

	private Tag createSelectTag( final String expression, final Map<String, String> attributes, MetawidgetTag metawidget ) {

		Object collection = HtmlWidgetBuilderUtils.evaluate( expression, metawidget );

		if ( collection == null ) {
			return null;
		}

		if ( collection instanceof Collection && !( collection instanceof List ) ) {
			collection = CollectionUtils.newArrayList( (Collection<?>) collection );
		} else if ( collection.getClass().isArray() ) {
			collection = CollectionUtils.newArrayList( (Object[]) collection );
		}

		return createSelectTag( (List<?>) collection, null, attributes, metawidget );
	}

	private Tag createSelectTag( final List<?> values, final List<String> labels, Map<String, String> attributes, MetawidgetTag metawidget ) {

		// See if we're using labels

		if ( labels != null && !labels.isEmpty() && labels.size() != values.size() ) {
			throw WidgetBuilderException.newException( "Labels list must be same size as values list" );
		}

		StringBuilder builder = new StringBuilder();

		// Start the SELECT tag

		builder.append( "<select" );
		builder.append( HtmlWidgetBuilderUtils.writeAttributes( attributes, metawidget ) );

		builder.append( ">" );

		// Empty option

		if ( WidgetBuilderUtils.needsEmptyLookupItem( attributes ) ) {
			builder.append( "<option value=\"\"></option>" );
		}

		// Evaluate the expression

		String selected = HtmlWidgetBuilderUtils.evaluateAsText( attributes, metawidget );

		// Add the options

		for ( int loop = 0, length = values.size(); loop < length; loop++ ) {
			Object value = values.get( loop );

			if ( value == null ) {
				continue;
			}

			String stringValue = StringUtils.quietValueOf( value );

			builder.append( "<option value=\"" );
			builder.append( stringValue );
			builder.append( "\"" );

			if ( stringValue.equals( selected ) ) {
				builder.append( " selected" );
			}

			builder.append( ">" );

			if ( labels == null || labels.isEmpty() ) {
				builder.append( stringValue );
			} else {
				builder.append( labels.get( loop ) );
			}

			builder.append( "</option>" );
		}

		// End the SELECT tag

		builder.append( "</select>" );

		return new LiteralTag( builder.toString() );
	}
}
