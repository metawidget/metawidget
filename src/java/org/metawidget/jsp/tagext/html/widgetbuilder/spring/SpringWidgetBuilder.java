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

package org.metawidget.jsp.tagext.html.widgetbuilder.spring;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.spring.SpringInspectionResultConstants.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import org.metawidget.jsp.JspUtils;
import org.metawidget.jsp.JspUtils.BodyPreparer;
import org.metawidget.jsp.tagext.StubTag;
import org.metawidget.jsp.tagext.html.spring.SpringMetawidgetTag;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilderException;
import org.metawidget.widgetbuilder.impl.BaseWidgetBuilder;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.tags.RequestContextAwareTag;
import org.springframework.web.servlet.tags.form.AbstractDataBoundFormElementTag;
import org.springframework.web.servlet.tags.form.AbstractHtmlElementTag;
import org.springframework.web.servlet.tags.form.CheckboxTag;
import org.springframework.web.servlet.tags.form.HiddenInputTag;
import org.springframework.web.servlet.tags.form.InputTag;
import org.springframework.web.servlet.tags.form.OptionTag;
import org.springframework.web.servlet.tags.form.OptionsTag;
import org.springframework.web.servlet.tags.form.PasswordInputTag;
import org.springframework.web.servlet.tags.form.SelectTag;
import org.springframework.web.servlet.tags.form.TextareaTag;

/**
 * WidgetBuilder for Spring environments.
 * <p>
 * Automatically creates native Spring form tags, such as <code>&lt;form:input&gt;</code> and
 * <code>&lt;form:select&gt;</code>, to suit the inspected fields.
 *
 * @author Richard Kennard
 */

