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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.context.FacesContext;

import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.BaseObjectInspector;
import org.metawidget.inspector.impl.Trait;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.InspectorUtils;
import org.metawidget.util.simple.StringUtils;
import org.w3c.dom.Element;

/**
 * Inspects annotations defined by Metawidget's Java Server Faces support (declared in this same
 * package).
 *
 * @author Richard Kennard
 */

// REFACTOR: rename this to FacesAnnotationInspector

public class FacesInspector
	extends BaseObjectInspector {

	//
	// Private statics
	//

	private final static String	THIS_ATTRIBUTE				= "this";

	private final static String	UNDERSCORE_THIS_ATTRIBUTE	= "_this";

	//
	// Private members
	//

	private boolean				mInjectThis;

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

		if ( mInjectThis ) {
			Map<String, Object> requestMap = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();

			requestMap.put( THIS_ATTRIBUTE, parentToInspect );
			requestMap.put( UNDERSCORE_THIS_ATTRIBUTE, parentToInspect );
		}

		return super.inspectParent( parentToInspect, propertyInParent );

		// THIS_ATTRIBUTE and UNDERSCORE_THIS_ATTRIBUTE will be cleaned up by inspect
	}

	@Override
	protected void inspect( Object toInspect, Class<?> classToInspect, Element toAddTo )
		throws Exception {

		// Sanity checks

		FacesContext context = FacesContext.getCurrentInstance();

		if ( context == null ) {
			throw InspectorException.newException( "FacesContext not available to FacesInspector" );
		}

		Map<String, Object> requestMap = context.getExternalContext().getRequestMap();

		try {
			if ( mInjectThis ) {
				requestMap.put( THIS_ATTRIBUTE, toInspect );
				requestMap.put( UNDERSCORE_THIS_ATTRIBUTE, toInspect );
			}

			super.inspect( toInspect, classToInspect, toAddTo );
		} finally {

			// THIS_ATTRIBUTE and UNDERSCORE_THIS_ATTRIBUTE should not be available outside of our
			// particular evaluation

			if ( mInjectThis ) {
				requestMap.remove( THIS_ATTRIBUTE );
				requestMap.remove( UNDERSCORE_THIS_ATTRIBUTE );
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

		if ( !isExpression( expression ) ) {
			throw InspectorException.newException( "Expression '" + expression + "' (for '" + attributeName + "') is not of the form #{...}" );
		}

		if ( mInjectThis ) {
			String unwrappedExpression = unwrapExpression( expression );

			if ( unwrappedExpression.startsWith( THIS_ATTRIBUTE + StringUtils.SEPARATOR_DOT ) ) {
				throw InspectorException.newException( "Expression '" + expression + "' (for '" + attributeName + "') must not contain '" + THIS_ATTRIBUTE + "' (see Metawidget Reference Guide)" );
			}

			if ( unwrappedExpression.startsWith( UNDERSCORE_THIS_ATTRIBUTE + StringUtils.SEPARATOR_DOT ) ) {
				throw InspectorException.newException( "Expression '" + expression + "' (for '" + attributeName + "') must not contain '" + UNDERSCORE_THIS_ATTRIBUTE + "' (see Metawidget Reference Guide)" );
			}
		}

		// Put the expression

		attributes.put( attributeName, expression );
	}

	/**
	 * Evaluate an 'Inspector-side' expression and put the result into an attribute.
	 */

	@SuppressWarnings( "deprecation" )
	private void evaluateAndPutExpression( Map<String, String> attributes, UiFacesAttribute facesAttribute ) {

		String expression = facesAttribute.expression();

		if ( "".equals( expression ) ) {
			return;
		}

		if ( !isExpression( expression ) ) {
			throw InspectorException.newException( "Expression '" + expression + "' is not of the form #{...}" );
		}

		if ( !mInjectThis ) {
			String unwrappedExpression = unwrapExpression( expression );

			if ( unwrappedExpression.startsWith( THIS_ATTRIBUTE + StringUtils.SEPARATOR_DOT ) ) {
				throw InspectorException.newException( "Expression for '" + expression + "' contains '" + THIS_ATTRIBUTE + "', but " + FacesInspectorConfig.class.getSimpleName() + ".setInjectThis is 'false'" );
			}

			if ( unwrappedExpression.startsWith( UNDERSCORE_THIS_ATTRIBUTE + StringUtils.SEPARATOR_DOT ) ) {
				throw InspectorException.newException( "Expression for '" + expression + "' contains '" + UNDERSCORE_THIS_ATTRIBUTE + "', but " + FacesInspectorConfig.class.getSimpleName() + ".setInjectThis is 'false'" );
			}
		}

		// Evaluate the expression

		Object value;

		try {
			FacesContext context = FacesContext.getCurrentInstance();
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

	/**
	 * Return <code>true</code> if the specified value conforms to the syntax requirements of a
	 * value binding expression.
	 * <p>
	 * This method is a mirror of the one in <code>UIComponentTag.isValueReference</code>, but that
	 * one is deprecated so may be removed in the future.
	 * <p>
	 * <em>Note: this code copied from org.metawidget.faces.FacesUtils. We do not want that dependency, because
	 * annotations may be deployed to a back-end tier</em>
	 *
	 * @param value
	 *            The value to evaluate
	 * @throws NullPointerException
	 *             if <code>value</code> is <code>null</code>
	 */

	private boolean isExpression( String value ) {

		return PATTERN_EXPRESSION.matcher( value ).matches();
	}

	/**
	 * <em>Note: this code copied from org.metawidget.faces.FacesUtils. We do not want that dependency, because
	 * annotations may be deployed to a back-end tier</em>
	 *
	 * @return the original String, not wrapped in #{...}. If the original String was not wrapped,
	 *         returns the original String
	 */

	private String unwrapExpression( String value ) {

		Matcher matcher = PATTERN_EXPRESSION.matcher( value );

		if ( !matcher.matches() ) {
			return value;
		}

		return matcher.group( 3 );
	}

	//
	// Private statics
	//

	/**
	 * Match #{...} and ${...}. This mirrors the approach in
	 * <code>UIComponentTag.isValueReference</code>, but that one is deprecated so may be removed in
	 * the future.
	 * <p>
	 * Like <code>UIComponentTag.isValueReference</code> we allow nested #{...} blocks, because this
	 * can still be a legitimate value reference:
	 * <p>
	 * <code>
	 * #{!empty bar ? '' : '#{foo}'}
	 * </code>
	 * <p>
	 * <em>Note: this code copied from org.metawidget.faces.FacesUtils. We do not want that dependency, because
	 * annotations may be deployed to a back-end tier</em>
	 */

	private static final Pattern	PATTERN_EXPRESSION	= Pattern.compile( "((#|\\$)\\{)(.*)(\\})" );
}
