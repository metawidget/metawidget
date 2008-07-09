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
import java.util.List;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ListBox;

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

	/**
	 * GWT-ified <code>Class.isPrimitive</code>.
	 * <p>
	 * This version takes a String argument, because we won't have been able to use Class.forName to
	 * create a Class.
	 */

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

	/**
	 * GWT-ified <code>ClassUtils.isPrimitiveWrapper</code>.
	 * <p>
	 * This version takes a String argument, not a Class argument.
	 */

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

	/**
	 * GWT-ified <code>CollectionUtils.fromString</code>.
	 * <p>
	 * This version does not use regular expressions.
	 */

	public static List<String> fromString( String collection, char separator )
	{
		if ( collection == null || "".equals( collection ) )
		{
			// (use Collections.EMPTY_LIST, not Collections.emptyList, so that we're 1.4 compatible)

			@SuppressWarnings( { "cast", "unchecked" } )
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

	/**
	 * GWT-ified <code>ArrayUtils.fromString</code>.
	 * <p>
	 * This version uses <code>GwtUtils.fromString</code>.
	 */

	public static String[] fromStringToArray( String array, char separator )
	{
		if ( array == null )
			return EMPTY_STRING_ARRAY;

		List<String> list = GwtUtils.fromString( array, separator );

		return list.toArray( new String[list.size()] );
	}

	/**
	 * GWT-ified <code>CollectionUtils.toString</code>.
	 * <p>
	 * This version does not use regular expressions.
	 */

	public static String toString( String[] collection, char separator )
	{
		if ( collection == null )
			return "";

		StringBuilder builder = new StringBuilder();

		for ( String item : collection )
		{
			if ( builder.length() > 0 )
				builder.append( separator );

			builder.append( item );
		}

		return builder.toString();
	}

	/**
	 * GWT-ified <code>CollectionUtils.toString</code>.
	 * <p>
	 * This version does not use regular expressions.
	 */

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

	public static void setListBoxSelectedItem( ListBox listBox, String value )
	{
		for ( int loop = 0, length = listBox.getItemCount(); loop < length; loop++ )
		{
			if ( value.equals( listBox.getValue( loop ) ) )
			{
				listBox.setSelectedIndex( loop );
				return;
			}
		}

		throw new RuntimeException( "'" + value + "' is not a valid value for the ListBox" );
	}

	public static void alert( Throwable caught )
	{
		StringBuilder builder = new StringBuilder( caught.getClass().getName() );

		if ( caught.getMessage() != null )
		{
			builder.append( ": " );
			builder.append( caught.getMessage() );
		}

		for ( Object item : caught.getStackTrace() )
		{
			builder.append( "\n\t" );
			builder.append( item );
		}

		Window.alert( builder.toString() );
	}

	//
	//
	// Private statics
	//
	//

	private final static String[]	EMPTY_STRING_ARRAY	= new String[0];

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
