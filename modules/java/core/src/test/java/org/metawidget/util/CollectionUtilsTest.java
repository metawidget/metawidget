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

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class CollectionUtilsTest
	extends TestCase {

	//
	// Public methods
	//

	public void testCollectionUtils()
		throws Exception {

		assertTrue( CollectionUtils.newArrayList( 2 ) != null );

		List<String> list = CollectionUtils.newArrayList( "foo", "bar" );
		assertTrue( list instanceof ArrayList<?> );
		assertEquals( "foo", list.get( 0 ) );
		assertEquals( "bar", list.get( 1 ) );
		assertEquals( "|foo|bar|", CollectionUtils.toString( list, "|", true, true ) );

		assertTrue( CollectionUtils.newLinkedHashMap() != null );

		assertEquals( CollectionUtils.newArrayList( "foo", "bar" ), CollectionUtils.fromString( "foo, bar" ) );
		assertEquals( CollectionUtils.newArrayList( "foo", "bar", "" ), CollectionUtils.fromString( "foo, bar," ) );
		assertEquals( CollectionUtils.newArrayList( "foo", "bar", "baz" ), CollectionUtils.fromString( "foo, bar,  baz" ) );

		assertTrue( CollectionUtils.fromString( null ).isEmpty() );
		assertTrue( CollectionUtils.fromString( "" ).isEmpty() );
		assertEquals( CollectionUtils.fromString( " " ).size(), 1 );
		assertEquals( CollectionUtils.fromString( "foo" ).size(), 1 );
		assertEquals( CollectionUtils.fromString( "," ).size(), 2 );
		assertEquals( CollectionUtils.fromString( "foo," ).size(), 2 );
		assertEquals( CollectionUtils.fromString( ",,foo" ).size(), 3 );
		assertEquals( CollectionUtils.fromString( ",,foo," ).size(), 4 );
	}
}
