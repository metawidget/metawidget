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
