// Metawidget
//
// This library is dual licensed under both LGPL and a commercial
// license.
//
// LGPL: this library is free software; you can redistribute it and/or
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
//
// Commercial License: See http://metawidget.org for details

package org.metawidget.jsp.tagext.html.spring;

import junit.framework.TestCase;

import org.metawidget.jsp.tagext.html.spring.SpringMetawidgetTag;

/**
 * SpringMetawidgetTag test cases.
 * <p>
 * These are just some fringe-case tests. Most of the testing is done by WebTest.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class SpringMetawidgetTagTest
	extends TestCase {

	//
	// Public methods
	//

	public void testMetawidget()
		throws Exception {

		SpringMetawidgetTag metawidget = new SpringMetawidgetTag();

		// Path without prefix

		metawidget.setPath( "foo" );
		assertEquals( "foo", metawidget.getPath() );
		assertEquals( null, metawidget.getPathPrefix() );

		// Path with prefix

		metawidget.setPath( "foo.bar" );
		assertEquals( "foo.bar", metawidget.getPath() );
		assertEquals( null, metawidget.getPathPrefix() );

		metawidget.setPath( "foo.bar.baz" );
		assertEquals( "foo.bar.baz", metawidget.getPath() );
		assertEquals( "bar.", metawidget.getPathPrefix() );
	}
}