public class SpringWidgetBuilder
	extends BaseWidgetBuilder<Object, SpringMetawidgetTag>
{
	//
	// Private statics
	//

	private final static List<Boolean>	LIST_BOOLEAN_VALUES	= CollectionUtils.unmodifiableList( Boolean.TRUE, Boolean.FALSE );

	//
	// Protected methods
	//

	@Override
	protected Object buildReadOnlyWidget( String elementName, Map<String, String> attributes, SpringMetawidgetTag metawidget )
		throws Exception
	{
		// Not for us?

		if ( TRUE.equals( attributes.get( HIDDEN ) ) )
		{
			if ( metawidget.isCreateHiddenFields() && !TRUE.equals( attributes.get( NO_SETTER ) ) )
				return writeSpringTag( HiddenInputTag.class, attributes, metawidget );

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

		String springLookup = attributes.get( SPRING_LOOKUP );

		if ( springLookup != null && !"".equals( springLookup ) )
			return writeReadOnlyTag( attributes, metawidget );

		String type = getType( attributes );

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
		}

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) )
			return writeReadOnlyTag( attributes, metawidget );

		// Nested Metawidget

		return null;
	}

	@Override
	protected Object buildActiveWidget( String elementName, Map<String, String> attributes, SpringMetawidgetTag metawidget )
		throws Exception
	{
		// Not for us?

		if ( TRUE.equals( attributes.get( HIDDEN ) ) )
		{
			if ( metawidget.isCreateHiddenFields() && !TRUE.equals( attributes.get( NO_SETTER ) ) )
				return writeSpringTag( HiddenInputTag.class, attributes, metawidget );

			return null;
		}

		// Actions (ignored)

		if ( ACTION.equals( elementName ) )
			return new StubTag.StubContent();

		String type = getType( attributes );

		// If no type, assume a String

		if ( type == null )
			type = String.class.getName();

		// Lookup the Class

		Class<?> clazz = ClassUtils.niceForName( type );

		// Support mandatory Booleans (can be rendered as a checkbox, even though they have a
		// Lookup)

		if ( Boolean.class.equals( clazz ) && TRUE.equals( attributes.get( REQUIRED ) ) )
			return writeSpringTag( CheckboxTag.class, attributes, metawidget );

		// Spring Lookups

		String springLookup = attributes.get( SPRING_LOOKUP );

		if ( springLookup != null && !"".equals( springLookup ) )
			return writeSelectTag( springLookup, attributes, metawidget );

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
					return writeSpringTag( CheckboxTag.class, attributes, metawidget );

				return writeSpringTag( InputTag.class, attributes, metawidget );
			}

			// Strings

			if ( String.class.equals( clazz ) )
			{
				if ( TRUE.equals( attributes.get( MASKED ) ) )
					return writeSpringTag( PasswordInputTag.class, attributes, metawidget );

				if ( TRUE.equals( attributes.get( LARGE ) ) )
					return writeSpringTag( TextareaTag.class, attributes, metawidget );

				return writeSpringTag( InputTag.class, attributes, metawidget );
			}

			// Dates

			if ( Date.class.equals( clazz ) )
				return writeSpringTag( InputTag.class, attributes, metawidget );

			// Booleans (are tri-state)

			if ( Boolean.class.equals( clazz ) )
				return writeSelectTag( LIST_BOOLEAN_VALUES, null, attributes, metawidget );

			// Numbers

			if ( Number.class.isAssignableFrom( clazz ) )
				return writeSpringTag( InputTag.class, attributes, metawidget );
		}

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) )
			return writeSpringTag( InputTag.class, attributes, metawidget );

		// Nested Metawidget

		return null;
	}

	//
	// Private methods
	//

	private String writeSpringTag( Class<? extends Tag> tagClass, Map<String, String> attributes, SpringMetawidgetTag metawidget )
		throws Exception
	{
		Tag tag = tagClass.newInstance();
		initSpringTag( tag, attributes, metawidget );

		return JspUtils.writeTag( metawidget.getPageContext(), tag, metawidget, null );
	}

	/**
	 * Initialize the Spring Tag with various attributes, CSS settings etc.
	 * <p>
	 * In other Metawidgets, this step is done after the widget has been built. However, because JSP
	 * lacks a 'true' component model (eg. buildActiveWidget returns a String) we must do it here.
	 */

	private void initSpringTag( Tag tag, Map<String, String> attributes, SpringMetawidgetTag metawidget )
		throws Exception
	{
		// Path

		String path = attributes.get( NAME );

		if ( metawidget.getPathPrefix() != null )
			path = metawidget.getPathPrefix() + path;

		if ( tag instanceof AbstractDataBoundFormElementTag )
		{
			( (AbstractDataBoundFormElementTag) tag ).setPath( path );
		}

		// Maxlength

		if ( tag instanceof InputTag )
		{
			if ( "char".equals( getType( attributes ) ) )
			{
				( (InputTag) tag ).setMaxlength( "1" );
			}
			else
			{
				String maximumLength = attributes.get( MAXIMUM_LENGTH );

				if ( maximumLength != null )
					( (InputTag) tag ).setMaxlength( maximumLength );
			}
		}

		// CSS

		if ( tag instanceof AbstractHtmlElementTag )
		{
			AbstractHtmlElementTag tagAbstractHtmlElement = (AbstractHtmlElementTag) tag;
			tagAbstractHtmlElement.setCssStyle( metawidget.getStyle() );
			tagAbstractHtmlElement.setCssClass( metawidget.getStyleClass() );
		}
	}

	private String writeReadOnlyTag( Map<String, String> attributes, SpringMetawidgetTag metawidget )
		throws Exception
	{
		// (use StringBuffer for J2SE 1.4 compatibility)

		StringBuffer buffer = new StringBuffer();

		// Use the Spring binder to render the read-only value

		RequestContext requestContext = (RequestContext) metawidget.getPageContext().getAttribute( RequestContextAwareTag.REQUEST_CONTEXT_PAGE_ATTRIBUTE );
		String path = metawidget.getPath() + StringUtils.SEPARATOR_DOT_CHAR + attributes.get( NAME );
		String value = requestContext.getBindStatus( path ).getDisplayValue();

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

		// May need a hidden input tag too

		if ( metawidget.isCreateHiddenFields() && !TRUE.equals( attributes.get( NO_SETTER ) ) )
			buffer.append( writeSpringTag( HiddenInputTag.class, attributes, metawidget ) );

		return buffer.toString();
	}

	private String writeSelectTag( final String expression, final Map<String, String> attributes, SpringMetawidgetTag metawidget )
		throws Exception
	{
		// Write the SELECT tag

		final SelectTag tagSelect = new SelectTag();
		initSpringTag( tagSelect, attributes, metawidget );

		final Class<?> clazz = ClassUtils.niceForName( getType( attributes ) );

		return JspUtils.writeTag( metawidget.getPageContext(), tagSelect, metawidget, new BodyPreparer()
		{
			// Within the SELECT tag, write the OPTION tags

			public void prepareBody( PageContext delgateContext )
				throws JspException, IOException
			{
				// Empty option

				if ( clazz == null || ( !clazz.isPrimitive() && !TRUE.equals( attributes.get( REQUIRED ) ) ) )
				{
					OptionTag tagOptionEmpty = new OptionTag();
					tagOptionEmpty.setValue( "" );
					delgateContext.getOut().write( JspUtils.writeTag( delgateContext, tagOptionEmpty, tagSelect, null ) );
				}

				// Options tag

				OptionsTag tagOptions = new OptionsTag();
				tagOptions.setItems( expression );

				// Optional itemValue and itemLabel

				String itemValue = attributes.get( SPRING_LOOKUP_ITEM_VALUE );

				if ( itemValue != null )
					tagOptions.setItemValue( itemValue );

				String itemLabel = attributes.get( SPRING_LOOKUP_ITEM_LABEL );

				if ( itemLabel != null )
					tagOptions.setItemLabel( itemLabel );

				delgateContext.getOut().write( JspUtils.writeTag( delgateContext, tagOptions, tagSelect, null ) );
			}
		} );
	}

	private String writeSelectTag( final List<?> values, final List<String> labels, final Map<String, String> attributes, SpringMetawidgetTag metawidget )
		throws Exception
	{
		// Write the SELECT tag

		final SelectTag tagSelect = new SelectTag();
		initSpringTag( tagSelect, attributes, metawidget );

		final Class<?> clazz = ClassUtils.niceForName( getType( attributes ) );

		return JspUtils.writeTag( metawidget.getPageContext(), tagSelect, metawidget, new BodyPreparer()
		{
			// Within the SELECT tag, write the OPTION tags

			public void prepareBody( PageContext delgateContext )
				throws JspException, IOException
			{
				// See if we're using labels

				if ( labels != null && !labels.isEmpty() && labels.size() != values.size() )
					throw WidgetBuilderException.newException( "Labels list must be same size as values list" );

				// Empty option

				if ( clazz == null || ( !clazz.isPrimitive() && !TRUE.equals( attributes.get( REQUIRED ) ) ) )
				{
					OptionTag tagOptionEmpty = new OptionTag();
					tagOptionEmpty.setValue( "" );
					delgateContext.getOut().write( JspUtils.writeTag( delgateContext, tagOptionEmpty, tagSelect, null ) );
				}

				// Add the options

				for ( int loop = 0, length = values.size(); loop < length; loop++ )
				{
					final OptionTag tagOption = new OptionTag();
					tagOption.setValue( values.get( loop ) );

					if ( labels != null && !labels.isEmpty() )
						tagOption.setLabel( labels.get( loop ) );

					delgateContext.getOut().write( JspUtils.writeTag( delgateContext, tagOption, tagSelect, null ) );
				}
			}
		} );
	}
}
