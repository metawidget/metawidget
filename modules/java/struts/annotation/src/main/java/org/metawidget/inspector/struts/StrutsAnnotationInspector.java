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

package org.metawidget.inspector.struts;

import static org.metawidget.inspector.struts.StrutsInspectionResultConstants.*;

import java.util.Map;

import org.metawidget.inspector.impl.BaseObjectInspector;
import org.metawidget.inspector.impl.BaseObjectInspectorConfig;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.util.CollectionUtils;

/**
 * Inspects annotations defined by Metawidget's Struts support (declared in this same package).
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class StrutsAnnotationInspector
	extends BaseObjectInspector {

	//
	// Constructor
	//

	public StrutsAnnotationInspector() {

		this( new BaseObjectInspectorConfig() );
	}

	public StrutsAnnotationInspector( BaseObjectInspectorConfig config ) {

		super( config );
	}

	//
	// Protected methods
	//

	@Override
	protected Map<String, String> inspectProperty( Property property )
		throws Exception {

		Map<String, String> attributes = CollectionUtils.newHashMap();

		// ExpressionLookup

		UiStrutsLookup expressionLookup = property.getAnnotation( UiStrutsLookup.class );

		if ( expressionLookup != null ) {
			attributes.put( STRUTS_LOOKUP_NAME, expressionLookup.name() );
			attributes.put( STRUTS_LOOKUP_PROPERTY, expressionLookup.property() );

			String labelName = expressionLookup.labelName();

			if ( !"".equals( labelName ) ) {
				attributes.put( STRUTS_LOOKUP_LABEL_NAME, labelName );
			}

			String labelProperty = expressionLookup.labelProperty();

			if ( !"".equals( labelProperty ) ) {
				attributes.put( STRUTS_LOOKUP_LABEL_PROPERTY, labelProperty );
			}
		}

		return attributes;
	}
}
