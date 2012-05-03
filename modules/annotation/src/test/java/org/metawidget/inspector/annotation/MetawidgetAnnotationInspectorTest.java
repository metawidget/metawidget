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

package org.metawidget.inspector.annotation;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.BaseObjectInspector;
import org.metawidget.inspector.impl.BaseObjectInspectorConfig;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyle;
import org.metawidget.test.model.annotatedaddressbook.Address;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Richard Kennard
 */

public class MetawidgetAnnotationInspectorTest
	extends TestCase {

	//
	// Private members
	//

	private MetawidgetAnnotationInspector	mInspector;

	//
	// Public methods
	//

	@Override
	public void setUp() {

		mInspector = new MetawidgetAnnotationInspector();
	}

	public void testInspection() {

		String inspect = mInspector.inspect( new Address(), Address.class.getName() );
		internalTestInspection( XmlUtils.documentFromString( inspect ) );

		Element domInspect = mInspector.inspectAsDom( new Address(), Address.class.getName() );
		assertEquals( inspect, XmlUtils.nodeToString( domInspect, false ) );
		internalTestInspection( domInspect.getOwnerDocument() );
	}

	private void internalTestInspection( Document document ) {

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Example Entity

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( Address.class.getName(), entity.getAttribute( TYPE ) );
		assertFalse( entity.hasAttribute( NAME ) );

		// Properties

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "city", property.getAttribute( NAME ) );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "owner", property.getAttribute( NAME ) );
		assertEquals( TRUE, property.getAttribute( HIDDEN ) );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "postcode", property.getAttribute( NAME ) );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "state", property.getAttribute( NAME ) );
		assertEquals( "Anytown,Cyberton,Lostville,Whereverton", property.getAttribute( LOOKUP ) );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "street", property.getAttribute( NAME ) );
	}

	public void testImaginaryEntity() {

		String xml = mInspector.inspect( new Foo(), Foo.class.getName() );
		Document document = XmlUtils.documentFromString( xml );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );
		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( Foo.class.getName(), entity.getAttribute( TYPE ) );

		Element property = (Element) entity.getFirstChild().getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "string1", property.getAttribute( NAME ) );
		assertEquals( "bar", property.getAttribute( LABEL ) );
		assertEquals( "bar1", property.getAttribute( "foo1" ) );
		assertEquals( "bar2", property.getAttribute( "foo2" ) );
		assertEquals( TRUE, property.getAttribute( READ_ONLY ) );
		assertEquals( TRUE, property.getAttribute( HIDDEN ) );
		assertEquals( "Foo", property.getAttribute( SECTION ) );
		assertEquals( TRUE, property.getAttribute( MASKED ) );
		assertEquals( TRUE, property.getAttribute( DONT_EXPAND ) );
		assertEquals( TRUE, property.getAttribute( LARGE ) );
		assertEquals( "object1", property.getAttribute( COMES_AFTER ) );
		assertEquals( 11, property.getAttributes().getLength() );

		property = XmlUtils.getNextSiblingElement( property );
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "string2", property.getAttribute( NAME ) );
		assertEquals( "abc-def", property.getAttribute( "abc" ) );
		assertEquals( "abc-def", property.getAttribute( "def" ) );
		assertEquals( "ghi-jkl", property.getAttribute( "ghi" ) );
		assertEquals( "ghi-jkl", property.getAttribute( "jkl" ) );
		assertEquals( "mno-pqr", property.getAttribute( "mno" ) );
		assertEquals( "mno-pqr", property.getAttribute( "pqr" ) );
		assertEquals( 7, property.getAttributes().getLength() );

		Element action = XmlUtils.getNextSiblingElement( property );
		assertEquals( ACTION, action.getNodeName() );
		assertEquals( "doNothing", action.getAttribute( NAME ) );
		assertEquals( "Bar", action.getAttribute( SECTION ) );
		assertEquals( "string1", action.getAttribute( COMES_AFTER ) );
		assertEquals( 3, action.getAttributes().getLength() );

		assertEquals( null, action.getNextSibling() );
	}

	public void testLookup() {

		MetawidgetAnnotationInspector inspector = new MetawidgetAnnotationInspector();
		Document document = XmlUtils.documentFromString( inspector.inspect( new Foo(), Foo.class.getName() ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( Foo.class.getName(), entity.getAttribute( TYPE ) );
		assertFalse( entity.hasAttribute( NAME ) );

		// Properties

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "object1", property.getAttribute( NAME ) );
		assertEquals( "foo\\,,bar", property.getAttribute( LOOKUP ) );
		assertEquals( TRUE, property.getAttribute( REQUIRED ) );
		assertEquals( TRUE, property.getAttribute( WIDE ) );
		assertEquals( property.getAttributes().getLength(), 4 );

		property = (Element) property.getNextSibling();
		assertEquals( "string1", property.getAttribute( NAME ) );
		assertEquals( "bar", property.getAttribute( LABEL ) );
		assertEquals( "bar1", property.getAttribute( "foo1" ) );
		assertEquals( "bar2", property.getAttribute( "foo2" ) );
		assertEquals( TRUE, property.getAttribute( HIDDEN ) );
		assertEquals( TRUE, property.getAttribute( READ_ONLY ) );
		assertEquals( TRUE, property.getAttribute( DONT_EXPAND ) );
		assertEquals( "Foo", property.getAttribute( SECTION ) );
		assertEquals( TRUE, property.getAttribute( MASKED ) );
		assertEquals( TRUE, property.getAttribute( LARGE ) );
		assertEquals( "object1", property.getAttribute( COMES_AFTER ) );

		assertEquals( property.getAttributes().getLength(), 11 );
	}

	public void testBadAction() {

		try {
			mInspector.inspect( new BadAction1(), BadAction1.class.getName() );
			fail();
		} catch ( InspectorException e ) {
			assertEquals( "@UiAction public void org.metawidget.inspector.annotation.MetawidgetAnnotationInspectorTest$BadAction1.doNothing(java.lang.String) must not take any parameters", e.getMessage() );
		}
	}

	public void testInspectString() {

		// Should 'short circuit' and return null, as an optimization for CompositeInspector

		assertEquals( null, mInspector.inspect( "foo", String.class.getName() ) );

		// Should gather annotations from parent

		String xml = mInspector.inspect( new Foo(), Foo.class.getName(), "string1" );
		Document document = XmlUtils.documentFromString( xml );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );
		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( String.class.getName(), entity.getAttribute( TYPE ) );
		assertEquals( "string1", entity.getAttribute( NAME ) );
		assertEquals( "bar", entity.getAttribute( LABEL ) );
	}

	/**
	 * Test that parent properties <em>and</em> parent traits get merged in properly.
	 */

	public void testInspectParent() {

		MetawidgetAnnotationInspector inspector = new MetawidgetAnnotationInspector();
		Document document = XmlUtils.documentFromString( inspector.inspect( new PropertyAndTraitAnnotation(), PropertyAndTraitAnnotation.class.getName(), "foo" ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( String.class.getName(), entity.getAttribute( TYPE ) );
		assertEquals( "foo", entity.getAttribute( NAME ) );
		assertEquals( TRUE, entity.getAttribute( MASKED ) );
		assertEquals( "Foo", entity.getAttribute( LABEL ) );
		assertEquals( 4, entity.getAttributes().getLength() );

		assertEquals( 0, entity.getChildNodes().getLength() );

		// Test null parent doesn't throw NullPointerException

		assertEquals( null, inspector.inspect( null, PropertyAndTraitAnnotation.class.getName(), "foo" ) );
	}

	@SuppressWarnings( "unchecked" )
	public void testNullPropertyStyle()
		throws Exception {

		MetawidgetAnnotationInspector inspector = new MetawidgetAnnotationInspector();
		Method getPropertiesMethod = BaseObjectInspector.class.getDeclaredMethod( "getProperties", String.class );
		getPropertiesMethod.setAccessible( true );

		// Should fail hard

		try {
			assertTrue( ( (Map<String, Property>) getPropertiesMethod.invoke( inspector, (Object) null ) ).isEmpty() );
			fail();
		} catch ( InvocationTargetException e ) {
			assertTrue( e.getCause() instanceof NullPointerException );
		}

		// Should fail gracefully

		inspector = new MetawidgetAnnotationInspector( new BaseObjectInspectorConfig().setPropertyStyle( null ) );
		assertTrue( ( (Map<String, Property>) getPropertiesMethod.invoke( inspector, (Object) null ) ).isEmpty() );

		// Un-null again

		assertEquals( 0, ( (Map<String, Property>) getPropertiesMethod.invoke( inspector, Foo.class.getName() ) ).size() );
		inspector = new MetawidgetAnnotationInspector( new BaseObjectInspectorConfig().setPropertyStyle( new JavaBeanPropertyStyle() ) );
		assertEquals( 3, ( (Map<String, Property>) getPropertiesMethod.invoke( inspector, Foo.class.getName() ) ).size() );
	}

	@SuppressWarnings( "unchecked" )
	public void testNullActionStyle()
		throws Exception {

		MetawidgetAnnotationInspector inspector = new MetawidgetAnnotationInspector();
		Method getActionsMethod = BaseObjectInspector.class.getDeclaredMethod( "getActions", String.class );
		getActionsMethod.setAccessible( true );

		// Should fail hard

		try {
			assertTrue( ( (Map<String, Property>) getActionsMethod.invoke( inspector, (Object) null ) ).isEmpty() );
			fail();
		} catch ( InvocationTargetException e ) {
			assertTrue( e.getCause() instanceof NullPointerException );
		}

		// Should fail gracefully

		BaseObjectInspectorConfig config = new BaseObjectInspectorConfig();
		config.setActionStyle( null );

		inspector = new MetawidgetAnnotationInspector( config );
		assertTrue( ( (Map<String, Property>) getActionsMethod.invoke( inspector, (Object) null ) ).isEmpty() );

		// Un-null again

		assertEquals( 0, ( (Map<String, Property>) getActionsMethod.invoke( inspector, Foo.class.getName() ) ).size() );
		inspector = new MetawidgetAnnotationInspector( new BaseObjectInspectorConfig().setPropertyStyle( new JavaBeanPropertyStyle() ) );
		assertEquals( 1, ( (Map<String, Property>) getActionsMethod.invoke( inspector, Foo.class.getName() ) ).size() );
	}

	public void testSuperclassAnnotations()
		throws Exception {

		MetawidgetAnnotationInspector inspector = new MetawidgetAnnotationInspector();
		Document document = XmlUtils.documentFromString( inspector.inspect( new SubFoo(), SubFoo.class.getName() ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( SubFoo.class.getName(), entity.getAttribute( TYPE ) );

		Element action = XmlUtils.getChildWithAttributeValue( entity, "name", "doNothing" );
		assertEquals( ACTION, action.getNodeName() );
		assertEquals( "string1", action.getAttribute( COMES_AFTER ) );
		assertEquals( "Bar", action.getAttribute( SECTION ) );
		assertEquals( 3, action.getAttributes().getLength() );
	}

	public void testAlphabeticalMethods() {

		String xml = mInspector.inspect( null, AlphabeticalMethods.class.getName() );
		Document document = XmlUtils.documentFromString( xml );
		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );
		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( AlphabeticalMethods.class.getName(), entity.getAttribute( TYPE ) );
		assertEquals( 3, entity.getChildNodes().getLength() );

		Element property = (Element) entity.getFirstChild();
		assertEquals( ACTION, property.getNodeName() );
		assertEquals( "action", property.getAttribute( NAME ) );
		assertEquals( 1, property.getAttributes().getLength() );

		property = (Element) property.getNextSibling();
		assertEquals( ACTION, property.getNodeName() );
		assertEquals( "WEPKey", property.getAttribute( NAME ) );
		assertEquals( 1, property.getAttributes().getLength() );

		property = (Element) property.getNextSibling();
		assertEquals( ACTION, property.getNodeName() );
		assertEquals( "wepKey", property.getAttribute( NAME ) );
		assertEquals( 1, property.getAttributes().getLength() );
	}

	//
	// Inner class
	//

	public static class PropertyAndTraitAnnotation {

		@UiMasked
		@UiLabel( "Foo" )
		public String getFoo() {

			return null;
		}
	}

	public static class Foo {

		@UiRequired
		@UiLookup( value = { "foo,", "bar" } )
		@UiWide
		public Object getObject1() {

			return null;
		}

		@UiLabel( "bar" )
		@UiAttributes( { @UiAttribute( name = "foo1", value = "bar1" ), @UiAttribute( name = "foo2", value = "bar2" ) } )
		@UiHidden
		@UiReadOnly
		@UiDontExpand
		@UiSection( "Foo" )
		@UiMasked
		@UiComesAfter( "object1" )
		@UiLarge
		public String getString1() {

			return null;
		}

		@UiAttribute( name = { "abc", "def" }, value = "abc-def" )
		@UiAttributes( { @UiAttribute( name = { "ghi", "jkl" }, value = "ghi-jkl" ), @UiAttribute( name = { "mno", "pqr" }, value = "mno-pqr" ) } )
		public String getString2() {

			return null;
		}

		@UiAction
		@UiComesAfter( "string1" )
		@UiSection( "Bar" )
		public void doNothing() {

			// Do nothing
		}
	}

	public static class BadAction1 {

		/**
		 * @param foo
		 */

		@UiAction
		public void doNothing( String foo ) {

			// Do nothing
		}
	}

	public static class SubFoo
		extends Foo {

		@Override
		public void doNothing() {

			// Overridden without annotations
		}
	}

	public static class AlphabeticalMethods {

		@UiAction
		public void action() {

			// Do nothing
		}

		@UiAction
		public void wepKey() {

			// Do nothing
		}

		@UiAction
		public void WEPKey() {

			// Do nothing
		}
	}
}
