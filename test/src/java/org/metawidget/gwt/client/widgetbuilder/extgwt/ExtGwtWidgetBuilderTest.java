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

package org.metawidget.gwt.client.widgetbuilder.extgwt;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.extjs.gxt.ui.client.widget.Slider;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author Richard Kennard
 */

public class ExtGwtWidgetBuilderTest
	extends GWTTestCase
{
	//
	// Public methods
	//

	@Override
	public String getModuleName()
	{
		return "org.metawidget.gwt.GwtMetawidgetTest";
	}

	public void testWidgetBuilder()
		throws Exception
	{
		ExtGwtWidgetBuilder widgetBuilder = new ExtGwtWidgetBuilder();

		Map<String, String> attributes = new HashMap<String, String>();
		assertTrue( null == widgetBuilder.buildWidget( PROPERTY, attributes, null ) );

		// DateField

		attributes.put( TYPE, "java.util.Date" );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, null ) instanceof DateField );

		// Slider

		attributes.put( TYPE, "long" );
		attributes.put( MINIMUM_VALUE, "1.6" );
		attributes.put( MAXIMUM_VALUE, "99.5" );
		Slider slider = (Slider) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( 2 == slider.getMinValue() );
		assertTrue( 2 == slider.getValue() );
		assertTrue( 1 == slider.getIncrement() );
		assertTrue( 99 == slider.getMaxValue() );

		attributes.remove( MAXIMUM_VALUE );
		assertTrue( null == widgetBuilder.buildWidget( PROPERTY, attributes, null ) );
	}

	public void testValueAccessor()
		throws Exception
	{
		final ExtGwtWidgetBuilder widgetBuilder = new ExtGwtWidgetBuilder();

		assertTrue( false == widgetBuilder.setValue( new TextBox(), null ) );

		// DateField

		DateField dateField = new DateField();
		Date date = new Date();
		assertTrue( true == widgetBuilder.setValue( dateField, date ) );
		assertTrue( date == dateField.getValue() );
		assertTrue( date.equals( widgetBuilder.getValue( dateField ) ));

		// Slider

		final Slider slider = new Slider();
		slider.setMinValue( 1 );
		slider.setValue( 1 );
		slider.setIncrement( 1 );
		slider.setMaxValue( 99 );
		assertTrue( true == widgetBuilder.setValue( slider, 2 ) );
		assertTrue( 2 == slider.getValue() );
		assertTrue( 2 == (Integer) widgetBuilder.getValue( slider ) );
	}
}
