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

package org.metawidget.inspector.jpa;

import static org.metawidget.inspector.InspectionResultConstants.*;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.persistence.Version;

import junit.framework.TestCase;

import org.metawidget.util.TestUtils;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Richard Kennard
 */

public class JpaInspectorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testInspection() {

		JpaInspector inspector = new JpaInspector();
		Document document = XmlUtils.documentFromString( inspector.inspect( new Foo(), Foo.class.getName() ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getFirstChild().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( Foo.class.getName(), entity.getAttribute( TYPE ) );
		assertFalse( entity.hasAttribute( NAME ) );

		// Properties

		Element property = XmlUtils.getChildWithAttributeValue( entity, NAME, "id" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( TRUE, property.getAttribute( HIDDEN ) );
		assertFalse( property.hasAttribute( MAXIMUM_LENGTH ) );
		assertTrue( property.getAttributes().getLength() == 2 );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "bar" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( TRUE, property.getAttribute( REQUIRED ) );
		assertEquals( "10", property.getAttribute( MAXIMUM_LENGTH ) );
		assertTrue( property.getAttributes().getLength() == 3 );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "bar1" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "20", property.getAttribute( MAXIMUM_LENGTH ) );
		assertTrue( property.getAttributes().getLength() == 2 );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "baz" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( TRUE, property.getAttribute( LARGE ) );
		assertEquals( TRUE, property.getAttribute( REQUIRED ) );
		assertTrue( property.getAttributes().getLength() == 3 );
	}

	public void testHideIds() {

		// Show ids

		JpaInspectorConfig config = new JpaInspectorConfig();
		config.setHideIds( false );
		JpaInspector inspector = new JpaInspector( config );
		Document document = XmlUtils.documentFromString( inspector.inspect( new Foo(), Foo.class.getName() ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );
		Element entity = (Element) document.getFirstChild().getFirstChild();

		assertEquals( XmlUtils.getChildWithAttributeValue( entity, NAME, "id" ), null );

		// Hidden by default

		config = new JpaInspectorConfig();
		inspector = new JpaInspector( config );
		document = XmlUtils.documentFromString( inspector.inspect( new Foo(), Foo.class.getName() ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );
		entity = (Element) document.getFirstChild().getFirstChild();

		Element property = XmlUtils.getChildWithAttributeValue( entity, NAME, "id" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( TRUE, property.getAttribute( HIDDEN ) );
		assertTrue( property.getAttributes().getLength() == 2 );
	}

	public void testHideVersions() {

		// Show versions

		JpaInspectorConfig config = new JpaInspectorConfig();
		config.setHideVersions( false );
		JpaInspector inspector = new JpaInspector( config );
		Document document = XmlUtils.documentFromString( inspector.inspect( new Foo(), Foo.class.getName() ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );
		Element entity = (Element) document.getFirstChild().getFirstChild();

		assertEquals( XmlUtils.getChildWithAttributeValue( entity, NAME, "version" ), null );

		// Hidden by default

		config = new JpaInspectorConfig();
		inspector = new JpaInspector( config );
		document = XmlUtils.documentFromString( inspector.inspect( new Foo(), Foo.class.getName() ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );
		entity = (Element) document.getFirstChild().getFirstChild();

		Element property = XmlUtils.getChildWithAttributeValue( entity, NAME, "version" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( TRUE, property.getAttribute( HIDDEN ) );
		assertTrue( property.getAttributes().getLength() == 2 );
	}

	public void testHideTransients() {

		// Hide transient

		JpaInspectorConfig config = new JpaInspectorConfig();
		config.setHideTransients( true );
		JpaInspector inspector = new JpaInspector( config );
		Document document = XmlUtils.documentFromString( inspector.inspect( new Foo(), Foo.class.getName() ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );
		Element entity = (Element) document.getFirstChild().getFirstChild();

		Element property = XmlUtils.getChildWithAttributeValue( entity, NAME, "transient1" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( TRUE, property.getAttribute( HIDDEN ) );
		assertTrue( property.getAttributes().getLength() == 2 );

		// Shown by default

		config = new JpaInspectorConfig();
		inspector = new JpaInspector( config );
		document = XmlUtils.documentFromString( inspector.inspect( new Foo(), Foo.class.getName() ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );
		entity = (Element) document.getFirstChild().getFirstChild();

		assertEquals( XmlUtils.getChildWithAttributeValue( entity, NAME, "transient1" ), null );
	}

	public void testConfig() {

		TestUtils.testEqualsAndHashcode( JpaInspectorConfig.class, new JpaInspectorConfig() {
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
	}
}
