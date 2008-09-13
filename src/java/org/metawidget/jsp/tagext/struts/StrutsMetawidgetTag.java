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

package org.metawidget.jsp.tagext.struts;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.propertytype.PropertyTypeInspectionResultConstants.*;
import static org.metawidget.inspector.struts.StrutsInspectionResultConstants.*;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.Tag;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionServletWrapper;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.taglib.html.BaseHandlerTag;
import org.apache.struts.taglib.html.BaseInputTag;
import org.apache.struts.taglib.html.CheckboxTag;
import org.apache.struts.taglib.html.HiddenTag;
import org.apache.struts.taglib.html.OptionTag;
import org.apache.struts.taglib.html.OptionsTag;
import org.apache.struts.taglib.html.PasswordTag;
import org.apache.struts.taglib.html.SelectTag;
import org.apache.struts.taglib.html.TextTag;
import org.apache.struts.taglib.html.TextareaTag;
import org.apache.struts.upload.MultipartRequestHandler;
import org.apache.struts.util.MessageResources;
import org.metawidget.MetawidgetException;
import org.metawidget.inspector.iface.Inspector;
import org.metawidget.jsp.JspUtils;
import org.metawidget.jsp.JspUtils.BodyPreparer;
import org.metawidget.jsp.tagext.html.BaseHtmlMetawidgetTag;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.PathUtils;
import org.metawidget.util.PathUtils.TypeAndNames;
import org.metawidget.util.simple.StringUtils;
import org.w3c.dom.Element;

/**
 * Metawidget for Struts environments.
 * <p>
 * Automatically creates native Struts tags, such as <code>&lt;h:text&gt;</code> and
 * <code>&lt;h:select&gt;</code>, to suit the inspected fields.
 *
 * @author Richard Kennard
 */

