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

package org.metawidget.inspector.json;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.io.ByteArrayInputStream;

import junit.framework.TestCase;

import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.util.MetawidgetTestUtils;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class JsonInspectorTest
	extends TestCase {

	@SuppressWarnings( "unused" )
	public void testMissingFile() {

		try {
			new JsonInspector( new JsonInspectorConfig() );
			fail();
		} catch ( InspectorException e ) {
			assertEquals( "No JSON input stream specified", e.getMessage() );
		}
	}

	public void testNormalUse() {

		String json = "{ \"foo\": \"Foo\", \"bar\": 42, \"baz\": true, \"abc\": [ 1, 2, 3 ], \"def\": { \"one\": 1, \"two\": 2 }}";
		JsonInspector inspector = new JsonInspector( new JsonInspectorConfig().setInputStream( new ByteArrayInputStream( json.getBytes() ) ) );
		Document document = XmlUtils.documentFromString( inspector.inspect( null, "fooObject" ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertFalse( entity.hasAttribute( NAME ) );
		assertEquals( "fooObject", entity.getAttribute( TYPE ) );

		// Properties

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "foo", property.getAttribute( NAME ) );
		assertEquals( String.class.getName(), property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "bar", property.getAttribute( NAME ) );
		assertEquals( int.class.getName(), property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "baz", property.getAttribute( NAME ) );
		assertEquals( boolean.class.getName(), property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "abc", property.getAttribute( NAME ) );
		assertEquals( "array", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "def", property.getAttribute( NAME ) );
		assertEquals( Object.class.getName(), property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		assertEquals( property.getNextSibling(), null );
	}

	public void testTraversal() {

		String json = "{ \"path1\": { \"foo\": \"Foo\", \"bar\": 42 }, \"path2\": { \"baz\": true }}";
		JsonInspector inspector = new JsonInspector( new JsonInspectorConfig().setInputStream( new ByteArrayInputStream( json.getBytes() ) ) );

		// Path1

		Document document = XmlUtils.documentFromString( inspector.inspect( null, "fooObject", "path1" ) );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );
		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertFalse( entity.hasAttribute( NAME ) );
		assertEquals( "fooObject", entity.getAttribute( TYPE ) );
		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "foo", property.getAttribute( NAME ) );
		assertEquals( String.class.getName(), property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );
		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "bar", property.getAttribute( NAME ) );
		assertEquals( int.class.getName(), property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );
		assertEquals( property.getNextSibling(), null );

		// Path2

		document = XmlUtils.documentFromString( inspector.inspect( null, "fooObject", "path2" ) );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );
		entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertFalse( entity.hasAttribute( NAME ) );
		assertEquals( "fooObject", entity.getAttribute( TYPE ) );
		property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "baz", property.getAttribute( NAME ) );
		assertEquals( property.getAttributes().getLength(), 2 );
		assertEquals( property.getNextSibling(), null );

		// Bad path

		assertEquals( null, XmlUtils.documentFromString( inspector.inspect( null, "fooObject", "badPath" ) ) );
	}

	public void testConfig() {

		MetawidgetTestUtils.testEqualsAndHashcode( JsonInspectorConfig.class, new JsonInspectorConfig() {
			// Subclass
		} );
	}
}
