// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

package org.metawidget.util;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

/**
 * Utilities for working with Layouts.
 * <p>
 * Some of the logic behind Layout decisions can be a little involved, so we refactor it here.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
