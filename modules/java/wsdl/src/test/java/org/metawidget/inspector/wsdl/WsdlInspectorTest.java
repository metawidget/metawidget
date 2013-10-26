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

package org.metawidget.inspector.wsdl;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.io.ByteArrayInputStream;

import javax.swing.JLabel;
import javax.swing.JTextField;

import junit.framework.TestCase;

import org.metawidget.config.impl.SimpleResourceResolver;
import org.metawidget.inspectionresultprocessor.xsd.XmlSchemaToJavaTypeMappingProcessor;
import org.metawidget.inspector.iface.Inspector;
import org.metawidget.inspector.xsd.XmlSchemaInspectorConfig;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
			assertTrue( e.getMessage(), e.getMessage().endsWith( " Premature end of file." ));
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

	public void testSampleWsdl() {

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

		// Header (not sure what this needs to return)

		assertEquals( null, XmlUtils.documentFromString( inspector.inspect( null, "header" ) ));

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

	public void testEndorsementSearchWsdl() {

		Inspector inspector = new WsdlInspector( new XmlSchemaInspectorConfig().setInputStream( new SimpleResourceResolver().openResource( "org/metawidget/inspector/wsdl/endorsementSearch.wsdl" ) ) );

		// GetEndorsingBoarder

		Document document = XmlUtils.documentFromString( inspector.inspect( null, "GetEndorsingBoarder" ) );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertFalse( entity.hasAttribute( NAME ) );
		assertEquals( "GetEndorsingBoarder", entity.getAttribute( TYPE ) );

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "manufacturer", property.getAttribute( NAME ) );
		assertEquals( "string", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "model", property.getAttribute( NAME ) );
		assertEquals( "string", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		assertEquals( property.getNextSibling(), null );

		// GetEndorsingBoarderResponse

		document = XmlUtils.documentFromString( inspector.inspect( null, "GetEndorsingBoarderResponse" ) );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertFalse( entity.hasAttribute( NAME ) );
		assertEquals( "GetEndorsingBoarderResponse", entity.getAttribute( TYPE ) );

		property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "endorsingBoarder", property.getAttribute( NAME ) );
		assertEquals( "string", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		assertEquals( property.getNextSibling(), null );

		// GetEndorsingBoarderFault

		document = XmlUtils.documentFromString( inspector.inspect( null, "GetEndorsingBoarderFault" ) );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertFalse( entity.hasAttribute( NAME ) );
		assertEquals( "GetEndorsingBoarderFault", entity.getAttribute( TYPE ) );

		property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "errorMessage", property.getAttribute( NAME ) );
		assertEquals( "string", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		assertEquals( property.getNextSibling(), null );
	}

	public void testStockQuoteWsdl() {

		Inspector inspector = new WsdlInspector( new XmlSchemaInspectorConfig().setInputStream( new SimpleResourceResolver().openResource( "org/metawidget/inspector/wsdl/stockquote.wsdl" ) ) );

		// GetEndorsingBoarder

		Document document = XmlUtils.documentFromString( inspector.inspect( null, "TradePriceRequest" ) );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertFalse( entity.hasAttribute( NAME ) );
		assertEquals( "TradePriceRequest", entity.getAttribute( TYPE ) );

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "tickerSymbol", property.getAttribute( NAME ) );
		assertEquals( "string", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		assertEquals( property.getNextSibling(), null );

		// GetEndorsingBoarderResponse

		document = XmlUtils.documentFromString( inspector.inspect( null, "TradePrice" ) );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertFalse( entity.hasAttribute( NAME ) );
		assertEquals( "TradePrice", entity.getAttribute( TYPE ) );

		property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "price", property.getAttribute( NAME ) );
		assertEquals( "float", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		assertEquals( property.getNextSibling(), null );
	}

	public void testSwingMetawidget() {

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setInspector( new WsdlInspector( new XmlSchemaInspectorConfig().setInputStream( new SimpleResourceResolver().openResource( "org/metawidget/inspector/wsdl/endorsementSearch.wsdl" ) ) ));
		metawidget.addInspectionResultProcessor( new XmlSchemaToJavaTypeMappingProcessor<SwingMetawidget>() );
		metawidget.setPath( "GetEndorsingBoarder" );

		assertEquals( "Manufacturer:", ((JLabel) metawidget.getComponent( 0 )).getText() );
		assertTrue( metawidget.getComponent( "manufacturer" ) instanceof JTextField );
		assertEquals( "Model:", ((JLabel) metawidget.getComponent( 2 )).getText() );
		assertTrue( metawidget.getComponent( "model" ) instanceof JTextField );
		assertEquals( 5, metawidget.getComponentCount() );
	}
}
