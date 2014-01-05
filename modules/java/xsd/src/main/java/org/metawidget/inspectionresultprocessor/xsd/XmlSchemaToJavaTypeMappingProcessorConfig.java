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

package org.metawidget.inspectionresultprocessor.xsd;

import java.math.BigDecimal;
import java.util.Date;

import org.metawidget.inspectionresultprocessor.type.TypeMappingInspectionResultProcessorConfig;

/**
 * Configures a XmlSchemaToJavaTypeMappingProcessor with standard Java types.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class XmlSchemaToJavaTypeMappingProcessorConfig
	extends TypeMappingInspectionResultProcessorConfig {

	//
	// Constructor
	//

	public XmlSchemaToJavaTypeMappingProcessorConfig() {

		// Primitive datatypes as defined by section 3.2 of XML Schema Part 2: Datatypes Second
		// Edition

		setTypeMapping( "string", String.class.getName() );
		// boolean->boolean
		setTypeMapping( "decimal", BigDecimal.class.getName() );
		// float->float
		// double->double
		setTypeMapping( "duration", long.class.getName() );
		setTypeMapping( "dateTime", Date.class.getName() );
		setTypeMapping( "time", Date.class.getName() );
		setTypeMapping( "date", Date.class.getName() );

		// Derived datatypes as defined by section 3.3 of XML Schema Part 2: Datatypes Second
		// Edition

		setTypeMapping( "token", String.class.getName() );
		setTypeMapping( "normalizedString", String.class.getName() );
		setTypeMapping( "integer", int.class.getName() );
		setTypeMapping( "nonPositiveInteger", int.class.getName() );
		setTypeMapping( "negativeInteger", int.class.getName() );
		// long->long
		// int->int
		// short->short
		// byte->byte
		setTypeMapping( "nonNegativeInteger", int.class.getName() );
		setTypeMapping( "unsignedLong", long.class.getName() );
		setTypeMapping( "unsignedInt", int.class.getName() );
		setTypeMapping( "unsignedShort", short.class.getName() );
		setTypeMapping( "unsignedByte", byte.class.getName() );
		setTypeMapping( "positiveInteger", int.class.getName() );
	}
}
