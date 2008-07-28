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

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.NumberConverter;

import org.metawidget.MetawidgetException;
import org.metawidget.faces.FacesUtils;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.BaseObjectInspector;
import org.metawidget.inspector.impl.BaseObjectInspectorConfig;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * Inspects annotations defined by Metawidget's Java Server Faces support (declared in this same package).
 *
 * @author Richard Kennard
 */

public class FacesInspector
	extends BaseObjectInspector
{
	//
	//
	// Constructor
	//
	//

	public FacesInspector()
	{
		this( new BaseObjectInspectorConfig() );
	}

	public FacesInspector( BaseObjectInspectorConfig config )
	{
		super( config );
	}

	//
	//
	// Protected methods
	//
	//

	@Override
	protected Map<String, String> inspectProperty( Property property, Object toInspect )
		throws Exception
	{
		Map<String, String> attributes = CollectionUtils.newHashMap();

		// ExpressionLookup

		UiFacesLookup expressionLookup = property.getAnnotation( UiFacesLookup.class );

		if ( expressionLookup != null )
			attributes.put( FACES_LOOKUP, expressionLookup.value() );

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

		// Attributes

		UiFacesAttribute facesAttribute = property.getAnnotation( UiFacesAttribute.class );
		UiFacesAttributes facesAttributes = property.getAnnotation( UiFacesAttributes.class );

		if ( facesAttribute != null || facesAttributes != null )
		{
			FacesContext context = FacesContext.getCurrentInstance();

			if ( context == null )
				throw InspectorException.newException( "FacesContext not available to FacesInspector" );

			Application application = context.getApplication();

			// UiFacesAttribute

			if ( facesAttribute != null )
			{
				putFacesAttribute( context, application, attributes, facesAttribute );
			}

			// UiFacesAttributes

			if ( facesAttributes != null )
			{
				for ( UiFacesAttribute nestedFacesAttribute : facesAttributes.value() )
				{
					putFacesAttribute( context, application, attributes, nestedFacesAttribute );
				}
			}
		}

		return attributes;
	}

	protected void putFacesAttribute( FacesContext context, Application application, Map<String, String> attributes, UiFacesAttribute facesAttribute )
	{
		// Optional condition

		String condition = facesAttribute.condition();

		if ( !"".equals( condition ))
		{
			if ( !FacesUtils.isValueReference( condition ))
				throw MetawidgetException.newException( "Condition '" + condition + "' is not of the form #{...}" );

			Object conditionResult = application.createValueBinding( condition ).getValue( context );

			if ( !Boolean.TRUE.equals( conditionResult ))
				return;
		}

		// Optionally expression-based

		String value = facesAttribute.value();

		if ( FacesUtils.isValueReference( value ) )
			value = StringUtils.quietValueOf( application.createValueBinding( value ).getValue( context ));

		// Set the value

		attributes.put( facesAttribute.name(), StringUtils.quietValueOf( value ));
	}
}
