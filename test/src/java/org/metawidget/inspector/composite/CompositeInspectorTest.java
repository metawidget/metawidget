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

import org.metawidget.example.shared.addressbook.model.PersonalContact;
import org.metawidget.inspector.annotation.MetawidgetAnnotationInspector;
import org.metawidget.inspector.iface.Inspector;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.inspector.xml.XmlInspector;
import org.metawidget.inspector.xml.XmlInspectorConfig;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.TestUtils;
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
		xml += "<entity type=\"org.metawidget.example.shared.addressbook.model.PersonalContact\">";
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

		XmlInspectorConfig configXml = new XmlInspectorConfig();
		configXml.setInputStream( new ByteArrayInputStream( xml.getBytes() ) );

		XmlInspector inspectorXml = new XmlInspector( configXml );

		ValidatingCompositeInspectorConfig config = new ValidatingCompositeInspectorConfig();
		config.setInspectors( inspectorXml, new PropertyTypeInspector() );

		ValidatingCompositeInspector inspector = new ValidatingCompositeInspector( config );

		// Inspect

		PersonalContact toInspect = new PersonalContact$EnhancerByCGLIB$$1234();
		xml = inspector.inspect( toInspect, ClassUtils.getUnproxiedClass( toInspect.getClass() ).getName() );
		Document document = XmlUtils.documentFromString( xml );

		// Test

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getFirstChild().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( PersonalContact.class.getName(), entity.getAttribute( TYPE ) );
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
			new CompositeInspector( new CompositeInspectorConfig().setInspectors( inspector, new MetawidgetAnnotationInspector(), inspector ) );
			assertTrue( false );
		} catch ( InspectorException e ) {
			assertEquals( "CompositeInspector's list of Inspectors contains two of the same org.metawidget.inspector.propertytype.PropertyTypeInspector", e.getMessage() );
		}
	}

	public void testConfig() {

		TestUtils.testEqualsAndHashcode( CompositeInspectorConfig.class, new CompositeInspectorConfig() {
			// Subclass
		} );
	}

	//
	// Inner class
	//

	public static class PersonalContact$EnhancerByCGLIB$$1234
		extends PersonalContact {

		//
		//
		// Private statics
		//
		//

		private final static long	serialVersionUID	= 1l;
	}
}
