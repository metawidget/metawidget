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

package org.metawidget.inspector.commons.jexl;

import static org.metawidget.inspector.InspectionResultConstants.*;
import junit.framework.TestCase;

import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Richard Kennard
 */

public class JexlInspectorTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testAnnotations()
	{
		JexlInspector inspector = new JexlInspector();
		assertTrue( null == inspector.inspect( (Object) null, null ) );

		// With a null Foo

		String xml = inspector.inspect( null, Foo.class.getName() );
		Document document = XmlUtils.documentFromString( xml );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getFirstChild().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( Foo.class.getName(), entity.getAttribute( TYPE ) );
		assertFalse( entity.hasAttribute( NAME ) );

		// Properties

		Element property = XmlUtils.getChildWithAttributeValue( entity, NAME, "bar1" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertFalse( property.hasAttribute( "value-is-el" ) );
		assertEquals( "text", property.getAttribute( "value-is-text" ) );
		assertEquals( "was set", property.getAttribute( "expression-is-false" ) );
		assertTrue( 3 == property.getAttributes().getLength() );

		assertTrue( entity.getChildNodes().getLength() == 1 );

		// With a real Foo

		xml = inspector.inspect( new Foo(), Foo.class.getName() );
		document = XmlUtils.documentFromString( xml );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		entity = (Element) document.getFirstChild().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( Foo.class.getName(), entity.getAttribute( TYPE ) );
		assertFalse( entity.hasAttribute( NAME ) );

		// Properties

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "bar1" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "from-baz", property.getAttribute( "value-is-el" ) );
		assertEquals( "text", property.getAttribute( "value-is-text" ) );
		assertFalse( property.hasAttribute( "expression-is-false" ) );
		assertTrue( 3 == property.getAttributes().getLength() );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "bar2" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "was set", property.getAttribute( "expression-is-true" ) );
		assertTrue( 2 == property.getAttributes().getLength() );

		assertTrue( entity.getChildNodes().getLength() == 2 );
	}

	public void testBadExpression()
	{
		try
		{
			new JexlInspector().inspect( new Bar(), Bar.class.getName() );
			assertTrue( false );
		}
		catch ( InspectorException e )
		{
			assertEquals( "Expression '${baz}' should be of the form 'foo.bar', not '${foo.bar}'", e.getMessage() );
		}
	}

	public void testThis()
	{
		ThisTest thisTest1 = new ThisTest();
		thisTest1.setIdentity( "ThisTest #1" );
		ThisTest thisTest2 = new ThisTest();
		thisTest2.setIdentity( "ThisTest #2" );
		thisTest1.setChild( thisTest2 );

		JexlInspector inspector = new JexlInspector();

		// Top-level

		String xml = inspector.inspect( thisTest1, ThisTest.class.getName() );
		Document document = XmlUtils.documentFromString( xml );
		Element entity = (Element) document.getFirstChild().getFirstChild();
		Element property = XmlUtils.getChildWithAttributeValue( entity, NAME, "me" );
		assertEquals( property.getAttribute( "who-am-i" ), "ThisTest #1" );

		// Property-level

		xml = inspector.inspect( thisTest1, ThisTest.class.getName(), "me" );
		document = XmlUtils.documentFromString( xml );
		entity = (Element) document.getFirstChild().getFirstChild();
		assertEquals( entity.getAttribute( "who-am-i" ), "ThisTest #1" );
		assertEquals( entity.getAttribute( NAME ), "me" );

		// Child-level

		xml = inspector.inspect( thisTest1, ThisTest.class.getName(), "child", "me" );
		document = XmlUtils.documentFromString( xml );
		entity = (Element) document.getFirstChild().getFirstChild();
		assertEquals( entity.getAttribute( "who-am-i" ), "ThisTest #2" );
		assertEquals( entity.getAttribute( NAME ), "me" );
	}

	//
	// Inner class
	//

	public static class Foo
	{
		@UiJexlAttributes( { @UiJexlAttribute( name = "value-is-el", expression = "this.baz" ), @UiJexlAttribute( name = "value-is-text", expression = "'text'" ), @UiJexlAttribute( name = "expression-is-false", expression = "if ( !this.expressionResult ) 'was set'" ) } )
		public String	bar1;

		@UiJexlAttribute( name = "expression-is-true", expression = "if ( this.expressionResult ) { 'was set'; }" )
		public String	bar2;

		public String getBaz()
		{
			return "from-baz";
		}

		public boolean isExpressionResult()
		{
			return true;
		}
	}

	public static class Bar
	{
		@UiJexlAttribute( name = "foo", expression = "${baz}" )
		public String	baz;
	}

	public static class ThisTest
	{
		private String		mIdentity;

		private ThisTest	mChild;

		/**
		 * The injection of 'this' into the JEXL context needs to work for both normal scenarios and
		 * 'direct to child' scenarios (ie. the Metawidget is pointed at #{thisTest.child.me}). In
		 * the latter, 'this' has to refer to the parent, not the child
		 */

		@UiJexlAttribute( name = "who-am-i", expression = "this.identity" )
		public String		me;

		public ThisTest getChild()
		{
			return mChild;
		}

		public void setChild( ThisTest child )
		{
			this.mChild = child;
		}

		public String getIdentity()
		{
			return this.mIdentity;
		}

		public void setIdentity( String identity )
		{
			this.mIdentity = identity;
		}
	}
}
