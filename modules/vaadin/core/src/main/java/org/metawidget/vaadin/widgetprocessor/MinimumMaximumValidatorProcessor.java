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

package org.metawidget.vaadin.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.util.ClassUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.vaadin.VaadinMetawidget;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;

/**
 * WidgetProcessor that adds a minimum/maximum validator to a Vaadin Component.
 *
 * @author Loghman Barari
 */

public class MinimumMaximumValidatorProcessor
	implements WidgetProcessor<Component, VaadinMetawidget> {

	//
	// Public methods
	//

	public Component processWidget( Component component, String elementName, Map<String, String> attributes, VaadinMetawidget metawidget ) {

		// If field has a minimum or maximum value...

		if ( !( component instanceof AbstractField ) ) {
			return component;
		}

		String minimumValue = attributes.get( MINIMUM_VALUE );
		String maximumValue = attributes.get( MAXIMUM_VALUE );

		if ( ( minimumValue == null || "".equals( minimumValue ) ) && ( maximumValue == null || "".equals( maximumValue ) ) ) {
			return component;
		}

		Class<?> clazz = WidgetBuilderUtils.getActualClassOrType( attributes, null );

		if ( clazz == null ) {
			return component;
		}

		if ( !clazz.isPrimitive() && !Number.class.isAssignableFrom( clazz ) ) {
			return component;
		}

		if ( char.class.equals( clazz ) || boolean.class.equals( clazz ) ) {
			return component;
		}

		// ...apply a validator

		( (AbstractField) component ).addValidator( new MinimumMaximumValidator( clazz, minimumValue, maximumValue ) );

		return component;
	}

	//
	// Inner Class
	//

	/* package private */static class MinimumMaximumValidator
			extends AbstractValidator {

		//
		// Private statics
		//

		private Class<? extends Number>	mValueType;

		private Object					mMinimum;

		private Object					mMaximum;

		//
		// Constructor
		//

		public MinimumMaximumValidator( Class<?> numberType, String minimum, String maximum ) {

			super( "" );

			mMinimum = ClassUtils.parseNumber( numberType, minimum );
			mMaximum = ClassUtils.parseNumber( numberType, maximum );

			if ( mMinimum == null ) {
				setErrorMessage( "Must be less than " + mMaximum );
			} else if ( mMaximum == null ) {
				setErrorMessage( "Must be greater than " + mMinimum );
			} else {
				setErrorMessage( "Must be between " + mMinimum + " and " + mMaximum );
			}

		}

		//
		// Public Methods
		//

		public boolean isValid( Object value ) {

			if ( value == null ) {
				return true;
			}

			if ( byte.class.equals( mValueType ) ) {
				if ( mMinimum != null && (Byte) value < (Byte) mMinimum ) {
					return false;
				}
				if ( mMaximum != null && (Byte) value > (Byte) mMaximum ) {
					return false;
				}
			}

			if ( short.class.equals( mValueType ) ) {
				if ( mMinimum != null && (Short) value < (Short) mMinimum ) {
					return false;
				}
				if ( mMaximum != null && (Short) value > (Short) mMaximum ) {
					return false;
				}
			}

			if ( int.class.equals( mValueType ) ) {
				if ( mMinimum != null && (Integer) value < (Integer) mMinimum ) {
					return false;
				}
				if ( mMaximum != null && (Integer) value > (Integer) mMaximum ) {
					return false;
				}
			}

			if ( long.class.equals( mValueType ) ) {
				if ( mMinimum != null && (Long) value < (Long) mMinimum ) {
					return false;
				}
				if ( mMaximum != null && (Long) value > (Long) mMaximum ) {
					return false;
				}
			}

			if ( float.class.equals( mValueType ) ) {
				if ( mMinimum != null && (Float) value < (Float) mMinimum ) {
					return false;
				}
				if ( mMaximum != null && (Float) value > (Float) mMaximum ) {
					return false;
				}
			}

			if ( double.class.equals( mValueType ) ) {
				if ( mMinimum != null && (Double) value < (Double) mMinimum ) {
					return false;
				}
				if ( mMaximum != null && (Double) value > (Double) mMaximum ) {
					return false;
				}
			}

			return true;
		}
	}
}
