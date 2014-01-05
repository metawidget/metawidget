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

package org.metawidget.gwt.client.ui;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.metawidget.pipeline.base.BasePipeline;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.XMLParser;

/**
 * Pipeline for platforms that use <code>com.google.gwt.xml.client</code>.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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

	//
	// Protected methods
	//

	@Override
	protected Element stringToElement( String xml ) {

		return XMLParser.parse( xml ).getDocumentElement();
	}

	@Override
	protected String elementToString( Element element ) {

		return element.toString();
	}

	@Override
	protected Element getFirstChildElement( Element parent ) {

		Node node = parent.getFirstChild();

		while( node != null && !( node instanceof Element )) {

			node = node.getNextSibling();
		}

		return (Element) node;
	}

	@Override
	protected Element getNextSiblingElement( Element element ) {

		Node node = element.getNextSibling();

		while( node != null && !( node instanceof Element )) {

			node = node.getNextSibling();
		}

		return (Element) node;
	}

	@Override
	protected String getElementName( Element element ) {

		return element.getNodeName();
	}

	@Override
	@SuppressWarnings( { "cast", "unchecked" } )
	protected Map<String, String> getAttributesAsMap( Element element ) {

		NamedNodeMap nodes = element.getAttributes();

		int length = nodes.getLength();

		if ( length == 0 ) {
			return (Map<String, String>) Collections.EMPTY_MAP;
		}

		Map<String, String> attributes = new HashMap<String, String>( length );

		for ( int loop = 0; loop < length; loop++ ) {
			Node node = nodes.item( loop );
			attributes.put( node.getNodeName(), node.getNodeValue() );
		}

		return attributes;
	}
}
