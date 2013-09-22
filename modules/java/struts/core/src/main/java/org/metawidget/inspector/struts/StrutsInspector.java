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

package org.metawidget.inspector.struts;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.io.InputStream;
import java.util.Map;

import org.metawidget.config.iface.ResourceResolver;
import org.metawidget.inspector.impl.BaseXmlInspector;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Inspector to look for metadata in struts-config.xml files.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class StrutsInspector
	extends BaseXmlInspector {

	//
	// Private statics
	//

	private static final String	FORM_BEANS_ELEMENT		= "form-beans";

	private static final String	FORM_PROPERTY_ELEMENT	= "form-property";

	//
	// Constructor
	//

	public StrutsInspector( StrutsInspectorConfig config ) {

		super( config );
	}

	//
	// Protected methods
	//

	@Override
	protected Element getDocumentElement( ResourceResolver resolver, InputStream... files )
		throws Exception {

		Document document = XmlUtils.newDocument();
		Element root = document.createElement( FORM_BEANS_ELEMENT );
		document.appendChild( root );

		for ( InputStream file : files ) {
			Document documentParsed = XmlUtils.parse( file );
			Element formBeans = XmlUtils.getChildNamed( documentParsed.getDocumentElement(), FORM_BEANS_ELEMENT );

			if ( formBeans == null ) {
				continue;
			}

			XmlUtils.combineElements( root, formBeans, getTopLevelTypeAttribute(), getNameAttribute() );
		}

		return root;
	}

	@Override
	protected Map<String, String> inspectProperty( Element toInspect ) {

		if ( !FORM_PROPERTY_ELEMENT.equals( toInspect.getNodeName() ) ) {
			return null;
		}

		Map<String, String> attributes = CollectionUtils.newHashMap();

		if ( toInspect.hasAttribute( getNameAttribute() ) ) {
			attributes.put( NAME, toInspect.getAttribute( getNameAttribute() ) );
		}

		if ( toInspect.hasAttribute( getTypeAttribute() ) ) {
			attributes.put( TYPE, toInspect.getAttribute( getTypeAttribute() ) );
		}

		return attributes;
	}

	/**
	 * Overridden to search by <code>name=</code>, not <code>type=</code>.
	 * <p>
	 * This is because <code>struts-config.xml</code> requires <code>form-beans/name</code> be
	 * unique, whereas <code>form-beans/type</code> can be, say, <code>DynaActionForm</code>.
	 */

	@Override
	protected String getTopLevelTypeAttribute() {

		return NAME;
	}
}
