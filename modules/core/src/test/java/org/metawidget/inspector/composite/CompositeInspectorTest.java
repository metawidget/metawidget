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

package org.metawidget.inspector.composite;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.io.ByteArrayInputStream;

import junit.framework.TestCase;

import org.metawidget.inspector.iface.Inspector;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyle;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.inspector.xml.XmlInspector;
import org.metawidget.inspector.xml.XmlInspectorConfig;
import org.metawidget.util.LogUtilsTest;
import org.metawidget.util.MetawidgetTestUtils;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Richard Kennard
 */

public class CompositeInspectorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testInspection() {

		// Set up

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspection-result http://metawidget.org/xsd/inspection-result-1.0.xsd\" version=\"1.0\">";
		xml += "<entity type=\"" + Foo.class.getName() + "\">";
		xml += "<property name=\"id\" hidden=\"true\"/>";
		xml += "<property name=\"fullname\" hidden=\"true\"/>";
		xml += "<property name=\"title\" lookup=\"Mr, Mrs, Miss, Dr, Cpt\"/>";
		xml += "<property name=\"firstname\"/>";
		xml += "<property name=\"surname\"/>";
		xml += "<property name=\"gender\"/>";
		xml += "<property name=\"dateOfBirth\"/>";
		xml += "<property name=\"address\" section=\"Contact Details\" type=\"org.metawidget.example.shared.addressbook.model.Address\"/>";
		xml += "<property name=\"communications\"/>";
		xml += "<property name=\"notes\" large=\"true\" section=\"other\"/>";
		xml += "</entity></inspection-result>";

		XmlInspector inspectorXml = new XmlInspector( new XmlInspectorConfig().setRestrictAgainstObject( new JavaBeanPropertyStyle() ).setInputStream( new ByteArrayInputStream( xml.getBytes() ) ) );

		ValidatingCompositeInspectorConfig config = new ValidatingCompositeInspectorConfig();
		config.setInspectors( inspectorXml, new PropertyTypeInspector() );

		ValidatingCompositeInspector inspector = new ValidatingCompositeInspector( config );

		// Inspect

		Foo toInspect = new Foo$EnhancerByCGLIB$$1234();
		inspector.inspect( toInspect, toInspect.getClass().getName() );
		xml = inspector.inspect( toInspect, toInspect.getClass().getName() );
		internalTestInspection( XmlUtils.documentFromString( xml ) );

		Element domInspect = inspector.inspectAsDom( toInspect, toInspect.getClass().getName() );
		assertEquals( xml, XmlUtils.nodeToString( domInspect, false ) );
		internalTestInspection( domInspect.getOwnerDocument() );

		// As a normal Inspector (not a DomInspector)

		final String finalXml = xml;

		config.setInspectors( new Inspector() {

			public String inspect( Object inspect, String type, String... names ) {

				return finalXml;
			}
		} );

		inspector = new ValidatingCompositeInspector( config );
		xml = inspector.inspect( null, null );
		internalTestInspection( XmlUtils.documentFromString( xml ) );

		// As a normal Inspector (not a DomInspector) with a null result

		config.setInspectors( new Inspector() {

			public String inspect( Object inspect, String type, String... names ) {

				return null;
			}
		} );

