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

package org.metawidget.util.simple;

import java.util.Comparator;

/**
 * Utilities for working with Strings.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public final class StringUtils {

	//
	// Public statics
	//

	/**
	 * Forward slash character.
	 * <p>
	 * For environments that use the fully qualified class name (eg. SwingMetawidget) as part of the
	 * path, we must use '/' not '.' as the separator.
	 */

	public static final char	SEPARATOR_FORWARD_SLASH_CHAR	= '/';

	public static final String	SEPARATOR_FORWARD_SLASH			= String.valueOf( SEPARATOR_FORWARD_SLASH_CHAR );

	public static final char	SEPARATOR_DOT_CHAR				= '.';

	public static final String	SEPARATOR_DOT					= String.valueOf( SEPARATOR_DOT_CHAR );

	public static final char	SEPARATOR_COMMA_CHAR			= ',';

	public static final String	SEPARATOR_COMMA					= String.valueOf( SEPARATOR_COMMA_CHAR );

	public static final char	SEPARATOR_COLON_CHAR			= ':';

	public static final String	SEPARATOR_COLON					= String.valueOf( SEPARATOR_COLON_CHAR );

	public static final String	RESOURCE_KEY_NOT_FOUND_PREFIX	= "???";

	public static final String	RESOURCE_KEY_NOT_FOUND_SUFFIX	= "???";

	/**
	 * Decapitalize without using <code>java.beans</code>, as that package is not available on all
	 * target platforms.
	 * <p>
	 * Following the rules defined in <tt>java.beans.Introspector</tt>: "This normally means
	 * converting the first character from upper case to lower case, but in the (unusual) special
	 * case when there is more than one character and both the first and second characters are upper
	 * case, we leave it alone. Thus 'FooBah' becomes 'fooBah' and 'X' becomes 'x', but 'URL' stays
	 * as 'URL'"
	 */

	public static String decapitalize( String in ) {

		if ( in.length() == 0 ) {
			return in;
		}

		// Nothing to do?

		char firstChar = in.charAt( 0 );

		if ( Character.isLowerCase( firstChar ) ) {
			return in;
		}

		// Second letter uppercase?

		if ( in.length() > 1 ) {
			if ( Character.isUpperCase( in.charAt( 1 ) ) ) {
				return in;
			}
		}

		return Character.toLowerCase( firstChar ) + in.substring( 1 );
	}

	/**
	 * Capitalize by uppercasing the first letter of the given String (e.g. from
	 * 'fooBarBaz' to 'FooBarBaz').
	 * <p>
	 * The rules for capitalizing are not clearly defined in <tt>java.beans.Introspector</tt>, but
	 * we try to make <code>capitalize</code> the inverse of <code>decapitalize</code> (this
	 * includes the 'second character' clause). For example, in Eclipse if you define a property
	 * 'aB123' and then 'generate getters' Eclipse will generate a method called 'getaB123'
	 * <em>not</em> 'getAB123'. See: https://community.jboss.org/thread/203202?start=0&tstart=0
	 */

	public static String capitalize( String in ) {

		if ( in.length() == 0 ) {
			return in;
		}

		// Second letter uppercase?

		if ( in.length() > 1 ) {
			if ( Character.isUpperCase( in.charAt( 1 ) ) ) {
				return in;
			}
		}

		return Character.toUpperCase( in.charAt( 0 ) ) + in.substring( 1 );
	}

	/**
	 * Converts the given string from camel case.
	 * <p>
	 * For example, converts <code>fooBar1</code> into <code>Foo Bar 1</code>. Used primarily to
	 * convert property names and paths into human-readable UI labels.
	 */

	public static String uncamelCase( String camelCase ) {

		return uncamelCase( camelCase, ' ' );
	}

	/**
	 * Converts the given string from camel case.
	 * <p>
	 * For example, converts <code>fooBar1</code> into <code>Foo Bar 1</code>. Used primarily to
	 * convert property names and paths into human-readable UI labels.
	 */

	public static String uncamelCase( String camelCase, char separator ) {

		// Nothing to do?

		if ( camelCase == null ) {
			return null;
		}

		int length = camelCase.length();
		StringBuilder builder = new StringBuilder( length );

		boolean first = true;
		char lastChar = separator;
		char[] chars = camelCase.toCharArray();

		for ( int loop = 0; loop < length; loop++ ) {
			char c = chars[loop];

			if ( first ) {
				builder.append( Character.toUpperCase( c ) );
				first = false;
			} else if ( Character.isUpperCase( c ) && ( !Character.isUpperCase( lastChar ) || ( loop < chars.length - 1 && chars[loop + 1] != separator && !Character.isUpperCase( chars[loop + 1] ) ) ) ) {
				if ( lastChar != separator ) {
					builder.append( separator );
				}

				// Don't do: if ( loop + 1 < length && !Character.isUpperCase( chars[loop + 1] ) )
				// buffer.append( Character.toLowerCase( c ) );
				//
				// It's ambiguous if we should lowercase the letter following a space, but in
				// general it looks nicer most of the time not to. The exception is 'joining' words
				// such as 'of' in 'Date of Birth'

				builder.append( c );
			} else if ( Character.isDigit( c ) && Character.isLetter( lastChar ) && lastChar != separator ) {
				builder.append( separator );
				builder.append( c );
			} else {
				builder.append( c );
			}

			lastChar = c;
		}

		return builder.toString();
	}

	/**
	 * Converts the given String to camel case.
	 * <p>
	 * The first letter following a <tt>separator</tt> is capitalized, as per Java convention. Non
	 * alpha numeric characters are also stripped. However no attempt is made to <em>de</em>
	 * capitalize the first name, because that gets very ambiguous with names like 'URL', 'ID' etc.
	 * <p>
	 * Used primarily to convert property paths into ids.
	 */

	public static String camelCase( String text ) {

		return camelCase( text, ' ' );
	}

	/**
	 * Converts the given String to camel case.
	 * <p>
	 * The first letter following a <tt>separator</tt> is capitalized, as per Java convention. Non
	 * alpha numeric characters are also stripped. The start of the String is decapitalized.
	 * <p>
	 * Used primarily to convert property paths into ids.
	 */

	public static String camelCase( String text, char separator ) {

		StringBuilder builder = new StringBuilder( text.length() );

		// Convert separators to camel case

		boolean lastWasSeparator = false;
		char[] chars = StringUtils.decapitalize( text ).toCharArray();

		for ( int loop = 0, length = chars.length; loop < length; loop++ ) {

			char c = chars[loop];

			if ( c == separator ) {
				lastWasSeparator = true;
				continue;
			}

			if ( !Character.isLetter( c ) && !Character.isDigit( c ) ) {
				continue;
			}

			if ( lastWasSeparator ) {
				builder.append( Character.toUpperCase( c ) );
				lastWasSeparator = false;
				continue;
			}

			builder.append( c );
		}

		return builder.toString();
	}

	/**
	 * Version of <code>String.valueOf</code> that fails 'quietly' for <code>null</code> Strings and
	 * returns an empty String rather than a String saying <code>null</code>.
	 */

	public static String quietValueOf( Object object ) {

		if ( object == null ) {
			return "";
		}

		return object.toString();
	}

	/**
	 * Returns the portion of the overall string that comes before the given string. If the given
	 * string is not found in the overall string, returns the entire string.
	 */

	public static String substringBefore( String text, String before ) {

		int indexOf = text.indexOf( before );

		if ( indexOf == -1 ) {
			return text;
		}

		return text.substring( 0, indexOf );
	}

	/**
	 * Returns the portion of the overall string that comes after the first occurance of the given
	 * string. If the given string is not found in the overall string, returns the entire string.
	 */

	public static String substringAfter( String text, String after ) {

		int indexOf = text.indexOf( after );

		if ( indexOf == -1 ) {
			return text;
		}

		return text.substring( indexOf + after.length() );
	}

	/**
	 * Returns the portion of the overall string that comes after the last occurance of the given
	 * string. If the given string is not found in the overall string, returns the entire string.
	 */

	public static String substringAfterLast( String text, char after ) {

		int indexOf = text.lastIndexOf( after );

		if ( indexOf == -1 ) {
			return text;
		}

		return text.substring( indexOf + 1 );
	}

	/**
	 * Returns the portion of the overall string that comes after the last occurance of the given
	 * string. If the given string is not found in the overall string, returns the entire string.
	 */

	public static String substringAfterLast( String text, String after ) {

		int indexOf = text.lastIndexOf( after );

		if ( indexOf == -1 ) {
			return text;
		}

		return text.substring( indexOf + after.length() );
	}

	/**
	 * Comparator that orders <code>String</code> objects as by <code>compareToIgnoreCase</code>.
	 * <p>
	 * Like <tt>String.CASE_INSENSITIVE_COMPARATOR</tt> but sorts case sensitively <em>within</em>
	 * case insensitive groups, thereby ensuring comparing 'Foo' to 'foo' does not equal 0. This is
	 * important if the Comparator is used within, say, a TreeMap. Otherwise the TreeMap will
	 * 'collapse' case insensitive keys.
	 */

	public static final Comparator<String>	CASE_INSENSITIVE_COMPARATOR	= new Comparator<String>() {

																			public int compare( String lhs, String rhs ) {

																				int compareTo = lhs.compareToIgnoreCase( rhs );

																				if ( compareTo == 0 ) {
																					compareTo = lhs.compareTo( rhs );
																				}

																				return compareTo;
																			}
																		};

	//
	// Private constructor
	//

	private StringUtils() {

		// Can never be called
	}
}
