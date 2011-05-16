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

package org.metawidget.util.simple;

/**
 * Utilities for working with Objects.
 *
 * @author Richard Kennard
 */

public final class ObjectUtils {

	//
	// Public statics
	//

	/**
	 * @return true if object1 is the same class (not assignable subclass) as object2. False
	 *         otherwise.
	 */

	public static boolean nullSafeClassEquals( Object object1, Object object2 ) {

		if ( object1 == null || object2 == null ) {
			return false;
		}

		if ( object1.getClass() != object2.getClass() ) {
			return false;
		}

		return true;
	}

	/**
	 * @return 0 if toHash is null, otherwise returns toHash.hashCode()
	 */

	public static int nullSafeHashCode( Object toHash ) {

		if ( toHash == null ) {
			return 0;
		}

		// Array.hashCode is broken by default

		if ( toHash.getClass().isArray() ) {
			return deepHashCode( (Object[]) toHash );
		}

		return toHash.hashCode();
	}

	/**
	 * Performs an equals() check on two Objects, either (or both) of which may be null.
	 * <p>
	 * This method returns <code>true</code> if both Objects are null. Whether, strictly speaking,
	 * null == null is debatable, but this approach is useful for comparing two parent Objects who
	 * have both left certain properties as null. Generally we want those two parent Objects to be
	 * considered equal.
	 *
	 * @return true if both object1 and object2 are null, false if one is null and the other is not,
	 *         otherwise returns object1.equals( object2 )
	 */

	public static boolean nullSafeEquals( Object object1, Object object2 ) {

		if ( object1 == null ) {
			return ( object2 == null );
		}

		// Array.equals is broken by default

		if ( object1.getClass().isArray() ) {
			if ( object2 == null || !object2.getClass().isArray() ) {
				return false;
			}

			return deepEquals( (Object[]) object1, (Object[]) object2 );
		}

		return object1.equals( object2 );
	}

	/**
	 * @return 0 if both object1 and object2 are null, -1 if object1 is null but object2 is not
	 *         (sorts nulls to the top), 1 if object2 is null but object1 is not, otherwise returns
	 *         object1.compareTo( object2 )
	 */

	public static <T> int nullSafeCompareTo( Comparable<T> object1, T object2 ) {

		if ( object1 == null ) {
			if ( object2 == null ) {
				return 0;
			}

			return -1;
		} else if ( object2 == null ) {
			return 1;
		}

		return object1.compareTo( object2 );
	}

	//
	// Private methods
	//

	/**
	 * Copied from <code>Arrays.deepHashCode</code>. <code>Arrays.deepHashCode</code> not supported
	 * by GWT 1.7.
	 */

	private static int deepHashCode( Object a[] ) {

		if ( a == null ) {
			return 0;
		}

		int result = 1;

		for ( Object element : a ) {
			int elementHash = 0;
			if ( element instanceof Object[] ) {
				elementHash = deepHashCode( (Object[]) element );
			} else if ( element != null ) {
				elementHash = element.hashCode();
			}

			result = 31 * result + elementHash;
		}

		return result;
	}

	/**
	 * Copied from <code>Arrays.deepEquals</code>. <code>Arrays.deepEquals</code> not supported by
	 * GWT 1.7.
	 */

	private static boolean deepEquals( Object[] a1, Object[] a2 ) {

		if ( a1 == a2 ) {
			return true;
		}
		if ( a1 == null || a2 == null ) {
			return false;
		}
		int length = a1.length;
		if ( a2.length != length ) {
			return false;
		}

		for ( int i = 0; i < length; i++ ) {
			Object e1 = a1[i];
			Object e2 = a2[i];

			if ( e1 == e2 ) {
				continue;
			}
			if ( e1 == null ) {
				return false;
			}

			// Figure out whether the two elements are equal
			boolean eq;
			if ( e1 instanceof Object[] && e2 instanceof Object[] ) {
				eq = deepEquals( (Object[]) e1, (Object[]) e2 );
			} else {
				eq = e1.equals( e2 );
			}

			if ( !eq ) {
				return false;
			}
		}
		return true;
	}

	//
	// Private constructor
	//

	private ObjectUtils() {

		// Can never be called
	}
}
