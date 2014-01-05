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

package org.metawidget.android;

import junit.framework.TestCase;

import org.metawidget.iface.MetawidgetException;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class AndroidConfigReaderTest
	extends TestCase {

	//
	// Public methods
	//

	public void testAndroidConfigReader()
		throws Exception {

		AndroidConfigReader androidConfigReader = new AndroidConfigReader( null );
		assertTrue( true == (Boolean) androidConfigReader.createNative( "boolean", null, "true" ) );
		assertTrue( 123 == (Integer) androidConfigReader.createNative( "int", null, "123" ) );

		try {
			androidConfigReader.getResourceResolver().openResource( "foo" );
		} catch ( MetawidgetException e ) {
			assertEquals( "Resource name does not start with '@': foo", e.getMessage() );
		}
	}
}
