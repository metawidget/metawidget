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
