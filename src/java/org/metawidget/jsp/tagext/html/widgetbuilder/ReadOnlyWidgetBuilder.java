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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import org.metawidget.jsp.JspUtils;
import org.metawidget.jsp.tagext.LiteralTag;
import org.metawidget.jsp.tagext.MetawidgetTag;
import org.metawidget.jsp.tagext.html.BaseHtmlMetawidgetTag;
import org.metawidget.jsp.tagext.html.HtmlStubTag;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.metawidget.widgetbuilder.iface.WidgetBuilderException;

/**
 * ReadOnlyWidgetBuilder for 'plain' JSP environment (eg. just a servlet-based backend, no Struts/Spring
 * etc) that outputs HTML.
 * <p>
 * Creates native HTML tags, such as <code>&lt;input type="text"&gt;</code> and
 * <code>&lt;select&gt;</code>, to suit the inspected fields.
 * <p>
 * When used in a JSP 2.0 environment, automatically initializes tags using JSP EL.
 *
 * @author Richard Kennard
 */

public class ReadOnlyWidgetBuilder
	implements WidgetBuilder<Tag, MetawidgetTag>
{
	//
	// Public methods
	//

	public Tag buildWidget( String elementName, Map<String, String> attributes, MetawidgetTag metawidget )
	{
		// Not read-only?

		if ( !WidgetBuilderUtils.isReadOnly( attributes ))
			return null;

		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) )
			return null;

		// Action (read-only actions ignored)

		if ( ACTION.equals( elementName ) )
			return new HtmlStubTag();

		// Masked (return an empty String, so that we DO still render a label)

		if ( TRUE.equals( attributes.get( MASKED ) ) )
			return new LiteralTag( "" );

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
				return new HtmlStubTag();
		}

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) )
			return writeReadOnlyTag( attributes, metawidget );

		// Nested Metawidget

		return null;
	}
	//
	// Private methods
	//

	private Tag writeReadOnlyTag( Map<String, String> attributes, MetawidgetTag metawidget )
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

		if ( ( (BaseHtmlMetawidgetTag) metawidget ).isCreateHiddenFields() && !TRUE.equals( attributes.get( NO_SETTER ) ) )
		{
			Tag hiddenTag = writeHiddenTag( attributes, metawidget );

			try
			{
				buffer.append( JspUtils.writeTag( metawidget.getPageContext(), hiddenTag, metawidget, null ) );
			}
			catch( JspException e )
			{
				throw WidgetBuilderException.newException( e );
			}

			// If value is empty, output an &nbsp; to stop this field being treated
			// as 'just a hidden field'

			if ( "".equals( value ) )
				buffer.append( "&nbsp;" );
		}

		return new LiteralTag( buffer.toString() );
	}

	private Tag writeHiddenTag( Map<String, String> attributes, MetawidgetTag metawidget )
	{
		// (use StringBuffer for J2SE 1.4 compatibility)

		StringBuffer buffer = new StringBuffer();
		buffer.append( "<input type=\"hidden\"" );
		buffer.append( writeValueAttribute( attributes, metawidget ) );
		buffer.append( writeAttributes( attributes, metawidget ) );
		buffer.append( "/>" );

		return new LiteralTag( buffer.toString() );
	}

	private String writeValueAttribute( Map<String, String> attributes, MetawidgetTag metawidget )
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

	/**
	 * Initialize the HTML tag with various attributes, CSS settings etc.
	 * <p>
	 * In other Metawidgets, this step is done after the widget has been built. However, because JSP
	 * lacks a 'true' component model (eg. buildActiveWidget returns a String) we must do it here.
	 */

	private String writeAttributes( Map<String, String> attributes, MetawidgetTag metawidget )
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

		BaseHtmlMetawidgetTag htmlMetawidgetTag = (BaseHtmlMetawidgetTag) metawidget;

		if ( htmlMetawidgetTag.getStyle() != null )
		{
			buffer.append( " style=\"" );
			buffer.append( htmlMetawidgetTag.getStyle() );
			buffer.append( "\"" );
		}

		if ( htmlMetawidgetTag.getStyleClass() != null )
		{
			buffer.append( " class=\"" );
			buffer.append( htmlMetawidgetTag.getStyleClass() );
			buffer.append( "\"" );
		}

		return buffer.toString();
	}

	/**
	 * Evaluate to text (via a PropertyEditor if available).
	 */

	private String evaluateAsText( Map<String, String> attributes, MetawidgetTag metawidget )
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

	private Object evaluate( Map<String, String> attributes, MetawidgetTag metawidget )
	{
		if ( metawidget.getPathPrefix() == null )
			return null;

		return evaluate( "${" + metawidget.getPathPrefix() + attributes.get( NAME ) + "}", metawidget );
	}

	private Object evaluate( String expression, MetawidgetTag metawidget )
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
