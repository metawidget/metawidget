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
import groovy.lang.MetaMethod;
import groovy.lang.MetaProperty;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.codehaus.groovy.reflection.CachedField;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.propertystyle.BaseProperty;
import org.metawidget.inspector.impl.propertystyle.BasePropertyStyle;
import org.metawidget.inspector.impl.propertystyle.Property;
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

		Map<String, Property> propertiesToReturn = CollectionUtils.newTreeMap();

		// Iterate over all Groovy properties.
		//
		// Note: we do not cache this like we do JavaBean properties, because presumably
		// these can change dynamically?

		@SuppressWarnings( "unchecked" )
		List<MetaProperty> properties = GroovySystem.getMetaClassRegistry().getMetaClass( clazz ).getProperties();

		for ( MetaProperty property : properties )
		{
			// Not CachedField, MetaArrayLengthProperty, or MetaExpandoProperty
			// TODO: test this

			if ( !( property instanceof MetaBeanProperty ))
				continue;

			MetaBeanProperty metaBeanProperty = (MetaBeanProperty) property;

			String name = metaBeanProperty.getName();
			Class<?> type = metaBeanProperty.getType();

			// Only public properties

			if ( !Modifier.isPublic( metaBeanProperty.getModifiers() ))
				continue;

			// Exclude based on criteria

			Class<?> declaringClass;

			if ( metaBeanProperty.getGetter() != null )
				declaringClass = metaBeanProperty.getGetter().getDeclaringClass().getTheClass();
			else if ( metaBeanProperty.getSetter() != null )
				declaringClass = metaBeanProperty.getSetter().getDeclaringClass().getTheClass();
			else
				declaringClass = clazz;

			if ( isExcluded( declaringClass, name, type ))
				continue;

			propertiesToReturn.put( name, new GroovyProperty( (MetaBeanProperty) property, clazz ) );
		}

		return propertiesToReturn;
	}

	/**
	 * Whether to exclude the given base type when searching up the model inheritance chain.
	 * <p>
	 * This can be useful when the convention or base class define properties that are
	 * framework-specific, and should be filtered out from 'real' business model properties.
	 * <p>
	 * By default, excludes any base types from the <code>org.groovy.*</code> packages, as
	 * well as those excluded by <code>BasePropertyStyle</code>.
	 *
	 * @return true if the property should be excluded, false otherwise
	 */

	@Override
	protected boolean isExcludedBaseType( Class<?> clazz )
	{
		Package pkg = clazz.getPackage();

		if ( pkg == null )
			return false;

		String pkgName = pkg.getName();

		if ( pkgName.startsWith( "org.groovy." ))
			return true;

		return super.isExcludedBaseType( clazz );
	}

	//
	// Inner classes
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
