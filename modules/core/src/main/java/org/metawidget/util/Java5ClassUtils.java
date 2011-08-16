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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Utilities for working with Java 5 Classes.
 *
 * @author Richard Kennard
 */

public final class Java5ClassUtils {

	//
	// Public statics
	//

	/**
	 * Gets the given annotationClass defined on the given method. If <em>no</em> annotations are
	 * defined at all (not just annotationClass) but the method is overridden, searches up the class
	 * heirarchy to original versions of the method and checks for annotationClass.
	 * <p>
	 * We work off <em>no</em> annotations being defined (rather than just annotationClass) because
	 * we still want to support explicitly overriding methods in order to suppress annotations (such
	 * as UiHidden). This isn't perfect, because the overridden method still has to define at least
	 * one annotation.
	 * <p>
	 * This approach is important for proxied classes, which don't always retain annotations.
	 */

	public static <T extends Annotation> T getOriginalAnnotation( Method method, Class<T> annotationClass ) {

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

	//
	// Private constructor
	//

	private Java5ClassUtils() {

		// Can never be called
	}
}
