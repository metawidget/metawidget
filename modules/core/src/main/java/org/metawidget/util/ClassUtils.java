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
import java.util.Set;

import org.metawidget.util.simple.StringUtils;

/**
 * Utilities for working with Classes.
 *
 * @author Richard Kennard
 */

public final class ClassUtils {

	//
	// Public statics
	//

	public static final String	JAVABEAN_SET_PREFIX	= "set";

	public static final String	JAVABEAN_GET_PREFIX	= "get";

	public static final String	JAVABEAN_IS_PREFIX	= "is";

	/**
	 * Lookup JavaBean-convention getter without using <code>java.beans</code>, as that package is
	 * not available on all target platforms.
	 */

	public static Method getReadMethod( Class<?> clazz, String property ) {

		String propertyUppercased = StringUtils.capitalize( property );

		try {
			return clazz.getMethod( JAVABEAN_GET_PREFIX + propertyUppercased );
		} catch ( Exception e1 ) {
			try {
				Method method = clazz.getMethod( JAVABEAN_IS_PREFIX + propertyUppercased );

				// As per section 8.3.2 (Boolean properties) of The JavaBeans API specification,
				// 'is' only applies to boolean (little 'b')

				if ( boolean.class.equals( method.getReturnType() ) ) {
					return method;
				}

			} catch ( Exception e2 ) {
				// Fall through
			}
		}

		throw new RuntimeException( "No such method " + JAVABEAN_GET_PREFIX + propertyUppercased + " (or boolean " + JAVABEAN_IS_PREFIX + propertyUppercased + ") on " + clazz );
	}

	/**
	 * Lookup JavaBean-convention setter without using <code>java.beans</code>, as that package is
	 * not available on all target platforms.
	 */

	public static Method getWriteMethod( Class<?> clazz, String property, Class<?> type ) {

		String propertyUppercased = StringUtils.capitalize( property );

		// First, try and match based on subtypes of the property type

		Class<?> typeSuper = type;

		while ( typeSuper != null ) {
			try {
				return clazz.getMethod( JAVABEAN_SET_PREFIX + propertyUppercased, typeSuper );
			} catch ( Exception e ) {
				typeSuper = typeSuper.getSuperclass();
			}
		}

		// Next, try and match based on interfaces of the property type

		for ( Class<?> anInterface : type.getInterfaces() ) {
			try {
				return clazz.getMethod( JAVABEAN_SET_PREFIX + propertyUppercased, anInterface );
			} catch ( Exception e ) {
				// Keep trying
			}
		}

		throw new RuntimeException( "No such method " + JAVABEAN_SET_PREFIX + propertyUppercased + "( " + type.getName() + " ) on " + clazz );
	}

	/**
	 * Get the value of the JavaBean-convention property without using <code>java.beans</code>, as
	 * that package is not available on all target platforms.
	 */

	@SuppressWarnings( "unchecked" )
	public static <T> T getProperty( Object base, String property ) {

		try {
			Method method = getReadMethod( base.getClass(), property );
			return (T) method.invoke( base );
		} catch ( Exception e ) {
			if ( base == null ) {
				throw new RuntimeException( "Unable to get '" + property + "' because base is null", e );
			}

			throw new RuntimeException( "Unable to get '" + property + "' of '" + base + "' (" + base.getClass() + ")", e );
		}
	}

	/**
	 * Set the value of the JavaBean-convention property without using <code>java.beans</code>, as
	 * that package is not available on all target platforms.
	 */

	public static void setProperty( Object base, String property, Object value ) {

		try {
			Class<?> baseClass = base.getClass();

			// Determine the type based on the 'read' method, not the value.getClass(), because
			// that is unreliable for 'Integer' versus 'int'

			Method method = getReadMethod( baseClass, property );
			method = getWriteMethod( baseClass, property, method.getReturnType() );
			method.invoke( base, value );
		} catch ( Exception e ) {
			throw new RuntimeException( "Unable to set '" + property + "' of '" + base + "' to '" + value + "'", e );
		}
	}

	/**
	 * Returns <code>true</code> if the given class is the Object-wrapper of a primitive type (eg.
	 * <code>Integer</code> for <code>int</code>).
	 * <p>
	 * We want to be able to distinguish these, because we can't usefully drill into them and may do
	 * better to 'inspect from parent' (see <code>UIMetawidget</code>).
	 */

