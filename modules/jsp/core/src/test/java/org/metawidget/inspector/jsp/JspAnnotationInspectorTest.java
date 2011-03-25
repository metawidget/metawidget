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

package org.metawidget.inspector.jsp;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.jsp.JspInspectionResultConstants.*;
import junit.framework.TestCase;

import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.jsp.JspMetawidgetTests.MockPageContext;
import org.metawidget.jsp.JspUtilsTest.DummyPageContext;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Richard Kennard
 */

public class JspAnnotationInspectorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testLookup() {

		JspAnnotationInspector.setThreadLocalPageContext( new MockPageContext() );

		JspAnnotationInspector inspector = new JspAnnotationInspector();
		Document document = XmlUtils.documentFromString( inspector.inspect( new Foo(), Foo.class.getName() ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getFirstChild().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( Foo.class.getName(), entity.getAttribute( TYPE ) );
		assertFalse( entity.hasAttribute( NAME ) );

		// Properties

		Element property = XmlUtils.getChildWithAttributeValue( entity, NAME, "object1" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "${foo.bar}", property.getAttribute( JSP_LOOKUP ) );
		assertTrue( 2 == property.getAttributes().getLength() );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "object2" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "result of ${foo1}", property.getAttribute( "foo1" ) );
		assertEquals( "result of ${foo1}", property.getAttribute( "foo2" ) );
		assertTrue( 3 == property.getAttributes().getLength() );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "object3" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "1,2", property.getAttribute( "array" ) );
		assertEquals( "${collection1},${collection1}", property.getAttribute( "collection" ) );
		assertTrue( 3 == property.getAttributes().getLength() );

		assertTrue( 3 == entity.getChildNodes().getLength() );

		JspAnnotationInspector.setThreadLocalPageContext( null );
	}

	public void testAttribute() {

		JspAnnotationInspector inspector = new JspAnnotationInspector();

		try {
			inspector.inspect( new Bar(), Bar.class.getName() );
		} catch ( InspectorException e ) {
			assertEquals( "ThreadLocalPageContext not set", e.getMessage() );
		}

		try {
			JspAnnotationInspector.setThreadLocalPageContext( new DummyPageContext() );
			inspector.inspect( new Bar(), Bar.class.getName() );
		} catch ( InspectorException e ) {
			assertEquals( "Expression 'bad-expression' is not of the form ${...}", e.getMessage() );
		}
	}

	//
	// Inner class
	//

	public static class Foo {

		@UiJspLookup( "${foo.bar}" )
		public Object	object1;

		@UiJspAttribute( name = { "foo1", "foo2" }, expression = "${foo1}" )
		public Object	object2;

		@UiJspAttributes( { @UiJspAttribute( name = "array", expression = "${array1}" ), @UiJspAttribute( name = "collection", expression = "${collection1}" ) } )
		public Object	object3;
	}

	public static class Bar {

		@UiJspAttribute( name = "baz", expression = "bad-expression" )
		public Object	object1;
	}
}
