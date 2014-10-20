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

		JsonIgnore jsonIgnore = property.getAnnotation( JsonIgnore.class );

		if ( jsonIgnore != null && jsonIgnore.value() ) {
			attributes.put( HIDDEN, TRUE );
		}

		return attributes;
	}
}
