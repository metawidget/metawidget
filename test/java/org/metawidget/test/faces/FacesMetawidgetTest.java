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

package org.metawidget.test.faces;

import java.io.IOException;
import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.UIStub;
import org.metawidget.faces.component.html.HtmlMetawidget;
import org.metawidget.util.CollectionUtils;

/**
 * UIMetawidget test cases.
 * <p>
 * These are just some fringe-case tests. Most of the testing is done by WebTest.
 *
 * @author Richard Kennard
 */

public class FacesMetawidgetTest
	extends TestCase
{
	//
	//
	// Public methods
	//
	//

	public void testMetawidget()
		throws Exception
	{
		UIMetawidget metawidget = new HtmlMetawidget();

		try
		{
			metawidget.encodeBegin( null );
		}
		catch ( Exception e )
		{
			// Should fail with an IOException (not a MetawidgetException)

			assertTrue( e instanceof IOException );
		}
	}

	public void testStub()
		throws Exception
	{
		UIStub stub = new UIStub();
		stub.setStubAttributes( "rendered:" );

		try
		{
			stub.getStubAttributes();
			assertTrue( false );
		}
		catch ( Exception e )
		{
			// Should fail

			assertTrue( "Unrecognized value 'rendered:'".equals( e.getMessage() ) );
		}

		stub.setStubAttributes( "rendered:;" );

		try
		{
			stub.getStubAttributes();
			assertTrue( false );
		}
		catch ( Exception e )
		{
			// Should fail

			assertTrue( "Unrecognized value 'rendered:'".equals( e.getMessage() ) );
		}

		stub.setStubAttributes( "rendered: true" );
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( "rendered", "true" );
		assertTrue( attributes.equals( stub.getStubAttributes() ) );

		stub.setStubAttributes( "rendered: false;" );
		attributes.put( "rendered", "false" );
		assertTrue( attributes.equals( stub.getStubAttributes() ) );
	}

	//
	//
	// Constructor
	//
	//

	/**
	 * JUnit 3.7 constructor.
	 */

	public FacesMetawidgetTest( String name )
	{
		super( name );
	}
}
