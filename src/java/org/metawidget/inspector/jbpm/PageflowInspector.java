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
 * @author Richard Kennard
 */

public class PageflowInspector
	extends BaseXmlInspector
{
	//
	// Constructor
	//

	public PageflowInspector( BaseXmlInspectorConfig config )
	{
		super( config );
	}

	//
	// Protected methods
	//

	/**
	 * Overridden to search by <code>name=</code>, not <code>type=</code>.
	 */

	@Override
	protected String getTopLevelTypeAttribute()
	{
		return NAME;
	}

	/**
	 * Overridden to use <code>to=</code>, not <code>type=</code>.
	 */

	@Override
	protected String getTypeAttribute()
	{
		return "to";
	}

	/**
	 * Overridden to strip everything but the &lt;page&gt; and &lt;transition&gt; nodes.
	 */

	@Override
	protected void preprocessDocument( Document document )
	{
		Element root = document.getDocumentElement();

		String pagePrefix = root.getAttribute( "name" ) + StringUtils.SEPARATOR_DOT_CHAR;
		String topLevelTypeAttribute = getTopLevelTypeAttribute();

		// For each 'page' node...

		NodeList rootChildren = root.getChildNodes();

		for ( int loopPages = 0, lengthPages = rootChildren.getLength(); loopPages < lengthPages; )
		{
			Node pageNode = rootChildren.item( loopPages );

			if ( !"page".equals( pageNode.getNodeName() ) )
			{
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

			for ( int loopTransition = 0, lengthTransitions = pageChildren.getLength(); loopTransition < lengthTransitions; )
			{
				Node transitionNode = pageChildren.item( loopTransition );

				if ( !"transition".equals( transitionNode.getNodeName() ) )
				{
					pageNode.removeChild( transitionNode );
					lengthTransitions--;
					continue;
				}

				loopTransition++;
			}
		}
	}

	@Override
	protected Map<String, String> inspectProperty( Element toInspect )
	{
		return null;
	}

	@Override
	protected Map<String, String> inspectAction( Element toInspect )
	{
		Map<String, String> attributes = CollectionUtils.newHashMap();

		// Name

		attributes.put( NAME, toInspect.getAttribute( NAME ) );

		return attributes;
	}
}
