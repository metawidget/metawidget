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

package org.metawidget.util.simple;

import junit.framework.TestCase;

import org.metawidget.util.simple.PathUtils;

/**
 * @author Richard Kennard
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
		assertTrue( 0 == PathUtils.parsePath( "foo" ).getNamesAsArray().length );
		assertTrue( 0 == PathUtils.parsePath( "foo/" ).getNamesAsArray().length );
		assertEquals( "bar", PathUtils.parsePath( "foo/bar" ).getNamesAsArray()[0] );
		assertEquals( "bar", PathUtils.parsePath( "foo/bar " ).getNamesAsArray()[0] );
		assertTrue( 1 == PathUtils.parsePath( "foo/bar/" ).getNamesAsArray().length );
		assertEquals( "bar", PathUtils.parsePath( "foo/bar/" ).getNamesAsArray()[0] );
		assertEquals( "baz", PathUtils.parsePath( "foo/bar/baz" ).getNamesAsArray()[1] );

		// test regex escaping

		assertEquals( "type", PathUtils.parsePath( "communication.type", '.' ).getNamesAsArray()[0] );
	}
}
