// Metawidget Examples (licensed under BSD License)
//
// Copyright (c) Richard Kennard
// All rights reserved
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
//
// * Redistributions of source code must retain the above copyright notice,
//   this list of conditions and the following disclaimer.
// * Redistributions in binary form must reproduce the above copyright notice,
//   this list of conditions and the following disclaimer in the documentation
//   and/or other materials provided with the distribution.
// * Neither the name of Richard Kennard nor the names of its contributors may
//   be used to endorse or promote products derived from this software without
//   specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
// OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
// OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
// OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
// OF THE POSSIBILITY OF SUCH DAMAGE.

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
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class TextAreaInspector
	implements Inspector {

	//
	// Private members
	//

	private final TextArea	mTextArea;

	//
	// Constructor
	//

	public TextAreaInspector( TextArea textArea ) {

		mTextArea = textArea;
	}

	//
	// Public method
	//

	public String inspect( Object toInspect, String type, String... names ) {

		// Parse the XML (do this each time as it may have been updated by the user)

		Document document = XMLParser.parse( mTextArea.getText() );

		// Fetch the type

		Element entityToReturn = getEntity( document, type );

		// Traverse any path

		for ( String name : names ) {
			Element property = getProperty( entityToReturn, name );

			if ( !property.hasAttribute( "type" ) ) {
				throw InspectorException.newException( "Property '" + name + "' does not have a 'type' attribute" );
			}

			entityToReturn = getEntity( document, property.getAttribute( "type" ) );
		}

		// Return the XML

		return "<metawidget-metadata>" + nodeToString( entityToReturn ) + "</metawidget-metadata>";
	}

	//
	// Private methods
	//

	private Element getEntity( Document document, String type ) {

		Element root = document.getDocumentElement();
		NodeList entities = root.getElementsByTagName( "entity" );

		for ( int loop = 0, length = entities.getLength(); loop < length; loop++ ) {
			Element entity = (Element) entities.item( loop );

			if ( type.equals( entity.getAttribute( "type" ) ) ) {
				return entity;
			}
		}

		throw new RuntimeException( "No such entity with type '" + type + "'" );
	}

	private Element getProperty( Element entity, String name ) {

		NodeList entities = entity.getElementsByTagName( "property" );

		for ( int loop = 0, length = entities.getLength(); loop < length; loop++ ) {
			Element property = (Element) entities.item( loop );

			if ( name.equals( property.getAttribute( "name" ) ) ) {
				return property;
			}
		}

		throw new RuntimeException( "No such property with name '" + name + "' of entity '" + entity.getAttribute( "type" ) + "'" );
	}

	private static String nodeToString( Node node ) {

		if ( !( node instanceof Element ) ) {
			return "";
		}

		StringBuilder builder = new StringBuilder();

		// Open tag

		String nodeName = node.getNodeName();
		builder.append( "<" );
		builder.append( nodeName );

		// Attributes (if any)

		NamedNodeMap attributes = node.getAttributes();

		for ( int loop = 0, length = attributes.getLength(); loop < length; loop++ ) {
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

		for ( int loop = 0, length = children.getLength(); loop < length; loop++ ) {
			builder.append( nodeToString( children.item( loop ) ) );
		}

		// Close tag

		builder.append( "</" );
		builder.append( nodeName );
		builder.append( ">" );

		return builder.toString();
	}
}
