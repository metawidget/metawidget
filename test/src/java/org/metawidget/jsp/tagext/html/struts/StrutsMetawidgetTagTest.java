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

package org.metawidget.jsp.tagext.html.struts;

import junit.framework.TestCase;

import org.metawidget.jsp.tagext.html.struts.StrutsMetawidgetTag;

/**
 * StrutsMetawidgetTag test cases.
 * <p>
 * These are just some fringe-case tests. Most of the testing is done by WebTest.
 *
 * @author Richard Kennard
 */

public class StrutsMetawidgetTagTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testMetawidget()
		throws Exception
	{
		StrutsMetawidgetTag metawidget = new StrutsMetawidgetTag();

		// Property without prefix

		metawidget.setProperty( "foo" );
		assertTrue( "foo".equals( metawidget.getPath() ));
		assertTrue( null == metawidget.getPathPrefix() );

		// Property with prefix

		metawidget.setProperty( "foo.bar" );
		assertTrue( "foo.bar".equals( metawidget.getPath() ));
		assertTrue( null == metawidget.getPathPrefix() );

		metawidget.setProperty( "foo.bar.baz" );
		assertTrue( "foo.bar.baz".equals( metawidget.getPath() ));
		assertTrue( "bar.".equals( metawidget.getPathPrefix() ));
	}
}
