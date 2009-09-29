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

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;

import org.metawidget.inspector.ResourceResolver;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.BaseXmlInspector;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.XmlUtils;
import org.metawidget.util.simple.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Inspector to look for relevant settings in <code>pageflow.jpdl.xml</code> files.
 * <p>
 * As a convenience, can also be pointed at a Seam <code>components.xml</code> file which has a
 * <code>jbpm:pageflow-definitions</code> element.
 *
 * @author Richard Kennard
 */

public class JbpmInspector
	extends BaseXmlInspector
{
	//
	// Private statics
	//

	/**
	 * In Seam, pageflows can be defined in <code>components.xml</code> in a
	 * <code>pageflow-definitions.xml</code> block.
	 * <p>
	 * They can also be scanned for automatically on startup (see
	 * https://jira.jboss.org/jira/browse/JBSEAM-979).
	 */

	private final static String			SEAM_COMPONENTS_ELEMENT	= "components";

	private final static String			SEAM_PAGEFLOWS_ELEMENT	= "pageflow-definitions";

	private final static String			JBPM_NAMESPACE			= "jbpm";

	private final static String			JBPM_PAGEFLOW_ELEMENT	= "pageflow-definition";

	private final static InputStream[]	EMPTY_INPUTSTREAM_ARRAY	= new InputStream[0];

	//
	// Constructor
	//

	public JbpmInspector( JbpmInspectorConfig config )
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

	@Override
	protected String getTypeAttribute()
	{
		return "to";
	}

	/**
	 * Overridden to automatically drill into Seam <code>components.xml</code> files.
	 */

	@Override
	protected Element getDocumentElement( DocumentBuilder builder, ResourceResolver resolver, InputStream... files )
		throws Exception
	{
		Document documentMaster = null;

		for ( InputStream file : files )
		{
			Document documentParsed = builder.parse( file );

			if ( !documentParsed.hasChildNodes() )
				continue;

			// If the document is a components.xml file...

			Element parsed = documentParsed.getDocumentElement();
			String nodeName = parsed.getNodeName();

			if ( SEAM_COMPONENTS_ELEMENT.equals( nodeName ) )
			{
				// ...look up each pageflow...

				Element value = XmlUtils.getChildNamed( documentParsed.getDocumentElement(), JBPM_NAMESPACE, SEAM_PAGEFLOWS_ELEMENT, "value" );

				if ( value == null )
					throw InspectorException.newException( "No " + SEAM_PAGEFLOWS_ELEMENT + " defined" );

				List<InputStream> inputStreamList = CollectionUtils.newArrayList();

				while ( value != null )
				{
					inputStreamList.add( resolver.openResource( value.getTextContent() ));
					value = XmlUtils.getSiblingNamed( value, "value" );
				}

				// ...and combine them

				parsed = getDocumentElement( builder, resolver, inputStreamList.toArray( EMPTY_INPUTSTREAM_ARRAY ) );

				if ( documentMaster == null || !documentMaster.hasChildNodes() )
				{
					documentMaster = parsed.getOwnerDocument();
					continue;
				}
			}

			// ...otherwise, read pageflow-definition files

			else if ( JBPM_PAGEFLOW_ELEMENT.equals( nodeName ) )
			{
				preprocessDocument( documentParsed );

				if ( documentMaster == null || !documentMaster.hasChildNodes() )
				{
					documentMaster = documentParsed;
					continue;
				}
			}
			else
			{
				throw InspectorException.newException( "Expected an XML document starting with '" + SEAM_COMPONENTS_ELEMENT + "' or '" + JBPM_PAGEFLOW_ELEMENT + "', but got '" + nodeName + "'" );
			}

			XmlUtils.combineElements( documentMaster.getDocumentElement(), parsed, getTopLevelTypeAttribute(), getNameAttribute() );
		}

		if ( documentMaster == null )
			return null;

		return documentMaster.getDocumentElement();
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
