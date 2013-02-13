// Metawidget
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
 * @author Richard Kennard
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

			if ( xmlElement.required() ) {
				attributes.put( REQUIRED, TRUE );
			}
		}

		return attributes;
	}
}
