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

package org.metawidget.inspector.jpa;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import junit.framework.TestCase;

import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyle;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyleConfig;
import org.metawidget.util.MetawidgetTestUtils;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class JpaInspectorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testInspection() {

		JpaInspector inspector = new JpaInspector( new JpaInspectorConfig().setPropertyStyle( new JavaBeanPropertyStyle( new JavaBeanPropertyStyleConfig().setSupportPublicFields( true ) ) ) );
		Document document = XmlUtils.documentFromString( inspector.inspect( new Foo(), Foo.class.getName() ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( Foo.class.getName(), entity.getAttribute( TYPE ) );
		assertFalse( entity.hasAttribute( NAME ) );

		// Properties

		Element property = XmlUtils.getChildWithAttributeValue( entity, NAME, "id" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( TRUE, property.getAttribute( HIDDEN ) );
		assertFalse( property.hasAttribute( MAXIMUM_LENGTH ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "embeddedId" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( TRUE, property.getAttribute( HIDDEN ) );
		assertFalse( property.hasAttribute( MAXIMUM_LENGTH ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "bar" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( TRUE, property.getAttribute( REQUIRED ) );
		assertEquals( "10", property.getAttribute( MAXIMUM_LENGTH ) );
		assertEquals( property.getAttributes().getLength(), 3 );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "bar1" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "20", property.getAttribute( MAXIMUM_LENGTH ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "baz" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( TRUE, property.getAttribute( LARGE ) );
		assertEquals( TRUE, property.getAttribute( REQUIRED ) );
		assertEquals( property.getAttributes().getLength(), 3 );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "date" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "date", property.getAttribute( DATETIME_TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "time" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "time", property.getAttribute( DATETIME_TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "datetime" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "both", property.getAttribute( DATETIME_TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "oneToOne" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( TRUE, property.getAttribute( REQUIRED ) );
		assertEquals( "foo", property.getAttribute( INVERSE_RELATIONSHIP ) );
		assertEquals( property.getAttributes().getLength(), 3 );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "oneToMany" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "bar", property.getAttribute( INVERSE_RELATIONSHIP ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		assertEquals( entity.getChildNodes().getLength(), 11 );
	}

	public void testHideIds() {

		// Show ids

		JpaInspectorConfig config = new JpaInspectorConfig();
		config.setPropertyStyle( new JavaBeanPropertyStyle( new JavaBeanPropertyStyleConfig().setSupportPublicFields( true ) ) );
		config.setHideIds( false );
		JpaInspector inspector = new JpaInspector( config );
		Document document = XmlUtils.documentFromString( inspector.inspect( new Foo(), Foo.class.getName() ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );
		Element entity = (Element) document.getDocumentElement().getFirstChild();

		assertEquals( XmlUtils.getChildWithAttributeValue( entity, NAME, "id" ), null );
		assertEquals( XmlUtils.getChildWithAttributeValue( entity, NAME, "embeddedId" ), null );

		// Hidden by default

		config = new JpaInspectorConfig();
		config.setPropertyStyle( new JavaBeanPropertyStyle( new JavaBeanPropertyStyleConfig().setSupportPublicFields( true ) ) );
		inspector = new JpaInspector( config );
		document = XmlUtils.documentFromString( inspector.inspect( new Foo(), Foo.class.getName() ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );
		entity = (Element) document.getDocumentElement().getFirstChild();

		Element property = XmlUtils.getChildWithAttributeValue( entity, NAME, "id" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( TRUE, property.getAttribute( HIDDEN ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "embeddedId" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( TRUE, property.getAttribute( HIDDEN ) );
		assertEquals( property.getAttributes().getLength(), 2 );
	}

	public void testHideVersions() {

		// Show versions

		JpaInspectorConfig config = new JpaInspectorConfig();
		config.setPropertyStyle( new JavaBeanPropertyStyle( new JavaBeanPropertyStyleConfig().setSupportPublicFields( true ) ) );
		config.setHideVersions( false );
		JpaInspector inspector = new JpaInspector( config );
		Document document = XmlUtils.documentFromString( inspector.inspect( new Foo(), Foo.class.getName() ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );
		Element entity = (Element) document.getDocumentElement().getFirstChild();

		assertEquals( XmlUtils.getChildWithAttributeValue( entity, NAME, "version" ), null );

		// Hidden by default

		config = new JpaInspectorConfig();
		config.setPropertyStyle( new JavaBeanPropertyStyle( new JavaBeanPropertyStyleConfig().setSupportPublicFields( true ) ) );
		inspector = new JpaInspector( config );
		document = XmlUtils.documentFromString( inspector.inspect( new Foo(), Foo.class.getName() ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );
		entity = (Element) document.getDocumentElement().getFirstChild();

		Element property = XmlUtils.getChildWithAttributeValue( entity, NAME, "version" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( TRUE, property.getAttribute( HIDDEN ) );
		assertEquals( property.getAttributes().getLength(), 2 );
	}

	public void testHideTransients() {

		// Hide transient

		JpaInspectorConfig config = new JpaInspectorConfig();
		config.setPropertyStyle( new JavaBeanPropertyStyle( new JavaBeanPropertyStyleConfig().setSupportPublicFields( true ) ) );
		config.setHideTransients( true );
		JpaInspector inspector = new JpaInspector( config );
		Document document = XmlUtils.documentFromString( inspector.inspect( new Foo(), Foo.class.getName() ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );
		Element entity = (Element) document.getDocumentElement().getFirstChild();

		Element property = XmlUtils.getChildWithAttributeValue( entity, NAME, "transient1" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( TRUE, property.getAttribute( HIDDEN ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		// Shown by default

		config = new JpaInspectorConfig();
		config.setPropertyStyle( new JavaBeanPropertyStyle( new JavaBeanPropertyStyleConfig().setSupportPublicFields( true ) ) );
		inspector = new JpaInspector( config );
		document = XmlUtils.documentFromString( inspector.inspect( new Foo(), Foo.class.getName() ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );
		entity = (Element) document.getDocumentElement().getFirstChild();

		assertEquals( XmlUtils.getChildWithAttributeValue( entity, NAME, "transient1" ), null );
	}

	public void testConfig() {

		MetawidgetTestUtils.testEqualsAndHashcode( JpaInspectorConfig.class, new JpaInspectorConfig() {
			// Subclass
		} );
	}

	//
	// Inner class
	//

	public static class Foo {

		@Id
		@Column
		public String	id;

		@EmbeddedId
		public String	embeddedId;

		@Version
		public String	version;

		@Column( nullable = false, length = 10 )
		public String	bar;

		@Column( length = 20 )
		public String	bar1;

		@Lob
		@ManyToOne( optional = false )
		public String	baz;

		@Transient
		public String	transient1;

		@Temporal( TemporalType.DATE )
		public Date		date;

		@Temporal( TemporalType.TIME )
		public Date		time;

		@Temporal( TemporalType.TIMESTAMP )
		public Date		datetime;

		@OneToOne( optional = false, mappedBy = "foo" )
		public String	oneToOne;

		@OneToMany( mappedBy = "bar" )
		public String	oneToMany;
	}
}
