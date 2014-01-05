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

package org.metawidget.inspector.jbpm;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.inspector.impl.BaseXmlInspector;
import org.metawidget.inspector.impl.BaseXmlInspectorConfig;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Inspector to look for relevant settings in JBoss jBPM pageflow jPDL files.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class PageflowInspector
	extends BaseXmlInspector {

	//
	// Constructor
	//

	public PageflowInspector( BaseXmlInspectorConfig config ) {

		super( config );
	}

	//
	// Protected methods
	//

	/**
	 * Overridden to search by <code>name=</code>, not <code>type=</code>.
	 */

	@Override
	protected String getTopLevelTypeAttribute() {

		return NAME;
	}

	/**
	 * Overridden to use <code>to=</code>, not <code>type=</code>.
	 */

	@Override
	protected String getTypeAttribute() {

		return "to";
	}

	/**
	 * Overridden to strip everything but the &lt;page&gt; and &lt;transition&gt; nodes.
	 */

	@Override
	protected void preprocessDocument( Document document ) {

		Element root = document.getDocumentElement();

		String pagePrefix = root.getAttribute( "name" ) + StringUtils.SEPARATOR_DOT_CHAR;
		String topLevelTypeAttribute = getTopLevelTypeAttribute();

		// For each 'page' node...

		NodeList rootChildren = root.getChildNodes();

		for ( int loopPages = 0, lengthPages = rootChildren.getLength(); loopPages < lengthPages; ) {
			Node pageNode = rootChildren.item( loopPages );

			if ( !"page".equals( pageNode.getNodeName() ) ) {
				root.removeChild( pageNode );
				lengthPages--;
				continue;
			}

			loopPages++;

			// ...prefix the page name...

			Element pageElement = (Element) pageNode;
			String pageName = pageElement.getAttribute( topLevelTypeAttribute );
			pageElement.setAttribute( topLevelTypeAttribute, pagePrefix + pageName );

			// ...and for each 'transition' node

			NodeList pageChildren = pageNode.getChildNodes();

			for ( int loopTransition = 0, lengthTransitions = pageChildren.getLength(); loopTransition < lengthTransitions; ) {
				Node transitionNode = pageChildren.item( loopTransition );

				if ( !"transition".equals( transitionNode.getNodeName() ) ) {
					pageNode.removeChild( transitionNode );
					lengthTransitions--;
					continue;
				}

				loopTransition++;
			}
		}
	}

	@Override
	protected Map<String, String> inspectAction( Element toInspect ) {

		Map<String, String> attributes = CollectionUtils.newHashMap();

		// Name

		attributes.put( NAME, toInspect.getAttribute( NAME ) );

		return attributes;
	}
}
