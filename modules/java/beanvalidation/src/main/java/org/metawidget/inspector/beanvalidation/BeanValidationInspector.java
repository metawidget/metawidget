// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

package org.metawidget.inspector.beanvalidation;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.metawidget.inspector.impl.BaseObjectInspector;
import org.metawidget.inspector.impl.BaseObjectInspectorConfig;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.util.CollectionUtils;

/**
 * Inspects annotations defined by Bean Validation (JSR 303).
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class BeanValidationInspector
	extends BaseObjectInspector {

	//
	// Constructor
	//

	public BeanValidationInspector() {

		this( new BaseObjectInspectorConfig() );
	}

	public BeanValidationInspector( BaseObjectInspectorConfig config ) {

		super( config );
	}

	//
	// Protected methods
	//

	@Override
	protected Map<String, String> inspectProperty( Property property )
		throws Exception {

		Map<String, String> attributes = CollectionUtils.newHashMap();

		// Digits

		Digits digits = property.getAnnotation( Digits.class );

		if ( digits != null ) {
			int integerDigits = digits.integer();

			if ( integerDigits > 0 ) {
				attributes.put( MAXIMUM_INTEGER_DIGITS, String.valueOf( integerDigits ) );
			}

			int fractionalDigits = digits.fraction();

			if ( fractionalDigits > 0 ) {
				attributes.put( MAXIMUM_FRACTIONAL_DIGITS, String.valueOf( fractionalDigits ) );
			}
		}

		// NotNull

		if ( property.isAnnotationPresent( NotNull.class ) ) {
			attributes.put( REQUIRED, TRUE );
		}

		// Min

		Min min = property.getAnnotation( Min.class );

		if ( min != null ) {
			attributes.put( MINIMUM_VALUE, String.valueOf( min.value() ) );
		}

		// Max

		Max max = property.getAnnotation( Max.class );

		if ( max != null ) {
			attributes.put( MAXIMUM_VALUE, String.valueOf( max.value() ) );
		}

		// Size

		Size size = property.getAnnotation( Size.class );

		if ( size != null ) {
			if ( size.min() > 0 ) {
				attributes.put( MINIMUM_LENGTH, String.valueOf( size.min() ) );
			}

			if ( size.max() > 0 ) {
				attributes.put( MAXIMUM_LENGTH, String.valueOf( size.max() ) );
			}
		}

		// Pattern

		Pattern pattern = property.getAnnotation( Pattern.class );

		if ( pattern != null ) {
			attributes.put( VALIDATION_PATTERN, String.valueOf( pattern.regexp() ) );
		}

		return attributes;
	}
}
