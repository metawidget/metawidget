// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

package org.metawidget.inspector.impl.propertystyle.groovy;

import groovy.lang.GroovySystem;
import groovy.lang.MetaBeanProperty;
import groovy.lang.MetaMethod;
import groovy.lang.MetaProperty;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

import org.codehaus.groovy.reflection.CachedField;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.propertystyle.BaseProperty;
import org.metawidget.inspector.impl.propertystyle.BasePropertyStyle;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * PropertyStyle for Groovy-style properties.
 * <p>
 * Groovy-style properties can <em>almost</em> be handled using <code>JavaBeanPropertyStyle</code>,
 * because the Groovy compiler automatically generates JavaBean-style getters and setters.
 * Unfortunately, it does not also copy any annotations defined on the property to the generated
 * getter and setters. This <code>PropertyStyle</code> is designed to access those annotations.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class GroovyPropertyStyle
	extends BasePropertyStyle {

	//
	// Constructor
	//

	public GroovyPropertyStyle() {

		this( new GroovyPropertyStyleConfig() );
	}

	public GroovyPropertyStyle( GroovyPropertyStyleConfig config ) {

		super( config );
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

		Map<String, Property> propertiesToReturn = CollectionUtils.newTreeMap( StringUtils.CASE_INSENSITIVE_COMPARATOR );

		// Iterate over all Groovy properties

		Class<?> clazz = ClassUtils.niceForName( type );

		if ( clazz == null ) {
			return propertiesToReturn;
		}
		
		List<MetaProperty> properties = GroovySystem.getMetaClassRegistry().getMetaClass( clazz ).getProperties();

		for ( MetaProperty property : properties ) {
			// Not CachedField, MetaArrayLengthProperty, or MetaExpandoProperty

			if ( !( property instanceof MetaBeanProperty ) ) {
				continue;
			}

			MetaBeanProperty metaBeanProperty = (MetaBeanProperty) property;

			String propertyName = metaBeanProperty.getName();
			Class<?> propertyType = metaBeanProperty.getType();

			// Only public properties

			if ( !Modifier.isPublic( metaBeanProperty.getModifiers() ) ) {
				continue;
			}

			// Exclude based on criteria
			//
			// Note: it would be nice to use 'getTheClass()' here, instead of Class.forName(
			// getName() ), but that requires Groovy 1.6+ and as of Seam 2.2.0.GA it ships with
			// Groovy 1.5

			Class<?> declaringClass;

			if ( metaBeanProperty.getGetter() != null ) {
				declaringClass = ClassUtils.niceForName( metaBeanProperty.getGetter().getDeclaringClass().getName() );
			} else if ( metaBeanProperty.getSetter() != null ) {
				declaringClass = ClassUtils.niceForName( metaBeanProperty.getSetter().getDeclaringClass().getName() );
			} else {
				declaringClass = clazz;
			}

			if ( isExcluded( declaringClass, propertyName, propertyType ) ) {
				continue;
			}

			propertiesToReturn.put( propertyName, new GroovyProperty( (MetaBeanProperty) property, clazz ) );
		}

		return propertiesToReturn;
	}

	/**
	 * Whether to exclude the given base type when searching up the model inheritance chain.
	 * <p>
	 * This can be useful when the convention or base class define properties that are
	 * framework-specific, and should be filtered out from 'real' business model properties.
	 * <p>
	 * By default, excludes any base types from the <code>org.groovy.*</code> packages, as well as
	 * those excluded by <code>BasePropertyStyle</code>.
	 *
	 * @return true if the property should be excluded, false otherwise
	 */

	@Override
	protected boolean isExcludedBaseType( Class<?> classToExclude ) {

		// (classToExclude might be null in the Groovy Console applet. We won't need this
		// after we can use .getTheClass)

		if ( classToExclude == null ) {
			return false;
		}

		String className = classToExclude.getName();

		if ( className.startsWith( "org.groovy." ) ) {
			return true;
		}

		return super.isExcludedBaseType( classToExclude );
	}

	/**
	 * Whether to exclude the given property name when searching for properties.
	 * <p>
	 * This can be useful when the convention or base class define properties that are
	 * framework-specific, and should be filtered out from 'real' business model properties.
	 * <p>
	 * By default, excludes 'metaClass'.
	 *
	 * @return true if the property should be excluded, false otherwise
	 */

	@Override
	protected boolean isExcludedName( String name ) {

		if ( "metaClass".equals( name ) ) {
			return true;
		}

		return super.isExcludedName( name );
	}

	//
	// Inner classes
	//

	/**
	 * Groovy-based property.
	 */

	private static class GroovyProperty
		extends BaseProperty {

		//
		// Private members
		//

		private MetaBeanProperty	mProperty;

		private Field				mField;

		private Method				mGetterMethod;

		private Method				mSetterMethod;

		//
		// Constructor
		//

		public GroovyProperty( MetaBeanProperty property, Class<?> javaClass ) {

			super( property.getName(), property.getType().getName() );

			mProperty = property;

			try {
				CachedField field = mProperty.getField();

				if ( field != null ) {
					mField = field.field;
				}

				MetaMethod getterMethod = mProperty.getGetter();

				if ( getterMethod != null ) {
					mGetterMethod = javaClass.getMethod( getterMethod.getName() );
				}

				MetaMethod setterMethod = mProperty.getSetter();

				if ( setterMethod != null ) {
					mSetterMethod = javaClass.getMethod( setterMethod.getName(), setterMethod.getNativeParameterTypes() );
				}
			} catch ( Exception e ) {
				throw InspectorException.newException( e );
			}
		}

		//
		// Public methods
		//

		public boolean isReadable() {

			return mProperty.getGetter() != null;
		}

		public Object read( Object obj ) {

			try {
				return mProperty.getProperty( obj );
			} catch ( Exception e ) {
				throw InspectorException.newException( e );
			}
		}

		public boolean isWritable() {

			return mProperty.getSetter() != null;
		}

		public void write( Object obj, Object value ) {

			try {
				mProperty.setProperty( obj, value );
			} catch ( Exception e ) {
				throw InspectorException.newException( e );
			}
		}

		public <T extends Annotation> T getAnnotation( Class<T> annotation ) {

			if ( mField != null ) {
				return mField.getAnnotation( annotation );
			}

			if ( mGetterMethod != null ) {
				return mGetterMethod.getAnnotation( annotation );
			}

			if ( mSetterMethod != null ) {
				return mSetterMethod.getAnnotation( annotation );
			}

			throw InspectorException.newException( "Don't know how to getAnnotation from " + getName() );
		}

		public String getGenericType() {

			if ( mField != null ) {
				return ClassUtils.getGenericTypeAsString( mField.getGenericType() );
			}

			if ( mGetterMethod != null ) {
				return ClassUtils.getGenericTypeAsString( mGetterMethod.getGenericReturnType() );
			}

			if ( mSetterMethod != null ) {
				return ClassUtils.getGenericTypeAsString( mSetterMethod.getGenericParameterTypes()[0] );
			}

			throw InspectorException.newException( "Don't know how to getGenericType from " + getName() );
		}
	}
}
