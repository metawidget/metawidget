// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

package org.metawidget.inspector.json.schema;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.json.JsonInspector;
import org.metawidget.inspector.json.JsonInspectorConfig;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Inspector to look for metadata in JSON Schema files.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class JsonSchemaInspector
	extends JsonInspector {

	//
	// Private statics
	//

	private static final String	PROPERTIES	= "properties";

	//
	// Constructor
	//

	public JsonSchemaInspector( JsonInspectorConfig config ) {

		super( config );
	}

	//
	// Protected methods
	//

	@Override
	public Element inspectAsDom( Object toInspect, String type, String... names ) {

		JsonObject root = getRoot();

		// Traverse names (via nested properties)

		for ( String name : names ) {

			if ( !root.has( PROPERTIES ) ) {
				return null;
			}

			root = root.getAsJsonObject( PROPERTIES );

			if ( !root.has( name ) ) {
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

		if ( names.length > 0 ) {
			entity.setAttribute( NAME, names[names.length - 1] );
		}

		// Write all JSON names into it as attributes

		writeJsonAttributesAsDom( entity, root );

		// Write all direct descendants

		if ( root.has( PROPERTIES ) ) {

			root = root.getAsJsonObject( PROPERTIES );

			for ( Map.Entry<String, JsonElement> entry : root.entrySet() ) {

				String name = entry.getKey();
				JsonElement element = entry.getValue();

				if ( !element.isJsonObject() ) {
					throw InspectorException.newException( "'" + name + "' is not a " + JsonObject.class );
				}

				JsonObject property = element.getAsJsonObject();

				Element child = document.createElementNS( NAMESPACE, PROPERTY );
				child.setAttribute( NAME, name );
				writeJsonAttributesAsDom( child, property );
				entity.appendChild( child );
			}
		}

		// Return the DOM

		return documentRoot;
	}

	//
	// Private methods
	//

	private void writeJsonAttributesAsDom( Element element, JsonObject object ) {

		for ( Map.Entry<String, JsonElement> entry : object.entrySet() ) {

			String name = entry.getKey();
			JsonElement jsonElement = entry.getValue();

			if ( jsonElement.isJsonArray() ) {
				List<String> array = CollectionUtils.newArrayList();
				for ( Iterator<JsonElement> i = jsonElement.getAsJsonArray().iterator(); i.hasNext(); ) {
					array.add( i.next().getAsString() );
				}
				element.setAttribute( name, CollectionUtils.toString( array ) );
				continue;
			}

			if ( !jsonElement.isJsonPrimitive() ) {
				continue;
			}

			element.setAttribute( name, jsonElement.getAsString() );
		}
	}
}
