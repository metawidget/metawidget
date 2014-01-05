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

package org.metawidget.inspector.hibernate.validator;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.hibernate.validator.HibernateValidatorInspectionResultConstants.*;

import java.lang.annotation.Annotation;
import java.util.Map;

import org.hibernate.validator.Email;
import org.hibernate.validator.Length;
import org.hibernate.validator.Max;
import org.hibernate.validator.Min;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Pattern;
import org.hibernate.validator.Range;
import org.metawidget.inspector.impl.BaseObjectInspector;
import org.metawidget.inspector.impl.BaseObjectInspectorConfig;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.util.CollectionUtils;

/**
 * Inspects annotations defined by Hibernate Validator.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class HibernateValidatorInspector
	extends BaseObjectInspector {

	//
	// Constructor
	//

	public HibernateValidatorInspector() {

		this( new BaseObjectInspectorConfig() );
	}

	public HibernateValidatorInspector( BaseObjectInspectorConfig config ) {

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

		try {
			@SuppressWarnings( "unchecked" )
			Class<? extends Annotation> digitsClass = (Class<? extends Annotation>) Class.forName( "org.hibernate.validator.Digits" );
			Object digitsAnnotation = property.getAnnotation( digitsClass );

			if ( digitsAnnotation != null ) {
				int integerDigits = (Integer) digitsClass.getMethod( "integerDigits" ).invoke( digitsAnnotation );

				if ( integerDigits > 0 ) {
					attributes.put( MAXIMUM_INTEGER_DIGITS, String.valueOf( integerDigits ) );
				}

				int fractionalDigits = (Integer) digitsClass.getMethod( "fractionalDigits" ).invoke( digitsAnnotation );

				if ( fractionalDigits > 0 ) {
					attributes.put( MAXIMUM_FRACTIONAL_DIGITS, String.valueOf( fractionalDigits ) );
				}
			}
		} catch ( ClassNotFoundException e ) {
			// Not all versions of Hibernate Validator support @Digits
		}

		// NotNull

		if ( property.isAnnotationPresent( NotNull.class ) ) {
			attributes.put( REQUIRED, TRUE );
		}

		// NotEmpty

		if ( property.isAnnotationPresent( NotEmpty.class ) ) {
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

		// Range

		Range range = property.getAnnotation( Range.class );

		if ( range != null ) {
			attributes.put( MINIMUM_VALUE, String.valueOf( range.min() ) );
			attributes.put( MAXIMUM_VALUE, String.valueOf( range.max() ) );
		}

		// Length

		Length length = property.getAnnotation( Length.class );

		if ( length != null ) {
			if ( length.min() > 0 ) {
				attributes.put( MINIMUM_LENGTH, String.valueOf( length.min() ) );
			}

			if ( length.max() > 0 ) {
				attributes.put( MAXIMUM_LENGTH, String.valueOf( length.max() ) );
			}
		}

		// Pattern

		Pattern pattern = property.getAnnotation( Pattern.class );

		if ( pattern != null ) {
			attributes.put( VALIDATION_PATTERN, String.valueOf( pattern.regex() ) );
		}

		// Email

		if ( property.isAnnotationPresent( Email.class ) ) {
			attributes.put( VALIDATION_EMAIL, TRUE );
		}

		return attributes;
	}
}
