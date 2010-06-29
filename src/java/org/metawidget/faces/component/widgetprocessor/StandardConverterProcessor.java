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

package org.metawidget.faces.component.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.faces.FacesInspectionResultConstants.*;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectMany;
import javax.faces.component.UISelectOne;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.NumberConverter;

import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.UIStub;
import org.metawidget.util.ClassUtils;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;
import org.metawidget.widgetprocessor.iface.WidgetProcessorException;

/**
 * WidgetProcessor to add standard JSF Converters to a UIComponent.
 *
 * @author Richard Kennard
 */

public class StandardConverterProcessor
	implements WidgetProcessor<UIComponent, UIMetawidget>, ConverterProcessor {

	//
	// Public methods
	//

	@SuppressWarnings( "deprecation" )
	@Override
	public UIComponent processWidget( UIComponent component, String elementName, Map<String, String> attributes, UIMetawidget metawidget ) {

		// Actions don't get converters

		if ( ACTION.equals( elementName ) ) {
			return component;
		}

		// Recurse into stubs...

		if ( component instanceof UIStub ) {
			// ...whose children have the same value binding as us...
			//
			// (this is important because choice of Converter is based off the attributes Map, and
			// if the value binding is different then all bets are off as to the accuracy of the
			// attributes)

			javax.faces.el.ValueBinding valueBinding = component.getValueBinding( "value" );

			if ( valueBinding != null ) {
				String expressionString = valueBinding.getExpressionString();

				List<UIComponent> children = component.getChildren();

				for ( UIComponent componentChild : children ) {
					javax.faces.el.ValueBinding childValueBinding = componentChild.getValueBinding( "value" );

					if ( childValueBinding == null ) {
						continue;
					}

					if ( !expressionString.equals( childValueBinding.getExpressionString() ) ) {
						continue;
					}

					// ...and apply the Converter to them

					processWidget( componentChild, elementName, attributes, metawidget );
				}
			}

			return component;
		}

		// Ignore components that cannot have Converters

		if ( !( component instanceof ValueHolder ) ) {
			return component;
		}

		ValueHolder valueHolder = (ValueHolder) component;
		valueHolder.setConverter( getConverter( valueHolder, attributes ) );

		return component;
	}

	public Converter getConverter( ValueHolder valueHolder, Map<String, String> attributes ) {

		// Use existing Converter (if any)

		Converter converter = valueHolder.getConverter();

		if ( converter != null ) {
			return converter;
		}

		// Create from id

		FacesContext context = FacesContext.getCurrentInstance();
		String converterId = attributes.get( FACES_CONVERTER_ID );

		if ( converterId != null ) {
			converter = context.getApplication().createConverter( converterId );
		}

		// Create from parameterized type (eg. a Date converter for List<Date>)

		else if ( valueHolder instanceof UISelectOne || valueHolder instanceof UISelectMany ) {
			String parameterizedType = attributes.get( PARAMETERIZED_TYPE );

			if ( parameterizedType != null ) {
				Class<?> parameterizedClass = ClassUtils.niceForName( parameterizedType );

				// The parameterized type might be null, or might not be concrete
				// enough to be instantiatable (eg. List<? extends Foo>)

				if ( parameterizedClass != null ) {
					converter = context.getApplication().createConverter( parameterizedClass );
				}
			}
		}

		// Support for DateTimeConverter

		if ( attributes.containsKey( DATE_STYLE ) ) {
			converter = getDateTimeConverter( converter );
			( (DateTimeConverter) converter ).setDateStyle( attributes.get( DATE_STYLE ) );
		}

		if ( attributes.containsKey( DATETIME_PATTERN ) ) {
			converter = getDateTimeConverter( converter );
			( (DateTimeConverter) converter ).setPattern( attributes.get( DATETIME_PATTERN ) );
		}

		if ( attributes.containsKey( TIME_STYLE ) ) {
			converter = getDateTimeConverter( converter );
			( (DateTimeConverter) converter ).setTimeStyle( attributes.get( TIME_STYLE ) );
		}

		if ( attributes.containsKey( TIME_ZONE ) ) {
			converter = getDateTimeConverter( converter );
			( (DateTimeConverter) converter ).setTimeZone( TimeZone.getTimeZone( attributes.get( TIME_ZONE ) ) );
		}

		if ( attributes.containsKey( DATETIME_TYPE ) ) {
			converter = getDateTimeConverter( converter );
			( (DateTimeConverter) converter ).setType( attributes.get( DATETIME_TYPE ) );
		}

		// Support for NumberConverter

		if ( attributes.containsKey( CURRENCY_CODE ) ) {
			converter = getNumberConverter( converter );
			( (NumberConverter) converter ).setCurrencyCode( attributes.get( CURRENCY_CODE ) );
		}

		if ( attributes.containsKey( CURRENCY_SYMBOL ) ) {
			converter = getNumberConverter( converter );
			( (NumberConverter) converter ).setCurrencySymbol( attributes.get( CURRENCY_SYMBOL ) );
		}

		if ( attributes.containsKey( NUMBER_USES_GROUPING_SEPARATORS ) ) {
			converter = getNumberConverter( converter );
			( (NumberConverter) converter ).setGroupingUsed( Boolean.parseBoolean( attributes.get( NUMBER_USES_GROUPING_SEPARATORS ) ) );
		}

		if ( attributes.containsKey( MINIMUM_INTEGER_DIGITS ) ) {
			converter = getNumberConverter( converter );
			( (NumberConverter) converter ).setMinIntegerDigits( Integer.parseInt( attributes.get( MINIMUM_INTEGER_DIGITS ) ) );
		}

		if ( attributes.containsKey( MAXIMUM_INTEGER_DIGITS ) ) {
			converter = getNumberConverter( converter );
			( (NumberConverter) converter ).setMaxIntegerDigits( Integer.parseInt( attributes.get( MAXIMUM_INTEGER_DIGITS ) ) );
		}

		if ( attributes.containsKey( MINIMUM_FRACTIONAL_DIGITS ) ) {
			converter = getNumberConverter( converter );
			( (NumberConverter) converter ).setMinFractionDigits( Integer.parseInt( attributes.get( MINIMUM_FRACTIONAL_DIGITS ) ) );
		}

		if ( attributes.containsKey( MAXIMUM_FRACTIONAL_DIGITS ) ) {
			converter = getNumberConverter( converter );
			( (NumberConverter) converter ).setMaxFractionDigits( Integer.parseInt( attributes.get( MAXIMUM_FRACTIONAL_DIGITS ) ) );
		}

		if ( attributes.containsKey( NUMBER_PATTERN ) ) {
			converter = getNumberConverter( converter );
			( (NumberConverter) converter ).setPattern( attributes.get( NUMBER_PATTERN ) );
		}

		if ( attributes.containsKey( NUMBER_TYPE ) ) {
			converter = getNumberConverter( converter );
			( (NumberConverter) converter ).setType( attributes.get( NUMBER_TYPE ) );
		}

		// Locale (applies to both DateTimeConverter and NumberConverter)

		if ( attributes.containsKey( LOCALE ) ) {
			if ( converter instanceof NumberConverter ) {
				( (NumberConverter) converter ).setLocale( new Locale( attributes.get( LOCALE ) ) );
			} else {
				converter = getDateTimeConverter( converter );
				( (DateTimeConverter) converter ).setLocale( new Locale( attributes.get( LOCALE ) ) );
			}
		}

		// Return it

		return converter;
	}

	//
	// Private methods
	//

	private DateTimeConverter getDateTimeConverter( Converter existingConverter ) {

		if ( existingConverter != null ) {
			if ( !( existingConverter instanceof DateTimeConverter ) ) {
				throw WidgetProcessorException.newException( "Unable to set date/time attributes on a " + existingConverter.getClass() );
			}

			return (DateTimeConverter) existingConverter;
		}

		// In case the application defines its own one

		FacesContext context = FacesContext.getCurrentInstance();
		DateTimeConverter dateTimeConverter = (DateTimeConverter) context.getApplication().createConverter( Date.class );

		if ( dateTimeConverter != null ) {
			return dateTimeConverter;
		}

		// The JSF default

		return new DateTimeConverter();
	}

	private NumberConverter getNumberConverter( Converter existingConverter ) {

		if ( existingConverter != null ) {
			if ( !( existingConverter instanceof NumberConverter ) ) {
				throw WidgetProcessorException.newException( "Unable to set number attributes on a " + existingConverter.getClass() );
			}

			return (NumberConverter) existingConverter;
		}

		// In case the application defines its own one

		FacesContext context = FacesContext.getCurrentInstance();
		NumberConverter numberConverter = (NumberConverter) context.getApplication().createConverter( Number.class );

		if ( numberConverter != null ) {
			return numberConverter;
		}

		// The JSF default

		return new NumberConverter();
	}
}
