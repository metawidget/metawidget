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

package org.metawidget.inspector.wsdl;

import java.io.InputStream;

import org.metawidget.config.iface.ResourceResolver;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.xsd.XmlSchemaInspector;
import org.metawidget.inspector.xsd.XmlSchemaInspectorConfig;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Element;

/**
 * Inspector to look for metadata in Web Services Description Language (WSDL) files.
 * <p>
 * It extracts the XML Schema from the <parameter>&lt;schema&gt;</parameter> element of the WSDL and
 * parses it for useful metadata.
 * <p>
 * If
 * <code>WsdlInspector</tt> is used for a Java environment, consider using it in conjunction with <code>XmlSchemaToJavaTypeMappingProcessor</code>
 * . For returning results to JavaScript environments, consider
 * <code>JavaToJavaScriptTypeMappingProcessor</code> and <code>XmlUtils.elementToJson</code>.
 * 
 * @author Richard Kennard
 */

public class WsdlInspector
	extends XmlSchemaInspector {

	//
	// Constructor
	//

	public WsdlInspector( XmlSchemaInspectorConfig config ) {

		super( config );
	}

	//
	// Protected methods
	//

	/**
	 * Overridden to return the root of the XML Schema, not the root of the WSDL.
	 */

	@Override
	protected Element getDocumentElement( ResourceResolver resolver, InputStream... files )
		throws Exception {

		Element element = super.getDocumentElement( resolver, files );
		Element types = XmlUtils.getChildNamed( element, "types" );

		if ( types == null ) {
			throw InspectorException.newException( "No types element" );
		}

		Element schema = XmlUtils.getChildNamed( types, "schema" );

		if ( schema == null ) {
			throw InspectorException.newException( "No types/schema element" );
		}

		return schema;
	}
}
