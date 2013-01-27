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

package org.metawidget.inspector.hibernate;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.io.ByteArrayInputStream;

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

public class HibernateInspectorTest
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

		mInspector = new HibernateInspector( new HibernateInspectorConfig().setInputStream( new SimpleResourceResolver().openResource( "org/metawidget/inspector/hibernate/test-hibernate.cfg.xml" ) ) );
	}

	@SuppressWarnings( "unused" )
	public void testMissingFile() {

		try {
			new HibernateInspector( new HibernateInspectorConfig() );
			fail();
		} catch ( InspectorException e ) {
			assertEquals( "java.io.FileNotFoundException: Unable to locate hibernate.cfg.xml on CLASSPATH", e.getMessage() );
		}

		try {
			new HibernateInspector( new HibernateInspectorConfig().setInputStream( new ByteArrayInputStream( "<foo></foo>".getBytes() ) ) );
			fail();
		} catch ( InspectorException e ) {
			assertEquals( "Expected an XML document starting with 'hibernate-configuration' or 'hibernate-mapping', but got 'foo'", e.getMessage() );
		}
	}

	public void testProperties() {

		Document document = XmlUtils.documentFromString( mInspector.inspect( null, "org.metawidget.inspector.hibernate.SubFoo" ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertFalse( entity.hasAttribute( NAME ) );
		assertEquals( "org.metawidget.inspector.hibernate.SubFoo", entity.getAttribute( TYPE ) );

		// Properties

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "baz", property.getAttribute( NAME ) );
		assertEquals( TRUE, property.getAttribute( HIDDEN ) );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "bar", property.getAttribute( NAME ) );
		assertEquals( TRUE, property.getAttribute( REQUIRED ) );
		assertEquals( "org.metawidget.inspector.hibernate.Bar", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 3 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "abc", property.getAttribute( NAME ) );
		assertEquals( property.getAttributes().getLength(), 1 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "def", property.getAttribute( NAME ) );
		assertEquals( "org.metawidget.inspector.hibernate.Baz", property.getAttribute( PARAMETERIZED_TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "ghi", property.getAttribute( NAME ) );
		assertEquals( "org.metawidget.inspector.hibernate.Baz", property.getAttribute( PARAMETERIZED_TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "jkl", property.getAttribute( NAME ) );
		assertEquals( "org.metawidget.inspector.hibernate.Baz", property.getAttribute( PARAMETERIZED_TYPE ) );
		assertEquals( "subFoo", property.getAttribute( INVERSE_RELATIONSHIP ) );
		assertEquals( property.getAttributes().getLength(), 3 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "mno", property.getAttribute( NAME ) );
		assertEquals( "subFoo", property.getAttribute( INVERSE_RELATIONSHIP ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		assertEquals( property.getNextSibling(), null );
	}

	public void testHideIds() {

		mInspector = new HibernateInspector( new HibernateInspectorConfig().setHideIds( false ).setInputStream( new SimpleResourceResolver().openResource( "org/metawidget/inspector/hibernate/test-hibernate.cfg.xml" ) ) );

		Document document = XmlUtils.documentFromString( mInspector.inspect( null, "org.metawidget.inspector.hibernate.SubFoo" ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getDocumentElement().getFirstChild();

		// Properties

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "baz", property.getAttribute( NAME ) );
		assertFalse( property.hasAttribute( HIDDEN ) );
	}

	public void testTraverseParent() {

		Document document = XmlUtils.documentFromString( mInspector.inspect( null, "org.metawidget.inspector.hibernate.SubFoo", "bar" ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "bar", entity.getAttribute( NAME ) );
		assertEquals( "org.metawidget.inspector.hibernate.Bar", entity.getAttribute( TYPE ) );
		assertEquals( TRUE, entity.getAttribute( REQUIRED ) );

		// Properties

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "id", property.getAttribute( NAME ) );
		assertEquals( TRUE, property.getAttribute( HIDDEN ) );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "baz", property.getAttribute( NAME ) );
		assertEquals( TRUE, property.getAttribute( LARGE ) );
		assertEquals( "15", property.getAttribute( MAXIMUM_LENGTH ) );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "bazWithColumn", property.getAttribute( NAME ) );
		assertEquals( TRUE, property.getAttribute( REQUIRED ) );
		assertEquals( "20", property.getAttribute( MAXIMUM_LENGTH ) );
		assertEquals( TRUE, property.getAttribute( LARGE ) );
		assertEquals( String.class.getName(), property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 5 );

		assertEquals( property.getNextSibling(), null );
	}

	public void testConfig() {

		MetawidgetTestUtils.testEqualsAndHashcode( HibernateInspectorConfig.class, new HibernateInspectorConfig() {
			// Subclass
		}, "inputStreams" );
	}
}
