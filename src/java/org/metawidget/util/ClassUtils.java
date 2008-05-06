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

import java.lang.reflect.Method;
import java.security.AccessControlException;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Utilities for working with Classes.
 *
 * @author Richard Kennard
 */

public final class ClassUtils
{
	//
	//
	// Public statics
	//
	//

	public final static String	JAVABEAN_SET_PREFIX		= "set";

	public final static String	JAVABEAN_GET_PREFIX		= "get";

	public final static String	JAVABEAN_IS_PREFIX		= "is";

	public final static Pattern	DEFAULT_PROXY_PATTERN	= Pattern.compile( "ByCGLIB\\$\\$|_\\$\\$_javassist_" );

	/**
	 * Lookup JavaBean-convention getter without using <code>java.beans</code>, as that package
	 * is not available on all target platforms.
	 */

	@SuppressWarnings( "unchecked" )
	public static Method getReadMethod( Class clazz, String property )
	{
		String propertyUppercased = StringUtils.uppercaseFirstLetter( property );

		try
		{
			return clazz.getMethod( JAVABEAN_GET_PREFIX + propertyUppercased );
		}
		catch ( Exception e1 )
		{
			try
			{
				return clazz.getMethod( JAVABEAN_IS_PREFIX + propertyUppercased );
			}
			catch ( Exception e2 )
			{
				throw new RuntimeException( "No such method " + JAVABEAN_GET_PREFIX + propertyUppercased + "() or " + JAVABEAN_IS_PREFIX + propertyUppercased + "() on " + clazz, e1 );
			}
		}
	}

	/**
	 * Lookup JavaBean-convention setter without using <code>java.beans</code>, as that package
	 * is not available on all target platforms.
	 */

	@SuppressWarnings( "unchecked" )
	public static Method getWriteMethod( Class clazz, String property, Class type )
	{
		String propertyUppercased = StringUtils.uppercaseFirstLetter( property );

		// First, try and match based on subtypes of the property type

		Class typeSuper = type;

		while ( typeSuper != null )
		{
			try
			{
				return clazz.getMethod( JAVABEAN_SET_PREFIX + propertyUppercased, typeSuper );
			}
			catch ( Throwable t )
			{
				typeSuper = typeSuper.getSuperclass();
			}
		}

		// Next, try and match based on interfaces of the property type

		for ( Class anInterface : type.getInterfaces() )
		{
			try
			{
				return clazz.getMethod( JAVABEAN_SET_PREFIX + propertyUppercased, anInterface );
			}
			catch ( Throwable t )
			{
				// Keep trying
			}
		}

		throw new RuntimeException( "No such method " + JAVABEAN_SET_PREFIX + propertyUppercased + "( " + type.getName() + " ) on " + clazz );
	}

	/**
	 * Get the value of the JavaBean-convention property without using <code>java.beans</code>,
	 * as that package is not available on all target platforms.
	 */

	public static Object getProperty( Object base, String property )
	{
		try
		{
			Method method = getReadMethod( base.getClass(), property );
			return method.invoke( base );
		}
		catch ( Exception e )
		{
			throw new RuntimeException( "Unable to get '" + property + "' of '" + base + "'", e );
		}
	}

	/**
	 * Set the value of the JavaBean-convention property without using <code>java.beans</code>,
	 * as that package is not available on all target platforms.
	 */

	public static void setProperty( Object base, String property, Object value )
	{
		try
		{
			Method method;
			Class<?> baseClass = base.getClass();

			if ( value == null )
			{
				method = getReadMethod( baseClass, property );
				method = getWriteMethod( baseClass, property, method.getReturnType() );
			}
			else
			{
				method = getWriteMethod( baseClass, property, value.getClass() );
			}

			method.invoke( base, value );
		}
		catch ( Throwable t )
		{
			throw new RuntimeException( "Unable to set '" + property + "' of '" + base + "' to '" + value + "'", t );
		}
	}

	/**
	 * Returns <code>true</code> if the given class is the Object-version of a primitive type (eg.
	 * <code>Integer</code> for <code>int</code>).
	 * <p>
	 * We want to be able to distinguish these, because we can't usefully drill into them and may do
	 * better to 'inspect from parent' (see <code>UIMetawidget</code>).
	 */

	public static boolean isObjectPrimitive( Class<?> clazz )
	{
		if ( Number.class.isAssignableFrom( clazz ) )
			return true;

		if ( Boolean.class.isAssignableFrom( clazz ) )
			return true;

		if ( Character.class.isAssignableFrom( clazz ) )
			return true;

		return false;
	}

	/**
	 * Gracefully test whether a class exists. Returns true or false, rather than throwing
	 * <code>ClassNotFoundException</code>.
	 */

	public static boolean classExists( String clazz )
	{
		try
		{
			Class.forName( clazz, false, null );

			return true;
		}
		catch ( ClassNotFoundException e )
		{
			return false;
		}
		catch ( AccessControlException e )
		{
			// Not accessible (eg. running in an applet)

			return false;
		}
	}

	/**
	 * Return the unproxied version of a class proxied by, say, CGLIB or Javassist.
	 */

	public static Class<?> getUnproxiedClass( Class<?> clazz )
	{
		return getUnproxiedClass( clazz, DEFAULT_PROXY_PATTERN );
	}

	/**
	 * Return the unproxied version of a class proxied by, say, CGLIB or Javassist.
	 */

	public static Class<?> getUnproxiedClass( Class<?> clazz, Pattern proxyPattern )
	{
		if ( proxyPattern == null || !proxyPattern.matcher( clazz.getName() ).find() )
			return clazz;

		return clazz.getSuperclass();
	}

	/**
	 * Replacement for <code>Class.forName()</code> that:
	 * <ul>
	 * <li>supports primitives (<code>int</code>, <code>long</code>, etc)</li>
	 * <li>returns <code>null</code> if there is no such class (eg. if the name is a symbolic
	 * type, such as 'Login Screen')</li>
	 * </ul>
	 */

	public static Class<?> niceForName( String className )
	{
		Class<?> clazz;

		try
		{
			// Use Thread.currentThread().getContextClassLoader(), in case metawidget.jar
			// is in JRE/lib/ext (which it might be for desktop apps)

			clazz = Thread.currentThread().getContextClassLoader().loadClass( className );
		}
		catch ( ClassNotFoundException e )
		{
			clazz = PRIMITIVE_CLASSNAMES.get( className );
		}

		return clazz;
	}

	//
	//
	// Private statics
	//
	//

	/**
	 * Map of primitive names (eg. <code>int</code>, <code>long</code> and their classes (eg.
	 * <code>int.class</code>, <code>long.class</code>). Allows quicker lookups during
	 * <code>niceForName</code>.
	 */

	private final static Map<String, Class<?>>	PRIMITIVE_CLASSNAMES;

	static
	{
		Map<String, Class<?>> primitiveClassnames = CollectionUtils.newHashMap();
		primitiveClassnames.put( "byte", byte.class );
		primitiveClassnames.put( "short", short.class );
		primitiveClassnames.put( "int", int.class );
		primitiveClassnames.put( "long", long.class );
		primitiveClassnames.put( "float", float.class );
		primitiveClassnames.put( "double", double.class );
		primitiveClassnames.put( "boolean", boolean.class );
		primitiveClassnames.put( "char", char.class );

		PRIMITIVE_CLASSNAMES = Collections.unmodifiableMap( primitiveClassnames );
	}

	//
	//
	// Private constructor
	//
	//

	private ClassUtils()
	{
		// Can never be called
	}
}
