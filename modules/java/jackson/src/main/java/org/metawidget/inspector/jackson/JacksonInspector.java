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

package org.metawidget.inspector.jackson;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.metawidget.inspector.impl.BaseObjectInspector;
import org.metawidget.inspector.impl.BaseObjectInspectorConfig;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.util.CollectionUtils;

/**
 * Inspects annotations defined by Jackson.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class JacksonInspector
	extends BaseObjectInspector {

	//
	// Constructor
	//

	public JacksonInspector() {

		this( new BaseObjectInspectorConfig() );
	}

	public JacksonInspector( BaseObjectInspectorConfig config ) {

		super( config );
	}

	//
	// Protected methods
	//

	@Override
	protected Map<String, String> inspectProperty( Property property )
		throws Exception {

		Map<String, String> attributes = CollectionUtils.newHashMap();

		// Hidden

		if ( property.isAnnotationPresent( JsonIgnore.class ) ) {
			attributes.put( HIDDEN, TRUE );
		}

		return attributes;
	}
}
