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

package org.metawidget.inspector.wsdl;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.io.ByteArrayInputStream;

import junit.framework.TestCase;

import org.metawidget.config.impl.SimpleResourceResolver;
import org.metawidget.inspector.iface.Inspector;
import org.metawidget.inspector.xsd.XmlSchemaInspectorConfig;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Richard Kennard
 */

public class WsdlInspectorTest
	extends TestCase {

	//
	// Public methods
	//

	@SuppressWarnings( "unused" )
	public void testBadWsdl() {

		// Empty

		String wsdl = "<?xml version=\"1.0\"?>";

		try {
			new WsdlInspector( new XmlSchemaInspectorConfig().setInputStream( new ByteArrayInputStream( wsdl.getBytes() ) ) );
		} catch ( Exception e ) {
			assertEquals( "org.xml.sax.SAXParseException: Premature end of file.", e.getMessage() );
		}

		// No types

		wsdl += "<description>";
		wsdl += "</description>";

		try {
			new WsdlInspector( new XmlSchemaInspectorConfig().setInputStream( new ByteArrayInputStream( wsdl.getBytes() ) ) );
		} catch ( Exception e ) {
			assertEquals( "No types element", e.getMessage() );
		}

		// No schema

		wsdl = "<?xml version=\"1.0\"?>";
		wsdl += "<description>";
		wsdl += "<types></types>";
		wsdl += "</description>";

		try {
			new WsdlInspector( new XmlSchemaInspectorConfig().setInputStream( new ByteArrayInputStream( wsdl.getBytes() ) ) );
		} catch ( Exception e ) {
			assertEquals( "No types/schema element", e.getMessage() );
		}
	}

	public void testWsdl() {

		Inspector inspector = new WsdlInspector( new XmlSchemaInspectorConfig().setInputStream( new SimpleResourceResolver().openResource( "org/metawidget/inspector/wsdl/sample.wsdl" ) ) );

		// Request

		Document document = XmlUtils.documentFromString( inspector.inspect( null, "request" ) );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertFalse( entity.hasAttribute( NAME ) );
		assertEquals( "request", entity.getAttribute( TYPE ) );

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "header", property.getAttribute( NAME ) );
		assertEquals( "xs:string", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "body", property.getAttribute( NAME ) );
		assertEquals( "xs:anyType", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		assertEquals( property.getNextSibling(), null );

		// Header

		try {
			document = XmlUtils.documentFromString( inspector.inspect( null, "request", "header" ) );
			assertTrue( false );
		} catch ( Exception e ) {
			assertEquals( "Unexpected child node 'simpleContent'", e.getMessage() );
		}

		// Response

		document = XmlUtils.documentFromString( inspector.inspect( null, "response" ) );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertFalse( entity.hasAttribute( NAME ) );
		assertEquals( "response", entity.getAttribute( TYPE ) );

		property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "header", property.getAttribute( NAME ) );
		assertEquals( "xs:string", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "body", property.getAttribute( NAME ) );
		assertEquals( "xs:anyType", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		assertEquals( property.getNextSibling(), null );
	}
}
