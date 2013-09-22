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
