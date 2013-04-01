// Metawidget (licensed under LGPL)
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

package org.metawidget.inspector.json;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.io.ByteArrayInputStream;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.composite.CompositeInspectorConfig;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.json.schema.JsonSchemaInspector;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Richard Kennard
 */

public class JsonSchemaInspectorTest
	extends TestCase {

	@SuppressWarnings( "unused" )
	public void testMissingFile() {

		try {
			new JsonSchemaInspector( new JsonInspectorConfig() );
			fail();
		} catch ( InspectorException e ) {
			assertEquals( "No JSON input stream specified", e.getMessage() );
		}
	}

	public void testNormalUse() {

		String json = "{ \"foo\": \"Foo\", properties: { \"bar\": { \"barProp\": 42, \"baz\": true }, \"abc\": { \"arrayProp\": [ 1, 2, \"3,4\" ] }}}";
		JsonSchemaInspector inspector = new JsonSchemaInspector( new JsonInspectorConfig().setInputStream( new ByteArrayInputStream( json.getBytes() ) ) );
		Document document = XmlUtils.documentFromString( inspector.inspect( null, "fooObject" ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertFalse( entity.hasAttribute( NAME ) );
		assertEquals( "fooObject", entity.getAttribute( TYPE ) );
		assertEquals( "Foo", entity.getAttribute( "foo" ) );
		assertEquals( entity.getAttributes().getLength(), 2 );

		// Properties

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "bar", property.getAttribute( NAME ) );
		assertEquals( "42", property.getAttribute( "barProp" ) );
		assertEquals( "true", property.getAttribute( "baz" ) );
		assertEquals( property.getAttributes().getLength(), 3 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "abc", property.getAttribute( NAME ) );
		assertEquals( "1,2,3\\,4", property.getAttribute( "arrayProp" ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		assertEquals( property.getNextSibling(), null );
	}

	public void testTraversal() {

		String json = "{ properties: { \"path1\": { \"properties\": { \"foo\": { \"fooProp\": \"Foo\" }}, \"bar\": 42 }, \"path2\": { \"baz\": true }}}";
		JsonSchemaInspector inspector = new JsonSchemaInspector( new JsonInspectorConfig().setInputStream( new ByteArrayInputStream( json.getBytes() ) ) );

		// Path1

		Document document = XmlUtils.documentFromString( inspector.inspect( null, "fooObject", "path1" ) );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );
		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "path1", entity.getAttribute( NAME ) );
		assertEquals( "fooObject", entity.getAttribute( TYPE ) );
		assertEquals( "42", entity.getAttribute( "bar" ) );
		assertEquals( entity.getAttributes().getLength(), 3 );
		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "foo", property.getAttribute( NAME ) );
		assertEquals( "Foo", property.getAttribute( "fooProp" ) );
		assertEquals( property.getAttributes().getLength(), 2 );
		assertEquals( property.getNextSibling(), null );

		// Path2

		document = XmlUtils.documentFromString( inspector.inspect( null, "fooObject", "path2" ) );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );
		entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "path2", entity.getAttribute( NAME ) );
		assertEquals( "fooObject", entity.getAttribute( TYPE ) );
		assertEquals( "true", entity.getAttribute( "baz" ) );
		assertEquals( entity.getAttributes().getLength(), 3 );
		assertEquals( entity.getFirstChild(), null );

		// Bad path

		assertEquals( null, XmlUtils.documentFromString( inspector.inspect( null, "fooObject", "badPath" ) ) );
	}

	public void testRealWorld() {

		Display display = new Display();
		Shell shell = new Shell( display );
		shell.setLayout( new FillLayout() );

		String json = "{ \"firstname\": \"Richard\", \"surname\": \"Kennard\", \"notes\": \"Software developer\" }";
		String jsonSchema = "{ properties: { \"firstname\": { \"required\": true }, \"notes\": { \"large\": true }}}";

		SwtMetawidget metawidget = new SwtMetawidget( shell, SWT.None );
		metawidget.setInspector( new CompositeInspector( new CompositeInspectorConfig().setInspectors(
				new JsonInspector( new JsonInspectorConfig().setInputStream( new ByteArrayInputStream( json.getBytes() ) ) ),
				new JsonSchemaInspector( new JsonInspectorConfig().setInputStream( new ByteArrayInputStream( jsonSchema.getBytes() ) ) ) ) ) );
		metawidget.setInspectionPath( "fooObject" );

		assertEquals( "Firstname*:", ( (Label) metawidget.getChildren()[0] ).getText() );
		assertTrue( metawidget.getChildren()[1] instanceof Text );
		assertEquals( "Surname:", ( (Label) metawidget.getChildren()[2] ).getText() );
		assertTrue( metawidget.getChildren()[3] instanceof Text );
		assertEquals( "Notes:", ( (Label) metawidget.getChildren()[4] ).getText() );
		assertTrue( metawidget.getChildren()[5] instanceof Text );
		assertEquals( ( metawidget.getChildren()[5].getStyle() & SWT.MULTI ), SWT.MULTI );
		assertEquals( 6, metawidget.getChildren().length );
	}
}
