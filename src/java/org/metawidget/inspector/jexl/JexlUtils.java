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

package org.metawidget.inspector.jexl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilities for working with JEXL.
 *
 * @author Richard Kennard
 */

public final class JexlUtils
{
	//
	//
	// Public statics
	//
	//

	public static boolean isValueReference( String binding )
	{
		return PATTERN_BINDING.matcher( binding ).matches();
	}
	/**
	 * @return the original String, not wrapped in #{...}. If the original String was not wrapped,
	 *         returns the original String
	 */

	public static String unwrapValueReference( String binding )
	{
		Matcher matcher = PATTERN_BINDING.matcher( binding );

		if ( !matcher.matches() )
			return binding;

		return matcher.group( 2 );
	}

	/**
	 * @return the original String, wrapped in #{...}. If the original String was already wrapped,
	 *         returns the original String
	 */

	public static String wrapValueReference( String binding )
	{
		if ( isValueReference( binding ))
			return binding;

		return BINDING_START + unwrapValueReference( binding ) + BINDING_END;
	}

	//
	//
	// Private statics
	//
	//

	private final static Pattern	PATTERN_BINDING	= Pattern.compile( "(\\$\\{)([^}]*)(\\})" );

	private final static String		BINDING_START	= "${";

	private final static String		BINDING_END		= "}";

	//
	//
	// Private constructor
	//
	//

	private JexlUtils()
	{
		// Can never be called
	}
}
