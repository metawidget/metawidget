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

package org.metawidget.swt.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.swt.SwtMetawidgetTests;
import org.metawidget.util.CollectionUtils;

/**
 * @author Richard Kennard
 */

public class SwtWidgetBuilderTest
	extends TestCase {

	//
	// Public methods
	//

	public void testWidgetBuilder()
		throws Exception {

		SwtMetawidget metawidget = new SwtMetawidget( new Shell( SwtMetawidgetTests.TEST_DISPLAY, SWT.NONE ), SWT.NONE );
		SwtWidgetBuilder widgetBuilder = new SwtWidgetBuilder();
		Map<String, String> attributes = CollectionUtils.newHashMap();

		// Scale

		attributes.put( TYPE, int.class.getName() );
		attributes.put( MINIMUM_VALUE, "2" );
		attributes.put( MAXIMUM_VALUE, "99" );

		Scale slider = (Scale) widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( 2, slider.getMinimum() );
		assertEquals( 2, slider.getSelection() );
		assertEquals( 99, slider.getMaximum() );

		try {
			attributes.put( MINIMUM_VALUE, "1.5" );
			widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
			fail();
		} catch ( NumberFormatException e ) {
			assertEquals( "For input string: \"1.5\"", e.getMessage() );
		}

		// Text area

		attributes.put( TYPE, String.class.getName() );
		attributes.put( LARGE, TRUE );

		Text textarea = (Text) widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( ( textarea.getStyle() & SWT.MULTI ), SWT.MULTI );
		assertEquals( ( textarea.getStyle() & SWT.BORDER ), SWT.BORDER );
		assertEquals( ( textarea.getStyle() & SWT.V_SCROLL ), SWT.V_SCROLL );
		assertEquals( ( textarea.getStyle() & SWT.WRAP ), SWT.WRAP );

		// Spinner

		// bytes

		attributes.remove( MAXIMUM_VALUE );
		attributes.put( MINIMUM_VALUE, "2" );
		attributes.put( TYPE, byte.class.getName() );

		Spinner spinner = (Spinner) widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( ( (byte) 2 ), spinner.getMinimum() );
		assertEquals( ( (byte) 2 ), spinner.getSelection() );

		attributes.remove( MINIMUM_VALUE );
		attributes.put( MAXIMUM_VALUE, "99" );
		spinner = (Spinner) widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( ( (byte) 99 ), spinner.getMaximum() );

		// shorts

		attributes.remove( MAXIMUM_VALUE );
		attributes.put( MINIMUM_VALUE, "3" );
		attributes.put( TYPE, short.class.getName() );

		spinner = (Spinner) widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( ( (short) 3 ), spinner.getMinimum() );
		assertEquals( ( (short) 3 ), spinner.getSelection() );

		attributes.remove( MINIMUM_VALUE );
		attributes.put( MAXIMUM_VALUE, "98" );
		spinner = (Spinner) widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( ( (short) 98 ), spinner.getMaximum() );

		// ints

		attributes.remove( MAXIMUM_VALUE );
		attributes.put( MINIMUM_VALUE, "4" );
		attributes.put( TYPE, int.class.getName() );

		spinner = (Spinner) widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( 4, spinner.getMinimum() );
		assertEquals( 4, spinner.getSelection() );

		attributes.remove( MINIMUM_VALUE );
		attributes.put( MAXIMUM_VALUE, "97" );
		spinner = (Spinner) widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( 97, spinner.getMaximum() );

		// longs

		attributes.remove( MAXIMUM_VALUE );
		attributes.put( MINIMUM_VALUE, "5" );
		attributes.put( TYPE, long.class.getName() );

		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) instanceof Text );

		// floats

		attributes.remove( MAXIMUM_VALUE );
		attributes.put( MINIMUM_VALUE, "1.6" );
		attributes.put( TYPE, float.class.getName() );

		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) instanceof Text );

		// doubles

		attributes.put( TYPE, double.class.getName() );
		attributes.remove( MAXIMUM_VALUE );
		attributes.put( MINIMUM_VALUE, "1.6" );

		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) instanceof Text );
	}
}
