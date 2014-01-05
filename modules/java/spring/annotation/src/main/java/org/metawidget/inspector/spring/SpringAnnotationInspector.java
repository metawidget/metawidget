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

package org.metawidget.inspector.spring;

import static org.metawidget.inspector.spring.SpringInspectionResultConstants.*;

import java.util.Map;

import org.metawidget.inspector.impl.BaseObjectInspector;
import org.metawidget.inspector.impl.BaseObjectInspectorConfig;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.util.CollectionUtils;

/**
 * Inspects annotations defined by Metawidget's Spring support (declared in this same package).
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class SpringAnnotationInspector
	extends BaseObjectInspector {

	//
	// Constructor
	//

	public SpringAnnotationInspector() {

		this( new BaseObjectInspectorConfig() );
	}

	public SpringAnnotationInspector( BaseObjectInspectorConfig config ) {

		super( config );
	}

	//
	// Protected methods
	//

	@Override
	protected Map<String, String> inspectProperty( Property property )
		throws Exception {

		Map<String, String> attributes = CollectionUtils.newHashMap();

		// SpringLookup

		UiSpringLookup springLookup = property.getAnnotation( UiSpringLookup.class );

		if ( springLookup != null ) {
			attributes.put( SPRING_LOOKUP, springLookup.value() );

			String itemValue = springLookup.itemValue();

			if ( !"".equals( itemValue ) ) {
				attributes.put( SPRING_LOOKUP_ITEM_VALUE, itemValue );
			}

			String itemLabel = springLookup.itemLabel();

			if ( !"".equals( itemLabel ) ) {
				attributes.put( SPRING_LOOKUP_ITEM_LABEL, itemLabel );
			}
		}

		return attributes;
	}
}
