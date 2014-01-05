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

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.util.CollectionUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>, Bernhard Huber
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
