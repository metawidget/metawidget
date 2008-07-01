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

package org.metawidget.inspector.impl.propertystyle.groovy;

import groovy.lang.GroovySystem;
import groovy.lang.MetaBeanProperty;
import groovy.lang.MetaClass;
import groovy.lang.MetaMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.codehaus.groovy.reflection.CachedField;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.propertystyle.BaseProperty;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.inspector.impl.propertystyle.PropertyStyle;
import org.metawidget.util.CollectionUtils;

/**
 * PropertyStyle for Groovy-style properties.
 * <p>
 * Groovy-style properties can <em>almost</em> be handled using <code>JavaBeanPropertyStyle</code>,
 * because the Groovy compiler automatically generates JavaBean-style getters and setters.
 * Unfortunately, it does not also copy any annotations defined on the property to the generated
 * getter and setters. This <code>PropertyStyle</code> is designed to access those annotations.
 *
 * @author Richard Kennard
 */

public class GroovyPropertyStyle
	implements PropertyStyle
{
	//
	//
	// Private members
	//
	//

	private Class<?>[]				mExcludeTypes;

	//
	//
	// Constructor
	//
	//

	public GroovyPropertyStyle()
	{
		mExcludeTypes = getExcludeTypes();
	}

	//
	//
	// Public methods
	//
	//

	/**
	 * Returns properties sorted by name.
	 */

	public Map<String, Property> getProperties( Class<?> clazz )
	{
		// TreeMap so that returns alphabetically sorted properties

		Map<String, Property> propertiesToReturn = CollectionUtils.newTreeMap();

		// Iterate over all Groovy properties.
		//
		// Note: we do not cache this like we do JavaBean properties, because presumably
		// these can change dynamically?

		@SuppressWarnings( "unchecked" )
		List<MetaBeanProperty> properties = GroovySystem.getMetaClassRegistry().getMetaClass( clazz ).getProperties();

		property: for ( MetaBeanProperty property : properties )
		{
			Class<?> type = property.getType();

			// Ignore certain types

			for( Class<?> excludeType : mExcludeTypes )
			{
				if ( excludeType.isAssignableFrom( type ))
					continue property;
			}

			propertiesToReturn.put( property.getName(), new GroovyProperty( property, clazz ) );
		}

		return propertiesToReturn;
	}

	//
	//
	// Protected methods
	//
	//

	/**
	 * Array of property return types to exclude when searching for properties. Subtypes of the
	 * specified exclude types are also excluded.
	 * <p>
	 * This can be useful when the convention or base class define properties that are
	 * framework-specific, and should be filtered out from 'real' business model properties.
	 * <p>
	 * By default, filters out any types that return <code>java.lang.Class</code> and
	 * <code>groovy.lang.MetaClass</code>.
	 */

	protected Class<?>[] getExcludeTypes()
	{
		return new Class<?>[] { Class.class, MetaClass.class };
	}

	//
	//
	// Inner classes
	//
	//

	/**
	 * Groovy-based property.
	 */

	private static class GroovyProperty
		extends BaseProperty
	{
		//
		//
		// Private members
		//
		//

		private MetaBeanProperty	mProperty;

		private Field				mField;

		private Method				mGetterMethod;

		private Method				mSetterMethod;

		//
		//
		// Constructor
		//
		//

		public GroovyProperty( MetaBeanProperty property, Class<?> javaClass )
		{
			super( property.getName(), property.getType() );

			mProperty = property;

			try
			{
				CachedField field = mProperty.getField();

				if ( field != null )
					mField = field.field;

				MetaMethod getterMethod = mProperty.getGetter();

				if ( getterMethod != null )
					mGetterMethod = javaClass.getMethod( getterMethod.getName() );

				MetaMethod setterMethod = mProperty.getSetter();

				if ( setterMethod != null )
					mSetterMethod = javaClass.getMethod( setterMethod.getName(), setterMethod.getNativeParameterTypes() );
			}
			catch ( Exception e )
			{
				throw InspectorException.newException( e );
			}
		}

		//
		//
		// Public methods
		//
		//

		public boolean isReadable()
		{
			return ( mProperty.getGetter() != null );
		}

		public Object read( Object obj )
		{
			try
			{
				return mProperty.getProperty( obj );
			}
			catch ( Exception e )
			{
				throw InspectorException.newException( e );
			}
		}

		public boolean isWritable()
		{
			return ( mProperty.getSetter() != null );
		}

		public <T extends Annotation> T getAnnotation( Class<T> annotation )
		{
			if ( mField != null )
				return mField.getAnnotation( annotation );

			if ( mGetterMethod != null )
				return mGetterMethod.getAnnotation( annotation );

			if ( mSetterMethod != null )
				return mSetterMethod.getAnnotation( annotation );

			throw InspectorException.newException( "Don't know how to getAnnotation from " + getName() );
		}

		public Type getGenericType()
		{
			if ( mField != null )
				return mField.getGenericType();

			if ( mGetterMethod != null )
				return mGetterMethod.getGenericReturnType();

			if ( mSetterMethod != null )
				return mSetterMethod.getGenericParameterTypes()[0];

			throw InspectorException.newException( "Don't know how to getGenericType from " + getName() );
		}
	}
}
