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

import org.metawidget.inspector.InspectorException;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.inspector.impl.propertystyle.PropertyImpl;
import org.metawidget.inspector.impl.propertystyle.PropertyStyle;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * @author Richard Kennard
 */

public class JavaBeanPropertyStyle
	implements PropertyStyle
{
	//
	//
	// Private statics
	//
	//

	private final static Map<Class<?>, Map<String, Property>>	PROPERTIES_CACHE		= CollectionUtils.newWeakHashMap();

	//
	//
	// Protected statics
	//
	//

	protected final static String[]								DEFAULT_EXCLUDE_NAMES	= new String[] { "propertyChangeListeners", "vetoableChangeListeners" };

	protected final static Class<?>[]							DEFAULT_EXCLUDE_TYPES	= new Class<?>[] { Class.class };

	//
	//
	// Private members
	//
	//

	private String[]											mExcludeNames;

	private Class<?>[]											mExcludeTypes;

	//
	//
	// Constructor
	//
	//

	public JavaBeanPropertyStyle()
	{
		this( DEFAULT_EXCLUDE_NAMES, DEFAULT_EXCLUDE_TYPES );
	}

	public JavaBeanPropertyStyle( String[] excludeNames, Class<?>[] excludeTypes )
	{
		mExcludeNames = excludeNames;
		mExcludeTypes = excludeTypes;
	}

	//
	//
	// Public methods
	//
	//

	public Map<String, Property> getProperties( Class<?> clazz )
	{
		synchronized ( PROPERTIES_CACHE )
		{
			Map<String, Property> properties = PROPERTIES_CACHE.get( clazz );

			if ( properties == null )
			{
				properties = CollectionUtils.newHashMap();

				// Public fields

				for ( Field field : clazz.getFields() )
				{
					// Ignore static public fields (like Groovy's __timestamp)

					if ( Modifier.isStatic( field.getModifiers() ) )
						continue;

					String propertyName = field.getName();
					properties.put( propertyName, new FieldProperty( propertyName, field ) );
				}

				// Getter methods

				for ( Method methodRead : clazz.getMethods() )
				{
					Class<?>[] parameters = methodRead.getParameterTypes();

					if ( parameters.length != 0 )
						continue;

					Class<?> toReturn = methodRead.getReturnType();

					if ( void.class.equals( toReturn ) )
						continue;

					// Ignore certain types

					if ( ArrayUtils.contains( mExcludeTypes, toReturn ) )
						continue;

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

					if ( ArrayUtils.contains( mExcludeNames, propertyName ) )
						continue;

					// Already found (via its field/getter)?

					String lowercasedPropertyName = StringUtils.lowercaseFirstLetter( propertyName );

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

				for ( Method methodWrite : clazz.getMethods() )
				{
					Class<?>[] parameters = methodWrite.getParameterTypes();

					if ( parameters.length != 1 )
						continue;

					Class<?> toSet = parameters[0];

					// Ignore certain types

					if ( ArrayUtils.contains( mExcludeTypes, toSet ) )
						continue;

					String methodName = methodWrite.getName();

					if ( !methodName.startsWith( ClassUtils.JAVABEAN_SET_PREFIX ) )
						continue;

					String propertyName = methodName.substring( ClassUtils.JAVABEAN_SET_PREFIX.length() );

					if ( !StringUtils.isFirstLetterUppercase( propertyName ) )
						continue;

					// Ignore certain names

					if ( ArrayUtils.contains( mExcludeNames, propertyName ) )
						continue;

					// Already found (via its field/getter)?

					String lowercasedPropertyName = StringUtils.lowercaseFirstLetter( propertyName );

					if ( properties.containsKey( lowercasedPropertyName ) )
						continue;

					properties.put( lowercasedPropertyName, new JavaBeanProperty( lowercasedPropertyName, toSet, null, methodWrite ) );
				}

				PROPERTIES_CACHE.put( clazz, Collections.unmodifiableMap( properties ) );
			}

			return properties;
		}
	}

	//
	//
	// Inner classes
	//
	//

	/**
	 * Public member field-based property.
	 */

	private static class FieldProperty
		extends PropertyImpl
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
		}

		//
		//
		// Public methods
		//
		//

		public Field getField()
		{
			return mField;
		}

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

		public Annotation[] getAnnotations()
		{
			return mField.getAnnotations();
		}

		public Annotation[] getDeclaredAnnotations()
		{
			return mField.getDeclaredAnnotations();
		}

		public Type getPropertyGenericType()
		{
			return mField.getGenericType();
		}
	}

	/**
	 * JavaBean-convention-based property.
	 */

	private static class JavaBeanProperty
		extends PropertyImpl
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
		}

		//
		//
		// Public methods
		//
		//

		public Method getReadMethod()
		{
			return mReadMethod;
		}

		public Method getWriteMethod()
		{
			return mWriteMethod;
		}

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
			}

			return null;
		}

		public Annotation[] getAnnotations()
		{
			if ( mReadMethod == null )
			{
				if ( mWriteMethod == null )
					return null;

				return mWriteMethod.getAnnotations();
			}

			Annotation[] annotationsRead = mReadMethod.getAnnotations();

			if ( mWriteMethod == null )
				return annotationsRead;

			return ArrayUtils.add( annotationsRead, mWriteMethod.getAnnotations() );
		}

		public Annotation[] getDeclaredAnnotations()
		{
			if ( mReadMethod == null )
			{
				if ( mWriteMethod == null )
					return null;

				return mWriteMethod.getDeclaredAnnotations();
			}

			Annotation[] annotationsRead = mReadMethod.getDeclaredAnnotations();

			if ( mWriteMethod == null )
				return annotationsRead;

			return ArrayUtils.add( annotationsRead, mWriteMethod.getDeclaredAnnotations() );
		}

		public Type getPropertyGenericType()
		{
			if ( mReadMethod != null )
				return mReadMethod.getGenericReturnType();

			if ( mWriteMethod != null )
				return mWriteMethod.getGenericParameterTypes()[0];

			return null;
		}
	}
}
