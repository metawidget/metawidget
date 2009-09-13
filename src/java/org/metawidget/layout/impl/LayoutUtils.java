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

package org.metawidget.layout.impl;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

/**
 * Utilities for working with Layouts.
 * <p>
 * Some of the logic behind Layouts decisions can be a little involved, so we refactor it
 * here.
 *
 * @author Richard Kennard
 */

public final class LayoutUtils
{
	//
	// Public methods
	//

	/**
	 * Returns true if the field is 'large' or 'wide'.
	 */

	public static boolean isSpanAllColumns( Map<String, String> attributes )
	{
		if ( attributes == null )
			return false;

		if ( TRUE.equals( attributes.get( LARGE ) ) )
			return true;

		if ( TRUE.equals( attributes.get( WIDE ) ) )
			return true;

		return false;
	}

	//
	// Private constructor
	//

	private LayoutUtils()
	{
		// Can never be called
	}
}
