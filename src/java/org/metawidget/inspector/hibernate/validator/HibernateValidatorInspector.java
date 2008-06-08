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

package org.metawidget.inspector.hibernate.validator;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.hibernate.validator.Digits;
import org.hibernate.validator.Length;
import org.hibernate.validator.Max;
import org.hibernate.validator.Min;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;
import org.metawidget.inspector.impl.BasePropertyInspector;
import org.metawidget.inspector.impl.BasePropertyInspectorConfig;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.util.CollectionUtils;

/**
 * Inspects annotations defined by Hibernate Validator.
 *
 * @author Richard Kennard
 */

public class HibernateValidatorInspector
	extends BasePropertyInspector
{
	//
	//
	// Constructor
	//
	//

	public HibernateValidatorInspector()
	{
		this( new BasePropertyInspectorConfig() );
	}

	public HibernateValidatorInspector( BasePropertyInspectorConfig config )
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

		// Digits

		Digits digits = property.getAnnotation( Digits.class );

		if ( digits != null )
		{
			if ( digits.integerDigits() > 0 )
				attributes.put( MAXIMUM_INTEGER_DIGITS, String.valueOf( digits.integerDigits() ));

			if ( digits.fractionalDigits() > 0 )
				attributes.put( MAXIMUM_FRACTIONAL_DIGITS, String.valueOf( digits.fractionalDigits() ));
		}

		// NotNull

		if ( property.isAnnotationPresent( NotNull.class ) )
			attributes.put( REQUIRED, TRUE );

		// NotEmpty

		if ( property.isAnnotationPresent( NotEmpty.class ) )
			attributes.put( REQUIRED, TRUE );

		// Min

		Min min = property.getAnnotation( Min.class );

		if ( min != null )
			attributes.put( MINIMUM_VALUE, String.valueOf( min.value() ));

		// Max

		Max max = property.getAnnotation( Max.class );

		if ( max != null )
			attributes.put( MAXIMUM_VALUE, String.valueOf( max.value() ));

		// Length

		Length length = property.getAnnotation( Length.class );

		if ( length != null )
		{
			if ( length.min() > 0 )
				attributes.put( MINIMUM_LENGTH, String.valueOf( length.min() ));

			if ( length.max() > 0 )
				attributes.put( MAXIMUM_LENGTH, String.valueOf( length.max() ));
		}

		return attributes;
	}
}
