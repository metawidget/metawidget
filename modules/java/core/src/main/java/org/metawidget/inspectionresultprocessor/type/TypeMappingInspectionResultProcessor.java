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

package org.metawidget.inspectionresultprocessor.type;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.inspectionresultprocessor.impl.BaseInspectionResultProcessor;
import org.metawidget.util.CollectionUtils;

/**
 * <code>InspectionResultProcessor</code> to map types from one domain into another.
 * <p>
 * Can be configured to map, say, <code>type="string"</code> to <code>type="java.lang.String"</code>
 * . Consider using one of its pre-configured subclasses for common use cases.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class TypeMappingInspectionResultProcessor<M>
	extends BaseInspectionResultProcessor<M> {

	//
	// Private members
	//

	private Map<String, String>	mTypeMappings	= CollectionUtils.newHashMap();

	private boolean				mRemoveUnmappedTypes;

	//
	// Constructor
	//

	public TypeMappingInspectionResultProcessor( TypeMappingInspectionResultProcessorConfig config ) {

		// Defensive copy

		if ( config.getTypeMappings() != null ) {
			mTypeMappings.putAll( config.getTypeMappings() );
		}

		mRemoveUnmappedTypes = config.isRemoveUnmappedTypes();
	}

	//
	// Protected methods
	//

	@Override
	protected void processAttributes( Map<String, String> attributes, M metawidget ) {

		// Nothing to map?

		if ( !attributes.containsKey( TYPE ) ) {
			return;
		}

		// Map (if any)

		String type = attributes.get( TYPE );

		if ( mTypeMappings.containsKey( type ) ) {
			attributes.put( TYPE, mTypeMappings.get( type ) );
			return;
		}

		// Remove (if configured)

		if ( mRemoveUnmappedTypes ) {
			attributes.put( TYPE, null );
		}
	}
}
