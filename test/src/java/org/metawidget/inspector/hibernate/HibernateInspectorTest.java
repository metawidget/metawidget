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

import org.metawidget.config.ConfigReader;
import org.metawidget.inspector.iface.Inspector;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Richard Kennard
 */

public class HibernateInspectorTest
	extends TestCase
{
	//
	// Private members
	//

	private Inspector	mInspector;

	//
	// Public methods
	//

	@Override
	public void setUp()
	{
		HibernateInspectorConfig config = new HibernateInspectorConfig();
		config.setInputStream( new ConfigReader().openResource( "org/metawidget/inspector/hibernate/test-hibernate.cfg.xml" ));
		mInspector = new HibernateInspector( config );
	}

	public void testMissingFile()
	{
		try
		{
			new HibernateInspector( new HibernateInspectorConfig() );
			assertTrue( false );
		}
		catch( InspectorException e )
		{
			assertTrue( "java.io.FileNotFoundException: Unable to locate hibernate.cfg.xml on CLASSPATH".equals( e.getMessage() ));
		}

		try
		{
			HibernateInspectorConfig config = new HibernateInspectorConfig();
			config.setInputStream( new ByteArrayInputStream( "<foo></foo>".getBytes() ) );
			new HibernateInspector( config );
			assertTrue( false );
		}
		catch( InspectorException e )
		{
			assertTrue( "Expected an XML document starting with 'hibernate-configuration' or 'hibernate-mapping', but got 'foo'".equals( e.getMessage() ));
		}
	}

	public void testProperties()
	{
		Document document = XmlUtils.documentFromString( mInspector.inspect( null, "org.metawidget.inspector.hibernate.SubFoo" ));

		assertTrue( "inspection-result".equals( document.getFirstChild().getNodeName() ) );

		// Entity

		Element entity = (Element) document.getFirstChild().getFirstChild();
		assertTrue( ENTITY.equals( entity.getNodeName() ) );
		assertTrue( !entity.hasAttribute( NAME ) );
		assertTrue( "org.metawidget.inspector.hibernate.SubFoo".equals( entity.getAttribute( TYPE ) ));

		// Properties

		Element property = (Element) entity.getFirstChild();
		assertTrue( PROPERTY.equals( property.getNodeName() ) );
		assertTrue( "baz".equals( property.getAttribute( NAME ) ) );
		assertTrue( TRUE.equals( property.getAttribute( HIDDEN ) ) );

		property = (Element) property.getNextSibling();
		assertTrue( PROPERTY.equals( property.getNodeName() ) );
		assertTrue( "bar".equals( property.getAttribute( NAME ) ) );
		assertTrue( TRUE.equals( property.getAttribute( REQUIRED ) ) );
		assertTrue( "org.metawidget.inspector.hibernate.Bar".equals( property.getAttribute( TYPE ) ) );
		assertTrue( property.getAttributes().getLength() == 3 );

		property = (Element) property.getNextSibling();
		assertTrue( PROPERTY.equals( property.getNodeName() ) );
		assertTrue( "abc".equals( property.getAttribute( NAME ) ) );
		assertTrue( property.getAttributes().getLength() == 1 );

		property = (Element) property.getNextSibling();
		assertTrue( PROPERTY.equals( property.getNodeName() ) );
		assertTrue( "def".equals( property.getAttribute( NAME ) ) );
		assertTrue( "org.metawidget.inspector.hibernate.Baz".equals( property.getAttribute( PARAMETERIZED_TYPE ) ) );
		assertTrue( property.getAttributes().getLength() == 2 );

		property = (Element) property.getNextSibling();
		assertTrue( PROPERTY.equals( property.getNodeName() ) );
		assertTrue( "ghi".equals( property.getAttribute( NAME ) ) );
		assertTrue( "org.metawidget.inspector.hibernate.Baz".equals( property.getAttribute( PARAMETERIZED_TYPE ) ) );
		assertTrue( property.getAttributes().getLength() == 2 );

		property = (Element) property.getNextSibling();
		assertTrue( PROPERTY.equals( property.getNodeName() ) );
		assertTrue( "jkl".equals( property.getAttribute( NAME ) ) );
		assertTrue( "org.metawidget.inspector.hibernate.Baz".equals( property.getAttribute( PARAMETERIZED_TYPE ) ) );
		assertTrue( property.getAttributes().getLength() == 2 );

		assertTrue( property.getNextSibling() == null );
	}

	public void testHideIds()
	{
		HibernateInspectorConfig config = new HibernateInspectorConfig().setHideIds( false );
		config.setInputStream( new ConfigReader().openResource( "org/metawidget/inspector/hibernate/test-hibernate.cfg.xml" ));
		mInspector = new HibernateInspector( config );

		Document document = XmlUtils.documentFromString( mInspector.inspect( null, "org.metawidget.inspector.hibernate.SubFoo" ));

		assertTrue( "inspection-result".equals( document.getFirstChild().getNodeName() ) );

		// Entity

		Element entity = (Element) document.getFirstChild().getFirstChild();

		// Properties

		Element property = (Element) entity.getFirstChild();
		assertTrue( PROPERTY.equals( property.getNodeName() ) );
		assertTrue( "baz".equals( property.getAttribute( NAME ) ) );
		assertTrue( !property.hasAttribute( HIDDEN ) );
	}

	public void testTraverseParent()
	{
		Document document = XmlUtils.documentFromString( mInspector.inspect( null, "org.metawidget.inspector.hibernate.SubFoo", "bar" ));

		assertTrue( "inspection-result".equals( document.getFirstChild().getNodeName() ) );

		// Entity

		Element entity = (Element) document.getFirstChild().getFirstChild();
		assertTrue( ENTITY.equals( entity.getNodeName() ) );
		assertTrue( "bar".equals( entity.getAttribute( NAME ) ));
		assertTrue( "org.metawidget.inspector.hibernate.Bar".equals( entity.getAttribute( TYPE ) ));
		assertTrue( TRUE.equals( entity.getAttribute( REQUIRED ) ) );

		// Properties

		Element property = (Element) entity.getFirstChild();
		assertTrue( PROPERTY.equals( property.getNodeName() ) );
		assertTrue( "id".equals( property.getAttribute( NAME ) ) );
		assertTrue( TRUE.equals( property.getAttribute( HIDDEN ) ) );

		property = (Element) property.getNextSibling();
		assertTrue( PROPERTY.equals( property.getNodeName() ) );
		assertTrue( "baz".equals( property.getAttribute( NAME ) ) );
		assertTrue( TRUE.equals( property.getAttribute( LARGE ) ) );
		assertTrue( "15".equals( property.getAttribute( MAXIMUM_LENGTH ) ) );
	}

	public void testConfig()
	{
		HibernateInspectorConfig config1 = new HibernateInspectorConfig();
		HibernateInspectorConfig config2 = new HibernateInspectorConfig();

		assertTrue( !config1.equals( "foo" ));
		assertTrue( config1.equals( config1 ) );
		assertTrue( config1.equals( config2 ));
		assertTrue( config1.hashCode() == config2.hashCode() );

		// hideIds

		config1.setHideIds( false );
		assertTrue( !config1.isHideIds() );
		assertTrue( !config1.equals( config2 ));

		config2.setHideIds( false );
		assertTrue( config1.equals( config2 ));
		assertTrue( config1.hashCode() == config2.hashCode() );
	}
}
