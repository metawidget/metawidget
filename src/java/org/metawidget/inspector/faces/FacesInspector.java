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

import org.metawidget.faces.FacesUtils;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.BaseObjectInspector;
import org.metawidget.inspector.impl.BaseObjectInspectorConfig;
import org.metawidget.inspector.impl.Trait;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * Inspects annotations defined by Metawidget's Java Server Faces support (declared in this same package).
 *
 * @author Richard Kennard
 */

@SuppressWarnings( "deprecation" )
public class FacesInspector
	extends BaseObjectInspector
{
	//
	// Constructor
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
	// Protected methods
	//

	@Override
	protected Map<String, String> inspectTrait( Trait trait )
		throws Exception
	{
		// UiFacesAttributes/UiFacesAttribute

		UiFacesAttributes facesAttributes = trait.getAnnotation( UiFacesAttributes.class );
		UiFacesAttribute facesAttribute = trait.getAnnotation( UiFacesAttribute.class );

		if ( facesAttributes == null && facesAttribute == null )
			return null;

		Map<String, String> attributes = CollectionUtils.newHashMap();
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

		return attributes;
	}

	@Override
	protected Map<String, String> inspectProperty( Property property )
		throws Exception
	{
		Map<String, String> attributes = CollectionUtils.newHashMap();

		// ExpressionLookup

		UiFacesLookup expressionLookup = property.getAnnotation( UiFacesLookup.class );

		if ( expressionLookup != null )
			attributes.put( FACES_LOOKUP, expressionLookup.value() );

		UiFacesSuggest expressionSuggest = property.getAnnotation( UiFacesSuggest.class );

		if ( expressionSuggest != null )
			attributes.put( FACES_SUGGEST, expressionSuggest.value() );

		// Component

		UiFacesComponent component = property.getAnnotation( UiFacesComponent.class );

		if ( component != null )
			attributes.put( FACES_COMPONENT, component.value() );

		// AJAX

		UiFacesAjax ajax = property.getAnnotation( UiFacesAjax.class );

		if ( ajax != null )
			attributes.put( FACES_AJAX_EVENT, ajax.event() );

		// Converters

		UiFacesConverter converter = property.getAnnotation( UiFacesConverter.class );

		if ( converter != null )
			attributes.put( FACES_CONVERTER_ID, converter.value() );

		UiFacesNumberConverter numberConverter = property.getAnnotation( UiFacesNumberConverter.class );

		if ( numberConverter != null )
		{
			if ( !"".equals( numberConverter.currencyCode() ))
				attributes.put( CURRENCY_CODE, numberConverter.currencyCode() );

			if ( !"".equals( numberConverter.currencySymbol() ))
				attributes.put( CURRENCY_SYMBOL, numberConverter.currencySymbol() );

			if ( numberConverter.groupingUsed() )
				attributes.put( NUMBER_USES_GROUPING_SEPARATORS, TRUE );

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

		return attributes;
	}

	protected void putFacesAttribute( FacesContext context, Application application, Map<String, String> attributes, UiFacesAttribute facesAttribute )
	{
		String expression = facesAttribute.expression();

		if ( !FacesUtils.isExpression( expression ) )
			throw InspectorException.newException( "Expression '" + expression + "' is not of the form #{...}" );

		Object value = application.createValueBinding( expression ).getValue( context );

		if ( value == null )
			return;

		attributes.put( facesAttribute.name(), StringUtils.quietValueOf( value ));
	}
}
