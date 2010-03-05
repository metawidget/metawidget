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
	extends TestCase
{
	//
	// Public statics
	//

	public static void main( String[] args )
	{
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

		while ( !shell.isDisposed() )
		{
			if ( !display.readAndDispatch() )
				display.sleep();
		}

		display.dispose();
	}

	//
	// Private statics
	//

	private final static String	DATE_FORMAT	= "E MMM dd HH:mm:ss z yyyy";

	//
	// Public methods
	//

	public void testAllWidgets()
		throws Exception
	{
		TimeZone.setDefault( TimeZone.getTimeZone( "GMT" ) );

		// Model

		AllWidgets allWidgets = new AllWidgets();

		// App

		SwtMetawidget metawidget = new SwtMetawidget( SwtMetawidgetTests.TEST_SHELL, SWT.NONE );
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

		assertTrue( "Textbox*:".equals( ( (Label) metawidget.getChildren()[0] ).getText() ) );
		assertTrue( metawidget.getChildren()[1] instanceof Text );
		assertTrue( "Textbox".equals( metawidget.getValue( "textbox" ) ) );
		( (Text) metawidget.getChildren()[1] ).setText( "Textbox1" );

		assertTrue( "Limited textbox:".equals( ( (Label) metawidget.getChildren()[2] ).getText() ) );
		assertTrue( metawidget.getChildren()[3] instanceof Text );
		assertTrue( "Limited Textbox".equals( metawidget.getValue( "limitedTextbox" ) ) );
		( (Text) metawidget.getChildren()[3] ).setText( "Limited Textbox1" );

		assertTrue( "Textarea:".equals( ( (Label) metawidget.getChildren()[4] ).getText() ) );
		assertTrue( metawidget.getChildren()[5] instanceof Text );
		assertTrue( ( (GridData) metawidget.getChildren()[5].getLayoutData() ).grabExcessVerticalSpace );
		assertTrue( ( metawidget.getChildren()[5].getStyle() & SWT.MULTI ) == SWT.MULTI );
		assertTrue( ( metawidget.getChildren()[5].getStyle() & SWT.BORDER ) == SWT.BORDER );
		assertTrue( ( metawidget.getChildren()[5].getStyle() & SWT.V_SCROLL ) == SWT.V_SCROLL );
		assertTrue( ( metawidget.getChildren()[5].getStyle() & SWT.WRAP ) == SWT.WRAP );
		assertTrue( ( metawidget.getChildren()[5].getStyle() & SWT.H_SCROLL ) == SWT.NONE );
		assertTrue( "Textarea".equals( metawidget.getValue( "textarea" ) ) );
		( (Text) metawidget.getChildren()[5] ).setText( "Textarea1" );

		assertTrue( "Password:".equals( ( (Label) metawidget.getChildren()[6] ).getText() ) );
		assertTrue( metawidget.getChildren()[7] instanceof Text );
		assertTrue( ( metawidget.getChildren()[7].getStyle() & SWT.PASSWORD ) == SWT.PASSWORD );
		assertTrue( ( metawidget.getChildren()[7].getStyle() & SWT.BORDER ) == SWT.BORDER );
		assertTrue( "Password".equals( metawidget.getValue( "password" ) ) );
		( (Text) metawidget.getChildren()[7] ).setText( "Password1" );

		// Primitives

		assertTrue( "Byte:".equals( ( (Label) metawidget.getChildren()[8] ).getText() ) );
		assertTrue( Byte.MAX_VALUE == (Integer) metawidget.getValue( "byte" ) );
		Spinner spinner = (Spinner) metawidget.getChildren()[9];

		// (negative Spinner values don't always work -
		// https://bugs.eclipse.org/bugs/show_bug.cgi?id=91317)

		assertTrue( -5 == spinner.getMinimum() || 0 == spinner.getMinimum() );
		assertTrue( Byte.MAX_VALUE == spinner.getMaximum() );
		spinner.setSelection( spinner.getSelection() - spinner.getIncrement() );

		assertTrue( "Byte object:".equals( ( (Label) metawidget.getChildren()[10] ).getText() ) );
		assertTrue( metawidget.getChildren()[11] instanceof Text );
		assertTrue( String.valueOf( Byte.MIN_VALUE ).equals( metawidget.getValue( "byteObject" ) ) );
		( (Text) metawidget.getChildren()[11] ).setText( String.valueOf( Byte.MIN_VALUE + 1 ) );

		assertTrue( "Short:".equals( ( (Label) metawidget.getChildren()[12] ).getText() ) );
		assertTrue( Short.MAX_VALUE == (Integer) metawidget.getValue( "short" ) );
		spinner = (Spinner) metawidget.getChildren()[13];
		assertTrue( -6 == spinner.getMinimum() || 0 == spinner.getMinimum() );
		assertTrue( Short.MAX_VALUE == spinner.getMaximum() );
		spinner.setSelection( spinner.getSelection() - spinner.getIncrement() );

		assertTrue( "Short object:".equals( ( (Label) metawidget.getChildren()[14] ).getText() ) );
		assertTrue( metawidget.getChildren()[15] instanceof Text );
		assertTrue( String.valueOf( Short.MIN_VALUE ).equals( ( (String) metawidget.getValue( "shortObject" ) ).replaceAll( ",", "" ) ) );
		( (Text) metawidget.getChildren()[15] ).setText( String.valueOf( Short.MIN_VALUE + 1 ) );

		assertTrue( "Int:".equals( ( (Label) metawidget.getChildren()[16] ).getText() ) );
		assertTrue( Integer.MAX_VALUE == (Integer) metawidget.getValue( "int" ) );
		spinner = (Spinner) metawidget.getChildren()[17];
		assertTrue( Integer.MIN_VALUE == spinner.getMinimum() || 0 == spinner.getMinimum() );
		assertTrue( Integer.MAX_VALUE == spinner.getMaximum() );
		spinner.setSelection( spinner.getSelection() - spinner.getIncrement() );

		assertTrue( "Integer object:".equals( ( (Label) metawidget.getChildren()[18] ).getText() ) );
		assertTrue( metawidget.getChildren()[19] instanceof Text );
		assertTrue( String.valueOf( Integer.MIN_VALUE ).equals( ( (String) metawidget.getValue( "integerObject" ) ).replaceAll( ",", "" ) ) );
		( (Text) metawidget.getChildren()[19] ).setText( String.valueOf( Integer.MIN_VALUE + 1 ) );

		assertTrue( "Ranged int:".equals( ( (Label) metawidget.getChildren()[20] ).getText() ) );
		assertTrue( metawidget.getChildren()[21] instanceof Scale );
		assertTrue( 1 == ( (Scale) metawidget.getChildren()[21] ).getMinimum() );
		assertTrue( 100 == ( (Scale) metawidget.getChildren()[21] ).getMaximum() );
		assertTrue( 32 == (Integer) metawidget.getValue( "rangedInt" ) );
		( (Scale) metawidget.getChildren()[21] ).setSelection( 33 );

		assertTrue( "Ranged integer:".equals( ( (Label) metawidget.getChildren()[22] ).getText() ) );
		assertTrue( metawidget.getChildren()[23] instanceof Text );
		assertTrue( "33".equals( metawidget.getValue( "rangedInteger" ) ) );
		( (Text) metawidget.getChildren()[23] ).setText( String.valueOf( 34 ) );

		assertTrue( "Long:".equals( ( (Label) metawidget.getChildren()[24] ).getText() ) );
		assertTrue( metawidget.getChildren()[25] instanceof Text );
		assertTrue( "42".equals( metawidget.getValue( "long" ) ) );
		( (Text) metawidget.getChildren()[25] ).setText( "43" );

		assertTrue( metawidget.getChildren()[26] instanceof Text );
		assertTrue( 2 == ( (GridData) metawidget.getChildren()[26].getLayoutData() ).horizontalSpan );
		assertTrue( "43".equals( metawidget.getValue( "longObject" ) ) );
		( (Text) metawidget.getChildren()[26] ).setText( "44" );

		assertTrue( "Float:".equals( ( (Label) metawidget.getChildren()[27] ).getText() ) );
		assertTrue( metawidget.getChildren()[28] instanceof Text );
		assertTrue( "4.2".equals( metawidget.getValue( "float" ) ) );
		( (Text) metawidget.getChildren()[28] ).setText( "4.3" );

		assertTrue( "nullInBundle:".equals( ( (Label) metawidget.getChildren()[29] ).getText() ) );
		assertTrue( metawidget.getChildren()[30] instanceof Text );
		assertTrue( "4.3".equals( metawidget.getValue( "floatObject" ) ) );
		( (Text) metawidget.getChildren()[30] ).setText( "5.4" );

		assertTrue( "Double:".equals( ( (Label) metawidget.getChildren()[31] ).getText() ) );
		assertTrue( metawidget.getChildren()[32] instanceof Text );
		assertTrue( "42.2".equals( metawidget.getValue( "double" ) ) );
		( (Text) metawidget.getChildren()[32] ).setText( "42.3" );

		assertTrue( metawidget.getChildren()[33] instanceof Text );
		assertTrue( 2 == ( (GridData) metawidget.getChildren()[33].getLayoutData() ).horizontalSpan );
		assertTrue( "43.3".equals( metawidget.getValue( "doubleObject" ) ) );
		( (Text) metawidget.getChildren()[33] ).setText( "54.4" );

		assertTrue( "Char:".equals( ( (Label) metawidget.getChildren()[34] ).getText() ) );
		assertTrue( metawidget.getChildren()[35] instanceof Text );
		assertTrue( "A".equals( metawidget.getValue( "char" ) ) );
		( (Text) metawidget.getChildren()[35] ).setText( "Z" );

		assertTrue( "Boolean:".equals( ( (Label) metawidget.getChildren()[36] ).getText() ) );
		assertTrue( metawidget.getChildren()[37] instanceof Button );
		assertTrue( ( metawidget.getChildren()[37].getStyle() & SWT.CHECK ) == SWT.CHECK );
		assertTrue( false == (Boolean) metawidget.getValue( "boolean" ) );
		( (Button) metawidget.getChildren()[37] ).setSelection( true );

		assertTrue( "Boolean object:".equals( ( (Label) metawidget.getChildren()[38] ).getText() ) );
		assertTrue( metawidget.getChildren()[39] instanceof Combo );
		assertTrue( ( metawidget.getChildren()[39].getStyle() & SWT.READ_ONLY ) == SWT.READ_ONLY );
		assertTrue( 3 == ( (Combo) metawidget.getChildren()[39] ).getItemCount() );
		assertTrue( "true".equals( metawidget.getValue( "booleanObject" ) ) );
		( (Combo) metawidget.getChildren()[39] ).setText( "false" );

		assertTrue( "Dropdown:".equals( ( (Label) metawidget.getChildren()[40] ).getText() ) );
		assertTrue( metawidget.getChildren()[41] instanceof Combo );
		assertTrue( ( metawidget.getChildren()[41].getStyle() & SWT.READ_ONLY ) == SWT.READ_ONLY );
		assertTrue( 4 == ( (Combo) metawidget.getChildren()[41] ).getItemCount() );
		assertTrue( "dropdown1".equals( metawidget.getValue( "dropdown" ) ) );
		( (Combo) metawidget.getChildren()[41] ).setText( "foo1" );

		assertTrue( "Dropdown with labels:".equals( ( (Label) metawidget.getChildren()[42] ).getText() ) );
		assertTrue( metawidget.getChildren()[43] instanceof Combo );
		assertTrue( 5 == ( (Combo) metawidget.getChildren()[43] ).getItemCount() );
		assertTrue( "dropdown2".equals( metawidget.getValue( "dropdownWithLabels" ) ) );
		( (Combo) metawidget.getChildren()[43] ).setText( "bar2" );

		assertTrue( "Not null dropdown:".equals( ( (Label) metawidget.getChildren()[44] ).getText() ) );
		assertTrue( metawidget.getChildren()[45] instanceof Combo );
		assertTrue( 3 == ( (Combo) metawidget.getChildren()[45] ).getItemCount() );
		assertTrue( "0".equals( metawidget.getValue( "notNullDropdown" ) ) );
		( (Combo) metawidget.getChildren()[45] ).setText( "1" );

		assertTrue( "Not null object dropdown*:".equals( ( (Label) metawidget.getChildren()[46] ).getText() ) );
		assertTrue( metawidget.getChildren()[47] instanceof Combo );
		assertTrue( 6 == ( (Combo) metawidget.getChildren()[47] ).getItemCount() );
		assertTrue( "dropdown3".equals( metawidget.getValue( "notNullObjectDropdown" ) ) );
		( (Combo) metawidget.getChildren()[47] ).setText( "foo3" );

		assertTrue( "Nested widgets:".equals( ( (Label) metawidget.getChildren()[48] ).getText() ) );
		assertTrue( metawidget.getChildren()[49] instanceof SwtMetawidget );

		SwtMetawidget metawidgetNested = (SwtMetawidget) metawidget.getChildren()[49];
		assertTrue( "Further nested widgets:".equals( ( (Label) metawidgetNested.getChildren()[0] ).getText() ) );

		SwtMetawidget metawidgetFurtherNested = (SwtMetawidget) metawidgetNested.getChildren()[1];
		assertTrue( "Further nested widgets:".equals( ( (Label) metawidgetFurtherNested.getChildren()[0] ).getText() ) );
		assertTrue( metawidgetFurtherNested.getChildren()[1] instanceof SwtMetawidget );
		assertTrue( ( (SwtMetawidget) metawidgetFurtherNested.getChildren()[1] ).getChildren().length == 0 );

		assertTrue( "Nested textbox 1:".equals( ( (Label) metawidgetFurtherNested.getChildren()[2] ).getText() ) );
		assertTrue( metawidgetFurtherNested.getChildren()[3] instanceof Text );
		assertTrue( "Nested Textbox 1".equals( metawidget.getValue( "nestedWidgets", "furtherNestedWidgets", "nestedTextbox1" ) ) );
		( (Text) metawidgetFurtherNested.getChildren()[3] ).setText( "Nested Textbox 1.1 (further)" );

		assertTrue( "Nested textbox 2:".equals( ( (Label) metawidgetFurtherNested.getChildren()[4] ).getText() ) );
		assertTrue( metawidgetFurtherNested.getChildren()[5] instanceof Text );
		assertTrue( "Nested Textbox 2".equals( metawidget.getValue( "nestedWidgets", "furtherNestedWidgets", "nestedTextbox2" ) ) );
		( (Text) metawidgetFurtherNested.getChildren()[5] ).setText( "Nested Textbox 2.2 (further)" );

		assertTrue( "Nested textbox 1:".equals( ( (Label) metawidgetNested.getChildren()[2] ).getText() ) );
		assertTrue( metawidgetNested.getChildren()[3] instanceof Text );
		assertTrue( "Nested Textbox 1".equals( metawidget.getValue( "nestedWidgets", "nestedTextbox1" ) ) );
		( (Text) metawidgetNested.getChildren()[3] ).setText( "Nested Textbox 1.1" );

		assertTrue( "Nested textbox 2:".equals( ( (Label) metawidgetNested.getChildren()[4] ).getText() ) );
		assertTrue( metawidgetNested.getChildren()[5] instanceof Text );
		assertTrue( "Nested Textbox 2".equals( metawidget.getValue( "nestedWidgets", "nestedTextbox2" ) ) );
		( (Text) metawidgetNested.getChildren()[5] ).setText( "Nested Textbox 2.2" );

		assertTrue( "Read only nested widgets:".equals( ( (Label) metawidget.getChildren()[50] ).getText() ) );
		assertTrue( metawidget.getChildren()[51] instanceof SwtMetawidget );

		metawidgetNested = (SwtMetawidget) metawidget.getChildren()[51];
		assertTrue( "Nested textbox 1:".equals( ( (Label) metawidgetNested.getChildren()[2] ).getText() ) );
		assertTrue( metawidgetNested.getChildren()[3] instanceof Label );
		assertTrue( "Nested Textbox 1".equals( metawidget.getValue( "readOnlyNestedWidgets", "nestedTextbox1" ) ) );

		assertTrue( "Nested textbox 2:".equals( ( (Label) metawidgetNested.getChildren()[4] ).getText() ) );
		assertTrue( metawidgetNested.getChildren()[5] instanceof Label );
		assertTrue( "Nested Textbox 2".equals( metawidget.getValue( "readOnlyNestedWidgets", "nestedTextbox2" ) ) );

		assertTrue( "Nested widgets dont expand:".equals( ( (Label) metawidget.getChildren()[52] ).getText() ) );
		assertTrue( "Nested Textbox 1, Nested Textbox 2".equals( ( (Text) metawidget.getChildren()[53] ).getText() ) );
		( (Text) metawidget.getChildren()[53] ).setText( "Nested Textbox 1.01, Nested Textbox 2.02" );

		assertTrue( "Read only nested widgets dont expand:".equals( ( (Label) metawidget.getChildren()[54] ).getText() ) );
		assertTrue( "Nested Textbox 1, Nested Textbox 2".equals( ( (Label) metawidget.getChildren()[55] ).getText() ) );

		assertTrue( "Date:".equals( ( (Label) metawidget.getChildren()[56] ).getText() ) );
		assertTrue( metawidget.getChildren()[57] instanceof Text );

		DateFormat dateFormat = new SimpleDateFormat( DATE_FORMAT );
		assertTrue( dateFormat.format( allWidgets.getDate() ).equals( metawidget.getValue( "date" ) ) );
		( (Text) metawidget.getChildren()[57] ).setText( "bad date" );

		assertTrue( metawidget.getChildren()[58] instanceof Stub );
		assertTrue( "hidden".equals( metawidget.getChildren()[58].getData( "name" ) ) );
		assertTrue( ( (GridData) metawidget.getChildren()[58].getLayoutData() ).exclude );

		Composite separatorComposite = (Composite) metawidget.getChildren()[59];
		assertTrue( "Section Break".equals( ( (Label) separatorComposite.getChildren()[0] ).getText() ) );
		assertTrue( separatorComposite.getChildren()[1] instanceof Label );
		assertTrue( ( separatorComposite.getChildren()[1].getStyle() & SWT.SEPARATOR ) == SWT.SEPARATOR );
		assertTrue( SWT.FILL == ( (GridData) separatorComposite.getChildren()[1].getLayoutData() ).horizontalAlignment );
		assertTrue( ( (GridData) separatorComposite.getChildren()[1].getLayoutData() ).grabExcessHorizontalSpace );

		assertTrue( "Read only:".equals( ( (Label) metawidget.getChildren()[60] ).getText() ) );
		assertTrue( metawidget.getChildren()[61] instanceof Label );
		assertTrue( "Read Only".equals( metawidget.getValue( "readOnly" ) ) );

		assertTrue( metawidget.getChildren()[62] instanceof Stub );
		assertTrue( "mystery".equals( metawidget.getChildren()[62].getData( "name" ) ) );
		assertTrue( ( (GridData) metawidget.getChildren()[62].getLayoutData() ).exclude );

		assertTrue( metawidget.getChildren()[63] instanceof Stub );
		assertTrue( "collection".equals( metawidget.getChildren()[63].getData( "name" ) ) );
		assertTrue( ( (GridData) metawidget.getChildren()[63].getLayoutData() ).exclude );

		assertTrue( metawidget.getChildren()[64] instanceof Button );
		Button button = ( (Button) metawidget.getChildren()[64] );
		assertTrue( "Do action".equals( button.getText() ) );
		try
		{
			button.notifyListeners( SWT.Selection, null );
			assertTrue( false );
		}
		catch ( Exception e )
		{
			assertTrue( "doAction called".equals( e.getCause().getCause().getMessage() ) );
		}

		assertTrue( 65 == metawidget.getChildren().length );

		// Check MetawidgetException

		try
		{
			metawidget.getWidgetProcessor( DataBindingProcessor.class ).save( metawidget );
			assertTrue( false );
		}
		catch ( Exception e )
		{
			assertTrue( "Could not parse 'bad date'".equals( e.getMessage() ) );
		}

		// Check saving

		String now = dateFormat.format( new GregorianCalendar().getTime() );
		( (Text) metawidget.getChildren()[57] ).setText( now );
		metawidget.getWidgetProcessor( DataBindingProcessor.class ).save( metawidget );

		// Check read-only

		metawidget.setReadOnly( true );

		assertTrue( "Textbox:".equals( ( (Label) metawidget.getChildren()[0] ).getText() ) );
		assertTrue( "Textbox1".equals( ( (Label) metawidget.getChildren()[1] ).getText() ) );
		assertTrue( "Limited textbox:".equals( ( (Label) metawidget.getChildren()[2] ).getText() ) );
		assertTrue( "Limited Textbox1".equals( ( (Label) metawidget.getChildren()[3] ).getText() ) );
		assertTrue( "Textarea:".equals( ( (Label) metawidget.getChildren()[4] ).getText() ) );
		assertTrue( "Textarea1".equals( ( (Text) metawidget.getChildren()[5] ).getText() ) );
		assertTrue( ( metawidget.getChildren()[5].getStyle() & SWT.READ_ONLY ) == SWT.READ_ONLY );
		assertTrue( ( metawidget.getChildren()[5].getStyle() & SWT.MULTI ) == SWT.MULTI );
		assertTrue( ( metawidget.getChildren()[5].getStyle() & SWT.BORDER ) == SWT.BORDER );
		assertTrue( ( metawidget.getChildren()[5].getStyle() & SWT.V_SCROLL ) == SWT.V_SCROLL );
		assertTrue( ( metawidget.getChildren()[5].getStyle() & SWT.WRAP ) == SWT.WRAP );
		assertTrue( "Password:".equals( ( (Label) metawidget.getChildren()[6] ).getText() ) );
		assertTrue( metawidget.getChildren()[7] instanceof Composite );
		assertTrue( "Byte:".equals( ( (Label) metawidget.getChildren()[8] ).getText() ) );
		assertTrue( "126".equals( ( (Label) metawidget.getChildren()[9] ).getText() ) );
		assertTrue( "Byte object:".equals( ( (Label) metawidget.getChildren()[10] ).getText() ) );
		assertTrue( "-127".equals( ( (Label) metawidget.getChildren()[11] ).getText() ) );
		assertTrue( "Short:".equals( ( (Label) metawidget.getChildren()[12] ).getText() ) );
		assertTrue( "32,766".equals( ( (Label) metawidget.getChildren()[13] ).getText() ) );
		assertTrue( "Short object:".equals( ( (Label) metawidget.getChildren()[14] ).getText() ) );
		assertTrue( "-32,767".equals( ( (Label) metawidget.getChildren()[15] ).getText() ) );
		assertTrue( "Int:".equals( ( (Label) metawidget.getChildren()[16] ).getText() ) );
		assertTrue( "2,147,483,646".equals( ( (Label) metawidget.getChildren()[17] ).getText() ) );
		assertTrue( "Integer object:".equals( ( (Label) metawidget.getChildren()[18] ).getText() ) );
		assertTrue( "-2,147,483,647".equals( ( (Label) metawidget.getChildren()[19] ).getText() ) );
		assertTrue( "Ranged int:".equals( ( (Label) metawidget.getChildren()[20] ).getText() ) );
		assertTrue( "33".equals( ( (Label) metawidget.getChildren()[21] ).getText() ) );
		assertTrue( "Ranged integer:".equals( ( (Label) metawidget.getChildren()[22] ).getText() ) );
		assertTrue( "34".equals( ( (Label) metawidget.getChildren()[23] ).getText() ) );
		assertTrue( "Long:".equals( ( (Label) metawidget.getChildren()[24] ).getText() ) );
		assertTrue( "43".equals( ( (Label) metawidget.getChildren()[25] ).getText() ) );
		assertTrue( "44".equals( ( (Label) metawidget.getChildren()[26] ).getText() ) );
		assertTrue( "Float:".equals( ( (Label) metawidget.getChildren()[27] ).getText() ) );
		assertTrue( ( (Label) metawidget.getChildren()[28] ).getText().startsWith( "4.3" ) || ( (Label) metawidget.getChildren()[28] ).getText().startsWith( "4.299" ) );
		assertTrue( "nullInBundle:".equals( ( (Label) metawidget.getChildren()[29] ).getText() ) );
		assertTrue( "5.4".equals( ( (Label) metawidget.getChildren()[30] ).getText() ) );
		assertTrue( "Double:".equals( ( (Label) metawidget.getChildren()[31] ).getText() ) );
		assertTrue( ( (Label) metawidget.getChildren()[32] ).getText().startsWith( "42.3" ) || ( (Label) metawidget.getChildren()[32] ).getText().startsWith( "42.299" ) );
		assertTrue( "54.4".equals( ( (Label) metawidget.getChildren()[33] ).getText() ) );
		assertTrue( "Char:".equals( ( (Label) metawidget.getChildren()[34] ).getText() ) );
		assertTrue( "Z".equals( ( (Label) metawidget.getChildren()[35] ).getText() ) );
		assertTrue( "Boolean:".equals( ( (Label) metawidget.getChildren()[36] ).getText() ) );
		assertTrue( "true".equals( ( (Label) metawidget.getChildren()[37] ).getText() ) );
		assertTrue( "Boolean object:".equals( ( (Label) metawidget.getChildren()[38] ).getText() ) );
		assertTrue( "false".equals( ( (Label) metawidget.getChildren()[39] ).getText() ) );
		assertTrue( "Dropdown:".equals( ( (Label) metawidget.getChildren()[40] ).getText() ) );
		assertTrue( "foo1".equals( ( (Label) metawidget.getChildren()[41] ).getText() ) );
		assertTrue( "Dropdown with labels:".equals( ( (Label) metawidget.getChildren()[42] ).getText() ) );
		assertTrue( "bar2".equals( ( (Label) metawidget.getChildren()[43] ).getText() ) );
		assertTrue( "Not null dropdown:".equals( ( (Label) metawidget.getChildren()[44] ).getText() ) );
		assertTrue( "1".equals( ( (Label) metawidget.getChildren()[45] ).getText() ) );
		assertTrue( "Not null object dropdown:".equals( ( (Label) metawidget.getChildren()[46] ).getText() ) );
		assertTrue( "foo3".equals( ( (Label) metawidget.getChildren()[47] ).getText() ) );
		assertTrue( "Nested widgets:".equals( ( (Label) metawidget.getChildren()[48] ).getText() ) );
		assertTrue( "Further nested widgets:".equals( ( (Label) ( (SwtMetawidget) metawidget.getChildren()[49] ).getChildren()[0] ).getText() ) );
		assertTrue( "Further nested widgets:".equals( ( (Label) ( (SwtMetawidget) ( (SwtMetawidget) metawidget.getChildren()[49] ).getChildren()[1] ).getChildren()[0] ).getText() ) );
		assertTrue( "Nested textbox 1:".equals( ( (Label) ( (SwtMetawidget) ( (SwtMetawidget) metawidget.getChildren()[49] ).getChildren()[1] ).getChildren()[2] ).getText() ) );
		assertTrue( "Nested Textbox 1.1 (further)".equals( ( (Label) ( (SwtMetawidget) ( (SwtMetawidget) metawidget.getChildren()[49] ).getChildren()[1] ).getChildren()[3] ).getText() ) );
		assertTrue( "Nested textbox 2:".equals( ( (Label) ( (SwtMetawidget) ( (SwtMetawidget) metawidget.getChildren()[49] ).getChildren()[1] ).getChildren()[4] ).getText() ) );
		assertTrue( "Nested Textbox 2.2 (further)".equals( ( (Label) ( (SwtMetawidget) ( (SwtMetawidget) metawidget.getChildren()[49] ).getChildren()[1] ).getChildren()[5] ).getText() ) );
		assertTrue( "Nested textbox 1:".equals( ( (Label) ( (SwtMetawidget) metawidget.getChildren()[49] ).getChildren()[2] ).getText() ) );
		assertTrue( "Nested Textbox 1.1".equals( ( (Label) ( (SwtMetawidget) metawidget.getChildren()[49] ).getChildren()[3] ).getText() ) );
		assertTrue( "Nested textbox 2:".equals( ( (Label) ( (SwtMetawidget) metawidget.getChildren()[49] ).getChildren()[4] ).getText() ) );
		assertTrue( "Nested Textbox 2.2".equals( ( (Label) ( (SwtMetawidget) metawidget.getChildren()[49] ).getChildren()[5] ).getText() ) );
		assertTrue( "Read only nested widgets:".equals( ( (Label) metawidget.getChildren()[50] ).getText() ) );
		assertTrue( "Further nested widgets:".equals( ( (Label) ( (SwtMetawidget) metawidget.getChildren()[51] ).getChildren()[0] ).getText() ) );
		assertTrue( "Nested textbox 1:".equals( ( (Label) ( (SwtMetawidget) metawidget.getChildren()[51] ).getChildren()[2] ).getText() ) );
		assertTrue( "Nested Textbox 1".equals( ( (Label) ( (SwtMetawidget) metawidget.getChildren()[51] ).getChildren()[3] ).getText() ) );
		assertTrue( "Nested textbox 2:".equals( ( (Label) ( (SwtMetawidget) metawidget.getChildren()[51] ).getChildren()[4] ).getText() ) );
		assertTrue( "Nested Textbox 2".equals( ( (Label) ( (SwtMetawidget) metawidget.getChildren()[51] ).getChildren()[5] ).getText() ) );
		assertTrue( "Nested widgets dont expand:".equals( ( (Label) metawidget.getChildren()[52] ).getText() ) );
		assertTrue( "Nested Textbox 1.01, Nested Textbox 2.02".equals( ( (Label) metawidget.getChildren()[53] ).getText() ) );
		assertTrue( "Read only nested widgets dont expand:".equals( ( (Label) metawidget.getChildren()[54] ).getText() ) );
		assertTrue( "Nested Textbox 1, Nested Textbox 2".equals( ( (Label) metawidget.getChildren()[55] ).getText() ) );
		assertTrue( "Date:".equals( ( (Label) metawidget.getChildren()[56] ).getText() ) );
		assertTrue( now.equals( ( (Label) metawidget.getChildren()[57] ).getText() ) );
		assertTrue( "Section Break".equals( ( (Label) ( (Composite) metawidget.getChildren()[59] ).getChildren()[0] ).getText() ) );
		assertTrue( "Read only:".equals( ( (Label) metawidget.getChildren()[60] ).getText() ) );
		assertTrue( "Read Only".equals( ( (Label) metawidget.getChildren()[61] ).getText() ) );

		assertTrue( metawidget.getChildren().length == 65 );
	}
}
