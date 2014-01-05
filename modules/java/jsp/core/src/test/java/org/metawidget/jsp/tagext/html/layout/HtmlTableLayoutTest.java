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

package org.metawidget.jsp.tagext.html.layout;

import junit.framework.TestCase;

import org.metawidget.layout.iface.LayoutException;
import org.metawidget.util.MetawidgetTestUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class HtmlTableLayoutTest
	extends TestCase {

	//
	// Public methods
	//

	public void testConfig() {

		MetawidgetTestUtils.testEqualsAndHashcode( HtmlTableLayoutConfig.class, new HtmlTableLayoutConfig() {
			// Subclass
		} );
	}

	public void testMinimumColumns() {

		HtmlTableLayoutConfig config = new HtmlTableLayoutConfig();
		assertEquals( 1, config.getNumberOfColumns() );

		config.setNumberOfColumns( 0 );
		assertEquals( 0, config.getNumberOfColumns() );

		try {
			config.setNumberOfColumns( -1 );
			fail();
		} catch ( LayoutException e ) {
			assertEquals( "numberOfColumns must be >= 0", e.getMessage() );
		}
	}
}
