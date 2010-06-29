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

import java.util.Collection;
import java.util.Map;

/**
 * Utilities for working with Inspectors.
 * <p>
 * Some of the logic behind Inspectors can be a little involved, so we refactor it here.
 *
 * @author Richard Kennard
 */

public final class InspectorUtils {

	//
	// Public methods
	//

	public static void putAttributeValue( Map<String, String> attributes, String name, Object value ) {

		if ( value == null ) {
			attributes.put( name, "" );
			return;
		}

		if ( value instanceof Collection<?> ) {
			attributes.put( name, CollectionUtils.toString( (Collection<?>) value ) );
			return;
		}

		if ( value.getClass().isArray() ) {
			attributes.put( name, ArrayUtils.toString( value ) );
			return;
		}

		attributes.put( name, value.toString() );
	}

	//
	// Private constructor
	//

	private InspectorUtils() {

		// Can never be called
	}
}
