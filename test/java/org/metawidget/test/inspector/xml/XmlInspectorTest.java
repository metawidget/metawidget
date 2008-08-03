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

package org.metawidget.test.inspector.xml;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.io.ByteArrayInputStream;

import junit.framework.TestCase;

import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.xml.XmlInspector;
import org.metawidget.inspector.xml.XmlInspectorConfig;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Richard Kennard
 */

public class XmlInspectorTest
	extends TestCase
{
	//
	//
	// Private members
	//
	//

	private XmlInspector	mInspector;

	//
	//
	// Public methods
	//
	//

	@Override
	public void setUp()
	{
		String xml = "<?xml version=\"1.0\"?>";
		xml += "<inspection-result xmlns=\"http://www.metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.metawidget.org/inspection-result ../../inspector/inspection-result-1.0.xsd\">";
		xml += "<entity type=\"org.metawidget.test.inspector.xml.XmlInspectorTest$SuperSuperFoo\">";
		xml += "<property name=\"bar\" type=\"Bar\" required=\"true\"/>";
		xml += "<property name=\"a\"/>";
		xml += "<property name=\"d\"/>";
		xml += "</entity>";
		xml += "<entity type=\"org.metawidget.test.inspector.xml.XmlInspectorTest$SuperFoo\" extends=\"org.metawidget.test.inspector.xml.XmlInspectorTest$SuperSuperFoo\"/>";
		xml += "<entity type=\"org.metawidget.test.inspector.xml.XmlInspectorTest$SubFoo\" extends=\"org.metawidget.test.inspector.xml.XmlInspectorTest$SuperFoo\">";
		xml += "<property name=\"a\" hidden=\"true\" label=\" \"/>";
		xml += "<property name=\"b\" label=\"\"/>";
		xml += "<property name=\"c\" lookup=\"Telephone, Mobile, Fax, E-mail\"/>";
		xml += "</entity>";
		xml += "<entity type=\"Bar\">";
		xml += "<property name=\"baz\"/>";
		xml += "</entity></inspection-result>";

		XmlInspectorConfig config = new XmlInspectorConfig();
		config.setInputStream( new ByteArrayInputStream( xml.getBytes() ) );
		config.setSchema( "" );
		mInspector = new XmlInspector( config );
	}

	public void testInspection()
	{
		Document document = XmlUtils.documentFromString( mInspector.inspect( null, "org.metawidget.test.inspector.xml.XmlInspectorTest$SubFoo" ));

		assertTrue( "inspection-result".equals( document.getFirstChild().getNodeName() ));

		// Entity

		Element entity = (Element) document.getFirstChild().getFirstChild();
		assertTrue( ENTITY.equals( entity.getNodeName() ));
		assertTrue( "org.metawidget.test.inspector.xml.XmlInspectorTest$SubFoo".equals( entity.getAttribute( TYPE ) ));
		assertTrue( !entity.hasAttribute( NAME ) );

		// Properties

		Element property = (Element) entity.getFirstChild();
		assertTrue( PROPERTY.equals( property.getNodeName() ));
		assertTrue( "bar".equals( property.getAttribute( NAME ) ));
		assertTrue( "Bar".equals( property.getAttribute( TYPE ) ));

		property = (Element) property.getNextSibling();
		assertTrue( PROPERTY.equals( property.getNodeName() ));
		assertTrue( "a".equals( property.getAttribute( NAME ) ));
		assertTrue( TRUE.equals( property.getAttribute( HIDDEN ) ));
		assertTrue( " ".equals( property.getAttribute( LABEL ) ));
		assertTrue( property.getAttributes().getLength() == 3 );

		property = (Element) property.getNextSibling();
		assertTrue( PROPERTY.equals( property.getNodeName() ));
		assertTrue( "b".equals( property.getAttribute( NAME ) ));
		assertTrue( property.hasAttribute( LABEL ) );
		assertTrue( property.getAttributes().getLength() == 2 );

		property = (Element) property.getNextSibling();
		assertTrue( PROPERTY.equals( property.getNodeName() ));
		assertTrue( "c".equals( property.getAttribute( NAME ) ));
		assertTrue( !property.hasAttribute( LABEL ) );
		assertTrue( "Telephone, Mobile, Fax, E-mail".equals( property.getAttribute( LOOKUP ) ));
		assertTrue( property.getAttributes().getLength() == 2 );
	}

	public void testTraverseViaParent()
	{
		Document document = XmlUtils.documentFromString( mInspector.inspect( null, "org.metawidget.test.inspector.xml.XmlInspectorTest$SubFoo", "bar" ));

		assertTrue( "inspection-result".equals( document.getFirstChild().getNodeName() ));

		Element entity = (Element) document.getFirstChild().getFirstChild();
		assertTrue( ENTITY.equals( entity.getNodeName() ));
		assertTrue( "Bar".equals( entity.getAttribute( TYPE ) ));
		assertTrue( "bar".equals( entity.getAttribute( NAME ) ));
		assertTrue( "true".equals( entity.getAttribute( REQUIRED ) ));

		// Properties

		Element property = (Element) entity.getFirstChild();
		assertTrue( PROPERTY.equals( property.getNodeName() ));
		assertTrue( "baz".equals( property.getAttribute( NAME ) ));
	}

	public void testMissingType()
	{
		assertTrue( null == mInspector.inspect( null, "org.metawidget.test.inspector.xml.XmlInspectorTest$SubFoo", "bar", "baz" ));

		try
		{
			mInspector.inspect( null, "org.metawidget.test.inspector.xml.XmlInspectorTest$SubFoo", "bar", "baz", "abc" );
			assertTrue( false );
		}
		catch( InspectorException e )
		{
			assertTrue( e.getMessage().endsWith( "Property baz of bar.baz.abc has no @type" ));
		}
	}

	public void testNullType()
	{
		assertTrue( null == mInspector.inspect( null, null ));
	}

	public void testBadName()
	{
		assertTrue( mInspector.inspect( null, "no-such-type" ) == null );
		assertTrue( mInspector.inspect( null, "org.metawidget.test.inspector.xml.XmlInspectorTest$SubFoo", "no-such-name" ) == null );
		assertTrue( mInspector.inspect( null, "org.metawidget.test.inspector.xml.XmlInspectorTest$SubFoo", "no-such-parent-name", "foo" ) == null );
	}

	//
	//
	// Constructor
	//
	//

	/**
	 * JUnit 3.7 constructor.
	 */

	public XmlInspectorTest( String name )
	{
		super( name );
	}
}
