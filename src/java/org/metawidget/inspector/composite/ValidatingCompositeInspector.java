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

package org.metawidget.inspector.composite;

import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.metawidget.inspector.iface.InspectorException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * CompositeInspector that validates the inspection result XMLs against the
 * <code>inspection-result-1.0.xsd</code> schema.
 * <p>
 * This is useful for debugging when developing your own <code>Inspector</code>, but it is less
 * performant and not all environments (eg. J2SE 1.4, Android) support schema validation. Android is
 * particularly bad because its Dalvik preprocessor balks at unsupported classes even if they're
 * wrapped in a <code>ClassNotFoundException</code>.
 *
 * @author Richard Kennard
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

		// TODO: make configurable

		InputStream in = config.getResourceResolver().openResource( "org/metawidget/inspector/inspection-result-1.0.xsd" );

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
	protected void validate( Document document )
		throws Exception {

		mSchema.newValidator().validate( new DOMSource( document ) );
	}
}
