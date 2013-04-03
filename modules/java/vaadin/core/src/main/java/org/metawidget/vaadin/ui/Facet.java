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

package org.metawidget.vaadin.ui;

import java.util.Iterator;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * Facet for Vaadin environments.
 *
 * @author Loghman Barari
 */

public class Facet
    extends Panel implements ComponentContainer {

    //
    // Constructor
    //

    private VerticalLayout layout = null;

    public Facet() {

        addStyleName( "light" );
        //((com.vaadin.ui.Layout) getContent()).setMargin( false );

        /*layout = new VerticalLayout();
        layout.setMargin(true);

        setContent(layout);*/
    }

    @Override
    public Iterator<Component> iterator() {
        if (layout != null) {
            layout.iterator();
        }
        return super.iterator();
    }

    @Override
	public void addComponent(Component c) {
        if (layout == null) {
            layout = new VerticalLayout();
            layout.setMargin(true);

            setContent(layout);
        }
        layout.addComponent(c);
    }

    @Override
	public void addComponents(Component... components) {
        // TODO Auto-generated method stub

    }

    @Override
	public void removeComponent(Component c) {
        layout.removeComponent(c);
    }

    @Override
	public void removeAllComponents() {
        // TODO Auto-generated method stub

    }

    @Override
	public void replaceComponent(Component oldComponent, Component newComponent) {
        // TODO Auto-generated method stub

    }

    @Override
	@Deprecated
    public Iterator<Component> getComponentIterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
	public void moveComponentsFrom(ComponentContainer source) {
        // TODO Auto-generated method stub

    }

    @Override
	@Deprecated
    public void addListener(ComponentAttachListener listener) {
        // TODO Auto-generated method stub

    }

    @Override
	@Deprecated
    public void removeListener(ComponentAttachListener listener) {
        // TODO Auto-generated method stub

    }

    @Override
	@Deprecated
    public void addListener(ComponentDetachListener listener) {
        // TODO Auto-generated method stub

    }

    @Override
	@Deprecated
    public void removeListener(ComponentDetachListener listener) {
        // TODO Auto-generated method stub

    }
}
