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

package org.metawidget.statically.faces;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilities for working with Java Server Faces statically.
 *
 * @author Richard Kennard
 */

public final class StaticFacesUtils {

	//
	// Public statics
	//

	/**
	 * Return <code>true</code> if the specified value conforms to the syntax requirements of a
	 * value binding expression.
	 * <p>
	 * This method is a mirror of the one in <code>UIComponentTag.isValueReference</code>, but that
	 * one is deprecated so may be removed in the future.
	 *
	 * @param value
	 *            The value to evaluate
	 * @throws NullPointerException
	 *             if <code>value</code> is <code>null</code>
	 */

	public static boolean isExpression( String value ) {

		return matchExpression( value ).matches();
	}

	public static Matcher matchExpression( String value ) {

		return PATTERN_EXPRESSION.matcher( value );
	}

	/**
	 * @return the original String, not wrapped in #{...}. If the original String was not wrapped,
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
	 * @return the original String, wrapped in #{...}. If the original String was already wrapped,
	 *         returns the original String
	 */

	public static String wrapExpression( String value ) {

		if ( isExpression( value ) ) {
			return value;
		}

		return EXPRESSION_START + unwrapExpression( value ) + EXPRESSION_END;
	}

	/**
	 * Match #{...} and ${...}. This mirrors the approach in
	 * <code>UIComponentTag.isValueReference</code>, but that one is deprecated so may be removed in
	 * the future.
	 */

	private static final Pattern	PATTERN_EXPRESSION	= Pattern.compile( "(#|\\$)\\{([^\\}]+)\\}" );

	private static final String		EXPRESSION_START	= "#{";

	private static final String		EXPRESSION_END		= "}";

	//
	// Private constructor
	//

	private StaticFacesUtils() {

		// Can never be called
	}
}
