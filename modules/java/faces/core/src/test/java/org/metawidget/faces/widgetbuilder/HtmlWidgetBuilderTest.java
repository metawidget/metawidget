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

package org.metawidget.faces.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.faces.FacesInspectionResultConstants.*;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.FacesException;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.UIOutput;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlInputSecret;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlInputTextarea;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.component.html.HtmlSelectManyCheckbox;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;

import junit.framework.TestCase;

import org.metawidget.faces.FacesMetawidgetTests.MockComponent;
import org.metawidget.faces.FacesMetawidgetTests.MockFacesContext;
import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.UIStub;
import org.metawidget.faces.component.html.HtmlMetawidget;
import org.metawidget.faces.component.html.widgetbuilder.HtmlLookupOutputText;
import org.metawidget.faces.component.html.widgetbuilder.HtmlWidgetBuilder;
import org.metawidget.faces.component.html.widgetbuilder.HtmlWidgetBuilderConfig;
import org.metawidget.faces.component.html.widgetbuilder.ReadOnlyWidgetBuilder;
import org.metawidget.faces.component.layout.SimpleLayout;
import org.metawidget.inspector.impl.BaseObjectInspectorConfig;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyle;
import org.metawidget.inspector.impl.propertystyle.javabean.JavaBeanPropertyStyleConfig;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.inspector.xml.XmlInspector;
import org.metawidget.inspector.xml.XmlInspectorConfig;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.MetawidgetTestUtils;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

/**
 * @author Richard Kennard
 */

