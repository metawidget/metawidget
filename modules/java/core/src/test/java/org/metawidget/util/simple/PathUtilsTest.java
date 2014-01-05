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

package org.metawidget.util.simple;

import junit.framework.TestCase;

import org.metawidget.util.simple.PathUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class PathUtilsTest
	extends TestCase {

	//
	// Public methods
	//

	public void testPathUtils()
		throws Exception {

		assertEquals( "foo", PathUtils.parsePath( "foo" ).getType() );
		assertEquals( "foo", PathUtils.parsePath( " foo " ).getType() );
		assertEquals( "foo", PathUtils.parsePath( "foo/" ).getType() );
		assertEquals( 0, PathUtils.parsePath( "foo" ).getNamesAsArray().length );
		assertEquals( 0, PathUtils.parsePath( "foo/" ).getNamesAsArray().length );
		assertEquals( "bar", PathUtils.parsePath( "foo/bar" ).getNamesAsArray()[0] );
		assertEquals( "bar", PathUtils.parsePath( "foo/bar " ).getNamesAsArray()[0] );
		assertEquals( 1, PathUtils.parsePath( "foo/bar/" ).getNamesAsArray().length );
		assertEquals( "bar", PathUtils.parsePath( "foo/bar/" ).getNamesAsArray()[0] );
		assertEquals( "baz", PathUtils.parsePath( "foo/bar/baz" ).getNamesAsArray()[1] );

		// test regex escaping

		assertEquals( "type", PathUtils.parsePath( "communication.type", '.' ).getNamesAsArray()[0] );
	}
}
