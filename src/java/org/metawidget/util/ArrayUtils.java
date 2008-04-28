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

import java.lang.reflect.Array;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Utilities for working with Arrays.
 *
 * @author Richard Kennard
 */

public final class ArrayUtils
{
	//
	//
	// Public statics
	//
	//

	public static <T> String toString( final T[] array )
	{
		return toString( array, StringUtils.SEPARATOR_COMMA );
	}

	public static <T> String toString( final T[] array, String separator )
	{
		return toString( array, separator, false, false );
	}

	public static <T> String toString( final T[] array, String separator, boolean leadingSeparator, boolean trailingSeparator )
	{
		if ( array == null )
			return "";

		String separatorEscaped = separator;

		// Workaround for bug in J2SE 1.4 - dots don't get escaped properly, even
		// when using Pattern.LITERAL

		if ( separatorEscaped.equals( "." ))
			separatorEscaped = "\\.";

		Pattern patternSeparator = Pattern.compile( separatorEscaped, Pattern.LITERAL );
		String replacement = "\\\\" + separator;

		// (use StringBuffer for J2SE 1.4 compatibility)

		StringBuffer buffer = new StringBuffer();

		for ( T t : array )
		{
			String value = String.valueOf( t );

			// Concatenate the separator

			if ( buffer.length() > 0 || leadingSeparator )
				buffer.append( separator );

			// Escape the separator

			value = patternSeparator.matcher( value ).replaceAll( replacement );

			// Build the string

			buffer.append( value );
		}

		if ( trailingSeparator && buffer.length() > 0 )
			buffer.append( separator );

		return buffer.toString();
	}

	public static String[] fromString( String array )
	{
		return fromString( array, ',' );
	}

	public static String[] fromString( String array, char separator )
	{
		if ( array == null )
			return null;

		List<String> list = CollectionUtils.fromString( array, separator );

		return list.toArray( new String[list.size()] );
	}

	@SuppressWarnings( "unchecked" )
	public static <T> T[] add( T[] array, T toAdd )
	{
		T[] newArray = (T[]) Array.newInstance( array.getClass().getComponentType(), array.length + 1 );

		System.arraycopy( array, 0, newArray, 0, array.length );
		newArray[array.length] = toAdd;

		return newArray;
	}

	@SuppressWarnings( "unchecked" )
	public static <T> T[] add( T[] array, T[] arrayToAdd )
	{
		if ( array == null )
			return arrayToAdd;

		if ( arrayToAdd == null )
			return array;

		int lengthToAdd = arrayToAdd.length;

		if ( lengthToAdd == 0 )
			return array;

		int originalLength = array.length;
		T[] newArray = (T[]) Array.newInstance( array.getClass().getComponentType(), originalLength + lengthToAdd );

		System.arraycopy( array, 0, newArray, 0, array.length );
		System.arraycopy( arrayToAdd, 0, newArray, originalLength, lengthToAdd );

		return newArray;
	}

	public static <T> boolean contains( T[] array, T contains )
	{
		return ( indexOf( array, contains ) != -1 );
	}

	public static <T> int indexOf( T[] array, T contains )
	{
		if ( array == null )
			return -1;

		for ( int index = 0; index < array.length; index++ )
		{
			Object object = array[index];

			if ( object == null )
			{
				if ( contains == null )
					return index;
			}
			else if ( object.equals( contains ) )
				return index;
		}

		return -1;
	}

	//
	//
	// Private constructor
	//
	//

	private ArrayUtils()
	{
		// Can never be called
	}
}
