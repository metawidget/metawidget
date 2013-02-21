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

package org.metawidget.integrationtest.faces.quirks.model;

import org.metawidget.inspector.faces.UiFacesNumberConverter;

/**
 * Models an entity that tests StandardConverterProcessor applying through Stubs.
 *
 * @author Richard Kennard
 */

public class StubConverterTest {

	//
	// Public methods
	//

	@UiFacesNumberConverter( pattern = "0.00" )
	public int getFoo() {

		return 1;
	}

	public int getBar() {

		return 2;
	}
}
