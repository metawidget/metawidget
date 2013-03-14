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

import java.util.Map;

import org.metawidget.inspectionresultprocessor.type.TypeMappingInspectionResultProcessor;
import org.metawidget.util.simple.StringUtils;

/**
 * <code>TypeMappingInspectionResultProcessor</code> to map types from XML Schemas to Java types.
 *
 * @author Richard Kennard
 */

public class XmlSchemaJavaTypeMappingProcessor<M>
	extends TypeMappingInspectionResultProcessor<M> {

	//
	// Protected methods
	//

	/**
	 * Overridden to configure standard Java types by default.
	 */

	@Override
	protected void configureDefaults( Map<String, String> typeMappings ) {

		typeMappings.put( "string", String.class.getName() );
		typeMappings.put( "integer", int.class.getName() );
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
