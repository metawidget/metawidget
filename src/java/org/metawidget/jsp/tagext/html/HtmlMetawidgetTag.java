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

package org.metawidget.jsp.tagext.html;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.propertytype.PropertyTypeInspectionResultConstants.*;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.jstl.fmt.LocalizationContext;

import org.metawidget.MetawidgetException;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;
import org.w3c.dom.Element;

/**
 * Metawidget for 'plain' JSP environment (eg. just a servlet-based backend, no Struts/Spring etc)
 * that outputs HTML.
 * <p>
 * Automatically creates native HTML tags, such as <code>&lt;input type="text"&gt;</code> and
 * <code>&lt;select&gt;</code>, to suit the inspected fields.
 * <p>
 * When used in a JSP 2.0 environment, automatically initializes tags using JSP EL.
 *
 * @author Richard Kennard
 */

public class HtmlMetawidgetTag
	extends BaseHtmlMetawidgetTag
{
	//
	//
	// Private statics
	//
	//

	private final static long			serialVersionUID	= 2764949384474589158L;

	private final static List<Boolean>	LIST_BOOLEAN_VALUES	= CollectionUtils.unmodifiableList( Boolean.TRUE, Boolean.FALSE );

	//
	//
	// Private members
	//
	//

	private String						mNamePrefix;

	//
	//
	// Public methods
	//
	//

	/**
	 * Sets the path of the object to inspect.
	 * <p>
	 * The value is set as a path (eg. <code>customerSearch</code>) rather than the object itself
	 * (eg. <code>${customerSearch}</code>) so that we can 'inspect from parent' if needed.
	 */

	public void setValue( String value )
	{
		mPath = value;

		// Take the whole LHS of the path as the prefix, so that names are unique

		if ( value != null )
		{
			int lastIndexOf = value.lastIndexOf( StringUtils.SEPARATOR_DOT_CHAR );

			if ( lastIndexOf != -1 )
				mNamePrefix = value.substring( 0, lastIndexOf + 1 );
		}
	}

	/**
	 * Sets the LocalizationContext used to localize labels.
	 */

	public void setBundle( LocalizationContext bundle )
	{
		super.setBundle( bundle.getResourceBundle() );
	}

	@Override
	public void release()
	{
		super.release();

		// Clear the prefix, in case we're reused

		mNamePrefix = null;
	}

	//
	//
	// Protected methods
	//
	//

	@Override
	protected String buildReadOnlyWidget( String elementName, Map<String, String> attributes )
		throws Exception
	{
		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) )
		{
			if ( !mCreateHiddenFields || TRUE.equals( attributes.get( NO_SETTER ) ) )
				return null;

			return writeHiddenTag( attributes );
		}

		// Action

		if ( ACTION.equals( elementName ) )
			return null;

		// Masked (return an empty String, so that we DO still render a label)

		if ( TRUE.equals( attributes.get( MASKED ) ) )
			return "";

		// Lookups

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) )
			return writeReadOnlyTag( attributes );

		String type = attributes.get( TYPE );

		// If no type, fail gracefully

		if ( type == null || type.length() == 0 )
			return writeReadOnlyTag( attributes );

		Class<?> clazz = ClassUtils.niceForName( type );

		if ( clazz != null )
		{
			// Primitives

			if ( clazz.isPrimitive() )
				return writeReadOnlyTag( attributes );

			// Object primitives

			if ( ClassUtils.isPrimitiveWrapper( clazz ) )
				return writeReadOnlyTag( attributes );

			// Dates

			if ( Date.class.isAssignableFrom( clazz ) )
				return writeReadOnlyTag( attributes );

			// Strings

			if ( String.class.equals( clazz ) )
				return writeReadOnlyTag( attributes );

			// Collections

			if ( Collection.class.isAssignableFrom( clazz ) )
				return null;
		}

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) )
			return writeReadOnlyTag( attributes );

		// Nested Metawidget

		return METAWIDGET;
	}

	@Override
	protected String buildActiveWidget( String elementName, Map<String, String> attributes )
		throws Exception
	{
		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) )
		{
			if ( !mCreateHiddenFields || TRUE.equals( attributes.get( NO_SETTER ) ) )
				return null;

			return writeHiddenTag( attributes );
		}

		// Action

		if ( ACTION.equals( elementName ) )
			return writeSubmitTag( attributes );

		// String Lookups

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) )
			return writeSelectTag( CollectionUtils.fromString( lookup ), CollectionUtils.fromString( attributes.get( LOOKUP_LABELS ) ), attributes );

		String type = attributes.get( TYPE );

		// If no type, fail gracefully with a text box

		if ( type == null || "".equals( type ) )
			return writeTextTag( attributes );

		Class<?> clazz = ClassUtils.niceForName( type );

		if ( clazz != null )
		{
			// Primitives

			if ( clazz.isPrimitive() )
			{
				if ( boolean.class.equals( clazz ) )
				{
					// (use StringBuffer for J2SE 1.4 compatibility)

					StringBuffer buffer = new StringBuffer();
					buffer.append( "<input type=\"checkbox\"" );
					buffer.append( writeAttributes( attributes ) );
					buffer.append( writeCheckedAttribute( attributes ) );
					buffer.append( ">" );

					return buffer.toString();
				}

				return writeTextTag( attributes );
			}

			// String

			if ( String.class.equals( clazz ) )
			{
				if ( TRUE.equals( attributes.get( LARGE ) ) )
				{
					// (use StringBuffer for J2SE 1.4 compatibility)

					StringBuffer buffer = new StringBuffer();
					buffer.append( "<textarea" );
					buffer.append( writeAttributes( attributes ) );
					buffer.append( ">" );
					buffer.append( evaluateAsText( attributes ) );
					buffer.append( "</textarea>" );

					return buffer.toString();
				}

				if ( TRUE.equals( attributes.get( MASKED ) ) )
					return writeTextTag( "password", attributes );

				return writeTextTag( attributes );
			}

			// Booleans (are tri-state)

			if ( Boolean.class.equals( clazz ) )
				return writeSelectTag( LIST_BOOLEAN_VALUES, null, attributes );

			// Dates

			if ( Date.class.equals( clazz ) )
				return writeTextTag( attributes );

			// Numbers

			if ( Number.class.isAssignableFrom( clazz ) )
				return writeTextTag( attributes );

			// Collections

			if ( Collection.class.isAssignableFrom( clazz ) )
				return null;
		}

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) )
			return writeTextTag( attributes );

		// Not simple

		return METAWIDGET;
	}

	@Override
	protected void beforeBuildCompoundWidget( Element element )
	{
		// Take the whole path as the name prefix, so that names are unique

		mNamePrefix = mPath + StringUtils.SEPARATOR_DOT_CHAR;
	}

	//
	//
	// Private methods
	//
	//

	private String writeReadOnlyTag( Map<String, String> attributes )
		throws Exception
	{
		// (use StringBuffer for J2SE 1.4 compatibility)

		StringBuffer buffer = new StringBuffer();
		String value = evaluateAsText( attributes );
		buffer.append( value );

		if ( mCreateHiddenFields && !TRUE.equals( attributes.get( NO_SETTER ) ) )
		{
			buffer.append( writeHiddenTag( attributes ) );

			// If value is empty, output an &nbsp; to stop this field being treated
			// as 'just a hidden field'

			if ( "".equals( value ) )
				buffer.append( "&nbsp;" );
		}

		return buffer.toString();
	}

	private String writeHiddenTag( Map<String, String> attributes )
		throws Exception
	{
		// (use StringBuffer for J2SE 1.4 compatibility)

		StringBuffer buffer = new StringBuffer();
		buffer.append( "<input type=\"hidden\"" );
		buffer.append( writeValueAttribute( attributes ) );
		buffer.append( writeAttributes( attributes ) );
		buffer.append( ">" );

		return buffer.toString();
	}

	private String writeTextTag( Map<String, String> attributes )
		throws Exception
	{
		return writeTextTag( "text", attributes );
	}

	private String writeTextTag( String textTag, Map<String, String> attributes )
		throws Exception
	{
		// (use StringBuffer for J2SE 1.4 compatibility)

		StringBuffer buffer = new StringBuffer();

		buffer.append( "<input type=\"" );
		buffer.append( textTag );
		buffer.append( "\"" );
		buffer.append( writeValueAttribute( attributes ) );
		buffer.append( writeAttributes( attributes ) );

		// Maxlength

		if ( "char".equals( attributes.get( TYPE ) ) )
		{
			buffer.append( " maxlength=\"1\"" );
		}
		else
		{
			String maximumLength = attributes.get( MAXIMUM_LENGTH );

			if ( maximumLength != null )
			{
				buffer.append( " maxlength=\"" );
				buffer.append( maximumLength );
				buffer.append( "\"" );
			}
		}

		buffer.append( ">" );

		return buffer.toString();
	}

	private String writeSubmitTag( Map<String, String> attributes )
		throws Exception
	{
		// (use StringBuffer for J2SE 1.4 compatibility)

		StringBuffer buffer = new StringBuffer();

		buffer.append( "<input type=\"submit\" value=\"" );
		buffer.append( getLabelString( attributes ));
		buffer.append( "\"" );
		buffer.append( writeAttributes( attributes ) );
		buffer.append( ">" );

		return buffer.toString();
	}

	private String writeValueAttribute( Map<String, String> attributes )
		throws Exception
	{
		String result = evaluateAsText( attributes );

		if ( result == null || "".equals( result ) )
			return "";

		// (use StringBuffer for J2SE 1.4 compatibility)

		StringBuffer buffer = new StringBuffer();

		buffer.append( " value=\"" );
		buffer.append( result );
		buffer.append( "\"" );

		return buffer.toString();
	}

	private String writeCheckedAttribute( Map<String, String> attributes )
		throws Exception
	{
		Object result = evaluate( attributes );

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

	private String writeAttributes( Map<String, String> attributes )
	{
		// (use StringBuffer for J2SE 1.4 compatibility)

		StringBuffer buffer = new StringBuffer();

		buffer.append( " name=\"" );

		String name = attributes.get( NAME );

		if ( mNamePrefix != null )
		{
			name = mNamePrefix + name;
		}

		buffer.append( name );
		buffer.append( "\"" );

		// CSS

		if ( mStyle != null )
		{
			buffer.append( " style=\"" );
			buffer.append( mStyle );
			buffer.append( "\"" );
		}

		if ( mStyleClass != null )
		{
			buffer.append( " class=\"" );
			buffer.append( mStyleClass );
			buffer.append( "\"" );
		}

		return buffer.toString();
	}

	private String writeSelectTag( final List<?> values, final List<String> labels, Map<String, String> attributes )
		throws Exception
	{
		// See if we're using labels

		if ( labels != null && !labels.isEmpty() && labels.size() != values.size() )
			throw MetawidgetException.newException( "Labels list must be same size as values list" );

		// (use StringBuffer for J2SE 1.4 compatibility)

		StringBuffer buffer = new StringBuffer();

		// Start the SELECT tag

		buffer.append( "<select" );
		buffer.append( writeAttributes( attributes ) );

		buffer.append( ">" );

		// Add an empty choice (if nullable, and not required)

		String type = attributes.get( TYPE );

		if ( type == null )
		{
			// Type can be null if this lookup was specified by a metawidget-metadata.xml
			// and the type was omitted from the XML. In that case, assume nullable

			buffer.append( "<option value=\"\"></option>" );
		}
		else
		{
			Class<?> clazz = ClassUtils.niceForName( type );

			if ( clazz == null || ( !clazz.isPrimitive() && !TRUE.equals( attributes.get( REQUIRED ) ) ) )
				buffer.append( "<option value=\"\"></option>" );
		}

		// Evaluate the expression

		String selected = evaluateAsText( attributes );

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

	private String evaluateAsText( Map<String, String> attributes )
		throws Exception
	{
		Object evaluated = evaluate( attributes );

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

	private Object evaluate( Map<String, String> attributes )
		throws Exception
	{
		if ( mNamePrefix == null )
			return null;

		return evaluate( "${" + mNamePrefix + attributes.get( NAME ) + "}" );
	}

	private Object evaluate( String expression )
		throws Exception
	{
		try
		{
			return pageContext.getExpressionEvaluator().evaluate( expression, Object.class, pageContext.getVariableResolver(), null );
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
