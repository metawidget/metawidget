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

package org.metawidget.inspector.jaxb;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.metawidget.inspector.impl.BaseObjectInspector;
import org.metawidget.inspector.impl.BaseObjectInspectorConfig;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.util.CollectionUtils;

/**
 * Inspects annotations defined by Java API for XML Binding (JAXB).
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class JaxbInspector
	extends BaseObjectInspector {

	//
	// Constructor
	//

	public JaxbInspector() {

		this( new BaseObjectInspectorConfig() );
	}

	public JaxbInspector( BaseObjectInspectorConfig config ) {

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

		if ( property.isAnnotationPresent( XmlTransient.class ) ) {
			attributes.put( HIDDEN, TRUE );
		}

		// Required

		XmlElement xmlElement = property.getAnnotation( XmlElement.class );

		if ( xmlElement != null ) {

			// XmlElement overrides XmlTransient
			
			attributes.put( HIDDEN, FALSE );
			
			if ( xmlElement.required() ) {
				attributes.put( REQUIRED, TRUE );
			}
		}

		return attributes;
	}
}
