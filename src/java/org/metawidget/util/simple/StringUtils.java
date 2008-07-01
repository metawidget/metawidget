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
 * Utilities for working with Strings.
 *
 * @author Richard Kennard
 */

public final class StringUtils
{
	//
	//
	// Public statics
	//
	//

	/**
	 * Forward slash character.
	 * <p>
	 * For environments that use the fully qualified class name (eg. SwingMetawidget) as part of the
	 * path, we must use '/' not '.' as the separator.
	 */

	public final static char	SEPARATOR_FORWARD_SLASH_CHAR	= '/';

	public final static String	SEPARATOR_FORWARD_SLASH			= String.valueOf( SEPARATOR_FORWARD_SLASH_CHAR );

	public final static char	SEPARATOR_DOT_CHAR				= '.';

	public final static String	SEPARATOR_DOT					= String.valueOf( SEPARATOR_DOT_CHAR );

	public final static String	SEPARATOR_COMMA					= ",";

	public final static String	RESOURCE_KEY_NOT_FOUND_PREFIX	= "???";

	public final static String	RESOURCE_KEY_NOT_FOUND_SUFFIX	= "???";

	public static String lowercaseFirstLetter( String in )
	{
		return Character.toLowerCase( in.charAt( 0 ) ) + in.substring( 1 );
	}

	public static String uppercaseFirstLetter( String in )
	{
		return Character.toUpperCase( in.charAt( 0 ) ) + in.substring( 1 );
	}

	public static boolean isFirstLetterUppercase( String in )
	{
		if ( in.length() == 0 )
			return false;

		return Character.isUpperCase( in.charAt( 0 ) );
	}

	public static String uncamelCase( final String camelCase )
	{
		// Nothing to do?

		if ( camelCase == null )
			return null;

		int length = camelCase.length();

		// (use StringBuffer for J2SE 1.4 compatibility)

		StringBuffer buffer = new StringBuffer( length );

		boolean first = true;
		char lastChar = ' ';
		char[] chars = camelCase.toCharArray();

		for ( int loop = 0; loop < length; loop++ )
		{
			char c = chars[loop];

			if ( first )
			{
				buffer.append( Character.toUpperCase( c ) );
				first = false;
			}
			else if ( Character.isUpperCase( c ) && ( !Character.isUpperCase( lastChar ) || ( loop < chars.length - 1 && chars[loop + 1] != ' ' && !Character.isUpperCase( chars[loop + 1] ) ) ) )
			{
				if ( Character.isLetter( lastChar ) )
					buffer.append( ' ' );

				if ( loop + 1 < length && !Character.isUpperCase( chars[loop + 1] ) )
					buffer.append( Character.toLowerCase( c ) );
				else
					buffer.append( c );
			}
			else if ( Character.isDigit( c ) && Character.isLetter( lastChar ) && lastChar != ' ' )
			{
				buffer.append( ' ' );
				buffer.append( c );
			}
			else
			{
				buffer.append( c );
			}

			lastChar = c;
		}

		return buffer.toString();
	}

	/**
	 * Converts the given String to camel case.
	 * <p>
	 * The first letter is lowercased, as per Java convention.
	 */

	public static String camelCase( String text )
	{
		return camelCase( text, ' ' );
	}

	/**
	 * Converts the given String to camel case.
	 * <p>
	 * The first letter is lowercased, as per Java convention.
	 */

	public static String camelCase( String text, char separator )
	{
		// (use StringBuffer for J2SE 1.4 compatibility)

		StringBuffer buffer = new StringBuffer( text.length() );

		// Convert separators to camel case

		boolean lastWasSeparator = false;
		char[] chars = text.toCharArray();

		for ( char c : chars )
		{
			if ( c == separator )
			{
				lastWasSeparator = true;
			}
			else if ( buffer.length() == 0 )
			{
				buffer.append( Character.toLowerCase( c ) );
			}
			else
			{
				if ( lastWasSeparator )
				{
					buffer.append( Character.toUpperCase( c ) );
					lastWasSeparator = false;
				}
				else
				{
					buffer.append( c );
				}
			}
		}

		return buffer.toString();
	}

	/**
	 * Version of <code>String.valueOf</code> that fails 'quietly' for <code>null</code> Strings
	 * and returns an empty String rather than a String saying <code>null</code>.
	 */

	public static String quietValueOf( Object object )
	{
		if ( object == null )
			return "";

		return object.toString();
	}

	//
	//
	// Private constructor
	//
	//

	private StringUtils()
	{
		// Can never be called
	}
}
