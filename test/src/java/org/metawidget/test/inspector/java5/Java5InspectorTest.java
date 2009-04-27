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

package org.metawidget.test.inspector.java5;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.metawidget.inspector.java5.Java5Inspector;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Richard Kennard
 */

public class Java5InspectorTest
	extends TestCase
{
	//
	// Constructor
	//

	/**
	 * JUnit 3.7 constructor.
	 */

	public Java5InspectorTest( String name )
	{
		super( name );
	}

	//
	// Public methods
	//

	public void testInspection()
	{
		Java5Inspector inspector = new Java5Inspector();

		Document document = XmlUtils.documentFromString( inspector.inspect( new Bar(), Bar.class.getName() ));

		assertTrue( "inspection-result".equals( document.getFirstChild().getNodeName() ) );

		// Entity

		Element entity = (Element) document.getFirstChild().getFirstChild();
		assertTrue( ENTITY.equals( entity.getNodeName() ) );
		assertTrue( Bar.class.getName().equals( entity.getAttribute( TYPE ) ) );
		assertTrue( !entity.hasAttribute( NAME ) );

		// Properties

		Element property = XmlUtils.getChildWithAttributeValue( entity, NAME, "foo" );
		assertTrue( PROPERTY.equals( property.getNodeName() ) );
		assertTrue( Foo.class.getName().equals( property.getAttribute( TYPE ) ) );
		assertTrue( "FOO1,FOO2".equals( property.getAttribute( LOOKUP ) ) );
		assertTrue( "foo1,foo2".equals( property.getAttribute( LOOKUP_LABELS ) ) );
		assertTrue( 4 == property.getAttributes().getLength() );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "baz" );
		assertTrue( PROPERTY.equals( property.getNodeName() ) );

		String genericArguments = Set.class.getName() + "<" + String.class.getName() + ">," + List.class.getName() + "<" + Set.class.getName() + "<" + Date.class.getName() + ">>";
		assertTrue( genericArguments.equals( property.getAttribute( PARAMETERIZED_TYPE ) ) );

		// Check there are no more properties (eg. getClass)

		assertTrue( entity.getChildNodes().getLength() == 2 );
	}

	public void testInspectString()
	{
		Java5Inspector inspector = new Java5Inspector();

		// Should 'short circuit' and return null, as an optimization for CompositeInspector

		assertTrue( null == inspector.inspect( "foo", String.class.getName() ));
	}

	//
	// Inner classes
	//

	protected enum Foo
	{
		FOO1
		{
			@Override
			public String toString()
			{
				return "foo1";
			}
		},

		FOO2
		{
			@Override
			public String toString()
			{
				return "foo2";
			}
		}
	}

	static class Bar
	{
		public Foo									foo;

		public Map<Set<String>, List<Set<Date>>>	baz;
	}
}
