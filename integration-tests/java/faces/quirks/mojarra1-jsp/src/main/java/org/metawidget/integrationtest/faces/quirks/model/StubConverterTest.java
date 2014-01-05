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

package org.metawidget.integrationtest.faces.quirks.model;

import org.metawidget.inspector.faces.UiFacesNumberConverter;

/**
 * Models an entity that tests StandardConverterProcessor applying through Stubs.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
