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

import javax.faces.context.FacesContext;

import org.metawidget.faces.FacesUtils;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.BaseObjectInspector;
import org.metawidget.inspector.impl.Trait;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.InspectorUtils;
import org.w3c.dom.Element;

/**
 * Inspects annotations defined by Metawidget's Java Server Faces support (declared in this same
 * package).
 *
 * @author Richard Kennard
 */

public class FacesInspector
	extends BaseObjectInspector {

	//
	// Private members
	//

	private boolean	mInjectThis;

	//
	// Constructor
	//

	public FacesInspector() {

		this( new FacesInspectorConfig() );
	}

	public FacesInspector( FacesInspectorConfig config ) {

		super( config );

		mInjectThis = config.isInjectThis();
	}

	//
	// Protected methods
	//

	@Override
	protected Map<String, String> inspectParent( Object parentToInspect, Property propertyInParent )
		throws Exception {

		try {
			if ( mInjectThis ) {
				FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put( "this", parentToInspect );
			}

			return super.inspectParent( parentToInspect, propertyInParent );
		} finally {

			// 'this' should not be available outside of this particular evaluation

			if ( mInjectThis ) {
				FacesContext.getCurrentInstance().getExternalContext().getRequestMap().remove( "this" );
			}
		}
	}

	@Override
	protected void inspect( Object toInspect, Class<?> classToInspect, Element toAddTo )
		throws Exception {

		try {
			if ( mInjectThis ) {
				FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put( "this", toInspect );
			}

			super.inspect( toInspect, classToInspect, toAddTo );
		} finally {

			// 'this' should not be available outside of this particular evaluation

			if ( mInjectThis ) {
				FacesContext.getCurrentInstance().getExternalContext().getRequestMap().remove( "this" );
			}
		}
	}

	@Override
	protected Map<String, String> inspectTrait( Trait trait )
		throws Exception {

		Map<String, String> attributes = CollectionUtils.newHashMap();

		// UiFacesAttributes/UiFacesAttribute

		UiFacesAttributes facesAttributes = trait.getAnnotation( UiFacesAttributes.class );
		UiFacesAttribute facesAttribute = trait.getAnnotation( UiFacesAttribute.class );

		if ( facesAttributes == null && facesAttribute == null ) {
			return null;
		}

		// UiFacesAttribute

		if ( facesAttribute != null ) {
			evaluateAndPutExpression( attributes, facesAttribute );
		}

		// UiFacesAttributes

		if ( facesAttributes != null ) {
			for ( UiFacesAttribute nestedFacesAttribute : facesAttributes.value() ) {
				evaluateAndPutExpression( attributes, nestedFacesAttribute );
			}
		}

		return attributes;
	}

	@Override
	protected Map<String, String> inspectProperty( Property property )
		throws Exception {

		Map<String, String> attributes = CollectionUtils.newHashMap();

		// FacesLookup

		UiFacesLookup facesLookup = property.getAnnotation( UiFacesLookup.class );

		if ( facesLookup != null ) {
			putExpression( attributes, FACES_LOOKUP, facesLookup.value() );
		}

		UiFacesSuggest facesSuggest = property.getAnnotation( UiFacesSuggest.class );

		if ( facesSuggest != null ) {
			putExpression( attributes, FACES_SUGGEST, facesSuggest.value() );
		}

		// Component

		UiFacesComponent component = property.getAnnotation( UiFacesComponent.class );

		if ( component != null ) {
			attributes.put( FACES_COMPONENT, component.value() );
		}

		// AJAX

		UiFacesAjax ajax = property.getAnnotation( UiFacesAjax.class );

		if ( ajax != null ) {
			attributes.put( FACES_AJAX_EVENT, ajax.event() );
			putExpression( attributes, FACES_AJAX_ACTION, ajax.action() );
		}

		// Converters

		UiFacesConverter converter = property.getAnnotation( UiFacesConverter.class );

		if ( converter != null ) {
			attributes.put( FACES_CONVERTER_ID, converter.value() );
		}

		UiFacesNumberConverter numberConverter = property.getAnnotation( UiFacesNumberConverter.class );

		if ( numberConverter != null ) {
			if ( !"".equals( numberConverter.currencyCode() ) ) {
				attributes.put( CURRENCY_CODE, numberConverter.currencyCode() );
			}

			if ( !"".equals( numberConverter.currencySymbol() ) ) {
				attributes.put( CURRENCY_SYMBOL, numberConverter.currencySymbol() );
			}

			if ( numberConverter.groupingUsed() ) {
				attributes.put( NUMBER_USES_GROUPING_SEPARATORS, TRUE );
			}

			if ( numberConverter.minIntegerDigits() != -1 ) {
				attributes.put( MINIMUM_INTEGER_DIGITS, String.valueOf( numberConverter.minIntegerDigits() ) );
			}

			if ( numberConverter.maxIntegerDigits() != -1 ) {
				attributes.put( MAXIMUM_INTEGER_DIGITS, String.valueOf( numberConverter.maxIntegerDigits() ) );
			}

			if ( numberConverter.minFractionDigits() != -1 ) {
				attributes.put( MINIMUM_FRACTIONAL_DIGITS, String.valueOf( numberConverter.minFractionDigits() ) );
			}

			if ( numberConverter.maxFractionDigits() != -1 ) {
				attributes.put( MAXIMUM_FRACTIONAL_DIGITS, String.valueOf( numberConverter.maxFractionDigits() ) );
			}

			if ( !"".equals( numberConverter.locale() ) ) {
				attributes.put( LOCALE, numberConverter.locale() );
			}

			if ( !"".equals( numberConverter.pattern() ) ) {
				attributes.put( NUMBER_PATTERN, numberConverter.pattern() );
			}

			if ( !"".equals( numberConverter.type() ) ) {
				attributes.put( NUMBER_TYPE, numberConverter.type() );
			}
		}

		UiFacesDateTimeConverter dateTimeConverter = property.getAnnotation( UiFacesDateTimeConverter.class );

		if ( dateTimeConverter != null ) {
			if ( !"".equals( dateTimeConverter.dateStyle() ) ) {
				attributes.put( DATE_STYLE, dateTimeConverter.dateStyle() );
			}

			if ( !"".equals( dateTimeConverter.locale() ) ) {
				attributes.put( LOCALE, dateTimeConverter.locale() );
			}

			if ( !"".equals( dateTimeConverter.pattern() ) ) {
				attributes.put( DATETIME_PATTERN, dateTimeConverter.pattern() );
			}

			if ( !"".equals( dateTimeConverter.timeStyle() ) ) {
				attributes.put( TIME_STYLE, dateTimeConverter.timeStyle() );
			}

			if ( !"".equals( dateTimeConverter.timeZone() ) ) {
				attributes.put( TIME_ZONE, dateTimeConverter.timeZone() );
			}

			if ( !"".equals( dateTimeConverter.type() ) ) {
				attributes.put( DATETIME_TYPE, dateTimeConverter.type() );
			}
		}

		return attributes;
	}

	//
	// Private methods
	//

	/**
	 * Put a 'Faces-side' expression into an attribute.
	 */

	private void putExpression( Map<String, String> attributes, String attributeName, String expression ) {

		if ( "".equals( expression ) ) {
			return;
		}

		// Sanity checks

		if ( !FacesUtils.isExpression( expression ) ) {
			throw InspectorException.newException( "Expression '" + expression + "' (for '" + attributeName + "') is not of the form #{...}" );
		}

		if ( mInjectThis && FacesUtils.unwrapExpression( expression ).startsWith( "this.") ) {
			throw InspectorException.newException( "Expression '" + expression + "' (for '" + attributeName + "') must not contain 'this' (see Metawidget Reference Guide)" );
		}

		// Put the expression

		attributes.put( attributeName, expression );
	}

	/**
	 * Evaluate an 'Inspector-side' expression and put the result into an attribute.
	 */

	@SuppressWarnings( "deprecation" )
	private void evaluateAndPutExpression( Map<String, String> attributes, UiFacesAttribute facesAttribute ) {

		// Sanity checks

		FacesContext context = FacesContext.getCurrentInstance();

		if ( context == null ) {
			throw InspectorException.newException( "FacesContext not available to FacesInspector" );
		}

		String expression = facesAttribute.expression();

		if ( "".equals( expression ) ) {
			return;
		}

		if ( !FacesUtils.isExpression( expression ) ) {
			throw InspectorException.newException( "Expression '" + expression + "' is not of the form #{...}" );
		}

		if ( !mInjectThis && FacesUtils.unwrapExpression( expression ).startsWith( "this.") ) {
			throw InspectorException.newException( "Expression for '" + expression + "' contains 'this', but " + FacesInspectorConfig.class.getSimpleName() + ".setInjectThis is 'false'" );
		}

		// Evaluate the expression

		Object value;

		try {
			value = context.getApplication().createValueBinding( expression ).getValue( context );

		} catch ( Exception e ) {

			// We have found it helpful to include the actual expression we were trying to evaluate

			throw InspectorException.newException( "Unable to getValue of " + expression, e );
		}

		if ( value == null ) {
			return;
		}

		// Put the result

		for ( String attributeName : facesAttribute.name() ) {
			InspectorUtils.putAttributeValue( attributes, attributeName, value );
		}
	}
}
