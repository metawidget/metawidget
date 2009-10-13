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

package org.metawidget.inspector.seam;

import java.io.InputStream;
import java.util.List;

import org.metawidget.inspector.ResourceResolver;
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
 * <li>Delegates <code>jbpm:pageflow-definitions</code> elements in <code>components.xml</code> to <code>PageflowInspector</code>.
 * </ul>
 *
 * @author Richard Kennard
 */

public class SeamInspector
	implements Inspector
{
	//
	// Private statics
	//

	/**
	 * In Seam, pageflows can be defined in <code>components.xml</code> in a
	 * <code>pageflow-definitions.xml</code> block.
	 * <p>
	 * Of course, this will not work if they are set to be scanned for automatically on startup (see
	 * https://jira.jboss.org/jira/browse/JBSEAM-979).
	 */

	private final static String			COMPONENTS_ELEMENT		= "components";

	private final static String			JBPM_ELEMENT			= "jbpm";

	private final static String			PAGEFLOW_DEFINITIONS_ELEMENT		= "pageflow-definitions";

	//
	// Private members
	//

	private PageflowInspector				mPageflowInspector;

	//
	// Constructors
	//

	public SeamInspector()
	{
		this( new SeamInspectorConfig() );
	}

	public SeamInspector( SeamInspectorConfig config )
	{
		// components.xml

		Element root;

		try
		{
			Document documentParsed = XmlUtils.newDocumentBuilder().parse( config.getComponentsInputStream() );
			root = documentParsed.getDocumentElement();
		}
		catch( Exception e )
		{
			throw InspectorException.newException( e );
		}

		String nodeName = root.getNodeName();

		if ( !COMPONENTS_ELEMENT.equals( nodeName ) )
			throw InspectorException.newException( "Expected an XML document starting with '" + COMPONENTS_ELEMENT + "', but got '" + nodeName + "'" );

		ResourceResolver resolver = config.getResourceResolver();

		// <pageflow-definitions />

		List<InputStream> pageflowDefinitionStreams = CollectionUtils.newArrayList();
		Element pageflowValue = XmlUtils.getChildNamed( root, JBPM_ELEMENT, PAGEFLOW_DEFINITIONS_ELEMENT, "value" );

		while ( pageflowValue != null )
		{
			pageflowDefinitionStreams.add( resolver.openResource( pageflowValue.getTextContent() ) );
			pageflowValue = XmlUtils.getSiblingNamed( pageflowValue, "value" );
		}

		if ( !pageflowDefinitionStreams.isEmpty() )
		{
			BaseXmlInspectorConfig jpdlConfig = new BaseXmlInspectorConfig();
			jpdlConfig.setInputStreams( pageflowDefinitionStreams.toArray( new InputStream[pageflowDefinitionStreams.size()] ) );
			mPageflowInspector = new PageflowInspector( jpdlConfig );
		}
	}

	//
	// Public methods
	//

	@Override
	public String inspect( Object toInspect, String type, String... names )
	{
		// Pageflow

		if ( mPageflowInspector != null )
			return mPageflowInspector.inspect( toInspect, type, names );

		return null;
	}
}
