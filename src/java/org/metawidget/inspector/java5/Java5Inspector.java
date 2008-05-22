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

package org.metawidget.inspector.java5;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.metawidget.inspector.impl.AbstractPojoInspector;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * Inspector to look for Java 5 language features, such as enums and generics.
 *
 * @author Richard Kennard
 */

public class Java5Inspector
	extends AbstractPojoInspector
{
	//
	//
	// Private members
	//
	//

	private String[]														mExcludeProperties;

	//
	//
	// Constructor
	//
	//

	public Java5Inspector()
	{
		this( new Java5InspectorConfig() );
	}

	public Java5Inspector( Java5InspectorConfig config )
	{
		// Defensive copy
	
		String[] excludeProperties = config.getExcludeProperties();
		mExcludeProperties = new String[excludeProperties.length];
		System.arraycopy( excludeProperties, 0, mExcludeProperties, 0, excludeProperties.length );
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
		// Ignore some type methods, even though they are parameterized

		if ( ArrayUtils.contains( mExcludeProperties, property.getName() ) )
			return null;

		// Enums

		Map<String, String> attributes = CollectionUtils.newHashMap();

		Class<?> propertyClass = property.getPropertyClass();

		if ( propertyClass.isEnum() )
		{
			// Invoke 'magic' values method

			Method methodValues = propertyClass.getMethod( "values" );
			Enum<?>[] enums = (Enum[]) methodValues.invoke( property );

			// Construct lookup values

			List<String> lookup = CollectionUtils.newArrayList();
			List<String> lookupLabels = CollectionUtils.newArrayList();

			for( Enum<?> anEnum : enums )
			{
				// Convert enum values to their .name() form, not their .toString()
				// form, so that clients can use .valueOf() to convert them back

				lookup.add( anEnum.name() );
				lookupLabels.add( anEnum.toString() );
			}

			attributes.put( LOOKUP, CollectionUtils.toString( lookup ) );
			attributes.put( LOOKUP_LABELS, CollectionUtils.toString( lookupLabels ) );
		}

		// Generics

		Type type = property.getPropertyGenericType();

		if ( type instanceof ParameterizedType )
		{
			Type[] typeActuals = ((ParameterizedType) type).getActualTypeArguments();

			if ( typeActuals.length > 0 )
			{
				StringBuilder builder = new StringBuilder();

				for ( Type typeActual : typeActuals )
				{
					if ( builder.length() > 0 )
						builder.append( StringUtils.SEPARATOR_COMMA );

					if ( typeActual instanceof Class )
						builder.append( ((Class<?>) typeActual).getName() );
					else
						builder.append( typeActual.toString() );
				}

				attributes.put( PARAMETERIZED_TYPE, builder.toString() );
			}
		}

		return attributes;
	}
}
