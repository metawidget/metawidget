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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Utilities for working with XML.
 *
 * @author Richard Kennard
 */

public class XmlUtils {

	//
	// Public statics
	//

	/**
	 * Gets the DOM attributes of the given Node as a Map.
	 */

	public static Map<String, String> getAttributesAsMap( Node node ) {

		NamedNodeMap nodes = node.getAttributes();
		int length = nodes.getLength();

		if ( length == 0 ) {
			// (use Collections.EMPTY_MAP, not Collections.emptyMap, so that
			// we're 1.4 compatible)

			@SuppressWarnings( "unchecked" )
			Map<String, String> empty = Collections.EMPTY_MAP;
			return empty;
		}

		Map<String, String> attributes = CollectionUtils.newHashMap( length );

		for ( int loop = 0; loop < length; loop++ ) {
			Node attributeNode = nodes.item( loop );
			attributes.put( attributeNode.getNodeName(), attributeNode.getNodeValue() );
		}

		return attributes;
	}

	/**
	 * Sets the Map as DOM attributes on the given Element.
	 * <p>
	 * This implementation uses <code>element.setAttribute</code>. Therefore if the element already
	 * has attributes, the new attributes are added amongst them. If attributes with the same name
	 * already exist, they are overwritten.
	 */

	public static void setMapAsAttributes( Element element, Map<String, String> attributes ) {

		if ( attributes == null ) {
			return;
		}

		for ( Map.Entry<String, String> entry : attributes.entrySet() ) {
			element.setAttribute( entry.getKey(), entry.getValue() );
		}
	}

	/**
	 * Looks up a child with the given (local) name.
	 * <p>
	 * An array of several names may be passed, in which case they will be traversed in a simple
	 * XPath-like fashion.
	 */

	public static Element getChildNamed( Element element, String... names ) {

		if ( element == null ) {
			return null;
		}

		Element child = null;
		NodeList children = element.getChildNodes();

		outer: for ( String name : names ) {
			for ( int loop = 0, length = children.getLength(); loop < length; loop++ ) {
				Node node = children.item( loop );

				if ( !( node instanceof Element ) ) {
					continue;
				}

				child = (Element) node;

				if ( name.equals( child.getLocalName() ) ) {
					children = child.getChildNodes();
					continue outer;
				}
			}

			// No match found

			return null;
		}

		return child;
	}

	public static Element getChildWithAttribute( Element element, String attributeName ) {

		if ( element == null ) {
			return null;
		}

		NodeList children = element.getChildNodes();

		for ( int loop = 0, length = children.getLength(); loop < length; loop++ ) {
			Node node = children.item( loop );

			if ( !( node instanceof Element ) ) {
				continue;
			}

			Element child = (Element) node;

			if ( child.hasAttribute( attributeName ) ) {
				return child;
			}
		}

		return null;
	}

	public static Element getChildWithAttributeValue( Element element, String attributeName, String attributeValue ) {

		if ( element == null ) {
			return null;
		}

		NodeList children;

		try {
			children = element.getChildNodes();
		} catch ( IllegalArgumentException e ) {
			// We've seen this throw a IllegalArgumentException from
			// com.sun.org.apache.xerces.internal.dom.DeferredDocumentImpl.getNodeObject(DeferredDocumentImpl.java:1081)
			// under GWT 1.7

			return null;
		}

		int length;

		try {
			length = children.getLength();
		} catch ( NullPointerException e ) {
			// We've seen this throw a NullPointerException from
			// com.sun.org.apache.xerces.internal.dom.ParentNode.nodeListGetLength(ParentNode.java:696)
			// under GWT 1.7

			return null;
		}

		for ( int loop = 0; loop < length; loop++ ) {
			Node node;

			try {
				node = children.item( loop );
			} catch ( NullPointerException e ) {
				// We've seen this throw a NullPointerException from
				// com.sun.org.apache.xerces.internal.dom.ParentNode.nodeListItem(ParentNode.java:780)
				// under GWT 1.7

				continue;
			}

			if ( !( node instanceof Element ) ) {
				continue;
			}

			Element child = (Element) node;

			try {
				if ( attributeValue.equals( child.getAttribute( attributeName ) ) ) {
					return child;
				}
			} catch ( NullPointerException e ) {
				// We've seen this throw a NullPointerException from
				// com.sun.org.apache.xerces.internal.dom.DeferredAttrNSImpl.synchronizeData(DeferredAttrNSImpl.java:97)
				// under GWT 1.7

				continue;
			}
		}

		return null;
	}

