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

package org.metawidget.statically.faces.component.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.faces.StaticFacesInspectionResultConstants.*;

import java.util.Date;
import java.util.Map;

import org.metawidget.statically.StaticXmlMetawidget;
import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.faces.component.ValueHolder;
import org.metawidget.statically.faces.component.html.CoreWidget;
import org.metawidget.util.ClassUtils;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;
import org.metawidget.widgetprocessor.iface.WidgetProcessorException;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class StandardConverterProcessor
	implements WidgetProcessor<StaticXmlWidget, StaticXmlMetawidget> {

	//
	// Public methods
	//

	public StaticXmlWidget processWidget( StaticXmlWidget widget, String elementName, Map<String, String> attributes, StaticXmlMetawidget metawidget ) {

		// Actions don't get converters

		if ( ACTION.equals( elementName ) ) {
			return widget;
		}

		// Ignore components that cannot have Converters

		if ( !( widget instanceof ValueHolder ) ) {
			return widget;
		}

		// Explicit Converter

		String converterId = attributes.get( FACES_CONVERTER );

		if ( converterId != null ) {
			( (ValueHolder) widget ).setConverter( converterId );
			return widget;
		}

		// Support for DateTimeConverter

		CoreWidget converter = null;

		// JSF does not appear to implicitly hook up DateTimeConverters without either an
		// explicit f:convertDateTime tag or a registered java.util.Date converter. Adding one
		// fixes both POSTback and display of read-only dates (otherwise JSF uses Date.toString)
		//
		// JSF *does* appear to implicitly hook up NumberConverters.

		String type = attributes.get( TYPE );
		Class<?> clazz = null;

		if ( type != null ) {

			clazz = ClassUtils.niceForName( type );

			if ( clazz != null ) {

				if ( clazz.isAssignableFrom( Date.class ) ) {
					converter = getDateTimeConverter( converter );
				}
			}
		}

		if ( attributes.containsKey( DATE_STYLE ) ) {
			converter = getDateTimeConverter( converter );
			converter.putAttribute( "dateStyle", attributes.get( DATE_STYLE ) );
		}

		if ( attributes.containsKey( DATETIME_PATTERN ) ) {
			converter = getDateTimeConverter( converter );
			converter.putAttribute( "pattern", attributes.get( DATETIME_PATTERN ) );
		}

		if ( attributes.containsKey( TIME_STYLE ) ) {
			converter = getDateTimeConverter( converter );
			converter.putAttribute( "timeStyle", attributes.get( TIME_STYLE ) );
		}

		if ( attributes.containsKey( TIME_ZONE ) ) {
			converter = getDateTimeConverter( converter );
			converter.putAttribute( "timeZone", attributes.get( TIME_ZONE ) );
		}

		if ( attributes.containsKey( DATETIME_TYPE ) ) {
			converter = getDateTimeConverter( converter );
			converter.putAttribute( "type", attributes.get( DATETIME_TYPE ) );
		}

		// Support for NumberConverter (so long as it's a number)

		if ( clazz == null || clazz.isAssignableFrom( Number.class ) || clazz.isPrimitive() ) {

			if ( attributes.containsKey( CURRENCY_CODE ) ) {
				converter = getNumberConverter( converter );
				converter.putAttribute( "currencyCode", attributes.get( CURRENCY_CODE ) );
			}

			if ( attributes.containsKey( CURRENCY_SYMBOL ) ) {
				converter = getNumberConverter( converter );
				converter.putAttribute( "currencySymbol", attributes.get( CURRENCY_SYMBOL ) );
			}

			if ( attributes.containsKey( NUMBER_USES_GROUPING_SEPARATORS ) ) {
				converter = getNumberConverter( converter );
				converter.putAttribute( "groupingUsed", attributes.get( NUMBER_USES_GROUPING_SEPARATORS ) );
			}

			if ( attributes.containsKey( MINIMUM_INTEGER_DIGITS ) ) {
				converter = getNumberConverter( converter );
				converter.putAttribute( "minIntegerDigits", attributes.get( MINIMUM_INTEGER_DIGITS ) );
			}

			if ( attributes.containsKey( MAXIMUM_INTEGER_DIGITS ) ) {
				converter = getNumberConverter( converter );
				converter.putAttribute( "maxIntegerDigits", attributes.get( MAXIMUM_INTEGER_DIGITS ) );
			}

			if ( attributes.containsKey( MINIMUM_FRACTIONAL_DIGITS ) ) {
				converter = getNumberConverter( converter );
				converter.putAttribute( "minFractionDigits", attributes.get( MINIMUM_FRACTIONAL_DIGITS ) );
			}

			if ( attributes.containsKey( MAXIMUM_FRACTIONAL_DIGITS ) ) {
				converter = getNumberConverter( converter );
				converter.putAttribute( "maxFractionDigits", attributes.get( MAXIMUM_FRACTIONAL_DIGITS ) );
			}

			if ( attributes.containsKey( NUMBER_PATTERN ) ) {
				converter = getNumberConverter( converter );
				converter.putAttribute( "pattern", attributes.get( NUMBER_PATTERN ) );
			}

			if ( attributes.containsKey( NUMBER_TYPE ) ) {
				converter = getNumberConverter( converter );
				converter.putAttribute( "type", attributes.get( NUMBER_TYPE ) );
			}
		}

		if ( converter != null ) {
			// Locale (applies to both DateTimeConverter and NumberConverter)

			if ( attributes.containsKey( LOCALE ) ) {
				converter.putAttribute( "locale", attributes.get( LOCALE ) );
			}

			widget.getChildren().add( converter );
		}

		return widget;
	}

	//
	// Private methods
	//

	private CoreWidget getDateTimeConverter( CoreWidget existingConverter ) {

		if ( existingConverter != null ) {
			if ( !( existingConverter instanceof ConvertDateTimeWidget ) ) {
				throw WidgetProcessorException.newException( "Unable to set date/time attributes on a " + existingConverter );
			}

			return existingConverter;
		}

		return new ConvertDateTimeWidget();
	}

	private CoreWidget getNumberConverter( CoreWidget existingConverter ) {

		if ( existingConverter != null ) {
			if ( !( existingConverter instanceof ConvertNumberWidget ) ) {
				throw WidgetProcessorException.newException( "Unable to set number attributes on a " + existingConverter );
			}

			return existingConverter;
		}

		return new ConvertNumberWidget();
	}

	//
	// Inner class
	//

	public static class ConvertDateTimeWidget
		extends CoreWidget {

		//
		// Constructor
		//

		public ConvertDateTimeWidget() {

			super( "convertDateTime" );
		}
	}

	public static class ConvertNumberWidget
		extends CoreWidget {

		//
		// Constructor
		//

		public ConvertNumberWidget() {

			super( "convertNumber" );
		}
	}
}
