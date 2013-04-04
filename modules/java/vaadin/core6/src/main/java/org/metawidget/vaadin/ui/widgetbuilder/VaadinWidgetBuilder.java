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

package org.metawidget.vaadin.ui.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.vaadin.ui.Stub;
import org.metawidget.vaadin.ui.VaadinMetawidget;
import org.metawidget.vaadin.ui.widgetprocessor.binding.BindingConverter;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.metawidget.widgetbuilder.iface.WidgetBuilderException;

import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Select;
import com.vaadin.ui.Slider;
import com.vaadin.ui.Slider.ValueOutOfBoundsException;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

/**
 * WidgetBuilder for Vaadin environments.
 * <p>
 * Creates native Vaadin <code>Components</code>, such as <code>TextField</code> and
 * <code>Select</code> or <code>CheckBox<code>, to suit the inspected fields.
 *
 * @author Loghman Barari
 */

public class VaadinWidgetBuilder
	implements WidgetBuilder<Component, VaadinMetawidget> {

	//
	// Public methods
	//

	public Component buildWidget( String elementName, Map<String, String> attributes, VaadinMetawidget metawidget ) {

		// Hidden

		if ( TRUE.equals( attributes.get( HIDDEN ) ) ) {
			return new Stub();
		}

		// Action

		if ( ACTION.equals( elementName ) ) {
			return new Button();
		}

		// Lookup the Class

		Class<?> clazz = WidgetBuilderUtils.getActualClassOrType( attributes, String.class );

		// Support mandatory Booleans (can be rendered as a checkbox, even
		// though they have a Lookup)

		if ( Boolean.class.equals( clazz ) && TRUE.equals( attributes.get( REQUIRED ) ) ) {
			return new CheckBox();
		}

		// Lookups

		String lookup = attributes.get( LOOKUP );

		if ( lookup != null && !"".equals( lookup ) ) {
			return createSelectComponent( attributes, lookup, metawidget );
		}

		if ( clazz != null ) {

			// Primitives

			if ( clazz.isPrimitive() ) {
				// booleans

				if ( boolean.class.equals( clazz ) ) {
					return new CheckBox();
				}

				// chars

				if ( char.class.equals( clazz ) ) {
					TextField textField = new TextField();
					textField.setMaxLength( 1 );

					return textField;
				}

				// Ranged

				String minimumValue = attributes.get( MINIMUM_VALUE );
				String maximumValue = attributes.get( MAXIMUM_VALUE );

				if ( minimumValue != null && !"".equals( minimumValue ) && maximumValue != null && !"".equals( maximumValue ) ) {
					Slider slider = new Slider();
					slider.setMin( Double.parseDouble( minimumValue ) );
					try {
						// Use big 'D' Double for Vaadin 6/7 compatibility
						slider.setValue( Double.valueOf( slider.getMin() ));
					} catch ( ValueOutOfBoundsException e ) {
						throw WidgetBuilderException.newException( e );
					}
					slider.setMax( Double.parseDouble( maximumValue ) );

					return slider;
				}

				// Not-ranged

				return createTextField( attributes );
			}

			// Strings

			if ( String.class.equals( clazz ) ) {
				if ( TRUE.equals( attributes.get( MASKED ) ) ) {
					return new PasswordField();
				}

				if ( TRUE.equals( attributes.get( LARGE ) ) ) {
					return new TextArea();
				}

				return createTextField( attributes );
			}

			// Characters

			if ( Character.class.isAssignableFrom( clazz ) ) {
				TextField textField = new TextField();
				textField.setMaxLength( 1 );

				return textField;
			}

			// Dates

			if ( Date.class.equals( clazz ) ) {
				return new PopupDateField();
			}

			// Numbers
			//
			// Note: we use a text field, not a JSpinner or JSlider, because
			// BeansBinding gets upset at doing 'setValue( null )' if the Integer
			// is null. We can still use JSpinner/JSliders for primitives, though.

			if ( Number.class.isAssignableFrom( clazz ) ) {
				return createTextField( attributes );
			}

			// Collections

			if ( Collection.class.isAssignableFrom( clazz ) ) {
				return new Stub();
			}
		}

		// Not simple, but don't expand

		if ( TRUE.equals( attributes.get( DONT_EXPAND ) ) ) {
			return createTextField( attributes );
		}

		return null;
	}

	//
	// Private methods
	//

	private TextField createTextField( Map<String, String> attributes ) {

		TextField textField = new TextField();

		String maximumLength = attributes.get( MAXIMUM_LENGTH );

		if ( maximumLength != null && !"".equals( maximumLength ) ) {
			textField.setMaxLength( Integer.parseInt( attributes.get( MAXIMUM_LENGTH ) ) );
		}

		return textField;
	}

	private Component createSelectComponent( Map<String, String> attributes, String lookup, VaadinMetawidget metawidget ) {

		Select select = new Select();

		// Add an empty choice (if nullable, and not required)

		if ( !WidgetBuilderUtils.needsEmptyLookupItem( attributes ) ) {
			select.setNullSelectionAllowed( false );
		}

		List<String> values = CollectionUtils.fromString( lookup );

		// May have alternate labels

		Map<String, String> labelsMap = null;
		String lookupLabels = attributes.get( LOOKUP_LABELS );

		if ( lookupLabels != null && !"".equals( lookupLabels ) ) {
			labelsMap = CollectionUtils.newHashMap( values, CollectionUtils.fromString( attributes.get( LOOKUP_LABELS ) ) );
		}

		// Lookup the Class
		//
		// (use TYPE, not ACTUAL_TYPE, because an Enum with a value will get a type of Enum$1)

		Class<?> clazz;
		String type = attributes.get( TYPE );

		if ( type != null ) {
			clazz = ClassUtils.niceForName( type );
		} else {
			clazz = null;
		}

		BindingConverter bindingConverter = metawidget.getWidgetProcessor( BindingConverter.class );

		for ( String value : values ) {

			Object convertedValue = value;

			if ( bindingConverter != null && clazz != null ) {
				convertedValue = bindingConverter.convertFromString( value, clazz );
			}

			select.addItem( convertedValue );

			if ( labelsMap != null ) {
				select.setItemCaption( convertedValue, labelsMap.get( value ) );
			}
		}

		if ( !WidgetBuilderUtils.needsEmptyLookupItem( attributes ) ) {
			select.setRequired( true );
		}

		return select;
	}
}
