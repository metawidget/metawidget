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

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
