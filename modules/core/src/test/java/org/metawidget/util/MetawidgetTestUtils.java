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
import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.swing.JPanel;

import junit.framework.Assert;

import org.metawidget.config.iface.ResourceResolver;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyle;
import org.metawidget.util.simple.StringUtils;

/**
 * Utilities for running unit tests.
 *
 * @author Richard Kennard
 */

public final class MetawidgetTestUtils {

	//
	// Public statics
	//

	/**
	 * Generic algorithm to test equals/hashCode implementations by calling each setter on the
	 * Class.
	 * <p>
	 * Of course, this method cannot be applied to all cases, but when it can it saves a
	 * <em>lot</em> of boilerplate testing code.
	 */

	public static <T, S extends T> void testEqualsAndHashcode( Class<T> clazz, S subclass, String... exclude ) {

		testEqualsAndHashcode( clazz, subclass, (Map<?,?>) null, exclude );
	}

	public static <T, S extends T> void testEqualsAndHashcode( Class<T> clazz, S subclass, Map<?, ?> dummyTypes, String... exclude ) {

		try {
			testEqualsAndHashcode( clazz.newInstance(), clazz.newInstance(), subclass, dummyTypes, exclude );
		} catch ( Exception e ) {
			throw new RuntimeException( e );
		}
	}

	public static <T, S extends T> void testEqualsAndHashcode( T object1, T object2, S subclass, String... exclude ) {

		testEqualsAndHashcode( object1, object2, subclass, (Map<?,?>) null, exclude );
	}

	/**
	 * @param dummyTypes
	 *            dummy values to set child properties to, in addition to the default dummy ones
	 *            already supported (eg. int, String etc). Can be null
	 */

	public static <T, S extends T, D> void testEqualsAndHashcode( T object1, T object2, S subclass, Map<?, ?> dummyTypes, String... exclude ) {

		try {
			// Test top-level object

			Assert.assertTrue( object1 != null );

			// (keep PMD happy)

			if ( object1 == null ) {
				throw new NullPointerException( "object1" );
			}

			Assert.assertFalse( object1.equals( "foo" ) );
			Assert.assertTrue( "subclass", !object1.equals( subclass ) );
			Assert.assertEquals( object1, object1 );

			Assert.assertTrue( object1 != object2 );
			Assert.assertEquals( object1, object2 );
			Assert.assertEquals( object1.hashCode(), object2.hashCode() );

			// Null check

			Assert.assertTrue( !object1.equals( null ) );

			// Test each property

			JavaBeanPropertyStyle propertyStyle = new JavaBeanPropertyStyle();

			for ( Property property : propertyStyle.getProperties( object1.getClass().getName() ).values() ) {
				String propertyName = property.getName();

				if ( ArrayUtils.contains( exclude, propertyName ) ) {
					continue;
				}

				// Setter

				if ( !property.isWritable() ) {
					Assert.assertTrue( "Property '" + propertyName + "' has no setter", false );
					continue;
				}

				// If the default for the property is not null, then test setting it to null returns
				// null (tests BaseObjectInspectorConfig.mNullPropertyStyle etc)

				Class<?> propertyType = ClassUtils.niceForName( property.getType() );
				Method readMethod = getProtectedReadMethod( object1.getClass(), propertyName );
				Method writeMethod = ClassUtils.getWriteMethod( object1.getClass(), propertyName, propertyType );

				if ( !propertyType.isPrimitive() ) {
					// (this doesn't apply to ResourceResolver)

					if ( !ResourceResolver.class.equals( propertyType ) ) {

						// (this isn't always safe to invoke to test for null)

						if ( !InputStream.class.equals( propertyType ) && !InputStream[].class.equals( propertyType )) {

							if ( readMethod != null && readMethod.invoke( object1 ) != null ) {
								writeMethod.invoke( object1, new Object[] { null } );
								Assert.assertEquals( propertyName, null, readMethod.invoke( object1 ) );
								Assert.assertFalse( propertyName, object1.equals( object2 ) );

								Assert.assertTrue( propertyName, null != readMethod.invoke( object2 ) );
								writeMethod.invoke( object2, new Object[] { null } );
								Assert.assertEquals( propertyName, null, readMethod.invoke( object2 ) );

								Assert.assertTrue( object1.equals( object2 ) );
								Assert.assertEquals( object1.hashCode(), object2.hashCode() );
							}
						}
					}
				}

				Object toSet;

				// Simple types

				if ( int.class.isAssignableFrom( propertyType ) ) {
					toSet = 42;
				} else if ( Font.class.equals( propertyType ) ) {
					toSet = new JPanel().getFont();
				} else if ( Color.class.equals( propertyType ) ) {
					toSet = Color.blue;
				} else if ( String.class.equals( propertyType ) ) {
					toSet = "foo";
				} else if ( boolean.class.equals( propertyType ) ) {
					// (toggle from the default)

					if ( readMethod != null  ) {
						toSet = !( (Boolean) readMethod.invoke( object1 ) );
					} else {
						toSet = true;
					}
				} else if ( MessageFormat.class.isAssignableFrom( propertyType ) ) {
					toSet = new MessageFormat( "'m'{0}{1}'p'" );
				}

				// Dummy types

				else if ( dummyTypes != null && dummyTypes.containsKey( propertyType ) ) {
					toSet = dummyTypes.get( propertyType );
				} else if ( propertyType.isArray() ) {
					Class<?> componentType = propertyType.getComponentType();

					if ( String.class.equals( componentType ) ) {
						toSet = new String[] { "foo", "bar", "baz" };
					} else if ( Class.class.equals( componentType ) ) {
						toSet = new Class[] { Date.class, String.class, Set.class };
					} else if ( Object.class.equals( componentType ) ) {
						toSet = new Object[] { "Foo", "Bar", "Baz" };
					} else if ( InputStream.class.isAssignableFrom( componentType ) ) {
						continue;
					} else if ( componentType.isInterface() ) {
						Object[] array = (Object[]) Array.newInstance( componentType, 3 );
						array[0] = newProxyInstance( componentType );
						array[1] = newProxyInstance( componentType );
						toSet = array;
					} else {
						throw new Exception( "Don't know how to test an array property of " + propertyType );
					}
				}

				// Dynamic proxy

				else if ( propertyType.isInterface() ) {
					toSet = newProxyInstance( propertyType );
				} else if ( propertyType.isEnum() ) {
					toSet = propertyType.getEnumConstants()[0];

					// (toggle from the default)

					if ( readMethod != null && toSet.equals( readMethod.invoke( object1 ) ) ) {
						toSet = propertyType.getEnumConstants()[1];
					}
				}

				// Types that are never equal to each other

				else if ( InputStream.class.isAssignableFrom( propertyType ) ) {
					continue;
				} else if ( Pattern.class.isAssignableFrom( propertyType ) ) {
					continue;
				} else if ( ClassLoader.class.isAssignableFrom( propertyType ) ) {
					continue;
				} else {
					throw new Exception( "Don't know how to test a property of " + propertyType );
				}

				int hashCodeBefore = object1.hashCode();

				if ( readMethod != null ) {

					readMethod.invoke( object1 );

					// Calling the getter should not alter the equals/hashCode, even under lazy
					// initialisation

					Assert.assertTrue( propertyName, object1.equals( object2 ) );
					Assert.assertEquals( propertyName, object1.hashCode(), hashCodeBefore );
				}

				writeMethod.invoke( object1, toSet );

				// The hashCode before and after *could* be the same, but it's not a very good idea

				Assert.assertTrue( propertyName, object1.hashCode() != hashCodeBefore );

				// Getter

				if ( readMethod != null ) {

					Assert.assertTrue( toSet.equals( readMethod.invoke( object1 ) ) );
				}

				// equals/hashCode

				Assert.assertTrue( propertyName, !object1.equals( object2 ) );

				writeMethod.invoke( object2, toSet );
				Assert.assertTrue( object1.equals( object2 ) );
				Assert.assertEquals( object1.hashCode(), object2.hashCode() );
			}
		} catch ( Exception e ) {
			throw new RuntimeException( e );
		}
	}

