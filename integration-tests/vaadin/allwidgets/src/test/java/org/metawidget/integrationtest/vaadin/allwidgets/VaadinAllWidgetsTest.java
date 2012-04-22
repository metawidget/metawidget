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

package org.metawidget.integrationtest.vaadin.allwidgets;

import java.util.Date;

import junit.framework.TestCase;

import org.metawidget.iface.MetawidgetException;
import org.metawidget.integrationtest.shared.allwidgets.model.AllWidgets;
import org.metawidget.vaadin.Stub;
import org.metawidget.vaadin.VaadinMetawidget;
import org.metawidget.vaadin.widgetprocessor.binding.simple.SimpleBindingProcessor;

import com.vaadin.data.Property;
import com.vaadin.event.ListenerMethod;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickShortcut;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

/**
 * @author Loghman Barari
 */

public class VaadinAllWidgetsTest
	extends TestCase {

	//
	// Protected statics
	//

	protected static final String	DATE_FORMAT	= "E MMM dd HH:mm:ss z yyyy";

	//
	// Constructor
	//

	public VaadinAllWidgetsTest() {

		// Default constructor
	}

	public VaadinAllWidgetsTest( String name ) {

		super( name );
	}

	//
	// Public methods
	//

	public void testAllWidgets()
		throws Exception {

		// Model

		AllWidgets allWidgets = new AllWidgets();

		// App

		VaadinMetawidget metawidget = new VaadinMetawidget();
		metawidget.setConfig( "org/metawidget/integrationtest/vaadin/allwidgets/metawidget.xml" );
		metawidget.setToInspect( allWidgets );

		metawidget.addComponent( new Stub( "mystery" ) );

		// Test missing components

		try {
			metawidget.getValue( "no-such-component" );
			fail();

		} catch ( MetawidgetException e1 ) {

			assertEquals( "No component named 'no-such-component'", e1.getMessage() );

			try {
				metawidget.getValue( "textbox", "no-such-component" );
				fail();

			} catch ( MetawidgetException e2 ) {

				assertEquals( "No component named 'textbox', 'no-such-component'", e2.getMessage() );

				try {
					metawidget.getValue( "textbox", "no-such-component1", "no-such-component2" );
					fail();

				} catch ( MetawidgetException e3 ) {

					assertEquals( "No such component 'no-such-component1' of 'textbox', 'no-such-component1', 'no-such-component2'", e3.getMessage() );
				}
			}
		}

		try {
			metawidget.setValue( null, "no-such-component" );
			fail();

		} catch ( MetawidgetException e1 ) {

			assertEquals( "No component named 'no-such-component'", e1.getMessage() );

			try {
				metawidget.setValue( null, "textbox", "no-such-component" );
				fail();

			} catch ( MetawidgetException e2 ) {

				assertEquals( "No component named 'textbox', 'no-such-component'", e2.getMessage() );

				try {
					metawidget.setValue( null, "textbox", "no-such-component1", "no-such-component2" );
					fail();

				} catch ( MetawidgetException e3 ) {
					assertEquals( "No such component 'no-such-component1' of 'textbox', 'no-such-component1', 'no-such-component2'", e3.getMessage() );
				}
			}
		}

		// Check what created, and edit it

		FormLayout layout = metawidget.getLayoutRoot();
		Component component = layout.getComponent( 0 );

		assertEquals( "Textbox:", component.getCaption() );
		assertTrue( component instanceof TextField );
		assertTrue( ( (TextField) component ).isRequired() );

		assertEquals( "Textbox", metawidget.getValue( "textbox" ) );
		( (TextField) component ).setValue( "Textbox1" );

		component = layout.getComponent( 1 );
		assertEquals( "Limited Textbox:", component.getCaption() );
		assertTrue( component instanceof TextField );
		assertEquals( 20, ( (TextField) component ).getMaxLength() );
		assertEquals( "Limited Textbox", metawidget.getValue( "limitedTextbox" ) );
		( (TextField) component ).setValue( "Limited Textbox1" );

		component = layout.getComponent( 2 );
		assertEquals( "Textarea:", component.getCaption() );
		assertTrue( component instanceof TextArea );
		assertEquals( 3, ( (TextArea) component ).getRows() );
		assertEquals( "Textarea", metawidget.getValue( "textarea" ) );
		assertTrue( ( (TextArea) component ).isWordwrap() );
		( (TextArea) component ).setValue( "Textarea1" );

		component = layout.getComponent( 3 );
		assertEquals( "Password:", component.getCaption() );
		assertTrue( component instanceof PasswordField );
		assertEquals( "Password", metawidget.getValue( "password" ) );
		( (PasswordField) component ).setValue( "Password1" );

		// Primitives

		component = layout.getComponent( 4 );
		assertEquals( "Byte Primitive:", component.getCaption() );
		assertTrue( component instanceof TextField );
		assertTrue( 0 < ( (TextField) component ).getValidators().size() );
		assertEquals( Byte.MAX_VALUE, metawidget.getValue( "bytePrimitive" ) );
		( (TextField) component ).setValue( (byte) 126 );

		component = layout.getComponent( 5 );
		assertEquals( "Byte Object:", component.getCaption() );
		assertTrue( component instanceof TextField );
		assertTrue( 0 < ( (TextField) component ).getValidators().size() );
		assertEquals( Byte.MIN_VALUE, metawidget.getValue( "byteObject" ) );
		( (TextField) component ).setValue( String.valueOf( Byte.MIN_VALUE + 1 ) );

		component = layout.getComponent( 6 );
		assertEquals( "Short Primitive:", component.getCaption() );
		assertTrue( component instanceof TextField );
		assertEquals( Short.MAX_VALUE, metawidget.getValue( "shortPrimitive" ) );
		assertTrue( 0 < ( (TextField) component ).getValidators().size() );
		( (TextField) component ).setValue( Short.MAX_VALUE - 1 );

		component = layout.getComponent( 7 );
		assertEquals( "Short Object:", component.getCaption() );
		assertTrue( component instanceof TextField );
		assertTrue( 0 < ( (TextField) component ).getValidators().size() );
		assertEquals( Short.MIN_VALUE, metawidget.getValue( "shortObject" ) );
		( (TextField) component ).setValue( Short.MIN_VALUE + 1 );

		component = layout.getComponent( 8 );
		assertEquals( "Int Primitive:", component.getCaption() );
		assertTrue( component instanceof TextField );
		assertEquals( Integer.MAX_VALUE, metawidget.getValue( "intPrimitive" ) );
		assertTrue( 0 < ( (TextField) component ).getValidators().size() );
		( (TextField) component ).setValue( Integer.MAX_VALUE - 1 );

		component = layout.getComponent( 9 );
		assertEquals( "Integer Object:", component.getCaption() );
		assertTrue( component instanceof TextField );
		assertTrue( 0 < ( (TextField) component ).getValidators().size() );
		assertEquals( Integer.MIN_VALUE, metawidget.getValue( "integerObject" ) );
		( (TextField) component ).setValue( Integer.MIN_VALUE + 1 );

		component = layout.getComponent( 10 );
		assertEquals( "Ranged Int:", component.getCaption() );
		assertTrue( component instanceof TextField );
		assertTrue( 0 < ( (TextField) component ).getValidators().size() );
		assertEquals( 32, metawidget.getValue( "rangedInt" ) );
		( (TextField) component ).setValue( 33 );

		component = layout.getComponent( 11 );
		assertEquals( "Ranged Integer:", component.getCaption() );
		assertTrue( component instanceof TextField );
		assertEquals( 33, metawidget.getValue( "rangedInteger" ) );
		( (TextField) component ).setValue( 34 );

		component = layout.getComponent( 12 );
		assertEquals( "Long Primitive:", component.getCaption() );
		assertTrue( component instanceof TextField );
		assertEquals( 42L, metawidget.getValue( "longPrimitive" ) );
		( (TextField) component ).setValue( 43 );

		component = layout.getComponent( 13 );
		assertTrue( component instanceof TextField );
		assertEquals( 43L, metawidget.getValue( "longObject" ) );
		( (TextField) component ).setValue( "44" );

		component = layout.getComponent( 14 );
		assertEquals( "Float Primitive:", component.getCaption() );
		assertTrue( component instanceof TextField );
		assertEquals( 4.2F, metawidget.getValue( "floatPrimitive" ) );
		( (TextField) component ).setValue( 4.3f );

		component = layout.getComponent( 15 );
		assertEquals( "nullInBundle:", component.getCaption() );
		assertTrue( component instanceof TextField );
		assertEquals( 4.3F, metawidget.getValue( "floatObject" ) );
		( (TextField) component ).setValue( 5.4f );

		component = layout.getComponent( 16 );
		assertEquals( "Double Primitive:", component.getCaption() );
		assertTrue( component instanceof TextField );
		assertEquals( 42.2D, metawidget.getValue( "doublePrimitive" ) );
		( (TextField) component ).setValue( 42.3D );

		component = layout.getComponent( 17 );
		assertTrue( component instanceof TextField );
		assertEquals( 43.3D, metawidget.getValue( "doubleObject" ) );
		( (TextField) component ).setValue( 54.4D );

		component = layout.getComponent( 18 );
		assertEquals( "Char Primitive:", component.getCaption() );
		assertTrue( component instanceof TextField );
		assertEquals( 'A', metawidget.getValue( "charPrimitive" ) );
		( (TextField) component ).setValue( "Z" );

		component = layout.getComponent( 19 );
		assertEquals( "Character Object:", component.getCaption() );
		assertTrue( component instanceof TextField );
		assertEquals( 'Z', metawidget.getValue( "characterObject" ) );
		( (TextField) component ).setValue( "A" );

		assertEquals( "Boolean Primitive:", layout.getComponent( 20 ).getCaption() );
		assertTrue( layout.getComponent( 20 ) instanceof CheckBox );
		assertEquals( false, ( (Boolean) metawidget.getValue( "booleanPrimitive" ) ).booleanValue() );
		( (CheckBox) layout.getComponent( 20 ) ).setValue( true );

		component = layout.getComponent( 21 );
		assertEquals( "Boolean Object:", component.getCaption() );
		assertTrue( component instanceof ComboBox );
		assertEquals( 2, ( (ComboBox) component ).getContainerDataSource().size() );
		assertEquals( Boolean.TRUE, metawidget.getValue( "booleanObject" ) );
		( (ComboBox) component ).setValue( "false" );

		component = layout.getComponent( 22 );
		assertEquals( "Dropdown:", component.getCaption() );
		assertTrue( component instanceof ComboBox );
		assertEquals( 3, ( (ComboBox) component ).getContainerDataSource().size() );
		assertEquals( "dropdown1", metawidget.getValue( "dropdown" ) );
		( (ComboBox) component ).setValue( "foo1" );

		component = layout.getComponent( 23 );
		assertEquals( "Dropdown With Labels:", component.getCaption() );
		assertTrue( component instanceof ComboBox );
		ComboBox combo = (ComboBox) component;
		assertEquals( 4, combo.getContainerDataSource().size() );
		assertEquals( "Foo #2", combo.getItemCaption( "foo2" ) );
		assertEquals( "Dropdown #2", combo.getItemCaption( "dropdown2" ) );
		assertEquals( "Bar #2", combo.getItemCaption( "bar2" ) );
		assertEquals( "Baz #2", combo.getItemCaption( "baz2" ) );
		assertEquals( "dropdown2", metawidget.getValue( "dropdownWithLabels" ) );
		( (ComboBox) component ).setValue( "bar2" );

		component = layout.getComponent( 24 );
		assertEquals( "Not Null Dropdown:", component.getCaption() );
		assertTrue( component instanceof ComboBox );
		assertEquals( 3, ( (ComboBox) component ).getContainerDataSource().size() );
		assertEquals( 0, ( (Byte) metawidget.getValue( "notNullDropdown" ) ).byteValue() );
		( (ComboBox) component ).setValue( "1" );

		component = layout.getComponent( 25 );
		assertEquals( "Not Null Object Dropdown:", component.getCaption() );
		assertTrue( component instanceof ComboBox );
		assertTrue( ( (ComboBox) component ).isRequired() );
		assertEquals( 6, ( (ComboBox) component ).getContainerDataSource().size() );
		assertEquals( "dropdown3", metawidget.getValue( "notNullObjectDropdown" ) );
		( (ComboBox) component ).setValue( "foo3" );

		assertEquals( "Nested Widgets:", layout.getComponent( 26 ).getCaption() );
		assertTrue( layout.getComponent( 26 ) instanceof VaadinMetawidget );

		FormLayout layoutNested = ( (VaadinMetawidget) layout.getComponent( 26 ) ).getLayoutRoot();

		assertEquals( "Further Nested Widgets:", layoutNested.getComponent( 0 ).getCaption() );

		FormLayout layoutFurtherNested = ( (VaadinMetawidget) layoutNested.getComponent( 0 ) ).getLayoutRoot();

		component = layoutFurtherNested.getComponent( 0 );
		assertEquals( "Further Nested Widgets:", component.getCaption() );
		assertTrue( component instanceof VaadinMetawidget );
		assertEquals( ( (FormLayout) ( (VaadinMetawidget) component ).getLayoutRoot() ).getComponentCount(), 0 );

		component = layoutFurtherNested.getComponent( 1 );
		assertEquals( "Nested Textbox 1:", component.getCaption() );
		assertTrue( component instanceof TextField );
		assertEquals( "Nested Textbox 1", metawidget.getValue( "nestedWidgets", "furtherNestedWidgets", "nestedTextbox1" ) );
		( (TextField) component ).setValue( "Nested Textbox 1.1 (further)" );

		component = layoutFurtherNested.getComponent( 2 );
		assertEquals( "Nested Textbox 2:", component.getCaption() );
		assertTrue( component instanceof TextField );
		assertEquals( "Nested Textbox 2", metawidget.getValue( "nestedWidgets", "furtherNestedWidgets", "nestedTextbox2" ) );
		( (TextField) component ).setValue( "Nested Textbox 2.2 (further)" );

		component = layoutNested.getComponent( 1 );
		assertEquals( "Nested Textbox 1:", component.getCaption() );
		assertTrue( component instanceof TextField );
		assertEquals( "Nested Textbox 1", metawidget.getValue( "nestedWidgets", "nestedTextbox1" ) );
		( (TextField) component ).setValue( "Nested Textbox 1.1" );

		component = layoutNested.getComponent( 2 );
		assertEquals( "Nested Textbox 2:", component.getCaption() );
		assertTrue( component instanceof TextField );
		assertEquals( "Nested Textbox 2", metawidget.getValue( "nestedWidgets", "nestedTextbox2" ) );
		( (TextField) component ).setValue( "Nested Textbox 2.2" );

		component = layout.getComponent( 27 );
		assertEquals( "Read Only Nested Widgets:", layout.getComponent( 27 ).getCaption() );
		assertTrue( component instanceof VaadinMetawidget );

		layoutNested = (FormLayout) ( (VaadinMetawidget) component ).getLayoutRoot();
		component = layoutNested.getComponent( 0 );
		assertEquals( "Further Nested Widgets:", component.getCaption() );
		assertTrue( component instanceof VaadinMetawidget );
		assertEquals( ( (FormLayout) ( (VaadinMetawidget) component ).getLayoutRoot() ).getComponentCount(), 0 );

		component = layoutNested.getComponent( 1 );
		assertEquals( "Nested Textbox 1:", component.getCaption() );
		assertTrue( component instanceof Label );
		assertEquals( "Nested Textbox 1", metawidget.getValue( "readOnlyNestedWidgets", "nestedTextbox1" ) );

		component = layoutNested.getComponent( 2 );
		assertEquals( "Nested Textbox 2:", component.getCaption() );
		assertTrue( component instanceof Label );
		assertEquals( "Nested Textbox 2", metawidget.getValue( "readOnlyNestedWidgets", "nestedTextbox2" ) );

		component = layout.getComponent( 28 );
		assertEquals( "Nested Widgets Dont Expand:", component.getCaption() );
		assertTrue( component instanceof TextField );
		assertEquals( "Nested Textbox 1, Nested Textbox 2", ( (TextField) component ).getValue().toString() );
		( (TextField) component ).setValue( "Nested Textbox 1.01, Nested Textbox 2.02" );

		component = layout.getComponent( 29 );
		assertEquals( "Read Only Nested Widgets Dont Expand:", component.getCaption() );
		assertTrue( component instanceof Label );
		assertEquals( "Nested Textbox 1, Nested Textbox 2", ( (Label) component ).getValue().toString() );

		component = layout.getComponent( 30 );
		assertEquals( "Date:", component.getCaption() );
		assertTrue( component instanceof PopupDateField );
		assertEquals( allWidgets.getDate(), metawidget.getValue( "date" ) );
		try {
			( (PopupDateField) component ).setValue( "bad date" );
			fail();
		} catch ( Exception e ) {
			assertTrue( e instanceof Property.ConversionException );
		}

		component = layout.getComponent( 31 );
		assertTrue( component instanceof Label );
		assertEquals( "h1", ( (Label) component ).getStyleName() );
		assertEquals( "Section Break", ( (Label) component ).getValue() );

		component = layout.getComponent( 32 );
		assertEquals( "Read Only:", component.getCaption() );
		assertTrue( component instanceof Label );
		assertEquals( "Read Only", metawidget.getValue( "readOnly" ) );

		assertEquals( "Do Action", layout.getComponent( 33 ).getCaption() );
		assertTrue( layout.getComponent( 33 ) instanceof Button );
		Button button = (Button) layout.getComponent( 33 );
		assertTrue( button.isEnabled() );
		try {
			clickButton( button );
			fail();
		} catch ( Exception e ) {
			assertTrue( e instanceof ListenerMethod.MethodException );
			assertEquals( "doAction called", e.getCause().getCause().getCause().getMessage() );
		}

		assertEquals( 34, layout.getComponentCount() );

		// Check MetawidgetException

		try {
			metawidget.getWidgetProcessor( SimpleBindingProcessor.class ).save( metawidget );
			fail();
		} catch ( Exception e ) {
			assertTrue( e instanceof DateField.UnparsableDateString );
		}

		// Check saving

		Date now = new Date();

		( (PopupDateField) layout.getComponent( 30 ) ).setValue( now );
		metawidget.getWidgetProcessor( SimpleBindingProcessor.class ).save( metawidget );

		// Check read-only

		metawidget.setReadOnly( true );
		layout = metawidget.getLayoutRoot();

		component = layout.getComponent( 0 );
		assertEquals( "Textbox:", component.getCaption() );
		assertEquals( "Textbox1", ( (Label) component ).getValue() );
		component = layout.getComponent( 1 );
		assertEquals( "Limited Textbox:", component.getCaption() );
		assertEquals( "Limited Textbox1", ( (Label) component ).getValue() );
		component = layout.getComponent( 2 );
		assertEquals( "Textarea:", component.getCaption() );
		assertEquals( "Textarea1", ( (Label) component ).getValue() );
		component = layout.getComponent( 3 );
		assertEquals( "Password:", component.getCaption() );
		component = layout.getComponent( 4 );
		assertEquals( "Byte Primitive:", component.getCaption() );
		assertEquals( (byte) 126, ( (Label) component ).getValue() );
		component = layout.getComponent( 5 );
		assertEquals( "Byte Object:", component.getCaption() );
		assertEquals( (byte) -127, ( (Label) component ).getValue() );
		component = layout.getComponent( 6 );
		assertEquals( "Short Primitive:", component.getCaption() );
		assertEquals( (short) 32766, ( (Label) component ).getValue() );
		component = layout.getComponent( 7 );
		assertEquals( "Short Object:", component.getCaption() );
		assertEquals( (short) -32767, ( (Label) component ).getValue() );
		component = layout.getComponent( 8 );
		assertEquals( "Int Primitive:", component.getCaption() );
		assertEquals( 2147483646, ( (Label) component ).getValue() );
		component = layout.getComponent( 9 );
		assertEquals( "Integer Object:", component.getCaption() );
		assertEquals( -2147483647, ( (Label) component ).getValue() );
		component = layout.getComponent( 10 );
		assertEquals( "Ranged Int:", component.getCaption() );
		assertEquals( 33, ( (Label) component ).getValue() );
		component = layout.getComponent( 11 );
		assertEquals( "Ranged Integer:", component.getCaption() );
		assertEquals( 34, ( (Label) component ).getValue() );
		component = layout.getComponent( 12 );
		assertEquals( "Long Primitive:", component.getCaption() );
		assertEquals( 43L, ( (Label) component ).getValue() );
		component = layout.getComponent( 13 );
		assertEquals( 44L, ( (Label) component ).getValue() );
		component = layout.getComponent( 14 );
		assertEquals( "Float Primitive:", component.getCaption() );
		assertEquals( 4.3f, ( (Label) component ).getValue() );
		component = layout.getComponent( 15 );
		assertEquals( "nullInBundle:", component.getCaption() );
		assertEquals( 5.4F, ( (Label) component ).getValue() );
		component = layout.getComponent( 16 );
		assertEquals( "Double Primitive:", component.getCaption() );
		assertEquals( 42.3D, ( (Label) component ).getValue() );
		component = layout.getComponent( 17 );
		assertEquals( 54.4D, ( (Label) component ).getValue() );
		component = layout.getComponent( 18 );
		assertEquals( "Char Primitive:", component.getCaption() );
		assertEquals( 'Z', ( (Label) component ).getValue() );
		component = layout.getComponent( 19 );
		assertEquals( "Character Object:", component.getCaption() );
		assertEquals( 'A', ( (Label) component ).getValue() );
		component = layout.getComponent( 20 );
		assertEquals( "Boolean Primitive:", component.getCaption() );
		assertEquals( true, ( (Label) component ).getValue() );
		component = layout.getComponent( 21 );
		assertEquals( "Boolean Object:", component.getCaption() );
		assertEquals( "No", ( (Label) component ).getValue() );
		component = layout.getComponent( 22 );
		assertEquals( "Dropdown:", component.getCaption() );
		assertEquals( "foo1", ( (Label) component ).getValue() );
		component = layout.getComponent( 23 );
		assertEquals( "Dropdown With Labels:", component.getCaption() );
		assertEquals( "Bar #2", ( (Label) component ).getValue() );
		component = layout.getComponent( 24 );
		assertEquals( "Not Null Dropdown:", component.getCaption() );
		assertEquals( "1", ( (Label) component ).getValue() );
		component = layout.getComponent( 25 );
		assertEquals( "Not Null Object Dropdown:", component.getCaption() );
		assertEquals( "foo3", ( (Label) component ).getValue() );
		assertEquals( "Nested Widgets:", layout.getComponent( 26 ).getCaption() );
		assertTrue( layout.getComponent( 26 ) instanceof VaadinMetawidget );
		layoutNested = ( (VaadinMetawidget) layout.getComponent( 26 ) ).getLayoutRoot();
		assertEquals( "Further Nested Widgets:", layoutNested.getComponent( 0 ).getCaption() );
		assertTrue( layoutNested.getComponent( 0 ) instanceof VaadinMetawidget );
		layoutFurtherNested = ( (VaadinMetawidget) layoutNested.getComponent( 0 ) ).getLayoutRoot();
		component = layoutFurtherNested.getComponent( 0 );
		assertEquals( "Further Nested Widgets:", component.getCaption() );
		assertTrue( component instanceof VaadinMetawidget );
		assertEquals( ( (FormLayout) ( (VaadinMetawidget) component ).getLayoutRoot() ).getComponentCount(), 0 );
		component = layoutFurtherNested.getComponent( 1 );
		assertEquals( "Nested Textbox 1:", component.getCaption() );
		assertEquals( "Nested Textbox 1.1 (further)", ( (Label) component ).getValue() );
		component = layoutFurtherNested.getComponent( 2 );
		assertEquals( "Nested Textbox 2:", component.getCaption() );
		assertEquals( "Nested Textbox 2.2 (further)", ( (Label) component ).getValue() );
		component = layoutNested.getComponent( 1 );
		assertEquals( "Nested Textbox 1:", component.getCaption() );
		assertEquals( "Nested Textbox 1.1", ( (Label) component ).getValue() );
		component = layoutNested.getComponent( 2 );
		assertEquals( "Nested Textbox 2:", component.getCaption() );
		assertEquals( "Nested Textbox 2.2", ( (Label) component ).getValue() );
		assertEquals( "Read Only Nested Widgets:", layout.getComponent( 27 ).getCaption() );
		assertTrue( layout.getComponent( 27 ) instanceof VaadinMetawidget );
		layoutNested = ( (VaadinMetawidget) layout.getComponent( 27 ) ).getLayoutRoot();
		component = layoutNested.getComponent( 0 );
		assertEquals( "Further Nested Widgets:", component.getCaption() );
		assertTrue( component instanceof VaadinMetawidget );
		assertEquals( ( (FormLayout) ( (VaadinMetawidget) component ).getLayoutRoot() ).getComponentCount(), 0 );
		component = layoutNested.getComponent( 1 );
		assertEquals( "Nested Textbox 1:", component.getCaption() );
		assertEquals( "Nested Textbox 1", ( (Label) component ).getValue() );
		component = layoutNested.getComponent( 2 );
		assertEquals( "Nested Textbox 2:", component.getCaption() );
		assertEquals( "Nested Textbox 2", ( (Label) component ).getValue() );
		component = layout.getComponent( 28 );
		assertEquals( "Nested Widgets Dont Expand:", component.getCaption() );
		assertEquals( "Nested Textbox 1.01, Nested Textbox 2.02", ( (Label) component ).getValue().toString() );
		component = layout.getComponent( 29 );
		assertEquals( "Read Only Nested Widgets Dont Expand:", component.getCaption() );
		assertEquals( "Nested Textbox 1, Nested Textbox 2", ( (Label) component ).getValue().toString() );
		component = layout.getComponent( 30 );
		assertEquals( "Date:", component.getCaption() );
		assertEquals( now, ( (Label) component ).getValue() );
		component = layout.getComponent( 31 );
		assertTrue( component instanceof Label );
		assertEquals( "h1", ( (Label) component ).getStyleName() );
		assertEquals( "Section Break", ( (Label) component ).getValue() );
		component = layout.getComponent( 32 );
		assertEquals( "Read Only:", component.getCaption() );
		assertEquals( "Read Only", ( (Label) component ).getValue() );
		component = layout.getComponent( 33 );
		assertEquals( "Do Action", layout.getComponent( 33 ).getCaption() );
		assertTrue( layout.getComponent( 33 ) instanceof Button );
		assertFalse( ( (Button) layout.getComponent( 33 ) ).isEnabled() );
		assertEquals( 34, layout.getComponentCount() );
	}

	private void clickButton( Button button ) {

		ClickShortcut clickShortcut = new ClickShortcut( button, "" );

		clickShortcut.handleAction( null, null );
	}
}