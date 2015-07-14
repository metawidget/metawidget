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

package org.metawidget.faces.widgetbuilder.primefaces;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.faces.FacesInspectionResultConstants.*;

import java.awt.Color;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;

import junit.framework.TestCase;

import org.metawidget.faces.FacesMetawidgetTests.MockFacesContext;
import org.metawidget.faces.component.UIStub;
import org.metawidget.faces.component.html.HtmlMetawidget;
import org.metawidget.faces.component.html.widgetbuilder.primefaces.PrimeFacesWidgetBuilder;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.util.CollectionUtils;
import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.colorpicker.ColorPicker;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.component.password.Password;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.component.slider.Slider;
import org.primefaces.component.spinner.Spinner;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class PrimeFacesWidgetBuilderTest
	extends TestCase {

	//
	// Private members
	//

	private FacesContext	mContext;

	//
	// Public methods
	//

	public void testPrimeFacesWidgetBuilder()
		throws Exception {

		PrimeFacesWidgetBuilder widgetBuilder = new PrimeFacesWidgetBuilder();

		// Read-only pass throughs

		Map<String, String> attributes = CollectionUtils.newHashMap();
		assertEquals( null, widgetBuilder.buildWidget( PROPERTY, attributes, null ) );
		attributes.put( READ_ONLY, TRUE );
		assertEquals( null, widgetBuilder.buildWidget( PROPERTY, attributes, null ) );
		attributes.put( LOOKUP, TRUE );
		assertEquals( null, widgetBuilder.buildWidget( PROPERTY, attributes, null ) );
		attributes.remove( LOOKUP );
		attributes.put( FACES_LOOKUP, "#{true}" );
		assertEquals( null, widgetBuilder.buildWidget( PROPERTY, attributes, null ) );
		attributes.remove( FACES_LOOKUP );
		attributes.put( HIDDEN, TRUE );
		assertEquals( null, widgetBuilder.buildWidget( PROPERTY, attributes, null ) );
		attributes.remove( HIDDEN );
		attributes.put( TYPE, "foo" );
		assertEquals( null, widgetBuilder.buildWidget( PROPERTY, attributes, null ) );

		// Active pass throughs

		attributes.remove( READ_ONLY );
		attributes.put( HIDDEN, TRUE );
		assertEquals( null, widgetBuilder.buildWidget( PROPERTY, attributes, null ) );
		attributes.remove( HIDDEN );
		attributes.put( TYPE, "foo" );
		assertEquals( null, widgetBuilder.buildWidget( PROPERTY, attributes, null ) );
		attributes.remove( HIDDEN );

		// Action

		attributes.put( NAME, "Press Me" );
		HtmlMetawidget metawidget = new HtmlMetawidget();
		CommandButton button = (CommandButton) widgetBuilder.buildWidget( ACTION, attributes, metawidget );
		assertEquals( "Press Me", button.getValue() );
		assertTrue( !button.isAjax() );
		attributes.remove( ACTION );

		// Faces lookup

		attributes.put( FACES_LOOKUP, "#{foo.bar}" );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, null ) instanceof SelectOneMenu );
		attributes.remove( FACES_LOOKUP );

		// Lookup

		attributes.put( TYPE, String.class.getName() );
		attributes.put( LOOKUP, "Foo, Bar, Baz" );
		metawidget.setInspector( new PropertyTypeInspector() );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, metawidget ) instanceof SelectOneMenu );
		attributes.remove( LOOKUP );

		// Sliders

		attributes.put( TYPE, int.class.getName() );
		attributes.put( MINIMUM_VALUE, "1" );
		attributes.put( MAXIMUM_VALUE, "1024" );
		UIStub stub = (UIStub) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( stub.getChildren().get( 0 ) instanceof HtmlInputText );
		Slider slider = (Slider) stub.getChildren().get( 1 );
		assertEquals( 1, slider.getMinValue() );
		assertEquals( 1024, slider.getMaxValue() );

		attributes.put( TYPE, Long.class.getName() );
		attributes.put( MINIMUM_VALUE, "2" );
		attributes.put( MAXIMUM_VALUE, "1023" );
		stub = (UIStub) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( stub.getChildren().get( 0 ) instanceof InputText );
		slider = (Slider) stub.getChildren().get( 1 );
		assertEquals( 2, slider.getMinValue() );
		assertEquals( 1023, slider.getMaxValue() );

		// Spinners

		attributes.put( TYPE, int.class.getName() );
		attributes.put( MAXIMUM_VALUE, "" );
		Spinner spinner = (Spinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( 2d, spinner.getMin() );
		attributes.put( MAXIMUM_VALUE, "1024" );

		attributes.put( MINIMUM_VALUE, "" );
		spinner = (Spinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( 1024d, spinner.getMax() );

		// (lower bound)

		attributes.put( TYPE, byte.class.getName() );
		spinner = (Spinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( (double) Byte.MIN_VALUE, spinner.getMin() );

		attributes.put( TYPE, short.class.getName() );
		spinner = (Spinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( (double) Short.MIN_VALUE, spinner.getMin() );

		attributes.put( TYPE, int.class.getName() );
		spinner = (Spinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( (double) Integer.MIN_VALUE, spinner.getMin() );

		attributes.put( TYPE, long.class.getName() );
		spinner = (Spinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( (double) Long.MIN_VALUE, spinner.getMin() );

		attributes.put( TYPE, float.class.getName() );
		spinner = (Spinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( (double) -Float.MAX_VALUE, spinner.getMin() );

		attributes.put( TYPE, double.class.getName() );
		spinner = (Spinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( -Double.MAX_VALUE, spinner.getMin() );

		// (upper bound)

		attributes.put( MAXIMUM_VALUE, "" );

		attributes.put( TYPE, byte.class.getName() );
		spinner = (Spinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( (double) Byte.MAX_VALUE, spinner.getMax() );

		attributes.put( TYPE, short.class.getName() );
		spinner = (Spinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( (double) Short.MAX_VALUE, spinner.getMax() );

		attributes.put( TYPE, int.class.getName() );
		spinner = (Spinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( (double) Integer.MAX_VALUE, spinner.getMax() );

		attributes.put( TYPE, long.class.getName() );
		spinner = (Spinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( (double) Long.MAX_VALUE, spinner.getMax() );

		attributes.put( TYPE, float.class.getName() );
		spinner = (Spinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( (double) Float.MAX_VALUE, spinner.getMax() );

		attributes.put( TYPE, double.class.getName() );
		spinner = (Spinner) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( Double.MAX_VALUE, spinner.getMax() );

		// Calendars

		attributes.put( TYPE, Date.class.getName() );
		attributes.put( DATETIME_PATTERN, "dd-MM-yyyy" );
		attributes.put( LOCALE, "en-AU" );
		attributes.put( TIME_ZONE, "Australia/Sydney" );
		Calendar calendar = (Calendar) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( "dd-MM-yyyy", calendar.getPattern() );
		assertEquals( new Locale( "en-AU" ), calendar.getLocale() );
		assertEquals( TimeZone.getTimeZone( "Australia/Sydney" ), calendar.getTimeZone() );

		// Suggest

		attributes.put( TYPE, String.class.getName() );
		attributes.put( FACES_SUGGEST, "#{foo.bar}" );

		AutoComplete autocomplete = (AutoComplete) widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( "#{foo.bar}", autocomplete.getCompleteMethod().getExpressionString() );
		assertEquals( Object.class, autocomplete.getCompleteMethod().getMethodInfo( null ).getReturnType() );
		assertEquals( String.class, autocomplete.getCompleteMethod().getMethodInfo( null ).getParamTypes()[0] );
		assertEquals( 1, autocomplete.getCompleteMethod().getMethodInfo( null ).getParamTypes().length );

		attributes.remove( FACES_SUGGEST );

        // Masked

        attributes.put( TYPE, String.class.getName() );
        attributes.put( MASKED, TRUE );
        assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, null ) instanceof Password );

        attributes.remove( MASKED );

        // Large

        attributes.put( TYPE, String.class.getName() );
        attributes.put( LARGE, TRUE );
        attributes.put( MAXIMUM_LENGTH, "20" );
        InputTextarea inputTextarea = (InputTextarea) widgetBuilder.buildWidget( PROPERTY, attributes, null );
        assertEquals( 20, inputTextarea.getMaxlength() );

        attributes.remove( LARGE );
        attributes.remove( MAXIMUM_LENGTH );

        // String

        attributes.put( TYPE, String.class.getName() );
        assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, null ) instanceof InputText );

		// ColorPickers. Note org.primefaces.component.ColorPickerRenderer does *not* support
		// java.awt.Color (http://forum.primefaces.org/viewtopic.php?t=21593) so it isn't much good
		// to us here

		attributes.put( TYPE, Color.class.getName() );
		assertEquals( null, widgetBuilder.buildWidget( PROPERTY, attributes, null ) );
	}

	//
	// Protected methods
	//

	@Override
	protected void setUp()
		throws Exception {

		super.setUp();

		mContext = new MockPrimeFacesFacesContext();
	}

	@Override
	protected void tearDown()
		throws Exception {

		super.tearDown();

		mContext.release();
	}

	//
	// Inner class
	//

	protected static class MockPrimeFacesFacesContext
		extends MockFacesContext {

		//
		// Protected methods
		//

		@Override
		public UIComponent createComponent( String componentName )
			throws FacesException {

			if ( Slider.COMPONENT_TYPE.equals( componentName ) ) {
				return new Slider();
			}

			if ( Spinner.COMPONENT_TYPE.equals( componentName ) ) {
				return new Spinner();
			}

			if ( Calendar.COMPONENT_TYPE.equals( componentName ) ) {
				return new Calendar();
			}

			if ( ColorPicker.COMPONENT_TYPE.equals( componentName ) ) {
				return new ColorPicker();
			}

			if ( SelectOneMenu.COMPONENT_TYPE.equals( componentName ) ) {
				return new SelectOneMenu();
			}

			if ( CommandButton.COMPONENT_TYPE.equals( componentName ) ) {
				return new CommandButton();
			}

			if ( AutoComplete.COMPONENT_TYPE.equals( componentName ) ) {
				return new AutoComplete();
			}

			if ( Password.COMPONENT_TYPE.equals( componentName ) ) {
                return new Password();
            }

            if ( InputTextarea.COMPONENT_TYPE.equals( componentName ) ) {
                return new InputTextarea();
            }

            if ( InputText.COMPONENT_TYPE.equals( componentName ) ) {
                return new InputText();
            }

			return super.createComponent( componentName );
		}
	}
}
