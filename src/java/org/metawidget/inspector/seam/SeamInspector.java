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

import javax.xml.parsers.DocumentBuilder;

import org.metawidget.inspector.ResourceResolver;
import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Inspector to look for relevant settings in Seam <code>components.xml</code> files. Specifically:
 * <p>
 * <ul>
 * <li>Delegates <code>jbpm:pageflow-definitions</code> elements to <code>JpdlInspector</code>.
 * </ul>
 *
 * @author Richard Kennard
 */

public class SeamInspector
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
	// Protected methods
	//

	/**
	 * Overridden to automatically drill into Seam <code>components.xml</code> files.
	 */

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
		}

		if ( documentMaster == null )
			return null;

		return documentMaster.getDocumentElement();
	}
}
