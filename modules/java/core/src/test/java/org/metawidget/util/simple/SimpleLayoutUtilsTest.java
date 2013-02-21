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

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.util.CollectionUtils;

/**
 * @author Richard Kennard, Bernhard Huber
 */

public class SimpleLayoutUtilsTest
	extends TestCase {

	//
	// Public methods
	//

	public void testLayoutException()
		throws Exception {

		assertTrue( !SimpleLayoutUtils.isSpanAllColumns( null ) );

		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( LARGE, TRUE );
		assertTrue( SimpleLayoutUtils.isSpanAllColumns( attributes ) );

		attributes.clear();
		attributes.put( WIDE, TRUE );
		assertTrue( SimpleLayoutUtils.isSpanAllColumns( attributes ) );

		attributes.clear();
		assertTrue( !SimpleLayoutUtils.isSpanAllColumns( attributes ) );
	}

	public void testStripMnemonic()
		throws Exception {

		assertEquals( "Company", SimpleLayoutUtils.stripMnemonic( "Company" ).getStrippedMnemonic() );
		assertEquals( -1, SimpleLayoutUtils.stripMnemonic( "Company" ).getFirstIndex() );

		assertEquals( "Company", SimpleLayoutUtils.stripMnemonic( "&Company" ).getStrippedMnemonic() );
		assertEquals( 0, SimpleLayoutUtils.stripMnemonic( "&Company" ).getFirstIndex() );

		assertEquals( "Company", SimpleLayoutUtils.stripMnemonic( "Compan&y" ).getStrippedMnemonic() );
		assertEquals( 6, SimpleLayoutUtils.stripMnemonic( "Compan&y" ).getFirstIndex() );

		assertEquals( "C&ompany", SimpleLayoutUtils.stripMnemonic( "C&&ompa&ny" ).getStrippedMnemonic() );
		assertEquals( 6, SimpleLayoutUtils.stripMnemonic( "C&&ompa&ny" ).getFirstIndex() );

		assertEquals( "Company", SimpleLayoutUtils.stripMnemonic( "C&ompa&ny" ).getStrippedMnemonic() );
		assertEquals( 1, SimpleLayoutUtils.stripMnemonic( "C&ompa&ny" ).getFirstIndex() );

		assertEquals( "Company&", SimpleLayoutUtils.stripMnemonic( "Company&" ).getStrippedMnemonic() );
		assertEquals( -1, SimpleLayoutUtils.stripMnemonic( "Company&" ).getFirstIndex() );

		assertEquals( "Company&", SimpleLayoutUtils.stripMnemonic( "Company&&" ).getStrippedMnemonic() );
		assertEquals( -1, SimpleLayoutUtils.stripMnemonic( "Company&&" ).getFirstIndex() );

		assertEquals( "&Company", SimpleLayoutUtils.stripMnemonic( "&&Company" ).getStrippedMnemonic() );
		assertEquals( -1, SimpleLayoutUtils.stripMnemonic( "&&Company" ).getFirstIndex() );

		assertEquals( "&Company", SimpleLayoutUtils.stripMnemonic( "&&&Company" ).getStrippedMnemonic() );
		assertEquals( 1, SimpleLayoutUtils.stripMnemonic( "&&&Company" ).getFirstIndex() );

		assertEquals( "& Company", SimpleLayoutUtils.stripMnemonic( "& Company" ).getStrippedMnemonic() );
		assertEquals( -1, SimpleLayoutUtils.stripMnemonic( "& Company" ).getFirstIndex() );

		assertEquals( "Com& pany", SimpleLayoutUtils.stripMnemonic( "Com& pany" ).getStrippedMnemonic() );
		assertEquals( -1, SimpleLayoutUtils.stripMnemonic( "Com& pany" ).getFirstIndex() );

		assertEquals( "Company& ", SimpleLayoutUtils.stripMnemonic( "Company& " ).getStrippedMnemonic() );
		assertEquals( -1, SimpleLayoutUtils.stripMnemonic( "Company& " ).getFirstIndex() );

		assertEquals( "Com& pany", SimpleLayoutUtils.stripMnemonic( "Com& &pany" ).getStrippedMnemonic() );
		assertEquals( 5, SimpleLayoutUtils.stripMnemonic( "Com& &pany" ).getFirstIndex() );
	}
}