	public static boolean isPrimitiveWrapper( Class<?> clazz ) {

		if ( Number.class.isAssignableFrom( clazz ) ) {
			return true;
		}

		if ( Boolean.class.isAssignableFrom( clazz ) ) {
			return true;
		}

		if ( Character.class.isAssignableFrom( clazz ) ) {
			return true;
		}

		return false;
	}

	/**
	 * Returns the wrapper class for a primitive class (eg. <code>Integer.class</code> for
	 * <code>int.class</code>)
	 */

	public static Class<?> getWrapperClass( Class<?> clazz ) {

		if ( clazz.equals( byte.class ) ) {
			return Byte.class;
		}

		if ( clazz.equals( short.class ) ) {
			return Short.class;
		}

		if ( clazz.equals( int.class ) ) {
			return Integer.class;
		}

		if ( clazz.equals( long.class ) ) {
			return Long.class;
		}

		if ( clazz.equals( float.class ) ) {
			return Float.class;
		}

		if ( clazz.equals( double.class ) ) {
			return Double.class;
		}

		if ( clazz.equals( boolean.class ) ) {
			return Boolean.class;
		}

		if ( clazz.equals( char.class ) ) {
			return Character.class;
		}

		throw new RuntimeException( clazz + " is not a primitive type" );
	}

	/**
	 * Gracefully test whether a class exists. Returns true or false, rather than throwing
	 * <code>ClassNotFoundException</code>.
	 */

	public static boolean classExists( String clazz ) {

		try {
			Class.forName( clazz, false, null );

			return true;
		} catch ( ClassNotFoundException e ) {
			return false;
		} catch ( AccessControlException e ) {
			// Not accessible (eg. running in an applet)

			return false;
		}
	}

	/**
	 * When dealing with multiple isolated ClassLoaders, sometimes the object being inspected may
	 * reference a class that is not available to any of our own ClassLoaders. Therefore
	 * <code>Class.forName</code> will always fail. To cope with this, we
	 * record 'alien' ClassLoaders as and when we encounter them.
	 * <p>
	 * This is a static Set, rather than a ThreadLocal, because we couldn't find a good place to
	 * reset the ThreadLocal.
	 */

	private static final Set<ClassLoader>	ALIEN_CLASSLOADERS	= CollectionUtils.newHashSet();

	/**
	 * When dealing with multiple isolated ClassLoaders, sometimes the object being inspected may
	 * reference a class that is not available to any of our own ClassLoaders. Therefore
	 * <code>Class.forName</code> will always fail. To cope with this, we
	 * record 'alien' ClassLoaders as and when we encounter them
	 */

	// TODO: unit test this

	public static void registerAlienClassLoader( ClassLoader classLoader ) {

		// (JDK classes like java.util.ArrayList have no classloader)

		if ( classLoader == null ) {
			return;
		}

		if ( classLoader.equals( Thread.currentThread().getContextClassLoader() ) ) {
			return;
		}

		if ( classLoader.equals( ClassUtils.class.getClassLoader() ) ) {
			return;
		}

		if ( !"false".equals( System.getProperty( "register.alien.classloaders" ))) {
			synchronized ( ALIEN_CLASSLOADERS ) {
				ALIEN_CLASSLOADERS.add( classLoader );
			}
		}
	}

	/**
	 * Replacement for <code>Class.forName()</code> that:
	 * <ul>
	 * <li>supports primitives (<code>int</code>, <code>long</code>, etc)</li>
	 * <li>returns <code>null</code> if there is no such class (eg. if the name is a symbolic type,
	 * such as 'Login Screen')</li>
	 * </ul>
	 */

	public static Class<?> niceForName( String className ) {

		return niceForName( className, null );
	}

	/**
	 * Replacement for <code>Class.forName()</code> that:
	 * <ul>
	 * <li>supports primitives (<code>int</code>, <code>long</code>, etc)</li>
	 * <li>returns <code>null</code> if there is no such class (eg. if the name is a symbolic type,
	 * such as 'Login Screen')</li>
	 * </ul>
	 * <p>
	 * This method first tries to load the class using the given ClassLoader (if any). If that
	 * fails, it then tries the Thread's current ClassLoader (this works best for EJB/WAR splits
	 * where, say, metawidget-core and metawidget-annotations are located in the EJB/lib and the
	 * other modules are located in the WAR/lib). If that fails, it tries ClassUtils' ClassLoader.
	 * If that fails, it tries our alien ClassLoader.
	 *
	 * @param classLoader
	 *            the specific ClassLoader to use to try and load this class. In general clients
	 *            should use the other form of this method, which will default to trying the current
	 *            Thread's ClassLoader. Can be null.
	 * @throws NullPointerException
	 *             if className is null
	 */