	public static Element getSiblingNamed( Element element, String name ) {

		if ( element == null ) {
			return null;
		}

		Node nodeNextSibling = element;

		while ( true ) {
			nodeNextSibling = nodeNextSibling.getNextSibling();

			if ( nodeNextSibling == null ) {
				return null;
			}

			if ( !( nodeNextSibling instanceof Element ) ) {
				continue;
			}

			if ( name.equals( nodeNextSibling.getNodeName() ) ) {
				return (Element) nodeNextSibling;
			}
		}
	}

	public static Element importElement( Document document, Element element ) {

		try {
			return (Element) document.importNode( element, true );
		} catch ( DOMException e ) {
			// Note: importNode returns 'DOMException' under Android 1.1_r1

			Element imported = document.createElementNS( element.getNamespaceURI(), element.getNodeName() );
			setMapAsAttributes( imported, getAttributesAsMap( element ) );

			NodeList nodeList = imported.getChildNodes();

			for ( int loop = 0; loop < nodeList.getLength(); loop++ ) {
				Node node = nodeList.item( loop );

				if ( !( node instanceof Element ) ) {
					continue;
				}

				imported.appendChild( importElement( document, (Element) node ) );
			}

			return imported;
		}
	}

	/**
	 * Convert the given Document to an XML String.
	 * <p>
	 * This method is a simplified version of...
	 * <p>
	 * <code>
	 * 	ByteArrayOutputStream out = new ByteArrayOutputStream();
	 * 	javax.xml.Transformer transformer = TransformerFactory.newInstance().newTransformer();
	 * 	transformer.transform( new DOMSource( node ), new StreamResult( out ));
	 * 	return out.toString();
	 * </code>
	 * <p>
	 * ...but not all platforms (eg. Android) support <code>javax.xml.transform.Transformer</code>.
	 */

	public static String documentToString( Document document, boolean pretty ) {

		// Nothing to do?

		if ( document == null ) {
			return "";
		}

		return nodeToString( document.getFirstChild(), pretty );
	}

	/**
	 * See documentToString.
	 */

	public static String nodeToString( Node node, boolean pretty ) {

		return nodeToString( node, ( pretty ? 0 : -1 ) );
	}

	/**
	 * Converts the given XML into a <code>org.w3c.dom.Document</code>.
	 * <p>
	 * Named <code>documentFromString</code>, rather than just <code>parse</code>, because
	 * <code>DocumentBuilder.parse( String )</code> already exists and, confusingly, uses the String
	 * as a URI to the XML rather than as the XML itself.
	 * <p>
	 * Note: in performance tests, this method was consistently found to be expensive, of the order
	 * of around 10%. Consider implementing <code>DomInspector</code> on your
	 * <code>Inspectors</code> or <code>DomInspectionResultProcessor</code> on your
	 * <code>InspectionResultProcessors</code> to avoid this hit.
	 */

	public static Document documentFromString( String xml ) {

		if ( xml == null ) {
			return null;
		}

		try {
			synchronized ( DOCUMENT_BUILDER ) {
				return DOCUMENT_BUILDER.parse( new InputSource( new StringReader( xml ) ) );
			}
		} catch ( Exception e ) {
			throw new RuntimeException( e );
		}
	}

	/**
	 * Creates a new Document built from a shared, no-external-connection-making DocumentBuilder
	 * created by a namespace-aware, comment-ignoring, whitespace-ignoring DocumentBuilderFactory.
	 */

	public static Document newDocument() {

		synchronized ( DOCUMENT_BUILDER ) {
			return DOCUMENT_BUILDER.newDocument();
		}
	}

