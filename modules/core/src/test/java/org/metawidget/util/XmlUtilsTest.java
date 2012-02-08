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

package org.metawidget.util;

import java.lang.reflect.Field;
import java.util.List;

import junit.framework.TestCase;

import org.metawidget.util.XmlUtils.CachingContentHandler;
import org.w3c.dom.Document;
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

	public void testCachingContentHandler()
		throws Exception {

		SimpleContentHandler simpleContentHandler = new SimpleContentHandler();
		CachingContentHandler cachingContentHandler = new CachingContentHandler( simpleContentHandler );

		try {
			cachingContentHandler.replay( simpleContentHandler );
			assertTrue( false );
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
			assertTrue( false );
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
			assertTrue( false );
		} catch( SAXException e  ) {
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
			assertTrue( false );
		} catch( Exception e ) {
			assertEquals( "Child node #1 has no @barAttr: <bar bazAttr=\"3\"/>", e.getMessage() );
		}

		// Non-aligned trees

		documentToAdd = XmlUtils.documentFromString( "<inspection-result><foo fooAttr=\"2\"><bar barAttr=\"3\" data=\"bar3\"/></foo></inspection-result>" );
		XmlUtils.combineElements( documentMaster.getDocumentElement(), documentToAdd.getDocumentElement(), "fooAttr", "barAttr" );
		assertEquals( "<inspection-result><foo fooAttr=\"1\"><bar barAttr=\"2\" data=\"bar2\"/></foo><foo fooAttr=\"2\"><bar barAttr=\"3\" data=\"bar3\"/></foo></inspection-result>", XmlUtils.documentToString( documentMaster, false ) );
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
