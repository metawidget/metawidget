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

package org.metawidget.gwt.client.ui;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.metawidget.impl.base.BaseMetawidgetMixin;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;

/**
 * Base class functionality for Metawidgets on platforms that support <code>com.google.gwt.xml.client</code>.
 *
 * @author Richard Kennard
 */

public abstract class GwtMetawidgetMixin<W>
	extends BaseMetawidgetMixin<W, Document, Element, Node>
{
	//
	//
	// Protected methods
	//
	//

	@Override
	protected Element getFirstElement( Document document )
	{
		return (Element) document.getDocumentElement().getFirstChild();
	}

	@Override
	protected int getChildCount( Element element )
	{
		return element.getChildNodes().getLength();
	}

	@Override
	protected Node getChildAt( Element element, int index )
	{
		return element.getChildNodes().item( index );
	}

	@Override
	protected boolean isElement( Node node )
	{
		return ( node instanceof Element );
	}

	@Override
	protected Map<String, String> getAttributesAsMap( Element element )
	{
		NamedNodeMap nodes = element.getAttributes();

		int length = nodes.getLength();

		if ( length == 0 )
		{
			@SuppressWarnings( { "cast", "unchecked" } )
			Map<String, String> empty = (Map<String, String>) Collections.EMPTY_MAP;
			return empty;
		}

		Map<String, String> attributes = new HashMap<String, String>( length );

		for ( int loop = 0; loop < length; loop++ )
		{
			Node node = nodes.item( loop );
			attributes.put( node.getNodeName(), node.getNodeValue() );
		}

		return attributes;
	}
}
