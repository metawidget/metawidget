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

package org.metawidget.inspectionresultprocessor.json.schema;

import java.awt.Color;
import java.util.Date;
import java.util.List;

import org.metawidget.inspectionresultprocessor.type.TypeMappingInspectionResultProcessorConfig;

/**
 * Configures a TypeMappingInspectionResultProcessor with standard Java types. Can be useful for,
 * say, displaying a JSON object using a Java-based UI framework.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class JsonSchemaTypeMappingProcessorConfig
	extends TypeMappingInspectionResultProcessorConfig {

	//
	// Constructor
	//

	public JsonSchemaTypeMappingProcessorConfig() {

		// Primitive datatypes as defined by JavaScript

		setTypeMapping( "number", int.class.getName() );
		setTypeMapping( "boolean", boolean.class.getName() );
		setTypeMapping( "string", String.class.getName() );
		setTypeMapping( "date", Date.class.getName() );
		setTypeMapping( "color", Color.class.getName() );
		setTypeMapping( "array", List.class.getName() );

		// Don't setRemoveUnmappedTypes - often times it is better to fall through. See example in
		// inspectors.xml
	}
}
