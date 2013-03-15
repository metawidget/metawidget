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

package org.metawidget.inspectionresultprocessor.xsd;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.metawidget.inspectionresultprocessor.type.TypeMappingInspectionResultProcessor;
import org.metawidget.util.simple.StringUtils;

/**
 * <code>TypeMappingInspectionResultProcessor</code> to map types from XML Schemas to Java types.
 *
 * @author Richard Kennard
 */

public class XmlSchemaToJavaTypeMappingProcessor<M>
	extends TypeMappingInspectionResultProcessor<M> {

	//
	// Protected methods
	//

	/**
	 * Overridden to configure standard Java types by default.
	 */

	@Override
	protected void configureDefaults( Map<String, String> typeMappings ) {

		// Primitive datatypes as defined by section 3.2 of XML Schema Part 2: Datatypes Second
		// Edition

		typeMappings.put( "string", String.class.getName() );
		// boolean->boolean
		typeMappings.put( "decimal", BigDecimal.class.getName() );
		// float->float
		// double->double
		typeMappings.put( "duration", long.class.getName() );
		typeMappings.put( "dateTime", Date.class.getName() );
		typeMappings.put( "time", Date.class.getName() );
		typeMappings.put( "date", Date.class.getName() );

		// Derived datatypes as defined by section 3.3 of XML Schema Part 2: Datatypes Second
		// Edition

		typeMappings.put( "normalizedString", String.class.getName() );
		typeMappings.put( "integer", int.class.getName() );
		typeMappings.put( "nonPositiveInteger", int.class.getName() );
		typeMappings.put( "negativeInteger", int.class.getName() );
		// long->long
		// int->int
		// short->short
		// byte->byte
		typeMappings.put( "nonNegativeInteger", int.class.getName() );
		typeMappings.put( "unsignedLong", long.class.getName() );
		typeMappings.put( "unsignedInt", int.class.getName() );
		typeMappings.put( "unsignedShort", short.class.getName() );
		typeMappings.put( "unsignedByte", byte.class.getName() );
		typeMappings.put( "positiveInteger", int.class.getName() );
	}

	/**
	 * Overridden to strip off XML Schema namespaces.
	 */

	@Override
	protected void processAttributes( Map<String, String> attributes, M metawidget ) {

		if ( attributes.containsKey( TYPE ) ) {
			attributes.put( TYPE, StringUtils.substringAfter( attributes.get( TYPE ), StringUtils.SEPARATOR_COLON ) );
		}

		super.processAttributes( attributes, metawidget );
	}

}
