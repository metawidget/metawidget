// Metawidget Examples (licensed under BSD License)
//
// Copyright (c) Richard Kennard
// All rights reserved
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
//
// * Redistributions of source code must retain the above copyright notice,
//   this list of conditions and the following disclaimer.
// * Redistributions in binary form must reproduce the above copyright notice,
//   this list of conditions and the following disclaimer in the documentation
//   and/or other materials provided with the distribution.
// * Neither the name of Richard Kennard nor the names of its contributors may
//   be used to endorse or promote products derived from this software without
//   specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
// OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
// OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
// OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
// OF THE POSSIBILITY OF SUCH DAMAGE.

package org.metawidget.integrationtest.swing.xsd;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import javax.swing.JComponent;

import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.ClassUtils;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.XmlUtils;
import org.metawidget.widgetprocessor.iface.AdvancedWidgetProcessor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DocumentBindingProcessor
	implements AdvancedWidgetProcessor<JComponent, SwingMetawidget> {

	//
	// Public methods
	//

	public void onStartBuild( SwingMetawidget metawidget ) {

		getWrittenComponents( metawidget ).clear();
	}

	/**
	 * Retrieve the values from the Document and put them in the Components.
	 */

	public JComponent processWidget( JComponent component, String elementName, Map<String, String> attributes, SwingMetawidget metawidget ) {

		String attributeName = attributes.get( NAME );
		getWrittenComponents( metawidget ).put( attributeName, component );

		// Fetch the value...

		Document toInspect = metawidget.getToInspect();
		Element root = toInspect.getDocumentElement();
		Element valueNode = XmlUtils.getChildNamed( root, attributeName );

		if ( valueNode == null ) {
			return component;
		}

		// ...and apply it to the component. For simplicity, we won't worry about converters

		String componentProperty = metawidget.getValueProperty( component );
		ClassUtils.setProperty( component, componentProperty, valueNode.getTextContent() );

		return component;
	}

	public void onEndBuild( SwingMetawidget metawidget ) {

		// Do nothing
	}

	/**
	 * Store the values from the Components back into the Document.
	 */

	public void save( SwingMetawidget metawidget ) {

		Document toInspect = metawidget.getToInspect();
		Element root = toInspect.getDocumentElement();

		for ( Map.Entry<String, JComponent> entry : getWrittenComponents( metawidget ).entrySet() ) {

			JComponent component = entry.getValue();
			String componentProperty = metawidget.getValueProperty( component );
			Object value = ClassUtils.getProperty( component, componentProperty );

			Element valueNode = XmlUtils.getChildNamed( root, entry.getKey() );

			if ( valueNode == null ) {
				valueNode = toInspect.createElement( entry.getKey() );
				root.appendChild( valueNode );
			}

			valueNode.setTextContent( String.valueOf( value ));
		}
	}

	//
	// Private methods
	//

	/**
	 * During load-time we keep track of all the components. At save-time we write them all back
	 * again.
	 */

	private Map<String, JComponent> getWrittenComponents( SwingMetawidget metawidget ) {

		@SuppressWarnings( "unchecked" )
		Map<String, JComponent> writtenComponents = (Map<String, JComponent>) metawidget.getClientProperty( DocumentBindingProcessor.class );

		if ( writtenComponents == null ) {
			writtenComponents = CollectionUtils.newHashMap();
			metawidget.putClientProperty( DocumentBindingProcessor.class, writtenComponents );
		}

		return writtenComponents;
	}
}