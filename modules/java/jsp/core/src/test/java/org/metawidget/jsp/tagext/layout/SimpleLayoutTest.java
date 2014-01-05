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

package org.metawidget.jsp.tagext.layout;

import junit.framework.TestCase;

import org.metawidget.layout.iface.LayoutException;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class SimpleLayoutTest
	extends TestCase {

	//
	// Public methods
	//

	public void testException() {

		try {
			new SimpleLayout().layoutWidget( null, null, null, null, null );
			fail();
		} catch ( LayoutException e ) {
			assertTrue( e.getCause() instanceof NullPointerException );
		}
	}
}
