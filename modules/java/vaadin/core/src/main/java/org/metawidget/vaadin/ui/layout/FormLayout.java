// Metawidget (licensed under LGPL)
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

package org.metawidget.vaadin.ui.layout;

import java.util.Map;

import org.metawidget.layout.iface.AdvancedLayout;
import org.metawidget.vaadin.ui.Facet;
import org.metawidget.vaadin.ui.Stub;
import org.metawidget.vaadin.ui.VaadinMetawidget;

import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;

/**
 * Layout to arrange widgets using Vaadin's <code>FormLayout</code>.
 *
 * @author Loghman Barari
 */

public class FormLayout
	implements AdvancedLayout<Component, ComponentContainer, VaadinMetawidget> {

	//
	// Private members
	//

	private final String	mLabelSuffix;

	//
	// Constructor
	//

	public FormLayout() {

		this( new FormLayoutConfig() );
	}

	public FormLayout( FormLayoutConfig config ) {

		mLabelSuffix = config.getLabelSuffix();
	}

	//
	// Public methods
	//

	public void onStartBuild( VaadinMetawidget metawidget ) {

		// Do nothing
	}

	public void startContainerLayout( ComponentContainer container, VaadinMetawidget metawidget ) {

		com.vaadin.ui.FormLayout layout = new com.vaadin.ui.FormLayout();
		container.addComponent( layout );
	}

	public void layoutWidget( Component component, String elementName, Map<String, String> attributes, ComponentContainer container, VaadinMetawidget metawidget ) {

		// Do not render empty stubs

		if ( component instanceof Stub && !( (Stub) component ).getComponentIterator().hasNext() ) {
			return;
		}

		// Fix caption

		if ( component.getCaption() != null && component.getCaption().length() != 0 && mLabelSuffix != null && mLabelSuffix.length() != 0 ) {
			if ( !( component instanceof Button ) || component instanceof CheckBox ) {
				component.setCaption( component.getCaption() + mLabelSuffix );
			}
		}

		// Add it

		com.vaadin.ui.FormLayout layout = (com.vaadin.ui.FormLayout) container.getComponentIterator().next();
		component.setWidth( "100%" );
		layout.addComponent( component );
	}

	public void endContainerLayout( ComponentContainer container, VaadinMetawidget metawidget ) {

		// Do nothing
	}

	public void onEndBuild( VaadinMetawidget metawidget ) {

		// Buttons

		Facet buttonsFacet = metawidget.getFacet( "buttons" );

		if ( buttonsFacet != null ) {
			com.vaadin.ui.FormLayout layout = (com.vaadin.ui.FormLayout) metawidget.getComponentIterator().next();
			layout.addComponent( buttonsFacet );
		}
	}
}
