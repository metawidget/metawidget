// Metawidget (licensed under LGPL)
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
import java.util.List;
import java.util.concurrent.CountDownLatch;

import junit.framework.TestCase;

import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyle;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyleConfig;
import org.metawidget.util.CollectionUtils;
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

	private String			mXml;

	private XmlInspector	mInspector;

	//
	// Public methods
	//

	@Override
	public void setUp() {

		mXml = "<?xml version=\"1.0\"?>";
		mXml += "<inspection-result xmlns=\"http://www.metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.metawidget.org/inspection-result ../../inspector/inspection-result-1.0.xsd\" version=\"1.0\">";
		mXml += "<entity type=\"org.metawidget.inspector.xml.XmlInspectorTest$SuperSuperFoo\">";
		mXml += "<property name=\"bar\" type=\"Bar\" required=\"true\"/>";
		mXml += "<property name=\"a\"/>";
		mXml += "<property name=\"d\"/>";
		mXml += "</entity>";
		mXml += "<entity type=\"org.metawidget.inspector.xml.XmlInspectorTest$SuperFoo\" extends=\"org.metawidget.inspector.xml.XmlInspectorTest$SuperSuperFoo\"/>";
		mXml += "<entity type=\"org.metawidget.inspector.xml.XmlInspectorTest$SubFoo\" extends=\"org.metawidget.inspector.xml.XmlInspectorTest$SuperFoo\">";
		mXml += "<property name=\"a\" hidden=\"true\" label=\" \"/>";
		mXml += "<property name=\"b\" label=\"\"/>";
		mXml += "<property name=\"c\" lookup=\"Telephone, Mobile, Fax, E-mail\"/>";
		mXml += "</entity>";
		mXml += "<entity type=\"Bar\">";
		mXml += "<property name=\"baz\"/>";
		mXml += "<action name=\"doAction\"/>";
		mXml += "<some-junk name=\"ignoreMe\"/>";
		mXml += "</entity>";
		mXml += "<entity type=\"Typo1\">";
		mXml += "<property name=\"foo\" readonly=\"true\"/>";
		mXml += "</entity>";
		mXml += "<entity type=\"Typo2\">";
		mXml += "<property name=\"foo\" dontexpand=\"true\"/>";
		mXml += "</entity>";
		mXml += "</inspection-result>";

		mInspector = new XmlInspector( new XmlInspectorConfig().setInputStream( new ByteArrayInputStream( mXml.getBytes() ) ) );
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

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "org.metawidget.inspector.xml.XmlInspectorTest$SubFoo", entity.getAttribute( TYPE ) );
		assertFalse( entity.hasAttribute( NAME ) );

		// Properties

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "bar", property.getAttribute( NAME ) );
		assertEquals( "Bar", property.getAttribute( TYPE ) );
		assertEquals( TRUE, property.getAttribute( REQUIRED ) );
		assertEquals( property.getAttributes().getLength(), 3 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "a", property.getAttribute( NAME ) );
		assertEquals( TRUE, property.getAttribute( HIDDEN ) );
		assertEquals( " ", property.getAttribute( LABEL ) );
		assertEquals( property.getAttributes().getLength(), 3 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "b", property.getAttribute( NAME ) );
		assertTrue( property.hasAttribute( LABEL ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "c", property.getAttribute( NAME ) );
		assertFalse( property.hasAttribute( LABEL ) );
		assertEquals( "Telephone, Mobile, Fax, E-mail", property.getAttribute( LOOKUP ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "d", property.getAttribute( NAME ) );
		assertEquals( property.getAttributes().getLength(), 1 );

		assertEquals( entity.getChildNodes().getLength(), 5 );
	}

	public void testTraverseViaParent() {

		Document document = XmlUtils.documentFromString( mInspector.inspect( null, "org.metawidget.inspector.xml.XmlInspectorTest$SubFoo", "bar" ) );

		assertEquals( "inspection-result", document.getFirstChild().getNodeName() );

		Element entity = (Element) document.getDocumentElement().getFirstChild();
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

		assertEquals( entity.getChildNodes().getLength(), 2 );

		// No extends support

		XmlInspector inspector = new XmlInspector( new XmlInspectorConfig().setInputStream( new ByteArrayInputStream( mXml.getBytes() ) ) )
		{

			@Override
			protected String getExtendsAttribute() {

				return null;
			}
		};
		assertEquals( null, XmlUtils.documentFromString( inspector.inspect( null, "org.metawidget.inspector.xml.XmlInspectorTest$SubFoo", "bar" ) ) );
	}

	public void testMissingType() {

		try {
			mInspector.inspect( null, "org.metawidget.inspector.xml.XmlInspectorTest$SubFoo", "bar", "baz" );
			fail();
		} catch ( InspectorException e ) {
			assertTrue( e.getMessage().endsWith( "Property baz in entity Bar has no @type attribute in the XML, so cannot navigate to org.metawidget.inspector.xml.XmlInspectorTest$SubFoo/bar/baz" ) );
		}

		try {
			mInspector.inspect( null, "org.metawidget.inspector.xml.XmlInspectorTest$SubFoo", "bar", "baz", "abc" );
			fail();
		} catch ( InspectorException e ) {
			assertTrue( e.getMessage().endsWith( "Property baz in entity Bar has no @type attribute in the XML, so cannot navigate to org.metawidget.inspector.xml.XmlInspectorTest$SubFoo/bar/baz/abc" ) );
		}
	}

	public void testNullType() {

		assertEquals( null, mInspector.inspect( null, (String) null ) );
	}

	public void testBadName() {

		assertEquals( mInspector.inspect( null, "no-such-type" ), null );
		assertEquals( mInspector.inspect( null, "org.metawidget.inspector.xml.XmlInspectorTest$SubFoo", "no-such-name" ), null );
		assertEquals( mInspector.inspect( null, "org.metawidget.inspector.xml.XmlInspectorTest$SubFoo", "no-such-parent-name", "foo" ), null );
	}

	@SuppressWarnings( "unused" )
	public void testDefaultConfig() {

		try {
			new XmlInspector( new XmlInspectorConfig() );
			fail();
		} catch ( InspectorException e ) {
			assertEquals( "java.io.FileNotFoundException: Unable to locate metawidget-metadata.xml on CLASSPATH", e.getMessage() );
		}
	}

	public void testTypos() {

		try {
			mInspector.inspect( null, "Typo1" );
			fail();
		} catch ( InspectorException e ) {
			assertEquals( "Attribute named 'readonly' should be 'read-only'", e.getMessage() );
		}

		try {
			mInspector.inspect( null, "Typo2" );
			fail();
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
		xml += "<entity type=\"" + NullObject.class.getName() + "\">";
		xml += "<property name=\"nestedNullObject\" type=\"org.metawidget.inspector.xml.XmlInspectorTest$NullObject\"/>";
		xml += "</entity>";
		xml += "</inspection-result>";

		// Without restrictAgainstObject

		mInspector = new XmlInspector( new XmlInspectorConfig().setInputStream( new ByteArrayInputStream( xml.getBytes() ) ) );

		assertEquals( null, mInspector.inspect( null, "MissingObject" ) );
		assertTrue( null != mInspector.inspect( null, "ImaginaryObject" ) );
		assertTrue( null != mInspector.inspect( null, NullObject.class.getName() ) );

		NullObject nullObject = new NullObject();
		assertTrue( null != mInspector.inspect( nullObject, NullObject.class.getName() ) );
		assertTrue( null != mInspector.inspect( nullObject, NullObject.class.getName(), "nestedNullObject" ) );

		// With restrictAgainstObject

		mInspector = new XmlInspector( new XmlInspectorConfig().setRestrictAgainstObject( new JavaBeanPropertyStyle( new JavaBeanPropertyStyleConfig().setSupportPublicFields( true ) ) ).setInputStream( new ByteArrayInputStream( xml.getBytes() ) ) );
		assertTrue( null != mInspector.inspect( null, "ImaginaryObject" ) );
		assertTrue( null != mInspector.inspect( null, NullObject.class.getName() ) );
		assertTrue( null != mInspector.inspect( nullObject, NullObject.class.getName() ) );
		assertEquals( null, mInspector.inspect( "", NullObject.class.getName() ) );
		assertEquals( mInspector.inspect( nullObject, NullObject.class.getName(), "nestedNullObject" ), "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" version=\"1.0\"><entity name=\"nestedNullObject\" type=\"org.metawidget.inspector.xml.XmlInspectorTest$NullObject\"/></inspection-result>" );
		assertEquals( null, mInspector.inspect( nullObject, NullObject.class.getName(), "foo" ) );
		assertEquals( null, mInspector.inspect( null, NullObject.class.getName(), "nestedNullObject", "foo" ) );

		// With several levels deep

		nullObject.setNestedNullObject( new NullObject() );
		assertEquals( mInspector.inspect( nullObject, NullObject.class.getName(), "nestedNullObject" ), "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" version=\"1.0\"><entity name=\"nestedNullObject\" type=\"org.metawidget.inspector.xml.XmlInspectorTest$NullObject\"><property name=\"nestedNullObject\" type=\"org.metawidget.inspector.xml.XmlInspectorTest$NullObject\"/></entity></inspection-result>" );
		assertEquals( mInspector.inspect( nullObject, NullObject.class.getName(), "nestedNullObject", "nestedNullObject" ), "<inspection-result xmlns=\"http://metawidget.org/inspection-result\" version=\"1.0\"><entity name=\"nestedNullObject\" type=\"org.metawidget.inspector.xml.XmlInspectorTest$NullObject\"/></inspection-result>" );
		assertEquals( null, mInspector.inspect( nullObject, NullObject.class.getName(), "nestedNullObject", "nestedNullObject", "nestedNullObject" ) );

		// With recursion

		nullObject.setNestedNullObject( nullObject );
		assertTrue( null != mInspector.inspect( nullObject, NullObject.class.getName() ) );
		assertEquals( null, mInspector.inspect( nullObject, NullObject.class.getName(), "nestedNullObject" ) );

		if ( LogUtils.getLog( XmlInspector.class ).isTraceEnabled() ) {
			assertEquals( "XmlInspector prevented infinite recursion on org.metawidget.inspector.xml.XmlInspectorTest$NullObject/nestedNullObject. Consider marking nestedNullObject as hidden='true'", LogUtilsTest.getLastTraceMessage() );
		} else {
			assertEquals( "Prevented infinite recursion on {0}{1}. Consider marking {2} as hidden", LogUtilsTest.getLastTraceMessage() );
			assertEquals( "org.metawidget.inspector.xml.XmlInspectorTest$NullObject", LogUtilsTest.getLastTraceArguments()[0] );
			assertEquals( "/nestedNullObject", LogUtilsTest.getLastTraceArguments()[1] );
			assertEquals( "nestedNullObject", LogUtilsTest.getLastTraceArguments()[2] );
		}
	}

	public void testInferInheritanceHierarchy() {

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<inspection-result xmlns=\"http://www.metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.metawidget.org/inspection-result ../../inspector/inspection-result-1.0.xsd\" version=\"1.0\">";
		xml += "<entity type=\"" + RestrictAgainstObjectFoo.class.getName() + "\">";
		xml += "<property name=\"xmlBar\" type=\"int\"/>";
		xml += "</entity>";
		xml += "<entity type=\"org.metawidget.inspector.xml.XmlInspectorTest$SubSubRestrictAgainstObjectFoo\" extends=\"org.metawidget.inspector.xml.XmlInspectorTest$SubRestrictAgainstObjectFoo\">";
		xml += "<property name=\"xmlSubSubBar\" type=\"boolean\"/>";
		xml += "</entity>";
		xml += "</inspection-result>";

		// Without inferInheritanceHierarchy

		mInspector = new XmlInspector( new XmlInspectorConfig().setInputStream( new ByteArrayInputStream( xml.getBytes() ) ) );
		assertEquals( null, mInspector.inspect( null, SubRestrictAgainstObjectFoo.class.getName() ) );

		Document document = XmlUtils.documentFromString( mInspector.inspect( null, "org.metawidget.inspector.xml.XmlInspectorTest$SubSubRestrictAgainstObjectFoo" ) );
		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "org.metawidget.inspector.xml.XmlInspectorTest$SubSubRestrictAgainstObjectFoo", entity.getAttribute( TYPE ) );
		assertFalse( entity.hasAttribute( NAME ) );

		// Properties

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "xmlSubSubBar", property.getAttribute( NAME ) );
		assertEquals( "boolean", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		assertEquals( entity.getChildNodes().getLength(), 1 );

		// With inferInheritanceHierarchy

		// Against a missing top-level entity

		mInspector = new XmlInspector( new XmlInspectorConfig().setInferInheritanceHierarchy( true ).setInputStream( new ByteArrayInputStream( xml.getBytes() ) ) );
		document = XmlUtils.documentFromString( mInspector.inspect( null, SubRestrictAgainstObjectFoo.class.getName() ) );

		// Entity

		entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );

		// (should be the top-level type, so as to align the XML with other Inspectors)

		assertEquals( SubRestrictAgainstObjectFoo.class.getName(), entity.getAttribute( TYPE ) );
		assertFalse( entity.hasAttribute( NAME ) );

		// Properties

		property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "xmlBar", property.getAttribute( NAME ) );
		assertEquals( "int", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		assertEquals( entity.getChildNodes().getLength(), 1 );

		// Against a missing middle-level entity

		document = XmlUtils.documentFromString( mInspector.inspect( null, "org.metawidget.inspector.xml.XmlInspectorTest$SubSubRestrictAgainstObjectFoo" ) );

		// Entity

		entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );

		// (should be the top-level type, so as to align the XML with other Inspectors)

		assertEquals( "org.metawidget.inspector.xml.XmlInspectorTest$SubSubRestrictAgainstObjectFoo", entity.getAttribute( TYPE ) );
		assertFalse( entity.hasAttribute( NAME ) );

		// Properties

		property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "xmlBar", property.getAttribute( NAME ) );
		assertEquals( "int", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "xmlSubSubBar", property.getAttribute( NAME ) );
		assertEquals( "boolean", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		assertEquals( entity.getChildNodes().getLength(), 2 );

		// Against a fake entity

		assertEquals( null, XmlUtils.documentFromString( mInspector.inspect( null, "Fake Entity" ) ) );
	}

	public void testValidateAgainstClasses() {

		// First entity is good, second has a bad name

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<inspection-result xmlns=\"http://www.metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.metawidget.org/inspection-result ../../inspector/inspection-result-1.0.xsd\" version=\"1.0\">";
		xml += "<entity type=\"" + NullObject.class.getName() + "\">";
		xml += "<property name=\"nestedNullObject\"/>";
		xml += "</entity>";
		xml += "<entity type=\"" + RestrictAgainstObjectFoo.class.getName() + "\">";
		xml += "<property name=\"baz\"/>";
		xml += "</entity>";
		xml += "</inspection-result>";

		XmlInspectorConfig config = new XmlInspectorConfig().setRestrictAgainstObject( new JavaBeanPropertyStyle() ).setValidateAgainstClasses( new JavaBeanPropertyStyle() );

		try {
			mInspector = new XmlInspector( config.setInputStream( new ByteArrayInputStream( xml.getBytes() ) ) );
			fail();
		} catch ( InspectorException e ) {
			assertEquals( "class org.metawidget.inspector.xml.XmlInspectorTest$RestrictAgainstObjectFoo does not define a property 'baz'", e.getMessage() );
		}

		// Bad type

		xml = "<?xml version=\"1.0\"?>";
		xml += "<inspection-result xmlns=\"http://www.metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.metawidget.org/inspection-result ../../inspector/inspection-result-1.0.xsd\" version=\"1.0\">";
		xml += "<entity type=\"" + NullObject.class.getName() + "\">";
		xml += "<property name=\"nestedNullObject\" type=\"int\"/>";
		xml += "</entity>";
		xml += "</inspection-result>";

		try {
			mInspector = new XmlInspector( config.setInputStream( new ByteArrayInputStream( xml.getBytes() ) ) );
			fail();
		} catch ( InspectorException e ) {
			assertEquals( "class org.metawidget.inspector.xml.XmlInspectorTest$NullObject defines property 'nestedNullObject' to be org.metawidget.inspector.xml.XmlInspectorTest$NullObject, not 'int'", e.getMessage() );
		}

		// Good extends

		xml = "<?xml version=\"1.0\"?>";
		xml += "<inspection-result xmlns=\"http://www.metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.metawidget.org/inspection-result ../../inspector/inspection-result-1.0.xsd\" version=\"1.0\">";
		xml += "<entity type=\"" + SubRestrictAgainstObjectFoo.class.getName() + "\" extends=\"" + RestrictAgainstObjectFoo.class.getName() + "\"/>";
		xml += "</inspection-result>";

		mInspector = new XmlInspector( config.setInputStream( new ByteArrayInputStream( xml.getBytes() ) ) );

		// Bad extends

		xml = "<?xml version=\"1.0\"?>";
		xml += "<inspection-result xmlns=\"http://www.metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.metawidget.org/inspection-result ../../inspector/inspection-result-1.0.xsd\" version=\"1.0\">";
		xml += "<entity type=\"" + SubRestrictAgainstObjectFoo.class.getName() + "\" extends=\"Bar\"/>";
		xml += "</inspection-result>";

		try {
			mInspector = new XmlInspector( config.setInputStream( new ByteArrayInputStream( xml.getBytes() ) ) );
			fail();
		} catch ( InspectorException e ) {
			assertEquals( "class org.metawidget.inspector.xml.XmlInspectorTest$SubRestrictAgainstObjectFoo extends class org.metawidget.inspector.xml.XmlInspectorTest$RestrictAgainstObjectFoo, not 'Bar'", e.getMessage() );
		}
	}

	public void testTraverseAgainstObject() {

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<inspection-result xmlns=\"http://www.metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.metawidget.org/inspection-result ../../inspector/inspection-result-1.0.xsd\" version=\"1.0\">";
		xml += "<entity type=\"" + RestrictAgainstObjectFoo.class.getName() + "\">";
		xml += "<property name=\"xmlBar\" type=\"int\"/>";
		xml += "</entity>";
		xml += "</inspection-result>";

		TraverseAgainstObjectFoo traverseAgainstObjectFoo = new TraverseAgainstObjectFoo();
		traverseAgainstObjectFoo.toTraverse = new RestrictAgainstObjectFoo();

		// Without traverseAgainstObject

		mInspector = new XmlInspector( new XmlInspectorConfig().setInputStream( new ByteArrayInputStream( xml.getBytes() ) ) );
		assertEquals( null, mInspector.inspect( traverseAgainstObjectFoo, TraverseAgainstObjectFoo.class.getName(), "toTraverse" ) );

		// With traverseAgainstObject

		XmlInspectorConfig config = new XmlInspectorConfig().setRestrictAgainstObject( new JavaBeanPropertyStyle( new JavaBeanPropertyStyleConfig().setSupportPublicFields( true ) ) );
		mInspector = new XmlInspector( config.setInputStream( new ByteArrayInputStream( xml.getBytes() ) ) );
		Document document = XmlUtils.documentFromString( mInspector.inspect( traverseAgainstObjectFoo, TraverseAgainstObjectFoo.class.getName(), "toTraverse" ) );

		// Entity

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );

		// (should be the parent property type, so as to align the XML with other Inspectors)

		assertEquals( Object.class.getName(), entity.getAttribute( TYPE ) );
		assertFalse( entity.hasAttribute( NAME ) );

		// Properties

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "xmlBar", property.getAttribute( NAME ) );
		assertEquals( "int", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		assertEquals( entity.getChildNodes().getLength(), 1 );

		// Traverse to a subtype

		traverseAgainstObjectFoo.toTraverse = new SubRestrictAgainstObjectFoo();

		mInspector = new XmlInspector( config.setInputStream( new ByteArrayInputStream( xml.getBytes() ) ) );
		document = XmlUtils.documentFromString( mInspector.inspect( traverseAgainstObjectFoo, TraverseAgainstObjectFoo.class.getName(), "toTraverse" ) );

		// Entity

		entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );

		// (should be the parent property type, so as to align the XML with other Inspectors)

		assertEquals( Object.class.getName(), entity.getAttribute( TYPE ) );
		assertFalse( entity.hasAttribute( NAME ) );

		// Properties

		property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "xmlBar", property.getAttribute( NAME ) );
		assertEquals( "int", property.getAttribute( TYPE ) );
		assertEquals( property.getAttributes().getLength(), 2 );

		assertEquals( entity.getChildNodes().getLength(), 1 );
	}

	public void testTraverseAgainstObjectImpliesType() {

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<inspection-result xmlns=\"http://www.metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.metawidget.org/inspection-result ../../inspector/inspection-result-1.0.xsd\" version=\"1.0\">";
		xml += "<entity type=\"" + TraverseAgainstObjectFoo.class.getName() + "\">";
		xml += "<property name=\"toTraverse\"/>";
		xml += "</entity>";
		xml += "</inspection-result>";

		TraverseAgainstObjectFoo traverseAgainstObjectFoo = new TraverseAgainstObjectFoo();
		traverseAgainstObjectFoo.toTraverse = new TraverseAgainstObjectFoo();

		// Top level

		XmlInspectorConfig config = new XmlInspectorConfig().setRestrictAgainstObject( new JavaBeanPropertyStyle( new JavaBeanPropertyStyleConfig().setSupportPublicFields( true ) ) );
		mInspector = new XmlInspector( config.setInputStream( new ByteArrayInputStream( xml.getBytes() ) ) );

		Document document = XmlUtils.documentFromString( mInspector.inspect( traverseAgainstObjectFoo, TraverseAgainstObjectFoo.class.getName() ) );

		// Entity

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( TraverseAgainstObjectFoo.class.getName(), entity.getAttribute( TYPE ) );
		assertEquals( 1, entity.getAttributes().getLength() );

		// Property

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "toTraverse", property.getAttribute( NAME ) );

		// (shouldn't need @type on properties, as XmlUtils.combineElements uses the @name)

		assertTrue( !property.hasAttribute( TYPE ) );
		assertEquals( 1, property.getAttributes().getLength() );

		assertEquals( 1, entity.getChildNodes().getLength() );

		// Sub-level

		document = XmlUtils.documentFromString( mInspector.inspect( traverseAgainstObjectFoo, TraverseAgainstObjectFoo.class.getName(), "toTraverse" ) );

		// Entity

		entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( Object.class.getName(), entity.getAttribute( TYPE ) );
		assertEquals( "toTraverse", entity.getAttribute( NAME ) );
		assertEquals( 2, entity.getAttributes().getLength() );

		// Property

		property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "toTraverse", property.getAttribute( NAME ) );
		assertTrue( !property.hasAttribute( TYPE ) );
		assertEquals( 1, property.getAttributes().getLength() );

		assertEquals( 1, entity.getChildNodes().getLength() );
	}

	public void testRestrictAgainstObjectImpliesInferInheritanceHeirarchy() {

		String xml = "<?xml version=\"1.0\"?><inspection-result />";

		XmlInspectorConfig config = new XmlInspectorConfig();
		config.setRestrictAgainstObject( new JavaBeanPropertyStyle() );
		config.setInferInheritanceHierarchy( true );
		config.setInputStream( new ByteArrayInputStream( xml.getBytes() ) );

		try {
			mInspector = new XmlInspector( config );
		} catch ( InspectorException e ) {
			assertEquals( "When using restrictAgainstObject, inferInheritanceHierarchy is implied", e.getMessage() );
		}
	}

	/**
	 * Test BaseXmlInspector under high concurrency.
	 * <p>
	 * As <a href="https://issues.apache.org/jira/browse/XERCESJ-727">pointed out here</a>: "There's
	 * no requirement that a DOM be thread safe, so applications need to make sure that threads are
	 * properly synchronized for concurrent access to [a shared] DOM. This is true even if you're
	 * just invoking read operations".
	 */

	public void testConcurrency()
		throws Exception {

		final List<Exception> concurrencyFailures = CollectionUtils.newArrayList();

		// Try a few times (just to make sure)...

		for ( int tryAFewTimes = 0; tryAFewTimes < 10; tryAFewTimes++ ) {

			// ...prepare some Threads...

			final CountDownLatch startSignal = new CountDownLatch( 1 );
			final CountDownLatch doneSignal = new CountDownLatch( 50 );

			for ( int concurrentThreads = 0; concurrentThreads < doneSignal.getCount(); concurrentThreads++ ) {

				new Thread( new Runnable() {

					public void run() {

						try {
							startSignal.await();
						} catch ( InterruptedException e ) {
							// (do nothing)
						}

						try {
							testInspection();
						} catch ( Exception e ) {
							concurrencyFailures.add( e );
							assertTrue( "Concurrency failure: " + e.getClass() + " " + e.getMessage(), false );
						} finally {
							doneSignal.countDown();
						}
					}
				} ).start();
			}

			// ...and run them all simultaneously

			startSignal.countDown();
			doneSignal.await();

			if ( !concurrencyFailures.isEmpty() ) {
				break;
			}
		}

		assertTrue( concurrencyFailures.isEmpty() );
	}

	public void testTraversalToNullTopLevelElement() {

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<inspection-result xmlns=\"http://www.metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.metawidget.org/inspection-result ../../inspector/inspection-result-1.0.xsd\" version=\"1.0\">";
		xml += "<entity type=\"Foo\">";
		xml += "<property name=\"bar\" type=\"Bar\"/>";
		xml += "<property name=\"baz\" type=\"Baz\"/>";
		xml += "</entity>";
		xml += "<entity type=\"Baz\">";
		xml += "<property name=\"abc\" type=\"Abc\"/>";
		xml += "</entity>";
		xml += "<entity type=\"Abc\">";
		xml += "<property name=\"def\" type=\"Def\"/>";
		xml += "</entity>";
		xml += "</inspection-result>";

		// Top level

		XmlInspectorConfig config = new XmlInspectorConfig();
		mInspector = new XmlInspector( config.setInputStream( new ByteArrayInputStream( xml.getBytes() ) ) );

		Document document = XmlUtils.documentFromString( mInspector.inspect( null, "Foo" ) );

		Element entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "Foo", entity.getAttribute( TYPE ) );
		assertEquals( 1, entity.getAttributes().getLength() );

		Element property = (Element) entity.getFirstChild();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "bar", property.getAttribute( NAME ) );
		assertEquals( "Bar", property.getAttribute( TYPE ) );
		assertEquals( 2, property.getAttributes().getLength() );

		property = (Element) property.getNextSibling();
		assertEquals( PROPERTY, property.getNodeName() );
		assertEquals( "baz", property.getAttribute( NAME ) );
		assertEquals( "Baz", property.getAttribute( TYPE ) );
		assertEquals( 2, property.getAttributes().getLength() );

		assertEquals( 2, entity.getChildNodes().getLength() );

		// Missing type

		document = XmlUtils.documentFromString( mInspector.inspect( null, "Foo", "bar" ) );

		entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "bar", entity.getAttribute( NAME ) );
		assertEquals( "Bar", entity.getAttribute( TYPE ) );
		assertEquals( 2, entity.getAttributes().getLength() );
		assertEquals( 0, entity.getChildNodes().getLength() );

		// Missing type after traverse from top

		document = XmlUtils.documentFromString( mInspector.inspect( null, "Foo", "baz" ) );

		entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "baz", entity.getAttribute( NAME ) );
		assertEquals( "Baz", entity.getAttribute( TYPE ) );
		assertEquals( 2, entity.getAttributes().getLength() );
		assertEquals( 1, entity.getChildNodes().getLength() );

		mInspector = new XmlInspector( config.setInputStream( new ByteArrayInputStream( xml.getBytes() ) ) ) {

			@Override
			protected Element traverseFromTopLevelTypeToNamedChildren( Element topLevel ) {

				return null;
			}
		};

		document = XmlUtils.documentFromString( mInspector.inspect( null, "Foo" ) );
		assertEquals( null, document );

		document = XmlUtils.documentFromString( mInspector.inspect( null, "Foo", "baz" ) );
		assertEquals( null, document );

		// Missing type after traverse from top (one level down)

		mInspector = new XmlInspector( config.setInputStream( new ByteArrayInputStream( xml.getBytes() ) ) );
		document = XmlUtils.documentFromString( mInspector.inspect( null, "Foo", "baz", "abc" ) );

		entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "abc", entity.getAttribute( NAME ) );
		assertEquals( "Abc", entity.getAttribute( TYPE ) );
		assertEquals( 2, entity.getAttributes().getLength() );
		assertEquals( 1, entity.getChildNodes().getLength() );

		mInspector = new XmlInspector( config.setInputStream( new ByteArrayInputStream( xml.getBytes() ) ) ) {

			@Override
			protected Element traverseFromTopLevelTypeToNamedChildren( Element topLevel ) {

				if ( "Abc".equals( topLevel.getAttribute( TYPE ))) {
					return null;
				}

				return topLevel;
			}
		};

		document = XmlUtils.documentFromString( mInspector.inspect( null, "Foo", "baz", "abc" ) );

		entity = (Element) document.getDocumentElement().getFirstChild();
		assertEquals( ENTITY, entity.getNodeName() );
		assertEquals( "abc", entity.getAttribute( NAME ) );
		assertEquals( "Abc", entity.getAttribute( TYPE ) );
		assertEquals( 2, entity.getAttributes().getLength() );
		assertEquals( 0, entity.getChildNodes().getLength() );
	}

	//
	// Inner class
	//

	public static class NullObject {

		private NullObject	mNestedNullObject;

		public NullObject getNestedNullObject() {

			return mNestedNullObject;
		}

		public void setNestedNullObject( NullObject nestedNullObject ) {

			this.mNestedNullObject = nestedNullObject;
		}
	}

	public static class RestrictAgainstObjectFoo {

		public String	bar;
	}

	public static class SubRestrictAgainstObjectFoo
		extends RestrictAgainstObjectFoo {

		public String	subBar;
	}

	public static class TraverseAgainstObjectFoo {

		public Object	toTraverse;
	}
}