	public static Document parse( InputStream stream )
		throws IOException, SAXException {

		synchronized ( DOCUMENT_BUILDER ) {
			return DOCUMENT_BUILDER.parse( stream );
		}
	}

	/**
	 * Get the indexed Element.
	 * <p>
	 * Similar to <code>Element.getChildNodes.item</code>, but ignores any Nodes (such as
	 * indentation TextNodes).
	 */

	public static Element getElementAt( Element element, int index ) {

		NodeList nodes = element.getChildNodes();

		int actualIndex = 0;

		for ( int loop = 0, length = nodes.getLength(); loop < length; loop++ ) {
			Node node = nodes.item( loop );

			if ( !( node instanceof Element ) ) {
				continue;
			}

			if ( actualIndex == index ) {
				return (Element) node;
			}

			actualIndex++;
		}

		return null;
	}

	/**
	 * Combine the attributes and child elements of the second element into the first element.
	 * <p>
	 * Combining is performed purely by matching a topLevelAttributeToCombineOn attribute on the
	 * element. The child element ordering of the first element is respected.
	 * <p>
	 * Child elements are matched recursively on childAttributeToCombineOn.
	 */

	public static void combineElements( Element master, Element toAdd, String topLevelAttributeToCombineOn, String childAttributeToCombineOn ) {

		// Combine attributes
		//
		// Note: when Android is fixed, we can go back to using
		// toAdd.getAttributes directly, which may be slightly faster

		NamedNodeMap attributesToAdd = toAdd.getAttributes();

		for ( int loop = 0, length = attributesToAdd.getLength(); loop < length; loop++ ) {
			Node nodeToAdd = attributesToAdd.item( loop );

			String attributeToAddName = nodeToAdd.getNodeName();
			String attributeToAddValue = nodeToAdd.getNodeValue();

			if ( attributeToAddValue == null || attributeToAddValue.length() == 0 ) {
				master.removeAttribute( attributeToAddName );
			}

			master.setAttribute( attributeToAddName, attributeToAddValue );
		}

		// Combine child elements: for each child...

		NodeList childrenToAdd = toAdd.getChildNodes();
		NodeList masterChildren = master.getChildNodes();

		Set<String> childNamesAdded = CollectionUtils.newHashSet();

		Node nodeLastMasterCombinePoint = null;

		outerLoop: for ( int addLoop = 0, addLength = childrenToAdd.getLength(); addLoop < addLength; addLoop++ ) {
			Node nodeChildToAdd = childrenToAdd.item( addLoop );

			if ( !( nodeChildToAdd instanceof Element ) ) {
				continue;
			}

			Element childToAdd = (Element) nodeChildToAdd;
			String childToAddName = childToAdd.getAttribute( topLevelAttributeToCombineOn );

			if ( childToAddName == null || "".equals( childToAddName ) ) {
				throw new RuntimeException( "Child node #" + ( addLoop + 1 ) + " has no @" + topLevelAttributeToCombineOn + ": " + nodeToString( childToAdd, false ));
			}

			if ( !childNamesAdded.add( childToAddName ) ) {
				throw new RuntimeException( "Element has more than one child with @" + topLevelAttributeToCombineOn + " '" + childToAddName + "'" );
			}

			// ...find one with the same @name in the 'master'...

			for ( int masterLoop = 0, masterLength = masterChildren.getLength(); masterLoop < masterLength; masterLoop++ ) {
				Node nodeMasterChild = masterChildren.item( masterLoop );

				if ( !( nodeMasterChild instanceof Element ) ) {
					continue;
				}

				Element masterChild = (Element) nodeMasterChild;
				String masterChildName = masterChild.getAttribute( topLevelAttributeToCombineOn );

				if ( !childToAddName.equals( masterChildName ) ) {
					continue;
				}

				String nodeNameInMaster = masterChild.getNodeName();
				String nodeNameInAdd = childToAdd.getNodeName();

				if ( !nodeNameInMaster.equals( nodeNameInAdd ) ) {
					throw new RuntimeException( "Matching elements named '" + masterChildName + "', but existing one is a '" + nodeNameInMaster + "' whilst new one is a '" + nodeNameInAdd + "'" );
				}

				// ...and combine them

				if ( masterLoop == masterLength - 1 ) {
					nodeLastMasterCombinePoint = null;
				} else {
					nodeLastMasterCombinePoint = masterChild;
				}

				combineElements( masterChild, childToAdd, childAttributeToCombineOn, childAttributeToCombineOn );
				continue outerLoop;
			}

			// If no such child exists, add one either immediately after the
			// last matched master...

			if ( nodeLastMasterCombinePoint != null ) {
				Element imported = XmlUtils.importElement( master.getOwnerDocument(), childToAdd );
				master.insertBefore( imported, nodeLastMasterCombinePoint.getNextSibling() );
				nodeLastMasterCombinePoint = imported;
				continue;
			}

			// ...or simply at the end

			master.appendChild( XmlUtils.importElement( master.getOwnerDocument(), childToAdd ) );
		}
	}

