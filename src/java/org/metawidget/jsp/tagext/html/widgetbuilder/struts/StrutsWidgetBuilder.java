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

package org.metawidget.jsp.tagext.html.widgetbuilder.struts;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.struts.StrutsInspectionResultConstants.*;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.Tag;

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
import org.metawidget.MetawidgetException;
import org.metawidget.jsp.JspUtils;
import org.metawidget.jsp.JspUtils.BodyPreparer;
import org.metawidget.jsp.tagext.StubTag;
import org.metawidget.jsp.tagext.html.struts.StrutsMetawidgetTag;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.widgetbuilder.impl.BaseWidgetBuilder;
import org.springframework.web.servlet.tags.form.InputTag;

/**
 * WidgetBuilder for Struts environments.
 * <p>
 * Automatically creates native Struts tags, such as <code>&lt;h:text&gt;</code> and
 * <code>&lt;h:select&gt;</code>, to suit the inspected fields.
 *
 * @author Richard Kennard
 */

@SuppressWarnings( "deprecation" )
public class StrutsWidgetBuilder
	extends BaseWidgetBuilder<Object, StrutsMetawidgetTag>
{
	//
	// Private statics
	//

	private final static List<Boolean>	LIST_BOOLEAN_VALUES	= CollectionUtils.unmodifiableList( Boolean.TRUE, Boolean.FALSE );

	//
	// Protected methods
	//

	@Override
	protected Object buildReadOnlyWidget( String elementName, Map<String, String> attributes, StrutsMetawidgetTag metawidget )
		throws Exception
	{
		// Not for us?

		if ( TRUE.equals( attributes.get( HIDDEN ) ) )
		{
			if ( metawidget.isCreateHiddenFields() && !TRUE.equals( attributes.get( NO_SETTER ) ) )
				return writeStrutsTag( HiddenTag.class, attributes, metawidget );

			return null;
		}

		if ( ACTION.equals( elementName ) )
			return null;

		if ( TRUE.equals( attributes.get( MASKED ) ) )
			return null;

		// Lookups

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) )
			return writeReadOnlyTag( attributes, metawidget );

		String strutsLookupName = attributes.get( STRUTS_LOOKUP_NAME );

		if ( strutsLookupName != null && !"".equals( strutsLookupName ) )
			return writeReadOnlyTag( attributes, metawidget );

		String type = attributes.get( TYPE );

		// If no type, fail gracefully

		if ( type == null || type.length() == 0 )
			return writeReadOnlyTag( attributes, metawidget );

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
	protected Object buildActiveWidget( String elementName, Map<String, String> attributes, StrutsMetawidgetTag metawidget )
		throws Exception
	{
		// Not for us?

		if ( TRUE.equals( attributes.get( HIDDEN ) ) )
		{
			if ( metawidget.isCreateHiddenFields() && !TRUE.equals( attributes.get( NO_SETTER ) ) )
				return writeStrutsTag( HiddenTag.class, attributes, metawidget );

			return null;
		}

		// Actions (ignored)

		if ( ACTION.equals( elementName ) )
			return new StubTag.StubContent();

		String type = attributes.get( TYPE );

		// If no type, fail gracefully with a text box

		if ( type == null || "".equals( type ) )
			return writeStrutsTag( InputTag.class, attributes, metawidget );

		// Lookup the Class

		Class<?> clazz = ClassUtils.niceForName( type );

		// Support mandatory Booleans (can be rendered as a checkbox, even though they have a
		// Lookup)

		if ( Boolean.class.equals( clazz ) && TRUE.equals( attributes.get( REQUIRED ) ) )
			return writeStrutsTag( CheckboxTag.class, attributes, metawidget );

		// Struts Lookups

		String strutsLookup = attributes.get( STRUTS_LOOKUP_NAME );

		if ( strutsLookup != null )
			return writeSelectTag( strutsLookup, attributes.get( STRUTS_LOOKUP_PROPERTY ), attributes, metawidget );

		// String Lookups

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) )
			return writeSelectTag( CollectionUtils.fromString( lookup ), CollectionUtils.fromString( attributes.get( LOOKUP_LABELS ) ), attributes, metawidget );

		if ( clazz != null )
		{
			// Primitives

			if ( clazz.isPrimitive() )
			{
				if ( boolean.class.equals( clazz ) )
					return writeStrutsTag( CheckboxTag.class, attributes, metawidget );

				return writeStrutsTag( TextTag.class, attributes, metawidget );
			}

			// Strings

			if ( String.class.equals( clazz ) )
			{
				if ( TRUE.equals( attributes.get( MASKED ) ) )
					return writeStrutsTag( PasswordTag.class, attributes, metawidget );

				if ( TRUE.equals( attributes.get( LARGE ) ) )
					return writeStrutsTag( TextareaTag.class, attributes, metawidget );

				return writeStrutsTag( TextTag.class, attributes, metawidget );
			}

			// Dates

			if ( Date.class.equals( clazz ) )
				return writeStrutsTag( TextTag.class, attributes, metawidget );

			// Booleans (are tri-state)

			if ( Boolean.class.equals( clazz ) )
				return writeSelectTag( LIST_BOOLEAN_VALUES, null, attributes, metawidget );

			// Numbers

			if ( Number.class.isAssignableFrom( clazz ) )
				return writeStrutsTag( TextTag.class, attributes, metawidget );
		}

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) )
			return writeStrutsTag( TextTag.class, attributes, metawidget );

		// Nested Metawidget

		return null;
	}

	//
	// Private methods
	//

	private String writeStrutsTag( Class<? extends Tag> tagClass, Map<String, String> attributes, StrutsMetawidgetTag metawidget )
		throws Exception
	{
		Tag tag = tagClass.newInstance();
		initStrutsTag( tag, attributes, metawidget );

		return JspUtils.writeTag( metawidget.getPageContext(), tag, metawidget, null );
	}

	/**
	 * Initialize the Struts Tag with various attributes, CSS settings etc.
	 * <p>
	 * In other Metawidgets, this step is done after the widget has been built. However, because JSP
	 * lacks a 'true' component model (eg. buildActiveWidget returns a String) we must do it here.
	 */

	private void initStrutsTag( Tag tag, Map<String, String> attributes, StrutsMetawidgetTag metawidget )
		throws Exception
	{
		// Property

		String name = attributes.get( NAME );

		if ( metawidget.getPathPrefix() != null )
			name = metawidget.getPathPrefix() + name;

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
			tagBaseHandler.setStyle( metawidget.getStyle() );
			tagBaseHandler.setStyleClass( metawidget.getStyleClass() );
		}
	}

	private String writeReadOnlyTag( Map<String, String> attributes, StrutsMetawidgetTag metawidget )
		throws Exception
	{
		HiddenTag tag = HiddenTag.class.newInstance();
		initStrutsTag( tag, attributes, metawidget );
		tag.setWrite( true );

		// Note: according to STR-1305 we'll get a proper html:label tag
		// with Struts 1.4.0, so we can use it instead of .setDisabled( true )

		if ( !metawidget.isCreateHiddenFields() || TRUE.equals( attributes.get( NO_SETTER ) ) )
			tag.setDisabled( true );

		String toReturn = JspUtils.writeTag( metawidget.getPageContext(), tag, metawidget, null );

		// If the String is just a hidden field, output a SPAN tag to
		// stop the whole thing vanishing under HtmlTableLayout. This is
		// a bit hacky, unfortunately, but it's because JSP doesn't have
		// a 'real' component model

		if ( JspUtils.isJustHiddenFields( toReturn ) )
			toReturn += "<span></span>";

		return toReturn;
	}

	private String writeSelectTag( final String name, final String property, final Map<String, String> attributes, StrutsMetawidgetTag metawidget )
		throws Exception
	{
		// Write the SELECT tag

		final SelectTag tagSelect = new SelectTag();
		initStrutsTag( tagSelect, attributes, metawidget );

		return JspUtils.writeTag( metawidget.getPageContext(), tagSelect, metawidget, new BodyPreparer()
		{
			// Within the SELECT tag, write the OPTION tags

			public void prepareBody( PageContext delgateContext )
				throws JspException, IOException
			{
				BodyContent bodyContentSelect = tagSelect.getBodyContent();

				// Empty option

				Class<?> clazz = ClassUtils.niceForName( attributes.get( TYPE ) );

				if ( clazz == null || ( !clazz.isPrimitive() && !TRUE.equals( attributes.get( REQUIRED ) ) ) )
				{
					OptionTag tagOptionEmpty = new OptionTag();
					tagOptionEmpty.setValue( "" );
					bodyContentSelect.write( JspUtils.writeTag( delgateContext, tagOptionEmpty, tagSelect, null ) );
				}

				// Options tag

				OptionsTag tagOptions = new OptionsTag();
				tagOptions.setName( name );
				tagOptions.setProperty( property );

				// Optional labelName and labelProperty

				String labelName = attributes.get( STRUTS_LOOKUP_LABEL_NAME );

				if ( labelName != null )
					tagOptions.setLabelName( labelName );

				String labelProperty = attributes.get( STRUTS_LOOKUP_LABEL_PROPERTY );

				if ( labelProperty != null )
					tagOptions.setLabelProperty( labelProperty );

				bodyContentSelect.write( JspUtils.writeTag( delgateContext, tagOptions, tagSelect, null ) );
			}
		} );
	}

	private String writeSelectTag( final List<?> values, final List<String> labels, final Map<String, String> attributes, StrutsMetawidgetTag metawidget )
		throws Exception
	{
		// Write the SELECT tag

		final SelectTag tagSelect = new SelectTag();
		initStrutsTag( tagSelect, attributes, metawidget );

		return JspUtils.writeTag( metawidget.getPageContext(), tagSelect, metawidget, new BodyPreparer()
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

				if ( clazz == null || ( !clazz.isPrimitive() && !TRUE.equals( attributes.get( REQUIRED ) ) ) )
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
