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

package org.metawidget.inspector.seam;

import java.io.InputStream;
import java.util.List;

import org.metawidget.config.iface.ResourceResolver;
import org.metawidget.inspector.iface.Inspector;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.BaseXmlInspectorConfig;
import org.metawidget.inspector.jbpm.PageflowInspector;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Inspector to look for relevant settings in Seam XML files. Specifically:
 * <p>
 * <ul>
 * <li>Delegates <code>jbpm:pageflow-definitions</code> elements from <code>components.xml</code> to
 * <code>PageflowInspector</code>.
 * </ul>
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class SeamInspector
	implements Inspector {

	//
	// Private statics
	//

	private static final String		JBPM_ELEMENT					= "jbpm";

	/**
	 * In Seam, pageflows can be defined in <code>components.xml</code> in a
	 * <code>pageflow-definitions/code> block.
	 */

	private static final String		PAGEFLOW_DEFINITIONS_ELEMENT	= "pageflow-definitions";

	//
	// Private members
	//

	private final PageflowInspector	mPageflowInspector;

	//
	// Constructors
	//

	public SeamInspector() {

		this( new SeamInspectorConfig() );
	}

	public SeamInspector( SeamInspectorConfig config ) {

		// components.xml

		Element root;

		try {
			Document documentParsed = XmlUtils.parse( config.getComponentsInputStream() );
			root = documentParsed.getDocumentElement();
		} catch ( Exception e ) {
			throw InspectorException.newException( e );
		}

		ResourceResolver resolver = config.getResourceResolver();

		// <pageflow-definitions />

		List<InputStream> pageflowDefinitionStreams = CollectionUtils.newArrayList();
		Element pageflowValue = XmlUtils.getChildNamed( root, JBPM_ELEMENT, PAGEFLOW_DEFINITIONS_ELEMENT, "value" );

		while ( pageflowValue != null ) {
			pageflowDefinitionStreams.add( resolver.openResource( pageflowValue.getTextContent() ) );
			pageflowValue = XmlUtils.getSiblingNamed( pageflowValue, "value" );
		}

		if ( pageflowDefinitionStreams.isEmpty() ) {
			mPageflowInspector = null;
		} else {
			BaseXmlInspectorConfig jpdlConfig = new BaseXmlInspectorConfig();
			jpdlConfig.setInputStreams( pageflowDefinitionStreams.toArray( new InputStream[pageflowDefinitionStreams.size()] ) );
			mPageflowInspector = new PageflowInspector( jpdlConfig );
		}
	}

	//
	// Public methods
	//

	public String inspect( Object toInspect, String type, String... names ) {

		// Pageflow

		if ( mPageflowInspector != null ) {
			return mPageflowInspector.inspect( toInspect, type, names );
		}

		return null;
	}
}