	public static String attributesToString( Attributes attributes ) {

		// (use StringBuffer for J2SE 1.4 compatibility)

		StringBuffer buffer = new StringBuffer();

		for ( int loop = 0, length = attributes.getLength(); loop < length; loop++ ) {
			buffer.append( " " );
			buffer.append( attributes.getLocalName( loop ) );
			buffer.append( "=\"" );
			buffer.append( attributes.getValue( loop ) );
			buffer.append( "\"" );
		}

		return buffer.toString();

	}

	//
	// Inner class
	//

	/**
	 * Allows clients to cache SAX events directly rather than, say, caching an XML String and then
	 * instantiating a new SAX Parser each time to parse it.
	 * <p>
	 * <code>CachingContentHandler</code> operates in two modes. In the first mode it is constructed
	 * and passed a delegate <code>ContentHandler</code>. It then proceeds to route all SAX events
	 * to the delegate as normal, recording them as it does so. Following <code>endDocument</code>,
	 * it sets the delegate to <code>null</code>.
	 * <p>
	 * In the second mode, clients can call <code>replay</code> to replay the recorded SAX events on
	 * a new <code>ContentHandler</code>.
	 */

	public static class CachingContentHandler
		extends DefaultHandler {

		//
		// Private members
		//

		private ContentHandler				mDelegate;

		private CachedCommand				mLastCommand;

		private ArrayList<CachedCommand>	mCache	= CollectionUtils.newArrayList();

		private boolean						mCachingPaused;

		//
		// Constructor
		//

		/**
		 * @param delegate
		 *            the delegate to route all SAX events to, recording as we go
		 */

		public CachingContentHandler( ContentHandler delegate ) {

			mDelegate = delegate;
		}

		//
		// Public methods
		//

		public boolean isPaused() {

			return mCachingPaused;
		}

		/**
		 * @param includeLastEvent
		 *            whether to include the most recent SAX event in the cache (ie. the one that
		 *            led us to call pause)
		 */

		public void pause( boolean includeLastEvent ) {

			if ( isPaused() ) {
				throw new RuntimeException( "CachingContentHandler already paused" );
			}

			mCachingPaused = true;

			if ( !includeLastEvent ) {
				mCache.remove( mCache.size() - 1 );
			}
		}

		/**
		 * @param includeLastEvent
		 *            whether to include the most recent SAX event in the cache (ie. the one that
		 *            led us to call unpause)
		 */

		public void unpause( boolean includeLastEvent ) {

			if ( !isPaused() ) {
				throw new RuntimeException( "CachingContentHandler not paused" );
			}

			mCachingPaused = false;

			if ( includeLastEvent ) {
				mCache.add( mLastCommand );
			}
		}

		/**
		 * Replay the cached events.
		 * <p>
		 * Note: <code>replay</code> does not necessarily trigger <code>startDocument</code> and
		 * <code>endDocument</code> (ie. it does not assume the original recording contained them).
		 * Because of this, <code>CachingContentHandler</code> can be used to cache
		 * <em>fragments</em> of SAX events.
		 */

		public void replay( ContentHandler replayTo )
			throws SAXException {

			if ( mCache.isEmpty() ) {
				throw new SAXException( "Nothing to replay. Not cached any SAX events" );
			}

			if ( mDelegate != null ) {
				throw new SAXException( "Not ready to replay - ContentHandler delegate is non-null. Either endDocument must be triggered, or releaseDelegate must be called explicitly" );
			}

			for ( CachedCommand cachedCommand : mCache ) {
				cachedCommand.replay( replayTo );
			}
		}

		/**
		 * The ContentHandler delegate passed to <code>CachingContentHandler</code>'s constructor
		 * must be released in order to prevent memory leaks. This is done automatically by
		 * <code>endDocument</code>. But for use cases where <code>endDocument</code> is not
		 * triggered, clients can call <code>releaseDelegate</code> to release the delegate
		 * manually.
		 */

		public void releaseDelegate() {

			mDelegate = null;
			mLastCommand = null;
			mCache.trimToSize();
		}

		//
		// ContentHandler implementation
		//

		@Override
		public void startDocument()
			throws SAXException {

			mLastCommand = new StartDocumentCommand();

			if ( !mCachingPaused ) {
				if ( !mCache.isEmpty() ) {
					throw new SAXException( "Already cached SAX events. CachingContentHandler can only cache SAX events once" );
				}

				mCache.add( mLastCommand );
			}

			mLastCommand.replay( mDelegate );
		}

		@Override
		public void processingInstruction( String target, String data )
			throws SAXException {

			mLastCommand = new ProcessingInstructionCommand( target, data );

			if ( !mCachingPaused ) {
				mCache.add( mLastCommand );
			}

			mLastCommand.replay( mDelegate );
		}

		@Override
		public void setDocumentLocator( Locator locator ) {

			mDelegate.setDocumentLocator( locator );
		}

		@Override
		public void skippedEntity( String name )
			throws SAXException {

			mLastCommand = new SkippedEntityCommand( name );

			if ( !mCachingPaused ) {
				mCache.add( mLastCommand );
			}

			mLastCommand.replay( mDelegate );
		}

		@Override
		public void startPrefixMapping( String prefix, String uri )
			throws SAXException {

			mLastCommand = new StartPrefixMappingCommand( prefix, uri );

			if ( !mCachingPaused ) {
				mCache.add( mLastCommand );
			}

			mLastCommand.replay( mDelegate );
		}

		@Override
		public void endPrefixMapping( String prefix )
			throws SAXException {

			mLastCommand = new EndPrefixMappingCommand( prefix );

			if ( !mCachingPaused ) {
				mCache.add( mLastCommand );
			}

			mLastCommand.replay( mDelegate );
		}

		@Override
		public void startElement( String uri, String localName, String name, Attributes attributes )
			throws SAXException {

			mLastCommand = new StartElementCommand( uri, localName, name, attributes );

			if ( !mCachingPaused ) {
				mCache.add( mLastCommand );
			}

			mLastCommand.replay( mDelegate );
		}

		@Override
		@SuppressWarnings( "PMD.ArrayIsStoredDirectly" )
		public void characters( char[] characters, int start, int length )
			throws SAXException {

			mLastCommand = new CharactersCommand( characters, start, length );

			if ( !mCachingPaused ) {
				mCache.add( mLastCommand );
			}

			mLastCommand.replay( mDelegate );
		}

		@Override
		@SuppressWarnings( "PMD.ArrayIsStoredDirectly" )
		public void ignorableWhitespace( char[] characters, int start, int length )
			throws SAXException {

			mLastCommand = new IgnorableWhitespaceCommand( characters, start, length );

			if ( !mCachingPaused ) {
				mCache.add( mLastCommand );
			}

			mLastCommand.replay( mDelegate );
		}

		@Override
		public void endElement( String uri, String localName, String name )
			throws SAXException {

			mLastCommand = new EndElementCommand( uri, localName, name );

			if ( !mCachingPaused ) {
				mCache.add( mLastCommand );
			}

			mLastCommand.replay( mDelegate );
		}

		@Override
		public void endDocument()
			throws SAXException {

			mLastCommand = new EndDocumentCommand();

			if ( !mCachingPaused ) {
				mCache.add( mLastCommand );
			}

			mLastCommand.replay( mDelegate );

			// Free up resources

			releaseDelegate();
		}

		//
		// Inner class
		//

		/**
		 * Encapsulates a cached SAX event.
		 * <p>
		 * The term <code>Command</code> refers to the Command Design Pattern.
		 */

		private/* implied static */interface CachedCommand {

			//
			// Methods
			//

			void replay( ContentHandler replayTo )
				throws SAXException;
		}

		private static class StartDocumentCommand
			implements CachedCommand {

			//
			// Constructor
			//

			public StartDocumentCommand() {

				// Public for better performance
			}

			//
			// Public methods
			//

			public void replay( ContentHandler replayTo )
				throws SAXException {

				replayTo.startDocument();
			}

			@Override
			public String toString() {

				return "startDocument";
			}
		}

		private static class ProcessingInstructionCommand
			implements CachedCommand {

			//
			// Private members
			//

			private String	mTarget;

			private String	mData;

			//
			// Constructor
			//

			public ProcessingInstructionCommand( String target, String data ) {

				mTarget = target;
				mData = data;
			}

			//
			// Public methods
			//

			public void replay( ContentHandler replayTo )
				throws SAXException {

				replayTo.processingInstruction( mTarget, mData );
			}

			@Override
			public String toString() {

				return "processInstruction " + mTarget + " " + mData;
			}
		}

		private static class SkippedEntityCommand
			implements CachedCommand {

			//
			// Private members
			//

			private String	mName;

			//
			// Constructor
			//

			public SkippedEntityCommand( String name ) {

				mName = name;
			}

			//
			// Public methods
			//

			public void replay( ContentHandler replayTo )
				throws SAXException {

				replayTo.skippedEntity( mName );
			}

			@Override
			public String toString() {

				return "skippedEntity " + mName;
			}
		}

		private static class StartPrefixMappingCommand
			implements CachedCommand {

			//
			// Private members
			//

			private String	mPrefix;

			private String	mUri;

			//
			// Constructor
			//

			public StartPrefixMappingCommand( String prefix, String uri ) {

				mPrefix = prefix;
				mUri = uri;
			}

			//
			// Public methods
			//

			public void replay( ContentHandler replayTo )
				throws SAXException {

				replayTo.startPrefixMapping( mPrefix, mUri );
			}

			@Override
			public String toString() {

				return "startPrefixMapping " + mPrefix + " " + mUri;
			}
		}

		private static class EndPrefixMappingCommand
			implements CachedCommand {

			//
			// Private members
			//

			private String	mPrefix;

			//
			// Constructor
			//

			public EndPrefixMappingCommand( String prefix ) {

				mPrefix = prefix;
			}

			//
			// Public methods
			//

			public void replay( ContentHandler replayTo )
				throws SAXException {

				replayTo.endPrefixMapping( mPrefix );
			}

			@Override
			public String toString() {

				return "endPrefixMapping " + mPrefix;
			}
		}

		private static class StartElementCommand
			implements CachedCommand {

			//
			// Private members
			//

			private String		mUri;

			private String		mLocalName;

			private String		mQName;

			private Attributes	mAttributes;

			//
			// Constructor
			//

			public StartElementCommand( String uri, String localName, String qName, Attributes attributes ) {

				mUri = uri;
				mLocalName = localName;
				mQName = qName;

				// Defensive copy - SAX implementations may reuse the Attributes

				mAttributes = new AttributesImpl( attributes );
			}

			//
			// Public methods
			//

			public void replay( ContentHandler replayTo )
				throws SAXException {

				replayTo.startElement( mUri, mLocalName, mQName, mAttributes );
			}

			@Override
			public String toString() {

				return "startElement " + mUri + " " + mLocalName + " " + mQName + attributesToString( mAttributes );
			}
		}

		private static class CharactersCommand
			implements CachedCommand {

			//
			// Private members
			//

			private char[]	mCharacters;

			//
			// Constructor
			//

			public CharactersCommand( char[] characters, int start, int length ) {

				// Defensive copy - SAX implementations may reuse the array

				mCharacters = new char[length];
				System.arraycopy( characters, start, mCharacters, 0, length );
			}

			//
			// Public methods
			//

			public void replay( ContentHandler replayTo )
				throws SAXException {

				replayTo.characters( mCharacters, 0, mCharacters.length );
			}

			@Override
			public String toString() {

				return "characters " + String.valueOf( mCharacters );
			}
		}

		private static class IgnorableWhitespaceCommand
			implements CachedCommand {

			//
			// Private members
			//

			private char[]	mCharacters;

			//
			// Constructor
			//

			public IgnorableWhitespaceCommand( char[] characters, int start, int length ) {

				// Defensive copy - SAX implementations may reuse the array

				mCharacters = new char[length];
				System.arraycopy( characters, start, mCharacters, 0, length );
			}

			//
			// Public methods
			//

			public void replay( ContentHandler replayTo )
				throws SAXException {

				replayTo.ignorableWhitespace( mCharacters, 0, mCharacters.length );
			}

			@Override
			public String toString() {

				return "ignorableWhitespace " + String.valueOf( mCharacters );
			}
		}

		private static class EndElementCommand
			implements CachedCommand {

			//
			// Private members
			//

			private String	mUri;

			private String	mLocalName;

			private String	mQName;

			//
			// Constructor
			//

			public EndElementCommand( String uri, String localName, String qName ) {

				mUri = uri;
				mLocalName = localName;
				mQName = qName;
			}

			//
			// Public methods
			//

			public void replay( ContentHandler replayTo )
				throws SAXException {

				replayTo.endElement( mUri, mLocalName, mQName );
			}

			@Override
			public String toString() {

				return "endElement " + mUri + " " + mLocalName + " " + mQName;
			}
		}

		private static class EndDocumentCommand
			implements CachedCommand {

			//
			// Constructor
			//

			public EndDocumentCommand() {

				// Public for better performance
			}

			//
			// Public methods
			//

			public void replay( ContentHandler replayTo )
				throws SAXException {

				replayTo.endDocument();
			}

			@Override
			public String toString() {

				return "endDocument";
			}
		}
	}

