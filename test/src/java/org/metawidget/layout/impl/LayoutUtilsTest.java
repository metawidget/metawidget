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

package org.metawidget.layout.impl;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.util.CollectionUtils;
import org.metawidget.util.LayoutUtils;
import org.metawidget.util.simple.SimpleLayoutUtils;

/**
 * @author Richard Kennard, Bernhard Huber
 */

public class LayoutUtilsTest
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

		assertEquals( "Company", LayoutUtils.stripMnemonic( "Company" ).getLeft() );
		assertTrue( -1 == LayoutUtils.stripMnemonic( "Company" ).getRight() );

		assertEquals( "Company", LayoutUtils.stripMnemonic( "&Company" ).getLeft() );
		assertTrue( 0 == LayoutUtils.stripMnemonic( "&Company" ).getRight() );

		assertEquals( "Company", LayoutUtils.stripMnemonic( "Compan&y" ).getLeft() );
		assertTrue( 6 == LayoutUtils.stripMnemonic( "Compan&y" ).getRight() );

		assertEquals( "C&ompany", LayoutUtils.stripMnemonic( "C&&ompa&ny" ).getLeft() );
		assertTrue( 6 == LayoutUtils.stripMnemonic( "C&&ompa&ny" ).getRight() );

		assertEquals( "Company", LayoutUtils.stripMnemonic( "C&ompa&ny" ).getLeft() );
		assertTrue( 1 == LayoutUtils.stripMnemonic( "C&ompa&ny" ).getRight() );

		assertEquals( "Company&", LayoutUtils.stripMnemonic( "Company&" ).getLeft() );
		assertTrue( -1 == LayoutUtils.stripMnemonic( "Company&" ).getRight() );

		assertEquals( "Company&", LayoutUtils.stripMnemonic( "Company&&" ).getLeft() );
		assertTrue( -1 == LayoutUtils.stripMnemonic( "Company&&" ).getRight() );

		assertEquals( "&Company", LayoutUtils.stripMnemonic( "&&Company" ).getLeft() );
		assertTrue( -1 == LayoutUtils.stripMnemonic( "&&Company" ).getRight() );

		assertEquals( "&Company", LayoutUtils.stripMnemonic( "&&&Company" ).getLeft() );
		assertTrue( 1 == LayoutUtils.stripMnemonic( "&&&Company" ).getRight() );

		assertEquals( "& Company", LayoutUtils.stripMnemonic( "& Company" ).getLeft() );
		assertTrue( -1 == LayoutUtils.stripMnemonic( "& Company" ).getRight() );

		assertEquals( "Com& pany", LayoutUtils.stripMnemonic( "Com& pany" ).getLeft() );
		assertTrue( -1 == LayoutUtils.stripMnemonic( "Com& pany" ).getRight() );

		assertEquals( "Company& ", LayoutUtils.stripMnemonic( "Company& " ).getLeft() );
		assertTrue( -1 == LayoutUtils.stripMnemonic( "Company& " ).getRight() );

		assertEquals( "Com& pany", LayoutUtils.stripMnemonic( "Com& &pany" ).getLeft() );
		assertTrue( 5 == LayoutUtils.stripMnemonic( "Com& &pany" ).getRight() );
	}
}
