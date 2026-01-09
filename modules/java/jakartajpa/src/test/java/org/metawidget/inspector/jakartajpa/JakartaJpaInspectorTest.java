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

package org.metawidget.inspector.jakartajpa;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;

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

public class JakartaJpaInspectorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testInspection() {

		JakartaJpaInspector inspector = new JakartaJpaInspector( new JakartaJpaInspectorConfig().setPropertyStyle( new JavaBeanPropertyStyle( new JavaBeanPropertyStyleConfig().setSupportPublicFields( true ) ) ) );
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

		JakartaJpaInspectorConfig config = new JakartaJpaInspectorConfig();
		config.setPropertyStyle( new JavaBeanPropertyStyle( new JavaBeanPropertyStyleConfig().setSupportPublicFields( true ) ) );
		config.setHideIds( false );
		JakartaJpaInspector inspector = new JakartaJpaInspector( config );
		Document document = XmlUtils.documentFromString( inspector.inspect( new Foo(), Foo.class.getName() ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );
		Element entity = (Element) document.getDocumentElement().getFirstChild();

		assertEquals( XmlUtils.getChildWithAttributeValue( entity, NAME, "id" ), null );
		assertEquals( XmlUtils.getChildWithAttributeValue( entity, NAME, "embeddedId" ), null );

		// Hidden by default

		config = new JakartaJpaInspectorConfig();
		config.setPropertyStyle( new JavaBeanPropertyStyle( new JavaBeanPropertyStyleConfig().setSupportPublicFields( true ) ) );
		inspector = new JakartaJpaInspector( config );
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

		JakartaJpaInspectorConfig config = new JakartaJpaInspectorConfig();
		config.setPropertyStyle( new JavaBeanPropertyStyle( new JavaBeanPropertyStyleConfig().setSupportPublicFields( true ) ) );
		config.setHideVersions( false );
		JakartaJpaInspector inspector = new JakartaJpaInspector( config );
		Document document = XmlUtils.documentFromString( inspector.inspect( new Foo(), Foo.class.getName() ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );
		Element entity = (Element) document.getDocumentElement().getFirstChild();

		assertEquals( XmlUtils.getChildWithAttributeValue( entity, NAME, "version" ), null );

		// Hidden by default

		config = new JakartaJpaInspectorConfig();
		config.setPropertyStyle( new JavaBeanPropertyStyle( new JavaBeanPropertyStyleConfig().setSupportPublicFields( true ) ) );
		inspector = new JakartaJpaInspector( config );
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

		JakartaJpaInspectorConfig config = new JakartaJpaInspectorConfig();
		config.setPropertyStyle( new JavaBeanPropertyStyle( new JavaBeanPropertyStyleConfig().setSupportPublicFields( true ) ) );
		config.setHideTransients( true );
		JakartaJpaInspector inspector = new JakartaJpaInspector( config );
		Document document = XmlUtils.documentFromString( inspector.inspect( new Foo(), Foo.class.getName() ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );
		Element entity = (Element) document.getDocumentElement().getFirstChild();

		Element property = XmlUtils.getChildWithAttributeValue( entity, NAME, "transient1" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( TRUE, property.getAttribute( HIDDEN ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		// Shown by default

		config = new JakartaJpaInspectorConfig();
		config.setPropertyStyle( new JavaBeanPropertyStyle( new JavaBeanPropertyStyleConfig().setSupportPublicFields( true ) ) );
		inspector = new JakartaJpaInspector( config );
		document = XmlUtils.documentFromString( inspector.inspect( new Foo(), Foo.class.getName() ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );
		entity = (Element) document.getDocumentElement().getFirstChild();

		assertEquals( XmlUtils.getChildWithAttributeValue( entity, NAME, "transient1" ), null );
	}

	public void testConfig() {

		MetawidgetTestUtils.testEqualsAndHashcode( JakartaJpaInspectorConfig.class, new JakartaJpaInspectorConfig() {
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
