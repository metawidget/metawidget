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

package org.metawidget.util;

import java.awt.Color;
import java.awt.Font;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.JPanel;

import junit.framework.Assert;

import org.metawidget.config.ResourceResolver;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyle;
import org.metawidget.util.simple.StringUtils;

/**
 * Utilities for running unit tests.
 *
 * @author Richard Kennard
 */

public class TestUtils
{
	//
	// Public methods
	//

	/**
	 * Generic algorithm to test equals/hashCode implementations by calling each setter on the
	 * Class.
	 * <p>
	 * Of course, this method cannot be applied to all cases, but when it can it saves a
	 * <em>lot</em> of boilerplate testing code.
	 */

	@SuppressWarnings( "unchecked" )
	public static <T, S extends T> void testEqualsAndHashcode( Class<T> clazz, S subclass, String... exclude )
	{
		testEqualsAndHashcode( clazz, subclass, (Map) null, exclude );
	}

	public static <T, S extends T> void testEqualsAndHashcode( Class<T> clazz, S subclass, Map<?,?> dummyTypes, String... exclude )
	{
		try
		{
			testEqualsAndHashcode( clazz.newInstance(), clazz.newInstance(), subclass, dummyTypes, exclude );
		}
		catch ( Exception e )
		{
			throw new RuntimeException( e );
		}
	}

	@SuppressWarnings( "unchecked" )
	public static <T, S extends T> void testEqualsAndHashcode( T object1, T object2, S subclass, String... exclude )
	{
		testEqualsAndHashcode( object1, object2, subclass, (Map) null, exclude );
	}

	/**
	 * @param dummyTypes
	 *            dummy values to set child properties to, in addition to the default dummy ones
	 *            already supported (eg. int, String etc). Can be null
	 */

