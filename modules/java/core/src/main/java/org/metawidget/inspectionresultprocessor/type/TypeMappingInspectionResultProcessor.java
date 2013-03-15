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
 * @author Richard Kennard
 */

public class TypeMappingInspectionResultProcessor<M>
	extends BaseInspectionResultProcessor<M> {

	//
	// Private members
	//

	private Map<String, String>	mTypeMappings	= CollectionUtils.newHashMap();

	//
	// Constructor
	//

	public TypeMappingInspectionResultProcessor( TypeMappingInspectionResultProcessorConfig config ) {

		this();

		// Defensive copy

		if ( config.getTypeMappings() != null ) {
			mTypeMappings.putAll( config.getTypeMappings() );
		}
	}

	/**
	 * Protected constructor for subclasses who just define defaults.
	 */

	protected TypeMappingInspectionResultProcessor() {

		// Defaults

		configureDefaults( mTypeMappings );
	}

	//
	// Protected methods
	//

	/**
	 * Subclasses can override this method to configure default type mappings.
	 * <p>
	 * Does nothing by default.
	 * 
	 * @param typeMappings
	 *            to add to. Never null
	 */

	protected void configureDefaults( Map<String, String> typeMappings ) {

		// Do nothing by default
	}

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
		}
	}
}
