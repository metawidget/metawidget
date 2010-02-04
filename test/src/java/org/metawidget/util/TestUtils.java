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
import java.util.regex.Pattern;

import javax.swing.JPanel;

import junit.framework.TestCase;

import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyle;
import org.metawidget.swing.layout.GridBagLayoutConfig;

/**
 * Utilities for running unit tests.
 *
 * @author Richard Kennard
 */

public class TestUtils
	extends TestCase
{
	//
	// Public methods
	//

	/**
	 * Generic algorithm to test equals/hashCode implementation by calling each setter on the Class.
	 * <p>
	 * Of course, this method will not work in all cases, but when it does it saves a lot of
	 * boilerplate testing code.
	 */

	public static void testEqualsAndHashcode( Class<?> clazz )
	{
		try
		{
			// Test top-level object

			Object object1 = clazz.newInstance();
			assertTrue( !object1.equals( "foo" ) );

			// LOW: subclass
			assertTrue( !object1.equals( new GridBagLayoutConfig()
			{
				// Subclass
			} ) );
			assertTrue( object1.equals( object1 ) );

			Object object2 = clazz.newInstance();
			assertTrue( object1.equals( object2 ) );
			assertTrue( object1.hashCode() == object2.hashCode() );

			// Test each property

			JavaBeanPropertyStyle propertyStyle = new JavaBeanPropertyStyle();

			for ( Property property : propertyStyle.getProperties( clazz ).values() )
			{
				// Setter

				if ( !property.isWritable() )
					continue;

				Object toSet;
				String propertyName = property.getName();
				Class<?> propertyType = property.getType();

				// Simple types

				if ( int.class.isAssignableFrom( propertyType ) )
					toSet = 42;
				else if ( Font.class.isAssignableFrom( propertyType ) )
					toSet = new JPanel().getFont();
				else if ( Color.class.isAssignableFrom( propertyType ) )
					toSet = Color.blue;
				else if ( String.class.isAssignableFrom( propertyType ) )
					toSet = "foo";
				else if ( boolean.class.equals( propertyType ))
				{
					// (toggle from the default)

					if ( property.isReadable() )
						toSet = !((Boolean) ClassUtils.getProperty( object1, propertyName ));
					else
						toSet = true;
				}

				// Arrays

				else if ( propertyType.isArray() )
				{
					Class<?> componentType = propertyType.getComponentType();

					if ( String.class.equals( componentType ))
						toSet = new String[]{ "foo", "bar", "baz" };

					// Array types that are never equal to each other

					else if ( InputStream.class.isAssignableFrom( componentType ))
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
					// (toggle from the default)

					if ( property.isReadable() )
					{
						toSet = ClassUtils.getProperty( object1, propertyName );

						if ( toSet.equals( propertyType.getEnumConstants()[0] ))
							toSet = propertyType.getEnumConstants()[1];
					}
					else
						toSet = propertyType.getEnumConstants()[0];
				}

				// Types that are never equal to each other

				else if ( InputStream.class.isAssignableFrom( propertyType ))
					continue;

				else if ( Pattern.class.isAssignableFrom( propertyType ))
					continue;

				// Unknown

				else
					throw new Exception( "Don't know how to test a property of " + propertyType );

				Method writeMethod = ClassUtils.getWriteMethod( object1.getClass(), propertyName, propertyType );
				writeMethod.invoke( object1, toSet );

				// Getter

				if ( property.isReadable() )
					assertTrue( toSet.equals( ClassUtils.getProperty( object1, propertyName ) ));

				// TODO: test nullable stays null

				// equals/hashCode

				assertTrue( propertyName, !object1.equals( object2 ) );

				writeMethod.invoke( object2, toSet );
				assertTrue( object1.equals( object2 ) );
				assertTrue( object1.hashCode() == object2.hashCode() );
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
				if ( "equals".equals( method.getName() ))
					return ( proxy == args[0] );

				if ( "hashCode".equals( method.getName() ))
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
