// Metawidget (licensed under LGPL)
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

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.tagext.Tag;

import org.apache.struts.taglib.html.CheckboxTag;
import org.apache.struts.taglib.html.OptionTag;
import org.apache.struts.taglib.html.OptionsTag;
import org.apache.struts.taglib.html.PasswordTag;
import org.apache.struts.taglib.html.SelectTag;
import org.apache.struts.taglib.html.TextTag;
import org.apache.struts.taglib.html.TextareaTag;
import org.metawidget.jsp.JspUtils;
import org.metawidget.jsp.tagext.MetawidgetTag;
import org.metawidget.jsp.tagext.html.HtmlStubTag;
import org.metawidget.jsp.tagext.html.widgetprocessor.HiddenFieldProcessor;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.metawidget.widgetbuilder.iface.WidgetBuilderException;

/**
 * WidgetBuilder for Struts environments.
 * <p>
 * Creates native Struts tags, such as <code>&lt;h:text&gt;</code> and <code>&lt;h:select&gt;</code>
 * , to suit the inspected fields.
 *
 * @author Richard Kennard
 */

public class StrutsWidgetBuilder
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

		// Lookup the Class

		Class<?> clazz = WidgetBuilderUtils.getActualClassOrType( attributes, String.class );

		// Support mandatory Booleans (can be rendered as a checkbox, even though they have a
		// Lookup)

		if ( Boolean.class.equals( clazz ) && TRUE.equals( attributes.get( REQUIRED ) ) ) {
			return new CheckboxTag();
		}

		// Struts Lookups

		String strutsLookup = attributes.get( STRUTS_LOOKUP_NAME );

		if ( strutsLookup != null ) {
			return createSelectTag( strutsLookup, attributes.get( STRUTS_LOOKUP_PROPERTY ), attributes );
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
					TextTag textTag = new TextTag();
					textTag.setMaxlength( "1" );
					return textTag;
				}

				return createTextTag( attributes );
			}

			// Strings

			if ( String.class.equals( clazz ) ) {
				if ( TRUE.equals( attributes.get( MASKED ) ) ) {
					PasswordTag passwordTag = new PasswordTag();
					String maximumLength = attributes.get( MAXIMUM_LENGTH );

					if ( maximumLength != null ) {
						passwordTag.setMaxlength( maximumLength );
					}

					return passwordTag;
				}

				if ( TRUE.equals( attributes.get( LARGE ) ) ) {
					return new TextareaTag();
				}

				return createTextTag( attributes );
			}

			// Character

			if ( Character.class.equals( clazz ) ) {
				TextTag textTag = new TextTag();
				textTag.setMaxlength( "1" );
				return textTag;
			}

			// Dates

			if ( Date.class.equals( clazz ) ) {
				return createTextTag( attributes );
			}

			// Booleans (are tri-state)

			if ( Boolean.class.equals( clazz ) ) {
				return createSelectTag( LIST_BOOLEAN_VALUES, null, attributes );
			}

			// Numbers

			if ( Number.class.isAssignableFrom( clazz ) ) {
				return createTextTag( attributes );
			}

			// Collections

			if ( Collection.class.isAssignableFrom( clazz ) ) {
				return new HtmlStubTag();
			}
		}

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) ) {
			return createTextTag( attributes );
		}

		// Nested Metawidget

		return null;
	}

	//
	// Private methods
	//

	private TextTag createTextTag( Map<String, String> attributes ) {

		TextTag textTag = new TextTag();

		// Maxlength

		String maximumLength = attributes.get( MAXIMUM_LENGTH );

		if ( maximumLength != null ) {
			textTag.setMaxlength( maximumLength );
		}

		return textTag;
	}

	private Tag createSelectTag( String name, String property, Map<String, String> attributes ) {

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
		optionsTag.setName( name );
		optionsTag.setProperty( property );

		// Optional labelName and labelProperty

		String labelName = attributes.get( STRUTS_LOOKUP_LABEL_NAME );

		if ( labelName != null ) {
			optionsTag.setLabelName( labelName );
		}

		String labelProperty = attributes.get( STRUTS_LOOKUP_LABEL_PROPERTY );

		if ( labelProperty != null ) {
			optionsTag.setLabelProperty( labelProperty );
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
			optionTag.setValue( String.valueOf( values.get( loop ) ) );

			if ( labels != null && !labels.isEmpty() ) {
				JspUtils.setBodyContent( optionTag, labels.get( loop ) );
			}

			JspUtils.addDeferredChild( select, optionTag );
		}

		return select;
	}
}
