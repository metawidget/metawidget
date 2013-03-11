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

package org.metawidget.inspector.xsd;

import static org.metawidget.inspector.InspectionResultConstants.*;
import junit.framework.TestCase;

import org.metawidget.config.impl.SimpleResourceResolver;
import org.metawidget.inspector.iface.Inspector;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.util.MetawidgetTestUtils;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Richard Kennard
 */

public class XmlSchemaInspectorTest
	extends TestCase {

	@SuppressWarnings( "unused" )
	public void testMissingFile() {

		try {
			new XmlSchemaInspector( new XmlSchemaInspectorConfig() );
			fail();
		} catch ( InspectorException e ) {
			assertEquals( "No XML input file specified", e.getMessage() );
		}
	}

	public void testProperties() {

		Inspector inspector = new XmlSchemaInspector( new XmlSchemaInspectorConfig().setInputStream( new SimpleResourceResolver().openResource( "org/metawidget/inspector/xsd/shiporder.xsd" ) ) );
		Document document = XmlUtils.documentFromString( inspector.inspect( null, "shiporder" ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertFalse( entity.hasAttribute( NAME ) );
		assertEquals( "shiporder", entity.getAttribute( TYPE ) );

		// Properties

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "orderperson", property.getAttribute( NAME ) );
		assertEquals( "xs:string", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "shipto", property.getAttribute( NAME ) );
		assertEquals( property.getAttributes().getLength(), 1 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "item", property.getAttribute( NAME ) );
		assertEquals( property.getAttributes().getLength(), 1 );

		assertEquals( property.getNextSibling(), null );
	}

	public void testDividedProperties() {

		Inspector inspector = new XmlSchemaInspector( new XmlSchemaInspectorConfig().setInputStream( new SimpleResourceResolver().openResource( "org/metawidget/inspector/xsd/shiporder-divided.xsd" ) ) );
		Document document = XmlUtils.documentFromString( inspector.inspect( null, "shiporder" ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertFalse( entity.hasAttribute( NAME ) );
		assertEquals( "shiporder", entity.getAttribute( TYPE ) );

		// Properties

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "orderperson", property.getAttribute( NAME ) );
		assertEquals( "xs:string", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "shipto", property.getAttribute( NAME ) );
		assertEquals( property.getAttributes().getLength(), 1 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "item", property.getAttribute( NAME ) );
		assertEquals( property.getAttributes().getLength(), 1 );

		assertEquals( property.getNextSibling(), null );
	}

	public void testNamedProperties() {

		Inspector inspector = new XmlSchemaInspector( new XmlSchemaInspectorConfig().setInputStream( new SimpleResourceResolver().openResource( "org/metawidget/inspector/xsd/shiporder-named.xsd" ) ) );
		Document document = XmlUtils.documentFromString( inspector.inspect( null, "shiporder" ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertFalse( entity.hasAttribute( NAME ) );
		assertEquals( "shiporder", entity.getAttribute( TYPE ) );

		// Properties

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "orderperson", property.getAttribute( NAME ) );
		assertEquals( "stringtype", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "shipto", property.getAttribute( NAME ) );
		assertEquals( "shiptotype", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "item", property.getAttribute( NAME ) );
		assertEquals( "itemtype", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		assertEquals( property.getNextSibling(), null );
	}

	public void testNestedProperties() {

		Inspector inspector = new XmlSchemaInspector( new XmlSchemaInspectorConfig().setInputStream( new SimpleResourceResolver().openResource( "org/metawidget/inspector/xsd/shiporder.xsd" ) ) );
		Document document = XmlUtils.documentFromString( inspector.inspect( null, "shiporder", "shipto" ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "shipto", entity.getAttribute( NAME ) );
		assertEquals( "", entity.getAttribute( TYPE ) );

		// Properties

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "name", property.getAttribute( NAME ) );
		assertEquals( "xs:string", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "address", property.getAttribute( NAME ) );
		assertEquals( "xs:string", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "city", property.getAttribute( NAME ) );
		assertEquals( "xs:string", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "country", property.getAttribute( NAME ) );
		assertEquals( "xs:string", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		assertEquals( property.getNextSibling(), null );
	}

	public void testDividedNestedProperties() {

		Inspector inspector = new XmlSchemaInspector( new XmlSchemaInspectorConfig().setInputStream( new SimpleResourceResolver().openResource( "org/metawidget/inspector/xsd/shiporder-divided.xsd" ) ) );
		Document document = XmlUtils.documentFromString( inspector.inspect( null, "shiporder", "shipto" ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "shipto", entity.getAttribute( NAME ) );
		assertEquals( "", entity.getAttribute( TYPE ) );

		// Properties

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "name", property.getAttribute( NAME ) );
		assertEquals( "xs:string", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "address", property.getAttribute( NAME ) );
		assertEquals( "xs:string", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "city", property.getAttribute( NAME ) );
		assertEquals( "xs:string", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "country", property.getAttribute( NAME ) );
		assertEquals( "xs:string", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		assertEquals( property.getNextSibling(), null );
	}

	public void testQuirks() {

		Inspector inspector = new XmlSchemaInspector( new XmlSchemaInspectorConfig().setInputStream( new SimpleResourceResolver().openResource( "org/metawidget/inspector/xsd/quirks.xsd" ) ) );
		Document document = XmlUtils.documentFromString( inspector.inspect( null, "quirks" ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "quirks", entity.getAttribute( TYPE ) );

		// Properties

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "name", property.getAttribute( NAME ) );
		assertEquals( TRUE, property.getAttribute( REQUIRED ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		assertEquals( property.getNextSibling(), null );
	}

	public void testConfig() {

		MetawidgetTestUtils.testEqualsAndHashcode( XmlSchemaInspectorConfig.class, new XmlSchemaInspectorConfig() {
			// Subclass
		}, "inputStreams" );
	}
}
