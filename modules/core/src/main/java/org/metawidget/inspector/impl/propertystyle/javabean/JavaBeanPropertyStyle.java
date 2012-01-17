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
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Map;

import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.propertystyle.BaseProperty;
import org.metawidget.inspector.impl.propertystyle.BasePropertyStyle;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.Java5ClassUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * PropertyStyle for JavaBean-style properties.
 * <p>
 * This PropertyStyle recognizes getters and setters declared using the JavaBean convention.
 * <p>
 * Please note:
 * <p>
 * <ul>
 * <li>by default, this implementation also recognizes public member fields - even though these are
 * not strictly JavaBean-convention. This can be disabled using
 * <code>JavaBeanPropertyStyleConfig.setSupportPublicFields</code></li>
 * <li>this implementation does not use <code>java.beans.Introspector</code>, as some environments
 * that use JavaBean-convention do not support the <code>java.bean</code> package (eg. Android)</li>
 * <li>by default, this implementation does <em>not</em> recognize private fields. This is because
 * the JavaBean convention does not specify any relationship between getters/setters and their
 * private fields. However, it is a common requirement to want to <em>annotate</em> the private
 * field rather than its getter/setter. Frameworks like JPA allow this because they can populate the
 * private field directly. But this does not work well for most UI frameworks, such as binding and
 * validation frameworks, which rely on public getters/setters. To support the best of both worlds
 * see <code>JavaBeanPropertyStyleConfig.setPrivateFieldConvention</code></li>
 * </ul>
 *
 * @author Richard Kennard
 */

