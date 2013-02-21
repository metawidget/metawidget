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

package org.metawidget.util.simple;

import junit.framework.TestCase;

/**
 * @author Richard Kennard
 */

public class ObjectUtilsTest
	extends TestCase {

	//
	// Public methods
	//

	public void testNullSafeHashCode()
		throws Exception {

		assertEquals( 0, ObjectUtils.nullSafeHashCode( null ) );
		assertEquals( "foo".hashCode(), ObjectUtils.nullSafeHashCode( "foo" ) );
	}

	public void testNullSafeEquals()
		throws Exception {

		assertEquals( true, ObjectUtils.nullSafeEquals( null, null ) );
		assertEquals( false, ObjectUtils.nullSafeEquals( null, "foo" ) );
		assertEquals( false, ObjectUtils.nullSafeEquals( "foo", null ) );
		assertEquals( true, ObjectUtils.nullSafeEquals( "foo", "foo" ) );
	}

	public void testNullSafeCompareTo()
		throws Exception {

		assertEquals( 0, ObjectUtils.nullSafeCompareTo( null, null ) );
		assertEquals( -1, ObjectUtils.nullSafeCompareTo( null, "foo" ) );
		assertEquals( 1, ObjectUtils.nullSafeCompareTo( "foo", null ) );
		assertEquals( 0, ObjectUtils.nullSafeCompareTo( "foo", "foo" ) );
	}
}
