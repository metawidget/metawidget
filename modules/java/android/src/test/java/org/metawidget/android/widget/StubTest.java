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

public class StubTest
	extends TestCase {

	//
	// Public methods
	//

	public void testStub() {

		Stub stub = new Stub( null );
		assertEquals( stub.getAttributes(), null );

		stub.setAttribute( "foo", "bar" );
		assertEquals( "bar", stub.getAttributes().get( "foo" ) );
		assertEquals( null, stub.getTag() );

		MockAttributeSet attributeSet = new MockAttributeSet();
		attributeSet.setAttributeValue( "foo", "should-not-appear" );
		attributeSet.setAttributeValue( "attribfoo", "should-not-appear-either" );
		attributeSet.setAttributeValue( "attribName", "bar" );
		attributeSet.setAttributeValue( "tag", "baz" );
		stub = new Stub( null, attributeSet );
		assertEquals( "bar", stub.getAttributes().get( "name" ) );
		assertEquals( 1, stub.getAttributes().size() );
		assertEquals( "baz", stub.getTag() );
	}
}
