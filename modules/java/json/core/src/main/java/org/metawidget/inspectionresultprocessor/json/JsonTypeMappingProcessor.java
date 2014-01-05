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

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Collection;
import java.util.Map;

import org.metawidget.inspectionresultprocessor.type.TypeMappingInspectionResultProcessor;
import org.metawidget.util.ClassUtils;

/**
 * <code>TypeMappingInspectionResultProcessor</code> to map types from Java types to JSON
 * types.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class JsonTypeMappingProcessor<M>
	extends TypeMappingInspectionResultProcessor<M> {

	//
	// Constructor
	//

	public JsonTypeMappingProcessor() {

		this( new JsonTypeMappingProcessorConfig() );
	}

	public JsonTypeMappingProcessor( JsonTypeMappingProcessorConfig config ) {

		super( config );
	}

	//
	// Protected methods
	//

	/**
	 * Overridden to use <code>isAssignableFrom</code>.
	 */

	@Override
	protected void processAttributes( Map<String, String> attributes, M metawidget ) {

		if ( attributes.containsKey( TYPE ) ) {
			Class<?> clazz = ClassUtils.niceForName( attributes.get( TYPE ) );

			if ( clazz != null ) {
				if ( Number.class.isAssignableFrom( clazz ) ) {
					attributes.put( TYPE, "number" );
					return;
				} else if ( clazz.isArray() || Collection.class.isAssignableFrom( clazz ) ) {
					attributes.put( TYPE, "array" );
					return;
				}
			}
		}

		super.processAttributes( attributes, metawidget );
	}
}
