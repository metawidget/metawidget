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

package org.metawidget.test.faces.widgetbuilder.richfaces;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.awt.Color;
import java.util.Date;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;

import junit.framework.TestCase;

import org.metawidget.faces.component.html.HtmlMetawidget;
import org.metawidget.faces.component.html.widgetbuilder.richfaces.RichFacesWidgetBuilder;
import org.metawidget.test.faces.FacesMetawidgetTests.MockComponent;
import org.metawidget.test.faces.FacesMetawidgetTests.MockFacesContext;
import org.metawidget.util.CollectionUtils;
import org.richfaces.component.UICalendar;
import org.richfaces.component.UIInputNumberSlider;
import org.richfaces.component.UIInputNumberSpinner;
import org.richfaces.component.html.HtmlCalendar;
import org.richfaces.component.html.HtmlInputNumberSlider;
import org.richfaces.component.html.HtmlInputNumberSpinner;

/**
 * @author Richard Kennard
 */

public class RichFacesWidgetBuilderTest
	extends TestCase
{
	//
	// Private members
	//

	private FacesContext mContext;

	//
	// Public methods
	//

	public void testRichFacesWidgetBuilder()
		throws Exception
	{
		RichFacesWidgetBuilder widgetBuilder = new RichFacesWidgetBuilder();

		// Sliders

		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( TYPE, int.class.getName() );
		attributes.put( MINIMUM_VALUE, "1" );
		attributes.put( MAXIMUM_VALUE, "1024" );
		UIInputNumberSlider slider = (UIInputNumberSlider) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( "1".equals( slider.getMinValue() ));
		assertTrue( "1024".equals( slider.getMaxValue() ));

		// Spinners

		attributes.put( MINIMUM_VALUE, "" );
		UIInputNumberSpinner spinner = (UIInputNumberSpinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( "1024".equals( spinner.getMaxValue() ));

		// Calendars

		attributes.put( TYPE, Date.class.getName() );
		attributes.put( DATETIME_PATTERN, "dd-MM-yyyy" );
		UICalendar calendar = (UICalendar) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( "dd-MM-yyyy".equals( calendar.getDatePattern() ) );

		// ColorPickers (as of RichFaces 3.3.1)

		attributes.put( TYPE, Color.class.getName() );
		MockComponent mockComponent = (MockComponent) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( "org.richfaces.ColorPicker".equals( mockComponent.getFamily() ) );
		attributes.put( READ_ONLY, "true" );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, new HtmlMetawidget() ) instanceof HtmlOutputText );
	}

	//
	// Protected methods
	//

	@Override
	protected void setUp()
		throws Exception
	{
		super.setUp();

		mContext = new MockRichFacesFacesContext();
	}

	@Override
	protected void tearDown()
		throws Exception
	{
		super.tearDown();

		mContext.release();
	}


	//
	// Inner class
	//

	protected static class MockRichFacesFacesContext
		extends MockFacesContext
	{
		//
		// Protected methods
		//

		@Override
		public UIComponent createComponent( String componentName )
			throws FacesException
		{
			if ( "org.richfaces.inputNumberSlider".equals( componentName ))
				return new HtmlInputNumberSlider();

			if ( "org.richfaces.inputNumberSpinner".equals( componentName ))
				return new HtmlInputNumberSpinner();

			if ( "org.richfaces.Calendar".equals( componentName ) )
				return new HtmlCalendar();

			if ( "org.richfaces.ColorPicker".equals( componentName ) )
				return new MockComponent( "org.richfaces.ColorPicker" );

			return super.createComponent( componentName );
		}
	}
}
