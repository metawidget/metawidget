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

package org.metawidget.inspector.java5;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.composite.CompositeInspectorConfig;
import org.metawidget.inspector.iface.Inspector;
import org.metawidget.inspector.propertytype.PropertyTypeInspectionResultConstants;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.util.Java5ClassUtils;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Richard Kennard
 */

public class Java5InspectorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testInspection() {

		Inspector inspector = new Java5Inspector();

		Document document = XmlUtils.documentFromString( inspector.inspect( new Bar(), Bar.class.getName() ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getFirstChild().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( Bar.class.getName(), entity.getAttribute( TYPE ) );
		assertFalse( entity.hasAttribute( NAME ) );

		// Properties

		Element property = XmlUtils.getChildWithAttributeValue( entity, NAME, "foo" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( Foo.class.getName(), property.getAttribute( TYPE ) );
		assertEquals( "FOO1,FOO2", property.getAttribute( LOOKUP ) );
		assertEquals( "foo1,foo2", property.getAttribute( LOOKUP_LABELS ) );
		assertTrue( 4 == property.getAttributes().getLength() );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "baz" );
		assertEquals( PROPERTY, property.getNodeName() );

		String genericArguments = Set.class.getName() + "<" + String.class.getName() + ">," + List.class.getName() + "<" + Set.class.getName() + "<" + Date.class.getName() + ">>";
		assertEquals( genericArguments, property.getAttribute( PARAMETERIZED_TYPE ) );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "abc" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( Boolean.class.getName(), property.getAttribute( PARAMETERIZED_TYPE ) );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "def" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( Integer.class.getName(), property.getAttribute( PARAMETERIZED_TYPE ) );

		// Check there are no more properties (eg. getClass)

		assertTrue( entity.getChildNodes().getLength() == 4 );

		// Test with an enum instance

		Bar bar = new Bar();
		bar.foo = Foo.FOO1;
		document = XmlUtils.documentFromString( inspector.inspect( bar, Bar.class.getName() ) );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );
		entity = (Element) document.getFirstChild().getFirstChild();
		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "foo" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( Foo.class.getName(), property.getAttribute( TYPE ) );
		assertEquals( "FOO1,FOO2", property.getAttribute( LOOKUP ) );
		assertEquals( "foo1,foo2", property.getAttribute( LOOKUP_LABELS ) );
		assertEquals( 4, property.getAttributes().getLength() );

		// Test pointed directly at an enum

		document = XmlUtils.documentFromString( inspector.inspect( Foo.FOO1, Foo.class.getName() ) );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );
		entity = (Element) document.getFirstChild().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( Foo.class.getName(), entity.getAttribute( TYPE ) );
		assertEquals( "FOO1,FOO2", entity.getAttribute( LOOKUP ) );
		assertEquals( "foo1,foo2", entity.getAttribute( LOOKUP_LABELS ) );
		assertEquals( 3, entity.getAttributes().getLength() );
		assertFalse( entity.hasChildNodes() );

		// Test pointed directly at an empty enum via a parent

		document = XmlUtils.documentFromString( inspector.inspect( new Bar(), Bar.class.getName(), "foo" ) );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );
		entity = (Element) document.getFirstChild().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "foo", entity.getAttribute( NAME ) );
		assertEquals( Foo.class.getName(), entity.getAttribute( TYPE ) );
		assertEquals( "FOO1,FOO2", entity.getAttribute( LOOKUP ) );
		assertEquals( "foo1,foo2", entity.getAttribute( LOOKUP_LABELS ) );
		assertEquals( 4, entity.getAttributes().getLength() );
		assertFalse( entity.hasChildNodes() );

		// Test an enum with PropertyTypeInspector

		inspector = new CompositeInspector( new CompositeInspectorConfig().setInspectors( new PropertyTypeInspector(), new Java5Inspector() ) );
		document = XmlUtils.documentFromString( inspector.inspect( bar, Bar.class.getName() ) );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );
		entity = (Element) document.getFirstChild().getFirstChild();
		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "foo" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( Foo.class.getName(), property.getAttribute( TYPE ) );
		assertEquals( Foo.FOO1.getClass().getName(), property.getAttribute( PropertyTypeInspectionResultConstants.ACTUAL_CLASS ) );
		assertEquals( "FOO1,FOO2", property.getAttribute( LOOKUP ) );
		assertEquals( "foo1,foo2", property.getAttribute( LOOKUP_LABELS ) );
		assertEquals( 5, property.getAttributes().getLength() );
	}

	public void testInspectString() {

		Java5Inspector inspector = new Java5Inspector();

		// Should 'short circuit' and return null, as an optimization for CompositeInspector

		assertTrue( null == inspector.inspect( "foo", String.class.getName() ) );
	}

	public void testSuperclassGenericReturnType() {

		Inspector inspector = new Java5Inspector();

		Document document = XmlUtils.documentFromString( inspector.inspect( new SubBar(), SubBar.class.getName() ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getFirstChild().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( SubBar.class.getName(), entity.getAttribute( TYPE ) );
		assertFalse( entity.hasAttribute( NAME ) );

		// Properties

		Element property = XmlUtils.getChildWithAttributeValue( entity, NAME, "abc" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( Boolean.class.getName(), property.getAttribute( PARAMETERIZED_TYPE ) );

		property = XmlUtils.getChildWithAttributeValue( entity, NAME, "def" );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( Integer.class.getName(), property.getAttribute( PARAMETERIZED_TYPE ) );
	}

	public void testJava5ClassUtils()
		throws Exception {

		Method method = Date.class.getMethod( "getTime" );
		assertEquals( method.getGenericReturnType(), Java5ClassUtils.getOriginalGenericReturnType( method ) );

		method = Date.class.getMethod( "setTime", long.class );
		assertEquals( method.getGenericParameterTypes().length, Java5ClassUtils.getOriginalGenericParameterTypes( method ).length );
	}

	//
	// Inner classes
	//

	protected enum Foo {
		FOO1 {

			@Override
			public String toString() {

				return "foo1";
			}
		},

		FOO2 {

			@Override
			public String toString() {

				return "foo2";
			}
		}
	}

	protected static class Bar {

		public Foo									foo;

		public Map<Set<String>, List<Set<Date>>>	baz;

		public List<Boolean> getAbc() {

			return null;
		}

		/**
		 * @param def
		 */

		public void setDef( List<Integer> def ) {

			// Do nothing
		}
	}

	protected static class SubBar
		extends Bar {

		@Override
		@SuppressWarnings( { "rawtypes", "unchecked" } )
		public List getAbc() {

			return null;
		}

		@SuppressWarnings( "rawtypes" )
		@Override
		public void setDef( List def ) {

			// Do nothing
		}
	}
}
