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

package org.metawidget.mixin.w3c;

import java.util.Map;

import org.metawidget.mixin.base.BaseMetawidgetMixin;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Mixin for platforms that support <code>org.w3c.dom</code>.
 *
 * @author Richard Kennard
 */

public abstract class MetawidgetMixin<W>
	extends BaseMetawidgetMixin<W, Element>
{
	//
	// Protected methods
	//

	@Override
	protected Element getFirstElement( String xml )
	{
		Document document = XmlUtils.documentFromString( xml );

		// TODO: test ignoring any indentation TextNodes
		// Get the first node (ignoring any indentation TextNodes)

		Node node = document.getDocumentElement().getFirstChild();

		while( node != null )
		{
			if ( node instanceof Element )
				return (Element) node;

			node = node.getNextSibling();
		}

		return null;
	}

	@Override
	protected int getChildCount( Element element )
	{
		return element.getChildNodes().getLength();
	}

	@Override
	protected Element getChildAt( Element element, int index )
	{
		// Get the indexed node (ignoring any indentation TextNodes)

		NodeList nodes = element.getChildNodes();

		int actualIndex = 0;

		for( int loop = 0, length = nodes.getLength(); loop < length; loop++ )
		{
			Node node = nodes.item( loop );

			if ( !( node instanceof Element ))
				continue;

			if ( actualIndex == index )
				return (Element) node;

			actualIndex++;
		}

		return null;
	}

	@Override
	protected String getElementName( Element element )
	{
		return element.getNodeName();
	}

	@Override
	protected Map<String, String> getAttributesAsMap( Element element )
	{
		return XmlUtils.getAttributesAsMap( element );
	}

	@Override
	protected boolean isMetawidget( W widget )
	{
		return getClass().getDeclaringClass().isAssignableFrom( widget.getClass() );
	}
}
