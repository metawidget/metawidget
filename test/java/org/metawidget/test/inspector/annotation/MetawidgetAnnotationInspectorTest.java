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

package org.metawidget.test.inspector.annotation;

import static org.metawidget.inspector.InspectionResultConstants.*;
import junit.framework.TestCase;

import org.metawidget.example.shared.addressbook.model.Address;
import org.metawidget.inspector.annotation.MetawidgetAnnotationInspector;
import org.metawidget.inspector.annotation.UiAttribute;
import org.metawidget.inspector.annotation.UiAttributes;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiDontExpand;
import org.metawidget.inspector.annotation.UiHidden;
import org.metawidget.inspector.annotation.UiLabel;
import org.metawidget.inspector.annotation.UiLarge;
import org.metawidget.inspector.annotation.UiLookup;
import org.metawidget.inspector.annotation.UiMasked;
import org.metawidget.inspector.annotation.UiReadOnly;
import org.metawidget.inspector.annotation.UiSection;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Richard Kennard
 */

public class MetawidgetAnnotationInspectorTest
	extends TestCase
{
	//
	//
	// Public methods
	//
	//

	public void testInspection()
	{
		MetawidgetAnnotationInspector inspector = new MetawidgetAnnotationInspector();
		Document document = XmlUtils.documentFromString( inspector.inspect( new Address(), Address.class.getName() ));

		assertTrue( "inspection-result".equals( document.getFirstChild().getNodeName() ));

		// Example Entity

		Element entity = (Element) document.getFirstChild().getFirstChild();
		assertTrue( ENTITY.equals( entity.getNodeName() ));
		assertTrue( Address.class.getName().equals( entity.getAttribute( TYPE ) ));
		assertTrue( !entity.hasAttribute( NAME ) );

		// Properties

		Element property = (Element) entity.getFirstChild();
		assertTrue( PROPERTY.equals( property.getNodeName() ));
		assertTrue( "owner".equals( property.getAttribute( NAME ) ));
		assertTrue( TRUE.equals( property.getAttribute( HIDDEN ) ));

		property = (Element) property.getNextSibling();
		assertTrue( PROPERTY.equals( property.getNodeName() ));
		assertTrue( "street".equals( property.getAttribute( NAME ) ));

		property = (Element) property.getNextSibling();
		assertTrue( PROPERTY.equals( property.getNodeName() ));
		assertTrue( "city".equals( property.getAttribute( NAME ) ));

		property = (Element) property.getNextSibling();
		assertTrue( PROPERTY.equals( property.getNodeName() ));
		assertTrue( "state".equals( property.getAttribute( NAME ) ));
		assertTrue( "Anytown,Cyberton,Lostville,Whereverton".equals( property.getAttribute( LOOKUP ) ));

		// Made-up Entity

		document = XmlUtils.documentFromString( inspector.inspect( new Foo(), Foo.class.getName() ));
		assertTrue( "inspection-result".equals( document.getFirstChild().getNodeName() ));
		entity = (Element) document.getFirstChild().getFirstChild();
		assertTrue( ENTITY.equals( entity.getNodeName() ));
		assertTrue( Foo.class.getName().equals( entity.getAttribute( TYPE ) ));

		property = (Element) entity.getFirstChild().getNextSibling();
		assertTrue( "string1".equals( property.getAttribute( NAME ) ));
		assertTrue( "bar".equals( property.getAttribute( LABEL ) ));
		assertTrue( "bar1".equals( property.getAttribute( "foo1" ) ));
		assertTrue( "bar2".equals( property.getAttribute( "foo2" ) ));
		assertTrue( TRUE.equals( property.getAttribute( READ_ONLY ) ));
		assertTrue( TRUE.equals( property.getAttribute( HIDDEN ) ));
		assertTrue( "Foo".equals( property.getAttribute( SECTION ) ));
		assertTrue( TRUE.equals( property.getAttribute( MASKED ) ));
	}

	public void testLookup()
	{
		MetawidgetAnnotationInspector inspector = new MetawidgetAnnotationInspector();
		Document document = XmlUtils.documentFromString( inspector.inspect( new Foo(), Foo.class.getName() ));

		assertTrue( "inspection-result".equals( document.getFirstChild().getNodeName() ));

		// Entity

		Element entity = (Element) document.getFirstChild().getFirstChild();
		assertTrue( ENTITY.equals( entity.getNodeName() ));
		assertTrue( Foo.class.getName().equals( entity.getAttribute( TYPE ) ));
		assertTrue( !entity.hasAttribute( NAME ) );

		// Properties

		Element property = (Element) entity.getFirstChild();
		assertTrue( PROPERTY.equals( property.getNodeName() ));
		assertTrue( "object1".equals( property.getAttribute( NAME ) ));
		assertTrue( "foo\\,,bar".equals( property.getAttribute( LOOKUP ) ));

		property = (Element) property.getNextSibling();
		assertTrue( "string1".equals( property.getAttribute( NAME ) ));
		assertTrue( "bar".equals( property.getAttribute( LABEL ) ));
		assertTrue( "bar1".equals( property.getAttribute( "foo1" ) ));
		assertTrue( "bar2".equals( property.getAttribute( "foo2" ) ));
		assertTrue( TRUE.equals( property.getAttribute( HIDDEN ) ));
		assertTrue( TRUE.equals( property.getAttribute( READ_ONLY ) ));
		assertTrue( TRUE.equals( property.getAttribute( DONT_EXPAND ) ));
		assertTrue( "Foo".equals( property.getAttribute( SECTION ) ));
		assertTrue( TRUE.equals( property.getAttribute( MASKED ) ));
		assertTrue( TRUE.equals( property.getAttribute( LARGE ) ));
	}

	public void testInfiniteLoop()
	{
		try
		{
			MetawidgetAnnotationInspector inspector = new MetawidgetAnnotationInspector();
			inspector.inspect( new InfiniteFoo(), InfiniteFoo.class.getName() );
			assertTrue( false );
		}
		catch( InspectorException e )
		{
			assertTrue( "Infinite loop detected when sorting @UiComesAfter".equals( e.getMessage() ));
		}
	}

	//
	//
	// Constructor
	//
	//

	/**
	 * JUnit 3.7 constructor.
	 */

	public MetawidgetAnnotationInspectorTest( String name )
	{
		super( name );
	}

	//
	//
	// Inner class
	//
	//

	public static class Foo
	{
		@UiLookup( value = { "foo,", "bar" } )
		public Object	object1;

		@UiLabel( "bar" )
		@UiAttributes( { @UiAttribute( name = "foo1", value = "bar1" ), @UiAttribute( name="foo2", value="bar2" ) })
		@UiHidden
		@UiReadOnly
		@UiDontExpand
		@UiSection( "Foo" )
		@UiMasked
		@UiComesAfter( "object1" )
		@UiLarge
		public String	string1;
	}

	public static class InfiniteFoo
	{
		@UiComesAfter( "object2" )
		public Object object1;

		@UiComesAfter( "object1" )
		public Object object2;
	}
}
