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

package org.metawidget.vaadin.ui.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.util.CollectionUtils;
import org.metawidget.vaadin.ui.widgetbuilder.VaadinWidgetBuilder;

import com.vaadin.ui.Slider;
import com.vaadin.ui.TextArea;

/**
 * @author Richard Kennard
 */

public class VaadinWidgetBuilderTest
	extends TestCase {

	//
	// Public methods
	//

	public void testWidgetBuilder()
		throws Exception {

		VaadinWidgetBuilder widgetBuilder = new VaadinWidgetBuilder();
		Map<String, String> attributes = CollectionUtils.newHashMap();

		// Slider

		attributes.put( TYPE, int.class.getName() );
		attributes.put( MINIMUM_VALUE, "2" );
		attributes.put( MAXIMUM_VALUE, "99" );

		Slider slider = (Slider) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( 2d, slider.getMin() );
		assertEquals( 2d, slider.getValue() );
		assertEquals( 99d, slider.getMax() );

		try {
			attributes.put( MINIMUM_VALUE, "1.5" );
			widgetBuilder.buildWidget( PROPERTY, attributes, null );
			fail();
		} catch ( Exception e ) {
			assertTrue( e.getCause() instanceof Slider.ValueOutOfBoundsException );
		}

		// TextArea

		attributes.put( TYPE, String.class.getName() );
		attributes.put( LARGE, TRUE );

		TextArea textarea = (TextArea) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( !textarea.isReadOnly() );

	}
}
