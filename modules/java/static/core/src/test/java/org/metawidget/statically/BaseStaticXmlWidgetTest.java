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

package org.metawidget.statically;

import junit.framework.TestCase;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class BaseStaticXmlWidgetTest
	extends TestCase {

	//
	// Public methods
	//

	public static void testBaseStaticXmlWidget() {

		BaseStaticXmlWidget widget = new BaseStaticXmlWidget( null, "foo", "foo.com" ) {
			// Just a concrete version
		};

		assertEquals( "<foo/>", widget.toString() );

		widget.putAttribute( "bar", "" );
		widget.putAttribute( "baz", "BAZ" );
		widget.putAttribute( "abc", null );
		assertEquals( "<foo bar=\"\" baz=\"BAZ\"/>", widget.toString() );
	}
}
