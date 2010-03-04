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

import java.util.TimeZone;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.metawidget.shared.allwidgets.model.AllWidgets;

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
	// Constructor
	//

	public SwtAllWidgetsTest()
	{
		// Default constructor
	}

	/**
	 * JUnit 3.7 constructor (SwtAllWidgetsTest gets run under JDK 1.4 using JUnit 3).
	 */

	public SwtAllWidgetsTest( String name )
	{
		super( name );
	}

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

		Display display = new Display();
		Shell shell = new Shell( display, SWT.NONE );
		SwtMetawidget metawidget = new SwtMetawidget( shell, SWT.NONE );
		metawidget.setConfig( "org/metawidget/swt/allwidgets/metawidget.xml" );

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
		assertTrue( ((GridData) metawidget.getChildren()[5].getLayoutData()).grabExcessVerticalSpace );
		assertTrue( (((Text) metawidget.getChildren()[5]).getStyle() & SWT.MULTI ) == SWT.MULTI );
		assertTrue( (((Text) metawidget.getChildren()[5]).getStyle() & SWT.BORDER ) == SWT.BORDER );
		assertTrue( (((Text) metawidget.getChildren()[5]).getStyle() & SWT.V_SCROLL ) == SWT.V_SCROLL );
		assertTrue( (((Text) metawidget.getChildren()[5]).getStyle() & SWT.WRAP ) == SWT.WRAP );
		assertTrue( (((Text) metawidget.getChildren()[5]).getStyle() & SWT.H_SCROLL ) == SWT.NONE );
		assertTrue( "Textarea".equals( metawidget.getValue( "textarea" ) ) );
		( (Text) metawidget.getChildren()[5] ).setText( "Textarea1" );

		assertTrue( "Password:".equals( ( (Label) metawidget.getChildren()[6] ).getText() ) );
		assertTrue( metawidget.getChildren()[7] instanceof Text );
		assertTrue( (((Text) metawidget.getChildren()[7]).getStyle() & SWT.PASSWORD ) == SWT.PASSWORD );
		assertTrue( (((Text) metawidget.getChildren()[7]).getStyle() & SWT.BORDER ) == SWT.BORDER );
		assertTrue( "Password".equals( metawidget.getValue( "password" ) ) );
		( (Text) metawidget.getChildren()[7] ).setText( "Password1" );

		// Primitives

		assertTrue( "Byte:".equals( ( (Label) metawidget.getChildren()[8] ).getText() ) );
		assertTrue( Byte.MAX_VALUE == (Integer) metawidget.getValue( "byte" ) );
		Spinner spinner = (Spinner) metawidget.getChildren()[9];

		// (negative Spinner values don't always work - https://bugs.eclipse.org/bugs/show_bug.cgi?id=91317)

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
		assertTrue( String.valueOf( Short.MIN_VALUE ).equals( ((String) metawidget.getValue( "shortObject" )).replaceAll( ",", "" ) ) );
		( (Text) metawidget.getChildren()[15] ).setText( String.valueOf( Short.MIN_VALUE + 1 ) );

		assertTrue( "Int:".equals( ( (Label) metawidget.getChildren()[16] ).getText() ) );
		assertTrue( Integer.MAX_VALUE == (Integer) metawidget.getValue( "int" ) );
		spinner = (Spinner) metawidget.getChildren()[17];
		assertTrue( Integer.MIN_VALUE == spinner.getMinimum() || 0 == spinner.getMinimum() );
		assertTrue( Integer.MAX_VALUE == spinner.getMaximum() );
		spinner.setSelection( spinner.getSelection() - spinner.getIncrement() );

		assertTrue( "Integer object:".equals( ( (Label) metawidget.getChildren()[18] ).getText() ) );
		assertTrue( metawidget.getChildren()[19] instanceof Text );
		assertTrue( String.valueOf( Integer.MIN_VALUE ).equals( ((String) metawidget.getValue( "integerObject" )).replaceAll( ",", "" ) ) );
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

		/*
		assertTrue( "Long:".equals( ( (Label) metawidget.getChildren()[24] ).getText() ) );
		assertTrue( metawidget.getChildren()[25] instanceof Spinner );
		assertTrue( 3 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getChildren()[25] ).gridx );
		assertTrue( 42 == (Long) metawidget.getValue( "long" ) );
		assertTrue( -7 == (Long) ( (SpinnerNumberModel) ( (Spinner) metawidget.getChildren()[25] ).getModel() ).getMinimum() );
		assertTrue( Long.MAX_VALUE == (Long) ( (SpinnerNumberModel) ( (Spinner) metawidget.getChildren()[25] ).getModel() ).getMaximum() );
		spinner = (Spinner) metawidget.getChildren()[25];
		assertTrue( 0 == ( (Spinner.DefaultEditor) spinner.getEditor() ).getTextField().getColumns() );
		spinner.setValue( spinner.getModel().getNextValue() );

		assertTrue( metawidget.getChildren()[26] instanceof Text );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getChildren()[26] ).gridx );
		assertTrue( "43".equals( metawidget.getValue( "longObject" ) ) );
		( (Text) metawidget.getChildren()[26] ).setText( "44" );

		assertTrue( "Float:".equals( ( (Label) metawidget.getChildren()[27] ).getText() ) );
		assertTrue( metawidget.getChildren()[28] instanceof Spinner );
		assertTrue( 3 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getChildren()[28] ).gridx );
		assertTrue( 4.2f == (Float) metawidget.getValue( "float" ) );
		assertTrue( -Float.MAX_VALUE == (Float) ( (SpinnerNumberModel) ( (Spinner) metawidget.getChildren()[28] ).getModel() ).getMinimum() );
		assertTrue( 2048 == (Float) ( (SpinnerNumberModel) ( (Spinner) metawidget.getChildren()[28] ).getModel() ).getMaximum() );
		spinner = (Spinner) metawidget.getChildren()[28];
		assertTrue( 0 == ( (Spinner.DefaultEditor) spinner.getEditor() ).getTextField().getColumns() );
		spinner.setValue( spinner.getModel().getNextValue() );

		assertTrue( "nullInBundle:".equals( ( (Label) metawidget.getChildren()[29] ).getText() ) );
		assertTrue( metawidget.getChildren()[30] instanceof Text );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getChildren()[30] ).gridx );
		assertTrue( "4.3".equals( metawidget.getValue( "floatObject" ) ) );
		( (Text) metawidget.getChildren()[30] ).setText( "5.4" );

		assertTrue( "Double:".equals( ( (Label) metawidget.getChildren()[31] ).getText() ) );
		assertTrue( metawidget.getChildren()[32] instanceof Spinner );
		assertTrue( 3 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getChildren()[32] ).gridx );
		assertTrue( 42.2d == (Double) metawidget.getValue( "double" ) );
		assertTrue( -8 == (Double) ( (SpinnerNumberModel) ( (Spinner) metawidget.getChildren()[32] ).getModel() ).getMinimum() );
		assertTrue( Double.MAX_VALUE == (Double) ( (SpinnerNumberModel) ( (Spinner) metawidget.getChildren()[32] ).getModel() ).getMaximum() );
		spinner = (Spinner) metawidget.getChildren()[32];
		assertTrue( 0 == ( (Spinner.DefaultEditor) spinner.getEditor() ).getTextField().getColumns() );
		spinner.setValue( spinner.getModel().getNextValue() );

		assertTrue( metawidget.getChildren()[33] instanceof Text );
		assertTrue( 0 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getChildren()[33] ).gridx );
		assertTrue( 2 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getChildren()[33] ).gridwidth );
		assertTrue( "43.3".equals( metawidget.getValue( "doubleObject" ) ) );
		( (Text) metawidget.getChildren()[33] ).setText( "54.4" );

		assertTrue( "Char:".equals( ( (Label) metawidget.getChildren()[34] ).getText() ) );
		assertTrue( metawidget.getChildren()[35] instanceof Text );
		assertTrue( 3 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getChildren()[35] ).gridx );
		assertTrue( "A".equals( metawidget.getValue( "char" ) ) );
		( (Text) metawidget.getChildren()[35] ).setText( "Z" );

		assertTrue( "Boolean:".equals( ( (Label) metawidget.getChildren()[36] ).getText() ) );
		assertTrue( metawidget.getChildren()[37] instanceof JCheckBox );
		assertTrue( false == (Boolean) metawidget.getValue( "boolean" ) );
		( (JCheckBox) metawidget.getChildren()[37] ).setSelected( true );

		assertTrue( "Boolean object:".equals( ( (Label) metawidget.getChildren()[38] ).getText() ) );
		assertTrue( metawidget.getChildren()[39] instanceof JComboBox );
		assertTrue( 3 == ( (JComboBox) metawidget.getChildren()[39] ).getItemCount() );
		assertTrue( Boolean.TRUE.equals( metawidget.getValue( "booleanObject" ) ) );
		( (JComboBox) metawidget.getChildren()[39] ).setSelectedItem( Boolean.FALSE );

		assertTrue( "Dropdown:".equals( ( (Label) metawidget.getChildren()[40] ).getText() ) );
		assertTrue( metawidget.getChildren()[41] instanceof JComboBox );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getChildren()[41] ).gridx );
		assertTrue( 4 == ( (JComboBox) metawidget.getChildren()[41] ).getItemCount() );
		assertTrue( "dropdown1".equals( metawidget.getValue( "dropdown" ) ) );
		( (JComboBox) metawidget.getChildren()[41] ).setSelectedItem( "foo1" );

		assertTrue( "Dropdown with labels:".equals( ( (Label) metawidget.getChildren()[42] ).getText() ) );
		assertTrue( metawidget.getChildren()[43] instanceof JComboBox );
		assertTrue( 3 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getChildren()[43] ).gridx );

		JComboBox combo = (JComboBox) metawidget.getChildren()[43];
		assertTrue( 5 == combo.getItemCount() );
		assertTrue( "Foo #2".equals( ( (Label) combo.getRenderer().getListCellRendererComponent( new JList(), "foo2", 1, false, false ) ).getText() ) );
		assertTrue( "Dropdown #2".equals( ( (Label) combo.getRenderer().getListCellRendererComponent( new JList(), "dropdown2", 1, false, false ) ).getText() ) );
		assertTrue( "Bar #2".equals( ( (Label) combo.getRenderer().getListCellRendererComponent( new JList(), "bar2", 1, false, false ) ).getText() ) );
		assertTrue( "Baz #2".equals( ( (Label) combo.getRenderer().getListCellRendererComponent( new JList(), "baz2", 1, false, false ) ).getText() ) );
		assertTrue( "dropdown2".equals( metawidget.getValue( "dropdownWithLabels" ) ) );
		( (JComboBox) metawidget.getChildren()[43] ).setSelectedItem( "bar2" );

		assertTrue( "Not null dropdown:".equals( ( (Label) metawidget.getChildren()[44] ).getText() ) );
		assertTrue( metawidget.getChildren()[45] instanceof JComboBox );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getChildren()[45] ).gridx );
		assertTrue( 3 == ( (JComboBox) metawidget.getChildren()[45] ).getItemCount() );
		assertTrue( 0 == (Byte) metawidget.getValue( "notNullDropdown" ) );
		( (JComboBox) metawidget.getChildren()[45] ).setSelectedItem( (byte) 1 );

		assertTrue( "Not null object dropdown*:".equals( ( (Label) metawidget.getChildren()[46] ).getText() ) );
		assertTrue( metawidget.getChildren()[47] instanceof JComboBox );
		assertTrue( 3 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getChildren()[47] ).gridx );
		assertTrue( 6 == ( (JComboBox) metawidget.getChildren()[47] ).getItemCount() );
		assertTrue( "dropdown3".equals( metawidget.getValue( "notNullObjectDropdown" ) ) );
		( (JComboBox) metawidget.getChildren()[47] ).setSelectedIndex( 0 );

		assertTrue( "Nested widgets:".equals( ( (Label) metawidget.getChildren()[48] ).getText() ) );
		assertTrue( metawidget.getChildren()[49] instanceof SwingMetawidget );

		SwingMetawidget metawidgetNested = (SwingMetawidget) metawidget.getChildren()[49];

		assertTrue( "Further nested widgets:".equals( ( (Label) metawidgetNested.getChildren()[0] ).getText() ) );
		assertTrue( 1 == ( (GridBagLayout) metawidgetNested.getLayout() ).getConstraints( metawidgetNested.getChildren()[1] ).gridx );

		SwingMetawidget metawidgetFurtherNested = (SwingMetawidget) metawidgetNested.getChildren()[1];
		assertTrue( "Further nested widgets:".equals( ( (Label) metawidgetFurtherNested.getChildren()[0] ).getText() ) );
		assertTrue( metawidgetFurtherNested.getChildren()[1] instanceof SwingMetawidget );
		assertTrue( ( (JPanel) ( (SwingMetawidget) metawidgetFurtherNested.getChildren()[1] ).getChildren()[0] ).getComponentCount() == 0 );

		assertTrue( "Nested textbox 1:".equals( ( (Label) metawidgetFurtherNested.getChildren()[2] ).getText() ) );
		assertTrue( metawidgetFurtherNested.getChildren()[3] instanceof Text );
		assertTrue( 1 == ( (GridBagLayout) metawidgetFurtherNested.getLayout() ).getConstraints( metawidgetFurtherNested.getChildren()[3] ).gridx );
		assertTrue( "Nested Textbox 1".equals( metawidget.getValue( "nestedWidgets", "furtherNestedWidgets", "nestedTextbox1" ) ) );
		( (Text) metawidgetFurtherNested.getChildren()[3] ).setText( "Nested Textbox 1.1 (further)" );

		assertTrue( "Nested textbox 2:".equals( ( (Label) metawidgetFurtherNested.getChildren()[4] ).getText() ) );
		assertTrue( metawidgetFurtherNested.getChildren()[5] instanceof Text );

		// (should be 1, as in next row, if getEffectiveNumberOfColumns is working)

		assertTrue( 1 == ( (GridBagLayout) metawidgetFurtherNested.getLayout() ).getConstraints( metawidgetFurtherNested.getChildren()[5] ).gridx );
		assertTrue( "Nested Textbox 2".equals( metawidget.getValue( "nestedWidgets", "furtherNestedWidgets", "nestedTextbox2" ) ) );
		( (Text) metawidgetFurtherNested.getChildren()[5] ).setText( "Nested Textbox 2.2 (further)" );

		assertTrue( "Nested textbox 1:".equals( ( (Label) metawidgetNested.getChildren()[2] ).getText() ) );
		assertTrue( metawidgetNested.getChildren()[3] instanceof Text );
		assertTrue( 1 == ( (GridBagLayout) metawidgetNested.getLayout() ).getConstraints( metawidgetNested.getChildren()[3] ).gridx );
		assertTrue( "Nested Textbox 1".equals( metawidget.getValue( "nestedWidgets", "nestedTextbox1" ) ) );
		( (Text) metawidgetNested.getChildren()[3] ).setText( "Nested Textbox 1.1" );

		assertTrue( "Nested textbox 2:".equals( ( (Label) metawidgetNested.getChildren()[4] ).getText() ) );
		assertTrue( metawidgetNested.getChildren()[5] instanceof Text );
		assertTrue( 1 == ( (GridBagLayout) metawidgetNested.getLayout() ).getConstraints( metawidgetNested.getChildren()[5] ).gridx );
		assertTrue( "Nested Textbox 2".equals( metawidget.getValue( "nestedWidgets", "nestedTextbox2" ) ) );
		( (Text) metawidgetNested.getChildren()[5] ).setText( "Nested Textbox 2.2" );

		assertTrue( "Read only nested widgets:".equals( ( (Label) metawidget.getChildren()[50] ).getText() ) );
		assertTrue( metawidget.getChildren()[51] instanceof SwingMetawidget );

		metawidgetNested = (SwingMetawidget) metawidget.getChildren()[51];
		assertTrue( "Nested textbox 1:".equals( ( (Label) metawidgetNested.getChildren()[2] ).getText() ) );
		assertTrue( metawidgetNested.getChildren()[3] instanceof Label );
		assertTrue( 1 == ( (GridBagLayout) metawidgetNested.getLayout() ).getConstraints( metawidgetNested.getChildren()[3] ).gridx );
		assertTrue( "Nested Textbox 1".equals( metawidget.getValue( "readOnlyNestedWidgets", "nestedTextbox1" ) ) );

		assertTrue( "Nested textbox 2:".equals( ( (Label) metawidgetNested.getChildren()[4] ).getText() ) );
		assertTrue( metawidgetNested.getChildren()[5] instanceof Label );
		assertTrue( 1 == ( (GridBagLayout) metawidgetNested.getLayout() ).getConstraints( metawidgetNested.getChildren()[5] ).gridx );
		assertTrue( "Nested Textbox 2".equals( metawidget.getValue( "readOnlyNestedWidgets", "nestedTextbox2" ) ) );

		assertTrue( "Nested widgets dont expand:".equals( ( (Label) metawidget.getChildren()[52] ).getText() ) );
		assertTrue( "Nested Textbox 1, Nested Textbox 2".equals( ( (Text) metawidget.getChildren()[53] ).getText() ) );
		( (Text) metawidget.getChildren()[53] ).setText( "Nested Textbox 1.01, Nested Textbox 2.02" );

		assertTrue( "Read only nested widgets dont expand:".equals( ( (Label) metawidget.getChildren()[54] ).getText() ) );
		assertTrue( "Nested Textbox 1, Nested Textbox 2".equals( ( (Label) metawidget.getChildren()[55] ).getText() ) );

		assertTrue( "Date:".equals( ( (Label) metawidget.getChildren()[56] ).getText() ) );
		assertTrue( metawidget.getChildren()[57] instanceof Text );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getChildren()[57] ).gridx );

		DateFormat dateFormat = new SimpleDateFormat( DATE_FORMAT );
		assertTrue( dateFormat.format( allWidgets.getDate() ).equals( metawidget.getValue( "date" ) ) );
		( (Text) metawidget.getChildren()[57] ).setText( "bad date" );

		JPanel separatorPanel = (JPanel) metawidget.getChildren()[58];
		assertTrue( "Section Break".equals( ((Label) separatorPanel.getChildren()[0]).getText() ) );
		assertTrue( GridBagConstraints.REMAINDER == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getChildren()[58] ).gridwidth );
		assertTrue( separatorPanel.getChildren()[1] instanceof JSeparator );

		assertTrue( "Read only:".equals( ( (Label) metawidget.getChildren()[59] ).getText() ) );
		assertTrue( metawidget.getChildren()[60] instanceof Label );
		assertTrue( "Read Only".equals( metawidget.getValue( "readOnly" ) ) );

		assertTrue( metawidget.getChildren()[61] instanceof JButton );
		assertTrue( 3 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getChildren()[61] ).gridx );
		assertTrue( GridBagConstraints.NONE == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getChildren()[61] ).fill );
		JButton button = ( (JButton) metawidget.getChildren()[61] );
		assertTrue( "Do action".equals( button.getText() ) );
		try
		{
			button.doClick();
			assertTrue( false );
		}
		catch ( Exception e )
		{
			assertTrue( "doAction called".equals( e.getCause().getCause().getMessage() ) );
		}

		assertTrue( 62 == metawidget.getComponentCount() );

		// Check painting

		JFrame frame = new JFrame();
		metawidget.paint( frame.getGraphics() );

		// Check MetawidgetException

		try
		{
			processor.getClass().getMethod( "save", SwingMetawidget.class ).invoke( processor, metawidget );
			assertTrue( false );
		}
		catch ( Exception e )
		{
			assertTrue( "Could not parse 'bad date'".equals( e.getCause().getCause().getMessage() ) );
		}

		// Check saving

		String now = dateFormat.format( new GregorianCalendar().getTime() );
		( (Text) metawidget.getChildren()[57] ).setText( now );
		processor.getClass().getMethod( "save", SwingMetawidget.class ).invoke( processor, metawidget );

		// Check read-only

		metawidget.setReadOnly( true );

		assertTrue( "Textbox:".equals( ( (Label) metawidget.getChildren()[0] ).getText() ) );
		assertTrue( "Textbox1".equals( ( (Label) metawidget.getChildren()[1] ).getText() ) );
		assertTrue( "Limited textbox:".equals( ( (Label) metawidget.getChildren()[2] ).getText() ) );
		assertTrue( "Limited Textbox1".equals( ( (Label) metawidget.getChildren()[3] ).getText() ) );
		assertTrue( "Textarea:".equals( ( (Label) metawidget.getChildren()[4] ).getText() ) );
		assertTrue( "Textarea1".equals( ( (JTextArea) ( (JScrollPane) metawidget.getChildren()[5] ).getViewport().getView() ).getText() ) );
		assertTrue( "Password:".equals( ( (Label) metawidget.getChildren()[6] ).getText() ) );
		assertTrue( metawidget.getChildren()[7] instanceof JPanel );
		assertTrue( "Byte:".equals( ( (Label) metawidget.getChildren()[8] ).getText() ) );
		assertTrue( "126".equals( ( (Label) metawidget.getChildren()[9] ).getText() ) );
		assertTrue( "Byte object:".equals( ( (Label) metawidget.getChildren()[10] ).getText() ) );
		assertTrue( "-127".equals( ( (Label) metawidget.getChildren()[11] ).getText() ) );
		assertTrue( "Short:".equals( ( (Label) metawidget.getChildren()[12] ).getText() ) );
		assertTrue( "32766".equals( ( (Label) metawidget.getChildren()[13] ).getText() ) );
		assertTrue( "Short object:".equals( ( (Label) metawidget.getChildren()[14] ).getText() ) );
		assertTrue( "-32767".equals( ( (Label) metawidget.getChildren()[15] ).getText() ) );
		assertTrue( "Int:".equals( ( (Label) metawidget.getChildren()[16] ).getText() ) );
		assertTrue( "2147483646".equals( ( (Label) metawidget.getChildren()[17] ).getText() ) );
		assertTrue( "Integer object:".equals( ( (Label) metawidget.getChildren()[18] ).getText() ) );
		assertTrue( "-2147483647".equals( ( (Label) metawidget.getChildren()[19] ).getText() ) );
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
		assertTrue( "No".equals( ( (Label) metawidget.getChildren()[39] ).getText() ) );
		assertTrue( "Dropdown:".equals( ( (Label) metawidget.getChildren()[40] ).getText() ) );
		assertTrue( "foo1".equals( ( (Label) metawidget.getChildren()[41] ).getText() ) );
		assertTrue( "Dropdown with labels:".equals( ( (Label) metawidget.getChildren()[42] ).getText() ) );
		assertTrue( "Bar #2".equals( ( (Label) metawidget.getChildren()[43] ).getText() ) );
		assertTrue( "Not null dropdown:".equals( ( (Label) metawidget.getChildren()[44] ).getText() ) );
		assertTrue( "1".equals( ( (Label) metawidget.getChildren()[45] ).getText() ) );
		assertTrue( "Not null object dropdown:".equals( ( (Label) metawidget.getChildren()[46] ).getText() ) );
		assertTrue( "foo3".equals( ( (Label) metawidget.getChildren()[47] ).getText() ) );
		assertTrue( "Nested widgets:".equals( ( (Label) metawidget.getChildren()[48] ).getText() ) );
		assertTrue( "Further nested widgets:".equals( ( (Label) ( (SwingMetawidget) metawidget.getChildren()[49] ).getChildren()[0] ).getText() ) );
		assertTrue( "Further nested widgets:".equals( ( (Label) ( (SwingMetawidget) ( (SwingMetawidget) metawidget.getChildren()[49] ).getChildren()[1] ).getChildren()[0] ).getText() ) );
		assertTrue( "Nested textbox 1:".equals( ( (Label) ( (SwingMetawidget) ( (SwingMetawidget) metawidget.getChildren()[49] ).getChildren()[1] ).getChildren()[2] ).getText() ) );
		assertTrue( "Nested Textbox 1.1 (further)".equals( ( (Label) ( (SwingMetawidget) ( (SwingMetawidget) metawidget.getChildren()[49] ).getChildren()[1] ).getChildren()[3] ).getText() ) );
		assertTrue( "Nested textbox 2:".equals( ( (Label) ( (SwingMetawidget) ( (SwingMetawidget) metawidget.getChildren()[49] ).getChildren()[1] ).getChildren()[4] ).getText() ) );
		assertTrue( "Nested Textbox 2.2 (further)".equals( ( (Label) ( (SwingMetawidget) ( (SwingMetawidget) metawidget.getChildren()[49] ).getChildren()[1] ).getChildren()[5] ).getText() ) );
		assertTrue( "Nested textbox 1:".equals( ( (Label) ( (SwingMetawidget) metawidget.getChildren()[49] ).getChildren()[2] ).getText() ) );
		assertTrue( "Nested Textbox 1.1".equals( ( (Label) ( (SwingMetawidget) metawidget.getChildren()[49] ).getChildren()[3] ).getText() ) );
		assertTrue( "Nested textbox 2:".equals( ( (Label) ( (SwingMetawidget) metawidget.getChildren()[49] ).getChildren()[4] ).getText() ) );
		assertTrue( "Nested Textbox 2.2".equals( ( (Label) ( (SwingMetawidget) metawidget.getChildren()[49] ).getChildren()[5] ).getText() ) );
		assertTrue( "Read only nested widgets:".equals( ( (Label) metawidget.getChildren()[50] ).getText() ) );
		assertTrue( "Further nested widgets:".equals( ( (Label) ( (SwingMetawidget) metawidget.getChildren()[51] ).getChildren()[0] ).getText() ) );
		assertTrue( "Nested textbox 1:".equals( ( (Label) ( (SwingMetawidget) metawidget.getChildren()[51] ).getChildren()[2] ).getText() ) );
		assertTrue( "Nested Textbox 1".equals( ( (Label) ( (SwingMetawidget) metawidget.getChildren()[51] ).getChildren()[3] ).getText() ) );
		assertTrue( "Nested textbox 2:".equals( ( (Label) ( (SwingMetawidget) metawidget.getChildren()[51] ).getChildren()[4] ).getText() ) );
		assertTrue( "Nested Textbox 2".equals( ( (Label) ( (SwingMetawidget) metawidget.getChildren()[51] ).getChildren()[5] ).getText() ) );
		assertTrue( "Nested widgets dont expand:".equals( ( (Label) metawidget.getChildren()[52] ).getText() ) );
		assertTrue( "Nested Textbox 1.01, Nested Textbox 2.02".equals( ( (Label) metawidget.getChildren()[53] ).getText() ) );
		assertTrue( "Read only nested widgets dont expand:".equals( ( (Label) metawidget.getChildren()[54] ).getText() ) );
		assertTrue( "Nested Textbox 1, Nested Textbox 2".equals( ( (Label) metawidget.getChildren()[55] ).getText() ) );
		assertTrue( "Date:".equals( ( (Label) metawidget.getChildren()[56] ).getText() ) );
		assertTrue( now.equals( ( (Label) metawidget.getChildren()[57] ).getText() ) );
		assertTrue( "Section Break".equals( ( (Label) ( (JPanel) metawidget.getChildren()[58] ).getChildren()[0] ).getText() ) );
		assertTrue( "Read only:".equals( ( (Label) metawidget.getChildren()[59] ).getText() ) );
		assertTrue( "Read Only".equals( ( (Label) metawidget.getChildren()[60] ).getText() ) );

		assertTrue( metawidget.getComponentCount() == 61 );

		// Test Binding.onStartBuild clears the state

		assertTrue( null != metawidget.getClientProperty( processor.getClass() ) );
		processor.onStartBuild( metawidget );
		assertTrue( null == metawidget.getClientProperty( processor.getClass() ) );
		*/
	}
}
