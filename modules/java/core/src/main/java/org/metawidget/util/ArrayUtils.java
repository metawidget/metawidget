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

import java.lang.reflect.Array;
import java.util.List;
import java.util.regex.Pattern;

import org.metawidget.util.simple.StringUtils;

/**
 * Utilities for working with Arrays.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public final class ArrayUtils {

	//
	// Public statics
	//

	public static String toString( Object array ) {

		return toString( array, StringUtils.SEPARATOR_COMMA );
	}

	public static String toString( Object array, String separator ) {

		return toString( array, separator, false, false );
	}

	public static String toString( Object array, String separator, boolean leadingSeparator, boolean trailingSeparator ) {

		if ( array == null ) {
			return "";
		}

		if ( !array.getClass().isArray() ) {
			throw new UnsupportedOperationException( "Not an array" );
		}

		Pattern patternSeparator = Pattern.compile( separator, Pattern.LITERAL );
		String replacement = "\\\\" + separator;

		StringBuilder builder = new StringBuilder();

		for ( int loop = 0, length = Array.getLength( array ); loop < length; loop++ ) {
			String value = String.valueOf( Array.get( array, loop ) );

			// Concatenate the separator

			if ( builder.length() > 0 || leadingSeparator ) {
				builder.append( separator );
			}

			// Escape the separator

			value = patternSeparator.matcher( value ).replaceAll( replacement );

			// Build the string

			builder.append( value );
		}

		if ( trailingSeparator && builder.length() > 0 ) {
			builder.append( separator );
		}

		return builder.toString();
	}

	public static String[] fromString( String array ) {

		return fromString( array, ',' );
	}

	/**
	 * @return the parsed array. Never null
	 */

	public static String[] fromString( String array, char separator ) {

		if ( array == null ) {
			return EMPTY_STRING_ARRAY;
		}

		List<String> list = CollectionUtils.fromString( array, separator );

		return list.toArray( new String[list.size()] );
	}

	public static <T> T[] add( T[] array, T... arrayToAdd ) {

		if ( array == null ) {
			return arrayToAdd;
		}

		return addAt( array, array.length, arrayToAdd );
	}

	@SuppressWarnings( "unchecked" )
	public static <T> T[] addAt( T[] array, int index, T... arrayToAdd ) {

		if ( array == null ) {
			return arrayToAdd;
		}

		if ( arrayToAdd == null ) {
			return array;
		}

		int lengthToAdd = arrayToAdd.length;

		if ( lengthToAdd == 0 ) {
			return array;
		}

		int originalLength = array.length;
		T[] newArray = (T[]) Array.newInstance( array.getClass().getComponentType(), originalLength + lengthToAdd );

		if ( index > 0 ) {
			System.arraycopy( array, 0, newArray, 0, index );
		}

		System.arraycopy( arrayToAdd, 0, newArray, index, lengthToAdd );

		if ( index < array.length ) {
			System.arraycopy( array, index, newArray, index + lengthToAdd, array.length - index );
		}

		return newArray;
	}

	/**
	 * @return true if the given array contains the given element. False if it does not, or if the
	 *         given array is null.
	 */

	public static <T> boolean contains( T[] array, T contains ) {

		return ( indexOf( array, contains ) != -1 );
	}

	/**
	 * @return true if the given array contains the given element. False if it does not, or if the
	 *         given array is null.
	 */

	public static <T> int indexOf( T[] array, T contains ) {

		if ( array == null ) {
			return -1;
		}

		for ( int index = 0; index < array.length; index++ ) {
			Object object = array[index];

			if ( object == null ) {
				if ( contains == null ) {
					return index;
				}
			} else if ( object.equals( contains ) ) {
				return index;
			}
		}

		return -1;
	}

	@SuppressWarnings( "unchecked" )
	public static <T> T[] removeAt( T[] array, int index ) {

		T[] newArray = (T[]) Array.newInstance( array.getClass().getComponentType(), array.length - 1 );

		if ( index > 0 ) {
			System.arraycopy( array, 0, newArray, 0, index );
		}

		if ( index < array.length - 1 ) {
			System.arraycopy( array, index + 1, newArray, index, array.length - ( index + 1 ) );
		}

		return newArray;
	}

	//
	// Private statics
	//

	private static final String[]	EMPTY_STRING_ARRAY	= new String[0];

	//
	// Private constructor
	//

	private ArrayUtils() {

		// Can never be called
	}
}
