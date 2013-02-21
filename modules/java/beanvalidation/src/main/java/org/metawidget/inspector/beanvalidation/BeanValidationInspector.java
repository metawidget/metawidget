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
 * @author Richard Kennard
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
