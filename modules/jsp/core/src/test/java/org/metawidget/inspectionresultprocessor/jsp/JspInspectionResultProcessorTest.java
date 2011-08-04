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

package org.metawidget.inspectionresultprocessor.jsp;

import static org.metawidget.inspector.InspectionResultConstants.*;
import junit.framework.TestCase;

import org.metawidget.jsp.JspMetawidgetTests.MockPageContext;
import org.metawidget.jsp.tagext.MetawidgetTag;
import org.metawidget.jsp.tagext.html.HtmlMetawidgetTag;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Richard Kennard
 */

public class JspInspectionResultProcessorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testXml() {

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<inspection-result xmlns=\"http://www.metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.metawidget.org/inspection-result ../../inspectionResultProcessor/inspection-result-1.0.xsd\" version=\"1.0\">";
		xml += "<entity type=\"Foo\">";
		xml += "<property name=\"bar1\" value-is-el=\"${baz1}\" value-is-text=\"text\"/>";
		xml += "<property name=\"bar2\" value-is-null=\"${null}\"/>";
		xml += "<action name=\"bar3\" value-is-el=\"${baz2}\" value-is-text=\"text\"/>";
		xml += "</entity></inspection-result>";

		JspInspectionResultProcessor inspectionResultProcessor = new JspInspectionResultProcessor();
		MetawidgetTag metawidgetTag = new HtmlMetawidgetTag();
		metawidgetTag.setPageContext( new MockPageContext() );

		String result = inspectionResultProcessor.processInspectionResult( xml, metawidgetTag, null, null );
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
		assertEquals( "result of ${baz1}", property.getAttribute( "value-is-el" ) );
		assertEquals( "text", property.getAttribute( "value-is-text" ) );
		assertTrue( 3 == property.getAttributes().getLength() );

		property = XmlUtils.getNextSiblingElement( property );
		assertEquals( PROPERTY, property.getNodeName() );
		assertTrue( !property.hasAttribute( "value-is-null" ) );
		assertTrue( 1 == property.getAttributes().getLength() );

		// Actions

		Element action = XmlUtils.getChildWithAttributeValue( entity, NAME, "bar3" );
		assertEquals( ACTION, action.getNodeName() );
		assertEquals( "result of ${baz2}", action.getAttribute( "value-is-el" ) );
		assertEquals( "text", action.getAttribute( "value-is-text" ) );
		assertTrue( 3 == action.getAttributes().getLength() );

		assertTrue( entity.getChildNodes().getLength() == 3 );
	}
}
