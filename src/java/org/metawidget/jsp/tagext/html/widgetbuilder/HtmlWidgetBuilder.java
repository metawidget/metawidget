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

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.PageContext;

import org.metawidget.jsp.tagext.StubTag;
import org.metawidget.jsp.tagext.html.BaseHtmlMetawidgetTag;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilderException;
import org.metawidget.widgetbuilder.impl.BaseWidgetBuilder;

/**
 * WidgetBuilder for 'plain' JSP environment (eg. just a servlet-based backend, no Struts/Spring
 * etc) that outputs HTML.
 * <p>
 * Automatically creates native HTML tags, such as <code>&lt;input type="text"&gt;</code> and
 * <code>&lt;select&gt;</code>, to suit the inspected fields.
 * <p>
 * When used in a JSP 2.0 environment, automatically initializes tags using JSP EL.
 *
 * @author Richard Kennard
 */

public class HtmlWidgetBuilder
	extends BaseWidgetBuilder<Object, BaseHtmlMetawidgetTag>
{
	//
	// Protected methods
	//

	@Override
	protected Object buildReadOnlyWidget( String elementName, Map<String, String> attributes, BaseHtmlMetawidgetTag metawidget )
		throws Exception
	{
		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) )
		{
			if ( !metawidget.isCreateHiddenFields() )
				return new StubTag.StubContent();

			if ( TRUE.equals( attributes.get( NO_SETTER ) ) )
				return new StubTag.StubContent();

			return writeHiddenTag( attributes, metawidget );
		}

		// Action (read-only actions ignored)

		if ( ACTION.equals( elementName ) )
			return new StubTag.StubContent();

		// Masked (return an empty String, so that we DO still render a label)

		if ( TRUE.equals( attributes.get( MASKED ) ) )
			return "";

		// Lookups

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) )
			return writeReadOnlyTag( attributes, metawidget );

		String jspLookup = attributes.get( JSP_LOOKUP );

		if ( jspLookup != null && !"".equals( jspLookup ) )
			return writeReadOnlyTag( attributes, metawidget );

		String type = WidgetBuilderUtils.getActualClassOrType( attributes );

		// If no type, assume a String

		if ( type == null )
			type = String.class.getName();

		Class<?> clazz = ClassUtils.niceForName( type );

		if ( clazz != null )
		{
			// Primitives

			if ( clazz.isPrimitive() )
				return writeReadOnlyTag( attributes, metawidget );

			// Object primitives

			if ( ClassUtils.isPrimitiveWrapper( clazz ) )
				return writeReadOnlyTag( attributes, metawidget );

			// Dates

			if ( Date.class.isAssignableFrom( clazz ) )
				return writeReadOnlyTag( attributes, metawidget );

			// Strings

			if ( String.class.equals( clazz ) )
				return writeReadOnlyTag( attributes, metawidget );

			// Collections

			if ( Collection.class.isAssignableFrom( clazz ) )
				return new StubTag.StubContent();
		}

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) )
			return writeReadOnlyTag( attributes, metawidget );

		// Nested Metawidget

		return null;
	}

	@Override
	protected Object buildActiveWidget( String elementName, Map<String, String> attributes, BaseHtmlMetawidgetTag metawidget )
		throws Exception
	{
		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) )
		{
			if ( !metawidget.isCreateHiddenFields() )
				return new StubTag.StubContent();

			if ( TRUE.equals( attributes.get( NO_SETTER ) ) )
				return new StubTag.StubContent();

			return writeHiddenTag( attributes, metawidget );
		}

		// Action

		if ( ACTION.equals( elementName ) )
			return writeSubmitTag( attributes, metawidget );

		String type = WidgetBuilderUtils.getActualClassOrType( attributes );

		// If no type, fail gracefully with a text box

		if ( type == null )
			return writeTextTag( attributes, metawidget );

		// Lookup the Class

		Class<?> clazz = ClassUtils.niceForName( type );

		// Support mandatory Booleans (can be rendered as a checkbox, even though they have a
		// Lookup)

		if ( Boolean.class.equals( clazz ) && TRUE.equals( attributes.get( REQUIRED ) ) )
			return writeCheckboxTag( attributes, metawidget );

		// Lookups

		String jspLookup = attributes.get( JSP_LOOKUP );

		if ( jspLookup != null && !"".equals( jspLookup ) )
			return writeSelectTag( jspLookup, attributes, metawidget );

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) )
			return writeSelectTag( CollectionUtils.fromString( lookup ), CollectionUtils.fromString( attributes.get( LOOKUP_LABELS ) ), attributes, metawidget );

		if ( clazz != null )
		{
			// booleans

			if ( boolean.class.equals( clazz ) )
				return writeCheckboxTag( attributes, metawidget );

			// Primitives

			if ( clazz.isPrimitive() )
				return writeTextTag( attributes, metawidget );

			// String

			if ( String.class.equals( clazz ) )
			{
				if ( TRUE.equals( attributes.get( LARGE ) ) )
				{
					// (use StringBuffer for J2SE 1.4 compatibility)

					StringBuffer buffer = new StringBuffer();
					buffer.append( "<textarea" );
					buffer.append( writeAttributes( attributes, metawidget ) );
					buffer.append( ">" );
					buffer.append( evaluateAsText( attributes, metawidget ) );
					buffer.append( "</textarea>" );

					return buffer.toString();
				}

				if ( TRUE.equals( attributes.get( MASKED ) ) )
					return writeTextTag( "password", attributes, metawidget );

				return writeTextTag( attributes, metawidget );
			}

			// Dates

			if ( Date.class.equals( clazz ) )
				return writeTextTag( attributes, metawidget );

			// Numbers

			if ( Number.class.isAssignableFrom( clazz ) )
				return writeTextTag( attributes, metawidget );

			// Collections

			if ( Collection.class.isAssignableFrom( clazz ) )
				return new StubTag.StubContent();
		}

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) )
			return writeTextTag( attributes, metawidget );

		// Not simple

		return null;
	}

	//
	// Private methods
	//

	private String writeReadOnlyTag( Map<String, String> attributes, BaseHtmlMetawidgetTag metawidget )
		throws Exception
	{
		// (use StringBuffer for J2SE 1.4 compatibility)

		StringBuffer buffer = new StringBuffer();
		String value = evaluateAsText( attributes, metawidget );

		// Support lookup labels

		String lookupLabels = attributes.get( LOOKUP_LABELS );

		if ( lookupLabels != null )
		{
			List<String> lookupList = CollectionUtils.fromString( attributes.get( LOOKUP ) );
			int indexOf = lookupList.indexOf( value );

			if ( indexOf != -1 )
			{
				List<String> lookupLabelsList = CollectionUtils.fromString( lookupLabels );

				if ( indexOf < lookupLabelsList.size() )
					value = lookupLabelsList.get( indexOf );
			}
		}

		buffer.append( value );

		if ( metawidget.isCreateHiddenFields() && !TRUE.equals( attributes.get( NO_SETTER ) ) )
		{
			buffer.append( writeHiddenTag( attributes, metawidget ) );

			// If value is empty, output an &nbsp; to stop this field being treated
			// as 'just a hidden field'

			if ( "".equals( value ) )
				buffer.append( "&nbsp;" );
		}

		return buffer.toString();
	}

	private String writeHiddenTag( Map<String, String> attributes, BaseHtmlMetawidgetTag metawidget )
		throws Exception
	{
		// (use StringBuffer for J2SE 1.4 compatibility)

		StringBuffer buffer = new StringBuffer();
		buffer.append( "<input type=\"hidden\"" );
		buffer.append( writeValueAttribute( attributes, metawidget ) );
		buffer.append( writeAttributes( attributes, metawidget ) );
		buffer.append( ">" );

		return buffer.toString();
	}

	private String writeCheckboxTag( Map<String, String> attributes, BaseHtmlMetawidgetTag metawidget )
		throws Exception
	{
		// (use StringBuffer for J2SE 1.4 compatibility)

		StringBuffer buffer = new StringBuffer();
		buffer.append( "<input type=\"checkbox\"" );
		buffer.append( writeAttributes( attributes, metawidget ) );
		buffer.append( writeCheckedAttribute( attributes, metawidget ) );
		buffer.append( ">" );

		return buffer.toString();
	}

	private String writeTextTag( Map<String, String> attributes, BaseHtmlMetawidgetTag metawidget )
		throws Exception
	{
		return writeTextTag( "text", attributes, metawidget );
	}

	private String writeTextTag( String textTag, Map<String, String> attributes, BaseHtmlMetawidgetTag metawidget )
		throws Exception
	{
		// (use StringBuffer for J2SE 1.4 compatibility)

		StringBuffer buffer = new StringBuffer();

		buffer.append( "<input type=\"" );
		buffer.append( textTag );
		buffer.append( "\"" );
		buffer.append( writeValueAttribute( attributes, metawidget ) );
		buffer.append( writeAttributes( attributes, metawidget ) );

		// Maxlength

		if ( "char".equals( WidgetBuilderUtils.getActualClassOrType( attributes ) ) )
		{
			buffer.append( " maxlength=\"1\"" );
		}
		else
		{
			String maximumLength = attributes.get( MAXIMUM_LENGTH );

			if ( maximumLength != null && !"".equals( maximumLength ))
			{
				buffer.append( " maxlength=\"" );
				buffer.append( maximumLength );
				buffer.append( "\"" );
			}
		}

		buffer.append( ">" );

		return buffer.toString();
	}

	private String writeSubmitTag( Map<String, String> attributes, BaseHtmlMetawidgetTag metawidget )
		throws Exception
	{
		// (use StringBuffer for J2SE 1.4 compatibility)

		StringBuffer buffer = new StringBuffer();

		buffer.append( "<input type=\"submit\" value=\"" );
		buffer.append( metawidget.getLabelString( attributes ) );
		buffer.append( "\"" );
		buffer.append( writeAttributes( attributes, metawidget ) );
		buffer.append( ">" );

		return buffer.toString();
	}

	private String writeValueAttribute( Map<String, String> attributes, BaseHtmlMetawidgetTag metawidget )
		throws Exception
	{
		String result = evaluateAsText( attributes, metawidget );

		if ( result == null || "".equals( result ) )
			return "";

		// (use StringBuffer for J2SE 1.4 compatibility)

		StringBuffer buffer = new StringBuffer();

		buffer.append( " value=\"" );
		buffer.append( result );
		buffer.append( "\"" );

		return buffer.toString();
	}

	private String writeCheckedAttribute( Map<String, String> attributes, BaseHtmlMetawidgetTag metawidget )
		throws Exception
	{
		Object result = evaluate( attributes, metawidget );

		if ( result != null && true == (Boolean) result )
			return " checked";

		return "";
	}

	/**
	 * Initialize the HTML tag with various attributes, CSS settings etc.
	 * <p>
	 * In other Metawidgets, this step is done after the widget has been built. However, because JSP
	 * lacks a 'true' component model (eg. buildActiveWidget returns a String) we must do it here.
	 */

	private String writeAttributes( Map<String, String> attributes, BaseHtmlMetawidgetTag metawidget )
	{
		// (use StringBuffer for J2SE 1.4 compatibility)

		StringBuffer buffer = new StringBuffer();

		buffer.append( " name=\"" );

		String name = attributes.get( NAME );

		if ( metawidget.getPathPrefix() != null )
			name = metawidget.getPathPrefix() + name;

		buffer.append( name );
		buffer.append( "\"" );

		// CSS

		if ( metawidget.getStyle() != null )
		{
			buffer.append( " style=\"" );
			buffer.append( metawidget.getStyle() );
			buffer.append( "\"" );
		}

		if ( metawidget.getStyleClass() != null )
		{
			buffer.append( " class=\"" );
			buffer.append( metawidget.getStyleClass() );
			buffer.append( "\"" );
		}

		return buffer.toString();
	}

	@SuppressWarnings( "unchecked" )
	private String writeSelectTag( final String expression, final Map<String, String> attributes, BaseHtmlMetawidgetTag metawidget )
		throws Exception
	{
		Object collection = evaluate( expression, metawidget );

		if ( collection == null )
			return null;

		if ( collection instanceof Collection && !( collection instanceof List ) )
			collection = CollectionUtils.newArrayList( (Collection) collection );

		else if ( collection.getClass().isArray() )
			collection = CollectionUtils.newArrayList( (Object[]) collection );

		return writeSelectTag( (List<?>) collection, null, attributes, metawidget );
	}

	private String writeSelectTag( final List<?> values, final List<String> labels, Map<String, String> attributes, BaseHtmlMetawidgetTag metawidget )
		throws Exception
	{
		// See if we're using labels

		if ( labels != null && !labels.isEmpty() && labels.size() != values.size() )
			throw WidgetBuilderException.newException( "Labels list must be same size as values list" );

		// (use StringBuffer for J2SE 1.4 compatibility)

		StringBuffer buffer = new StringBuffer();

		// Start the SELECT tag

		buffer.append( "<select" );
		buffer.append( writeAttributes( attributes, metawidget ) );

		buffer.append( ">" );

		// Empty option

		if ( WidgetBuilderUtils.needsEmptyLookupItem( attributes ))
			buffer.append( "<option value=\"\"></option>" );

		// Evaluate the expression

		String selected = evaluateAsText( attributes, metawidget );

		// Add the options

		for ( int loop = 0, length = values.size(); loop < length; loop++ )
		{
			Object value = values.get( loop );

			if ( value == null )
				continue;

			String stringValue = StringUtils.quietValueOf( value );

			buffer.append( "<option value=\"" );
			buffer.append( stringValue );
			buffer.append( "\"" );

			if ( stringValue.equals( selected ) )
				buffer.append( " selected" );

			buffer.append( ">" );

			if ( labels == null || labels.isEmpty() )
				buffer.append( stringValue );
			else
				buffer.append( labels.get( loop ) );

			buffer.append( "</option>" );
		}

		// End the SELECT tag

		buffer.append( "</select>" );

		return buffer.toString();
	}

	/**
	 * Evaluate to text (via a PropertyEditor if available).
	 */

	private String evaluateAsText( Map<String, String> attributes, BaseHtmlMetawidgetTag metawidget )
		throws Exception
	{
		Object evaluated = evaluate( attributes, metawidget );

		if ( evaluated == null )
			return "";

		Class<?> clazz = evaluated.getClass();

		while ( clazz != null )
		{
			PropertyEditor editor = PropertyEditorManager.findEditor( clazz );

			if ( editor != null )
			{
				editor.setValue( evaluated );
				return editor.getAsText();
			}

			clazz = clazz.getSuperclass();
		}

		return StringUtils.quietValueOf( evaluated );
	}

	private Object evaluate( Map<String, String> attributes, BaseHtmlMetawidgetTag metawidget )
		throws Exception
	{
		if ( metawidget.getPathPrefix() == null )
			return null;

		return evaluate( "${" + metawidget.getPathPrefix() + attributes.get( NAME ) + "}", metawidget );
	}

	private Object evaluate( String expression, BaseHtmlMetawidgetTag metawidget )
		throws Exception
	{
		try
		{
			PageContext context = metawidget.getPageContext();
			return context.getExpressionEvaluator().evaluate( expression, Object.class, context.getVariableResolver(), null );
		}
		catch ( Throwable t )
		{
			// EL should fail gracefully
			//
			// Note: pageContext.getExpressionEvaluator() is only available with JSP 2.0

			return null;
		}
	}
}
