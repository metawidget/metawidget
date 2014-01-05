// Metawidget
//
// For historical reasons, this file is licensed under the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html).
//
// Most other files in Metawidget are licensed under both the
// LGPL/EPL and a commercial license. See http://metawidget.org
// for details.

package org.metawidget.inspector.oval;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.Max;
import net.sf.oval.constraint.MaxLength;
import net.sf.oval.constraint.Min;
import net.sf.oval.constraint.MinLength;
import net.sf.oval.constraint.NotBlank;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;
import net.sf.oval.constraint.Range;

import org.metawidget.inspector.impl.BaseObjectInspector;
import org.metawidget.inspector.impl.BaseObjectInspectorConfig;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.util.CollectionUtils;

/**
 * Inspects annotations defined by OVal.
 * <p>
 * For more information on OVal, the Object Validation Framework for Java, see <a
 * href="http://oval.sourceforge.net">http://oval.sourceforge.net</a>.
 *
 * @author Renato Garcia
 */

public class OvalInspector
	extends BaseObjectInspector {

	//
	// Constructor
	//

	public OvalInspector() {

		this( new BaseObjectInspectorConfig() );
	}

	public OvalInspector( BaseObjectInspectorConfig config ) {

		super( config );
	}

	//
	// Protected methods
	//

	@Override
	protected Map<String, String> inspectProperty( Property property )
		throws Exception {

		Map<String, String> attributes = CollectionUtils.newHashMap();

		// NotNull

		if ( property.isAnnotationPresent( NotNull.class ) ) {
			attributes.put( REQUIRED, TRUE );
		}

		// NotEmpty

		if ( property.isAnnotationPresent( NotEmpty.class ) ) {
			attributes.put( REQUIRED, TRUE );
		}

		// NotBlank

		if ( property.isAnnotationPresent( NotBlank.class ) ) {
			attributes.put( REQUIRED, TRUE );
		}

		// Range

		Range range = property.getAnnotation( Range.class );

		if ( range != null ) {
			attributes.put( MAXIMUM_VALUE, niceValueOf( range.max() ) );
			attributes.put( MINIMUM_VALUE, niceValueOf( range.min() ) );
		}

		// Min

		Min min = property.getAnnotation( Min.class );

		if ( min != null ) {
			attributes.put( MINIMUM_VALUE, niceValueOf( min.value() ) );
		}

		// Max

		Max max = property.getAnnotation( Max.class );

		if ( max != null ) {
			attributes.put( MAXIMUM_VALUE, niceValueOf( max.value() ) );
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

		// MinLength

		MinLength minLength = property.getAnnotation( MinLength.class );

		if ( minLength != null ) {
			attributes.put( MINIMUM_LENGTH, String.valueOf( minLength.value() ) );
		}

		// MaxLength

		MaxLength maxLength = property.getAnnotation( MaxLength.class );

		if ( maxLength != null ) {
			attributes.put( MAXIMUM_LENGTH, String.valueOf( maxLength.value() ) );
		}

		return attributes;
	}

	//
	// Private members
	//

	/**
	 * @return the given double as a String. If the double is a whole number (ie. value % 1 == 0)
	 *         then the String does not contain a decimal place (ie. no '.0'). This allows it to be
	 *         parsed back again as an integer/long.
	 */

	private String niceValueOf( double value ) {

		if ( value % 1 == 0 ) {
			return String.valueOf( Double.valueOf( value ).intValue() );
		}

		return String.valueOf( value );
	}
}
