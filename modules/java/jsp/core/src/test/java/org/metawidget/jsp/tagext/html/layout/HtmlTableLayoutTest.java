// Metawidget
//
// This library is dual licensed under both LGPL and a commercial
// license.
//
// LGPL: this library is free software; you can redistribute it and/or
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
//
// Commercial License: See http://metawidget.org for details

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
