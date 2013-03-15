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

import java.util.Collection;
import java.util.Map;

import org.metawidget.inspectionresultprocessor.type.TypeMappingInspectionResultProcessor;
import org.metawidget.util.ClassUtils;

/**
 * <code>TypeMappingInspectionResultProcessor</code> to map types from Java types to JavaScript
 * types.
 * <p>
 * For Maven users, this module can be included using the <code>classes</code> classifier (e.g.
 * <code>org.metawidget.modules.js:metawidget-corejs:classes</code>).
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
				} else if ( Boolean.class.isAssignableFrom( clazz ) ) {
					attributes.put( TYPE, "boolean" );
				} else if ( Character.class.isAssignableFrom( clazz ) ) {
					attributes.put( TYPE, "string" );
				} else if ( clazz.isArray() || Collection.class.isAssignableFrom( clazz )) {
					attributes.put( TYPE, "array" );
				}
			}
		}

		super.processAttributes( attributes, metawidget );
	}

}
