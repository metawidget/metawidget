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

package org.metawidget.inspector.propertytype;

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

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getFirstChild().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( PersonalContact.class.getName(), entity.getAttribute( TYPE ) );
		assertFalse( entity.hasAttribute( NAME ) );

		// Properties (should be sorted alphabetically)

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "address", property.getAttribute( NAME ) );
		assertEquals( Address.class.getName(), property.getAttribute( TYPE ) );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "communications", property.getAttribute( NAME ) );
		assertEquals( Set.class.getName(), property.getAttribute( TYPE ) );

		// Test declared-class

		DeclaredTypeTester tester = new DeclaredTypeTester();
		tester.value = personalContact;

		document = XmlUtils.documentFromString( mInspector.inspect( tester, DeclaredTypeTester.class.getName() ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		entity = (Element) document.getFirstChild().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( DeclaredTypeTester.class.getName(), entity.getAttribute( TYPE ) );
		assertFalse( entity.hasAttribute( NAME ) );

		property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "fOO", property.getAttribute( NAME ) );
		assertEquals( Object.class.getName(), property.getAttribute( TYPE ) );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "foo", property.getAttribute( NAME ) );
		assertEquals( Object.class.getName(), property.getAttribute( TYPE ) );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "value", property.getAttribute( NAME ) );
		assertEquals( PersonalContact.class.getName(), property.getAttribute( ACTUAL_CLASS ) );
		assertEquals( Contact.class.getName(), property.getAttribute( TYPE ) );
	}

	public void testInspectString()
	{
		// Test pointed directly at an actual String

		String xml = mInspector.inspect( "foo", String.class.getName() );
		Document document = XmlUtils.documentFromString( xml );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );
		Element entity = (Element) document.getFirstChild().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( String.class.getName(), entity.getAttribute( TYPE ) );
		assertTrue( 1 == entity.getAttributes().getLength() );
		assertTrue( 0 == entity.getChildNodes().getLength() );

		// Test pointed directly at a null String

		xml = mInspector.inspect( null, String.class.getName() );
		document = XmlUtils.documentFromString( xml );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );
		entity = (Element) document.getFirstChild().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( String.class.getName(), entity.getAttribute( TYPE ) );
		assertTrue( 1 == entity.getAttributes().getLength() );
		assertTrue( 0 == entity.getChildNodes().getLength() );

		// Test pointed indirectly at an actual String

		StringHolder stringHolder = new StringHolder();
		stringHolder.string = "foo";
		xml = mInspector.inspect( stringHolder, StringHolder.class.getName(), "string" );
		document = XmlUtils.documentFromString( xml );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );
		entity = (Element) document.getFirstChild().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( String.class.getName(), entity.getAttribute( TYPE ) );
		assertEquals( "string", entity.getAttribute( NAME ) );
		assertTrue( 2 == entity.getAttributes().getLength() );
		assertTrue( 0 == entity.getChildNodes().getLength() );

		// Test pointed indirectly at a null String

		stringHolder.string = null;
		xml = mInspector.inspect( stringHolder, StringHolder.class.getName(), "string" );
		document = XmlUtils.documentFromString( xml );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );
		entity = (Element) document.getFirstChild().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( String.class.getName(), entity.getAttribute( TYPE ) );
		assertEquals( "string", entity.getAttribute( NAME ) );
		assertTrue( 2 == entity.getAttributes().getLength() );
		assertTrue( 0 == entity.getChildNodes().getLength() );
	}

	public void testTraverseViaParent()
	{
		// Indirectly pointed at a not null complex type

		PropertyTypeInspector inspector = new PropertyTypeInspector();

		DeclaredTypeTester tester = new DeclaredTypeTester();
		tester.value = new PersonalContact();
		String xml = inspector.inspect( tester, DeclaredTypeTester.class.getName(), "value" );
		Document document = XmlUtils.documentFromString( xml );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		Element entity = (Element) document.getFirstChild().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( Contact.class.getName(), entity.getAttribute( TYPE ) );
		assertEquals( "value", entity.getAttribute( NAME ) );
		assertEquals( PersonalContact.class.getName(), entity.getAttribute( ACTUAL_CLASS ) );
		assertTrue( 3 == entity.getAttributes().getLength() );

		Element property = XmlUtils.getChildWithAttributeValue( entity, NAME, "address" );
		assertEquals( PROPERTY, property.getNodeName() );

		// Indirectly pointed at a null complex type

		tester.value = null;
		xml = inspector.inspect( tester, DeclaredTypeTester.class.getName(), "value" );
		document = XmlUtils.documentFromString( xml );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		entity = (Element) document.getFirstChild().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( Contact.class.getName(), entity.getAttribute( TYPE ) );
		assertEquals( "value", entity.getAttribute( NAME ) );
		assertTrue( 2 == entity.getAttributes().getLength() );

		// (should be no children because value was null)

		assertTrue( 0 == entity.getChildNodes().getLength() );
	}

	public void testCovariantReturn()
	{
		// Superclass

		Document document = XmlUtils.documentFromString( mInspector.inspect( new SuperFoo(), SuperFoo.class.getName() ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		Element entity = (Element) document.getFirstChild().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );

		assertEquals( SuperFoo.class.getName(), entity.getAttribute( TYPE ) );

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "contact", property.getAttribute( NAME ) );
		assertEquals( Contact.class.getName(), property.getAttribute( TYPE ) );

		// Subclass

		document = XmlUtils.documentFromString( mInspector.inspect( new SubFoo(), SubFoo.class.getName() ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		entity = (Element) document.getFirstChild().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( SubFoo.class.getName(), entity.getAttribute( TYPE ) );

		property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "bar", property.getAttribute( NAME ) );
		assertEquals( TRUE, property.getAttribute( NO_GETTER ) );
		assertEquals( String.class.getName(), property.getAttribute( TYPE ) );
		assertTrue( 3 == property.getAttributes().getLength() );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "contact", property.getAttribute( NAME ) );
		assertEquals( PersonalContact.class.getName(), property.getAttribute( TYPE ) );
		assertEquals( TRUE, property.getAttribute( NO_SETTER ) );
		assertTrue( 3 == property.getAttributes().getLength() );

		// Check there are no more properties (eg. public static int notVisible)

		assertTrue( property.getNextSibling() == null );
	}

	public void testCovariantSetter()
	{
		Document document = XmlUtils.documentFromString( mInspector.inspect( new SubFoo2(), SubFoo2.class.getName() ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		Element entity = (Element) document.getFirstChild().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( SubFoo2.class.getName(), entity.getAttribute( TYPE ) );

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "contact", property.getAttribute( NAME ) );
		assertEquals( PersonalContact.class.getName(), property.getAttribute( TYPE ) );
		assertTrue( 2 == property.getAttributes().getLength() );

		// Check there are no more properties (eg. public static int notVisible)

		assertTrue( property.getNextSibling() == null );
	}

	public void testRecursion()
	{
		RecursiveFoo recursiveFoo = new RecursiveFoo();
		recursiveFoo.foo = recursiveFoo;

		// Top level

		Document document = XmlUtils.documentFromString( mInspector.inspect( recursiveFoo, RecursiveFoo.class.getName() ) );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		Element entity = (Element) document.getFirstChild().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( RecursiveFoo.class.getName(), entity.getAttribute( TYPE ) );

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "foo", property.getAttribute( NAME ) );
		assertEquals( RecursiveFoo.class.getName(), property.getAttribute( ACTUAL_CLASS ) );
		assertEquals( Object.class.getName(), property.getAttribute( TYPE ) );
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
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		entity = (Element) document.getFirstChild().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( Object.class.getName(), entity.getAttribute( TYPE ) );

		property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "foo", property.getAttribute( NAME ) );
		assertTrue( 3 == property.getAttributes().getLength() );
		assertEquals( RecursiveFoo.class.getName(), property.getAttribute( ACTUAL_CLASS ) );
		assertEquals( Object.class.getName(), property.getAttribute( TYPE ) );
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

	public void testBoolean()
	{
		// Boolean (big 'b')

		Document document = XmlUtils.documentFromString( mInspector.inspect( null, Boolean.class.getName() ) );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		Element entity = (Element) document.getFirstChild().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( Boolean.class.getName(), entity.getAttribute( TYPE ) );
		assertEquals( "true, false", entity.getAttribute( LOOKUP ) );
		assertEquals( "Yes, No", entity.getAttribute( LOOKUP_LABELS ) );
		assertTrue( 3 == entity.getAttributes().getLength() );

		// boolean (little 'b')

		document = XmlUtils.documentFromString( mInspector.inspect( null, boolean.class.getName() ) );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		entity = (Element) document.getFirstChild().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( boolean.class.getName(), entity.getAttribute( TYPE ) );
		assertFalse( entity.hasAttribute( LOOKUP ) );
		assertTrue( 1 == entity.getAttributes().getLength() );

		// boolean with a value

		document = XmlUtils.documentFromString( mInspector.inspect( new BooleanHolder(), BooleanHolder.class.getName() ) );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		entity = (Element) document.getFirstChild().getFirstChild();
		Element property = XmlUtils.getChildWithAttributeValue( entity, NAME, "littleBoolean" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( boolean.class.getName(), property.getAttribute( TYPE ) );
		assertFalse( property.hasAttribute( LOOKUP ) );
		assertTrue( 2 == property.getAttributes().getLength() );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "bigBoolean" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( Boolean.class.getName(), property.getAttribute( TYPE ) );
		assertEquals( "true, false", property.getAttribute( LOOKUP ) );
		assertEquals( "Yes, No", property.getAttribute( LOOKUP_LABELS ) );
		assertTrue( 4 == property.getAttributes().getLength() );

		// Pointed directly at a boolean (little 'b')

		document = XmlUtils.documentFromString( mInspector.inspect( new BooleanHolder(), BooleanHolder.class.getName(), "littleBoolean" ) );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		entity = (Element) document.getFirstChild().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "littleBoolean", entity.getAttribute( NAME ) );
		assertEquals( boolean.class.getName(), entity.getAttribute( TYPE ) );
		assertFalse( entity.hasAttribute( LOOKUP ) );
		assertFalse( entity.hasAttribute( LOOKUP_LABELS ) );
		assertTrue( 2 == entity.getAttributes().getLength() );
	}

	public void testTraverseActualTypes()
	{
		DeclaredTypeTester test = new DeclaredTypeTester();
		test.foo = new DeclaredTypeTester();
		( (DeclaredTypeTester) test.foo ).foo = new DeclaredTypeTester();

		// Traversal from foo to foo.foo will fail if PropertyTypeInspector is using
		// the declared type (java.lang.Object) instead of the actual type (DeclaredTypeTester)

		Document document = XmlUtils.documentFromString( mInspector.inspect( test, test.getClass().getName(), "foo", "foo" ) );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		Element entity = (Element) document.getFirstChild().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "foo", entity.getAttribute( NAME ) );
		assertEquals( Object.class.getName(), entity.getAttribute( TYPE ) );
		assertEquals( DeclaredTypeTester.class.getName(), entity.getAttribute( ACTUAL_CLASS ) );

		// Traversal to a null property can still return information about the properties type

		document = XmlUtils.documentFromString( mInspector.inspect( test, test.getClass().getName(), "foo", "foo", "foo" ) );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		entity = (Element) document.getFirstChild().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "foo", entity.getAttribute( NAME ) );
		assertEquals( Object.class.getName(), entity.getAttribute( TYPE ) );
		assertFalse( entity.hasAttribute( ACTUAL_CLASS ) );

		// Traversal any further should fail gracefully (ie. not NullPointerException)

		assertTrue( null == mInspector.inspect( test, test.getClass().getName(), "foo", "foo", "foo", "foo" ) );
		assertTrue( null == mInspector.inspect( test, test.getClass().getName(), "foo", "foo", "foo", "foo", "foo" ) );
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

	protected static class SuperFoo
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

	protected static class SubFoo
		extends SuperFoo
	{
		public final static int	notVisible	= 0;

		//
		// Public methods
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

	protected static class SubFoo2
		extends SuperFoo
	{
		//
		// Public methods
		//

		@Override
		public PersonalContact getContact()
		{
			return null;
		}

		public void setContact( Contact contact )
		{
			// Do nothing
		}
	}

	public static class RecursiveFoo
	{
		public Object	foo;
	}

	public static class StringHolder
	{
		public String	string;
	}

	public static class BooleanHolder
	{
		public boolean	littleBoolean;

		public Boolean	bigBoolean;
	}
}
