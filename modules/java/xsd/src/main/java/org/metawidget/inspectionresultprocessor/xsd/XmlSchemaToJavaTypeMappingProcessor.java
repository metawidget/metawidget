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

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.inspectionresultprocessor.type.TypeMappingInspectionResultProcessor;
import org.metawidget.util.simple.StringUtils;

/**
 * <code>TypeMappingInspectionResultProcessor</code> to map types from XML Schemas to Java types.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class XmlSchemaToJavaTypeMappingProcessor<M>
	extends TypeMappingInspectionResultProcessor<M> {

	//
	// Constructor
	//

	public XmlSchemaToJavaTypeMappingProcessor() {

		this( new XmlSchemaToJavaTypeMappingProcessorConfig() );
	}

	public XmlSchemaToJavaTypeMappingProcessor( XmlSchemaToJavaTypeMappingProcessorConfig config ) {

		super( config );
	}

	//
	// Protected methods
	//

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
