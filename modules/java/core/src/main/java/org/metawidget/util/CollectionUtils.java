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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilderException;

/**
 * Utilities for working with Java Collections.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public final class CollectionUtils {

	//
	// Public statics
	//

	/**
	 * Type-safe initializer.
	 */

	public static <T> ArrayList<T> newArrayList() {

		return new ArrayList<T>();
	}

	/**
	 * Type-safe initializer.
	 */

	public static <T> ArrayList<T> newArrayList( Collection<T> collection ) {

		return new ArrayList<T>( collection );
	}

	/**
	 * Type-safe initializer.
	 */

	public static <T> ArrayList<T> newArrayList( int capacity ) {

		return new ArrayList<T>( capacity );
	}

	/**
	 * Type-safe initializer.
	 */

	public static <T> ArrayList<T> newArrayList( T... array ) {

		if ( array == null ) {
			return new ArrayList<T>();
		}

		return new ArrayList<T>( Arrays.asList( array ) );
	}

	/**
	 * Type-safe initializer.
	 */

	public static <K> HashSet<K> newHashSet() {

		return new HashSet<K>();
	}

	/**
	 * Type-safe initializer.
	 */

	public static <K> HashSet<K> newHashSet( Collection<K> set ) {

		return new HashSet<K>( set );
	}

	/**
	 * Type-safe initializer.
	 */

	public static <K> HashSet<K> newHashSet( K... array ) {

		return new HashSet<K>( Arrays.asList( array ) );
	}

	/**
	 * Type-safe initializer.
	 */

	public static <K> Stack<K> newStack() {

		return new Stack<K>();
	}

	/**
	 * Type-safe initializer.
	 */

	public static <K, V> HashMap<K, V> newHashMap() {

		return new HashMap<K, V>();
	}

	/**
	 * Type-safe initializer.
	 */

	public static <K, V> HashMap<K, V> newHashMap( Map<K, V> map ) {

		return new HashMap<K, V>( map );
	}

	/**
	 * Type-safe initializer.
	 */

	public static <K, V> HashMap<K, V> newHashMap( int size ) {

		return new HashMap<K, V>( size );
	}

	/**
	 * Returns a Map initialized using the first given List as keys, and the second given List as
	 * values.
	 */

	public static <K, V> Map<K, V> newHashMap( List<K> keys, List<V> values ) {

		if ( keys.size() != values.size() ) {
			throw WidgetBuilderException.newException( "Keys list must be same size as values list" );
		}

		Map<K, V> map = CollectionUtils.newHashMap();

		for ( int loop = 0, length = keys.size(); loop < length; loop++ ) {
			map.put( keys.get( loop ), values.get( loop ) );
		}

		return map;
	}

	/**
	 * Type-safe initializer.
	 * <p>
	 * WeakHashMap is preferrable if <code>K</code> is of type <code>Class</code>.
	 */

	public static <K, V> WeakHashMap<K, V> newWeakHashMap() {

		return new WeakHashMap<K, V>();
	}

	/**
	 * Type-safe initializer.
	 */

	public static <K, V> LinkedHashMap<K, V> newLinkedHashMap() {

		return new LinkedHashMap<K, V>();
	}

	/**
	 * Type-safe initializer.
	 */

	public static <K, V> LinkedHashMap<K, V> newLinkedHashMap( Map<K, V> map ) {

		return new LinkedHashMap<K, V>( map );
	}

	/**
	 * Type-safe initializer.
	 */

	public static <K, V> TreeMap<K, V> newTreeMap() {

		return new TreeMap<K, V>();
	}

	public static <K, V> TreeMap<K, V> newTreeMap( Comparator<K> comparator ) {

		return new TreeMap<K, V>( comparator );
	}

	public static <T> List<T> unmodifiableList( T... array ) {

		return Collections.unmodifiableList( Arrays.asList( array ) );
	}

	public static <T> String toString( Collection<T> collection ) {

		return toString( collection, StringUtils.SEPARATOR_COMMA );
	}

	public static <T> String toString( Collection<T> collection, String separator ) {

		return toString( collection, separator, false, false );
	}

	public static <T> String toString( Collection<T> collection, String separator, boolean leadingSeparator, boolean trailingSeparator ) {

		// Nothing to do?

		if ( collection == null ) {
			return "";
		}

		// If Collection is a Set, sort it for consistency in unit tests. Never
		// sort the original Collection, as users wouldn't expect toString() to do that!

		Collection<T> consistentlyOrderedCollection = collection;

		if ( consistentlyOrderedCollection instanceof Set<?> ) {
			consistentlyOrderedCollection = newArrayList( collection );
			Collections.sort( (List<T>) consistentlyOrderedCollection, null );
		}

		// Output as a String

		Pattern patternSeparator = Pattern.compile( separator, Pattern.LITERAL );
		String replacement = "\\\\" + separator;

		StringBuilder builder = new StringBuilder();

		for ( T t : consistentlyOrderedCollection ) {
			String value = String.valueOf( t );

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

	/**
	 * Split the given String by comma. Commas within Strings may be escaped using the backslash (\)
	 * character. Starting and ending whitespace around each String will be trimmed.
	 */

	public static List<String> fromString( final String collection ) {

		return fromString( collection, ',' );
	}

	/**
	 * Split the given String by the given separator. Separators within Strings may be escaped using
	 * the backslash (\) character. Starting and ending whitespace around each sub-String will be
	 * trimmed.
	 */

	public static List<String> fromString( final String collection, char separator ) {

		if ( collection == null || collection.length() == 0 ) {
			return Collections.emptyList();
		}

		List<String> split = CollectionUtils.newArrayList();
		Pattern patternSplit = Pattern.compile( "((\\\\" + separator + "|[^" + separator + "])*)(" + separator + "|$)" );
		Pattern patternSeparator = Pattern.compile( "\\" + separator, Pattern.LITERAL );
		String replacement = String.valueOf( separator );

		Matcher matcher = patternSplit.matcher( collection );

		while ( matcher.find() ) {
			String match = matcher.group( 1 ).trim();
			match = patternSeparator.matcher( match ).replaceAll( replacement );

			split.add( match );

			if ( matcher.end( 1 ) == collection.length() ) {
				break;
			}
		}

		return split;
	}

	//
	// Private constructor
	//

	private CollectionUtils() {

		// Can never be called
	}
}