public class StrutsMetawidgetTag
	extends BaseHtmlMetawidgetTag
{
	//
	// Private statics
	//

	private final static long			serialVersionUID	= 2195312831615495157L;

	private final static List<Boolean>	LIST_BOOLEAN_VALUES	= CollectionUtils.unmodifiableList( Boolean.TRUE, Boolean.FALSE );

	//
	// Private members
	//

	private String						mPropertyPrefix;

	//
	// Public methods
	//

	public void setProperty( String property )
	{
		mPath = property;

		// Take the LHS minus the first property (if any), as we assume that will
		// be supplied by the form

		if ( property != null )
		{
			int lastIndexOf = property.lastIndexOf( StringUtils.SEPARATOR_DOT_CHAR );

			if ( lastIndexOf != -1 )
			{
				int firstIndexOf = property.indexOf( StringUtils.SEPARATOR_DOT_CHAR );

				if ( firstIndexOf != lastIndexOf )
					mPropertyPrefix = property.substring( firstIndexOf + 1, lastIndexOf + 1 );
			}
		}
	}

	@Override
	public String getLocalizedKey( String key )
	{
		String localizedKey = super.getLocalizedKey( key );

		if ( localizedKey != null )
			return localizedKey;

		// Use Struts MessageResources (if any)

		MessageResources resources = (MessageResources) pageContext.getAttribute( Globals.MESSAGES_KEY, PageContext.APPLICATION_SCOPE );

		if ( resources == null )
			return null;

		return resources.getMessage( key );
	}

	@Override
	public void release()
	{
		super.release();

		// Clear the prefix, in case we're reused

		mPropertyPrefix = null;
	}

	//
	// Protected methods
	//

	@Override
	protected String inspect( Inspector inspector, String value )
	{
		TypeAndNames typeAndNames = PathUtils.parsePath( value, '.' );
		String type = typeAndNames.getType();

		// Try to locate a runtime Struts bean. This allows the Inspectors
		// to act on it polymorphically.
		//
		// Because Metawidget will typically be embedded in a Struts FORM
		// tag, Struts will typically have initialized 'strType' just-in-time

		Object obj = pageContext.findAttribute( type );

		if ( obj instanceof DynaActionForm )
		{
			// (never inspect DynaActionForms, though)

			obj = null;
		}
		else if ( obj != null )
		{
			type = obj.getClass().getName();
		}

		return inspector.inspect( obj, type, typeAndNames.getNames() );
	}

	@Override
	protected String buildReadOnlyWidget( String elementName, Map<String, String> attributes )
		throws Exception
	{
		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) )
		{
			if ( !mCreateHiddenFields || TRUE.equals( attributes.get( NO_SETTER ) ) )
				return null;

			return writeStrutsTag( HiddenTag.class, attributes );
		}

		// Action

		if ( ACTION.equals( elementName ))
			return null;

		// Masked (return an empty String, so that we DO still render a label)

		if ( TRUE.equals( attributes.get( MASKED ) ) )
			return "";

		// Lookups

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) )
			return writeReadOnlyTag( attributes );

		String strutsLookupName = attributes.get( STRUTS_LOOKUP_NAME );

		if ( strutsLookupName != null && !"".equals( strutsLookupName ) )
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
			if ( !mCreateHiddenFields )
				return null;

			if ( TRUE.equals( attributes.get( NO_SETTER ) ) )
				return null;

			return writeStrutsTag( HiddenTag.class, attributes );
		}

		// Action

		if ( ACTION.equals( elementName ))
			return null;

		// Struts Lookups

		String strutsLookup = attributes.get( STRUTS_LOOKUP_NAME );

		if ( strutsLookup != null )
		{
			return writeSelectTag( strutsLookup, attributes.get( STRUTS_LOOKUP_PROPERTY ), attributes );
		}

		// String Lookups

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) )
		{
			return writeSelectTag( CollectionUtils.fromString( lookup ), CollectionUtils.fromString( attributes.get( LOOKUP_LABELS ) ), attributes );
		}

		String type = attributes.get( TYPE );

		// If no type, fail gracefully with a text box

		if ( type == null || "".equals( type ) )
			return writeStrutsTag( TextTag.class, attributes );

		Class<?> clazz = ClassUtils.niceForName( type );

		if ( clazz != null )
		{
			// Primitives

			if ( clazz.isPrimitive() )
			{
				if ( boolean.class.equals( clazz ) )
					return writeStrutsTag( CheckboxTag.class, attributes );

				return writeStrutsTag( TextTag.class, attributes );
			}

			// Never inspect ActionForm base properties

			if ( ActionServletWrapper.class.isAssignableFrom( clazz ) )
				return null;

			if ( MultipartRequestHandler.class.isAssignableFrom( clazz ) )
				return null;

			if ( Servlet.class.isAssignableFrom( clazz ) )
				return null;

			// Strings

			if ( String.class.equals( clazz ) )
			{
				if ( TRUE.equals( attributes.get( MASKED ) ) )
					return writeStrutsTag( PasswordTag.class, attributes );

				if ( TRUE.equals( attributes.get( LARGE ) ) )
					return writeStrutsTag( TextareaTag.class, attributes );

				return writeStrutsTag( TextTag.class, attributes );
			}

			// Dates

			if ( Date.class.equals( clazz ) )
				return writeStrutsTag( TextTag.class, attributes );

			// Booleans (are tri-state)

			if ( Boolean.class.equals( clazz ) )
				return writeSelectTag( LIST_BOOLEAN_VALUES, null, attributes );

			// Numbers

			if ( Number.class.isAssignableFrom( clazz ) )
				return writeStrutsTag( TextTag.class, attributes );

			// Collections

			if ( Collection.class.isAssignableFrom( clazz ) )
				return null;

			// Not simple, but don't expand

			if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) )
				return writeStrutsTag( TextTag.class, attributes );
		}

		// Nested Metawidget

		return METAWIDGET;
	}

	@Override
	protected void beforeBuildCompoundWidget( Element element )
	{
		// Take the whole path minus the first value (if any), as we assume that will
		// be supplied by the form

		int firstIndexOf = mPath.indexOf( StringUtils.SEPARATOR_DOT_CHAR );

		if ( firstIndexOf != -1 )
			mPropertyPrefix = mPath.substring( firstIndexOf + 1 ) + StringUtils.SEPARATOR_DOT_CHAR;
	}

	//
	// Private methods
	//

	private String writeStrutsTag( Class<? extends Tag> tagClass, Map<String, String> attributes )
		throws Exception
	{
		Tag tag = tagClass.newInstance();
		initStrutsTag( tag, attributes );

		return JspUtils.writeTag( pageContext, tag, this, null );
	}

	/**
	 * Initialize the Struts Tag with various attributes, CSS settings etc.
	 * <p>
	 * In other Metawidgets, this step is done after the widget has been built. However, because JSP
	 * lacks a 'true' component model (eg. buildActiveWidget returns a String) we must do it here.
	 */

	private void initStrutsTag( Tag tag, Map<String, String> attributes )
		throws Exception
	{
		// Property

		String name = attributes.get( NAME );

		if ( mPropertyPrefix != null )
			name = mPropertyPrefix + name;

		if ( tag instanceof BaseInputTag )
		{
			( (BaseInputTag) tag ).setProperty( name );
		}
		else if ( tag instanceof SelectTag )
		{
			( (SelectTag) tag ).setProperty( name );
		}
		else if ( tag instanceof CheckboxTag )
		{
			( (CheckboxTag) tag ).setProperty( name );
		}

		// Maxlength

		if ( tag instanceof BaseInputTag )
		{
			if ( "char".equals( attributes.get( TYPE ) ) )
			{
				( (BaseInputTag) tag ).setMaxlength( "1" );
			}
			else
			{
				String maximumLength = attributes.get( MAXIMUM_LENGTH );

				if ( maximumLength != null )
					( (BaseInputTag) tag ).setMaxlength( maximumLength );
			}
		}

		// CSS

		if ( tag instanceof BaseHandlerTag )
		{
			BaseHandlerTag tagBaseHandler = (BaseHandlerTag) tag;
			tagBaseHandler.setStyle( mStyle );
			tagBaseHandler.setStyleClass( mStyleClass );
		}
	}

	private String writeReadOnlyTag( Map<String, String> attributes )
		throws Exception
	{
		HiddenTag tag = HiddenTag.class.newInstance();
		initStrutsTag( tag, attributes );
		tag.setWrite( true );

		// Note: according to STR-1305 we'll get a proper html:label tag
		// with Struts 1.4.0, so we can use it instead of .setDisabled( true )

		if ( !mCreateHiddenFields || TRUE.equals( attributes.get( NO_SETTER ) ) )
			tag.setDisabled( true );

		return JspUtils.writeTag( pageContext, tag, this, null );
	}

	private String writeSelectTag( final String name, final String property, final Map<String, String> attributes )
		throws Exception
	{
		// Write the SELECT tag

		final SelectTag tagSelect = new SelectTag();
		initStrutsTag( tagSelect, attributes );

		return JspUtils.writeTag( pageContext, tagSelect, this, new BodyPreparer()
		{
			// Within the SELECT tag, write the OPTION tags

			public void prepareBody( PageContext delgateContext )
				throws JspException, IOException
			{
				BodyContent bodyContentSelect = tagSelect.getBodyContent();

				// Empty option

				Class<?> clazz = ClassUtils.niceForName( attributes.get( TYPE ) );

				if ( clazz == null || ( !clazz.isPrimitive() && !TRUE.equals( attributes.get( REQUIRED ))))
				{
					OptionTag tagOptionEmpty = new OptionTag();
					tagOptionEmpty.setValue( "" );
					bodyContentSelect.write( JspUtils.writeTag( delgateContext, tagOptionEmpty, tagSelect, null ) );
				}

				// Options tag

				OptionsTag tagOptions = new OptionsTag();
				tagOptions.setName( name );
				tagOptions.setProperty( property );

				bodyContentSelect.write( JspUtils.writeTag( delgateContext, tagOptions, tagSelect, null ) );
			}
		} );
	}

	private String writeSelectTag( final List<?> values, final List<String> labels, final Map<String, String> attributes )
		throws Exception
	{
		// Write the SELECT tag

		final SelectTag tagSelect = new SelectTag();
		initStrutsTag( tagSelect, attributes );

		return JspUtils.writeTag( pageContext, tagSelect, this, new BodyPreparer()
		{
			// Within the SELECT tag, write the OPTION tags

			public void prepareBody( PageContext delgateContext )
				throws JspException, IOException
			{
				// See if we're using labels

				if ( labels != null && !labels.isEmpty() && labels.size() != values.size() )
					throw MetawidgetException.newException( "Labels list must be same size as values list" );

				BodyContent bodyContentSelect = tagSelect.getBodyContent();

				// Empty option

				Class<?> clazz = ClassUtils.niceForName( attributes.get( TYPE ) );

				if ( clazz == null || ( !clazz.isPrimitive() && !TRUE.equals( attributes.get( REQUIRED ))))
				{
					OptionTag tagOptionEmpty = new OptionTag();
					tagOptionEmpty.setValue( "" );
					bodyContentSelect.write( JspUtils.writeTag( delgateContext, tagOptionEmpty, tagSelect, null ) );
				}

				// Add the options

				for ( int loop = 0, length = values.size(); loop < length; loop++ )
				{
					final OptionTag tagOption = new OptionTag();
					tagOption.setValue( String.valueOf( values.get( loop ) ) );

					if ( labels == null || labels.isEmpty() )
					{
						bodyContentSelect.write( JspUtils.writeTag( delgateContext, tagOption, tagSelect, null ) );
						continue;
					}

					final String label = labels.get( loop );

					bodyContentSelect.write( JspUtils.writeTag( delgateContext, tagOption, tagSelect, new BodyPreparer()
					{
						public void prepareBody( PageContext optionDelgateContext )
							throws IOException
						{
							tagOption.getBodyContent().write( label );
						}
					} ) );
				}
			}
		} );
	}
}
