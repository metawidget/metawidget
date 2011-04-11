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

package org.metawidget.inspector.faces;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.io.ByteArrayInputStream;

import javax.faces.context.FacesContext;

import junit.framework.TestCase;

import org.metawidget.faces.FacesMetawidgetTests.MockFacesContext;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.util.MetawidgetTestUtils;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Richard Kennard
 */

// TODO: doco

public class FacesXmlInspectorTest
	extends TestCase {

	//
	// Private members
	//

	private FacesContext	mContext;

	//
	// Public methods
	//

	public void testXml() {

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<inspection-result xmlns=\"http://www.metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.metawidget.org/inspection-result ../../inspector/inspection-result-1.0.xsd\" version=\"1.0\">";
		xml += "<entity type=\"Foo\">";
		xml += "<property name=\"bar1\" value-is-el=\"#{this.baz}\" value-is-text=\"text\"/>";
		xml += "<action name=\"bar2\" value-is-el=\"#{this.baz}\" value-is-text=\"text\"/>";
		xml += "</entity></inspection-result>";

		FacesXmlInspector inspector = new FacesXmlInspector( new FacesXmlInspectorConfig().setInputStream( new ByteArrayInputStream( xml.getBytes() ) ).setInjectThis( true ) );

		String result = inspector.inspect( null, "Foo" );
		Document document = XmlUtils.documentFromString( result );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getFirstChild().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "Foo", entity.getAttribute( TYPE ) );
		assertFalse( entity.hasAttribute( NAME ) );

		// Properties

		Element property = XmlUtils.getChildWithAttributeValue( entity, NAME, "bar1" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "result of #{this.baz}", property.getAttribute( "value-is-el" ) );
		assertEquals( "text", property.getAttribute( "value-is-text" ) );
		assertTrue( 3 == property.getAttributes().getLength() );

		// Actions

		Element action = XmlUtils.getChildWithAttributeValue( entity, NAME, "bar2" );
		assertEquals( ACTION, action.getNodeName() );
		assertEquals( "result of #{this.baz}", action.getAttribute( "value-is-el" ) );
		assertEquals( "text", action.getAttribute( "value-is-text" ) );
		assertTrue( 3 == action.getAttributes().getLength() );

		assertTrue( entity.getChildNodes().getLength() == 2 );

		// Test 'this' check

		inspector = new FacesXmlInspector( new FacesXmlInspectorConfig().setInputStream( new ByteArrayInputStream( xml.getBytes() ) ) );

		try {
			inspector.inspect( null, "Foo" );
			assertTrue( false );
		} catch ( InspectorException e ) {
			assertEquals( "Expression for '#{this.baz}' contains 'this', but FacesXmlInspectorConfig.setInjectThis is 'false'", e.getMessage() );
		}
	}

	public void testConfig() {

		MetawidgetTestUtils.testEqualsAndHashcode( FacesXmlInspectorConfig.class, new FacesXmlInspectorConfig() {
			// Subclass
		} );
	}

	//
	// Protected methods
	//

	@Override
	protected final void setUp()
		throws Exception {

		super.setUp();

		mContext = new MockFacesContext();
	}

	@Override
	protected final void tearDown()
		throws Exception {

		super.tearDown();

		mContext.release();
	}
}
