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

package org.metawidget.inspectionresultprocessor.commons.jexl;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Collection;

import junit.framework.TestCase;

import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyle;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyleConfig;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.MetawidgetTestUtils;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Richard Kennard
 */

public class JexlInspectionResultProcessorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testXml() {

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<inspection-result xmlns=\"http://www.metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.metawidget.org/inspection-result ../../inspector/inspection-result-1.0.xsd\" version=\"1.0\">";
		xml += "<entity type=\"org.metawidget.inspector.commons.jexl.JexlXmlInspectorTest$Foo\">";
		xml += "<property name=\"bar1\" value-is-el=\"${this.baz}\" value-is-text=\"text\"/>";
		xml += "<property name=\"bar2\" value-is-null=\"${null}\" value-is-embedded-el=\"first ${this.abc} middle ${null}${this.def} last\"/>";
		xml += "<action name=\"bar3\" value-is-el=\"${this.baz}\" value-is-text=\"text\"/>";
		xml += "</entity></inspection-result>";

		JexlInspectionResultProcessor<?> inspectionResultProcessor = new JexlInspectionResultProcessor<Object>();

		String result = inspectionResultProcessor.processInspectionResult( xml, null, new Foo(), Foo.class.getName() );
		Document document = XmlUtils.documentFromString( result );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "org.metawidget.inspector.commons.jexl.JexlXmlInspectorTest$Foo", entity.getAttribute( TYPE ) );
		assertFalse( entity.hasAttribute( NAME ) );

		// Properties

		Element property = XmlUtils.getFirstChildElement( entity );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "bar1", property.getAttribute( NAME ) );
		assertEquals( "from-baz", property.getAttribute( "value-is-el" ) );
		assertEquals( "text", property.getAttribute( "value-is-text" ) );
		assertEquals( property.getAttributes().getLength(), 3 );

		property = XmlUtils.getNextSiblingElement( property );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "bar2", property.getAttribute( NAME ) );
		assertTrue( !property.hasAttribute( "value-is-null" ) );
		assertEquals( "first from-abc middle from-def last", property.getAttribute( "value-is-embedded-el" ) );
		assertEquals( 2, property.getAttributes().getLength() );

		// Actions

		Element action = XmlUtils.getNextSiblingElement( property );
		assertEquals( ACTION, action.getNodeName() );
		assertEquals( "bar3", action.getAttribute( NAME ) );
		assertEquals( "from-baz", action.getAttribute( "value-is-el" ) );
		assertEquals( "text", action.getAttribute( "value-is-text" ) );
		assertEquals( action.getAttributes().getLength(), 3 );

		assertEquals( entity.getChildNodes().getLength(), 3 );

		// Test null

		result = inspectionResultProcessor.processInspectionResult( xml, null, null, Foo.class.getName() );
		document = XmlUtils.documentFromString( result );
		entity = (Element) document.getDocumentElement().getFirstChild();

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "bar1" );
		assertTrue( !property.hasAttribute( "value-is-el" ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = XmlUtils.getNextSiblingElement( property );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "bar2", property.getAttribute( NAME ) );
		assertTrue( !property.hasAttribute( "value-is-null" ) );
		assertEquals( "first  middle  last", property.getAttribute( "value-is-embedded-el" ) );
		assertEquals( 2, property.getAttributes().getLength() );

		action = XmlUtils.getChildWithAttributeValue( entity, NAME, "bar2" );
		assertTrue( !action.hasAttribute( "value-is-el" ) );
		assertEquals( action.getAttributes().getLength(), 2 );

		assertEquals( entity.getChildNodes().getLength(), 3 );
	}

	public void testConfig()
		throws Exception {

		MetawidgetTestUtils.testEqualsAndHashcode( JexlInspectionResultProcessorConfig.class, new JexlInspectionResultProcessorConfig() {
			// Subclass
		} );
	}

	public void testThis() {

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<inspection-result xmlns=\"http://www.metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.metawidget.org/inspection-result ../../inspector/inspection-result-1.0.xsd\" version=\"1.0\">";
		xml += "<entity type=\"" + ThisTest.class.getName() + "\" who-am-i=\"${this.identity}\">";
		xml += "<property name=\"me\" who-am-i=\"${this.identity}\" />";
		xml += "<property name=\"child\" who-am-i=\"${this.child.identity}\" />";
		xml += "</entity></inspection-result>";

		ThisTest thisTest1 = new ThisTest();
		thisTest1.setIdentity( "ThisTest #1" );
		ThisTest thisTest2 = new ThisTest();
		thisTest2.setIdentity( "ThisTest #2" );
		thisTest1.setChild( thisTest2 );

		JexlInspectionResultProcessor<?> inspectionResultProcessor = new JexlInspectionResultProcessor<Object>( new JexlInspectionResultProcessorConfig().setInjectThis( new JavaBeanPropertyStyle( new JavaBeanPropertyStyleConfig().setSupportPublicFields( true ) ) ) );

		// Top-level

		String result = inspectionResultProcessor.processInspectionResult( xml, null, thisTest1, ThisTest.class.getName() );
		Document document = XmlUtils.documentFromString( result );
		Element entity = XmlUtils.getFirstChildElement( document.getDocumentElement() );
		assertEquals( ThisTest.class.getName(), entity.getAttribute( TYPE ) );
		assertTrue( !entity.hasAttribute( "who-am-i" ) );
		assertEquals( entity.getAttributes().getLength(), 1 );

		Element property = XmlUtils.getFirstChildElement( entity );
		assertEquals( "me", property.getAttribute( NAME ) );
		assertEquals( property.getAttribute( "who-am-i" ), "ThisTest #1" );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = XmlUtils.getNextSiblingElement( property );
		assertEquals( "child", property.getAttribute( NAME ) );
		assertEquals( property.getAttribute( "who-am-i" ), "ThisTest #2" );
		assertEquals( property.getAttributes().getLength(), 2 );

		assertEquals( entity.getChildNodes().getLength(), 2 );

		// Property-level

		result = inspectionResultProcessor.processInspectionResult( xml, null, thisTest1, ThisTest.class.getName(), "me" );
		document = XmlUtils.documentFromString( result );
		entity = XmlUtils.getFirstChildElement( document.getDocumentElement() );
		assertEquals( ThisTest.class.getName(), entity.getAttribute( TYPE ) );
		assertEquals( entity.getAttribute( "who-am-i" ), "ThisTest #1" );
		assertEquals( entity.getAttributes().getLength(), 2 );

		property = XmlUtils.getFirstChildElement( entity );
		assertEquals( "me", property.getAttribute( NAME ) );
		assertTrue( !property.hasAttribute( "who-am-i" ) );
		assertEquals( property.getAttributes().getLength(), 1 );

		property = XmlUtils.getNextSiblingElement( property );
		assertEquals( "child", property.getAttribute( NAME ) );
		assertTrue( !property.hasAttribute( "who-am-i" ) );
		assertEquals( property.getAttributes().getLength(), 1 );

		assertEquals( entity.getChildNodes().getLength(), 2 );

		// Child-level

		result = inspectionResultProcessor.processInspectionResult( xml, null, thisTest1, ThisTest.class.getName(), "child", "me" );
		document = XmlUtils.documentFromString( result );
		entity = XmlUtils.getFirstChildElement( document.getDocumentElement() );
		assertEquals( ThisTest.class.getName(), entity.getAttribute( TYPE ) );
		assertEquals( entity.getAttribute( "who-am-i" ), "ThisTest #2" );
		assertEquals( entity.getAttributes().getLength(), 2 );

		property = XmlUtils.getFirstChildElement( entity );
		assertEquals( "me", property.getAttribute( NAME ) );
		assertTrue( !property.hasAttribute( "who-am-i" ) );
		assertEquals( property.getAttributes().getLength(), 1 );

		property = XmlUtils.getNextSiblingElement( property );
		assertEquals( "child", property.getAttribute( NAME ) );
		assertTrue( !property.hasAttribute( "who-am-i" ) );
		assertEquals( property.getAttributes().getLength(), 1 );

		assertEquals( entity.getChildNodes().getLength(), 2 );
	}

	public void testInject() {

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<inspection-result xmlns=\"http://www.metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.metawidget.org/inspection-result ../../inspector/inspection-result-1.0.xsd\" version=\"1.0\">";
		xml += "<entity type=\"Company\">";
		xml += "<property name=\"employee\" lookup=\"${personController.all}\" />";
		xml += "</entity></inspection-result>";

		JexlInspectionResultProcessor<?> inspectionResultProcessor = new JexlInspectionResultProcessor<Object>( new JexlInspectionResultProcessorConfig().setInject( new PersonController() ) );

		String result = inspectionResultProcessor.processInspectionResult( xml, null, null, "Company" );
		Document document = XmlUtils.documentFromString( result );
		Element entity = XmlUtils.getFirstChildElement( document.getDocumentElement() );
		Element property = XmlUtils.getFirstChildElement( entity );
		assertEquals( "employee", property.getAttribute( NAME ) );
		assertEquals( "Tom1, Dick1, Harry1", property.getAttribute( "lookup" ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		assertEquals( entity.getChildNodes().getLength(), 1 );
	}

	public void testArrays() {

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<inspection-result xmlns=\"http://www.metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.metawidget.org/inspection-result ../../inspector/inspection-result-1.0.xsd\" version=\"1.0\">";
		xml += "<entity type=\"Company\">";
		xml += "<property name=\"employee\" lookup=\"${personController.allArray}\" lookup2=\"${personController.allCollection}\"/>";
		xml += "</entity></inspection-result>";

		JexlInspectionResultProcessor<?> inspectionResultProcessor = new JexlInspectionResultProcessor<Object>( new JexlInspectionResultProcessorConfig().setInject( new PersonController() ) );

		String result = inspectionResultProcessor.processInspectionResult( xml, null, null, "Company" );
		Document document = XmlUtils.documentFromString( result );
		Element entity = XmlUtils.getFirstChildElement( document.getDocumentElement() );
		Element property = XmlUtils.getFirstChildElement( entity );
		assertEquals( "employee", property.getAttribute( NAME ) );
		assertEquals( "Tom2\\,,Dick2,Harry2", property.getAttribute( "lookup" ) );
		assertEquals( "Tom3\\,,Dick3,Harry3", property.getAttribute( "lookup2" ) );
		assertEquals( 3, property.getAttributes().getLength() );

		assertEquals( entity.getChildNodes().getLength(), 1 );
	}

	//
	// Inner class
	//

	public static class Foo {

		public String getAbc() {

			return "from-abc";
		}

		public String getDef() {

			return "from-def";
		}

		public String getBaz() {

			return "from-baz";
		}
	}

	public static class ThisTest {

		private String		mIdentity;

		private ThisTest	mChild;

		public ThisTest		me;

		public ThisTest getChild() {

			return mChild;
		}

		public void setChild( ThisTest child ) {

			this.mChild = child;
		}

		public String getIdentity() {

			return this.mIdentity;
		}

		public void setIdentity( String identity ) {

			this.mIdentity = identity;
		}
	}

	public static class PersonController {

		public String getAll() {

			return "Tom1, Dick1, Harry1";
		}

		public String[] getAllArray() {

			return new String[] { "Tom2,", "Dick2", "Harry2" };
		}

		public Collection<String> getAllCollection() {

			return CollectionUtils.newArrayList( "Tom3,", "Dick3", "Harry3" );
		}
	}
}
