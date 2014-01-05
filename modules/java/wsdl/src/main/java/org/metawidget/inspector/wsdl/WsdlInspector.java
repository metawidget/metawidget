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
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
