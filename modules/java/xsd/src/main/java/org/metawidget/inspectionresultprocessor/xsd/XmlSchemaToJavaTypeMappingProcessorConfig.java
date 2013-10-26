// Metawidget
//
// This library is dual licensed under both LGPL and a commercial
// license.
//
// LGPL: this library is free software; you can redistribute it and/or
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
//
// Commercial License: See http://metawidget.org for details

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
