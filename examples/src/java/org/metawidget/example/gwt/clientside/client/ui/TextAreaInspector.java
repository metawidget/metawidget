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

package org.metawidget.example.gwt.clientside.client.ui;

import org.metawidget.inspector.iface.Inspector;
import org.metawidget.inspector.iface.InspectorException;

import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

/**
 * @author Richard Kennard
 */

public class TextAreaInspector
	implements Inspector
{
	//
	// Private members
	//

	private final TextArea	mTextArea;

	//
	// Constructor
	//

	public TextAreaInspector( TextArea textArea )
	{
		mTextArea = textArea;
	}

	//
	// Public method
	//

	@Override
	public String inspect( Object toInspect, String type, String... names )
	{
		// Parse the XML (do this each time as it may have been updated by the user)

		Document document = XMLParser.parse( mTextArea.getText() );

		// Fetch the type

		Element entityToReturn = getEntity( document, type );

		// Traverse any path

		for ( String name : names )
		{
			Element property = getProperty( entityToReturn, name );

			if ( !property.hasAttribute( "type" ) )
				throw InspectorException.newException( "Property '" + name + "' does not have a 'type' attribute" );

			entityToReturn = getEntity( document, property.getAttribute( "type" ) );
		}

		// Return the XML

		return "<metawidget-metadata>" + nodeToString( entityToReturn ) + "</metawidget-metadata>";
	}

	//
	// Private methods
	//

	private Element getEntity( Document document, String type )
	{
		Element root = document.getDocumentElement();
		NodeList entities = root.getElementsByTagName( "entity" );

		for ( int loop = 0, length = entities.getLength(); loop < length; loop++ )
		{
			Element entity = (Element) entities.item( loop );

			if ( type.equals( entity.getAttribute( "type" ) ) )
				return entity;
		}

		throw new RuntimeException( "No such entity with type '" + type + "'" );
	}

	private Element getProperty( Element entity, String name )
	{
		NodeList entities = entity.getElementsByTagName( "property" );

		for ( int loop = 0, length = entities.getLength(); loop < length; loop++ )
		{
			Element property = (Element) entities.item( loop );

			if ( name.equals( property.getAttribute( "name" ) ) )
				return property;
		}

		throw new RuntimeException( "No such property with name '" + name + "' of entity '" + entity.getAttribute( "type" ) + "'" );
	}

	private static String nodeToString( Node node )
	{
		if ( !( node instanceof Element ) )
			return "";

		StringBuilder builder = new StringBuilder();

		// Open tag

		String nodeName = node.getNodeName();
		builder.append( "<" );
		builder.append( nodeName );

		// Attributes (if any)

		NamedNodeMap attributes = node.getAttributes();

		for ( int loop = 0, length = attributes.getLength(); loop < length; loop++ )
		{
			Node attribute = attributes.item( loop );
			builder.append( " " );
			builder.append( attribute.getNodeName() );
			builder.append( "=\"" );
			builder.append( attribute.getNodeValue() );
			builder.append( "\"" );
		}

		builder.append( ">" );

		// Children (if any)

		NodeList children = node.getChildNodes();

		for ( int loop = 0, length = children.getLength(); loop < length; loop++ )
		{
			builder.append( nodeToString( children.item( loop ) ) );
		}

		// Close tag

		builder.append( "</" );
		builder.append( nodeName );
		builder.append( ">" );

		return builder.toString();
	}
}
