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

package org.metawidget.jsp.tagext.html.widgetbuilder.spring;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.spring.SpringInspectionResultConstants.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.tagext.Tag;

import org.metawidget.jsp.JspUtils;
import org.metawidget.jsp.tagext.MetawidgetTag;
import org.metawidget.jsp.tagext.html.HtmlStubTag;
import org.metawidget.jsp.tagext.html.widgetprocessor.HiddenFieldProcessor;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.metawidget.widgetbuilder.iface.WidgetBuilderException;
import org.springframework.web.servlet.tags.form.CheckboxTag;
import org.springframework.web.servlet.tags.form.InputTag;
import org.springframework.web.servlet.tags.form.OptionTag;
import org.springframework.web.servlet.tags.form.OptionsTag;
import org.springframework.web.servlet.tags.form.PasswordInputTag;
import org.springframework.web.servlet.tags.form.SelectTag;
import org.springframework.web.servlet.tags.form.TextareaTag;

/**
 * WidgetBuilder for Spring environments.
 * <p>
 * Creates native Spring form tags, such as <code>&lt;form:input&gt;</code> and
 * <code>&lt;form:select&gt;</code>, to suit the inspected fields.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class SpringWidgetBuilder
	implements WidgetBuilder<Tag, MetawidgetTag> {

	//
	// Private statics
	//

	private static final List<Boolean>	LIST_BOOLEAN_VALUES	= CollectionUtils.unmodifiableList( Boolean.TRUE, Boolean.FALSE );

	//
	// Public methods
	//

	public Tag buildWidget( String elementName, Map<String, String> attributes, MetawidgetTag metawidget ) {

		// Hidden?

		if ( TRUE.equals( attributes.get( HIDDEN ) ) ) {
			attributes.put( HiddenFieldProcessor.ATTRIBUTE_NEEDS_HIDDEN_FIELD, TRUE );
			return new HtmlStubTag();
		}

		// Actions (ignored)

		if ( ACTION.equals( elementName ) ) {
			return new HtmlStubTag();
		}

		// Lookup the class

		Class<?> clazz = WidgetBuilderUtils.getActualClassOrType( attributes, String.class );

		// Support mandatory Booleans (can be rendered as a checkbox, even though they have a
		// Lookup)

		if ( Boolean.class.equals( clazz ) && TRUE.equals( attributes.get( REQUIRED ) ) ) {
			return new CheckboxTag();
		}

		// Spring Lookups

		String springLookup = attributes.get( SPRING_LOOKUP );

		if ( springLookup != null && !"".equals( springLookup ) ) {
			return createSelectTag( springLookup, attributes );
		}

		// String Lookups

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) ) {
			return createSelectTag( CollectionUtils.fromString( lookup ), CollectionUtils.fromString( attributes.get( LOOKUP_LABELS ) ), attributes );
		}

		if ( clazz != null ) {
			// Primitives

			if ( clazz.isPrimitive() ) {

				if ( boolean.class.equals( clazz ) ) {
					return new CheckboxTag();
				}

				if ( char.class.equals( clazz ) ) {
					InputTag inputTag = new InputTag();
					inputTag.setMaxlength( "1" );
					return inputTag;
				}

				return createInputTag( attributes );
			}

			// Strings

			if ( String.class.equals( clazz ) ) {
				if ( TRUE.equals( attributes.get( MASKED ) ) ) {
					InputTag passwordInputTag = new PasswordInputTag();
					String maximumLength = attributes.get( MAXIMUM_LENGTH );

					if ( maximumLength != null ) {
						passwordInputTag.setMaxlength( maximumLength );
					}

					return passwordInputTag;
				}

				if ( TRUE.equals( attributes.get( LARGE ) ) ) {
					return new TextareaTag();
				}

				return createInputTag( attributes );
			}

			// Character

			if ( Character.class.equals( clazz ) ) {
				InputTag inputTag = new InputTag();
				inputTag.setMaxlength( "1" );
				return inputTag;
			}

			// Dates

			if ( Date.class.equals( clazz ) ) {
				return createInputTag( attributes );
			}

			// Booleans (are tri-state)

			if ( Boolean.class.equals( clazz ) ) {
				return createSelectTag( LIST_BOOLEAN_VALUES, null, attributes );
			}

			// Numbers

			if ( Number.class.isAssignableFrom( clazz ) ) {
				return createInputTag( attributes );
			}

			// Collections

			if ( Collection.class.isAssignableFrom( clazz ) ) {
				return new HtmlStubTag();
			}
		}

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) ) {
			return createInputTag( attributes );
		}

		// Nested Metawidget

		return null;
	}

	//
	// Private methods
	//

	private InputTag createInputTag( Map<String, String> attributes ) {

		InputTag inputTag = new InputTag();

		// Maxlength

		String maximumLength = attributes.get( MAXIMUM_LENGTH );

		if ( maximumLength != null ) {
			inputTag.setMaxlength( maximumLength );
		}

		return inputTag;
	}

	private Tag createSelectTag( String expression, Map<String, String> attributes ) {

		// Select tag

		SelectTag select = new SelectTag();

		// Empty option (if needed)

		if ( WidgetBuilderUtils.needsEmptyLookupItem( attributes ) ) {
			OptionTag emptyOption = new OptionTag();
			emptyOption.setValue( "" );
			JspUtils.addDeferredChild( select, emptyOption );
		}

		// Options tag

		OptionsTag optionsTag = new OptionsTag();
		optionsTag.setItems( expression );

		// Optional itemValue and itemLabel

		String itemValue = attributes.get( SPRING_LOOKUP_ITEM_VALUE );

		if ( itemValue != null ) {
			optionsTag.setItemValue( itemValue );
		}

		String itemLabel = attributes.get( SPRING_LOOKUP_ITEM_LABEL );

		if ( itemLabel != null ) {
			optionsTag.setItemLabel( itemLabel );
		}

		JspUtils.addDeferredChild( select, optionsTag );
		return select;
	}

	private Tag createSelectTag( List<?> values, List<String> labels, Map<String, String> attributes ) {

		// See if we're using labels

		if ( labels != null && !labels.isEmpty() && labels.size() != values.size() ) {
			throw WidgetBuilderException.newException( "Labels list must be same size as values list" );
		}

		// Select tag

		SelectTag select = new SelectTag();

		// Empty option (if needed)

		if ( WidgetBuilderUtils.needsEmptyLookupItem( attributes ) ) {
			OptionTag emptyOption = new OptionTag();
			emptyOption.setValue( "" );
			JspUtils.addDeferredChild( select, emptyOption );
		}

		// Add the options

		for ( int loop = 0, length = values.size(); loop < length; loop++ ) {
			OptionTag optionTag = new OptionTag();
			optionTag.setValue( values.get( loop ) );

			if ( labels != null && !labels.isEmpty() ) {
				optionTag.setLabel( labels.get( loop ) );
			}

			JspUtils.addDeferredChild( select, optionTag );
		}

		return select;
	}
}
