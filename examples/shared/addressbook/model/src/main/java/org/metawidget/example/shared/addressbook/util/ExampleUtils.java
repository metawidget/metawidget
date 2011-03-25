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

package org.metawidget.example.shared.addressbook.util;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.metawidget.util.CollectionUtils;

/**
 * Utilities for the example apps.
 *
 * @author Richard Kennard
 */

public final class ExampleUtils {

	//
	// Public statics
	//

	public static <T extends Comparable<T>> List<T> sortSet( Set<T> set ) {

		if ( set == null ) {
			return null;
		}

		List<T> sorted = CollectionUtils.newArrayList( set );
		Collections.sort( sorted );

		return sorted;
	}

	//
	// Private constructor
	//

	private ExampleUtils() {

		// Can never be called
	}
}