public class HtmlWidgetBuilderTest
	extends TestCase {

	//
	// Private members
	//

	private FacesContext	mContext;

	//
	// Public methods
	//

	public void testReadOnly()
		throws Exception {

		WidgetBuilder<UIComponent, UIMetawidget> widgetBuilder = new ReadOnlyWidgetBuilder();
		HtmlMetawidget dummyMetawidget = new HtmlMetawidget();

		// Read only

		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( READ_ONLY, TRUE );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawidget ) instanceof HtmlOutputText );

		// Hidden

		attributes.put( HIDDEN, TRUE );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawidget ) instanceof UIStub );
		attributes.remove( HIDDEN );

		// Masked

		attributes.put( MASKED, TRUE );
		UIStub stub = (UIStub) widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawidget );
		assertEquals( 1, stub.getChildCount() );
		assertTrue( stub.getChildren().get( 0 ) instanceof UIStub );
		attributes.remove( MASKED );

		// Lookups

		attributes.put( LOOKUP, "foo" );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawidget ) instanceof HtmlOutputText );

		attributes.put( LOOKUP_LABELS, "Foo" );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawidget ) instanceof HtmlLookupOutputText );
		attributes.remove( LOOKUP_LABELS );
		attributes.remove( LOOKUP );

		// Faces lookup

		attributes.put( FACES_LOOKUP, "#{foo.bar}" );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawidget ) instanceof HtmlOutputText );
		attributes.remove( FACES_LOOKUP );

		// Other types

		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawidget ) instanceof HtmlOutputText );
		attributes.put( TYPE, int.class.getName() );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawidget ) instanceof HtmlOutputText );
		attributes.put( TYPE, Integer.class.getName() );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawidget ) instanceof HtmlOutputText );
		attributes.put( TYPE, Date.class.getName() );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawidget ) instanceof HtmlOutputText );

		// Arrays

		attributes.put( TYPE, Foo[].class.getName() );
		dummyMetawidget.setInspector( new PropertyTypeInspector() );
		assertEquals( null, widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawidget ) );

		// Lists

		attributes.put( TYPE, List.class.getName() );
		attributes.put( NAME, "bar" );

		// (dataTableRowAction cannot be wrapped when used on the JSP page)

		dummyMetawidget.setParameter( "dataTableRowAction", "foo.action" );
		assertEquals( null, widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawidget ) );

		// Other collections

		attributes.put( TYPE, Set.class.getName() );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawidget ) instanceof HtmlOutputText );

		// Unsupported types

		attributes.put( TYPE, Color.class.getName() );
		assertEquals( null, widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawidget ) );

		// Don't expand

		attributes.put( DONT_EXPAND, TRUE );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawidget ) instanceof HtmlOutputText );
		attributes.remove( DONT_EXPAND );

		dummyMetawidget.setLayout( new SimpleLayout() );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawidget ) instanceof HtmlOutputText );
	}

	public void testActive()
		throws Exception {

		WidgetBuilder<UIComponent, UIMetawidget> widgetBuilder = new HtmlWidgetBuilder();
		HtmlMetawidget dummyMetawidget = new HtmlMetawidget();

		// Hidden

		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( HIDDEN, TRUE );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawidget ) instanceof UIStub );
		attributes.remove( HIDDEN );

		// Masked

		attributes.put( TYPE, String.class.getName() );
		attributes.put( MASKED, TRUE );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, null ) instanceof HtmlInputSecret );

		// Overridden component

		attributes.put( FACES_COMPONENT, "foo" );
		assertEquals( "foo", ( (MockComponent) widgetBuilder.buildWidget( PROPERTY, attributes, null ) ).getFamily() );
		attributes.remove( FACES_COMPONENT );

		// Unsupported types

		attributes.put( TYPE, Color.class.getName() );
		assertEquals( null, widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawidget ) );

		// Unsupported Collections

		attributes.put( TYPE, Set.class.getName() );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawidget ) instanceof UIStub );

		// Don't expand

		attributes.put( TYPE, Color.class.getName() );
		attributes.put( DONT_EXPAND, TRUE );
		assertTrue( widgetBuilder.buildWidget( PROPERTY, attributes, dummyMetawidget ) instanceof HtmlInputText );
		attributes.remove( DONT_EXPAND );
	}

	@SuppressWarnings( "deprecation" )
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

		// No type

		HtmlInputText htmlInputText = (HtmlInputText) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		furtherAssert( htmlInputText );

		// Faces lookup

		attributes.put( FACES_LOOKUP, "#{foo.bar}" );
		HtmlSelectOneMenu htmlSelectOneMenu = (HtmlSelectOneMenu) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( "".equals( ( (UISelectItem) htmlSelectOneMenu.getChildren().get( 0 ) ).getItemLabel() ) );
		assertEquals( null, ( (UISelectItem) htmlSelectOneMenu.getChildren().get( 0 ) ).getItemValue() );
		assertEquals( "#{foo.bar}", ( (UISelectItems) htmlSelectOneMenu.getChildren().get( 1 ) ).getValueBinding( "value" ).getExpressionString() );
		assertEquals( null, ( (UISelectItems) htmlSelectOneMenu.getChildren().get( 1 ) ).getAttributes().get( "var" ) );
		assertEquals( null, ( (UISelectItems) htmlSelectOneMenu.getChildren().get( 1 ) ).getValueBinding( "itemLabel" ) );
		assertEquals( null, ( (UISelectItems) htmlSelectOneMenu.getChildren().get( 1 ) ).getValueBinding( "itemValue" ) );
		furtherAssert( htmlSelectOneMenu );

		attributes.put( FACES_LOOKUP_VAR, "_fooBar" );
		attributes.put( FACES_LOOKUP_ITEM_LABEL, "#{_fooBar.label}" );
		attributes.put( FACES_LOOKUP_ITEM_VALUE, "#{_fooBar.value}" );
		htmlSelectOneMenu = (HtmlSelectOneMenu) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertTrue( "".equals( ( (UISelectItem) htmlSelectOneMenu.getChildren().get( 0 ) ).getItemLabel() ) );
		assertEquals( null, ( (UISelectItem) htmlSelectOneMenu.getChildren().get( 0 ) ).getItemValue() );
		assertEquals( "#{foo.bar}", ( (UISelectItems) htmlSelectOneMenu.getChildren().get( 1 ) ).getValueBinding( "value" ).getExpressionString() );
		assertEquals( "_fooBar", ( (UISelectItems) htmlSelectOneMenu.getChildren().get( 1 ) ).getAttributes().get( "var" ) );
		assertEquals( "#{_fooBar.label}", ( (UISelectItems) htmlSelectOneMenu.getChildren().get( 1 ) ).getValueBinding( "itemLabel" ).getExpressionString() );
		assertEquals( "#{_fooBar.value}", ( (UISelectItems) htmlSelectOneMenu.getChildren().get( 1 ) ).getValueBinding( "itemValue" ).getExpressionString() );
		furtherAssert( htmlSelectOneMenu );

		attributes.put( REQUIRED, TRUE );
		htmlSelectOneMenu = (HtmlSelectOneMenu) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( "#{foo.bar}", ( (UISelectItems) htmlSelectOneMenu.getChildren().get( 0 ) ).getValueBinding( "value" ).getExpressionString() );
		furtherAssert( htmlSelectOneMenu );
		attributes.remove( REQUIRED );

		attributes.put( TYPE, List.class.getName() );
		HtmlSelectManyCheckbox htmlSelectManyCheckbox = (HtmlSelectManyCheckbox) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( "pageDirection", htmlSelectManyCheckbox.getLayout() );
		assertEquals( "#{foo.bar}", ( (UISelectItems) htmlSelectManyCheckbox.getChildren().get( 0 ) ).getValueBinding( "value" ).getExpressionString() );
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
		assertEquals( "Baz", ( (UISelectItem) htmlSelectOneMenu.getChildren().get( 3 ) ).getItemLabel() );
		assertEquals( "Baz", ( (UISelectItem) htmlSelectOneMenu.getChildren().get( 3 ) ).getItemValue() );
		furtherAssert( htmlSelectOneMenu );

		attributes.put( LOOKUP_LABELS, "foo-label, bar-label, baz-label" );
		htmlSelectOneMenu = (HtmlSelectOneMenu) widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( "", ( (UISelectItem) htmlSelectOneMenu.getChildren().get( 0 ) ).getItemLabel() );
		assertEquals( null, ( (UISelectItem) htmlSelectOneMenu.getChildren().get( 0 ) ).getItemValue() );
		assertEquals( "foo-label", ( (UISelectItem) htmlSelectOneMenu.getChildren().get( 1 ) ).getItemLabel() );
		assertEquals( "Foo", ( (UISelectItem) htmlSelectOneMenu.getChildren().get( 1 ) ).getItemValue() );
		assertEquals( "bar-label", ( (UISelectItem) htmlSelectOneMenu.getChildren().get( 2 ) ).getItemLabel() );
		assertEquals( "Bar", ( (UISelectItem) htmlSelectOneMenu.getChildren().get( 2 ) ).getItemValue() );
		assertEquals( "baz-label", ( (UISelectItem) htmlSelectOneMenu.getChildren().get( 3 ) ).getItemLabel() );
		assertEquals( "Baz", ( (UISelectItem) htmlSelectOneMenu.getChildren().get( 3 ) ).getItemValue() );
		furtherAssert( htmlSelectOneMenu );
		attributes.remove( LOOKUP_LABELS );

		attributes.put( REQUIRED, TRUE );
		htmlSelectOneMenu = (HtmlSelectOneMenu) widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( "Foo", ( (UISelectItem) htmlSelectOneMenu.getChildren().get( 0 ) ).getItemLabel() );
		assertEquals( "Foo", ( (UISelectItem) htmlSelectOneMenu.getChildren().get( 0 ) ).getItemValue() );
		assertEquals( "Bar", ( (UISelectItem) htmlSelectOneMenu.getChildren().get( 1 ) ).getItemLabel() );
		assertEquals( "Bar", ( (UISelectItem) htmlSelectOneMenu.getChildren().get( 1 ) ).getItemValue() );
		assertEquals( "Baz", ( (UISelectItem) htmlSelectOneMenu.getChildren().get( 2 ) ).getItemLabel() );
		assertEquals( "Baz", ( (UISelectItem) htmlSelectOneMenu.getChildren().get( 2 ) ).getItemValue() );
		furtherAssert( htmlSelectOneMenu );
		attributes.remove( REQUIRED );

		attributes.put( TYPE, List.class.getName() );
		htmlSelectManyCheckbox = (HtmlSelectManyCheckbox) widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( null, htmlSelectManyCheckbox.getLayout() );
		attributes.put( LOOKUP, "Foo, Bar, Baz, Abc" );
		htmlSelectManyCheckbox = (HtmlSelectManyCheckbox) widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( "pageDirection", htmlSelectManyCheckbox.getLayout() );
		attributes.remove( LOOKUP );

		// Boolean

		attributes.put( TYPE, Boolean.class.getName() );
		attributes.put( REQUIRED, TRUE );
		HtmlSelectBooleanCheckbox htmlSelectBooleanCheckbox = (HtmlSelectBooleanCheckbox) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		furtherAssert( htmlSelectBooleanCheckbox );

		// boolean

		attributes.put( TYPE, boolean.class.getName() );
		htmlSelectBooleanCheckbox = (HtmlSelectBooleanCheckbox) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		furtherAssert( htmlSelectBooleanCheckbox );

		// char

		attributes.put( TYPE, char.class.getName() );
		htmlInputText = (HtmlInputText) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( 1, htmlInputText.getMaxlength() );
		furtherAssert( htmlInputText );

		// int

		attributes.put( TYPE, int.class.getName() );
		htmlInputText = (HtmlInputText) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( Integer.MIN_VALUE, htmlInputText.getMaxlength() );
		furtherAssert( htmlInputText );

		// Integer

		attributes.put( TYPE, Integer.class.getName() );
		htmlInputText = (HtmlInputText) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( Integer.MIN_VALUE, htmlInputText.getMaxlength() );
		furtherAssert( htmlInputText );

		// Large

		attributes.put( TYPE, String.class.getName() );
		attributes.remove( MASKED );
		attributes.put( LARGE, TRUE );
		HtmlInputTextarea htmlInputTextarea = (HtmlInputTextarea) widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( 20, htmlInputTextarea.getCols() );
		assertEquals( 2, htmlInputTextarea.getRows() );
		furtherAssert( htmlInputTextarea );
	}

	@SuppressWarnings( "deprecation" )
	public void testCollectionWithConverters()
		throws Exception {

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
		UIData data = (UIData) widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );

		UIColumn column = (UIColumn) data.getChildren().get( 0 );
		assertEquals( "Bar", ( (UIOutput) column.getHeader() ).getValue() );
		UIOutput outputText = (UIOutput) column.getChildren().get( 0 );
		assertEquals( "#{" + data.getVar() + ".bar}", outputText.getValueBinding( "value" ).getExpressionString() );
		assertEquals( " (from converter barConverter)", outputText.getConverter().getAsString( null, outputText, "" ) );

		column = (UIColumn) data.getChildren().get( 1 );
		assertEquals( "Baz", ( (UIOutput) column.getHeader() ).getValue() );
		outputText = (UIOutput) column.getChildren().get( 0 );
		assertEquals( "#{" + data.getVar() + ".baz}", outputText.getValueBinding( "value" ).getExpressionString() );
		assertEquals( " (from converter bazConverter)", outputText.getConverter().getAsString( null, outputText, "" ) );

		assertEquals( 2, data.getChildCount() );
	}

	public void testCollectionWithSingleColumn()
		throws Exception {

		HtmlMetawidget metawidget = new HtmlMetawidget();
		metawidget.setInspector( new PropertyTypeInspector() );

		WidgetBuilder<UIComponent, UIMetawidget> widgetBuilder = newWidgetBuilder();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( TYPE, List.class.getName() );
		UIData data = (UIData) widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( 1, data.getChildCount() );

		// Should not try and recurse String. Should just create a single column

		attributes.put( PARAMETERIZED_TYPE, String.class.getName() );
		data = (UIData) widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( 1, data.getChildCount() );
	}

	public void testCollectionWithManyColumns()
		throws Exception {

		HtmlMetawidget metawidget = new HtmlMetawidget();
		metawidget.setInspector( new PropertyTypeInspector( new BaseObjectInspectorConfig().setPropertyStyle( new JavaBeanPropertyStyle( new JavaBeanPropertyStyleConfig().setSupportPublicFields( true ) ) ) ) );

		WidgetBuilder<UIComponent, UIMetawidget> widgetBuilder = newWidgetBuilder();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( TYPE, List.class.getName() );
		attributes.put( PARAMETERIZED_TYPE, LargeFoo.class.getName() );
		UIData data = (UIData) widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( 5, data.getChildCount() );

		widgetBuilder = new HtmlWidgetBuilder( new HtmlWidgetBuilderConfig().setMaximumColumnsInDataTable( 2 ) );
		data = (UIData) widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( 2, data.getChildCount() );

		widgetBuilder = new HtmlWidgetBuilder( new HtmlWidgetBuilderConfig().setMaximumColumnsInDataTable( 0 ) );
		data = (UIData) widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( 6, data.getChildCount() );
	}

	public void testConfig() {

		MetawidgetTestUtils.testEqualsAndHashcode( HtmlWidgetBuilderConfig.class, new HtmlWidgetBuilderConfig() {
			// Subclass
		} );
	}

	//
	// Protected methods
	//

	@Override
	protected final void setUp()
		throws Exception {

		super.setUp();

		mContext = newMockFacesContext();
	}

	protected MockFacesContext newMockFacesContext() {

		return new MockFacesContext() {

			@Override
			public UIComponent createComponent( String componentName )
				throws FacesException {

				if ( HtmlLookupOutputText.COMPONENT_TYPE.equals( componentName ) ) {
					return new HtmlLookupOutputText();
				}

				return super.createComponent( componentName );
			}
		};
	}

	protected WidgetBuilder<UIComponent, UIMetawidget> newWidgetBuilder() {

		return new HtmlWidgetBuilder();
	}

	/**
	 * @param component
	 */

	protected void furtherAssert( UIComponent component ) {

		// Nothing by default
	}

	@Override
	protected final void tearDown()
		throws Exception {

		super.tearDown();

		mContext.release();
	}

	//
	// Inner class
	//

	public static class Foo {

		public String	bar;

		public String	baz;

		public String	abc;
	}

	public static class LargeFoo {

		public String	column1;

		public String	column2;

		public String	column3;

		public String	column4;

		public String	column5;

		public String	column6;
	}
}
