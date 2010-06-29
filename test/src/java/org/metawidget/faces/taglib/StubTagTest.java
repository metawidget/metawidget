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

package org.metawidget.faces.taglib;

import javax.faces.context.FacesContext;

import junit.framework.TestCase;

import org.metawidget.faces.FacesMetawidgetTests.MockFacesContext;
import org.metawidget.iface.MetawidgetException;
import org.metawidget.jsp.JspMetawidgetTests.MockPageContext;

/**
 * StubTag test cases.
 * <p>
 * These are just some fringe-case tests. Most of the testing is done by WebTest.
 *
 * @author Richard Kennard
 */

public class StubTagTest
	extends TestCase {

	//
	// Private members
	//

	private FacesContext	mContext;

	//
	// Public methods
	//

	public void testStubTag()
		throws Exception {

		StubTag stubTag = new StubTag();
		stubTag.setPageContext( new MockPageContext() );

		// Value

		stubTag.setValue( "foo" );

		try {
			stubTag.setProperties( null );
			assertTrue( false );
		} catch( MetawidgetException e ) {
			assertEquals( "Value 'foo' must be an EL expression", e.getMessage() );
		}

		// Action

		stubTag.setAction( "bar" );

		try {
			stubTag.setProperties( null );
			assertTrue( false );
		} catch( MetawidgetException e ) {
			assertEquals( "Action 'bar' must be an EL expression", e.getMessage() );
		}
	}

	//
	// Protected methods
	//

	@Override
	protected final void setUp()
		throws Exception {

		super.setUp();

		mContext = new MockFacesContext();
	}

	@Override
	protected final void tearDown()
		throws Exception {

		super.tearDown();

		mContext.release();
	}
}
