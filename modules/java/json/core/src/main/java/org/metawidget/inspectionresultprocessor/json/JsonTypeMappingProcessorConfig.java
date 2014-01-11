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

package org.metawidget.inspectionresultprocessor.json;

import java.awt.Color;
import java.util.Date;

import org.metawidget.inspectionresultprocessor.type.TypeMappingInspectionResultProcessorConfig;

/**
 * Configures a JsonTypeMappingProcessor with standard Java types.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class JsonTypeMappingProcessorConfig
	extends TypeMappingInspectionResultProcessorConfig {

	//
	// Constructor
	//

	public JsonTypeMappingProcessorConfig() {

		// Primitive datatypes as defined by JavaScript

		setTypeMapping( byte.class.getName(), "number" );
		setTypeMapping( short.class.getName(), "number" );
		setTypeMapping( int.class.getName(), "number" );
		setTypeMapping( long.class.getName(), "number" );
		setTypeMapping( float.class.getName(), "number" );
		setTypeMapping( double.class.getName(), "number" );
		setTypeMapping( boolean.class.getName(), "boolean" );
		setTypeMapping( char.class.getName(), "string" );
		setTypeMapping( String.class.getName(), "string" );
		setTypeMapping( Boolean.class.getName(), "boolean" );
		setTypeMapping( Character.class.getName(), "string" );
		setTypeMapping( Date.class.getName(), "date" );
		setTypeMapping( Color.class.getName(), "color" );

		setRemoveUnmappedTypes( true );
	}
}
