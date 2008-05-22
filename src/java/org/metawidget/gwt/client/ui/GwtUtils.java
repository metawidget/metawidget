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

package org.metawidget.gwt.client.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Utilities for working with Google Web Toolkit.
 * <p>
 * These utility methods are copies of those defined in <code>org.metawidget.util</code>, but
 * they either are 'GWT flavoured' (eg. they use <code>com.google.gwt.xml</code> instead of
 * <code>org.w3c.dom</code>) or they are free encumberances (eg. <code>java.util.regex</code>)
 * that GWT doesn't support.
 *
 * @author Richard Kennard
 */

public final class GwtUtils
{
	//
	//
	// Public statics
	//
	//

	public static boolean isPrimitive( String className )
	{
		if ( "byte".equals( className ) || "short".equals( className ) )
			return true;

		if ( "int".equals( className ) || "long".equals( className ) )
			return true;

		if ( "float".equals( className ) || "double".equals( className ) )
			return true;

		if ( "boolean".equals( className ) )
			return true;

		if ( "char".equals( className ) )
			return true;

		return false;
	}

	public static boolean isPrimitiveWrapper( String className )
	{
		if ( Byte.class.getName().equals( className ) || Short.class.getName().equals( className ) )
			return true;

		if ( Integer.class.getName().equals( className ) || Long.class.getName().equals( className ) )
			return true;

		if ( Float.class.getName().equals( className ) || Double.class.getName().equals( className ) )
			return true;

		if ( Boolean.class.getName().equals( className ) )
			return true;

		if ( Character.class.getName().equals( className ) )
			return true;

		return false;
	}

	public static boolean isCollection( String className )
	{
		if ( Collection.class.getName().equals( className ))
			return true;

		if ( List.class.getName().equals( className ))
			return true;

		if ( ArrayList.class.getName().equals( className ))
			return true;

		if ( Set.class.getName().equals( className ))
			return true;

		if ( HashSet.class.getName().equals( className ))
			return true;

		if ( Map.class.getName().equals( className ))
			return true;

		if ( HashMap.class.getName().equals( className ))
			return true;

		return false;
	}

	/**
	 * Splits and trims a list of Strings.
	 */

	public static List<String> fromString( String collection, char separator )
	{
		if ( collection == null || "".equals( collection ))
		{
			// (use Collections.EMPTY_LIST, not Collections.emptyList, so that we're 1.4 compatible)

			@SuppressWarnings( { "cast", "unchecked" })
			List<String> list = (List<String>) Collections.EMPTY_LIST;
			return list;
		}

		List<String> split = new ArrayList<String>();

		for ( String item : collection.split( String.valueOf( separator ) ) )
		{
			split.add( item.trim() );
		}

		return split;
	}

	public static String toString( String[] collection, char separator )
	{
		StringBuilder builder = new StringBuilder();

		for ( String item : collection )
		{
			if ( builder.length() > 0 )
				builder.append( separator );

			builder.append( item );
		}

		return builder.toString();
	}

	public static String toString( Collection<?> collection, char separator )
	{
		if ( collection == null )
			return "";

		StringBuilder builder = new StringBuilder();

		for ( Object item : collection )
		{
			if ( builder.length() > 0 )
				builder.append( separator );

			builder.append( item );
		}

		return builder.toString();
	}

	public static Object[] parsePath( String path, char separator )
	{
		int indexOfTypeEnd = path.indexOf( separator );

		// Just type?

		if ( indexOfTypeEnd == -1 )
			return new String[] { path, null };

		String type = path.substring( 0, indexOfTypeEnd );

		// Parse names

		int indexOfNameEnd = indexOfTypeEnd;

		List<String> names = new ArrayList<String>();

		while ( true )
		{
			int indexOfNameStart = indexOfNameEnd + 1;
			indexOfNameEnd = path.indexOf( separator, indexOfNameStart );

			if ( indexOfNameEnd == -1 )
			{
				names.add( path.substring( indexOfNameStart ) );
				break;
			}

			names.add( path.substring( indexOfNameStart, indexOfNameEnd ) );
		}

		if ( names.isEmpty() )
			return new Object[] { path, null };

		return new Object[] { type, names.toArray( new String[names.size()] ) };
	}

	@SuppressWarnings( "unchecked" )
	public static String[] add( String[] array, String toAdd )
	{
		String[] newArray = new String[array.length + 1];

		System.arraycopy( array, 0, newArray, 0, array.length );
		newArray[array.length] = toAdd;

		return newArray;
	}

	//
	//
	// Private constructor
	//
	//

	private GwtUtils()
	{
		// Can never be called
	}
}