public class JavaBeanPropertyStyle
	extends BasePropertyStyle {

	//
	// Private statics
	//

	private static ThreadLocal<ClassLoader>	LOCAL_CLASSLOADER	= new ThreadLocal<ClassLoader>();

	//
	// Private members
	//

	private boolean							mSupportPublicFields;

	private MessageFormat					mPrivateFieldConvention;

	//
	// Constructor
	//

	public JavaBeanPropertyStyle() {

		this( new JavaBeanPropertyStyleConfig() );
	}

	public JavaBeanPropertyStyle( JavaBeanPropertyStyleConfig config ) {

		super( config );

		mSupportPublicFields = config.isSupportPublicFields();
		mPrivateFieldConvention = config.getPrivateFieldConvention();
	}

	//
	// Public methods
	//

	/**
	 * Set the ClassLoader (local to this Thread) to use to resolve classes.
	 * <p>
	 * This can be useful if using <code>JavaBeanPropertyStyle</code> outside of Metawidget. For
	 * example, you use a <code>JavaBeanPropertyStyle</code> in your EJB layer, but pass it a type
	 * String that refers to a class from the WAR layer. In order to resolve that type, the EJB
	 * layer must use the WAR layer's ClassLoader.
	 */

	public void setLocalClassLoader( ClassLoader classLoader ) {

		LOCAL_CLASSLOADER.set( classLoader );
	}

	//
	// Protected methods
	//

	/**
	 * Returns properties sorted by name.
	 */

	@Override
	protected Map<String, Property> inspectProperties( String type ) {

		// TreeMap so that returns alphabetically sorted properties

		Map<String, Property> properties = CollectionUtils.newTreeMap( StringUtils.CASE_INSENSITIVE_COMPARATOR );

		// Lookup fields, getters and setters

		Class<?> clazz = ClassUtils.niceForName( type, LOCAL_CLASSLOADER.get() );

		if ( clazz == null ) {
			return properties;
		}

		lookupFields( properties, clazz );
		lookupGetters( properties, clazz );
		lookupSetters( properties, clazz );

		// Clean up explicit excludes

		for ( Iterator<Map.Entry<String, Property>> i = properties.entrySet().iterator(); i.hasNext(); ) {

			if ( i.next().getValue() == null ) {
				i.remove();
			}
		}

		return properties;
	}

	/**
	 * Lookup public field-based properties.
	 * <p>
	 * This method will be called before <code>lookupGetters</code> and <code>lookupSetters</code>.
	 */

	protected void lookupFields( Map<String, Property> properties, Class<?> clazz ) {

		if ( !mSupportPublicFields ) {
			return;
		}

		// Note: we must use clazz.getFields(), not clazz.getDeclaredFields(), in order
		// to avoid Applet SecurityExceptions

		for ( Field field : clazz.getFields() ) {

			// Exclude static fields

			int modifiers = field.getModifiers();

			if ( Modifier.isStatic( modifiers ) ) {
				continue;
			}

			// Get name and type

			String fieldName = field.getName();
			Class<?> type = field.getType();

			// Exclude based on other criteria
			//
			// (explicitly set to null in case we encounter an imbalanced getter/setter)

			if ( isExcluded( field.getDeclaringClass(), fieldName, type ) ) {
				properties.put( fieldName, null );
				continue;
			}

			properties.put( fieldName, new FieldProperty( fieldName, field ) );
		}
	}

	/**
	 * Lookup getter-based properties.
	 * <p>
	 * This method will be called after <code>lookupFields</code> but before
	 * <code>lookupSetters</code>.
	 */

	protected void lookupGetters( Map<String, Property> properties, Class<?> clazz ) {

		// Note: we must use clazz.getMethods(), not clazz.getDeclaredMethods(), in order
		// to avoid Applet SecurityExceptions

		for ( Method method : clazz.getMethods() ) {

			// Exclude static methods

			if ( Modifier.isStatic( method.getModifiers() ) ) {
				continue;
			}

			// Get type

			if ( method.getParameterTypes().length != 0 ) {
				continue;
			}

			Class<?> type = method.getReturnType();

			if ( void.class.equals( type ) ) {
				continue;
			}

			// Get name

			String propertyName = isGetter( method );

			if ( propertyName == null ) {
				continue;
			}

			// Exclude based on other criteria
			//
			// (explicitly set to null in case we encounted/encounter an imbalanced field/setter)

			if ( isExcluded( ClassUtils.getOriginalDeclaringClass( method ), propertyName, type ) ) {
				properties.put( propertyName, null );
				continue;
			}

			// Already found via its field?
			// (error, because otherwise will really confuse things like Commons JEXL)

			Property existingProperty = properties.get( propertyName );

			if ( existingProperty instanceof FieldProperty ) {
				throw InspectorException.newException( "JavaBeanProperty '" + ( (FieldProperty) existingProperty ).getField() + "' has both a public member variable and a public getter method. Should be one or the other" );
			}

			// Already found via another getter?

			if ( existingProperty instanceof JavaBeanProperty ) {
				JavaBeanProperty existingJavaBeanProperty = (JavaBeanProperty) existingProperty;

				// Beware covariant return types: always prefer the
				// subclass

				if ( type.isAssignableFrom( ClassUtils.niceForName( existingJavaBeanProperty.getType(), LOCAL_CLASSLOADER.get() ) ) ) {
					continue;
				}
			}

			// Explicitly excluded based on field already?

			if ( existingProperty == null && properties.containsKey( propertyName ) ) {
				continue;
			}

			properties.put( propertyName, new JavaBeanProperty( propertyName, type.getName(), method, null, getPrivateField( clazz, propertyName ) ) );
		}
	}

	/**
	 * Returns whether the given method is a 'getter' method.
	 *
	 * @param method
	 *            a parameterless method that returns a non-void
	 * @return the property name
	 */

	protected String isGetter( Method method ) {

		String methodName = method.getName();
		String propertyName;

		if ( methodName.startsWith( ClassUtils.JAVABEAN_GET_PREFIX ) ) {
			propertyName = methodName.substring( ClassUtils.JAVABEAN_GET_PREFIX.length() );

		} else if ( methodName.startsWith( ClassUtils.JAVABEAN_IS_PREFIX ) && boolean.class.equals( method.getReturnType() ) ) {

			// As per section 8.3.2 (Boolean properties) of The JavaBeans API specification, 'is'
			// only applies to boolean (little 'b')

			propertyName = methodName.substring( ClassUtils.JAVABEAN_IS_PREFIX.length() );
		} else {
			return null;
		}

		if ( !StringUtils.isCapitalized( propertyName ) ) {
			return null;
		}

		return StringUtils.decapitalize( propertyName );
	}

	/**
	 * Lookup setter-based properties.
	 * <p>
	 * This method will be called after <code>lookupFields</code> and <code>lookupGetters</code>.
	 */

	protected void lookupSetters( Map<String, Property> properties, Class<?> clazz ) {

		for ( Method method : clazz.getMethods() ) {

			// Exclude static methods

			if ( Modifier.isStatic( method.getModifiers() ) ) {
				continue;
			}

			// Get type

			Class<?>[] parameters = method.getParameterTypes();

			if ( parameters.length != 1 ) {
				continue;
			}

			Class<?> type = parameters[0];

			// Get name

			String propertyName = isSetter( method );

			if ( propertyName == null ) {
				continue;
			}

			// Exclude based on other criteria
			//
			// (explicitly set to null in case we encountered an imbalanced field/getter)

			if ( isExcluded( ClassUtils.getOriginalDeclaringClass( method ), propertyName, type ) ) {
				properties.put( propertyName, null );
				continue;
			}

			// Already found via its field?
			// (error, because otherwise will really confuse things like Commons JEXL)

			Property existingProperty = properties.get( propertyName );

			if ( existingProperty instanceof FieldProperty ) {
				throw InspectorException.newException( "JavaBeanProperty '" + ( (FieldProperty) existingProperty ).getField() + "' has both a public member variable and a public setter method. Should be one or the other" );
			}

			// Already found via its getter?

			if ( existingProperty instanceof JavaBeanProperty ) {
				JavaBeanProperty existingJavaBeanProperty = (JavaBeanProperty) existingProperty;

				// Beware covariant return types: always prefer the getter's type

				properties.put( propertyName, new JavaBeanProperty( propertyName, existingJavaBeanProperty.getType(), existingJavaBeanProperty.getReadMethod(), method, getPrivateField( clazz, propertyName ) ) );
				continue;
			}

			// Explicitly excluded based on getter/field already?

			if ( existingProperty == null && properties.containsKey( propertyName ) ) {
				continue;
			}

			properties.put( propertyName, new JavaBeanProperty( propertyName, type.getName(), null, method, getPrivateField( clazz, propertyName ) ) );
		}
	}

	/**
	 * Returns whether the given method is a 'setter' method.
	 *
	 * @param method
	 *            a single-parametered method. May return non-void (ie. for Fluent interfaces)
	 * @return the property name
	 */

	protected String isSetter( Method method ) {

		String methodName = method.getName();

		if ( !methodName.startsWith( ClassUtils.JAVABEAN_SET_PREFIX ) ) {
			return null;
		}

		String propertyName = methodName.substring( ClassUtils.JAVABEAN_SET_PREFIX.length() );

		if ( !StringUtils.isCapitalized( propertyName ) ) {
			return null;
		}

		return StringUtils.decapitalize( propertyName );
	}

	/**
	 * Gets the private field representing the given <code>propertyName</code> within the given
	 * class. Uses the configured <code>privateFieldConvention</code> (if any). Traverses up the
	 * superclass heirarchy as necessary.
	 * <p>
	 * Note it is <em>not</em> considered an error if a property has no corresponding private field:
	 * not all properties do. For example a property <code>getAge</code> may be calculated
	 * internally based off a <code>mDateOfBirth</code> field, with no corresponding
	 * <code>mAge</code> field per se.
	 * <p>
	 * Clients may override this method to change how the public-method-to-private-field mapping
	 * operates.
	 *
	 * @return the private Field for this propertyName, or null if no such field (should not throw
	 *         NoSuchFieldException)
	 */

	protected Field getPrivateField( Class<?> clazz, String propertyName ) {

		// No convention?

		if ( mPrivateFieldConvention == null ) {
			return null;
		}

		// Determine field name based on convention. MessageFormat arguments are:
		//
		// {0} = dateOfBirth, surname
		// {1} = DateOfBirth, Surname

		String[] arguments = new String[] { propertyName, StringUtils.capitalize( propertyName ) };
		String fieldName;

		synchronized ( mPrivateFieldConvention ) {
			fieldName = mPrivateFieldConvention.format( arguments, new StringBuffer(), null ).toString();
		}

		// Go looking for such a field, traversing the superclass heirarchy as necessary
		//
		// Note: the use of clazz.getDeclaredFields() may lead to Applet SecurityExceptions

		Class<?> currentClass = clazz;

		while ( currentClass != null && !isExcludedBaseType( currentClass ) ) {

			try {
				return currentClass.getDeclaredField( fieldName );
			} catch ( NoSuchFieldException e ) {
				currentClass = currentClass.getSuperclass();
			}
		}

		return null;
	}

	//
	// Inner classes
	//

	/**
	 * Public member field-based property.
	 * <p>
	 * We found <code>JavaBeanPropertyStyle</code> to be generally useful outside of any
	 * <code>Inspector</code>. When using it that way, it is also generally useful to have access to
	 * <code>FieldProperty</code>. So this class is public.
	 */

	public static class FieldProperty
		extends BaseProperty {

		//
		// Private methods
		//

		private Field	mField;

		//
		// Constructor
		//

		public FieldProperty( String name, Field field ) {

			super( name, field.getType().getName() );

			mField = field;
		}

		//
		// Public methods
		//

		public boolean isReadable() {

			return true;
		}

		public Object read( Object obj ) {

			try {
				return mField.get( obj );
			} catch ( Exception e ) {
				throw InspectorException.newException( e );
			}
		}

		public boolean isWritable() {

			return true;
		}

		public <T extends Annotation> T getAnnotation( Class<T> annotation ) {

			return mField.getAnnotation( annotation );
		}

		public String getGenericType() {

			return Java5ClassUtils.getGenericTypeAsString( mField.getGenericType() );
		}

		public Field getField() {

			return mField;
		}
	}

	/**
	 * JavaBean-convention-based property.
	 * <p>
	 * We found <code>JavaBeanPropertyStyle</code> to be generally useful outside of any
	 * <code>Inspector</code>. When using it that way, it is also generally useful to have access to
	 * <code>JavaBeanProperty</code>. So this class is public.
	 */

	public static class JavaBeanProperty
		extends BaseProperty {

		//
		// Private methods
		//

		private Method	mReadMethod;

		private Method	mWriteMethod;

		private Field	mPrivateField;

		//
		// Constructor
		//

		public JavaBeanProperty( String name, String type, Method readMethod, Method writeMethod, Field privateField ) {

			super( name, type );

			mReadMethod = readMethod;
			mWriteMethod = writeMethod;

			// Must have a getter or a setter (or both)

			if ( mReadMethod == null && mWriteMethod == null ) {
				throw InspectorException.newException( "JavaBeanProperty '" + name + "' has no getter and no setter" );
			}

			mPrivateField = privateField;
		}

		//
		// Public methods
		//

		public boolean isReadable() {

			return ( mReadMethod != null );
		}

		/**
		 * Reads this JavaBeanProperty from the given Object. Note: because of
		 * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4071957 this may fail for custom
		 * properties of Enums (eg. Gender.isMale)
		 */

		public Object read( Object obj ) {

			try {
				return mReadMethod.invoke( obj );
			} catch ( Exception e ) {
				throw InspectorException.newException( e );
			}
		}

		public boolean isWritable() {

			return ( mWriteMethod != null );
		}

		public <T extends Annotation> T getAnnotation( Class<T> annotationClass ) {

			if ( mReadMethod != null ) {
				T annotation = Java5ClassUtils.getOriginalAnnotation( mReadMethod, annotationClass );

				if ( annotation != null ) {
					return annotation;
				}
			}

			if ( mWriteMethod != null ) {
				T annotation = Java5ClassUtils.getOriginalAnnotation( mWriteMethod, annotationClass );

				if ( annotation != null ) {
					return annotation;
				}
			}

			if ( mPrivateField != null ) {
				T annotation = mPrivateField.getAnnotation( annotationClass );

				if ( annotation != null ) {
					return annotation;
				}

				return null;
			}

			return null;
		}

		public String getGenericType() {

			Type type;

			if ( mReadMethod != null ) {
				type = Java5ClassUtils.getOriginalGenericReturnType( mReadMethod );
			} else {
				type = Java5ClassUtils.getOriginalGenericParameterTypes( mWriteMethod )[0];
			}

			return Java5ClassUtils.getGenericTypeAsString( type );
		}

		public Method getReadMethod() {

			return mReadMethod;
		}

		public Method getWriteMethod() {

			return mWriteMethod;
		}
	}
}
