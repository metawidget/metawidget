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

import org.metawidget.pipeline.base.BasePipeline;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

/**
 * Pipeline for platforms that use <code>com.google.gwt.xml.client</code>.
 *
 * @author Richard Kennard
 */

public abstract class GwtPipeline<W, C extends W, M extends C>
	extends BasePipeline<W, C, Element, M> {

	//
	// Public methods
	//

	/**
	 * Returns the first WidgetProcessor in this pipeline's list of WidgetProcessors (ie. as added
	 * by <code>addWidgetProcessor</code>) that equals the given class. Note this implementation
	 * does not use <code>isAssignableFrom</code>, because GWT does not support it.
	 *
	 * @param widgetProcessorClass
	 *            the class to find. Returns <code>null</code> if no such WidgetProcessor
	 */

	@SuppressWarnings( "unchecked" )
	public <T> T getWidgetProcessor( Class<T> widgetProcessorClass ) {

		if ( getWidgetProcessors() == null ) {
			return null;
		}

		for ( WidgetProcessor<W, M> widgetProcessor : getWidgetProcessors() ) {
			if ( widgetProcessorClass.equals( widgetProcessor.getClass() ) ) {
				return (T) widgetProcessor;
			}
		}

		return null;
	}

	@Override
	public String elementToString( Element element ) {

		// TODO: does this work?

		return element.toString();
	}

	//
	// Protected methods
	//

	@Override
	protected Element stringToElement( String xml ) {

		Document document = XMLParser.parse( xml );
		return document.getDocumentElement();
	}

	@Override
	protected int getChildCount( Element element ) {

		return element.getChildNodes().getLength();
	}

	@Override
	protected Element getChildAt( Element element, int index ) {

		// Get the indexed node (ignoring any indentation TextNodes)

		NodeList nodes = element.getChildNodes();

		int actualIndex = 0;

		for ( int loop = 0, length = nodes.getLength(); loop < length; loop++ ) {
			Node node = nodes.item( loop );

			if ( !( node instanceof Element ) ) {
				continue;
			}

			if ( actualIndex == index ) {
				return (Element) node;
			}

			actualIndex++;
		}

		return null;
	}

	@Override
	protected String getElementName( Element element ) {

		return element.getNodeName();
	}

	@Override
	protected Map<String, String> getAttributesAsMap( Element element ) {

		NamedNodeMap nodes = element.getAttributes();

		int length = nodes.getLength();

		if ( length == 0 ) {
			@SuppressWarnings( { "cast", "unchecked" } )
			Map<String, String> empty = (Map<String, String>) Collections.EMPTY_MAP;
			return empty;
		}

		Map<String, String> attributes = new HashMap<String, String>( length );

		for ( int loop = 0; loop < length; loop++ ) {
			Node node = nodes.item( loop );
			attributes.put( node.getNodeName(), node.getNodeValue() );
		}

		return attributes;
	}
}
