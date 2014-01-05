// Metawidget
//
// For historical reasons, this file is licensed under the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html).
//
// Most other files in Metawidget are licensed under both the
// LGPL/EPL and a commercial license. See http://metawidget.org
// for details.

package org.metawidget.statically.jsp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilities for working with Java Server Pages statically.
 * 
 * @author Ryan Bradley
 */

public final class StaticJspUtils {

	//
	// Public static methods
	//

	/**
	 * Return <code>true</code> if the specified value conforms to the syntax requirements of a
	 * JSP EL expression.
	 * 
	 * @param value
	 *            The value to evaluate
	 * @throws NullPointerException
	 *             if <code>value</code> is <code>null</code>
	 */

	private static boolean isExpression( String value ) {

		return matchExpression( value ).matches();
	}

	private static Matcher matchExpression( String value ) {

		return PATTERN_EXPRESSION.matcher( value );
	}

	/**
	 * @return the original String, not wrapped in ${...}. If the original String was not wrapped,
	 *         returns the original String
	 */

	public static String unwrapExpression( String value ) {

		Matcher matcher = PATTERN_EXPRESSION.matcher( value );

		if ( !matcher.matches() ) {
			return value;
		}

		return matcher.group( 2 );
	}

	/**
	 * @return the original String, wrapped in ${...}. If the original String was already wrapped,
	 *         returns the original String.
	 */

	public static String wrapExpression( String value ) {

		if ( isExpression( value ) ) {
			return value;
		}

		return EXPRESSION_START + value + EXPRESSION_END;
	}

	//
	// Private statics
	//

	/**
	 * Match both #{..} and ${...}
	 */
	
	private static final Pattern	PATTERN_EXPRESSION	= Pattern.compile( "(#|\\$)\\{([^\\}]+)\\}" );

	private static final String		EXPRESSION_START	= "${";

	private static final String		EXPRESSION_END		= "}";

	//
	// Private constructor
	//

	private StaticJspUtils() {

		// Can never be called.
	}
}
