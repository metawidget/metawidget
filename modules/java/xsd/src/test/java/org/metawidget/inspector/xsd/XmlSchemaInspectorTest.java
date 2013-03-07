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

	//
	// Private members
	//

	private Inspector	mInspector;

	//
	// Public methods
	//

	@Override
	public void setUp() {

		mInspector = new XmlSchemaInspector( new XmlSchemaInspectorConfig().setInputStream( new SimpleResourceResolver().openResource( "org/metawidget/inspector/xsd/shiporder.xsd" ) ) );
	}

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

		Document document = XmlUtils.documentFromString( mInspector.inspect( null, "shiporder" ) );

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
		assertEquals( property.getAttributes().getLength(), 1 );

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

	public void testConfig() {

		MetawidgetTestUtils.testEqualsAndHashcode( XmlSchemaInspectorConfig.class, new XmlSchemaInspectorConfig() {
			// Subclass
		}, "inputStreams" );
	}
}