	public static Class<?> niceForName( String className, ClassLoader classLoader ) {

		// Try given ClassLoader (may be none)

		try {
			if ( classLoader != null ) {
				return Class.forName( className, false, classLoader );
			}
		} catch ( ClassNotFoundException e ) {

			// Fall through and try other ClassLoaders
		}

		// Try given Thread ClassLoader (may be none, such as on Android)

		ClassLoader threadClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			if ( threadClassLoader != null && !threadClassLoader.equals( classLoader ) ) {
				return Class.forName( className, false, threadClassLoader );
			}
		} catch ( ClassNotFoundException e ) {

			// Fall through and try other ClassLoaders
		}

		// Try our own ClassLoader (if different to threadClassLoader)

		ClassLoader thisClassLoader = ClassUtils.class.getClassLoader();

		try {
			if ( !thisClassLoader.equals( threadClassLoader ) && !thisClassLoader.equals( classLoader )) {
				return Class.forName( className, false, thisClassLoader );
			}
		} catch ( ClassNotFoundException e ) {

			// Fall through and try other ClassLoaders
		}

		// Try our alien ClassLoaders

		synchronized ( ALIEN_CLASSLOADERS ) {
			for ( ClassLoader alienClassLoader : ALIEN_CLASSLOADERS ) {
				try {
					return Class.forName( className, false, alienClassLoader );
				} catch ( ClassNotFoundException e ) {

					// Fall through and try other ClassLoaders
				}
			}
		}

		return getPrimitive( className );
	}

	public static boolean isPrimitive( String className ) {

		return ( getPrimitive( className ) != null );
	}

	/**
	 * Gets the 'simple' name of the class.
	 * <p>
	 * Essentially a simplified version of <code>Class.getSimpleName</code>, which is JDK
	 * 1.5-specific.
	 */

	public static String getSimpleName( Class<?> clazz ) {

		return getSimpleName( clazz.getName() );
	}

	/**
	 * Gets the 'simple' name of the class, given the fully qualified name.
	 * <p>
	 * Essentially a simplified version of <code>Class.getSimpleName</code>, which is JDK
	 * 1.5-specific.
	 */

	public static String getSimpleName( String qualifiedClassName ) {

		return StringUtils.substringAfterLast( qualifiedClassName, StringUtils.SEPARATOR_DOT_CHAR );
	}

	/**
	 * Similar to <code>Method.getDeclaringClass</code>, but traverses the class heirarchy to find
	 * the class that <em>originally</em> declared this method, in the event the implementation has
	 * been overridden.
	 */

	public static Class<?> getOriginalDeclaringClass( Method method ) {

		Class<?> declaringClass = method.getDeclaringClass();
		Class<?> superclass = declaringClass.getSuperclass();

		while ( superclass != null ) {

			try {
				superclass.getDeclaredMethod( method.getName(), method.getParameterTypes() );
				declaringClass = superclass;
			} catch ( Exception e ) {
				// Not in this superclass, but may be in super-superclass
			}

			superclass = superclass.getSuperclass();
		}

		return declaringClass;
	}

	public static String getPackagesAsFolderNames( Class<?> clazz ) {

		return clazz.getPackage().getName().replace( StringUtils.SEPARATOR_DOT_CHAR, StringUtils.SEPARATOR_FORWARD_SLASH_CHAR );
	}

	//
	// Private statics
	//

	private static Class<?> getPrimitive( String className ) {

		if ( "byte".equals( className ) ) {
			return byte.class;
		}

		if ( "short".equals( className ) ) {
			return short.class;
		}

		if ( "int".equals( className ) ) {
			return int.class;
		}

		if ( "long".equals( className ) ) {
			return long.class;
		}

		if ( "float".equals( className ) ) {
			return float.class;
		}

		if ( "double".equals( className ) ) {
			return double.class;
		}

		if ( "boolean".equals( className ) ) {
			return boolean.class;
		}

		if ( "char".equals( className ) ) {
			return char.class;
		}

		return null;
	}

	//
	// Private constructor
	//

	private ClassUtils() {

		// Can never be called
	}
}
