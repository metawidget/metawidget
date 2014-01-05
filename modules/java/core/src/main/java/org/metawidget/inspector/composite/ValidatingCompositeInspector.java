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

package org.metawidget.inspector.composite;

import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.metawidget.inspector.iface.Inspector;
import org.metawidget.inspector.iface.InspectorException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * CompositeInspector that validates the inspection result XMLs against the
 * <code>inspection-result-1.0.xsd</code> schema.
 * <p>
 * This is useful for debugging when developing your own <code>Inspector</code>, but it is less
 * performant and not all environments (eg. Android) support schema validation. Android is
 * particularly bad because its Dalvik preprocessor balks at unsupported classes even if they're
 * wrapped in a <code>ClassNotFoundException</code>.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class ValidatingCompositeInspector
	extends CompositeInspector {

	//
	// Private members
	//

	private final Schema	mSchema;

	//
	// Constructor
	//

	public ValidatingCompositeInspector( ValidatingCompositeInspectorConfig config ) {

		super( config );

		InputStream in = config.getSchema();

		try {
			mSchema = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI ).newSchema( new StreamSource( in ) );
		} catch ( SAXException e ) {
			throw InspectorException.newException( e );
		}
	}

	//
	// Protected methods
	//

	@Override
	protected Document runInspector( Inspector inspector, Object toInspect, String type, String... names )
		throws Exception {

		Document document = super.runInspector( inspector, toInspect, type, names );

		if ( document != null ) {
			mSchema.newValidator().validate( new DOMSource( document ) );
		}

		return document;
	}
}
