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

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

/**
 * Utilities for working with Layouts.
 * <p>
 * Some of the logic behind Layout decisions can be a little involved, so we refactor it here.
 *
 * @author Richard Kennard, Bernhard Huber
 */

public final class LayoutUtils {

	//
	// Public methods
	//

	/**
	 * Strips the first section off the section attribute (if any).
	 *
	 * @param attributes
	 *            the attributes. If there is a section attribute, it will be modified to remove its
	 *            leftmost section (ie. 'foo' in 'foo,bar,baz')
	 * @return the stripped section
	 */

	public static String stripSection( Map<String, String> attributes ) {

		String sections = attributes.remove( SECTION );

		// (null means 'no change to current section')

		if ( sections == null ) {
			return null;
		}

		String[] sectionsAsArray = ArrayUtils.fromString( sections );

		switch ( sectionsAsArray.length ) {
			// (empty String means 'end current section')

			case 0:
				return "";

			case 1:
				return sectionsAsArray[0];

			default:
				String section = sectionsAsArray[0];
				attributes.put( SECTION, ArrayUtils.toString( ArrayUtils.removeAt( sectionsAsArray, 0 ) ) );
				return section;
		}
	}

	//
	// Private constructor
	//

	private LayoutUtils() {

		// Can never be called
	}
}
