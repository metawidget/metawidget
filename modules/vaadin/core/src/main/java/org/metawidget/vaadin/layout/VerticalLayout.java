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

package org.metawidget.vaadin.layout;

import java.util.Map;

import org.metawidget.layout.iface.AdvancedLayout;
import org.metawidget.vaadin.Stub;
import org.metawidget.vaadin.VaadinMetawidget;

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
		( (VaadinMetawidget) container ).setLayoutRoot( layout );
	}

	public void layoutWidget( Component component, String elementName, Map<String, String> attributes, ComponentContainer container, VaadinMetawidget metawidget ) {

		// Do not render empty stubs

		if ( component instanceof Stub && !( (Stub) component ).getComponentIterator().hasNext() ) {
			return;
		}

		// Add it

		com.vaadin.ui.VerticalLayout layout = (com.vaadin.ui.VerticalLayout) ( (VaadinMetawidget) container ).getLayoutRoot();
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
