// Metawidget
//
// This library is dual licensed under both LGPL and a commercial
// license.
//
// LGPL: this library is free software; you can redistribute it and/or
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
//
// Commercial License: See http://metawidget.org for details

package org.metawidget.vaadin.ui.layout;

import static org.metawidget.inspector.InspectionResultConstants.*;
import junit.framework.TestCase;

import org.metawidget.vaadin.ui.Stub;
import org.metawidget.vaadin.ui.VaadinMetawidget;
import org.metawidget.vaadin.ui.layout.VerticalLayout;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Slider;
import com.vaadin.ui.TextField;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class VerticalLayoutTest
	extends TestCase {

	//
	// Public methods
	//

	public void testLayout()
		throws Exception {

		VaadinMetawidget metawidget = new VaadinMetawidget();
		ComponentContainer container = new Panel();

		// startLayout

		VerticalLayout VerticalLayout = new VerticalLayout();
		VerticalLayout.startContainerLayout( container, metawidget );

		assertTrue( container.getComponentIterator().next() instanceof com.vaadin.ui.VerticalLayout );
		assertTrue( ((com.vaadin.ui.VerticalLayout) container.getComponentIterator().next()).isSpacing() );
		assertFalse( ( metawidget.getComponentIterator().next() instanceof com.vaadin.ui.VerticalLayout ) );

		// layoutWidget

		assertEquals( 0, ( (com.vaadin.ui.VerticalLayout) container.getComponentIterator().next() ).getComponentCount() );

		Stub stub = new Stub();
		VerticalLayout.layoutWidget( stub, PROPERTY, null, container, metawidget );
		assertEquals( 0, ( (com.vaadin.ui.VerticalLayout) container.getComponentIterator().next() ).getComponentCount() );

		stub.addComponent( new Slider() );
		VerticalLayout.layoutWidget( stub, PROPERTY, null, container, metawidget );
		assertEquals( stub, ( (com.vaadin.ui.VerticalLayout) container.getComponentIterator().next() ).getComponent( 0 ) );
		assertEquals( 1, ( (com.vaadin.ui.VerticalLayout) container.getComponentIterator().next() ).getComponentCount() );

		VerticalLayout.layoutWidget( new TextField(), PROPERTY, null, container, metawidget );
		assertTrue( ( (com.vaadin.ui.VerticalLayout) container.getComponentIterator().next() ).getComponent( 1 ) instanceof TextField );
		assertEquals( 2, ( (com.vaadin.ui.VerticalLayout) container.getComponentIterator().next() ).getComponentCount() );
	}
}
