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

package org.metawidget.inspector.impl.propertystyle.javabean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;

import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.propertystyle.BaseProperty;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.inspector.impl.propertystyle.PropertyStyle;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * PropertyStyle for JavaBean-style properties.
 * <p>
 * This PropertyStyle recognizes both getters and setters and public member fields.
 *
 * @author Richard Kennard
 */

public class JavaBeanPropertyStyle
	implements PropertyStyle
{
	//
	//
	// Private members
	//
	//

	/**
	 * Cache of property lookups.
	 * <p>
	 * Property lookups are potentially expensive, so we cache them. The cache itself is a member
	 * variable, not a static, because we rely on <code>BasePropertyInspector</code> to only
	 * create one instance of <code>JavaBeanPropertyStyle</code> for all <code>Inspectors</code>.
	 * <p>
	 * This also stops problems with subclasses of <code>JavaBeanPropertyStyle</code> sharing the
	 * same static cache.
	 */

	private Map<Class<?>, Map<String, Property>>	mPropertiesCache	= CollectionUtils.newHashMap();

	private String[]								mExcludeNames;

	private Class<?>[]								mExcludeReturnTypes;

	//
	//
	// Constructor
	//
	//

	public JavaBeanPropertyStyle()
	{
		mExcludeNames = getExcludeNames();
		mExcludeReturnTypes = getExcludeReturnTypes();
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
		synchronized ( mPropertiesCache )
		{
			Map<String, Property> properties = mPropertiesCache.get( clazz );

			if ( properties == null )
			{
				properties = inspectProperties( clazz );
				mPropertiesCache.put( clazz, Collections.unmodifiableMap( properties ) );
			}

			return properties;
		}
	}

	//
	//
	// Protected methods
	//
	//

	/**
	 * Array of property names to exclude when searching for properties.
	 * <p>
	 * This can be useful when the convention or base class define properties that are
	 * framework-specific, and should be filtered out from 'real' business model properties.
	 * <p>
	 * By default, filters out the properties 'propertyChangeListeners' and
	 * 'vetoableChangeListeners'.
	 */

	protected String[] getExcludeNames()
	{
		return new String[] { "propertyChangeListeners", "vetoableChangeListeners" };
	}

	/**
	 * Array of property return types to exclude when searching for properties. Subtypes of the
	 * specified exclude types are also excluded.
	 * <p>
	 * This can be useful when the convention or base class define properties that are
	 * framework-specific, and should be filtered out from 'real' business model properties.
	 * <p>
	 * By default, filters out any types that return <code>java.lang.Class</code>.
	 */

	protected Class<?>[] getExcludeReturnTypes()
	{
		return new Class<?>[] { Class.class };
	}

	/**
	 * @return the properties of the given class. Never null.
	 */

	protected Map<String, Property> inspectProperties( Class<?> clazz )
	{
		// TreeMap so that returns alphabetically sorted properties

		Map<String, Property> properties = CollectionUtils.newTreeMap();

		// Public fields

		field: for ( Field field : clazz.getFields() )
		{
			// Ignore static public fields

			if ( Modifier.isStatic( field.getModifiers() ) )
				continue;

			// Ignore certain types

			Class<?> type = field.getType();

			for ( Class<?> excludeType : mExcludeReturnTypes )
			{
				if ( excludeType.isAssignableFrom( type ) )
					continue field;
			}

			String propertyName = field.getName();
			properties.put( propertyName, new FieldProperty( propertyName, field ) );
		}

		// Getter methods

		getter: for ( Method methodRead : clazz.getMethods() )
		{
			Class<?>[] parameters = methodRead.getParameterTypes();

			if ( parameters.length != 0 )
				continue;

			Class<?> toReturn = methodRead.getReturnType();

			if ( void.class.equals( toReturn ) )
				continue;

			// Ignore certain types

			for ( Class<?> excludeType : mExcludeReturnTypes )
			{
				if ( excludeType.isAssignableFrom( toReturn ) )
					continue getter;
			}

			String methodName = methodRead.getName();
			String propertyName = null;

			if ( methodName.startsWith( ClassUtils.JAVABEAN_GET_PREFIX ) )
				propertyName = methodName.substring( ClassUtils.JAVABEAN_GET_PREFIX.length() );
			else if ( methodName.startsWith( ClassUtils.JAVABEAN_IS_PREFIX ) )
				propertyName = methodName.substring( ClassUtils.JAVABEAN_IS_PREFIX.length() );
			else
				continue;

			if ( !StringUtils.isFirstLetterUppercase( propertyName ) )
				continue;

			// Ignore certain names

			String lowercasedPropertyName = StringUtils.lowercaseFirstLetter( propertyName );

			if ( ArrayUtils.contains( mExcludeNames, lowercasedPropertyName ) )
				continue;

			// Already found (via its field)?

			Property propertyExisting = properties.get( lowercasedPropertyName );

			if ( propertyExisting != null )
			{
				if ( !( propertyExisting instanceof JavaBeanProperty ) )
					continue;

				// Beware covariant return types: always prefer the
				// subclass

				if ( toReturn.isAssignableFrom( propertyExisting.getType() ) )
					continue;
			}

			// Try to find a matching setter

			Method methodWrite = null;

			try
			{
				methodWrite = clazz.getMethod( ClassUtils.JAVABEAN_SET_PREFIX + propertyName, toReturn );
			}
			catch ( NoSuchMethodException e )
			{
				// May not be one
			}

			properties.put( lowercasedPropertyName, new JavaBeanProperty( lowercasedPropertyName, toReturn, methodRead, methodWrite ) );
		}

		// Setter methods (for those without getters)

		setter: for ( Method methodWrite : clazz.getMethods() )
		{
			Class<?>[] parameters = methodWrite.getParameterTypes();

			if ( parameters.length != 1 )
				continue;

			Class<?> toSet = parameters[0];

			// Ignore certain types

			for ( Class<?> excludeType : mExcludeReturnTypes )
			{
				if ( excludeType.isAssignableFrom( toSet ) )
					continue setter;
			}

			String methodName = methodWrite.getName();

			if ( !methodName.startsWith( ClassUtils.JAVABEAN_SET_PREFIX ) )
				continue;

			String propertyName = methodName.substring( ClassUtils.JAVABEAN_SET_PREFIX.length() );

			if ( !StringUtils.isFirstLetterUppercase( propertyName ) )
				continue;

			// Ignore certain names

			String lowercasedPropertyName = StringUtils.lowercaseFirstLetter( propertyName );

			if ( ArrayUtils.contains( mExcludeNames, lowercasedPropertyName ) )
				continue;

			// Already found (via its field/getter)?

			if ( properties.containsKey( lowercasedPropertyName ) )
				continue;

			properties.put( lowercasedPropertyName, new JavaBeanProperty( lowercasedPropertyName, toSet, null, methodWrite ) );
		}

		return properties;
	}

	//
	//
	// Inner classes
	//
	//

	/**
	 * Public member field-based property.
	 */

	protected static class FieldProperty
		extends BaseProperty
	{
		//
		//
		// Private methods
		//
		//

		private Field	mField;

		//
		//
		// Constructor
		//
		//

		public FieldProperty( String name, Field field )
		{
			super( name, field.getType() );

			mField = field;

			if ( mField == null )
				throw new NullPointerException( "field" );
		}

		//
		//
		// Public methods
		//
		//

		public boolean isReadable()
		{
			return true;
		}

		public Object read( Object obj )
		{
			try
			{
				return mField.get( obj );
			}
			catch ( Exception e )
			{
				throw InspectorException.newException( e );
			}
		}

		public boolean isWritable()
		{
			return true;
		}

		public <T extends Annotation> T getAnnotation( Class<T> annotation )
		{
			return mField.getAnnotation( annotation );
		}

		public Type getGenericType()
		{
			return mField.getGenericType();
		}

		/**
		 * Exposed for JavassistPropertyStyle.
		 */

		public Field getField()
		{
			return mField;
		}
	}

	/**
	 * JavaBean-convention-based property.
	 */

	protected static class JavaBeanProperty
		extends BaseProperty
	{
		//
		//
		// Private methods
		//
		//

		private Method	mReadMethod;

		private Method	mWriteMethod;

		//
		//
		// Constructor
		//
		//

		public JavaBeanProperty( String name, Class<?> clazz, Method readMethod, Method writeMethod )
		{
			super( name, clazz );

			mReadMethod = readMethod;
			mWriteMethod = writeMethod;

			// Must have either a getter or a setter (or both)

			if ( mReadMethod == null && mWriteMethod == null )
				throw InspectorException.newException( "JavaBeanProperty '" + name + "' has no getter and no setter" );
		}

		//
		//
		// Public methods
		//
		//

		public boolean isReadable()
		{
			return ( mReadMethod != null );
		}

		public Object read( Object obj )
		{
			try
			{
				return mReadMethod.invoke( obj );
			}
			catch ( Exception e )
			{
				throw InspectorException.newException( e );
			}
		}

		public boolean isWritable()
		{
			return ( mWriteMethod != null );
		}

		public <T extends Annotation> T getAnnotation( Class<T> annotationClass )
		{
			if ( mReadMethod != null )
			{
				T annotation = mReadMethod.getAnnotation( annotationClass );

				if ( annotation != null )
					return annotation;
			}

			if ( mWriteMethod != null )
			{
				T annotation = mWriteMethod.getAnnotation( annotationClass );

				if ( annotation != null )
					return annotation;

				return null;
			}

			return null;
		}

		public Type getGenericType()
		{
			if ( mReadMethod != null )
				return mReadMethod.getGenericReturnType();

			return mWriteMethod.getGenericParameterTypes()[0];
		}

		/**
		 * Exposed for JavassistPropertyStyle.
		 */

		public Method getReadMethod()
		{
			return mReadMethod;
		}

		/**
		 * Exposed for JavassistPropertyStyle.
		 */

		public Method getWriteMethod()
		{
			return mWriteMethod;
		}
	}
}
