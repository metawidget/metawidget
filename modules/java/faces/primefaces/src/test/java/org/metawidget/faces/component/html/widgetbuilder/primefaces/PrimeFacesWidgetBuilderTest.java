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

package org.metawidget.faces.component.html.widgetbuilder.primefaces;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.faces.FacesInspectionResultConstants.*;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.util.*;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.component.html.*;

import org.metawidget.faces.FacesMetawidgetTests.MockFacesContext;
import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.UIStub;
import org.metawidget.faces.component.html.HtmlMetawidget;
import org.metawidget.faces.widgetbuilder.HtmlWidgetBuilderTest;
import org.metawidget.inspector.impl.BaseObjectInspectorConfig;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyle;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyleConfig;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.inspector.xml.XmlInspector;
import org.metawidget.inspector.xml.XmlInspectorConfig;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.MetawidgetTestUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;
import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.colorpicker.ColorPicker;
import org.primefaces.component.column.Column;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.component.password.Password;
import org.primefaces.component.selectmanycheckbox.SelectManyCheckbox;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.component.slider.Slider;
import org.primefaces.component.spinner.Spinner;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class PrimeFacesWidgetBuilderTest
	extends HtmlWidgetBuilderTest {

	//
	// Public methods
	//

	@Override
	public void testReadOnly() {

		// No tests
	}

	@Override
	public void testActive()
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

	// PrimeFaces standalone widget builder does not support some widget types.
	// Therefore, this test has been rewritten, with a few changes.

	@SuppressWarnings( "deprecation" )
	@Override
	public void testSharedWidgetBuilder()
			throws Exception {

		WidgetBuilder<UIComponent, UIMetawidget> widgetBuilder = newWidgetBuilder();

		// Action

		Map<String, String> attributes = CollectionUtils.newHashMap();
		HtmlCommandButton htmlCommandButton = (HtmlCommandButton) widgetBuilder.buildWidget( ACTION, attributes, new HtmlMetawidget() {

			@Override
			public String getLabelString( Map<String, String> passedAttributes ) {

				return "foo";
			}
		} );
		assertEquals( "foo", htmlCommandButton.getValue() );

		// No type not supported with PrimeFaces builder only

		assertEquals( null, widgetBuilder.buildWidget( PROPERTY, attributes, null ) );

		// Faces lookup

		attributes.put( FACES_LOOKUP, "#{foo.bar}" );
		HtmlSelectOneMenu htmlSelectOneMenu = (HtmlSelectOneMenu) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( "".equals( ( (UISelectItem) htmlSelectOneMenu.getChildren().get( 0 ) ).getItemLabel() ) );
		assertEquals( null, ( (UISelectItem) htmlSelectOneMenu.getChildren().get( 0 ) ).getItemValue() );
		assertEquals( "#{foo.bar}", ( htmlSelectOneMenu.getChildren().get( 1 ) ).getValueBinding( "value" ).getExpressionString() );
		assertEquals( null, ( htmlSelectOneMenu.getChildren().get( 1 ) ).getAttributes().get( "var" ) );
		assertEquals( null, ( htmlSelectOneMenu.getChildren().get( 1 ) ).getValueBinding( "itemLabel" ) );
		assertEquals( null, ( htmlSelectOneMenu.getChildren().get( 1 ) ).getValueBinding( "itemValue" ) );
		furtherAssert( htmlSelectOneMenu );

		attributes.put( FACES_LOOKUP_VAR, "_fooBar" );
		attributes.put( FACES_LOOKUP_ITEM_LABEL, "#{_fooBar.label}" );
		attributes.put( FACES_LOOKUP_ITEM_VALUE, "#{_fooBar.value}" );
		htmlSelectOneMenu = (HtmlSelectOneMenu) widgetBuilder.buildWidget( PROPERTY, attributes, null);
		assertTrue( "".equals( ( (UISelectItem) htmlSelectOneMenu.getChildren().get( 0 ) ).getItemLabel() ) );
		assertEquals( null, ( (UISelectItem) htmlSelectOneMenu.getChildren().get( 0 ) ).getItemValue() );
		assertEquals( "#{foo.bar}", ( htmlSelectOneMenu.getChildren().get( 1 ) ).getValueBinding( "value" ).getExpressionString() );
		assertEquals( "_fooBar", ( htmlSelectOneMenu.getChildren().get( 1 ) ).getAttributes().get( "var" ) );
		assertEquals( "#{_fooBar.label}", ( htmlSelectOneMenu.getChildren().get( 1 ) ).getValueBinding( "itemLabel" ).getExpressionString() );
		assertEquals( "#{_fooBar.value}", ( htmlSelectOneMenu.getChildren().get( 1 ) ).getValueBinding( "itemValue" ).getExpressionString() );
		furtherAssert( htmlSelectOneMenu );

		attributes.put( REQUIRED, TRUE );
		htmlSelectOneMenu = (HtmlSelectOneMenu) widgetBuilder.buildWidget( PROPERTY, attributes, null);
		assertEquals( "#{foo.bar}", (htmlSelectOneMenu.getChildren().get( 0 ) ).getValueBinding( "value" ).getExpressionString() );
		furtherAssert( htmlSelectOneMenu );
		attributes.remove( REQUIRED );

		attributes.put( TYPE, List.class.getName() );
		HtmlSelectManyCheckbox htmlSelectManyCheckbox = (HtmlSelectManyCheckbox) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( "pageDirection", htmlSelectManyCheckbox.getLayout() );
		assertEquals( "#{foo.bar}", (htmlSelectManyCheckbox.getChildren().get( 0 ) ).getValueBinding( "value" ).getExpressionString() );
		attributes.remove( FACES_LOOKUP );

		// Lookup

		attributes.put( TYPE, String.class.getName() );
		attributes.put( LOOKUP, "Foo, Bar, Baz" );
		HtmlMetawidget metawidget = new HtmlMetawidget();
		metawidget.setInspector( new PropertyTypeInspector() );
		htmlSelectOneMenu = (HtmlSelectOneMenu) widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( "", ( (UISelectItem) htmlSelectOneMenu.getChildren().get( 0 ) ).getItemLabel() );
		assertEquals( null, ( (UISelectItem) htmlSelectOneMenu.getChildren().get( 0 ) ).getItemValue() );
		assertEquals( "Foo", ( (UISelectItem) htmlSelectOneMenu.getChildren().get( 1 ) ).getItemLabel() );
		assertEquals( "Foo", ( (UISelectItem) htmlSelectOneMenu.getChildren().get( 1 ) ).getItemValue() );
		assertEquals( "Bar", ( (UISelectItem) htmlSelectOneMenu.getChildren().get( 2 ) ).getItemLabel() );
		assertEquals( "Bar", ( (UISelectItem) htmlSelectOneMenu.getChildren().get( 2 ) ).getItemValue() );
		assertEquals( "Baz", ( (UISelectItem) htmlSelectOneMenu.getChildren().get(3)).getItemLabel() );
		assertEquals( "Baz", ((UISelectItem) htmlSelectOneMenu.getChildren().get(3)).getItemValue() );
		furtherAssert( htmlSelectOneMenu );

		attributes.put( LOOKUP_LABELS, "foo-label, bar-label, baz-label" );
		htmlSelectOneMenu = (HtmlSelectOneMenu) widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( "", ( (UISelectItem) htmlSelectOneMenu.getChildren().get( 0 ) ).getItemLabel() );
		assertEquals( null, ( (UISelectItem) htmlSelectOneMenu.getChildren().get( 0 ) ).getItemValue() );
		assertEquals( "foo-label", ( (UISelectItem) htmlSelectOneMenu.getChildren().get( 1 ) ).getItemLabel() );
		assertEquals( "Foo", ( (UISelectItem) htmlSelectOneMenu.getChildren().get( 1 ) ).getItemValue() );
		assertEquals( "bar-label", ( (UISelectItem) htmlSelectOneMenu.getChildren().get( 2 ) ).getItemLabel() );
		assertEquals( "Bar", ( (UISelectItem) htmlSelectOneMenu.getChildren().get( 2 ) ).getItemValue() );
		assertEquals( "baz-label", ( (UISelectItem) htmlSelectOneMenu.getChildren().get(3) ).getItemLabel());
		assertEquals( "Baz", ((UISelectItem) htmlSelectOneMenu.getChildren().get(3)).getItemValue() );
		furtherAssert( htmlSelectOneMenu);
		attributes.remove( LOOKUP_LABELS );

		attributes.put( REQUIRED, TRUE );
		htmlSelectOneMenu = (HtmlSelectOneMenu) widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( "Foo", ( (UISelectItem) htmlSelectOneMenu.getChildren().get( 0 ) ).getItemLabel() );
		assertEquals( "Foo", ( (UISelectItem) htmlSelectOneMenu.getChildren().get( 0 ) ).getItemValue() );
		assertEquals( "Bar", ( (UISelectItem) htmlSelectOneMenu.getChildren().get( 1 ) ).getItemLabel() );
		assertEquals( "Bar", ( (UISelectItem) htmlSelectOneMenu.getChildren().get( 1 ) ).getItemValue() );
		assertEquals( "Baz", ( (UISelectItem) htmlSelectOneMenu.getChildren().get( 2) ).getItemLabel() );
		assertEquals( "Baz", ( (UISelectItem) htmlSelectOneMenu.getChildren().get( 2) ).getItemValue() );
		furtherAssert( htmlSelectOneMenu );
		attributes.remove( REQUIRED );

		attributes.put( TYPE, List.class.getName() );
		htmlSelectManyCheckbox = (HtmlSelectManyCheckbox) widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( null, htmlSelectManyCheckbox.getLayout() );
		attributes.put( LOOKUP, "Foo, Bar, Baz, Abc" );
		htmlSelectManyCheckbox = (HtmlSelectManyCheckbox) widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( "pageDirection", htmlSelectManyCheckbox.getLayout() );
		attributes.remove( LOOKUP );

		// Required Boolean not supported with PrimeFaces builder only

		attributes.put( TYPE, Boolean.class.getName() );
		attributes.put( REQUIRED, TRUE );
		assertEquals( null, widgetBuilder.buildWidget( PROPERTY, attributes, null ) );

		// boolean primitive not supported with PrimeFaces builder only

		attributes.put( TYPE, boolean.class.getName());
		assertEquals( null, widgetBuilder.buildWidget( PROPERTY, attributes, null ) );

		// char

		attributes.put( TYPE, char.class.getName() );
		attributes.put( MAXIMUM_LENGTH, "2" );
		HtmlInputText htmlInputText = (HtmlInputText) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( 1, htmlInputText.getMaxlength() );
		furtherAssert( htmlInputText );

		// int

		attributes.remove( MAXIMUM_LENGTH );
		attributes.put( TYPE, int.class.getName() );
		htmlInputText = (HtmlInputText) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( Integer.MIN_VALUE, htmlInputText.getMaxlength() );
		furtherAssert( htmlInputText);

		// Integer not supported in PrimeFaces only environment

		attributes.put( TYPE, Integer.class.getName());
		assertEquals( null, widgetBuilder.buildWidget( PROPERTY, attributes, null ) );

		// Large

		attributes.put( TYPE, String.class.getName() );
		attributes.remove( MASKED );
		attributes.put( LARGE, TRUE );
		HtmlInputTextarea htmlInputTextarea = (HtmlInputTextarea) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		furtherAssert( htmlInputTextarea );
	}

	@SuppressWarnings("deprecation")
	@Override
	public void testCollectionWithConverters() {

		String xml = "<?xml version=\"1.0\"?>";
		xml += "<inspection-result xmlns=\"http://www.metawidget.org/inspection-result\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.metawidget.org/inspection-result ../../inspector/inspection-result-1.0.xsd\" version=\"1.0\">";
		xml += "<entity type=\"ConverterFoo\">";
		xml += "<property name=\"bar\" faces-converter=\"barConverter\"/>";
		xml += "<property name=\"baz\" faces-converter=\"bazConverter\"/>";
		xml += "</entity>";
		xml += "</inspection-result>";

		HtmlMetawidget metawidget = new HtmlMetawidget();
		metawidget.setInspector( new XmlInspector( new XmlInspectorConfig().setInputStream( new ByteArrayInputStream( xml.getBytes() ) ) ) );

		WidgetBuilder<UIComponent, UIMetawidget> widgetBuilder = newWidgetBuilder();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( TYPE, List.class.getName() );
		attributes.put( PARAMETERIZED_TYPE, "ConverterFoo" );
		DataTable dataTable = (DataTable) widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );

		Column column = (Column) dataTable.getChildren().get( 0 );
		assertEquals( "Bar", ( (HtmlOutputText) column.getHeader() ).getValue() );
		HtmlOutputText outputText = (HtmlOutputText) column.getChildren().get( 0 );
		assertEquals( "#{" + dataTable.getVar() + ".bar}", outputText.getValueBinding( "value" ).getExpressionString() );
		assertEquals( " (from converter barConverter)", outputText.getConverter().getAsString( null, outputText, "" ) );

		column = (Column) dataTable.getChildren().get( 1 );
		assertEquals( "Baz", ( (HtmlOutputText) column.getHeader() ).getValue() );
		outputText = (HtmlOutputText) column.getChildren().get( 0 );
		assertEquals( "#{" + dataTable.getVar() + ".baz}", outputText.getValueBinding( "value" ).getExpressionString() );
		assertEquals( " (from converter bazConverter)", outputText.getConverter().getAsString( null, outputText, "" ) );

		assertEquals( 2, dataTable.getChildCount() );
	}

	@Override
	public void testCollectionWithSingleColumn() {

		HtmlMetawidget metawidget = new HtmlMetawidget();
		metawidget.setInspector( new PropertyTypeInspector() );

		WidgetBuilder<UIComponent, UIMetawidget> widgetBuilder = newWidgetBuilder();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( TYPE, List.class.getName() );
		DataTable dataTable = (DataTable) widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( 1, dataTable.getColumnsCount() );

		// Should not try and recurse String. Should just create a single column

		attributes.put( PARAMETERIZED_TYPE, String.class.getName() );
		dataTable = (DataTable) widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( 1, dataTable.getColumnsCount() );
	}

	@Override
	public void testCollectionWithManyColumns() {

		HtmlMetawidget metawidget = new HtmlMetawidget();
		metawidget.setInspector( new PropertyTypeInspector( new BaseObjectInspectorConfig().setPropertyStyle( new JavaBeanPropertyStyle( new JavaBeanPropertyStyleConfig().setSupportPublicFields( true ) ) ) ) );

		WidgetBuilder<UIComponent, UIMetawidget> widgetBuilder = newWidgetBuilder();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( TYPE, List.class.getName() );
		attributes.put( PARAMETERIZED_TYPE, LargeFoo.class.getName() );
		DataTable dataTable = (DataTable) widgetBuilder.buildWidget(PROPERTY, attributes, metawidget);
		assertEquals( 5, dataTable.getColumnsCount() );

		widgetBuilder = new PrimeFacesWidgetBuilder( new PrimeFacesWidgetBuilderConfig().setMaximumColumnsInDataTable( 2 ) );
		dataTable = (DataTable) widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( 2, dataTable.getColumnsCount() );

		widgetBuilder = new PrimeFacesWidgetBuilder( new PrimeFacesWidgetBuilderConfig().setMaximumColumnsInDataTable( 0 ) );
		dataTable = (DataTable) widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( 6, dataTable.getColumnsCount() );
	}

	@Override
	public void testConfig() {

		MetawidgetTestUtils.testEqualsAndHashcode( PrimeFacesWidgetBuilderConfig.class, new PrimeFacesWidgetBuilderConfig() {
			// Subclass
		});
	}

	//
	// Protected methods
	//

	@Override
	protected WidgetBuilder<UIComponent, UIMetawidget> newWidgetBuilder() {

		return new PrimeFacesWidgetBuilder();
	}

	@Override
	protected MockFacesContext newMockFacesContext() {

		return new MockFacesContext() {

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

				if ( DataTable.COMPONENT_TYPE.equals( componentName ) ) {
					return new DataTable();
				}

				if ( Column.COMPONENT_TYPE.equals( componentName ) ) {
					return new Column();
				}

				if ( SelectManyCheckbox.COMPONENT_TYPE.equals( componentName ) ) {
					return new SelectManyCheckbox();
				}

				return super.createComponent(componentName);
			}
		};
	}

	@Override
	protected void furtherAssert( UIComponent component ) {

		// Assert every component we create is an PrimeFaces component (never a regular JSF one)

		assertTrue( component.getClass().getPackage().getName().startsWith( "org.primefaces.component" ) );

	}
}
