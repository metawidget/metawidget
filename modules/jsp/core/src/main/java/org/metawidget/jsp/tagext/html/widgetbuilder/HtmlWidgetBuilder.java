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
import org.metawidget.util.ClassUtils;
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
 * @author Richard Kennard
 */

public class HtmlWidgetBuilder
	implements WidgetBuilder<Tag, MetawidgetTag> {

	//
	// Public methods
	//

	public Tag buildWidget( String elementName, Map<String, String> attributes, MetawidgetTag metawidget ) {

		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) ) {
			attributes.put( MetawidgetTag.ATTRIBUTE_NEEDS_HIDDEN_FIELD, TRUE );
			return new HtmlStubTag();
		}

		// Action

		if ( ACTION.equals( elementName ) ) {
			return createSubmitTag( attributes, metawidget );
		}

		String type = WidgetBuilderUtils.getActualClassOrType( attributes );

		// If no type, fail gracefully with a text box

		if ( type == null ) {
			return createTextTag( attributes, metawidget );
		}

		// Lookup the Class

		Class<?> clazz = ClassUtils.niceForName( type );

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

			// Primitives

			if ( clazz.isPrimitive() ) {
				return createTextTag( attributes, metawidget );
			}

			// String

			if ( String.class.equals( clazz ) ) {
				if ( TRUE.equals( attributes.get( LARGE ) ) ) {
					// (use StringBuffer for J2SE 1.4 compatibility)

					StringBuffer buffer = new StringBuffer();
					buffer.append( "<textarea" );
					buffer.append( HtmlWidgetBuilderUtils.writeAttributes( attributes, metawidget ) );
					buffer.append( ">" );
					buffer.append( HtmlWidgetBuilderUtils.evaluateAsText( attributes, metawidget ) );
					buffer.append( "</textarea>" );

					return new LiteralTag( buffer.toString() );
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
				return createTextTag( attributes, metawidget );
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

		// (use StringBuffer for J2SE 1.4 compatibility)

		StringBuffer buffer = new StringBuffer();
		buffer.append( "<input type=\"checkbox\"" );
		buffer.append( HtmlWidgetBuilderUtils.writeAttributes( attributes, metawidget ) );
		buffer.append( writeCheckedAttribute( attributes, metawidget ) );
		buffer.append( "/>" );

		return new LiteralTag( buffer.toString() );
	}

	private Tag createTextTag( Map<String, String> attributes, MetawidgetTag metawidget ) {

		return createTextTag( "text", attributes, metawidget );
	}

	private Tag createTextTag( String textTag, Map<String, String> attributes, MetawidgetTag metawidget ) {

		// (use StringBuffer for J2SE 1.4 compatibility)

		StringBuffer buffer = new StringBuffer();

		buffer.append( "<input type=\"" );
		buffer.append( textTag );
		buffer.append( "\"" );
		buffer.append( HtmlWidgetBuilderUtils.writeValueAttribute( attributes, metawidget ) );
		buffer.append( HtmlWidgetBuilderUtils.writeAttributes( attributes, metawidget ) );

		// Maxlength

		String actualType = WidgetBuilderUtils.getActualClassOrType( attributes );

		if ( "char".equals( actualType ) || Character.class.getName().equals( actualType )) {
			buffer.append( " maxlength=\"1\"" );
		} else {
			String maximumLength = attributes.get( MAXIMUM_LENGTH );

			if ( maximumLength != null && !"".equals( maximumLength ) ) {
				buffer.append( " maxlength=\"" );
				buffer.append( maximumLength );
				buffer.append( "\"" );
			}
		}

		buffer.append( "/>" );

		return new LiteralTag( buffer.toString() );
	}

	private Tag createSubmitTag( Map<String, String> attributes, MetawidgetTag metawidget ) {

		// (use StringBuffer for J2SE 1.4 compatibility)

		StringBuffer buffer = new StringBuffer();

		buffer.append( "<input type=\"submit\" value=\"" );
		buffer.append( metawidget.getLabelString( attributes ) );
		buffer.append( "\"" );
		buffer.append( HtmlWidgetBuilderUtils.writeAttributes( attributes, metawidget ) );
		buffer.append( "/>" );

		return new LiteralTag( buffer.toString() );
	}

	private String writeCheckedAttribute( Map<String, String> attributes, MetawidgetTag metawidget ) {

		Object result = HtmlWidgetBuilderUtils.evaluate( attributes, metawidget );

		if ( result != null && true == (Boolean) result ) {
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

		// (use StringBuffer for J2SE 1.4 compatibility)

		StringBuffer buffer = new StringBuffer();

		// Start the SELECT tag

		buffer.append( "<select" );
		buffer.append( HtmlWidgetBuilderUtils.writeAttributes( attributes, metawidget ) );

		buffer.append( ">" );

		// Empty option

		if ( WidgetBuilderUtils.needsEmptyLookupItem( attributes ) ) {
			buffer.append( "<option value=\"\"></option>" );
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

			buffer.append( "<option value=\"" );
			buffer.append( stringValue );
			buffer.append( "\"" );

			if ( stringValue.equals( selected ) ) {
				buffer.append( " selected" );
			}

			buffer.append( ">" );

			if ( labels == null || labels.isEmpty() ) {
				buffer.append( stringValue );
			} else {
				buffer.append( labels.get( loop ) );
			}

			buffer.append( "</option>" );
		}

		// End the SELECT tag

		buffer.append( "</select>" );

		return new LiteralTag( buffer.toString() );
	}
}
