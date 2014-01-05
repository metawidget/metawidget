// Metawidget
//
// For historical reasons, this file is licensed under the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html).
//
// Most other files in Metawidget are licensed under both the
// LGPL/EPL and a commercial license. See http://metawidget.org
// for details.

package org.metawidget.vaadin.ui.layout;

import java.util.Map;

import org.metawidget.layout.iface.AdvancedLayout;
import org.metawidget.vaadin.ui.Stub;
import org.metawidget.vaadin.ui.VaadinMetawidget;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;

/**
 * Layout to arrange widgets using Vaadin's <code>VerticalLayout</code>.
 *
 * @author Loghman Barari
 */

public class VerticalLayout
	implements AdvancedLayout<Component, ComponentContainer, VaadinMetawidget> {

	//
	// Public methods
	//

	public void onStartBuild( VaadinMetawidget metawidget ) {

		// Do nothing
	}

	public void startContainerLayout( ComponentContainer container, VaadinMetawidget metawidget ) {

		com.vaadin.ui.VerticalLayout layout = new com.vaadin.ui.VerticalLayout();
		layout.setSpacing( true );
		container.addComponent( layout );
	}

	public void layoutWidget( Component component, String elementName, Map<String, String> attributes, ComponentContainer container, VaadinMetawidget metawidget ) {

		// Do not render empty stubs

		if ( component instanceof Stub && !( (Stub) component ).getComponentIterator().hasNext() ) {
			return;
		}

		// Add it

		com.vaadin.ui.VerticalLayout layout = (com.vaadin.ui.VerticalLayout) container.getComponentIterator().next();
		component.setWidth( "100%" );
		layout.addComponent( component );
	}

	public void endContainerLayout( ComponentContainer container, VaadinMetawidget metawidget ) {

		// Do nothing
	}

	public void onEndBuild( VaadinMetawidget metawidget ) {

		// Do nothing
	}
}
