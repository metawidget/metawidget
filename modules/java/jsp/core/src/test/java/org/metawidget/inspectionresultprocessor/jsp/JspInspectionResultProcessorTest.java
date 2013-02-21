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
		xml += "<property name=\"bar2\" value-is-null=\"${null}\" value-is-embedded-el=\"first ${abc} middle ${null}${def} last\"/>";
		xml += "<action name=\"bar3\" value-is-el=\"${baz2}\" value-is-text=\"text\"/>";
		xml += "</entity></inspection-result>";

		JspInspectionResultProcessor inspectionResultProcessor = new JspInspectionResultProcessor();
		MetawidgetTag metawidgetTag = new HtmlMetawidgetTag();
		metawidgetTag.setPageContext( new MockPageContext() );

		String result = inspectionResultProcessor.processInspectionResult( xml, metawidgetTag, null, null );
		Document document = XmlUtils.documentFromString( result );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "Foo", entity.getAttribute( TYPE ) );
		assertFalse( entity.hasAttribute( NAME ) );

		// Properties

		Element property = XmlUtils.getFirstChildElement( entity );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "bar1", property.getAttribute( NAME ));
		assertEquals( "result of ${baz1}", property.getAttribute( "value-is-el" ) );
		assertEquals( "text", property.getAttribute( "value-is-text" ) );
		assertEquals( 3, property.getAttributes().getLength() );

		property = XmlUtils.getNextSiblingElement( property );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "bar2", property.getAttribute( NAME ));
		assertTrue( !property.hasAttribute( "value-is-null" ) );
		assertEquals( "first result of ${abc} middle result of ${def} last", property.getAttribute( "value-is-embedded-el" ) );
		assertEquals( 2, property.getAttributes().getLength() );

		// Actions

		Element action = XmlUtils.getNextSiblingElement( property );
		assertEquals( ACTION, action.getNodeName() );
		assertEquals( "bar3", action.getAttribute( NAME ));
		assertEquals( "result of ${baz2}", action.getAttribute( "value-is-el" ) );
		assertEquals( "text", action.getAttribute( "value-is-text" ) );
		assertEquals( 3, action.getAttributes().getLength() );

		assertEquals( entity.getChildNodes().getLength(), 3 );
	}

	public void testArrays() {

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<inspection-result xmlns=\"http://www.metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.metawidget.org/inspection-result ../../inspector/inspection-result-1.0.xsd\" version=\"1.0\">";
		xml += "<entity type=\"Company\">";
		xml += "<property name=\"employee\" lookup=\"${array1,}\" lookup2=\"${collection1,}\"/>";
		xml += "</entity></inspection-result>";

		JspInspectionResultProcessor inspectionResultProcessor = new JspInspectionResultProcessor();
		MetawidgetTag metawidgetTag = new HtmlMetawidgetTag();
		metawidgetTag.setPageContext( new MockPageContext() );

		String result = inspectionResultProcessor.processInspectionResult( xml, metawidgetTag, null, null );
		Document document = XmlUtils.documentFromString( result );
		Element entity = XmlUtils.getFirstChildElement( document.getDocumentElement() );
		Element property = XmlUtils.getFirstChildElement( entity );
		assertEquals( "employee", property.getAttribute( NAME ) );
		assertEquals( "${array1\\,},${array1\\,}", property.getAttribute( "lookup" ) );
		assertEquals( "${collection1\\,},${collection1\\,}", property.getAttribute( "lookup2" ) );
		assertEquals( 3, property.getAttributes().getLength() );

		assertEquals( entity.getChildNodes().getLength(), 1 );
	}

}
