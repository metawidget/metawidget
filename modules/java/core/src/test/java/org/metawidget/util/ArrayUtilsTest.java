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

package org.metawidget.util;

import java.util.Arrays;

import junit.framework.TestCase;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class ArrayUtilsTest
	extends TestCase {

	//
	// Public methods
	//

	public void testArrayUtils()
		throws Exception {

		String[] compareTo = new String[] { "foo", "bar", "baz" };

		// fromString

		assertTrue( Arrays.equals( compareTo, ArrayUtils.fromString( "foo,bar,baz" ) ) );
		assertEquals( 0, ArrayUtils.fromString( null ).length );

		// toString

		assertEquals( "|foo|bar|baz|", ArrayUtils.toString( compareTo, "|", true, true ) );
		assertEquals( "1,2,3", ArrayUtils.toString( new byte[] { 1, 2, 3 } ) );
		assertEquals( "foo\\,bar,bar\\,foo", ArrayUtils.toString( new String[] { "foo,bar", "bar,foo" } ) );

		// add

		assertTrue( Arrays.equals( compareTo, ArrayUtils.add( new String[] { "foo", "bar" }, "baz" ) ) );
		assertTrue( Arrays.equals( compareTo, ArrayUtils.add( (String[]) null, compareTo ) ) );
		assertTrue( Arrays.equals( compareTo, ArrayUtils.add( new String[] { "foo" }, new String[] { "bar", "baz" } ) ) );
		assertTrue( Arrays.equals( compareTo, ArrayUtils.add( compareTo, (String[]) null ) ) );
		assertTrue( Arrays.equals( compareTo, ArrayUtils.add( compareTo, new String[0] ) ) );
		assertTrue( Arrays.equals( new String[] { "foo", "bar", "baz", null }, ArrayUtils.add( compareTo, (String) null ) ) );

		// addAt

		assertTrue( Arrays.equals( compareTo, ArrayUtils.addAt( new String[] { "foo", "bar" }, 2, "baz" ) ) );
		assertTrue( Arrays.equals( compareTo, ArrayUtils.addAt( (String[]) null, 2, compareTo ) ) );
		assertTrue( Arrays.equals( compareTo, ArrayUtils.addAt( new String[] { "bar", "baz" }, 0, new String[] { "foo" } ) ) );
		assertTrue( Arrays.equals( compareTo, ArrayUtils.addAt( compareTo, 0, (String[]) null ) ) );
		assertTrue( Arrays.equals( compareTo, ArrayUtils.addAt( compareTo, 0, new String[0] ) ) );
		assertTrue( Arrays.equals( compareTo, ArrayUtils.addAt( new String[] { "foo", "baz" }, 1, "bar" ) ) );
		assertTrue( Arrays.equals( compareTo, ArrayUtils.addAt( new String[] { "baz" }, 0, new String[] { "foo", "bar" } ) ) );

		// indexOf

		assertEquals( ArrayUtils.indexOf( null, "bar" ), -1 );
		assertEquals( ArrayUtils.indexOf( compareTo, "bar" ), 1 );
		assertEquals( ArrayUtils.indexOf( compareTo, "abc" ), -1 );
		assertEquals( ArrayUtils.indexOf( new String[] { "foo", null }, null ), 1 );

		// removeAt

		assertTrue( Arrays.equals( new String[] { "bar", "baz" }, ArrayUtils.removeAt( compareTo, 0 ) ) );
		assertTrue( Arrays.equals( new String[] { "foo", "baz" }, ArrayUtils.removeAt( compareTo, 1 ) ) );
		assertTrue( Arrays.equals( new String[] { "foo", "bar" }, ArrayUtils.removeAt( compareTo, 2 ) ) );
		assertTrue( Arrays.equals( new String[] { "baz" }, ArrayUtils.removeAt( ArrayUtils.removeAt( compareTo, 0 ), 0 ) ) );
		assertTrue( Arrays.equals( new String[] { "bar" }, ArrayUtils.removeAt( ArrayUtils.removeAt( compareTo, 0 ), 1 ) ) );
		assertTrue( Arrays.equals( new String[0], ArrayUtils.removeAt( ArrayUtils.removeAt( ArrayUtils.removeAt( compareTo, 0 ), 0 ), 0 ) ) );
	}
}
