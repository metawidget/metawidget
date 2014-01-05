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

package org.metawidget.vaadin.ui.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.util.CollectionUtils;

import com.vaadin.ui.Slider;
import com.vaadin.ui.TextArea;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
			assertTrue( e instanceof Slider.ValueOutOfBoundsException || e.getCause() instanceof Slider.ValueOutOfBoundsException );
		}

		// TextArea

		attributes.put( TYPE, String.class.getName() );
		attributes.put( LARGE, TRUE );

		TextArea textarea = (TextArea) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( !textarea.isReadOnly() );

	}
}
