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

package org.metawidget.inspector.xml;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.io.ByteArrayInputStream;

import junit.framework.TestCase;

import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyle;
import org.metawidget.util.LogUtils;
import org.metawidget.util.LogUtilsTest;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Richard Kennard
 */

public class XmlInspectorTest
	extends TestCase {

	//
	// Private members
	//

	private XmlInspector	mInspector;

	//
	// Public methods
	//

	@Override
	public void setUp() {

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<inspection-result xmlns=\"http://www.metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.metawidget.org/inspection-result ../../inspector/inspection-result-1.0.xsd\" version=\"1.0\">";
		xml += "<entity type=\"org.metawidget.inspector.xml.XmlInspectorTest$SuperSuperFoo\">";
		xml += "<property name=\"bar\" type=\"Bar\" required=\"true\"/>";
		xml += "<property name=\"a\"/>";
		xml += "<property name=\"d\"/>";
		xml += "</entity>";
		xml += "<entity type=\"org.metawidget.inspector.xml.XmlInspectorTest$SuperFoo\" extends=\"org.metawidget.inspector.xml.XmlInspectorTest$SuperSuperFoo\"/>";
		xml += "<entity type=\"org.metawidget.inspector.xml.XmlInspectorTest$SubFoo\" extends=\"org.metawidget.inspector.xml.XmlInspectorTest$SuperFoo\">";
		xml += "<property name=\"a\" hidden=\"true\" label=\" \"/>";
		xml += "<property name=\"b\" label=\"\"/>";
		xml += "<property name=\"c\" lookup=\"Telephone, Mobile, Fax, E-mail\"/>";
		xml += "</entity>";
		xml += "<entity type=\"Bar\">";
		xml += "<property name=\"baz\"/>";
		xml += "<action name=\"doAction\"/>";
		xml += "<some-junk name=\"ignoreMe\"/>";
		xml += "</entity>";
		xml += "<entity type=\"Typo1\">";
		xml += "<property name=\"foo\" readonly=\"true\"/>";
		xml += "</entity>";
		xml += "<entity type=\"Typo2\">";
		xml += "<property name=\"foo\" dontexpand=\"true\"/>";
		xml += "</entity>";
		xml += "</inspection-result>";

		XmlInspectorConfig config = new XmlInspectorConfig();
		config.setInputStream( new ByteArrayInputStream( xml.getBytes() ) );
		mInspector = new XmlInspector( config );
	}

	public void testInspection() {

		String inspect = mInspector.inspect( null, "org.metawidget.inspector.xml.XmlInspectorTest$SubFoo" );
		internalTestInspection( XmlUtils.documentFromString( inspect ) );

		Element domInspect = mInspector.inspectAsDom( null, "org.metawidget.inspector.xml.XmlInspectorTest$SubFoo" );
		assertEquals( inspect, XmlUtils.nodeToString( domInspect, false ) );
		internalTestInspection( domInspect.getOwnerDocument() );
	}

	private void internalTestInspection( Document document ) {

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		// Entity

		Element entity = (Element) document.getFirstChild().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "org.metawidget.inspector.xml.XmlInspectorTest$SubFoo", entity.getAttribute( TYPE ) );
		assertFalse( entity.hasAttribute( NAME ) );

		// Properties

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "bar", property.getAttribute( NAME ) );
		assertEquals( "Bar", property.getAttribute( TYPE ) );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "a", property.getAttribute( NAME ) );
		assertEquals( TRUE, property.getAttribute( HIDDEN ) );
		assertEquals( " ", property.getAttribute( LABEL ) );
		assertTrue( property.getAttributes().getLength() == 3 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "b", property.getAttribute( NAME ) );
		assertTrue( property.hasAttribute( LABEL ) );
		assertTrue( property.getAttributes().getLength() == 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "c", property.getAttribute( NAME ) );
		assertFalse( property.hasAttribute( LABEL ) );
		assertEquals( "Telephone, Mobile, Fax, E-mail", property.getAttribute( LOOKUP ) );
		assertTrue( property.getAttributes().getLength() == 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "d", property.getAttribute( NAME ) );
		assertTrue( property.getAttributes().getLength() == 1 );

		assertTrue( entity.getChildNodes().getLength() == 5 );
	}

	public void testTraverseViaParent() {

		Document document = XmlUtils.documentFromString( mInspector.inspect( null, "org.metawidget.inspector.xml.XmlInspectorTest$SubFoo", "bar" ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		Element entity = (Element) document.getFirstChild().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "Bar", entity.getAttribute( TYPE ) );
		assertEquals( "bar", entity.getAttribute( NAME ) );
		assertEquals( "true", entity.getAttribute( REQUIRED ) );

		// Properties

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "baz", property.getAttribute( NAME ) );

		property = (Element) property.getNextSibling();
		assertEquals( ACTION, property.getNodeName() );
		assertEquals( "doAction", property.getAttribute( NAME ) );

		assertTrue( entity.getChildNodes().getLength() == 2 );
	}

	public void testMissingType() {

		try {
			mInspector.inspect( null, "org.metawidget.inspector.xml.XmlInspectorTest$SubFoo", "bar", "baz" );
			assertTrue( false );
		} catch ( InspectorException e ) {
			assertTrue( e.getMessage().endsWith( "Property baz has no @type attribute in the XML, so cannot navigate to org.metawidget.inspector.xml.XmlInspectorTest$SubFoo.bar.baz" ) );
		}

		try {
			mInspector.inspect( null, "org.metawidget.inspector.xml.XmlInspectorTest$SubFoo", "bar", "baz", "abc" );
			assertTrue( false );
		} catch ( InspectorException e ) {
			assertTrue( e.getMessage().endsWith( "Property baz in entity Bar has no @type attribute in the XML, so cannot navigate to org.metawidget.inspector.xml.XmlInspectorTest$SubFoo.bar.baz.abc" ) );
		}
	}

	public void testNullType() {

		assertTrue( null == mInspector.inspect( null, (String) null ) );
	}

	public void testBadName() {

		assertEquals( mInspector.inspect( null, "no-such-type" ), null );
		assertEquals( mInspector.inspect( null, "org.metawidget.inspector.xml.XmlInspectorTest$SubFoo", "no-such-name" ), null );
		assertEquals( mInspector.inspect( null, "org.metawidget.inspector.xml.XmlInspectorTest$SubFoo", "no-such-parent-name", "foo" ), null );
	}

	public void testDefaultConfig() {

		try {
			new XmlInspector( new XmlInspectorConfig() );
			assertTrue( false );
		} catch ( InspectorException e ) {
			assertEquals( "java.io.FileNotFoundException: Unable to locate metawidget-metadata.xml on CLASSPATH", e.getMessage() );
		}
	}

	public void testTypos() {

		try {
			mInspector.inspect( null, "Typo1" );
			assertTrue( false );
		} catch ( InspectorException e ) {
			assertEquals( "Attribute named 'readonly' should be 'read-only'", e.getMessage() );
		}

		try {
			mInspector.inspect( null, "Typo2" );
			assertTrue( false );
		} catch ( InspectorException e ) {
			assertEquals( "Attribute named 'dontexpand' should be 'dont-expand'", e.getMessage() );
		}
	}

	public void testRestrictAgainstObject() {

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<inspection-result xmlns=\"http://www.metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.metawidget.org/inspection-result ../../inspector/inspection-result-1.0.xsd\" version=\"1.0\">";
		xml += "<entity type=\"ImaginaryObject\">";
		xml += "<property name=\"foo\" type=\"ImaginaryObject\"/>";
		xml += "</entity>";
		xml += "<entity type=\"org.metawidget.inspector.xml.XmlInspectorTest$NullObject\">";
		xml += "<property name=\"nestedNullObject\" type=\"org.metawidget.inspector.xml.XmlInspectorTest$NullObject\"/>";
		xml += "</entity>";
		xml += "</inspection-result>";

		// Without restrictAgainstObject

		XmlInspectorConfig config = new XmlInspectorConfig();
		config.setInputStream( new ByteArrayInputStream( xml.getBytes() ) );
		mInspector = new XmlInspector( config );

		assertEquals( null, mInspector.inspect( null, "MissingObject" ) );
		assertTrue( null != mInspector.inspect( null, "ImaginaryObject" ) );
		assertTrue( null != mInspector.inspect( null, NullObject.class.getName() ) );

		NullObject nullObject = new NullObject();
		assertTrue( null != mInspector.inspect( nullObject, NullObject.class.getName() ) );
		assertTrue( null != mInspector.inspect( nullObject, NullObject.class.getName(), "nestedNullObject" ) );

		// With restrictAgainstObject

		config.setRestrictAgainstObject( new JavaBeanPropertyStyle() );
		config.setInputStream( new ByteArrayInputStream( xml.getBytes() ) );
		mInspector = new XmlInspector( config );
		assertTrue( null != mInspector.inspect( null, "ImaginaryObject" ) );
		assertTrue( null != mInspector.inspect( null, NullObject.class.getName() ) );
		assertTrue( null != mInspector.inspect( nullObject, NullObject.class.getName() ) );
		assertTrue( null != mInspector.inspect( "", NullObject.class.getName() ) );
		assertEquals( null, mInspector.inspect( nullObject, NullObject.class.getName(), "nestedNullObject" ) );
		assertEquals( null, mInspector.inspect( nullObject, NullObject.class.getName(), "foo" ) );
		assertEquals( null, mInspector.inspect( null, NullObject.class.getName(), "nestedNullObject", "foo" ) );

		// With several levels deep

		nullObject.nestedNullObject = new NullObject();
		assertTrue( null != mInspector.inspect( nullObject, NullObject.class.getName(), "nestedNullObject" ) );
		assertEquals( null, mInspector.inspect( nullObject, NullObject.class.getName(), "nestedNullObject", "nestedNullObject" ) );

		// With recursion

		nullObject.nestedNullObject = nullObject;
		assertTrue( null != mInspector.inspect( nullObject, NullObject.class.getName() ) );
		assertTrue( null == mInspector.inspect( nullObject, NullObject.class.getName(), "nestedNullObject" ) );

		if ( LogUtils.getLog( XmlInspector.class ).isTraceEnabled() ) {
			assertEquals( "XmlInspector prevented infinite recursion on org.metawidget.inspector.xml.XmlInspectorTest$NullObject/nestedNullObject. Consider marking nestedNullObject as hidden='true'", LogUtilsTest.getLastTraceMessage() );
		} else {
			assertEquals( "{0} prevented infinite recursion on {1}{2}. Consider marking {3} as hidden=''true''", LogUtilsTest.getLastTraceMessage() );
			assertEquals( "XmlInspector", LogUtilsTest.getLastTraceArguments()[0] );
			assertEquals( "org.metawidget.inspector.xml.XmlInspectorTest$NullObject", LogUtilsTest.getLastTraceArguments()[1] );
			assertEquals( "/nestedNullObject", LogUtilsTest.getLastTraceArguments()[2] );
			assertEquals( "nestedNullObject", LogUtilsTest.getLastTraceArguments()[3] );
		}
	}

	//
	// Inner class
	//

	public static class NullObject {

		public NullObject	nestedNullObject;
	}
}