		inspector = new ValidatingCompositeInspector( config );
		assertTrue( null == inspector.inspect( null, null ) );
	}

	@SuppressWarnings( "unused" )
	public void testDefensiveCopy()
		throws Exception {

		PropertyTypeInspector inspector = new PropertyTypeInspector();
		Inspector[] inspectors = new Inspector[] { inspector };
		CompositeInspectorConfig config = new CompositeInspectorConfig();
		config.setInspectors( inspectors );

		CompositeInspector inspectorComposite = new CompositeInspector( config );
		Inspector[] inspectorsCopied = inspectorComposite.mInspectors;
		assertTrue( inspectorsCopied[0] == inspector );
		inspectors[0] = null;
		assertTrue( inspectorsCopied[0] != null );

		// Test duplicates

		try {
			new CompositeInspector( new CompositeInspectorConfig().setInspectors( inspector, null, inspector ) );
			assertTrue( false );
		} catch ( InspectorException e ) {
			assertEquals( "CompositeInspector's list of Inspectors contains two of the same org.metawidget.inspector.propertytype.PropertyTypeInspector", e.getMessage() );
		}
	}

	public void testConfig() {

		MetawidgetTestUtils.testEqualsAndHashcode( CompositeInspectorConfig.class, new CompositeInspectorConfig() {
			// Subclass
		} );
	}

	//
	// Private methods
	//

	private void internalTestInspection( Document document ) {

		// Test

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getFirstChild().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( Foo$EnhancerByCGLIB$$1234.class.getName(), entity.getAttribute( TYPE ) );
		assertFalse( entity.hasAttribute( NAME ) );

		// Properties

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "id", property.getAttribute( NAME ) );
		assertEquals( TRUE, property.getAttribute( HIDDEN ) );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "fullname", property.getAttribute( NAME ) );
		assertEquals( String.class.getName(), property.getAttribute( TYPE ) );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "title", property.getAttribute( NAME ) );
		assertEquals( String.class.getName(), property.getAttribute( TYPE ) );
		assertEquals( "Mr, Mrs, Miss, Dr, Cpt", property.getAttribute( LOOKUP ) );
	}

	public void testCombineInspectionResult() {

		// Set up

		Inspector inspectorNull = new Inspector() {

			public String inspect( Object toInspect, String type, String... names ) {

				return null;
			}
		};

		Inspector inspectorEmpty = new Inspector() {

			public String inspect( Object toInspect, String type, String... names ) {

				return "<inspection-result/>";
			}
		};

		StringBuilder sampleXml = new StringBuilder( "<?xml version=\"1.0\"?>" );
		sampleXml.append( "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspection-result http://metawidget.org/xsd/inspection-result-1.0.xsd\" version=\"1.0\">" );
		sampleXml.append( "<entity type=\"foo\"/>" );
		sampleXml.append( "</inspection-result>" );
		final String sampleXml1 = sampleXml.toString();

		Inspector inspector1 = new Inspector() {

			public String inspect( Object toInspect, String type, String... names ) {

				return sampleXml1;
			}
		};

		sampleXml = new StringBuilder( "<?xml version=\"1.0\"?>" );
		sampleXml.append( "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspection-result http://metawidget.org/xsd/inspection-result-1.0.xsd\" version=\"1.0\">" );
		sampleXml.append( "<entity type=\"bar\">" );
		sampleXml.append( "<property name=\"abc\"/>" );
		sampleXml.append( "</entity>" );
		sampleXml.append( "</inspection-result>" );
		final String sampleXml2 = sampleXml.toString();

		Inspector inspector2 = new Inspector() {

			public String inspect( Object toInspect, String type, String... names ) {

				return sampleXml2;
			}
		};

		sampleXml = new StringBuilder( "<?xml version=\"1.0\"?>" );
		sampleXml.append( "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://metawidget.org/inspection-result http://metawidget.org/xsd/inspection-result-1.0.xsd\" version=\"1.0\">" );
		sampleXml.append( "<entity type=\"bar\">" );
		sampleXml.append( "<property name=\"def\"/>" );
		sampleXml.append( "</entity>" );
		sampleXml.append( "</inspection-result>" );
		final String sampleXml3 = sampleXml.toString();

		Inspector inspector3 = new Inspector() {

			public String inspect( Object toInspect, String type, String... names ) {

				return sampleXml3;
			}
		};

		// Test

		CompositeInspector compositeInspector = new CompositeInspector( new CompositeInspectorConfig().setInspectors( inspectorNull ) );
		assertEquals( null, compositeInspector.inspect( "Foo", "bar" ) );
		assertEquals( "No inspectors matched path == bar", LogUtilsTest.getLastWarnMessage() );
		LogUtilsTest.clearLastWarnMessage();

		compositeInspector = new CompositeInspector( new CompositeInspectorConfig().setInspectors( inspectorNull ) );
		assertEquals( null, compositeInspector.inspect( null, "bar" ) );
		assertEquals( null, LogUtilsTest.getLastWarnMessage() );

		compositeInspector = new CompositeInspector( new CompositeInspectorConfig().setInspectors( inspectorEmpty ) );
		Document document = XmlUtils.documentFromString( compositeInspector.inspect( "Foo", "bar" ) );
		assertEquals( null, document.getFirstChild().getFirstChild() );
		assertEquals( "No inspectors matched path == bar", LogUtilsTest.getLastWarnMessage() );
		LogUtilsTest.clearLastWarnMessage();

		compositeInspector = new CompositeInspector( new CompositeInspectorConfig().setInspectors( inspectorEmpty ) );
		document = XmlUtils.documentFromString( compositeInspector.inspect( null, "bar" ) );
		assertEquals( null, document.getFirstChild().getFirstChild() );
		assertEquals( null, LogUtilsTest.getLastWarnMessage() );

		compositeInspector = new CompositeInspector( new CompositeInspectorConfig().setInspectors( inspector1 ) );
		document = XmlUtils.documentFromString( compositeInspector.inspect( null, "bar" ) );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );
		Element entity = (Element) document.getFirstChild().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "foo", entity.getAttribute( TYPE ) );
		assertEquals( null, XmlUtils.getFirstChildElement( entity ) );

		compositeInspector = new CompositeInspector( new CompositeInspectorConfig().setInspectors( inspector1, inspector2 ) );
		document = XmlUtils.documentFromString( compositeInspector.inspect( null, "bar" ) );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );
		entity = (Element) document.getFirstChild().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "foo", entity.getAttribute( TYPE ) );
		assertEquals( null, XmlUtils.getFirstChildElement( entity ) );

		compositeInspector = new CompositeInspector( new CompositeInspectorConfig().setInspectors( inspector2, inspector1 ) );
		document = XmlUtils.documentFromString( compositeInspector.inspect( null, "baz" ) );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );
		entity = (Element) document.getFirstChild().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "bar", entity.getAttribute( TYPE ) );
		Element property = XmlUtils.getFirstChildElement( entity );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "abc", property.getAttribute( NAME ) );
		assertEquals( null, property.getNextSibling() );

		compositeInspector = new CompositeInspector( new CompositeInspectorConfig().setInspectors( inspector1, inspector2, inspector3 ) );
		document = XmlUtils.documentFromString( compositeInspector.inspect( null, "baz" ) );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );
		entity = (Element) document.getFirstChild().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "foo", entity.getAttribute( TYPE ) );
		assertEquals( null, XmlUtils.getFirstChildElement( entity ) );

		compositeInspector = new CompositeInspector( new CompositeInspectorConfig().setInspectors( inspector2, inspector1, inspector3 ) );
		document = XmlUtils.documentFromString( compositeInspector.inspect( null, "baz" ) );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );
		entity = (Element) document.getFirstChild().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "bar", entity.getAttribute( TYPE ) );
		property = XmlUtils.getFirstChildElement( entity );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "abc", property.getAttribute( NAME ) );
		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "def", property.getAttribute( NAME ) );
		assertEquals( null, property.getNextSibling() );
	}

	//
	// Inner class
	//

	static class Foo$EnhancerByCGLIB$$1234
		extends Foo {

		// Just a proxy test
	}

	static class Foo {

		public String	id;

		public String	fullname;

		public String	title;
	}
}
