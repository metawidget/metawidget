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

package org.metawidget.test.inspector.jpa;

import static org.metawidget.inspector.InspectionResultConstants.*;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import junit.framework.TestCase;

import org.metawidget.inspector.jpa.JpaInspector;
import org.metawidget.inspector.jpa.JpaInspectorConfig;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Richard Kennard
 */

public class JpaInspectorTest
	extends TestCase
{
	//
	// Constructor
	//

	/**
	 * JUnit 3.7 constructor.
	 */

	public JpaInspectorTest( String name )
	{
		super( name );
	}

	//
	// Public methods
	//

	public void testInspection()
	{
		JpaInspector inspector = new JpaInspector();
		Document document = XmlUtils.documentFromString( inspector.inspect( new Foo(), Foo.class.getName() ));

		assertTrue( "inspection-result".equals( document.getFirstChild().getNodeName() ) );

		// Entity

		Element entity = (Element) document.getFirstChild().getFirstChild();
		assertTrue( ENTITY.equals( entity.getNodeName() ) );
		assertTrue( Foo.class.getName().equals( entity.getAttribute( TYPE ) ) );
		assertTrue( !entity.hasAttribute( NAME ) );

		// Properties

		Element property = XmlUtils.getChildWithAttributeValue( entity, NAME, "id" );
		assertTrue( PROPERTY.equals( property.getNodeName() ) );
		assertTrue( TRUE.equals( property.getAttribute( HIDDEN ) ) );
		assertTrue( property.getAttributes().getLength() == 2 );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "bar" );
		assertTrue( PROPERTY.equals( property.getNodeName() ) );
		assertTrue( TRUE.equals( property.getAttribute( REQUIRED ) ) );
		assertTrue( "10".equals( property.getAttribute( MAXIMUM_LENGTH ) ) );
		assertTrue( property.getAttributes().getLength() == 3 );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "bar1" );
		assertTrue( PROPERTY.equals( property.getNodeName() ) );
		assertTrue( "20".equals( property.getAttribute( MAXIMUM_LENGTH ) ) );
		assertTrue( property.getAttributes().getLength() == 2 );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "baz" );
		assertTrue( PROPERTY.equals( property.getNodeName() ) );
		assertTrue( TRUE.equals( property.getAttribute( LARGE ) ) );
		assertTrue( TRUE.equals( property.getAttribute( REQUIRED ) ) );
		assertTrue( property.getAttributes().getLength() == 3 );
	}

	public void testHideIds()
	{
		JpaInspectorConfig config = new JpaInspectorConfig().setHideIds( false );
		JpaInspector inspector = new JpaInspector( config );
		Document document = XmlUtils.documentFromString( inspector.inspect( new Foo(), Foo.class.getName() ));

		assertTrue( "inspection-result".equals( document.getFirstChild().getNodeName() ) );
		Element entity = (Element) document.getFirstChild().getFirstChild();

		assertTrue( XmlUtils.getChildWithAttributeValue( entity, NAME, "id" ) == null );

		// Show id

		config = new JpaInspectorConfig();
		config.setHideIds( true );
		inspector = new JpaInspector( config );
		document = XmlUtils.documentFromString( inspector.inspect( new Foo(), Foo.class.getName() ));

		assertTrue( "inspection-result".equals( document.getFirstChild().getNodeName() ) );
		entity = (Element) document.getFirstChild().getFirstChild();

		Element property = XmlUtils.getChildWithAttributeValue( entity, NAME, "id" );
		assertTrue( PROPERTY.equals( property.getNodeName() ) );
		assertTrue( TRUE.equals( property.getAttribute( HIDDEN ) ) );
		assertTrue( property.getAttributes().getLength() == 2 );
	}

	//
	// Inner class
	//

	public static class Foo
	{
		@Id
		public String id;

		@Column( nullable = false, length = 10 )
		public String	bar;

		@Column( length = 20 )
		public String	bar1;

		@Lob
		@ManyToOne( optional = false )
		public String	baz;
	}
}
