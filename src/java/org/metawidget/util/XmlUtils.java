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
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 * Utilities for working with XML.
 *
 * @author Richard Kennard
 */

public class XmlUtils
{
	//
	// Public statics
	//

	/**
	 * Gets the DOM attributes of the given Node as a Map.
	 */

	public static Map<String, String> getAttributesAsMap( Node node )
	{
		NamedNodeMap nodes = node.getAttributes();

		// Running on Android m5-rc15?

		if ( nodes == null )
			return getAttributesAsMapAndroidHack( node );

		// Running on something compliant :)

		int length = nodes.getLength();

		if ( length == 0 )
		{
			// (use Collections.EMPTY_MAP, not Collections.emptyMap, so that
			// we're 1.4 compatible)

			@SuppressWarnings( "unchecked" )
			Map<String, String> empty = Collections.EMPTY_MAP;
			return empty;
		}

		Map<String, String> attributes = CollectionUtils.newHashMap( length );

		for ( int loop = 0; loop < length; loop++ )
		{
			Node attributeNode = nodes.item( loop );
			attributes.put( attributeNode.getNodeName(), attributeNode.getNodeValue() );
		}

		return attributes;
	}

	/**
	 * Sets the Map as DOM attributes on the given Element.
	 */

	public static void setMapAsAttributes( Element element, Map<String, String> attributes )
	{
		if ( attributes == null )
			return;

		for ( Map.Entry<String, String> entry : attributes.entrySet() )
		{
			element.setAttribute( entry.getKey(), entry.getValue() );
		}
	}

	/**
	 * Looks up a child with the given (local) name.
	 * <p>
	 * An array of several names may be passed, in which case they will be traversed in a simple
	 * XPath-like fashion.
	 */

	public static Element getChildNamed( Element element, String... names )
	{
		if ( element == null )
			return null;

		Element child = null;
		NodeList children = element.getChildNodes();

		outer: for ( String name : names )
		{
			for ( int loop = 0, length = children.getLength(); loop < length; loop++ )
			{
				Node node = children.item( loop );

				if ( !( node instanceof Element ) )
					continue;

				child = (Element) node;

				if ( name.equals( child.getLocalName() ) )
				{
					children = child.getChildNodes();
					continue outer;
				}
			}

			// No match found

			return null;
		}

		return child;
	}

	public static Element getChildWithAttribute( Element element, String attributeName )
	{
		if ( element == null )
			return null;

		NodeList children = element.getChildNodes();

		for ( int loop = 0, length = children.getLength(); loop < length; loop++ )
		{
			Node node = children.item( loop );

			if ( !( node instanceof Element ) )
				continue;

			Element child = (Element) node;

			if ( child.hasAttribute( attributeName ) )
				return child;
		}

		return null;
	}

	public static Element getChildWithAttributeValue( Element element, String attributeName, String attributeValue )
	{
		if ( element == null )
			return null;

		NodeList children = element.getChildNodes();

		for ( int loop = 0, length = children.getLength(); loop < length; loop++ )
		{
			Node node = children.item( loop );

			if ( !( node instanceof Element ) )
				continue;

			Element child = (Element) node;

			if ( attributeValue.equals( child.getAttribute( attributeName ) ) )
				return child;
		}

		return null;
	}

	public static Element getSiblingNamed( Element element, String name )
	{
		if ( element == null )
			return null;

		Node nodeNextSibling = element;

		while ( true )
		{
			nodeNextSibling = nodeNextSibling.getNextSibling();

			if ( nodeNextSibling == null )
				return null;

			if ( !( nodeNextSibling instanceof Element ) )
				continue;

			if ( name.equals( nodeNextSibling.getNodeName() ) )
				return (Element) nodeNextSibling;
		}
	}

