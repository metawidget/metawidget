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

import org.metawidget.inspector.ConfigReader;
import org.metawidget.inspector.ResourceResolver;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.inspector.impl.BaseXmlInspector;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Inspector to look for relevant settings in hibernate.cfg.xml and mapping.hbm.xml files.
 *
 * @author Richard Kennard
 */

public class JbpmInspector
	extends BaseXmlInspector
{
	//
	//
	// Private statics
	//
	//

	private final static String		SEAM_COMPONENTS_ELEMENT	= "components";

	private final static String		JBPM_PAGEFLOW_ELEMENT	= "pageflow-definition";

	private final static String[]	EMPTY_STRING_ARRAY		= new String[0];

	//
	//
	// Constructor
	//
	//

	public JbpmInspector()
	{
		this( new JbpmInspectorConfig() );
	}

	public JbpmInspector( JbpmInspectorConfig config )
	{
		this( config, new ConfigReader() );
	}

	public JbpmInspector( ResourceResolver resolver )
	{
		this( new JbpmInspectorConfig(), resolver );
	}

	public JbpmInspector( JbpmInspectorConfig config, ResourceResolver resolver )
	{
		super( config, resolver );
	}

	//
	//
	// Protected methods
	//
	//

	/**
	 * Overriden to search by <code>name=</code>, not <code>type=</code>.
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
	 * Overriden to automatically drill into Hibernate Configuration files.
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

				Element jbpm = XmlUtils.getChildNamed( documentParsed.getDocumentElement(), "jbpm" );
				Element pageflow = XmlUtils.getChildNamed( jbpm, "pageflow-definitions" );
				Element value = XmlUtils.getChildNamed( pageflow, "value" );

				List<String> fileList = CollectionUtils.newArrayList();

				while ( value != null )
				{
					fileList.add( value.getTextContent() );
					value = XmlUtils.getSiblingNamed( value, "value" );
				}

				// ...and combine them

				parsed = getDocumentElement( builder, resolver, fileList.toArray( EMPTY_STRING_ARRAY ) );

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
	 * Overriden to strip everything but the &lt;page&gt; and &lt;transition&gt; nodes.
	 */

	@Override
	protected void preprocessDocument( Document document )
	{
		Element root = document.getDocumentElement();

		// For each 'page' node...

		NodeList rootChildren = root.getChildNodes();

		for ( int loopPages = 0, lengthPages = rootChildren.getLength(); loopPages < lengthPages; )
		{
			Node pageNode = rootChildren.item( loopPages );

			if ( !"page".equals( pageNode.getNodeName() ))
			{
				root.removeChild( pageNode );
				lengthPages--;
				continue;
			}

			loopPages++;

			// ...and for each 'transition' node

			NodeList pageChildren = pageNode.getChildNodes();

			for ( int loopTransition = 0, lengthTransitions = pageChildren.getLength(); loopTransition < lengthTransitions; )
			{
				Node transitionNode = pageChildren.item( loopTransition );

				if ( !"transition".equals( transitionNode.getNodeName() ))
				{
					pageNode.removeChild( transitionNode );
					lengthTransitions--;
					continue;
				}

				loopTransition++;
			}
		}
	}

	/**
	 * Overriden to return 'action' nodes.
	 */

	@Override
	protected Element inspect( Document toAddTo, Element toInspect )
	{
		Map<String, String> attributes = inspect( toInspect );

		if ( attributes == null || attributes.isEmpty() )
			return null;

		// ...create an element...

		Element child = toAddTo.createElementNS( NAMESPACE, ACTION );
		XmlUtils.setMapAsAttributes( child, attributes );

		return child;
	}

	@Override
	protected Map<String, String> inspect( Element toInspect )
	{
		Map<String, String> attributes = CollectionUtils.newHashMap();

		// Name

		attributes.put( NAME, toInspect.getAttribute( NAME ) );

		return attributes;
	}
}
