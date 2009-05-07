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

import org.metawidget.inspector.impl.BaseObjectInspector;
import org.metawidget.inspector.impl.BaseObjectInspectorConfig;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * Inspector to look for Java 5 language features, such as enums and generics.
 *
 * @author Richard Kennard
 */

public class Java5Inspector
	extends BaseObjectInspector
{
	//
	// Constructor
	//

	public Java5Inspector()
	{
		this( new BaseObjectInspectorConfig() );
	}

	public Java5Inspector( BaseObjectInspectorConfig config )
	{
		super( config );
	}

	//
	// Protected methods
	//

	@Override
	protected boolean shouldInspectPropertyAsEntity( Property property )
	{
		return true;
	}

	@Override
	protected Map<String, String> inspectEntity( Class<?> classToInspect )
		throws Exception
	{
		Map<String, String> attributes = CollectionUtils.newHashMap();

		// Enums - classToInspect may an Enum type or an enum instance type (ie. Foo$1)

		if ( Enum.class.isAssignableFrom( classToInspect ) )
		{
			// Invoke 'magic' values method

			Method methodValues = classToInspect.getMethod( "values" );
			Enum<?>[] enums = (Enum[]) methodValues.invoke( classToInspect );

			// Construct lookup values

			List<String> lookup = CollectionUtils.newArrayList();
			List<String> lookupLabels = CollectionUtils.newArrayList();

			for ( Enum<?> anEnum : enums )
			{
				// Convert enum values to their .name() form, not their .toString()
				// form, so that clients can use .valueOf() to convert them back

				lookup.add( anEnum.name() );
				lookupLabels.add( anEnum.toString() );
			}

			attributes.put( LOOKUP, CollectionUtils.toString( lookup ) );
			attributes.put( LOOKUP_LABELS, CollectionUtils.toString( lookupLabels ) );

			// Put the type in too. This is not strictly necessary, as generally we
			// will be teamed up with PropertyTypeInspector, but we are used standalone
			// in the tutorial so we need to support this (contrived) use case.

			if ( classToInspect.isEnum() )
				attributes.put( TYPE, classToInspect.getName() );
			else
				attributes.put( TYPE, classToInspect.getSuperclass().getName() );
		}

		return attributes;
	}

	@Override
	protected Map<String, String> inspectProperty( Property property, Object toInspect )
		throws Exception
	{
		Map<String, String> attributes = CollectionUtils.newHashMap();

		// Generics

		Type type = property.getGenericType();

		if ( type instanceof ParameterizedType )
		{
			Type[] typeActuals = null;

			try
			{
				typeActuals = ( (ParameterizedType) type ).getActualTypeArguments();
			}
			catch ( Exception e )
			{
				// Android 1.1_r1 fails here with a ClassNotFoundException
			}

			if ( typeActuals != null && typeActuals.length > 0 )
			{
				StringBuilder builder = new StringBuilder();

				for ( Type typeActual : typeActuals )
				{
					if ( builder.length() > 0 )
						builder.append( StringUtils.SEPARATOR_COMMA );

					if ( typeActual instanceof Class )
						builder.append( ( (Class<?>) typeActual ).getName() );
					else
						builder.append( typeActual.toString() );
				}

				attributes.put( PARAMETERIZED_TYPE, builder.toString() );
			}
		}

		return attributes;
	}
}