	public static Element importElement( Document document, Element element )
	{
		try
		{
			return (Element) document.importNode( element, true );
		}
		catch ( RuntimeException e )
		{
			// Note: importNode returns 'implement me!' under Android m5-rc15

			Element imported = document.createElementNS( element.getNamespaceURI(), element.getNodeName() );
			setMapAsAttributes( imported, getAttributesAsMap( element ) );

			NodeList nodeList = imported.getChildNodes();

			for ( int loop = 0; loop < nodeList.getLength(); loop++ )
			{
				Node node = nodeList.item( loop );

				if ( !( node instanceof Element ) )
					continue;

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

	public static String documentToString( Document document, boolean pretty )
	{
		// Nothing to do?

		if ( document == null )
			return "";

		return nodeToString( document.getFirstChild(), ( pretty ? 0 : -1 ) );
	}

	public static Document documentFromString( String xml )
	{
		if ( xml == null )
			return null;

		try
		{
			return newDocumentBuilder().parse( new InputSource( new StringReader( xml ) ) );
		}
		catch ( Exception e )
		{
			throw new RuntimeException( e );
		}
	}

	/**
	 * Creates a new DocumentBuilder using a shared, namespace-aware, comment-ignoring,
	 * whitespace-ignoring DocumentBuilderFactory.
	 */

	public static DocumentBuilder newDocumentBuilder()
		throws ParserConfigurationException
	{
		return DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
	}

	/**
	 * EntityResolver that does a 'no-op' and does not actually resolve entities. Useful to prevent
	 * <code>DocumentBuilder</code> making URL connections.
	 */

	public static class NopEntityResolver
		implements EntityResolver
	{
		//
		//
		// Private statics
		//
		//

		private final static byte[]	BYTES	= "<?xml version='1.0' encoding='UTF-8'?>".getBytes();

		//
		//
		// Public methods
		//
		//

		public InputSource resolveEntity( String publicId, String systemId )
		{
			return new InputSource( new ByteArrayInputStream( BYTES ) );
		}
	}

	/**
	 * Combine the attributes and child elements of the second element into the first element.
	 * <p>
	 * Combining is performed purely by matching a topLevelAttributeToCombineOn attribute on the
	 * element. The child element ordering of the first element is respected.
	 * <p>
	 * Child elements are matched recursively on childAttributeToCombineOn.
	 */

	public static void combineElements( Element master, Element toAdd, String topLevelAttributeToCombineOn, String childAttributeToCombineOn )
	{
		// Combine attributes
		//
		// Note: when Android is fixed, we can go back to using
		// toAdd.getAttributes directly, which may be slightly faster

		Map<String, String> attributesToAdd = XmlUtils.getAttributesAsMap( toAdd );

		for ( Map.Entry<String, String> entryToAdd : attributesToAdd.entrySet() )
		{
			String attributeToAddName = entryToAdd.getKey();
			String attributeToAddValue = entryToAdd.getValue();

			if ( attributeToAddValue == null || attributeToAddValue.length() == 0 )
				master.removeAttribute( attributeToAddName );

			master.setAttribute( attributeToAddName, attributeToAddValue );
		}

		// Combine child elements: for each child...

		NodeList childrenToAdd = toAdd.getChildNodes();
		NodeList masterChildren = master.getChildNodes();

		Set<String> childNamesAdded = CollectionUtils.newHashSet();

		Node nodeLastMasterCombinePoint = null;

		outerLoop: for ( int addLoop = 0, addLength = childrenToAdd.getLength(); addLoop < addLength; addLoop++ )
		{
			Node nodeChildToAdd = childrenToAdd.item( addLoop );

			if ( !( nodeChildToAdd instanceof Element ) )
				continue;

			Element childToAdd = (Element) nodeChildToAdd;
			String childToAddName = childToAdd.getAttribute( topLevelAttributeToCombineOn );

			if ( childToAddName == null || "".equals( childToAddName ) )
				throw new RuntimeException( "Child node #" + addLoop + " (" + childToAdd.getNodeName() + ") has no @" + topLevelAttributeToCombineOn );

			if ( !childNamesAdded.add( childToAddName ) )
				throw new RuntimeException( "Element has more than one child with @" + topLevelAttributeToCombineOn + " '" + childToAddName + "'" );

			// ...find one with the same @name in the 'master'...

			for ( int masterLoop = 0, masterLength = masterChildren.getLength(); masterLoop < masterLength; masterLoop++ )
			{
				Node nodeMasterChild = masterChildren.item( masterLoop );

				if ( !( nodeMasterChild instanceof Element ) )
					continue;

				Element masterChild = (Element) nodeMasterChild;
				String masterChildName = masterChild.getAttribute( topLevelAttributeToCombineOn );

				if ( !childToAddName.equals( masterChildName ) )
					continue;

				String nodeNameInMaster = masterChild.getNodeName();
				String nodeNameInAdd = childToAdd.getNodeName();

				if ( !nodeNameInMaster.equals( nodeNameInAdd ) )
					throw new RuntimeException( "Matching elements named '" + masterChildName + "', but existing one is a '" + nodeNameInMaster + "' whilst new one is a '" + nodeNameInAdd + "'" );

				// ...and combine them

				if ( masterLoop == masterLength - 1 )
					nodeLastMasterCombinePoint = null;
				else
					nodeLastMasterCombinePoint = masterChild;

				combineElements( masterChild, childToAdd, childAttributeToCombineOn, childAttributeToCombineOn );
				continue outerLoop;
			}

			// If no such child exists, add one either immediately after the
			// last matched master...

			if ( nodeLastMasterCombinePoint != null )
			{
				Element imported = XmlUtils.importElement( master.getOwnerDocument(), childToAdd );
				master.insertBefore( imported, nodeLastMasterCombinePoint.getNextSibling() );
				nodeLastMasterCombinePoint = imported;
				continue;
			}

			// ...or simply at the end

			master.appendChild( XmlUtils.importElement( master.getOwnerDocument(), childToAdd ) );
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
	 * 	ByteArrayOutputStream out = new ByteArrayOutputStream();
	 * 	javax.xml.Transformer transformer = TransformerFactory.newInstance().newTransformer();
	 * 	transformer.transform( new DOMSource( node ), new StreamResult( out ));
	 * 	return out.toString();
	 * </code>
	 * <p>
	 * ...but not all platforms (eg. Android) support <code>javax.xml.transform.Transformer</code>.
	 *
	 * @param indent
	 *            how much to indent the output. -1 for no indent.
	 */

	private static String nodeToString( Node node, int indent )
	{
		// Ignore non-Elements

		if ( !( node instanceof Element ) )
			return "";

		// (use StringBuffer for J2SE 1.4 compatibility)

		StringBuffer buffer = new StringBuffer();

		// Indent

		for ( int loop = 0; loop < indent; loop++ )
		{
			buffer.append( "   " );
		}

		// Open tag

		String nodeName = escapeForXml( node.getNodeName() );
		buffer.append( "<" );
		buffer.append( nodeName );

		// Changing namespace

		String namespace = node.getNamespaceURI();
		Node parentNode = node.getParentNode();

		if ( namespace != null && ( parentNode == null || !namespace.equals( parentNode.getNamespaceURI() ) ) )
		{
			buffer.append( " xmlns=\"" );
			buffer.append( namespace );
			buffer.append( "\"" );
		}

		// Attributes (go via getAttributesAsMap to benefit from getAttributesAsMapHack)

		Map<String, String> attributes = getAttributesAsMap( node );

		// In pretty mode, always put name first for easy reading

		if ( indent > -1 )
		{
			String name = attributes.get( "name" );

			if ( name != null )
			{
				buffer.append( " name=\"" );
				buffer.append( escapeForXml( name ) );
				buffer.append( "\"" );
			}
		}

		for ( Map.Entry<String, String> attribute : attributes.entrySet() )
		{
			String attributeName = attribute.getKey();

			// (I'm a bit surprised xmlns is an attribute - is that a bug?)

			if ( "xmlns".equals( attributeName ) )
				continue;

			// (in pretty mode, always put name first for easy reading)

			if ( indent > -1 && "name".equals( attributeName ) )
				continue;

			buffer.append( " " );
			buffer.append( escapeForXml( attributeName ) );
			buffer.append( "=\"" );
			buffer.append( escapeForXml( attribute.getValue() ) );
			buffer.append( "\"" );
		}

		// Children (if any)

		NodeList children = node.getChildNodes();
		int length = children.getLength();

		if ( length == 0 )
		{
			buffer.append( "/>" );
		}
		else
		{
			buffer.append( ">" );

			int nextIndent = indent;

			if ( indent != -1 )
			{
				buffer.append( "\n" );
				nextIndent++;
			}

			for ( int loop = 0; loop < length; loop++ )
			{
				buffer.append( nodeToString( children.item( loop ), nextIndent ) );
			}

			// Indent

			for ( int loop = 0; loop < indent; loop++ )
			{
				buffer.append( "   " );
			}

			// Close tag

			buffer.append( "</" );
			buffer.append( nodeName );
			buffer.append( ">" );
		}

		if ( indent > 0 )
			buffer.append( "\n" );

		return buffer.toString();
	}

	/**
	 * Hack to workaround bug in Android m5-rc15.
	 */

	private static Map<String, String> getAttributesAsMapAndroidHack( Node node )
	{
		try
		{
			Field field = node.getClass().getDeclaredField( "attributes" );
			field.setAccessible( true );

			@SuppressWarnings( "unchecked" )
			Map<String, Attr> map = (Map<String, Attr>) field.get( node );

			if ( map == null )
			{
				// (use Collections.EMPTY_MAP, not Collections.emptyMap, so that
				// we're 1.4 compatible)

				@SuppressWarnings( "unchecked" )
				Map<String, String> empty = Collections.EMPTY_MAP;
				return empty;
			}

			Map<String, String> attributes = CollectionUtils.newHashMap( map.size() );

			for ( Attr attr : map.values() )
			{
				attributes.put( attr.getName(), attr.getValue() );
			}

			return attributes;
		}
		catch ( Exception e )
		{
			// (use Collections.EMPTY_MAP, not Collections.emptyMap, so that
			// we're 1.4 compatible)

			@SuppressWarnings( "unchecked" )
			Map<String, String> empty = Collections.EMPTY_MAP;
			return empty;
		}
	}

	private static String escapeForXml( String in )
	{
		if ( in == null )
			return "";

		String out = in;

		out = PATTERN_AMP.matcher( out ).replaceAll( "&amp;" );
		out = PATTERN_LT.matcher( out ).replaceAll( "&lt;" );
		out = PATTERN_GT.matcher( out ).replaceAll( "&gt;" );
		out = PATTERN_QUOT.matcher( out ).replaceAll( "&quot;" );

		return out;
	}

	//
	// Private statics
	//

	private final static DocumentBuilderFactory	DOCUMENT_BUILDER_FACTORY;

	static
	{
		DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();
		DOCUMENT_BUILDER_FACTORY.setNamespaceAware( true );
		DOCUMENT_BUILDER_FACTORY.setIgnoringComments( true );
		DOCUMENT_BUILDER_FACTORY.setIgnoringElementContentWhitespace( true );
	}

	private final static Pattern				PATTERN_AMP		= Pattern.compile( "&", Pattern.LITERAL );

	private final static Pattern				PATTERN_LT		= Pattern.compile( "<", Pattern.LITERAL );

	private final static Pattern				PATTERN_GT		= Pattern.compile( ">", Pattern.LITERAL );

	private final static Pattern				PATTERN_QUOT	= Pattern.compile( "\"", Pattern.LITERAL );

	//
	// Private constructor
	//

	private XmlUtils()
	{
		// Can never be called
	}
}
