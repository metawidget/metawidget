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

package org.metawidget.inspectionresultprocessor.js;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.metawidget.inspectionresultprocessor.type.TypeMappingInspectionResultProcessor;
import org.metawidget.util.ClassUtils;

/**
 * <code>TypeMappingInspectionResultProcessor</code> to map types from Java types to JavaScript
 * types.
 *
 * @author Richard Kennard
 */

public class JavaToJavaScriptTypeMappingProcessor<M>
	extends TypeMappingInspectionResultProcessor<M> {

	//
	// Protected methods
	//

	/**
	 * Overridden to configure standard JavaScript types by default.
	 */

	@Override
	protected void configureDefaults( Map<String, String> typeMappings ) {

		typeMappings.put( String.class.getName(), "string" );
	}

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
				} else if ( clazz.isArray() || Set.class.isAssignableFrom( clazz ) || List.class.isAssignableFrom( clazz ) ) {
					attributes.put( TYPE, "array" );
				}
			}
		}

		super.processAttributes( attributes, metawidget );
	}

}
