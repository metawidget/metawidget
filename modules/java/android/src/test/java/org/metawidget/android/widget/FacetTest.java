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

package org.metawidget.android.widget;

import junit.framework.TestCase;

import org.metawidget.android.AndroidMetawidgetTests.MockAttributeSet;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class FacetTest
	extends TestCase {

	//
	// Public methods
	//

	public void testFacet() {

		Facet facet = new Facet( null );
		facet.setName( "foo" );
		assertEquals( "foo", facet.getName() );

		MockAttributeSet attributeSet = new MockAttributeSet();
		attributeSet.setAttributeValue( "name", "bar" );
		facet = new Facet( null, attributeSet );
		assertEquals( "bar", facet.getName() );
	}
}
