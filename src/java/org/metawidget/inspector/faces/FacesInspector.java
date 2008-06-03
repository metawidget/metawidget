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

package org.metawidget.inspector.faces;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.faces.FacesInspectionResultConstants.*;

import java.util.Map;

import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.NumberConverter;

import org.metawidget.inspector.InspectorException;
import org.metawidget.inspector.impl.AbstractPropertyInspector;
import org.metawidget.inspector.impl.AbstractPropertyInspectorConfig;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.CollectionUtils;

/**
 * Inspects annotations defined by Metawidget's Java Server Faces support (declared in this same package).
 *
 * @author Richard Kennard
 */

public class FacesInspector
	extends AbstractPropertyInspector
{
	//
	//
	// Constructor
	//
	//

	public FacesInspector()
	{
		this( new AbstractPropertyInspectorConfig() );
	}

	public FacesInspector( AbstractPropertyInspectorConfig config )
	{
		super( config );
	}

	//
	//
	// Protected methods
	//
	//

	@Override
	protected Map<String, String> inspect( Property property, Object toInspect )
		throws Exception
	{
		Map<String, String> attributes = CollectionUtils.newHashMap();

		// ExpressionLookup

		UiFacesLookup expressionLookup = property.getAnnotation( UiFacesLookup.class );

		if ( expressionLookup != null )
		{
			if ( !expressionLookup.onlyIfNull() || property.read( toInspect ) == null )
			{
				attributes.put( FACES_LOOKUP, expressionLookup.value() );
			}
		}

		// Component

		UiFacesComponent component = property.getAnnotation( UiFacesComponent.class );

		if ( component != null )
			attributes.put( FACES_COMPONENT, component.value() );

		// Converters

		UiFacesConverter converter = property.getAnnotation( UiFacesConverter.class );

		if ( converter != null )
			attributes.put( FACES_CONVERTER_ID, converter.value() );

		UiFacesNumberConverter numberConverter = property.getAnnotation( UiFacesNumberConverter.class );

		if ( numberConverter != null )
		{
			if ( attributes.containsKey( FACES_CONVERTER_ID ))
				throw InspectorException.newException( "Property " + property + " cannot define both UiFacesNumberConverter and another converter" );

			attributes.put( FACES_CONVERTER_CLASS, NumberConverter.class.getName() );

			if ( !"".equals( numberConverter.currencyCode() ))
				attributes.put( CURRENCY_CODE, numberConverter.currencyCode() );

			if ( !"".equals( numberConverter.currencySymbol() ))
				attributes.put( CURRENCY_SYMBOL, numberConverter.currencySymbol() );

			if ( numberConverter.groupingUsed() )
				attributes.put( NUMBER_USES_GROUPING_SEPARATORS, TRUE );

			if ( !"".equals( numberConverter.currencyCode() ))
				attributes.put( CURRENCY_CODE, numberConverter.currencyCode() );

			if ( numberConverter.minIntegerDigits() != -1 )
				attributes.put( MINIMUM_INTEGER_DIGITS, String.valueOf( numberConverter.minIntegerDigits() ));

			if ( numberConverter.maxIntegerDigits() != -1 )
				attributes.put( MAXIMUM_INTEGER_DIGITS, String.valueOf( numberConverter.maxIntegerDigits() ));

			if ( numberConverter.minFractionDigits() != -1 )
				attributes.put( MINIMUM_FRACTIONAL_DIGITS, String.valueOf( numberConverter.minFractionDigits() ));

			if ( numberConverter.maxFractionDigits() != -1 )
				attributes.put( MAXIMUM_FRACTIONAL_DIGITS, String.valueOf( numberConverter.maxFractionDigits() ));

			if ( !"".equals( numberConverter.locale() ))
				attributes.put( LOCALE, numberConverter.locale() );

			if ( !"".equals( numberConverter.pattern() ))
				attributes.put( NUMBER_PATTERN, numberConverter.pattern() );

			if ( !"".equals( numberConverter.type() ))
				attributes.put( NUMBER_TYPE, numberConverter.type() );
		}

		UiFacesDateTimeConverter dateTimeConverter = property.getAnnotation( UiFacesDateTimeConverter.class );

		if ( dateTimeConverter != null )
		{
			if ( attributes.containsKey( FACES_CONVERTER_CLASS ) || attributes.containsKey( FACES_CONVERTER_ID ))
				throw InspectorException.newException( "Property " + property + " cannot define both UiFacesDateTimeConverter and another converter" );

			attributes.put( FACES_CONVERTER_CLASS, DateTimeConverter.class.getName() );

			if ( !"".equals( dateTimeConverter.dateStyle() ))
				attributes.put( DATE_STYLE, dateTimeConverter.dateStyle() );

			if ( !"".equals( dateTimeConverter.locale() ))
				attributes.put( LOCALE, dateTimeConverter.locale() );

			if ( !"".equals( dateTimeConverter.pattern() ))
				attributes.put( DATETIME_PATTERN, dateTimeConverter.pattern() );

			if ( !"".equals( dateTimeConverter.timeStyle() ))
				attributes.put( TIME_STYLE, dateTimeConverter.timeStyle() );

			if ( !"".equals( dateTimeConverter.timeZone() ))
				attributes.put( TIME_ZONE, dateTimeConverter.timeZone() );

			if ( !"".equals( dateTimeConverter.type() ))
				attributes.put( DATETIME_TYPE, dateTimeConverter.type() );
		}

		// NotHiddenInRole

		UiFacesNotHiddenInRole notHiddenInRole = property.getAnnotation( UiFacesNotHiddenInRole.class );

		if ( notHiddenInRole != null )
			attributes.put( FACES_NOT_HIDDEN_IN_ROLE, ArrayUtils.toString( notHiddenInRole.value() ) );

		// NotReadOnlyInRole

		UiFacesNotReadOnlyInRole notReadOnlyInRole = property.getAnnotation( UiFacesNotReadOnlyInRole.class );

		if ( notReadOnlyInRole != null )
			attributes.put( FACES_NOT_READ_ONLY_IN_ROLE, ArrayUtils.toString( notReadOnlyInRole.value() ) );

		return attributes;
	}
}
