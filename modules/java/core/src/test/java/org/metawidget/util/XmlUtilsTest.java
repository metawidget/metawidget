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

package org.metawidget.util;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.util.XmlUtils.CachingContentHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Richard Kennard
 */

public class XmlUtilsTest
	extends TestCase {

	//
	// Public methods
	//

	public void testMapAsAttributes() {

		// setMapAsAttributes

		Document document = XmlUtils.documentFromString( "<root foo=\"bar\"/>" );
		Element root = document.getDocumentElement();

		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( "foo", null );
		attributes.put( "baz", "abc" );

		XmlUtils.setMapAsAttributes( root, null );

		assertEquals( "bar", root.getAttribute( "foo" ) );
		assertTrue( !root.hasAttribute( "baz" ) );

		XmlUtils.setMapAsAttributes( root, attributes );

		assertTrue( !root.hasAttribute( "foo" ) );
		assertEquals( "abc", root.getAttribute( "baz" ) );

		// getAttributesAsMap

		attributes.remove( "foo" );
		assertEquals( attributes, XmlUtils.getAttributesAsMap( root ));

		attributes.remove( "foo" );
		root.removeAttribute( "baz" );
		assertTrue( XmlUtils.getAttributesAsMap( root ).isEmpty() );
	}

	public void testChildNamed() {

		assertEquals( null, XmlUtils.getChildNamed( null, (String[]) null ) );

		Document document = XmlUtils.documentFromString( "<root xmlns:xs=\"foo\">Blah<node1/>Blah<xs:node2/>Blah<xs:node3/>Blah</root>" );
		Element root = document.getDocumentElement();

		assertEquals( null, XmlUtils.getChildNamed( root, "node4" ) );

		Element element = XmlUtils.getFirstChildElement( root );
		assertEquals( "node1", element.getNodeName() );
		element = XmlUtils.getNextSiblingElement( element );
		assertEquals( "xs:node2", element.getNodeName() );
		element = XmlUtils.getNextSiblingElement( element );
		assertEquals( "xs:node3", element.getNodeName() );
		assertEquals( null, XmlUtils.getNextSiblingElement( element ) );

		// Test getChildNamed and getSiblingNamed are using getLocalName(), not getNodeName()

		element = XmlUtils.getChildNamed( root, "node2" );
		assertEquals( "xs:node2", element.getNodeName() );
		element = XmlUtils.getSiblingNamed( element, "node3" );
		assertEquals( "xs:node3", element.getNodeName() );
	}

	public void testChildWithAttribute() {

		assertEquals( null, XmlUtils.getChildWithAttribute( null, null ) );

		Document document = XmlUtils.documentFromString( "<root xmlns:xs=\"fooNamespace\">Blah<node1 foo=\"bar\"/>Blah<xs:node2/>Blah<xs:node3/>Blah</root>" );
		Element root = document.getDocumentElement();

		assertEquals( null, XmlUtils.getChildNamed( root, "baz" ) );

		Element element = XmlUtils.getChildWithAttribute( root, "foo" );
		assertEquals( "node1", element.getNodeName() );
		assertEquals( null, XmlUtils.getChildWithAttribute( element, "noChild" ));
	}

	public void testChildWithAttributeValue() {

		assertEquals( null, XmlUtils.getChildWithAttributeValue( null, null, null ) );

		Document document = XmlUtils.documentFromString( "<root xmlns:xs=\"fooNamespace\">Blah<node1 foo=\"bar\"/>Blah<xs:node2/>Blah<xs:node3/>Blah</root>" );
		Element root = document.getDocumentElement();

		assertEquals( null, XmlUtils.getChildWithAttributeValue( root, "foo", "baz" ) );

		Element element = XmlUtils.getChildWithAttributeValue( root, "foo", "bar" );
		assertEquals( "node1", element.getNodeName() );
	}

	public void testCachingContentHandler()
		throws Exception {

		SimpleContentHandler simpleContentHandler = new SimpleContentHandler();
		CachingContentHandler cachingContentHandler = new CachingContentHandler( simpleContentHandler );

		try {
			cachingContentHandler.replay( simpleContentHandler );
			fail();
		} catch ( SAXException e ) {
			assertEquals( "Nothing to replay. Not cached any SAX events", e.getMessage() );
		}

		// Check delegate ready

		Field delegateField = CachingContentHandler.class.getDeclaredField( "mDelegate" );
		delegateField.setAccessible( true );
		assertEquals( simpleContentHandler, delegateField.get( cachingContentHandler ) );

		// Fire events

		cachingContentHandler.startDocument();
		cachingContentHandler.processingInstruction( "pi-target", "pi-data" );
		cachingContentHandler.skippedEntity( "se-name" );
		cachingContentHandler.startPrefixMapping( "spm-prefix", "spm-uri" );
		cachingContentHandler.endPrefixMapping( "epm-prefix" );
		AttributesImpl attributes = new AttributesImpl();
		attributes.addAttribute( "a-uri", "a-localName", "a-qName", "a-type", "a-value" );
		cachingContentHandler.startElement( "se-uri", "se-localName", "se-name", attributes );
		char[] characters = "c-characters".toCharArray();
		cachingContentHandler.characters( characters, 0, 12 );
		char[] ignorableWhitespaceCharacters = "iw-characters".toCharArray();
		cachingContentHandler.ignorableWhitespace( ignorableWhitespaceCharacters, 0, 13 );
		cachingContentHandler.endElement( "ee-uri", "ee-localName", "ee-name" );
		cachingContentHandler.endDocument();

		// Check delegate field was released

		assertEquals( null, delegateField.get( cachingContentHandler ) );

		try {
			cachingContentHandler.startDocument();
			fail();
		} catch ( SAXException e ) {
			assertEquals( "Already cached SAX events. CachingContentHandler can only cache SAX events once", e.getMessage() );
		}

		// Check they got delegated

		assertSimpleContentHandler( simpleContentHandler );

		// Check defensive copy

		attributes.clear();

		for ( int loop = 0, length = characters.length; loop < length; loop++ ) {
			characters[loop] = '_';
		}

		for ( int loop = 0, length = ignorableWhitespaceCharacters.length; loop < length; loop++ ) {
			ignorableWhitespaceCharacters[loop] = '_';
		}

		// Replay

		SimpleContentHandler newSimpleContentHandler = new SimpleContentHandler();
		cachingContentHandler.replay( newSimpleContentHandler );

		// Check delegate field was not used

		assertEquals( null, delegateField.get( cachingContentHandler ) );

		// Check they got replayed

		assertSimpleContentHandler( newSimpleContentHandler );
	}

	public void testCachingContentHandlerFragment()
		throws Exception {

		SimpleContentHandler simpleContentHandler = new SimpleContentHandler();
		CachingContentHandler cachingContentHandler = new CachingContentHandler( simpleContentHandler );
		cachingContentHandler.startElement( "se-uri", "se-localName", "se-name", new AttributesImpl() );
		cachingContentHandler.endElement( "ee-uri", "ee-localName", "ee-name" );

		SimpleContentHandler newSimpleContentHandler = new SimpleContentHandler();

		try {
			cachingContentHandler.replay( newSimpleContentHandler );
			fail();
		} catch ( SAXException e ) {
			assertEquals( "Not ready to replay - ContentHandler delegate is non-null. Either endDocument must be triggered, or releaseDelegate must be called explicitly", e.getMessage() );
		}

		cachingContentHandler.releaseDelegate();
		cachingContentHandler.replay( newSimpleContentHandler );

		assertEquals( newSimpleContentHandler.mEvents.size(), 2 );
		assertEquals( "startElement", simpleContentHandler.mEvents.get( 0 )[0] );
		assertEquals( "se-uri", simpleContentHandler.mEvents.get( 0 )[1] );
		assertEquals( "se-localName", simpleContentHandler.mEvents.get( 0 )[2] );
		assertEquals( "se-name", simpleContentHandler.mEvents.get( 0 )[3] );
		assertEquals( 0, ( (Attributes) simpleContentHandler.mEvents.get( 0 )[4] ).getLength() );
		assertEquals( "endElement", simpleContentHandler.mEvents.get( 1 )[0] );
		assertEquals( "ee-uri", simpleContentHandler.mEvents.get( 1 )[1] );
		assertEquals( "ee-localName", simpleContentHandler.mEvents.get( 1 )[2] );
		assertEquals( "ee-name", simpleContentHandler.mEvents.get( 1 )[3] );
	}

	public void testToFromString() {

		assertEquals( "", XmlUtils.documentToString( null, false ) );

		Document document = XmlUtils.documentFromString( "<foo id=\"1\"><bar id=\"2\">Baz</bar></foo>" );
		assertEquals( "<foo id=\"1\"><bar id=\"2\">Baz</bar></foo>", XmlUtils.documentToString( document, false ) );
		assertEquals( "<foo id=\"1\">\n   <bar id=\"2\">Baz</bar>\n</foo>", XmlUtils.documentToString( document, true ) );

		document = XmlUtils.documentFromString( "<foo id1=\"1\" id2=\"2\">\n\t<bar>Baz</bar>\n</foo>" );
		assertEquals( "<foo id1=\"1\" id2=\"2\"><bar>Baz</bar></foo>", XmlUtils.documentToString( document, false ) );
		assertEquals( "<foo id1=\"1\" id2=\"2\">\n   <bar>Baz</bar>\n</foo>", XmlUtils.documentToString( document, true ) );

		document = XmlUtils.documentFromString( "<foo>\n\t<bar>Baz<abc>Abc</abc><def>Def</def></bar>\n</foo>" );
		assertEquals( "<foo><bar>Baz<abc>Abc</abc><def>Def</def></bar></foo>", XmlUtils.documentToString( document, false ) );
		assertEquals( "<foo>\n   <bar>Baz\n      <abc>Abc</abc>\n      <def>Def</def>\n   </bar>\n</foo>", XmlUtils.documentToString( document, true ) );

		document = XmlUtils.documentFromString( "<foo>&lt;&apos;&quot;&amp;&gt;</foo>" );
		assertEquals( "<foo>&lt;&apos;&quot;&amp;&gt;</foo>", XmlUtils.documentToString( document, false ) );
		assertEquals( "<foo>&lt;&apos;&quot;&amp;&gt;</foo>", XmlUtils.documentToString( document, true ) );
	}

	public void testCombineElements() {

		Document documentMaster = XmlUtils.documentFromString( "<inspection-result><foo fooAttr=\"1\"><bar barAttr=\"2\" data=\"bar2\"/></foo></inspection-result>" );

		// Missing @barAttr

		Document documentToAdd = XmlUtils.documentFromString( "<inspection-result><foo fooAttr=\"1\"><bar bazAttr=\"3\"/></foo></inspection-result>" );

		try {
			XmlUtils.combineElements( documentMaster.getDocumentElement(), documentToAdd.getDocumentElement(), "fooAttr", "barAttr" );
			fail();
		} catch ( Exception e ) {
			assertEquals( "Child node #1 has no @barAttr: <bar bazAttr=\"3\"/>", e.getMessage() );
		}

		// Non-aligned trees

		documentToAdd = XmlUtils.documentFromString( "<inspection-result><foo fooAttr=\"2\"><bar barAttr=\"3\" data=\"bar3\"/></foo></inspection-result>" );
		XmlUtils.combineElements( documentMaster.getDocumentElement(), documentToAdd.getDocumentElement(), "fooAttr", "barAttr" );
		assertEquals( "<inspection-result><foo fooAttr=\"1\"><bar barAttr=\"2\" data=\"bar2\"/></foo><foo fooAttr=\"2\"><bar barAttr=\"3\" data=\"bar3\"/></foo></inspection-result>", XmlUtils.documentToString( documentMaster, false ) );
	}

	public void testElementToJsonSchema() {

		// Normal case

		Document document = XmlUtils.documentFromString( "<inspection-result><entity name=\"root\" type=\"1\"><property name=\"bar\" barAttr=\"2\" data=\"bar2\"/><baz name=\"bazName\" bazAttr=\"3\"/><ignore ignoreMe=\"please\"/></entity></inspection-result>" );
		assertEquals( "{\"name\":\"root\",\"type\":\"1\",\"properties\":{\"bar\":{\"barAttr\":\"2\",\"data\":\"bar2\"},\"bazName\":{\"bazAttr\":\"3\"}}}", XmlUtils.elementToJsonSchema( document.getDocumentElement() ) );

		// Nested elements are ignored, root attributes are optional

		document = XmlUtils.documentFromString( "<inspection-result><entity><property name=\"bar\" barAttr=\"2\" data=\"bar2\"><property nestedAttr=\"3\"/></property></entity></inspection-result>" );
		assertEquals( "{\"properties\":{\"bar\":{\"barAttr\":\"2\",\"data\":\"bar2\"}}}", XmlUtils.elementToJsonSchema( document.getDocumentElement() ) );

		// Empty elements are okay

		document = XmlUtils.documentFromString( "<inspection-result/>" );
		assertEquals( "{}", XmlUtils.elementToJsonSchema( document.getDocumentElement() ) );

		// Test arrays

		document = XmlUtils.documentFromString( "<inspection-result><entity type=\"1\" enum=\"foo\\,bar,baz\"/>></inspection-result>" );
		assertEquals( "{\"enum\":[\"foo,bar\",\"baz\"],\"type\":\"1\"}", XmlUtils.elementToJsonSchema( document.getDocumentElement() ) );

		document = XmlUtils.documentFromString( "<inspection-result><entity type=\"1\" enum=\"1,2,3,4,5\"/>></inspection-result>" );
		assertEquals( "{\"enum\":[\"1\",\"2\",\"3\",\"4\",\"5\"],\"type\":\"1\"}", XmlUtils.elementToJsonSchema( document.getDocumentElement() ) );

		document = XmlUtils.documentFromString( "<inspection-result><entity type=\"1\" enum-titles=\"foo&quot;bar,baz\"/>></inspection-result>" );
		assertEquals( "{\"enumTitles\":[\"foo\\\"bar\",\"baz\"],\"type\":\"1\"}", XmlUtils.elementToJsonSchema( document.getDocumentElement() ) );

		document = XmlUtils.documentFromString( "<inspection-result><entity type=\"1\" section=\"foo\"/>></inspection-result>" );
		assertEquals( "{\"section\":[\"foo\"],\"type\":\"1\"}", XmlUtils.elementToJsonSchema( document.getDocumentElement() ) );
	}

	//
	// Private members
	//

	private void assertSimpleContentHandler( SimpleContentHandler simpleContentHandler ) {

		assertEquals( simpleContentHandler.mEvents.size(), 10 );
		assertEquals( "startDocument", simpleContentHandler.mEvents.get( 0 )[0] );
		assertEquals( "processingInstruction", simpleContentHandler.mEvents.get( 1 )[0] );
		assertEquals( "pi-target", simpleContentHandler.mEvents.get( 1 )[1] );
		assertEquals( "pi-data", simpleContentHandler.mEvents.get( 1 )[2] );
		assertEquals( "skippedEntity", simpleContentHandler.mEvents.get( 2 )[0] );
		assertEquals( "se-name", simpleContentHandler.mEvents.get( 2 )[1] );
		assertEquals( "startPrefixMapping", simpleContentHandler.mEvents.get( 3 )[0] );
		assertEquals( "spm-prefix", simpleContentHandler.mEvents.get( 3 )[1] );
		assertEquals( "spm-uri", simpleContentHandler.mEvents.get( 3 )[2] );
		assertEquals( "endPrefixMapping", simpleContentHandler.mEvents.get( 4 )[0] );
		assertEquals( "epm-prefix", simpleContentHandler.mEvents.get( 4 )[1] );
		assertEquals( "startElement", simpleContentHandler.mEvents.get( 5 )[0] );
		assertEquals( "se-uri", simpleContentHandler.mEvents.get( 5 )[1] );
		assertEquals( "se-localName", simpleContentHandler.mEvents.get( 5 )[2] );
		assertEquals( "se-name", simpleContentHandler.mEvents.get( 5 )[3] );
		assertEquals( "a-value", ( (Attributes) simpleContentHandler.mEvents.get( 5 )[4] ).getValue( "a-uri", "a-localName" ) );
		assertEquals( "a-type", ( (Attributes) simpleContentHandler.mEvents.get( 5 )[4] ).getType( "a-uri", "a-localName" ) );
		assertEquals( 1, ( (Attributes) simpleContentHandler.mEvents.get( 5 )[4] ).getLength() );
		assertEquals( "characters", simpleContentHandler.mEvents.get( 6 )[0] );
		assertEquals( "c-characters", String.valueOf( (char[]) simpleContentHandler.mEvents.get( 6 )[1] ) );
		assertTrue( 0 == (Integer) simpleContentHandler.mEvents.get( 6 )[2] );
		assertTrue( 12 == (Integer) simpleContentHandler.mEvents.get( 6 )[3] );
		assertEquals( "ignorableWhitespace", simpleContentHandler.mEvents.get( 7 )[0] );
		assertEquals( "iw-characters", String.valueOf( (char[]) simpleContentHandler.mEvents.get( 7 )[1] ) );
		assertTrue( 0 == (Integer) simpleContentHandler.mEvents.get( 7 )[2] );
		assertTrue( 13 == (Integer) simpleContentHandler.mEvents.get( 7 )[3] );
		assertEquals( "endElement", simpleContentHandler.mEvents.get( 8 )[0] );
		assertEquals( "ee-uri", simpleContentHandler.mEvents.get( 8 )[1] );
		assertEquals( "ee-localName", simpleContentHandler.mEvents.get( 8 )[2] );
		assertEquals( "ee-name", simpleContentHandler.mEvents.get( 8 )[3] );
		assertEquals( "endDocument", simpleContentHandler.mEvents.get( 9 )[0] );
	}

	//
	// Inner class
	//

	/* package private */static class SimpleContentHandler
		extends DefaultHandler {

		//
		// Public members
		//

		public List<Object[]>	mEvents	= CollectionUtils.newArrayList();

		//
		// Public methods
		//

		@Override
		public void startDocument() {

			mEvents.add( new Object[] { "startDocument" } );
		}

		@Override
		public void processingInstruction( String target, String data ) {

			mEvents.add( new Object[] { "processingInstruction", target, data } );
		}

		@Override
		public void skippedEntity( String name ) {

			mEvents.add( new Object[] { "skippedEntity", name } );
		}

		@Override
		public void startPrefixMapping( String prefix, String uri ) {

			mEvents.add( new Object[] { "startPrefixMapping", prefix, uri } );
		}

		@Override
		public void endPrefixMapping( String prefix ) {

			mEvents.add( new Object[] { "endPrefixMapping", prefix } );
		}

		@Override
		public void startElement( String uri, String localName, String name, Attributes attributes ) {

			mEvents.add( new Object[] { "startElement", uri, localName, name, new AttributesImpl( attributes ) } );
		}

		@Override
		public void characters( char[] characters, int start, int length ) {

			mEvents.add( new Object[] { "characters", characters, start, length } );
		}

		@Override
		public void ignorableWhitespace( char[] characters, int start, int length ) {

			mEvents.add( new Object[] { "ignorableWhitespace", characters, start, length } );
		}

		@Override
		public void endElement( String uri, String localName, String name ) {

			mEvents.add( new Object[] { "endElement", uri, localName, name } );
		}

		@Override
		public void endDocument() {

			mEvents.add( new Object[] { "endDocument" } );
		}
	}
}
