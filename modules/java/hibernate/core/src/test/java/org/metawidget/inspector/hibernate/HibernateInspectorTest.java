// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

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
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
