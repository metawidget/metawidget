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

package org.metawidget.swt;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.metawidget.shared.allwidgets.model.AllWidgets;
import org.metawidget.swt.allwidgets.converter.DateToStringConverter;
import org.metawidget.swt.allwidgets.converter.NestedWidgetsToStringConverter;
import org.metawidget.swt.allwidgets.converter.StringToDateConverter;
import org.metawidget.swt.allwidgets.converter.StringToNestedWidgetsConverter;
import org.metawidget.swt.widgetprocessor.binding.databinding.DataBindingProcessor;
import org.metawidget.swt.widgetprocessor.binding.databinding.DataBindingProcessorConfig;
import org.metawidget.swt.widgetprocessor.binding.reflection.ReflectionBindingProcessor;

/**
 * @author Richard Kennard
 */

public class SwtAllWidgetsTest
	extends TestCase {

	//
	// Public statics
	//

	public static void main( String[] args ) {

		Display display = new Display();
		Shell shell = new Shell( display, SWT.DIALOG_TRIM | SWT.RESIZE );
		shell.setLayout( new FillLayout() );

		SwtMetawidget metawidget = new SwtMetawidget( shell, SWT.NONE );
		metawidget.setConfig( "org/metawidget/swt/allwidgets/metawidget.xml" );
		metawidget.setToInspect( new AllWidgets() );
		Stub stub = new Stub( metawidget, SWT.NONE );
		stub.setData( "name", "mystery" );

		shell.setVisible( true );
		shell.open();

		while ( !shell.isDisposed() ) {
			if ( !display.readAndDispatch() ) {
				display.sleep();
			}
		}

		display.dispose();
	}

	//
	// Private statics
	//

	private static final String	DATE_FORMAT	= "E MMM dd HH:mm:ss z yyyy";

	//
	// Public methods
	//

	public void testAllWidgets()
		throws Exception {

		TimeZone.setDefault( TimeZone.getTimeZone( "GMT" ) );

		// Model

		AllWidgets allWidgets = new AllWidgets();

		// App

		Shell shell = new Shell( SwtMetawidgetTests.TEST_DISPLAY, SWT.NONE );
		SwtMetawidget metawidget = new SwtMetawidget( shell, SWT.NONE );
		metawidget.setConfig( "org/metawidget/swt/allwidgets/metawidget.xml" );

		// Binding

		DataBindingProcessorConfig config = new DataBindingProcessorConfig();
		config.setConverters( new DateToStringConverter( DATE_FORMAT ), new StringToDateConverter( DATE_FORMAT ), new NestedWidgetsToStringConverter(), new StringToNestedWidgetsConverter() );
		metawidget.addWidgetProcessor( new DataBindingProcessor( config ) );
		metawidget.addWidgetProcessor( new ReflectionBindingProcessor() );

		metawidget.setToInspect( allWidgets );

		// Stub

		Stub stub = new Stub( metawidget, SWT.NONE );
		stub.setData( "name", "mystery" );

		// Check what created, and edit it

		assertEquals( "Textbox*:", ( (Label) metawidget.getChildren()[0] ).getText() );
		assertTrue( metawidget.getChildren()[1] instanceof Text );
		assertEquals( "Textbox", metawidget.getValue( "textbox" ) );
		( (Text) metawidget.getChildren()[1] ).setText( "Textbox1" );

		assertEquals( "Limited textbox:", ( (Label) metawidget.getChildren()[2] ).getText() );
		assertTrue( metawidget.getChildren()[3] instanceof Text );
		assertEquals( "Limited Textbox", metawidget.getValue( "limitedTextbox" ) );
		( (Text) metawidget.getChildren()[3] ).setText( "Limited Textbox1" );

		assertEquals( "Textarea:", ( (Label) metawidget.getChildren()[4] ).getText() );
		assertTrue( metawidget.getChildren()[5] instanceof Text );
		assertTrue( ( (GridData) metawidget.getChildren()[5].getLayoutData() ).grabExcessVerticalSpace );
		assertTrue( ( metawidget.getChildren()[5].getStyle() & SWT.MULTI ) == SWT.MULTI );
		assertTrue( ( metawidget.getChildren()[5].getStyle() & SWT.BORDER ) == SWT.BORDER );
		assertTrue( ( metawidget.getChildren()[5].getStyle() & SWT.V_SCROLL ) == SWT.V_SCROLL );
		assertTrue( ( metawidget.getChildren()[5].getStyle() & SWT.WRAP ) == SWT.WRAP );
		assertTrue( ( metawidget.getChildren()[5].getStyle() & SWT.H_SCROLL ) == SWT.NONE );
		assertEquals( "Textarea", metawidget.getValue( "textarea" ) );
		( (Text) metawidget.getChildren()[5] ).setText( "Textarea1" );

		assertEquals( "Password:", ( (Label) metawidget.getChildren()[6] ).getText() );
		assertTrue( metawidget.getChildren()[7] instanceof Text );
		assertTrue( ( metawidget.getChildren()[7].getStyle() & SWT.PASSWORD ) == SWT.PASSWORD );
		assertTrue( ( metawidget.getChildren()[7].getStyle() & SWT.BORDER ) == SWT.BORDER );
		assertEquals( "Password", metawidget.getValue( "password" ) );
		( (Text) metawidget.getChildren()[7] ).setText( "Password1" );

		// Primitives

		assertEquals( "Byte:", ( (Label) metawidget.getChildren()[8] ).getText() );
		assertTrue( Byte.MAX_VALUE == (Integer) metawidget.getValue( "byte" ) );
		Spinner spinner = (Spinner) metawidget.getChildren()[9];

		// (negative Spinner values don't always work -
		// https://bugs.eclipse.org/bugs/show_bug.cgi?id=91317)

		assertTrue( -5 == spinner.getMinimum() || 0 == spinner.getMinimum() );
		assertTrue( Byte.MAX_VALUE == spinner.getMaximum() );
		spinner.setSelection( spinner.getSelection() - spinner.getIncrement() );

		assertEquals( "Byte object:", ( (Label) metawidget.getChildren()[10] ).getText() );
		assertTrue( metawidget.getChildren()[11] instanceof Text );
		assertEquals( String.valueOf( Byte.MIN_VALUE ), metawidget.getValue( "byteObject" ) );
		( (Text) metawidget.getChildren()[11] ).setText( String.valueOf( Byte.MIN_VALUE + 1 ) );

		assertEquals( "Short:", ( (Label) metawidget.getChildren()[12] ).getText() );
		assertTrue( Short.MAX_VALUE == (Integer) metawidget.getValue( "short" ) );
		spinner = (Spinner) metawidget.getChildren()[13];
		assertTrue( -6 == spinner.getMinimum() || 0 == spinner.getMinimum() );
		assertTrue( Short.MAX_VALUE == spinner.getMaximum() );
		spinner.setSelection( spinner.getSelection() - spinner.getIncrement() );

		assertEquals( "Short object:", ( (Label) metawidget.getChildren()[14] ).getText() );
		assertTrue( metawidget.getChildren()[15] instanceof Text );
		assertEquals( String.valueOf( Short.MIN_VALUE ), ( (String) metawidget.getValue( "shortObject" ) ).replaceAll( ",", "" ) );
		( (Text) metawidget.getChildren()[15] ).setText( String.valueOf( Short.MIN_VALUE + 1 ) );

		assertEquals( "Int:", ( (Label) metawidget.getChildren()[16] ).getText() );
		assertTrue( Integer.MAX_VALUE == (Integer) metawidget.getValue( "int" ) );
		spinner = (Spinner) metawidget.getChildren()[17];
		assertTrue( Integer.MIN_VALUE == spinner.getMinimum() || 0 == spinner.getMinimum() );
		assertTrue( Integer.MAX_VALUE == spinner.getMaximum() );
		spinner.setSelection( spinner.getSelection() - spinner.getIncrement() );

		assertEquals( "Integer object:", ( (Label) metawidget.getChildren()[18] ).getText() );
		assertTrue( metawidget.getChildren()[19] instanceof Text );
		assertEquals( String.valueOf( Integer.MIN_VALUE ), ( (String) metawidget.getValue( "integerObject" ) ).replaceAll( ",", "" ) );
		( (Text) metawidget.getChildren()[19] ).setText( String.valueOf( Integer.MIN_VALUE + 1 ) );

		assertEquals( "Ranged int:", ( (Label) metawidget.getChildren()[20] ).getText() );
		assertTrue( metawidget.getChildren()[21] instanceof Scale );
		assertTrue( 1 == ( (Scale) metawidget.getChildren()[21] ).getMinimum() );
		assertTrue( 100 == ( (Scale) metawidget.getChildren()[21] ).getMaximum() );
		assertTrue( 32 == (Integer) metawidget.getValue( "rangedInt" ) );
		( (Scale) metawidget.getChildren()[21] ).setSelection( 33 );

		assertEquals( "Ranged integer:", ( (Label) metawidget.getChildren()[22] ).getText() );
		assertTrue( metawidget.getChildren()[23] instanceof Text );
		assertEquals( "33", metawidget.getValue( "rangedInteger" ) );
		( (Text) metawidget.getChildren()[23] ).setText( String.valueOf( 34 ) );

		assertEquals( "Long:", ( (Label) metawidget.getChildren()[24] ).getText() );
		assertTrue( metawidget.getChildren()[25] instanceof Text );
		assertEquals( "42", metawidget.getValue( "long" ) );
		( (Text) metawidget.getChildren()[25] ).setText( "43" );

		assertTrue( metawidget.getChildren()[26] instanceof Text );
		assertTrue( 2 == ( (GridData) metawidget.getChildren()[26].getLayoutData() ).horizontalSpan );
		assertEquals( "43", metawidget.getValue( "longObject" ) );
		( (Text) metawidget.getChildren()[26] ).setText( "44" );

		assertEquals( "Float:", ( (Label) metawidget.getChildren()[27] ).getText() );
		assertTrue( metawidget.getChildren()[28] instanceof Text );
		assertEquals( "4.2", metawidget.getValue( "float" ) );
		( (Text) metawidget.getChildren()[28] ).setText( "4.3" );

		assertEquals( "nullInBundle:", ( (Label) metawidget.getChildren()[29] ).getText() );
		assertTrue( metawidget.getChildren()[30] instanceof Text );
		assertEquals( "4.3", metawidget.getValue( "floatObject" ) );
		( (Text) metawidget.getChildren()[30] ).setText( "5.4" );

		assertEquals( "Double:", ( (Label) metawidget.getChildren()[31] ).getText() );
		assertTrue( metawidget.getChildren()[32] instanceof Text );
		assertEquals( "42.2", metawidget.getValue( "double" ) );
		( (Text) metawidget.getChildren()[32] ).setText( "42.3" );

		assertTrue( metawidget.getChildren()[33] instanceof Text );
		assertTrue( 2 == ( (GridData) metawidget.getChildren()[33].getLayoutData() ).horizontalSpan );
		assertEquals( "43.3", metawidget.getValue( "doubleObject" ) );
		( (Text) metawidget.getChildren()[33] ).setText( "54.4" );

		assertEquals( "Char:", ( (Label) metawidget.getChildren()[34] ).getText() );
		assertTrue( metawidget.getChildren()[35] instanceof Text );
		assertEquals( "A", metawidget.getValue( "char" ) );
		( (Text) metawidget.getChildren()[35] ).setText( "Z" );

		assertEquals( "Boolean:", ( (Label) metawidget.getChildren()[36] ).getText() );
		assertTrue( metawidget.getChildren()[37] instanceof Button );
		assertTrue( ( metawidget.getChildren()[37].getStyle() & SWT.CHECK ) == SWT.CHECK );
		assertTrue( false == (Boolean) metawidget.getValue( "boolean" ) );
		( (Button) metawidget.getChildren()[37] ).setSelection( true );

		assertEquals( "Boolean object:", ( (Label) metawidget.getChildren()[38] ).getText() );
		assertTrue( metawidget.getChildren()[39] instanceof Combo );
		assertTrue( ( metawidget.getChildren()[39].getStyle() & SWT.READ_ONLY ) == SWT.READ_ONLY );
		assertTrue( 3 == ( (Combo) metawidget.getChildren()[39] ).getItemCount() );
		assertEquals( "true", metawidget.getValue( "booleanObject" ) );
		( (Combo) metawidget.getChildren()[39] ).setText( "false" );

		assertEquals( "Dropdown:", ( (Label) metawidget.getChildren()[40] ).getText() );
		assertTrue( metawidget.getChildren()[41] instanceof Combo );
		assertTrue( ( metawidget.getChildren()[41].getStyle() & SWT.READ_ONLY ) == SWT.READ_ONLY );
		assertTrue( 4 == ( (Combo) metawidget.getChildren()[41] ).getItemCount() );
		assertEquals( "dropdown1", metawidget.getValue( "dropdown" ) );
		( (Combo) metawidget.getChildren()[41] ).setText( "foo1" );

		assertEquals( "Dropdown with labels:", ( (Label) metawidget.getChildren()[42] ).getText() );
		assertTrue( metawidget.getChildren()[43] instanceof Combo );
		assertTrue( 5 == ( (Combo) metawidget.getChildren()[43] ).getItemCount() );
		assertEquals( "dropdown2", metawidget.getValue( "dropdownWithLabels" ) );
		( (Combo) metawidget.getChildren()[43] ).setText( "bar2" );

		assertEquals( "Not null dropdown:", ( (Label) metawidget.getChildren()[44] ).getText() );
		assertTrue( metawidget.getChildren()[45] instanceof Combo );
		assertTrue( 3 == ( (Combo) metawidget.getChildren()[45] ).getItemCount() );
		assertEquals( "0", metawidget.getValue( "notNullDropdown" ) );
		( (Combo) metawidget.getChildren()[45] ).setText( "1" );

		assertEquals( "Not null object dropdown*:", ( (Label) metawidget.getChildren()[46] ).getText() );
		assertTrue( metawidget.getChildren()[47] instanceof Combo );
		assertTrue( 6 == ( (Combo) metawidget.getChildren()[47] ).getItemCount() );
		assertEquals( "dropdown3", metawidget.getValue( "notNullObjectDropdown" ) );
		( (Combo) metawidget.getChildren()[47] ).setText( "foo3" );

		assertEquals( "Nested widgets:", ( (Label) metawidget.getChildren()[48] ).getText() );
		assertTrue( 1 == ( (GridData) metawidget.getChildren()[48].getLayoutData() ).horizontalSpan );
		assertTrue( metawidget.getChildren()[49] instanceof SwtMetawidget );
		assertTrue( 1 == ( (GridData) metawidget.getChildren()[49].getLayoutData() ).horizontalSpan );

		SwtMetawidget metawidgetNested = (SwtMetawidget) metawidget.getChildren()[49];
		assertEquals( "Further nested widgets:", ( (Label) metawidgetNested.getChildren()[0] ).getText() );

		SwtMetawidget metawidgetFurtherNested = (SwtMetawidget) metawidgetNested.getChildren()[1];
		assertEquals( "Further nested widgets:", ( (Label) metawidgetFurtherNested.getChildren()[0] ).getText() );
		assertTrue( metawidgetFurtherNested.getChildren()[1] instanceof SwtMetawidget );
		assertTrue( ( (SwtMetawidget) metawidgetFurtherNested.getChildren()[1] ).getChildren().length == 0 );

		assertEquals( "Nested textbox 1:", ( (Label) metawidgetFurtherNested.getChildren()[2] ).getText() );
		assertTrue( metawidgetFurtherNested.getChildren()[3] instanceof Text );
		assertEquals( "Nested Textbox 1", metawidget.getValue( "nestedWidgets", "furtherNestedWidgets", "nestedTextbox1" ) );
		( (Text) metawidgetFurtherNested.getChildren()[3] ).setText( "Nested Textbox 1.1 (further)" );

		assertEquals( "Nested textbox 2:", ( (Label) metawidgetFurtherNested.getChildren()[4] ).getText() );
		assertTrue( metawidgetFurtherNested.getChildren()[5] instanceof Text );
		assertEquals( "Nested Textbox 2", metawidget.getValue( "nestedWidgets", "furtherNestedWidgets", "nestedTextbox2" ) );
		( (Text) metawidgetFurtherNested.getChildren()[5] ).setText( "Nested Textbox 2.2 (further)" );

		assertEquals( "Nested textbox 1:", ( (Label) metawidgetNested.getChildren()[2] ).getText() );
		assertTrue( metawidgetNested.getChildren()[3] instanceof Text );
		assertEquals( "Nested Textbox 1", metawidget.getValue( "nestedWidgets", "nestedTextbox1" ) );
		( (Text) metawidgetNested.getChildren()[3] ).setText( "Nested Textbox 1.1" );

		assertEquals( "Nested textbox 2:", ( (Label) metawidgetNested.getChildren()[4] ).getText() );
		assertTrue( metawidgetNested.getChildren()[5] instanceof Text );
		assertEquals( "Nested Textbox 2", metawidget.getValue( "nestedWidgets", "nestedTextbox2" ) );
		( (Text) metawidgetNested.getChildren()[5] ).setText( "Nested Textbox 2.2" );

		assertEquals( "Read only nested widgets:", ( (Label) metawidget.getChildren()[50] ).getText() );
		assertTrue( metawidget.getChildren()[51] instanceof SwtMetawidget );

		metawidgetNested = (SwtMetawidget) metawidget.getChildren()[51];
		assertEquals( "Nested textbox 1:", ( (Label) metawidgetNested.getChildren()[2] ).getText() );
		assertTrue( metawidgetNested.getChildren()[3] instanceof Label );
		assertEquals( "Nested Textbox 1", metawidget.getValue( "readOnlyNestedWidgets", "nestedTextbox1" ) );

		assertEquals( "Nested textbox 2:", ( (Label) metawidgetNested.getChildren()[4] ).getText() );
		assertTrue( metawidgetNested.getChildren()[5] instanceof Label );
		assertEquals( "Nested Textbox 2", metawidget.getValue( "readOnlyNestedWidgets", "nestedTextbox2" ) );

		assertEquals( "Nested widgets dont expand:", ( (Label) metawidget.getChildren()[52] ).getText() );
		assertEquals( "Nested Textbox 1, Nested Textbox 2", ( (Text) metawidget.getChildren()[53] ).getText() );
		( (Text) metawidget.getChildren()[53] ).setText( "Nested Textbox 1.01, Nested Textbox 2.02" );

		assertEquals( "Read only nested widgets dont expand:", ( (Label) metawidget.getChildren()[54] ).getText() );
		assertEquals( "Nested Textbox 1, Nested Textbox 2", ( (Label) metawidget.getChildren()[55] ).getText() );

		assertEquals( "Date:", ( (Label) metawidget.getChildren()[56] ).getText() );
		assertTrue( metawidget.getChildren()[57] instanceof Text );

		DateFormat dateFormat = new SimpleDateFormat( DATE_FORMAT );
		assertEquals( dateFormat.format( allWidgets.getDate() ), metawidget.getValue( "date" ) );
		( (Text) metawidget.getChildren()[57] ).setText( "bad date" );

		assertTrue( metawidget.getChildren()[58] instanceof Stub );
		assertEquals( "hidden", metawidget.getChildren()[58].getData( "name" ) );
		assertTrue( ( (GridData) metawidget.getChildren()[58].getLayoutData() ).exclude );

		Composite separatorComposite = (Composite) metawidget.getChildren()[59];
		assertTrue( ( (org.eclipse.swt.layout.GridLayout) separatorComposite.getLayout() ).marginWidth == 0 );
		assertEquals( "Section Break", ( (Label) separatorComposite.getChildren()[0] ).getText() );
		assertTrue( separatorComposite.getChildren()[1] instanceof Label );
		assertTrue( ( separatorComposite.getChildren()[1].getStyle() & SWT.SEPARATOR ) == SWT.SEPARATOR );
		assertTrue( SWT.FILL == ( (GridData) separatorComposite.getChildren()[1].getLayoutData() ).horizontalAlignment );
		assertTrue( ( (GridData) separatorComposite.getChildren()[1].getLayoutData() ).grabExcessHorizontalSpace );

		assertEquals( "Read only:", ( (Label) metawidget.getChildren()[60] ).getText() );
		assertTrue( metawidget.getChildren()[61] instanceof Label );
		assertEquals( "Read Only", metawidget.getValue( "readOnly" ) );

		assertTrue( metawidget.getChildren()[62] instanceof Stub );
		assertEquals( "mystery", metawidget.getChildren()[62].getData( "name" ) );
		assertTrue( ( (GridData) metawidget.getChildren()[62].getLayoutData() ).exclude );

		assertTrue( metawidget.getChildren()[63] instanceof Stub );
		assertEquals( "collection", metawidget.getChildren()[63].getData( "name" ) );
		assertTrue( ( (GridData) metawidget.getChildren()[63].getLayoutData() ).exclude );

		assertTrue( metawidget.getChildren()[64] instanceof Button );
		Button button = ( (Button) metawidget.getChildren()[64] );
		assertEquals( "Do action", button.getText() );
		try {
			button.notifyListeners( SWT.Selection, null );
			assertTrue( false );
		} catch ( Exception e ) {
			assertEquals( "doAction called", e.getCause().getCause().getMessage() );
		}

		assertTrue( 65 == metawidget.getChildren().length );

		// Check MetawidgetException

		try {
			metawidget.getWidgetProcessor( DataBindingProcessor.class ).save( metawidget );
			assertTrue( false );
		} catch ( Exception e ) {
			assertEquals( "Could not parse 'bad date'", e.getCause().getMessage() );
		}

		// Check saving

		String now = dateFormat.format( new GregorianCalendar().getTime() );
		( (Text) metawidget.getChildren()[57] ).setText( now );
		metawidget.getWidgetProcessor( DataBindingProcessor.class ).save( metawidget );

		// Check read-only

		metawidget.setReadOnly( true );

		assertEquals( "Textbox:", ( (Label) metawidget.getChildren()[0] ).getText() );
		assertEquals( "Textbox1", ( (Label) metawidget.getChildren()[1] ).getText() );
		assertEquals( "Limited textbox:", ( (Label) metawidget.getChildren()[2] ).getText() );
		assertEquals( "Limited Textbox1", ( (Label) metawidget.getChildren()[3] ).getText() );
		assertEquals( "Textarea:", ( (Label) metawidget.getChildren()[4] ).getText() );
		assertEquals( "Textarea1", ( (Text) metawidget.getChildren()[5] ).getText() );
		assertTrue( ( metawidget.getChildren()[5].getStyle() & SWT.READ_ONLY ) == SWT.READ_ONLY );
		assertTrue( ( metawidget.getChildren()[5].getStyle() & SWT.MULTI ) == SWT.MULTI );
		assertTrue( ( metawidget.getChildren()[5].getStyle() & SWT.BORDER ) == SWT.BORDER );
		assertTrue( ( metawidget.getChildren()[5].getStyle() & SWT.V_SCROLL ) == SWT.V_SCROLL );
		assertTrue( ( metawidget.getChildren()[5].getStyle() & SWT.WRAP ) == SWT.WRAP );
		assertEquals( "Password:", ( (Label) metawidget.getChildren()[6] ).getText() );
		assertTrue( metawidget.getChildren()[7] instanceof Composite );
		assertEquals( "Byte:", ( (Label) metawidget.getChildren()[8] ).getText() );
		assertEquals( "126", ( (Label) metawidget.getChildren()[9] ).getText() );
		assertEquals( "Byte object:", ( (Label) metawidget.getChildren()[10] ).getText() );
		assertEquals( "-127", ( (Label) metawidget.getChildren()[11] ).getText() );
		assertEquals( "Short:", ( (Label) metawidget.getChildren()[12] ).getText() );
		assertEquals( "32,766", ( (Label) metawidget.getChildren()[13] ).getText() );
		assertEquals( "Short object:", ( (Label) metawidget.getChildren()[14] ).getText() );
		assertEquals( "-32,767", ( (Label) metawidget.getChildren()[15] ).getText() );
		assertEquals( "Int:", ( (Label) metawidget.getChildren()[16] ).getText() );
		assertEquals( "2,147,483,646", ( (Label) metawidget.getChildren()[17] ).getText() );
		assertEquals( "Integer object:", ( (Label) metawidget.getChildren()[18] ).getText() );
		assertEquals( "-2,147,483,647", ( (Label) metawidget.getChildren()[19] ).getText() );
		assertEquals( "Ranged int:", ( (Label) metawidget.getChildren()[20] ).getText() );
		assertEquals( "33", ( (Label) metawidget.getChildren()[21] ).getText() );
		assertEquals( "Ranged integer:", ( (Label) metawidget.getChildren()[22] ).getText() );
		assertEquals( "34", ( (Label) metawidget.getChildren()[23] ).getText() );
		assertEquals( "Long:", ( (Label) metawidget.getChildren()[24] ).getText() );
		assertEquals( "43", ( (Label) metawidget.getChildren()[25] ).getText() );
		assertEquals( "44", ( (Label) metawidget.getChildren()[26] ).getText() );
		assertEquals( "Float:", ( (Label) metawidget.getChildren()[27] ).getText() );
		assertTrue( ( (Label) metawidget.getChildren()[28] ).getText().startsWith( "4.3" ) || ( (Label) metawidget.getChildren()[28] ).getText().startsWith( "4.299" ) );
		assertEquals( "nullInBundle:", ( (Label) metawidget.getChildren()[29] ).getText() );
		assertEquals( "5.4", ( (Label) metawidget.getChildren()[30] ).getText() );
		assertEquals( "Double:", ( (Label) metawidget.getChildren()[31] ).getText() );
		assertTrue( ( (Label) metawidget.getChildren()[32] ).getText().startsWith( "42.3" ) || ( (Label) metawidget.getChildren()[32] ).getText().startsWith( "42.299" ) );
		assertEquals( "54.4", ( (Label) metawidget.getChildren()[33] ).getText() );
		assertEquals( "Char:", ( (Label) metawidget.getChildren()[34] ).getText() );
		assertEquals( "Z", ( (Label) metawidget.getChildren()[35] ).getText() );
		assertEquals( "Boolean:", ( (Label) metawidget.getChildren()[36] ).getText() );
		assertEquals( "true", ( (Label) metawidget.getChildren()[37] ).getText() );
		assertEquals( "Boolean object:", ( (Label) metawidget.getChildren()[38] ).getText() );
		assertEquals( "false", ( (Label) metawidget.getChildren()[39] ).getText() );
		assertEquals( "Dropdown:", ( (Label) metawidget.getChildren()[40] ).getText() );
		assertEquals( "foo1", ( (Label) metawidget.getChildren()[41] ).getText() );
		assertEquals( "Dropdown with labels:", ( (Label) metawidget.getChildren()[42] ).getText() );
		assertEquals( "bar2", ( (Label) metawidget.getChildren()[43] ).getText() );
		assertEquals( "Not null dropdown:", ( (Label) metawidget.getChildren()[44] ).getText() );
		assertEquals( "1", ( (Label) metawidget.getChildren()[45] ).getText() );
		assertEquals( "Not null object dropdown:", ( (Label) metawidget.getChildren()[46] ).getText() );
		assertEquals( "foo3", ( (Label) metawidget.getChildren()[47] ).getText() );
		assertEquals( "Nested widgets:", ( (Label) metawidget.getChildren()[48] ).getText() );
		assertEquals( "Further nested widgets:", ( (Label) ( (SwtMetawidget) metawidget.getChildren()[49] ).getChildren()[0] ).getText() );
		assertEquals( "Further nested widgets:", ( (Label) ( (SwtMetawidget) ( (SwtMetawidget) metawidget.getChildren()[49] ).getChildren()[1] ).getChildren()[0] ).getText() );
		assertEquals( "Nested textbox 1:", ( (Label) ( (SwtMetawidget) ( (SwtMetawidget) metawidget.getChildren()[49] ).getChildren()[1] ).getChildren()[2] ).getText() );
		assertEquals( "Nested Textbox 1.1 (further)", ( (Label) ( (SwtMetawidget) ( (SwtMetawidget) metawidget.getChildren()[49] ).getChildren()[1] ).getChildren()[3] ).getText() );
		assertEquals( "Nested textbox 2:", ( (Label) ( (SwtMetawidget) ( (SwtMetawidget) metawidget.getChildren()[49] ).getChildren()[1] ).getChildren()[4] ).getText() );
		assertEquals( "Nested Textbox 2.2 (further)", ( (Label) ( (SwtMetawidget) ( (SwtMetawidget) metawidget.getChildren()[49] ).getChildren()[1] ).getChildren()[5] ).getText() );
		assertEquals( "Nested textbox 1:", ( (Label) ( (SwtMetawidget) metawidget.getChildren()[49] ).getChildren()[2] ).getText() );
		assertEquals( "Nested Textbox 1.1", ( (Label) ( (SwtMetawidget) metawidget.getChildren()[49] ).getChildren()[3] ).getText() );
		assertEquals( "Nested textbox 2:", ( (Label) ( (SwtMetawidget) metawidget.getChildren()[49] ).getChildren()[4] ).getText() );
		assertEquals( "Nested Textbox 2.2", ( (Label) ( (SwtMetawidget) metawidget.getChildren()[49] ).getChildren()[5] ).getText() );
		assertEquals( "Read only nested widgets:", ( (Label) metawidget.getChildren()[50] ).getText() );
		assertEquals( "Further nested widgets:", ( (Label) ( (SwtMetawidget) metawidget.getChildren()[51] ).getChildren()[0] ).getText() );
		assertEquals( "Nested textbox 1:", ( (Label) ( (SwtMetawidget) metawidget.getChildren()[51] ).getChildren()[2] ).getText() );
		assertEquals( "Nested Textbox 1", ( (Label) ( (SwtMetawidget) metawidget.getChildren()[51] ).getChildren()[3] ).getText() );
		assertEquals( "Nested textbox 2:", ( (Label) ( (SwtMetawidget) metawidget.getChildren()[51] ).getChildren()[4] ).getText() );
		assertEquals( "Nested Textbox 2", ( (Label) ( (SwtMetawidget) metawidget.getChildren()[51] ).getChildren()[5] ).getText() );
		assertEquals( "Nested widgets dont expand:", ( (Label) metawidget.getChildren()[52] ).getText() );
		assertEquals( "Nested Textbox 1.01, Nested Textbox 2.02", ( (Label) metawidget.getChildren()[53] ).getText() );
		assertEquals( "Read only nested widgets dont expand:", ( (Label) metawidget.getChildren()[54] ).getText() );
		assertEquals( "Nested Textbox 1, Nested Textbox 2", ( (Label) metawidget.getChildren()[55] ).getText() );
		assertEquals( "Date:", ( (Label) metawidget.getChildren()[56] ).getText() );
		assertEquals( now, ( (Label) metawidget.getChildren()[57] ).getText() );
		assertEquals( "Section Break", ( (Label) ( (Composite) metawidget.getChildren()[59] ).getChildren()[0] ).getText() );
		assertEquals( "Read only:", ( (Label) metawidget.getChildren()[60] ).getText() );
		assertEquals( "Read Only", ( (Label) metawidget.getChildren()[61] ).getText() );

		assertTrue( metawidget.getChildren().length == 65 );

		// All done

		shell.dispose();
	}
}
