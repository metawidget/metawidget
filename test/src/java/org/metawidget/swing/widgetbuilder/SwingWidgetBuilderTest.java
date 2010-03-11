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

package org.metawidget.swing.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;

import junit.framework.TestCase;

import org.metawidget.util.CollectionUtils;

/**
 * @author Richard Kennard
 */

public class SwingWidgetBuilderTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testWidgetBuilder()
		throws Exception
	{
		SwingWidgetBuilder widgetBuilder = new SwingWidgetBuilder();
		Map<String, String> attributes = CollectionUtils.newHashMap();

		// JSlider

		attributes.put( TYPE, int.class.getName() );
		attributes.put( MINIMUM_VALUE, "2" );
		attributes.put( MAXIMUM_VALUE, "99" );

		JSlider slider = (JSlider) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( 2 == slider.getMinimum() );
		assertTrue( 2 == slider.getValue() );
		assertTrue( 99 == slider.getMaximum() );

		try
		{
			attributes.put( MINIMUM_VALUE, "1.5" );
			slider = (JSlider) widgetBuilder.buildWidget( PROPERTY, attributes, null );
			assertTrue( false );
		}
		catch( NumberFormatException e )
		{
			assertEquals( "For input string: \"1.5\"", e.getMessage() );
		}

		// JTextArea

		attributes.put( TYPE, String.class.getName() );
		attributes.put( LARGE, TRUE );

		JScrollPane scrollPane = (JScrollPane) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( null != scrollPane.getBorder() );
		JTextArea textarea = (JTextArea) scrollPane.getViewport().getView();
		assertTrue( true == textarea.getLineWrap() );
		assertTrue( true == textarea.getWrapStyleWord() );
		assertTrue( true == textarea.isEditable() );
		assertTrue( 2 == textarea.getRows() );

		// JSpinner

		// bytes

		attributes.remove( MAXIMUM_VALUE );
		attributes.put( MINIMUM_VALUE, "2" );
		attributes.put( TYPE, byte.class.getName() );

		JSpinner spinner = (JSpinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( ((byte) 2) == (Byte) ((SpinnerNumberModel) spinner.getModel()).getMinimum() );
		assertTrue( ((byte) 2) == (Byte) spinner.getValue() );

		attributes.remove( MINIMUM_VALUE );
		attributes.put( MAXIMUM_VALUE, "99" );
		spinner = (JSpinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( ((byte) 99) == (Byte) ((SpinnerNumberModel) spinner.getModel()).getMaximum() );

		// shorts

		attributes.remove( MAXIMUM_VALUE );
		attributes.put( MINIMUM_VALUE, "3" );
		attributes.put( TYPE, short.class.getName() );

		spinner = (JSpinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( ((short) 3) == (Short) ((SpinnerNumberModel) spinner.getModel()).getMinimum() );
		assertTrue( ((short) 3) == (Short) spinner.getValue() );

		attributes.remove( MINIMUM_VALUE );
		attributes.put( MAXIMUM_VALUE, "98" );
		spinner = (JSpinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( ((short) 98) == (Short) ((SpinnerNumberModel) spinner.getModel()).getMaximum() );

		// ints

		attributes.remove( MAXIMUM_VALUE );
		attributes.put( MINIMUM_VALUE, "4" );
		attributes.put( TYPE, int.class.getName() );

		spinner = (JSpinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( 4 == (Integer) ((SpinnerNumberModel) spinner.getModel()).getMinimum() );
		assertTrue( 4 == (Integer) spinner.getValue() );

		attributes.remove( MINIMUM_VALUE );
		attributes.put( MAXIMUM_VALUE, "97" );
		spinner = (JSpinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( 97 == (Integer) ((SpinnerNumberModel) spinner.getModel()).getMaximum() );

		// longs

		attributes.remove( MAXIMUM_VALUE );
		attributes.put( MINIMUM_VALUE, "5" );
		attributes.put( TYPE, long.class.getName() );

		spinner = (JSpinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( ((long) 5) == (Long) ((SpinnerNumberModel) spinner.getModel()).getMinimum() );
		assertTrue( ((long) 5) == (Long) spinner.getValue() );

		attributes.remove( MINIMUM_VALUE );
		attributes.put( MAXIMUM_VALUE, "96" );
		spinner = (JSpinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( ((long) 96) == (Long) ((SpinnerNumberModel) spinner.getModel()).getMaximum() );

		// floats

		attributes.remove( MAXIMUM_VALUE );
		attributes.put( MINIMUM_VALUE, "1.6" );
		attributes.put( TYPE, float.class.getName() );

		spinner = (JSpinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( 1.6f == (Float) spinner.getValue() );
		assertTrue( 0.1f == (Float) ((SpinnerNumberModel) spinner.getModel()).getStepSize() );

		attributes.put( MAXIMUM_FRACTIONAL_DIGITS, "3" );
		spinner = (JSpinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( 0.001f == (Float) ((SpinnerNumberModel) spinner.getModel()).getStepSize() );

		attributes.put( MINIMUM_VALUE, "-1.6" );
		spinner = (JSpinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( 0f == (Float) spinner.getValue() );

		attributes.remove( MINIMUM_VALUE );
		attributes.put( MAXIMUM_VALUE, "-1" );
		spinner = (JSpinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( -1f == (Float) spinner.getValue() );

		// doubles

		attributes.put( TYPE, double.class.getName() );
		attributes.remove( MAXIMUM_VALUE );
		attributes.put( MINIMUM_VALUE, "1.6" );
		spinner = (JSpinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( 1.6d == (Double) spinner.getValue() );
		assertTrue( 1000 == Math.round( ((Double) ((SpinnerNumberModel) spinner.getModel()).getStepSize()) * 1000000 ));

		attributes.put( MINIMUM_VALUE, "-1.6" );
		spinner = (JSpinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( 0d == (Double) spinner.getValue() );

		attributes.remove( MINIMUM_VALUE );
		attributes.put( MAXIMUM_VALUE, "-1" );
		spinner = (JSpinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( -1d == (Double) spinner.getValue() );
	}
}