	//
	// Private statics
	//

	/**
	 * Convert the given Node to an XML String.
	 * <p>
	 * This method is a simplified version of...
	 * <p>
	 * <code>
	 * 	ByteArrayOutputStream out = new ByteArrayOutputStream();<br/>
	 * 	javax.xml.Transformer transformer = TransformerFactory.newInstance().newTransformer();<br/>
	 * 	transformer.transform( new DOMSource( node ), new StreamResult( out ));<br/>
	 * 	return out.toString();
	 * </code>
	 * <p>
	 * ...but not all platforms (eg. Android) support <code>javax.xml.transform.Transformer</code>.
	 *
	 * @param indent
	 *            how much to indent the output. -1 for no indent.
	 */

	private static String nodeToString( Node node, int indent ) {

		// Text nodes

		if ( node == null ) {
			return null;
		}

		if ( !( node instanceof Element ) ) {

			String value = node.getNodeValue();

			if ( value == null ) {
				return null;
			}

			return escapeForXml( value.trim() );
		}

		// (use StringBuffer for J2SE 1.4 compatibility)

		StringBuffer buffer = new StringBuffer();

		// Open tag

		indent( buffer, indent );
		String nodeName = escapeForXml( node.getNodeName() );
		buffer.append( "<" );
		buffer.append( nodeName );

		// Changing namespace

		String namespace = node.getNamespaceURI();
		Node parentNode = node.getParentNode();

		if ( namespace != null && ( parentNode == null || !namespace.equals( parentNode.getNamespaceURI() ) ) ) {
			buffer.append( " xmlns=\"" );
			buffer.append( namespace );
			buffer.append( "\"" );
		}

		// Attributes

		NamedNodeMap attributes = node.getAttributes();

		// Always put name first for easy unit tests

		Node name = attributes.getNamedItem( "name" );

		if ( name != null ) {
			buffer.append( " name=\"" );
			buffer.append( escapeForXml( name.getNodeValue() ) );
			buffer.append( "\"" );
		}

		for ( int loop = 0; loop < attributes.getLength(); loop++ ) {
			Node attribute = attributes.item( loop );
			String attributeName = attribute.getNodeName();

			// (I'm a bit surprised xmlns is an attribute - is that a bug?)

			if ( "xmlns".equals( attributeName ) ) {
				continue;
			}

			// (always put name first for easy unit tests)

			if ( "name".equals( attributeName ) ) {
				continue;
			}

			buffer.append( " " );
			buffer.append( escapeForXml( attributeName ) );
			buffer.append( "=\"" );
			buffer.append( escapeForXml( attribute.getNodeValue() ) );
			buffer.append( "\"" );
		}

		// Children (if any)

		NodeList children = node.getChildNodes();
		int length = children.getLength();

		if ( length == 0 ) {
			buffer.append( "/>" );
		} else {
			buffer.append( ">" );

			int nextIndent = indent;

			if ( indent != -1 ) {
				nextIndent++;
			}

			for ( int loop = 0; loop < length; loop++ ) {
				Node childNode = children.item( loop );

				if ( indent != -1 && childNode instanceof Element ) {
					buffer.append( "\n" );
				}

				buffer.append( nodeToString( childNode, nextIndent ) );
			}

			if ( indent != -1 && buffer.charAt( buffer.length() - 1 ) == '>' ) {
				buffer.append( "\n" );
				indent( buffer, indent );
			}

			// Close tag

			buffer.append( "</" );
			buffer.append( nodeName );
			buffer.append( ">" );
		}

		return buffer.toString();
	}

