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
import java.util.Map;

import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.propertystyle.BaseProperty;
import org.metawidget.inspector.impl.propertystyle.BasePropertyStyle;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * PropertyStyle for JavaBean-style properties.
 * <p>
 * This PropertyStyle recognizes getters and setters declared using the JavaBean convention.
 * <p>
 * Please note:
 * <p>
 * <ul>
 *  <li>this implementation also recognizes public member fields. These are not strictly JavaBean-convention,
 *  but is a useful default</li>
 *  <li>this implementation does not use <code>java.beans.Introspector</code>, as some environments that
 *  use JavaBean-convention do not support the <code>java.bean</code> package (eg. Android).</li>
 * </ul>
 *
 * @author Richard Kennard
 */

public class JavaBeanPropertyStyle
	extends BasePropertyStyle
{
	//
	// Protected methods
	//

	/**
	 * Returns properties sorted by name.
	 */

	@Override
	protected Map<String, Property> inspectProperties( Class<?> clazz )
	{
		// TreeMap so that returns alphabetically sorted properties

		Map<String, Property> properties = CollectionUtils.newTreeMap();

		// Lookup fields, getters and setters

		lookupFields( properties, clazz );
		lookupGetters( properties, clazz );
		lookupSetters( properties, clazz );

		return properties;
	}

	/**
	 * Lookup public field-based properties.
	 * <p>
	 * This method will be called before <code>lookupGetters</code> and <code>lookupSetters</code>.
	 */

	protected void lookupFields( Map<String, Property> properties, Class<?> clazz )
	{
		// Note: we must use clazz.getFields(), not clazz.getDeclaredFields(), in order
		// to avoid Applet SecurityExceptions

		for ( Field field : clazz.getFields() )
		{
			// Exclude static fields

			int modifiers = field.getModifiers();

			if ( Modifier.isStatic( modifiers ) )
				continue;

			// Get name and type

			String fieldName = field.getName();
			Class<?> type = field.getType();

			// Exclude based on other criteria

			if ( isExcluded( field.getDeclaringClass().getName(), fieldName, type ) )
				continue;

			properties.put( fieldName, new FieldProperty( fieldName, field ) );
		}
	}

	/**
	 * Lookup getter-based properties.
	 * <p>
	 * This method will be called after <code>lookupGetters</code> but before
	 * <code>lookupSetters</code>.
	 */

	protected void lookupGetters( Map<String, Property> properties, Class<?> clazz )
	{
		// Note: we must use clazz.getMethods(), not clazz.getDeclaredMethods(), in order
		// to avoid Applet SecurityExceptions

		for ( Method method : clazz.getMethods() )
		{
			// Get type

			Class<?>[] parameters = method.getParameterTypes();

			if ( parameters.length != 0 )
				continue;

			Class<?> type = method.getReturnType();

			if ( void.class.equals( type ) )
				continue;

			// Get name

			String propertyName = isGetter( method );

			if ( propertyName == null )
				continue;

			// Exclude based on other criteria

			if ( isExcluded( method.getDeclaringClass().getName(), propertyName, type ) )
				continue;

			// Already found via its field?

			Property existingProperty = properties.get( propertyName );

			if ( existingProperty instanceof FieldProperty )
				continue;

			// Already found via another getter?

			if ( existingProperty instanceof JavaBeanProperty )
			{
				JavaBeanProperty existingJavaBeanProperty = (JavaBeanProperty) existingProperty;

				// Beware covariant return types: always prefer the
				// subclass

				if ( type.isAssignableFrom( existingJavaBeanProperty.getType() ) )
					continue;
			}

			properties.put( propertyName, new JavaBeanProperty( propertyName, type, method, null ) );
		}
	}

	/**
	 * Returns whether the given method is a 'getter' method.
	 *
	 * @param method
	 *            a parameterless method that returns a non-void
	 * @return the property name
	 */

	protected String isGetter( Method method )
	{
		String methodName = method.getName();
		String propertyName;

		if ( methodName.startsWith( ClassUtils.JAVABEAN_GET_PREFIX ) )
			propertyName = methodName.substring( ClassUtils.JAVABEAN_GET_PREFIX.length() );
		else if ( methodName.startsWith( ClassUtils.JAVABEAN_IS_PREFIX ) )
			propertyName = methodName.substring( ClassUtils.JAVABEAN_IS_PREFIX.length() );
		else
			return null;

		if ( !StringUtils.isFirstLetterUppercase( propertyName ) )
			return null;

		return StringUtils.lowercaseFirstLetter( propertyName );
	}

	/**
	 * Lookup setter-based properties.
	 * <p>
	 * This method will be called after <code>lookupGetters</code> and <code>lookupSetters</code>.
	 */

	protected void lookupSetters( Map<String, Property> properties, Class<?> clazz )
	{
		for ( Method method : clazz.getMethods() )
		{
			// Get type

			Class<?>[] parameters = method.getParameterTypes();

			if ( parameters.length != 1 )
				continue;

			Class<?> type = parameters[0];

			// Get name

			String propertyName = isSetter( method );

			if ( propertyName == null )
				continue;

			// Exclude based on other criteria

			if ( isExcluded( method.getDeclaringClass().getName(), propertyName, type ) )
				continue;

			// Already found via its field?

			Property existingProperty = properties.get( propertyName );

			if ( existingProperty instanceof FieldProperty )
				continue;

			// Already found via its getter?

			if ( existingProperty instanceof JavaBeanProperty )
			{
				JavaBeanProperty existingJavaBeanProperty = (JavaBeanProperty) existingProperty;
				properties.put( propertyName, new JavaBeanProperty( propertyName, type, existingJavaBeanProperty.getReadMethod(), method ) );
				continue;
			}

			properties.put( propertyName, new JavaBeanProperty( propertyName, type, null, method ) );
		}
	}

	/**
	 * Returns whether the given method is a 'setter' method.
	 *
	 * @param method
	 *            a single-parametered method. May return non-void (ie. for Fluent interfaces)
	 * @return the property name
	 */

	protected String isSetter( Method method )
	{
		String methodName = method.getName();

		if ( !methodName.startsWith( ClassUtils.JAVABEAN_SET_PREFIX ) )
			return null;

		String propertyName = methodName.substring( ClassUtils.JAVABEAN_SET_PREFIX.length() );

		if ( !StringUtils.isFirstLetterUppercase( propertyName ) )
			return null;

		return StringUtils.lowercaseFirstLetter( propertyName );
	}

	/**
	 * Whether to exclude the given property name when searching for properties.
	 * <p>
	 * This can be useful when the convention or base class define properties that are
	 * framework-specific, and should be filtered out from 'real' business model properties.
	 * <p>
	 * By default, excludes 'propertyChangeListeners' and 'vetoableChangeListeners'.
	 *
	 * @return true if the property should be excluded, false otherwise
	 */

	@Override
	protected boolean isExcludedName( String name )
	{
		if ( "propertyChangeListeners".equals( name ) )
			return true;

		if ( "vetoableChangeListeners".equals( name ) )
			return true;

		return super.isExcludedName( name );
	}

	//
	// Inner classes
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
