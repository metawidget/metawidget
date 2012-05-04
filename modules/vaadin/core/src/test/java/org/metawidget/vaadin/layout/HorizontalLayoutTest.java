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

import static org.metawidget.inspector.InspectionResultConstants.*;
import junit.framework.TestCase;

import org.metawidget.vaadin.Stub;
import org.metawidget.vaadin.VaadinMetawidget;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Slider;
import com.vaadin.ui.TextField;

/**
 * @author Richard Kennard
 */

public class HorizontalLayoutTest
	extends TestCase {

	//
	// Public methods
	//

	public void testLayout()
		throws Exception {

		VaadinMetawidget metawidget = new VaadinMetawidget();
		ComponentContainer container = new Panel();

		// startLayout

		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.startContainerLayout( container, metawidget );

		assertTrue( container.getComponentIterator().next() instanceof com.vaadin.ui.HorizontalLayout );
		assertTrue( ((com.vaadin.ui.HorizontalLayout) container.getComponentIterator().next()).isSpacing() );
		assertFalse( ( metawidget.getComponentIterator().next() instanceof com.vaadin.ui.HorizontalLayout ) );

		// layoutWidget

		assertEquals( 0, ( (com.vaadin.ui.HorizontalLayout) container.getComponentIterator().next() ).getComponentCount() );

		Stub stub = new Stub();
		horizontalLayout.layoutWidget( stub, PROPERTY, null, container, metawidget );
		assertEquals( 0, ( (com.vaadin.ui.HorizontalLayout) container.getComponentIterator().next() ).getComponentCount() );

		stub.addComponent( new Slider() );
		horizontalLayout.layoutWidget( stub, PROPERTY, null, container, metawidget );
		assertEquals( stub, ( (com.vaadin.ui.HorizontalLayout) container.getComponentIterator().next() ).getComponent( 0 ) );
		assertEquals( 1, ( (com.vaadin.ui.HorizontalLayout) container.getComponentIterator().next() ).getComponentCount() );

		horizontalLayout.layoutWidget( new TextField(), PROPERTY, null, container, metawidget );
		assertTrue( ( (com.vaadin.ui.HorizontalLayout) container.getComponentIterator().next() ).getComponent( 1 ) instanceof TextField );
		assertEquals( 2, ( (com.vaadin.ui.HorizontalLayout) container.getComponentIterator().next() ).getComponentCount() );
	}
}
