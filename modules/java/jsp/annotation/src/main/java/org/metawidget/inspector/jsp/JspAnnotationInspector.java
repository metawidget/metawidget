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

package org.metawidget.inspector.jsp;

import static org.metawidget.inspector.jsp.JspInspectionResultConstants.*;

import java.util.Map;

import org.metawidget.inspector.impl.BaseObjectInspector;
import org.metawidget.inspector.impl.BaseObjectInspectorConfig;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.util.CollectionUtils;

/**
 * Inspects annotations defined by Metawidget's JSP support (declared in this same package).
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class JspAnnotationInspector
	extends BaseObjectInspector {

	//
	// Constructor
	//

	public JspAnnotationInspector() {

		this( new BaseObjectInspectorConfig() );
	}

	public JspAnnotationInspector( BaseObjectInspectorConfig config ) {

		super( config );
	}

	//
	// Protected methods
	//

	@Override
	protected Map<String, String> inspectProperty( Property property )
		throws Exception {

		Map<String, String> attributes = CollectionUtils.newHashMap();

		// UiJspLookup

		UiJspLookup jspLookup = property.getAnnotation( UiJspLookup.class );

		if ( jspLookup != null ) {
			attributes.put( JSP_LOOKUP, jspLookup.value() );
		}

		return attributes;
	}
}
