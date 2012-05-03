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
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Select;
import com.vaadin.ui.Slider;
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

		assertEquals( null, metawidget.getComponent( "no-such-component" ) );
		assertEquals( null, metawidget.getComponent( "textbox", "no-such-component" ) );

		try {
			metawidget.getComponent( "textbox", "no-such-component1", "no-such-component2" );
			fail();

		} catch ( MetawidgetException e3 ) {

			assertEquals( "No such component 'no-such-component1' of 'textbox', 'no-such-component1', 'no-such-component2'", e3.getMessage() );
		}

		// Check what created, and edit it

		FormLayout layout = metawidget.getContent();
		Component component = layout.getComponent( 0 );

		assertEquals( "Textbox:", component.getCaption() );
		assertTrue( component instanceof TextField );
		assertTrue( ( (TextField) component ).isRequired() );

		assertEquals( "Textbox", ( (Property) metawidget.getComponent( "textbox" ) ).getValue() );
		( (TextField) component ).setValue( "Textbox1" );

		component = layout.getComponent( 1 );
		assertEquals( "Limited Textbox:", component.getCaption() );
		assertTrue( component instanceof TextField );
		assertEquals( 20, ( (TextField) component ).getMaxLength() );
		assertEquals( "Limited Textbox", ( (Property) metawidget.getComponent( "limitedTextbox" ) ).getValue() );
		( (TextField) component ).setValue( "Limited Textbox1" );

		component = layout.getComponent( 2 );
		assertEquals( "Textarea:", component.getCaption() );
		assertTrue( component instanceof TextArea );
		assertEquals( "Textarea", ( (Property) metawidget.getComponent( "textarea" ) ).getValue() );
		assertTrue( ( (TextArea) component ).isWordwrap() );
		( (TextArea) component ).setValue( "Textarea1" );

		component = layout.getComponent( 3 );
		assertEquals( "Password:", component.getCaption() );
		assertTrue( component instanceof PasswordField );
		assertEquals( "Password", ( (Property) metawidget.getComponent( "password" ) ).getValue() );
		( (PasswordField) component ).setValue( "Password1" );

		// Primitives

		component = layout.getComponent( 4 );
		assertEquals( "Byte Primitive:", component.getCaption() );
		assertTrue( component instanceof TextField );
		assertEquals( 1, ( (TextField) component ).getValidators().size() );
		assertEquals( String.valueOf( Byte.MAX_VALUE ), ( (Property) metawidget.getComponent( "bytePrimitive" ) ).getValue() );
		( (TextField) component ).setValue( (byte) ( Byte.MAX_VALUE - 1 ) );

		component = layout.getComponent( 5 );
		assertEquals( "Byte Object:", component.getCaption() );
		assertTrue( component instanceof TextField );
		assertEquals( null, ( (TextField) component ).getValidators() );
		assertEquals( String.valueOf( Byte.MIN_VALUE ), ( (Property) metawidget.getComponent( "byteObject" ) ).getValue() );
		( (TextField) component ).setValue( String.valueOf( Byte.MIN_VALUE + 1 ) );

		component = layout.getComponent( 6 );
		assertEquals( "Short Primitive:", component.getCaption() );
		assertTrue( component instanceof TextField );
		assertEquals( String.valueOf( Short.MAX_VALUE ), ( (Property) metawidget.getComponent( "shortPrimitive" ) ).getValue() );
		assertEquals( 1, ( (TextField) component ).getValidators().size() );
		( (TextField) component ).setValue( (short) ( Short.MAX_VALUE - 1 ) );

		component = layout.getComponent( 7 );
		assertEquals( "Short Object:", component.getCaption() );
		assertTrue( component instanceof TextField );
		assertEquals( null, ( (TextField) component ).getValidators() );
		assertEquals( String.valueOf( Short.MIN_VALUE ), ( (Property) metawidget.getComponent( "shortObject" ) ).getValue() );
		( (TextField) component ).setValue( String.valueOf( Short.MIN_VALUE + 1 ) );

		component = layout.getComponent( 8 );
		assertEquals( "Int Primitive:", component.getCaption() );
		assertTrue( component instanceof TextField );
		assertEquals( String.valueOf( Integer.MAX_VALUE ), ( (Property) metawidget.getComponent( "intPrimitive" ) ).getValue() );
		assertEquals( null, ( (TextField) component ).getValidators() );
		( (TextField) component ).setValue( String.valueOf( Integer.MAX_VALUE - 1 ) );

		component = layout.getComponent( 9 );
		assertEquals( "Integer Object:", component.getCaption() );
		assertTrue( component instanceof TextField );
		assertEquals( null, ( (TextField) component ).getValidators() );
		assertEquals( String.valueOf( Integer.MIN_VALUE ), ( (Property) metawidget.getComponent( "integerObject" ) ).getValue() );
		( (TextField) component ).setValue( String.valueOf( Integer.MIN_VALUE + 1 ) );

		component = layout.getComponent( 10 );
		assertEquals( "Ranged Int:", component.getCaption() );
		assertTrue( component instanceof Slider );
		assertEquals( 1d, ( (Slider) component ).getMin() );
		assertEquals( 100d, ( (Slider) component ).getMax() );
		assertEquals( "32", String.valueOf( ( (Property) metawidget.getComponent( "rangedInt" ) ).getValue() ) );
		( (Slider) component ).setValue( "33" );

		component = layout.getComponent( 11 );
		assertEquals( "Ranged Integer:", component.getCaption() );
		assertTrue( component instanceof TextField );
		assertEquals( "33", ( (Property) metawidget.getComponent( "rangedInteger" ) ).getValue() );
		( (TextField) component ).setValue( "34" );

		component = layout.getComponent( 12 );
		assertEquals( "Long Primitive:", component.getCaption() );
		assertTrue( component instanceof TextField );
		assertEquals( "42", ( (Property) metawidget.getComponent( "longPrimitive" ) ).getValue() );
		( (TextField) component ).setValue( "43" );

		component = layout.getComponent( 13 );
		assertTrue( component instanceof TextField );
		assertEquals( "43", ( (Property) metawidget.getComponent( "longObject" ) ).getValue() );
		( (TextField) component ).setValue( "44" );

		component = layout.getComponent( 14 );
		assertEquals( "Float Primitive:", component.getCaption() );
		assertTrue( component instanceof TextField );
		assertEquals( "4.2", ( (Property) metawidget.getComponent( "floatPrimitive" ) ).getValue() );
		( (TextField) component ).setValue( "4.3" );

		component = layout.getComponent( 15 );
		assertEquals( "nullInBundle:", component.getCaption() );
		assertTrue( component instanceof TextField );
		assertEquals( "4.3", ( (Property) metawidget.getComponent( "floatObject" ) ).getValue() );
		( (TextField) component ).setValue( "5.4" );

		component = layout.getComponent( 16 );
		assertEquals( "Double Primitive:", component.getCaption() );
		assertTrue( component instanceof TextField );
		assertEquals( "42.2", ( (Property) metawidget.getComponent( "doublePrimitive" ) ).getValue() );
		( (TextField) component ).setValue( "42.3" );

		component = layout.getComponent( 17 );
		assertTrue( component instanceof TextField );
		assertEquals( "43.3", ( (Property) metawidget.getComponent( "doubleObject" ) ).getValue() );
		( (TextField) component ).setValue( "54.4" );

		component = layout.getComponent( 18 );
		assertEquals( "Char Primitive:", component.getCaption() );
		assertTrue( component instanceof TextField );
		assertEquals( 1, ( (TextField) component ).getMaxLength() );
		assertEquals( "A", ( (Property) metawidget.getComponent( "charPrimitive" ) ).getValue() );
		( (TextField) component ).setValue( "Z" );

		component = layout.getComponent( 19 );
		assertEquals( "Character Object:", component.getCaption() );
		assertTrue( component instanceof TextField );
		assertEquals( 1, ( (TextField) component ).getMaxLength() );
		assertEquals( "Z", ( (Property) metawidget.getComponent( "characterObject" ) ).getValue() );
		( (TextField) component ).setValue( "A" );

		assertEquals( "Boolean Primitive:", layout.getComponent( 20 ).getCaption() );
		assertTrue( layout.getComponent( 20 ) instanceof CheckBox );
		assertEquals( false, ( (Boolean) ( (Property) metawidget.getComponent( "booleanPrimitive" ) ).getValue() ).booleanValue() );
		( (CheckBox) layout.getComponent( 20 ) ).setValue( true );

		component = layout.getComponent( 21 );
		assertEquals( "Boolean Object:", component.getCaption() );
		assertTrue( component instanceof Select );
		assertEquals( 2, ( (Select) component ).getContainerDataSource().size() );
		assertEquals( Boolean.TRUE, ( (Property) metawidget.getComponent( "booleanObject" ) ).getValue() );
		( (Select) component ).setValue( Boolean.FALSE );

		component = layout.getComponent( 22 );
		assertEquals( "Dropdown:", component.getCaption() );
		assertTrue( component instanceof Select );
		assertEquals( 3, ( (Select) component ).getContainerDataSource().size() );
		assertEquals( "dropdown1", ( (Property) metawidget.getComponent( "dropdown" ) ).getValue() );
		( (Select) component ).setValue( "foo1" );

		component = layout.getComponent( 23 );
		assertEquals( "Dropdown With Labels:", component.getCaption() );
		assertTrue( component instanceof Select );
		Select combo = (Select) component;
		assertEquals( 4, combo.getContainerDataSource().size() );
		assertEquals( "Foo #2", combo.getItemCaption( "foo2" ) );
		assertEquals( "Dropdown #2", combo.getItemCaption( "dropdown2" ) );
		assertEquals( "Bar #2", combo.getItemCaption( "bar2" ) );
		assertEquals( "Baz #2", combo.getItemCaption( "baz2" ) );
		assertEquals( "dropdown2", ( (Property) metawidget.getComponent( "dropdownWithLabels" ) ).getValue() );
		( (Select) component ).setValue( "bar2" );

		component = layout.getComponent( 24 );
		assertEquals( "Not Null Dropdown:", component.getCaption() );
		assertTrue( component instanceof Select );
		assertEquals( 3, ( (Select) component ).getContainerDataSource().size() );
		assertEquals( 0, ( (Byte) ( (Property) metawidget.getComponent( "notNullDropdown" ) ).getValue() ).byteValue() );
		( (Select) component ).setValue( "1" );

		component = layout.getComponent( 25 );
		assertEquals( "Not Null Object Dropdown:", component.getCaption() );
		assertTrue( component instanceof Select );
		assertTrue( ( (Select) component ).isRequired() );
		assertEquals( 6, ( (Select) component ).getContainerDataSource().size() );
		assertEquals( "dropdown3", ( (Property) metawidget.getComponent( "notNullObjectDropdown" ) ).getValue() );
		( (Select) component ).setValue( "foo3" );

		assertEquals( "Nested Widgets:", layout.getComponent( 26 ).getCaption() );
		assertTrue( layout.getComponent( 26 ) instanceof VaadinMetawidget );
		FormLayout layoutNested = ( (VaadinMetawidget) layout.getComponent( 26 ) ).getContent();
		assertEquals( "Further Nested Widgets:", layoutNested.getComponent( 0 ).getCaption() );
		FormLayout layoutFurtherNested = ( (VaadinMetawidget) layoutNested.getComponent( 0 ) ).getContent();

		component = layoutFurtherNested.getComponent( 0 );
		assertEquals( "Further Nested Widgets:", component.getCaption() );
		assertTrue( component instanceof VaadinMetawidget );
		assertEquals( ( (FormLayout) ( (VaadinMetawidget) component ).getContent() ).getComponentCount(), 0 );

		component = layoutFurtherNested.getComponent( 1 );
		assertEquals( "Nested Textbox 1:", component.getCaption() );
		assertTrue( component instanceof TextField );
		assertEquals( "Nested Textbox 1", ( (Property) metawidget.getComponent( "nestedWidgets", "furtherNestedWidgets", "nestedTextbox1" ) ).getValue() );
		( (TextField) component ).setValue( "Nested Textbox 1.1 (further)" );

		component = layoutFurtherNested.getComponent( 2 );
		assertEquals( "Nested Textbox 2:", component.getCaption() );
		assertTrue( component instanceof TextField );
		assertEquals( "Nested Textbox 2", ( (Property) metawidget.getComponent( "nestedWidgets", "furtherNestedWidgets", "nestedTextbox2" ) ).getValue() );
		( (TextField) component ).setValue( "Nested Textbox 2.2 (further)" );

		component = layoutNested.getComponent( 1 );
		assertEquals( "Nested Textbox 1:", component.getCaption() );
		assertTrue( component instanceof TextField );
		assertEquals( "Nested Textbox 1", ( (Property) metawidget.getComponent( "nestedWidgets", "nestedTextbox1" ) ).getValue() );
		( (TextField) component ).setValue( "Nested Textbox 1.1" );

		component = layoutNested.getComponent( 2 );
		assertEquals( "Nested Textbox 2:", component.getCaption() );
		assertTrue( component instanceof TextField );
		assertEquals( "Nested Textbox 2", ( (Property) metawidget.getComponent( "nestedWidgets", "nestedTextbox2" ) ).getValue() );
		( (TextField) component ).setValue( "Nested Textbox 2.2" );

		component = layout.getComponent( 27 );
		assertEquals( "Read Only Nested Widgets:", layout.getComponent( 27 ).getCaption() );
		assertTrue( component instanceof VaadinMetawidget );
		layoutNested = ( (VaadinMetawidget) component ).getContent();

		component = layoutNested.getComponent( 0 );
		assertEquals( "Further Nested Widgets:", component.getCaption() );
		assertTrue( component instanceof VaadinMetawidget );
		assertEquals( ( (FormLayout) ( (VaadinMetawidget) component ).getContent() ).getComponentCount(), 0 );

		component = layoutNested.getComponent( 1 );
		assertEquals( "Nested Textbox 1:", component.getCaption() );
		assertTrue( component instanceof Label );
		assertEquals( "Nested Textbox 1", ( (Property) metawidget.getComponent( "readOnlyNestedWidgets", "nestedTextbox1" ) ).getValue() );

		component = layoutNested.getComponent( 2 );
		assertEquals( "Nested Textbox 2:", component.getCaption() );
		assertTrue( component instanceof Label );
		assertEquals( "Nested Textbox 2", ( (Property) metawidget.getComponent( "readOnlyNestedWidgets", "nestedTextbox2" ) ).getValue() );

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
		assertEquals( allWidgets.getDate(), ( (Property) metawidget.getComponent( "date" ) ).getValue() );
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
		assertEquals( "Read Only", ( (Property) metawidget.getComponent( "readOnly" ) ).getValue() );
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

		// Check saving

		Date now = new Date();

		( (PopupDateField) layout.getComponent( 30 ) ).setValue( now );
		metawidget.getWidgetProcessor( SimpleBindingProcessor.class ).save( metawidget );

		// Check read-only

		metawidget.setReadOnly( true );
		layout = (FormLayout) metawidget.getContent();

		component = layout.getComponent( 0 );
		assertEquals( "Textbox:", component.getCaption() );
		assertEquals( "Textbox1", ( (Label) component ).getValue() );
		component = layout.getComponent( 1 );
		assertEquals( "Limited Textbox:", component.getCaption() );
		assertEquals( "Limited Textbox1", ( (Label) component ).getValue() );
		component = layout.getComponent( 2 );
		assertEquals( "Textarea:", component.getCaption() );
		assertEquals( "Textarea1", ( (TextArea) component ).getValue() );
		assertTrue( ( (TextArea) component ).isReadOnly() );
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
		assertEquals( "No", ( (Label) component ).toString() );
		assertEquals( Boolean.FALSE, ( (Label) component ).getValue() );
		component = layout.getComponent( 22 );
		assertEquals( "Dropdown:", component.getCaption() );
		assertEquals( "foo1", ( (Label) component ).getValue() );
		component = layout.getComponent( 23 );
		assertEquals( "Dropdown With Labels:", component.getCaption() );
		assertEquals( "Bar #2", ( (Label) component ).toString() );
		assertEquals( "bar2", ( (Label) component ).getValue() );
		component = layout.getComponent( 24 );
		assertEquals( "Not Null Dropdown:", component.getCaption() );
		assertEquals( (byte) 1, ( (Label) component ).getValue() );
		component = layout.getComponent( 25 );
		assertEquals( "Not Null Object Dropdown:", component.getCaption() );
		assertEquals( "foo3", ( (Label) component ).getValue() );
		assertEquals( "Nested Widgets:", layout.getComponent( 26 ).getCaption() );
		assertTrue( layout.getComponent( 26 ) instanceof VaadinMetawidget );
		layoutNested = ( (VaadinMetawidget) layout.getComponent( 26 ) ).getContent();
		assertEquals( "Further Nested Widgets:", layoutNested.getComponent( 0 ).getCaption() );
		assertTrue( layoutNested.getComponent( 0 ) instanceof VaadinMetawidget );
		layoutFurtherNested = (FormLayout) ( (VaadinMetawidget) layoutNested.getComponent( 0 ) ).getContent();
		component = layoutFurtherNested.getComponent( 0 );
		assertEquals( "Further Nested Widgets:", component.getCaption() );
		assertTrue( component instanceof VaadinMetawidget );
		assertEquals( ( (FormLayout) ( (VaadinMetawidget) component ).getContent() ).getComponentCount(), 0 );
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
		layoutNested = ( (VaadinMetawidget) layout.getComponent( 27 ) ).getContent();
		component = layoutNested.getComponent( 0 );
		assertEquals( "Further Nested Widgets:", component.getCaption() );
		assertTrue( component instanceof VaadinMetawidget );
		assertEquals( ( (FormLayout) ( (VaadinMetawidget) component ).getContent() ).getComponentCount(), 0 );
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