	public static <T, S extends T, D> void testEqualsAndHashcode( T object1, T object2, S subclass, Map<?,?> dummyTypes, String... exclude )
	{
		try
		{
			// Test top-level object

			Assert.assertTrue( !object1.equals( null ) );
			Assert.assertTrue( !object1.equals( "foo" ) );
			Assert.assertTrue( "subclass", !object1.equals( subclass ) );
			Assert.assertTrue( object1.equals( object1 ) );

			Assert.assertTrue( object1 != object2 );
			Assert.assertTrue( object1.equals( object2 ) );
			Assert.assertTrue( object1.hashCode() == object2.hashCode() );

			// Test each property

			JavaBeanPropertyStyle propertyStyle = new JavaBeanPropertyStyle();

			for ( Property property : propertyStyle.getProperties( object1.getClass() ).values() )
			{
				String propertyName = property.getName();

				if ( ArrayUtils.contains( exclude, propertyName ) )
					continue;

				// Setter

				if ( !property.isWritable() )
					continue;

				// If the default for the property is not null, then test setting it to null returns
				// null (tests BaseObjectInspectorConfig.mNullPropertyStyle etc)

				Class<?> propertyType = property.getType();
				Method writeMethod = ClassUtils.getWriteMethod( object1.getClass(), propertyName, propertyType );

				if ( !propertyType.isPrimitive() )
				{
					// (this doesn't apply to ResourceResolver)

					if ( !ResourceResolver.class.equals( propertyType ) )
					{
						Method readMethod = null;

						try
						{
							readMethod = ClassUtils.getReadMethod( object1.getClass(), propertyName );
						}
						catch ( Exception e1 )
						{
							try
							{
								readMethod = object1.getClass().getDeclaredMethod( "get" + StringUtils.uppercaseFirstLetter( propertyName ) );
								readMethod.setAccessible( true );
							}
							catch ( Exception e2 )
							{
								// Fair enough, no such method
							}
						}

						if ( readMethod != null && readMethod.invoke( object1 ) != null )
						{
							writeMethod.invoke( object1, new Object[] { null } );
							Assert.assertTrue( propertyName, null == readMethod.invoke( object1 ) );
							Assert.assertTrue( !object1.equals( object2 ) );

							Assert.assertTrue( propertyName, null != readMethod.invoke( object2 ) );
							writeMethod.invoke( object2, new Object[] { null } );
							Assert.assertTrue( propertyName, null == readMethod.invoke( object2 ) );

							Assert.assertTrue( object1.equals( object2 ) );
							Assert.assertTrue( object1.hashCode() == object2.hashCode() );
						}
					}
				}

				Object toSet;

				// Simple types

				if ( int.class.isAssignableFrom( propertyType ) )
					toSet = 42;
				else if ( Font.class.isAssignableFrom( propertyType ) )
					toSet = new JPanel().getFont();
				else if ( Color.class.isAssignableFrom( propertyType ) )
					toSet = Color.blue;
				else if ( String.class.isAssignableFrom( propertyType ) )
					toSet = "foo";
				else if ( boolean.class.equals( propertyType ) )
				{
					// (toggle from the default)

					if ( property.isReadable() )
						toSet = !( (Boolean) ClassUtils.getProperty( object1, propertyName ) );
					else
						toSet = true;
				}

				// Dummy types

				else if ( dummyTypes != null && dummyTypes.containsKey( propertyType ) )
					toSet = dummyTypes.get( propertyType );

				// Arrays

				else if ( propertyType.isArray() )
				{
					Class<?> componentType = propertyType.getComponentType();

					if ( String.class.equals( componentType ) )
						toSet = new String[] { "foo", "bar", "baz" };

					// Array types that are never equal to each other

					else if ( InputStream.class.isAssignableFrom( componentType ) )
						continue;

					// Arrays of dynamic proxies

					else if ( componentType.isInterface() )
					{
						Object[] array = (Object[]) Array.newInstance( componentType, 3 );
						array[0] = newProxyInstance( componentType );
						array[1] = newProxyInstance( componentType );
						toSet = array;
					}
					else
						throw new Exception( "Don't know how to test an array property of " + propertyType );
				}

				// Dynamic proxy

				else if ( propertyType.isInterface() )
					toSet = newProxyInstance( propertyType );

				// Enums

				else if ( propertyType.isEnum() )
				{
					toSet = propertyType.getEnumConstants()[0];

					// (toggle from the default)

					if ( property.isReadable() && toSet.equals( ClassUtils.getProperty( object1, propertyName ) ) )
						toSet = propertyType.getEnumConstants()[1];
				}

				// Types that are never equal to each other

				else if ( InputStream.class.isAssignableFrom( propertyType ) )
					continue;

				else if ( Pattern.class.isAssignableFrom( propertyType ) )
					continue;

				// Unknown

				else
					throw new Exception( "Don't know how to test a property of " + propertyType );

				int hashCodeBefore = object1.hashCode();
				writeMethod.invoke( object1, toSet );

				// The hashCode before and after *could* be the same, but it's not a very good idea

				Assert.assertTrue( propertyName, object1.hashCode() != hashCodeBefore );

				// Getter

				if ( property.isReadable() )
					Assert.assertTrue( toSet.equals( ClassUtils.getProperty( object1, propertyName ) ) );

				// equals/hashCode

				Assert.assertTrue( propertyName, !object1.equals( object2 ) );

				writeMethod.invoke( object2, toSet );
				Assert.assertTrue( object1.equals( object2 ) );
				Assert.assertTrue( object1.hashCode() == object2.hashCode() );
			}
		}
		catch ( Exception e )
		{
			throw new RuntimeException( e );
		}
	}

	//
	// Private methods
	//

	private static Object newProxyInstance( Class<?> clazz )
	{
		return Proxy.newProxyInstance( TestUtils.class.getClassLoader(), new Class[] { clazz }, new InvocationHandler()
		{
			public Object invoke( Object proxy, Method method, Object[] args )
				throws Throwable
			{
				if ( "equals".equals( method.getName() ) )
					return ( proxy == args[0] );

				if ( "hashCode".equals( method.getName() ) )
					return System.identityHashCode( proxy );

				return null;
			}
		} );
	}

	//
	// Private constructor
	//

	private TestUtils()
	{
		// Can never be called
	}
}