	private static void indent( StringBuffer buffer, int indent ) {

		for ( int loop = 0; loop < indent; loop++ ) {
			buffer.append( "   " );
		}
	}

	private static String escapeForXml( String in ) {

		if ( in == null ) {
			return "";
		}

		String out = in;

		out = PATTERN_AMP.matcher( out ).replaceAll( "&amp;" );
		out = PATTERN_LT.matcher( out ).replaceAll( "&lt;" );
		out = PATTERN_GT.matcher( out ).replaceAll( "&gt;" );
		out = PATTERN_QUOT.matcher( out ).replaceAll( "&quot;" );
		out = PATTERN_APOS.matcher( out ).replaceAll( "&apos;" );

		return out;
	}

	//
	// Inner class
	//

	/**
	 * EntityResolver that does a 'no-op' and does not actually resolve entities. Useful to prevent
	 * <code>DocumentBuilder</code> making URL connections.
	 */

	/* package private */static class NopEntityResolver
		implements EntityResolver {

		//
		// Private statics
		//

		private static final byte[]	BYTES	= "<?xml version='1.0' encoding='UTF-8'?>".getBytes();

		//
		// Public methods
		//

		public InputSource resolveEntity( String publicId, String systemId ) {

			return new InputSource( new ByteArrayInputStream( BYTES ) );
		}
	}

	//
	// Private statics
	//

	private static final DocumentBuilder	DOCUMENT_BUILDER;

	static {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware( true );
		factory.setIgnoringComments( true );
		factory.setIgnoringElementContentWhitespace( true );
		try {
			DOCUMENT_BUILDER = factory.newDocumentBuilder();
			DOCUMENT_BUILDER.setEntityResolver( new NopEntityResolver() );
		} catch ( ParserConfigurationException e ) {
			throw new RuntimeException( e );
		}
	}

	private static final Pattern			PATTERN_AMP		= Pattern.compile( "&", Pattern.LITERAL );

	private static final Pattern			PATTERN_LT		= Pattern.compile( "<", Pattern.LITERAL );

	private static final Pattern			PATTERN_GT		= Pattern.compile( ">", Pattern.LITERAL );

	private static final Pattern			PATTERN_QUOT	= Pattern.compile( "\"", Pattern.LITERAL );

	private static final Pattern			PATTERN_APOS	= Pattern.compile( "\'", Pattern.LITERAL );

	//
	// Private constructor
	//

	private XmlUtils() {

		// Can never be called
	}
}