	//
	// Private statics
	//

	/**
	 * Gets the public/protected getter for the given <code>propertyName</code> within the given
	 * class. Traverses up the superclass heirarchy as necessary.
	 */

	private static Method getProtectedReadMethod( Class<?> clazz, String propertyName ) {

		String propertyUppercased = StringUtils.capitalize( propertyName );

		// Go looking for such a field, traversing the superclass heirarchy as necessary

		Class<?> currentClass = clazz;

		while ( currentClass != null ) {

			try {
				Method method = currentClass.getDeclaredMethod( ClassUtils.JAVABEAN_GET_PREFIX + propertyUppercased );
				method.setAccessible( true );

				return method;

			} catch ( Exception e1 ) {
				try {
					Method method = currentClass.getDeclaredMethod( ClassUtils.JAVABEAN_IS_PREFIX + propertyUppercased );

					// As per section 8.3.2 (Boolean properties) of The JavaBeans API specification, 'is'
					// only applies to boolean (little 'b')

					if ( boolean.class.equals( method.getReturnType() )) {

						method.setAccessible( true );
						return method;
					}
				} catch ( Exception e2 ) {
					// Fall through
				}
			}
			currentClass = currentClass.getSuperclass();
		}

		return null;
	}

	private static Object newProxyInstance( Class<?> clazz ) {

		return Proxy.newProxyInstance( MetawidgetTestUtils.class.getClassLoader(), new Class[] { clazz }, new InvocationHandler() {

			public Object invoke( Object proxy, Method method, Object[] args )
				throws Throwable {

				if ( "equals".equals( method.getName() ) ) {
					return ( proxy == args[0] );
				}

				if ( "hashCode".equals( method.getName() ) ) {
					return System.identityHashCode( proxy );
				}

				return null;
			}
		} );
	}

	//
	// Private constructor
	//

	private MetawidgetTestUtils() {

		// Can never be called
	}
}
