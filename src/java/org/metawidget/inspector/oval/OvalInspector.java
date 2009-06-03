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
	extends BaseObjectInspector
{
	//
	// Constructor
	//

	public OvalInspector()
	{
		this( new BaseObjectInspectorConfig() );
	}

	public OvalInspector( BaseObjectInspectorConfig config )
	{
		super( config );
	}

	//
	// Protected methods
	//

	@Override
	protected Map<String, String> inspectProperty( Property property )
		throws Exception
	{
		Map<String, String> attributes = CollectionUtils.newHashMap();

		// NotNull

		if ( property.isAnnotationPresent( NotNull.class ) )
			attributes.put( REQUIRED, TRUE );

		// NotEmpty

		if ( property.isAnnotationPresent( NotEmpty.class ) )
			attributes.put( REQUIRED, TRUE );

		// NotBlank

		if ( property.isAnnotationPresent( NotBlank.class ) )
			attributes.put( REQUIRED, TRUE );

		// Range

		Range range = property.getAnnotation( Range.class );

		if ( range != null )
		{
			attributes.put( MAXIMUM_VALUE, String.valueOf( Math.round( range.max() ) ) );
			attributes.put( MINIMUM_VALUE, String.valueOf( Math.round( range.min() ) ) );
		}

		// Min

		Min min = property.getAnnotation( Min.class );

		if ( min != null )
			attributes.put( MINIMUM_VALUE, String.valueOf( min.value() ) );

		// Max

		Max max = property.getAnnotation( Max.class );

		if ( max != null )
			attributes.put( MAXIMUM_VALUE, String.valueOf( max.value() ) );

		// Length

		Length length = property.getAnnotation( Length.class );

		if ( length != null )
		{
			if ( length.min() > 0 )
				attributes.put( MINIMUM_LENGTH, String.valueOf( length.min() ) );

			if ( length.max() > 0 )
				attributes.put( MAXIMUM_LENGTH, String.valueOf( length.max() ) );
		}

		// MinLength

		MinLength minLength = property.getAnnotation( MinLength.class );

		if ( minLength != null )
			attributes.put( MINIMUM_LENGTH, String.valueOf( minLength.value() ) );

		// MaxLength

		MaxLength maxLength = property.getAnnotation( MaxLength.class );

		if ( maxLength != null )
			attributes.put( MAXIMUM_LENGTH, String.valueOf( maxLength.value() ) );

		return attributes;
	}
}
