// Metawidget (licensed under LGPL)
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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.AccessControlException;
import java.util.Map;
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

		if ( byte.class.equals( clazz ) ) {
			return Byte.class;
		}

		if ( short.class.equals( clazz ) ) {
			return Short.class;
		}

		if ( int.class.equals( clazz ) ) {
			return Integer.class;
		}

		if ( long.class.equals( clazz ) ) {
			return Long.class;
		}

		if ( float.class.equals( clazz ) ) {
			return Float.class;
		}

		if ( double.class.equals( clazz ) ) {
			return Double.class;
		}

		if ( boolean.class.equals( clazz ) ) {
			return Boolean.class;
		}

		if ( char.class.equals( clazz ) ) {
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

	public static Object parseNumber( Class<?> clazz, String value ) {

		if ( value == null || "".equals( value ) ) {
			return null;
		}

		if ( byte.class.equals( clazz ) ) {
			return Byte.parseByte( value );
		}

		if ( Byte.class.equals( clazz ) ) {
			return Byte.valueOf( value );
		}

		if ( short.class.equals( clazz ) ) {
			return Short.parseShort( value );
		}

		if ( Short.class.equals( clazz ) ) {
			return Short.valueOf( value );
		}

		if ( int.class.equals( clazz ) ) {
			return Integer.parseInt( value );
		}

		if ( Integer.class.equals( clazz ) ) {
			return Integer.valueOf( value );
		}

		if ( long.class.equals( clazz ) ) {
			return Long.parseLong( value );
		}

		if ( Long.class.equals( clazz ) ) {
			return Long.valueOf( value );
		}

		if ( float.class.equals( clazz ) ) {
			return Float.parseFloat( value );
		}

		if ( Float.class.equals( clazz ) ) {
			return Float.valueOf( value );
		}

		if ( double.class.equals( clazz ) ) {
			return Double.parseDouble( value );
		}

		if ( Double.class.equals( clazz ) ) {
			return Double.valueOf( value );
		}

		throw new RuntimeException( clazz + " is not a number type" );
	}

	public static Object getNumberMinValue( Class<?> clazz ) {

		if ( byte.class.equals( clazz ) || Byte.class.equals( clazz ) ) {
			return Byte.MIN_VALUE;
		}

		if ( short.class.equals( clazz ) || Short.class.equals( clazz ) ) {
			return Short.MIN_VALUE;
		}

		if ( int.class.equals( clazz ) || Integer.class.equals( clazz ) ) {
			return Integer.MIN_VALUE;
		}

		if ( int.class.equals( clazz ) || Integer.class.equals( clazz ) ) {
			return Integer.MIN_VALUE;
		}

		if ( long.class.equals( clazz ) || Long.class.equals( clazz ) ) {
			return Long.MIN_VALUE;
		}

		if ( float.class.equals( clazz ) || Float.class.equals( clazz ) ) {
			return -Float.MAX_VALUE;
		}

		if ( double.class.equals( clazz ) || Double.class.equals( clazz ) ) {
			return -Double.MAX_VALUE;
		}

		throw new RuntimeException( clazz + " is not a number type" );
	}

	public static Object getNumberMaxValue( Class<?> clazz ) {

		if ( byte.class.equals( clazz ) || Byte.class.equals( clazz ) ) {
			return Byte.MAX_VALUE;
		}

		if ( short.class.equals( clazz ) || Short.class.equals( clazz ) ) {
			return Short.MAX_VALUE;
		}

		if ( int.class.equals( clazz ) || Integer.class.equals( clazz ) ) {
			return Integer.MAX_VALUE;
		}

		if ( int.class.equals( clazz ) || Integer.class.equals( clazz ) ) {
			return Integer.MAX_VALUE;
		}

		if ( long.class.equals( clazz ) || Long.class.equals( clazz ) ) {
			return Long.MAX_VALUE;
		}

		if ( float.class.equals( clazz ) || Float.class.equals( clazz ) ) {
			return Float.MAX_VALUE;
		}

		if ( double.class.equals( clazz ) || Double.class.equals( clazz ) ) {
			return Double.MAX_VALUE;
		}

		throw new RuntimeException( clazz + " is not a number type" );
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

	/* package private */static final Set<ClassLoader>	ALIEN_CLASSLOADERS	= CollectionUtils.newHashSet();

	/**
	 * When dealing with multiple isolated ClassLoaders, sometimes the object being inspected may
	 * reference a class that is not available to any of our own ClassLoaders. Therefore
	 * <code>Class.forName</code> will always fail. To cope with this, we record 'alien'
	 * ClassLoaders as and when we encounter them
	 */

	public static void registerAlienClassLoader( ClassLoader classLoader ) {

		// JDK classes like java.util.ArrayList have no classloader

		if ( classLoader == null ) {
			return;
		}

		// No need to register, as not alien?

		if ( classLoader.equals( Thread.currentThread().getContextClassLoader() ) ) {
			return;
		}

		if ( classLoader.equals( ClassUtils.class.getClassLoader() ) ) {
			return;
		}

		synchronized ( ALIEN_CLASSLOADERS ) {
			ALIEN_CLASSLOADERS.add( classLoader );
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
	 * <li>returns <code>null</code> if there is no such class (eg. if the name is a symbolic type,
	 * such as 'Login Screen')</li>
	 * <li>supports primitives (<code>int</code>, <code>long</code>, etc)</li>
	 * <li>supports parameterized types (strips off the &lt;...&gt;)</li>
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

		String classNameToUse = className;

		// Support parameterized type

		int indexOf = classNameToUse.indexOf( '<' );

		if ( indexOf != -1 ) {
			classNameToUse = classNameToUse.substring( 0, indexOf );
		}

		// Try given ClassLoader (may be none)

		try {
			if ( classLoader != null ) {
				return Class.forName( classNameToUse, false, classLoader );
			}
		} catch ( ClassNotFoundException e ) {

			// Fall through and try other ClassLoaders
		}

		// Try given Thread ClassLoader (may be none, such as on Android)

		ClassLoader threadClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			if ( threadClassLoader != null && !threadClassLoader.equals( classLoader ) ) {
				return Class.forName( classNameToUse, false, threadClassLoader );
			}
		} catch ( ClassNotFoundException e ) {

			// Fall through and try other ClassLoaders
		}

		// Try our own ClassLoader (if different to threadClassLoader)

		ClassLoader thisClassLoader = ClassUtils.class.getClassLoader();

		try {
			if ( !thisClassLoader.equals( threadClassLoader ) && !thisClassLoader.equals( classLoader ) ) {
				return Class.forName( classNameToUse, false, thisClassLoader );
			}
		} catch ( ClassNotFoundException e ) {

			// Fall through and try other ClassLoaders
		}

		// Try our alien ClassLoaders

		synchronized ( ALIEN_CLASSLOADERS ) {
			for ( ClassLoader alienClassLoader : ALIEN_CLASSLOADERS ) {
				try {
					return Class.forName( classNameToUse, false, alienClassLoader );
				} catch ( ClassNotFoundException e ) {

					// Fall through and try other ClassLoaders
				}
			}
		}

		return getPrimitive( classNameToUse );
	}

	public static boolean isPrimitive( String className ) {

		return ( getPrimitive( className ) != null );
	}

	/**
	 * Gets the 'simple' name of the class, given the fully qualified name.
	 * <p>
	 * Named after <code>Class.getSimpleName</code>.
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

	/**
	 * Converts <code>com.foo.Bar</code> into <code>/com/foo</code>. The leading forward slash can
	 * be important in some module loading environments (e.g. for JBoss Forge).
	 */

	public static String getPackagesAsFolderNames( Class<?> clazz ) {

		return StringUtils.SEPARATOR_FORWARD_SLASH_CHAR + clazz.getPackage().getName().replace( StringUtils.SEPARATOR_DOT_CHAR, StringUtils.SEPARATOR_FORWARD_SLASH_CHAR );
	}

	/**
	 * Gets the given annotationClass defined on the given method. If <em>no</em> annotations are
	 * defined at all (not just annotationClass) but the method is overridden, searches up the class
	 * heirarchy to original versions of the method and checks for annotationClass.
	 * <p>
	 * We work off <em>no</em> annotations being defined (rather than just annotationClass) because
	 * we still want to support explicitly overriding methods in order to suppress annotations (such
	 * as <code>UiHidden</code>). This isn't perfect, because the overridden method still has to
	 * define at least one annotation.
	 * <p>
	 * This approach is important for proxied classes, which don't always retain annotations.
	 */

	public static <T extends Annotation> T getOriginalAnnotation( Method method, Class<T> annotationClass ) {

		Map<Class<? extends Annotation>, Annotation> cache = ORIGINAL_ANNOTATION_CACHE.get( method );

		if ( cache == null ) {
			cache = CollectionUtils.newWeakHashMap();
			ORIGINAL_ANNOTATION_CACHE.put( method, cache );
		}

		// Must synchronize, as WeakHashMap is not Thread safe and WeakHashMap:383 contains an
		// infinite while: while (e != null && !(e.hash == h && eq(k, e.get())))

		synchronized ( cache ) {

			// Use 'containsKey', because we may cache it as being 'null'

			if ( cache.containsKey( annotationClass ) ) {
				@SuppressWarnings( "unchecked" )
				T annotation = (T) cache.get( annotationClass );
				return annotation;
			}

			T annotation = _getOriginalAnnotation( method, annotationClass );
			cache.put( annotationClass, annotation );

			return annotation;
		}
	}

	/**
	 * Gets the given genericReturnType defined on the given method. If no such genericReturnType is
	 * defined but the method is overridden, searches up the class heirarchy to original versions of
	 * the method, and checks for genericReturnType.
	 * <p>
	 * This approach is important for proxied classes, which don't always retain genetics.
	 */

	public static Type getOriginalGenericReturnType( Method method ) {

		Method methodToUse = method;
		String name = methodToUse.getName();
		Class<?>[] parameterTypes = methodToUse.getParameterTypes();

		do {

			// If this method has a ParameterizedType, return it...

			Type type = methodToUse.getGenericReturnType();

			if ( type instanceof ParameterizedType ) {
				return type;
			}

			// ...otherwise traverse up the hierarchy

			Class<?> superclass = methodToUse.getDeclaringClass().getSuperclass();
			methodToUse = null;

			while ( superclass != null ) {

				try {
					methodToUse = superclass.getDeclaredMethod( name, parameterTypes );
					break;
				} catch ( Exception e ) {
					// Not in this superclass, but may be in super-superclass
				}

				superclass = superclass.getSuperclass();
			}

		} while ( methodToUse != null );

		// No generic return type found. Return normal

		return method.getGenericReturnType();
	}

	/**
	 * Gets the given genericParameterTypes defined on the given method. If no such
	 * genericParameterTypes is defined but the method is overridden, searches up the class
	 * heirarchy to original versions of the method, and checks for genericParameterTypes.
	 * <p>
	 * This approach is important for proxied classes, which don't always retain genetics.
	 */

	public static Type[] getOriginalGenericParameterTypes( Method method ) {

		Method methodToUse = method;
		String name = methodToUse.getName();
		Class<?>[] parameterTypes = methodToUse.getParameterTypes();

		do {

			// If this method has a ParameterizedType, return it...

			Type[] type = methodToUse.getGenericParameterTypes();

			if ( type[0] instanceof ParameterizedType ) {
				return type;
			}

			// ...otherwise traverse up the hierarchy

			Class<?> superclass = methodToUse.getDeclaringClass().getSuperclass();
			methodToUse = null;

			while ( superclass != null ) {

				try {
					methodToUse = superclass.getDeclaredMethod( name, parameterTypes );
					break;
				} catch ( Exception e ) {
					// Not in this superclass, but may be in super-superclass
				}

				superclass = superclass.getSuperclass();
			}

		} while ( methodToUse != null );

		// No generic parameter types found. Return normal

		return method.getGenericParameterTypes();
	}

	/**
	 * Converts a <code>java.lang.reflect.Type</code>, as returned by <code>getGenericType</code>
	 * into a String representation.
	 */

	public static String getGenericTypeAsString( Type type ) {

		if ( !( type instanceof ParameterizedType ) ) {
			return null;
		}

		Type[] typeActuals = null;

		try {
			typeActuals = ( (ParameterizedType) type ).getActualTypeArguments();
		} catch ( Exception e ) {
			// Android 1.1_r1 fails here with a ClassNotFoundException
		}

		if ( typeActuals == null || typeActuals.length == 0 ) {
			return null;
		}

		StringBuilder builder = new StringBuilder();

		for ( Type typeActual : typeActuals ) {
			// Android 1.1_r1 sometimes provides null typeActuals while
			// testing the AddressBook application

			if ( typeActual == null ) {
				continue;
			}

			if ( builder.length() > 0 ) {
				builder.append( StringUtils.SEPARATOR_COMMA );
			}

			if ( typeActual instanceof Class<?> ) {
				builder.append( ( (Class<?>) typeActual ).getName() );
			} else {
				builder.append( typeActual.toString() );
			}
		}

		return builder.toString();
	}

	//
	// Private statics
	//

	private static final Map<Method, Map<Class<? extends Annotation>, Annotation>>	ORIGINAL_ANNOTATION_CACHE	= CollectionUtils.newHashMap();

	/**
	 * We found <code>getOriginalAnnotation</code> to be around 10x slower that just
	 * <code>method.getAnnotation</code>, so we cache it.
	 */

	private static <T extends Annotation> T _getOriginalAnnotation( Method method, Class<T> annotationClass ) {

		Method methodToUse = method;
		String name = methodToUse.getName();
		Class<?>[] parameterTypes = methodToUse.getParameterTypes();

		// If no annotations are defined at all, traverse up the hierarchy

		while ( methodToUse.getAnnotations().length == 0 ) {

			Class<?> superclass = methodToUse.getDeclaringClass().getSuperclass();
			methodToUse = null;

			while ( superclass != null ) {

				try {
					methodToUse = superclass.getDeclaredMethod( name, parameterTypes );
					break;
				} catch ( Exception e ) {
					// Not in this superclass, but may be in super-superclass
				}

				superclass = superclass.getSuperclass();
			}

			if ( methodToUse == null ) {
				break;
			}
		}

		// If this method has the annotation, return it

		if ( methodToUse != null ) {
			T annotation = methodToUse.getAnnotation( annotationClass );

			if ( annotation != null ) {
				return annotation;
			}
		}

		// Try interfaces too, in case annotation is defined there

		for ( Class<?> iface : method.getDeclaringClass().getInterfaces() ) {

			try {
				methodToUse = iface.getDeclaredMethod( name, parameterTypes );
				T annotation = methodToUse.getAnnotation( annotationClass );

				if ( annotation != null ) {
					return annotation;
				}
			} catch ( Exception e ) {
				// Not in this interface
			}
		}

		// No annotation found

		return null;
	}

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
