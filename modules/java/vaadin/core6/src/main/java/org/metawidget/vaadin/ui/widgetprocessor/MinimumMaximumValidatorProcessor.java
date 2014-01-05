// Metawidget
//
// For historical reasons, this file is licensed under the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html).
//
// Most other files in Metawidget are licensed under both the
// LGPL/EPL and a commercial license. See http://metawidget.org
// for details.

package org.metawidget.vaadin.ui.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.util.ClassUtils;
import org.metawidget.util.WidgetBuilderUtils;
import org.metawidget.vaadin.ui.VaadinMetawidget;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;
import org.metawidget.widgetprocessor.iface.WidgetProcessorException;

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

	private static class MinimumMaximumValidator
		extends AbstractValidator {

		//
		// Private statics
		//

		private Class<?>	mNumberType;

		private Object		mMinimum;

		private Object		mMaximum;

		//
		// Constructor
		//

		public MinimumMaximumValidator( Class<?> numberType, String minimum, String maximum ) {

			super( "" );

			mNumberType = numberType;
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
				return !mNumberType.isPrimitive();
			}

			// Range check

			if ( byte.class.equals( mNumberType ) || Byte.class.equals( mNumberType ) ) {
				if ( mMinimum != null && (Byte) value < (Byte) mMinimum ) {
					return false;
				}
				if ( mMaximum != null && (Byte) value > (Byte) mMaximum ) {
					return false;
				}
			} else if ( short.class.equals( mNumberType ) || Short.class.equals( mNumberType ) ) {
				if ( mMinimum != null && (Short) value < (Short) mMinimum ) {
					return false;
				}
				if ( mMaximum != null && (Short) value > (Short) mMaximum ) {
					return false;
				}
			} else if ( int.class.equals( mNumberType ) || Integer.class.equals( mNumberType ) ) {
				if ( mMinimum != null && ((Number) value).intValue() < (Integer) mMinimum ) {
					return false;
				}
				if ( mMaximum != null && ((Number) value).intValue() > (Integer) mMaximum ) {
					return false;
				}
			} else if ( long.class.equals( mNumberType ) || Long.class.equals( mNumberType ) ) {
				if ( mMinimum != null && (Long) value < (Long) mMinimum ) {
					return false;
				}
				if ( mMaximum != null && (Long) value > (Long) mMaximum ) {
					return false;
				}
			} else if ( float.class.equals( mNumberType ) || Float.class.equals( mNumberType ) ) {
				if ( mMinimum != null && (Float) value < (Float) mMinimum ) {
					return false;
				}
				if ( mMaximum != null && (Float) value > (Float) mMaximum ) {
					return false;
				}
			} else if ( double.class.equals( mNumberType ) || Double.class.equals( mNumberType ) ) {
				if ( mMinimum != null && (Double) value < (Double) mMinimum ) {
					return false;
				}
				if ( mMaximum != null && (Double) value > (Double) mMaximum ) {
					return false;
				}
			} else {
				throw WidgetProcessorException.newException( mNumberType + " cannot be validated within min/max" );
			}

			return true;
		}
	}
}
