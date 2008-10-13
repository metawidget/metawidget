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

package org.metawidget.test.inspector.propertytype;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.propertytype.PropertyTypeInspectionResultConstants.*;

import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.util.Set;

import junit.framework.TestCase;

import org.metawidget.example.shared.addressbook.model.Address;
import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.example.shared.addressbook.model.PersonalContact;
import org.metawidget.inspector.iface.Inspector;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Richard Kennard
 */

public class PropertyTypeInspectorTest
	extends TestCase
{
	//
	// Private members
	//

	private Inspector	mInspector;

	//
	// Constructor
	//

	/**
	 * JUnit 3.7 constructor.
	 */

	public PropertyTypeInspectorTest( String name )
	{
		super( name );
	}

	//
	// Public methods
	//

	@Override
	public void setUp()
	{
		mInspector = new PropertyTypeInspector();
	}

	public void testInspection()
	{
		PersonalContact personalContact = new PersonalContact();
		Document document = XmlUtils.documentFromString( mInspector.inspect( personalContact, PersonalContact.class.getName() ) );

		assertTrue( "inspection-result".equals( document.getFirstChild().getNodeName() ) );

		// Entity

		Element entity = (Element) document.getFirstChild().getFirstChild();
		assertTrue( ENTITY.equals( entity.getNodeName() ) );
		assertTrue( PersonalContact.class.getName().equals( entity.getAttribute( TYPE ) ) );
		assertTrue( !entity.hasAttribute( NAME ) );

		// Properties (should be sorted alphabetically)

		Element property = (Element) entity.getFirstChild();
		assertTrue( PROPERTY.equals( property.getNodeName() ) );
		assertTrue( "address".equals( property.getAttribute( NAME ) ) );
		assertTrue( Address.class.getName().equals( property.getAttribute( TYPE ) ) );

		property = (Element) property.getNextSibling();
		assertTrue( PROPERTY.equals( property.getNodeName() ) );
		assertTrue( "communications".equals( property.getAttribute( NAME ) ) );
		assertTrue( Set.class.getName().equals( property.getAttribute( TYPE ) ) );

		// Test declared-class

		DeclaredTypeTester tester = new DeclaredTypeTester();
		tester.value = personalContact;

		document = XmlUtils.documentFromString( mInspector.inspect( tester, DeclaredTypeTester.class.getName() ) );

		assertTrue( "inspection-result".equals( document.getFirstChild().getNodeName() ) );

		entity = (Element) document.getFirstChild().getFirstChild();
		assertTrue( ENTITY.equals( entity.getNodeName() ) );
		assertTrue( DeclaredTypeTester.class.getName().equals( entity.getAttribute( TYPE ) ) );
		assertTrue( !entity.hasAttribute( NAME ) );

		property = (Element) entity.getFirstChild();
		assertTrue( PROPERTY.equals( property.getNodeName() ) );
		assertTrue( "fOO".equals( property.getAttribute( NAME ) ) );
		assertTrue( Object.class.getName().equals( property.getAttribute( TYPE ) ) );

		property = (Element) property.getNextSibling();
		assertTrue( PROPERTY.equals( property.getNodeName() ) );
		assertTrue( "foo".equals( property.getAttribute( NAME ) ) );
		assertTrue( Object.class.getName().equals( property.getAttribute( TYPE ) ) );

		property = (Element) property.getNextSibling();
		assertTrue( PROPERTY.equals( property.getNodeName() ) );
		assertTrue( "value".equals( property.getAttribute( NAME ) ) );
		assertTrue( PersonalContact.class.getName().equals( property.getAttribute( ACTUAL_CLASS ) ) );
		assertTrue( Contact.class.getName().equals( property.getAttribute( TYPE ) ) );
	}

	public void testInspectString()
	{
		String xml = mInspector.inspect( "foo", String.class.getName() );
		Document document = XmlUtils.documentFromString( xml );

		assertTrue( "inspection-result".equals( document.getFirstChild().getNodeName() ) );

		// Entity only

		Element entity = (Element) document.getFirstChild().getFirstChild();
		assertTrue( ENTITY.equals( entity.getNodeName() ) );
		assertTrue( String.class.getName().equals( entity.getAttribute( TYPE ) ) );

		assertTrue( 1 == entity.getAttributes().getLength() );
		assertTrue( 0 == entity.getChildNodes().getLength() );
	}

	public void testTraverseViaParent()
	{
		PropertyTypeInspector inspector = new PropertyTypeInspector();

		DeclaredTypeTester tester = new DeclaredTypeTester();
		tester.value = new PersonalContact();
		String xml = inspector.inspect( tester, DeclaredTypeTester.class.getName(), "value" );
		Document document = XmlUtils.documentFromString( xml );

		assertTrue( "inspection-result".equals( document.getFirstChild().getNodeName() ) );

		Element entity = (Element) document.getFirstChild().getFirstChild();
		assertTrue( ENTITY.equals( entity.getNodeName() ) );
		assertTrue( PersonalContact.class.getName().equals( entity.getAttribute( ACTUAL_CLASS ) ) );
		assertTrue( Contact.class.getName().equals( entity.getAttribute( TYPE ) ) );
		assertTrue( "value".equals( entity.getAttribute( NAME ) ) );

		Element property = XmlUtils.getChildWithAttributeValue( entity, NAME, "address" );
		assertTrue( PROPERTY.equals( property.getNodeName() ) );
	}

	public void testCovariantReturn()
	{
		// Superclass

		Document document = XmlUtils.documentFromString( mInspector.inspect( new SuperFoo(), SuperFoo.class.getName() ) );

		assertTrue( "inspection-result".equals( document.getFirstChild().getNodeName() ) );

		Element entity = (Element) document.getFirstChild().getFirstChild();
		assertTrue( ENTITY.equals( entity.getNodeName() ) );

		assertTrue( SuperFoo.class.getName().equals( entity.getAttribute( TYPE ) ) );

		Element property = (Element) entity.getFirstChild();
		assertTrue( PROPERTY.equals( property.getNodeName() ) );
		assertTrue( "contact".equals( property.getAttribute( NAME ) ) );
		assertTrue( Contact.class.getName().equals( property.getAttribute( TYPE ) ) );

		// Subclass

		document = XmlUtils.documentFromString( mInspector.inspect( new SubFoo(), SubFoo.class.getName() ) );

		assertTrue( "inspection-result".equals( document.getFirstChild().getNodeName() ) );

		entity = (Element) document.getFirstChild().getFirstChild();
		assertTrue( ENTITY.equals( entity.getNodeName() ) );
		assertTrue( SubFoo.class.getName().equals( entity.getAttribute( TYPE ) ) );

		property = (Element) entity.getFirstChild();
		assertTrue( PROPERTY.equals( property.getNodeName() ) );
		assertTrue( "bar".equals( property.getAttribute( NAME ) ) );
		assertTrue( TRUE.equals( property.getAttribute( NO_GETTER ) ) );
		assertTrue( String.class.getName().equals( property.getAttribute( TYPE ) ) );
		assertTrue( 3 == property.getAttributes().getLength() );

		property = (Element) property.getNextSibling();
		assertTrue( PROPERTY.equals( property.getNodeName() ) );
		assertTrue( "contact".equals( property.getAttribute( NAME ) ) );
		assertTrue( PersonalContact.class.getName().equals( property.getAttribute( TYPE ) ) );
		assertTrue( TRUE.equals( property.getAttribute( NO_SETTER ) ) );
		assertTrue( TRUE.equals( property.getAttribute( READ_ONLY ) ) );
		assertTrue( 4 == property.getAttributes().getLength() );

		// Check there are no more properties (eg. public static int notVisible)

		assertTrue( property.getNextSibling() == null );
	}

	public void testRecursion()
	{
		RecursiveFoo recursiveFoo = new RecursiveFoo();
		recursiveFoo.foo = recursiveFoo;

		// Top level

		Document document = XmlUtils.documentFromString( mInspector.inspect( recursiveFoo, RecursiveFoo.class.getName() ) );
		assertTrue( "inspection-result".equals( document.getFirstChild().getNodeName() ) );

		Element entity = (Element) document.getFirstChild().getFirstChild();
		assertTrue( ENTITY.equals( entity.getNodeName() ) );
		assertTrue( RecursiveFoo.class.getName().equals( entity.getAttribute( TYPE ) ) );

		Element property = (Element) entity.getFirstChild();
		assertTrue( PROPERTY.equals( property.getNodeName() ) );
		assertTrue( "foo".equals( property.getAttribute( NAME ) ) );
		assertTrue( RecursiveFoo.class.getName().equals( property.getAttribute( ACTUAL_CLASS ) ) );
		assertTrue( Object.class.getName().equals( property.getAttribute( TYPE ) ) );
		assertTrue( 3 == property.getAttributes().getLength() );
		assertTrue( property.getNextSibling() == null );

		// Second level (should block)

		assertTrue( mInspector.inspect( recursiveFoo, RecursiveFoo.class.getName(), "foo" ) == null );

		// Start over

		RecursiveFoo recursiveFoo2 = new RecursiveFoo();
		recursiveFoo.foo = recursiveFoo2;
		recursiveFoo2.foo = recursiveFoo;

		// Second level

		document = XmlUtils.documentFromString( mInspector.inspect( recursiveFoo, RecursiveFoo.class.getName(), "foo" ) );
		assertTrue( "inspection-result".equals( document.getFirstChild().getNodeName() ) );

		entity = (Element) document.getFirstChild().getFirstChild();
		assertTrue( ENTITY.equals( entity.getNodeName() ) );
		assertTrue( Object.class.getName().equals( entity.getAttribute( TYPE ) ) );

		property = (Element) entity.getFirstChild();
		assertTrue( PROPERTY.equals( property.getNodeName() ) );
		assertTrue( "foo".equals( property.getAttribute( NAME ) ) );
		assertTrue( 3 == property.getAttributes().getLength() );
		assertTrue( RecursiveFoo.class.getName().equals( property.getAttribute( ACTUAL_CLASS ) ) );
		assertTrue( Object.class.getName().equals( property.getAttribute( TYPE ) ) );
		assertTrue( property.getNextSibling() == null );

		// Third level (should block)

		assertTrue( mInspector.inspect( recursiveFoo, RecursiveFoo.class.getName(), "foo", "foo" ) == null );
	}

	public void testBadName()
	{
		assertTrue( mInspector.inspect( new SubFoo(), "no-such-type" ) == null );
		assertTrue( mInspector.inspect( new SubFoo(), SubFoo.class.getName(), "no-such-name" ) == null );
		assertTrue( mInspector.inspect( new SubFoo(), SubFoo.class.getName(), "no-such-parent-name", "foo" ) == null );
	}

	public void testNullType()
	{
		assertTrue( null == mInspector.inspect( null, null ) );
	}

	//
	// Inner classes
	//

	protected static class DeclaredTypeTester
	{
		public Object	foo;

		public Object	fOO;

		public Contact	value;
	}

	static class SuperFoo
	{
		public Contact getContact()
		{
			return null;
		}

		protected String getShouldntFindMe()
		{
			return null;
		}

		@SuppressWarnings( "unused" )
		private String getShouldntFindMeEither()
		{
			return null;
		}
	}

	static class SubFoo
		extends SuperFoo
	{
		public final static int	notVisible	= 0;

		//
		//
		// Public methods
		//
		//

		@Override
		public PersonalContact getContact()
		{
			return null;
		}

		public final PropertyChangeListener[] getPropertyChangeListeners()
		{
			return null;
		}

		public final VetoableChangeListener[] getVetoableChangeListeners()
		{
			return null;
		}

		public void setBar( String bar )
		{
			// Do nothing
		}
	}

	public static class RecursiveFoo
	{
		public Object	foo;
	}
}
