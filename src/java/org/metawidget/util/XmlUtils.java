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
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.metawidget.inspector.InspectorException;
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
	//
	// Public statics
	//
	//

	/**
	 * Gets the DOM attributes of the given Element as a Map.
	 */

	public static Map<String, String> getAttributesAsMap( Element element )
	{
		NamedNodeMap nodes = element.getAttributes();

		// Running on Android m5-rc15?

		if ( nodes == null )
			return getAttributesAsMapAndroidHack( element );

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
			Node node = nodes.item( loop );
			attributes.put( node.getNodeName(), node.getNodeValue() );
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

	public static Element getChildNamed( Element element, String name )
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

			if ( name.equals( child.getNodeName() ) )
				return child;
		}

		return null;
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

	public static TypeAndNames parsePath( String path )
	{
		return parsePath( path, StringUtils.SEPARATOR_SLASH_CHAR );
	}

	public static TypeAndNames parsePath( String path, char separator )
	{
		int indexOfTypeEnd = path.indexOf( separator );

		// Just type?

		if ( indexOfTypeEnd == -1 )
			return new TypeAndNames( path, null );

		String type = path.substring( 0, indexOfTypeEnd );

		// Parse names

		int indexOfNameEnd = indexOfTypeEnd;

		List<String> names = CollectionUtils.newArrayList();

		while ( true )
		{
			int indexOfNameStart = indexOfNameEnd + 1;
			indexOfNameEnd = path.indexOf( separator, indexOfNameStart );

			if ( indexOfNameEnd == -1 )
			{
				names.add( path.substring( indexOfNameStart ) );
				break;
			}

			names.add( path.substring( indexOfNameStart, indexOfNameEnd ) );
		}

		if ( names.isEmpty() )
			return new TypeAndNames( type, null );

		return new TypeAndNames( type, names.toArray( new String[names.size()] ) );
	}

	/**
	 * Tuple for returning a <code>type</code> and an array of <code>names</code>.
	 */

	public static class TypeAndNames
	{
		//
		//
		// Private methods
		//
		//

		private String		mType;

		private String[]	mNames;

		//
		//
		// Constructor
		//
		//

		public TypeAndNames( String type, String[] names )
		{
			mType = type;
			mNames = names;
		}

		//
		//
		// Public methods
		//
		//

		public String getType()
		{
			return mType;
		}

		public String[] getNames()
		{
			return mNames;
		}
	}

	//
	//
	// Private statics
	//
	//

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

			if ( childToAddName == null || "".equals( childToAddName ))
				throw InspectorException.newException( "Child node #" + addLoop + " has no @" + topLevelAttributeToCombineOn );

			if ( !childNamesAdded.add( childToAddName ) )
				throw InspectorException.newException( "Element has more than one child with @" + topLevelAttributeToCombineOn + " '" + childToAddName + "'" );

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

				// ...and combine them

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

	/**
	 * Hack to workaround bug in Android m5-rc15.
	 */

	private static Map<String, String> getAttributesAsMapAndroidHack( Element element )
	{
		try
		{
			Field field = element.getClass().getDeclaredField( "attributes" );
			field.setAccessible( true );

			@SuppressWarnings( "unchecked" )
			Map<String, Attr> map = (Map<String, Attr>) field.get( element );

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

	//
	//
	// Private constructor
	//
	//

	private XmlUtils()
	{
		// Can never be called
	}
}
