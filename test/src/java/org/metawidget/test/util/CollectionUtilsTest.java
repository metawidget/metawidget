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

package org.metawidget.test.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.metawidget.util.CollectionUtils;

/**
 * @author Richard Kennard
 */

public class CollectionUtilsTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testCollectionUtils()
		throws Exception
	{
		assertTrue( CollectionUtils.newArrayList( 2 ) != null );

		List<String> list = CollectionUtils.newArrayList( "foo", "bar" );
		assertTrue( list instanceof ArrayList );
		assertTrue( "foo".equals( list.get( 0 )));
		assertTrue( "bar".equals( list.get( 1 )));
		assertTrue( "|foo|bar|".equals( CollectionUtils.toString( list, "|", true, true )));

		assertTrue( CollectionUtils.newLinkedHashMap() != null );

		@SuppressWarnings("unchecked")
		Set<Comparable> emptySet = Collections.emptySet();
		assertTrue( CollectionUtils.sort( emptySet ).isEmpty() );

		assertTrue( CollectionUtils.newArrayList( "foo", "bar" ).equals( CollectionUtils.fromString( "foo, bar" ) ));
		assertTrue( CollectionUtils.newArrayList( "foo", "bar", "" ).equals( CollectionUtils.fromString( "foo, bar," ) ));
		assertTrue( CollectionUtils.newArrayList( "foo", "bar", "baz" ).equals( CollectionUtils.fromString( "foo, bar,  baz" ) ));

		assertTrue( CollectionUtils.fromString( null ).isEmpty() );
		assertTrue( CollectionUtils.fromString( "" ).isEmpty() );
		assertTrue( CollectionUtils.fromString( " " ).size() == 1 );
		assertTrue( CollectionUtils.fromString( "foo" ).size() == 1 );
		assertTrue( CollectionUtils.fromString( "," ).size() == 2 );
		assertTrue( CollectionUtils.fromString( "foo," ).size() == 2 );
		assertTrue( CollectionUtils.fromString( ",,foo" ).size() == 3 );
		assertTrue( CollectionUtils.fromString( ",,foo," ).size() == 4 );
	}
}
