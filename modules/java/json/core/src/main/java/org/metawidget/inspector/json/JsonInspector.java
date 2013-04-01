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

package org.metawidget.inspector.json;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.metawidget.inspector.iface.DomInspector;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

/**
 * Inspector to look for metadata in JSON files.
 *
 * @author Richard Kennard
 */

public class JsonInspector
	implements DomInspector<Element> {

	//
	// Private members
	//

	private JsonObject	mRoot;

	//
	// Constructor
	//

	public JsonInspector( JsonInspectorConfig config ) {

		InputStream inputStream = config.getInputStream();

		if ( inputStream == null ) {
			throw InspectorException.newException( "No JSON input stream specified" );
		}

		// Parse the JSON

		mRoot = (JsonObject) new JsonParser().parse( new InputStreamReader( inputStream ) );
	}

	//
	// Public methods
	//

	public final String inspect( Object toInspect, String type, String... names ) {

		Element element = inspectAsDom( toInspect, type, names );

		if ( element == null ) {
			return null;
		}

		return XmlUtils.nodeToString( element, false );
	}

	public Element inspectAsDom( Object toInspect, String type, String... names ) {

		JsonObject root = mRoot;

		// Traverse names

		for( String name : names ) {

			if ( !root.has( name )) {
				return null;
			}

			root = root.getAsJsonObject( name );
		}

		// Start the DOM

		Document document = XmlUtils.newDocument();
		Element documentRoot = document.createElementNS( NAMESPACE, ROOT );
		documentRoot.setAttribute( VERSION, "1.0" );
		document.appendChild( documentRoot );
		Element entity = document.createElementNS( NAMESPACE, ENTITY );
		entity.setAttribute( TYPE, type );
		documentRoot.appendChild( entity );

		// Write all JSON values into it

		for ( Map.Entry<String, JsonElement> entry : root.entrySet() ) {

			JsonElement element = entry.getValue();

			// Write the name

			Element child = document.createElementNS( NAMESPACE, PROPERTY );
			child.setAttribute( NAME, entry.getKey() );

			// Write the type

			if ( element.isJsonPrimitive() ) {

				JsonPrimitive primitive = (JsonPrimitive) element;

				if ( primitive.isNumber() ) {
					child.setAttribute( TYPE, int.class.getName() );
				} else if ( primitive.isBoolean() ) {
					child.setAttribute( TYPE, boolean.class.getName() );
				} else {
					child.setAttribute( TYPE, String.class.getName() );
				}
			} else if ( element.isJsonArray() ) {
				child.setAttribute( TYPE, "array" );
			} else {
				child.setAttribute( TYPE, Object.class.getName() );
			}

			entity.appendChild( child );
		}

		// Return the DOM

		return documentRoot;
	}

	//
	// Protected methods
	//

	protected JsonObject getRoot() {

		return mRoot;
	}
}
