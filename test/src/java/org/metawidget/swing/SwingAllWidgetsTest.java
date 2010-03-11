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

package org.metawidget.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import junit.framework.TestCase;

import org.apache.commons.beanutils.ConvertUtils;
import org.metawidget.iface.MetawidgetException;
import org.metawidget.shared.allwidgets.model.AllWidgets;
import org.metawidget.shared.allwidgets.model.AllWidgets.NestedWidgets;
import org.metawidget.shared.allwidgets.proxy.AllWidgets$$EnhancerByCGLIB$$1234;
import org.metawidget.swing.layout.GridBagLayoutConfig;
import org.metawidget.swing.layout.SeparatorLayoutDecorator;
import org.metawidget.swing.layout.SeparatorLayoutDecoratorConfig;
import org.metawidget.swing.widgetprocessor.binding.beanutils.BeanUtilsBindingProcessor;
import org.metawidget.swing.widgetprocessor.binding.reflection.ReflectionBindingProcessor;
import org.metawidget.widgetprocessor.iface.AdvancedWidgetProcessor;

/**
 * @author Richard Kennard
 */

public class SwingAllWidgetsTest
	extends TestCase
{
	//
	// Protected statics
	//

	protected final static String	DATE_FORMAT	= "E MMM dd HH:mm:ss z yyyy";

	//
	// Constructor
	//

	public SwingAllWidgetsTest()
	{
		// Default constructor
	}

	/**
	 * JUnit 3.7 constructor (SwingAllWidgetsTest gets run under JDK 1.4 using JUnit 3).
	 */

	public SwingAllWidgetsTest( String name )
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

		// BeanUtilsBinding

		ConvertUtils.register( new org.metawidget.swing.allwidgets.converter.beanutils.DateConverter( DATE_FORMAT ), Date.class );
		ConvertUtils.register( new org.metawidget.swing.allwidgets.converter.beanutils.NestedWidgetsConverter(), NestedWidgets.class );
		runTest( new BeanUtilsBindingProcessor() );
	}

	//
	// Protected methods
	//

	protected void runTest( AdvancedWidgetProcessor<JComponent, SwingMetawidget> processor )
		throws Exception
	{
		// Model

		AllWidgets allWidgets = new AllWidgets$$EnhancerByCGLIB$$1234();

		// App

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.addWidgetProcessor( processor );
		metawidget.addWidgetProcessor( new ReflectionBindingProcessor() );
		metawidget.setConfig( "org/metawidget/swing/allwidgets/metawidget.xml" );
		metawidget.setMetawidgetLayout( new SeparatorLayoutDecorator( new SeparatorLayoutDecoratorConfig().setLayout( new org.metawidget.swing.layout.GridBagLayout( new GridBagLayoutConfig().setNumberOfColumns( 2 ) ) )));

		metawidget.setToInspect( allWidgets );

		metawidget.add( new Stub( "mystery" ) );

		// Test missing components

		try
		{
			metawidget.getValue( "no-such-component" );
			assertTrue( false );
		}
		catch ( MetawidgetException e1 )
		{
			assertEquals( "No component named 'no-such-component'", e1.getMessage() );

			try
			{
				metawidget.getValue( "textbox", "no-such-component" );
				assertTrue( false );
			}
			catch ( MetawidgetException e2 )
			{
				assertEquals( "No component named 'textbox', 'no-such-component'", e2.getMessage() );

				try
				{
					metawidget.getValue( "textbox", "no-such-component1", "no-such-component2" );
					assertTrue( false );
				}
				catch ( MetawidgetException e3 )
				{
					assertEquals( "No such component 'no-such-component1' of 'textbox', 'no-such-component1', 'no-such-component2'", e3.getMessage() );
				}
			}
		}

		try
		{
			metawidget.setValue( null, "no-such-component" );
			assertTrue( false );
		}
		catch ( MetawidgetException e1 )
		{
			assertEquals( "No component named 'no-such-component'", e1.getMessage() );

			try
			{
				metawidget.setValue( null, "textbox", "no-such-component" );
				assertTrue( false );
			}
			catch ( MetawidgetException e2 )
			{
				assertEquals( "No component named 'textbox', 'no-such-component'", e2.getMessage() );

				try
				{
					metawidget.setValue( null, "textbox", "no-such-component1", "no-such-component2" );
					assertTrue( false );
				}
				catch ( MetawidgetException e3 )
				{
					assertEquals( "No such component 'no-such-component1' of 'textbox', 'no-such-component1', 'no-such-component2'", e3.getMessage() );
				}
			}
		}

		// Check what created, and edit it

		assertEquals( "Textbox*:", ( (JLabel) metawidget.getComponent( 0 ) ).getText() );
		assertTrue( metawidget.getComponent( 1 ) instanceof JTextField );
		assertEquals( "Textbox", metawidget.getValue( "textbox" ) );
		( (JTextField) metawidget.getComponent( 1 ) ).setText( "Textbox1" );

		assertEquals( "Limited textbox:", ( (JLabel) metawidget.getComponent( 2 ) ).getText() );
		assertTrue( metawidget.getComponent( 3 ) instanceof JTextField );
		assertTrue( 3 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 3 ) ).gridx );
		assertEquals( "Limited Textbox", metawidget.getValue( "limitedTextbox" ) );
		( (JTextField) metawidget.getComponent( 3 ) ).setText( "Limited Textbox1" );

		assertEquals( "Textarea:", ( (JLabel) metawidget.getComponent( 4 ) ).getText() );
		assertTrue( metawidget.getComponent( 5 ) instanceof JScrollPane );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 5 ) ).gridx );
		assertEquals( "Textarea", metawidget.getValue( "textarea" ) );

		JTextArea textarea = (JTextArea) ( (JScrollPane) metawidget.getComponent( 5 ) ).getViewport().getView();
		assertTrue( 2 == textarea.getRows() );
		assertTrue( true == textarea.getLineWrap() );
		assertTrue( true == textarea.getWrapStyleWord() );
		textarea.setText( "Textarea1" );

		assertEquals( "Password:", ( (JLabel) metawidget.getComponent( 6 ) ).getText() );
		assertTrue( metawidget.getComponent( 7 ) instanceof JPasswordField );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 7 ) ).gridx );
		assertEquals( "Password", metawidget.getValue( "password" ) );
		( (JPasswordField) metawidget.getComponent( 7 ) ).setText( "Password1" );

		// Primitives

		assertEquals( "Byte:", ( (JLabel) metawidget.getComponent( 8 ) ).getText() );
		assertTrue( metawidget.getComponent( 9 ) instanceof JSpinner );
		assertTrue( 3 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 9 ) ).gridx );
		assertTrue( Byte.MAX_VALUE == (Byte) metawidget.getValue( "byte" ) );
		assertTrue( -5 == (Byte) ( (SpinnerNumberModel) ( (JSpinner) metawidget.getComponent( 9 ) ).getModel() ).getMinimum() );
		assertTrue( Byte.MAX_VALUE == (Byte) ( (SpinnerNumberModel) ( (JSpinner) metawidget.getComponent( 9 ) ).getModel() ).getMaximum() );
		assertTrue( 0 == ( (JSpinner.DefaultEditor) ( (JSpinner) metawidget.getComponent( 9 ) ).getEditor() ).getTextField().getColumns() );
		JSpinner spinner = (JSpinner) metawidget.getComponent( 9 );
		spinner.setValue( spinner.getModel().getPreviousValue() );

		assertEquals( "Byte object:", ( (JLabel) metawidget.getComponent( 10 ) ).getText() );
		assertTrue( metawidget.getComponent( 11 ) instanceof JTextField );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 11 ) ).gridx );
		assertEquals( String.valueOf( Byte.MIN_VALUE ), metawidget.getValue( "byteObject" ) );
		( (JTextField) metawidget.getComponent( 11 ) ).setText( String.valueOf( Byte.MIN_VALUE + 1 ) );

		assertEquals( "Short:", ( (JLabel) metawidget.getComponent( 12 ) ).getText() );
		assertTrue( metawidget.getComponent( 13 ) instanceof JSpinner );
		assertTrue( 3 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 13 ) ).gridx );
		assertTrue( Short.MAX_VALUE == (Short) metawidget.getValue( "short" ) );
		assertTrue( -6 == (Short) ( (SpinnerNumberModel) ( (JSpinner) metawidget.getComponent( 13 ) ).getModel() ).getMinimum() );
		assertTrue( Short.MAX_VALUE == (Short) ( (SpinnerNumberModel) ( (JSpinner) metawidget.getComponent( 13 ) ).getModel() ).getMaximum() );
		spinner = (JSpinner) metawidget.getComponent( 13 );
		assertTrue( 0 == ( (JSpinner.DefaultEditor) spinner.getEditor() ).getTextField().getColumns() );
		spinner.setValue( spinner.getModel().getPreviousValue() );

		assertEquals( "Short object:", ( (JLabel) metawidget.getComponent( 14 ) ).getText() );
		assertTrue( metawidget.getComponent( 15 ) instanceof JTextField );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 15 ) ).gridx );
		assertEquals( String.valueOf( Short.MIN_VALUE ), metawidget.getValue( "shortObject" ) );
		( (JTextField) metawidget.getComponent( 15 ) ).setText( String.valueOf( Short.MIN_VALUE + 1 ) );

		assertEquals( "Int:", ( (JLabel) metawidget.getComponent( 16 ) ).getText() );
		assertTrue( metawidget.getComponent( 17 ) instanceof JSpinner );
		assertTrue( 3 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 17 ) ).gridx );
		assertTrue( Integer.MAX_VALUE == (Integer) metawidget.getValue( "int" ) );
		assertTrue( Integer.MIN_VALUE == (Integer) ( (SpinnerNumberModel) ( (JSpinner) metawidget.getComponent( 17 ) ).getModel() ).getMinimum() );
		assertTrue( Integer.MAX_VALUE == (Integer) ( (SpinnerNumberModel) ( (JSpinner) metawidget.getComponent( 17 ) ).getModel() ).getMaximum() );
		spinner = (JSpinner) metawidget.getComponent( 17 );
		assertTrue( 0 == ( (JSpinner.DefaultEditor) spinner.getEditor() ).getTextField().getColumns() );
		spinner.setValue( spinner.getModel().getPreviousValue() );

		assertEquals( "Integer object:", ( (JLabel) metawidget.getComponent( 18 ) ).getText() );
		assertTrue( metawidget.getComponent( 19 ) instanceof JTextField );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 19 ) ).gridx );
		assertEquals( String.valueOf( Integer.MIN_VALUE ), metawidget.getValue( "integerObject" ) );
		( (JTextField) metawidget.getComponent( 19 ) ).setText( String.valueOf( Integer.MIN_VALUE + 1 ) );

		assertEquals( "Ranged int:", ( (JLabel) metawidget.getComponent( 20 ) ).getText() );
		assertTrue( metawidget.getComponent( 21 ) instanceof JSlider );
		assertTrue( 3 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 21 ) ).gridx );
		assertTrue( 1 == ( (JSlider) metawidget.getComponent( 21 ) ).getMinimum() );
		assertTrue( 100 == ( (JSlider) metawidget.getComponent( 21 ) ).getMaximum() );
		assertTrue( 32 == (Integer) metawidget.getValue( "rangedInt" ) );
		( (JSlider) metawidget.getComponent( 21 ) ).setValue( 33 );

		assertEquals( "Ranged integer:", ( (JLabel) metawidget.getComponent( 22 ) ).getText() );
		assertTrue( metawidget.getComponent( 23 ) instanceof JTextField );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 23 ) ).gridx );
		assertEquals( "33", metawidget.getValue( "rangedInteger" ) );
		( (JTextField) metawidget.getComponent( 23 ) ).setText( String.valueOf( 34 ) );

		assertEquals( "Long:", ( (JLabel) metawidget.getComponent( 24 ) ).getText() );
		assertTrue( metawidget.getComponent( 25 ) instanceof JSpinner );
		assertTrue( 3 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 25 ) ).gridx );
		assertTrue( 42 == (Long) metawidget.getValue( "long" ) );
		assertTrue( -7 == (Long) ( (SpinnerNumberModel) ( (JSpinner) metawidget.getComponent( 25 ) ).getModel() ).getMinimum() );
		assertTrue( Long.MAX_VALUE == (Long) ( (SpinnerNumberModel) ( (JSpinner) metawidget.getComponent( 25 ) ).getModel() ).getMaximum() );
		spinner = (JSpinner) metawidget.getComponent( 25 );
		assertTrue( 0 == ( (JSpinner.DefaultEditor) spinner.getEditor() ).getTextField().getColumns() );
		spinner.setValue( spinner.getModel().getNextValue() );

		assertTrue( metawidget.getComponent( 26 ) instanceof JTextField );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 26 ) ).gridx );
		assertEquals( "43", metawidget.getValue( "longObject" ) );
		( (JTextField) metawidget.getComponent( 26 ) ).setText( "44" );

		assertEquals( "Float:", ( (JLabel) metawidget.getComponent( 27 ) ).getText() );
		assertTrue( metawidget.getComponent( 28 ) instanceof JSpinner );
		assertTrue( 3 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 28 ) ).gridx );
		assertTrue( 4.2f == (Float) metawidget.getValue( "float" ) );
		assertTrue( -Float.MAX_VALUE == (Float) ( (SpinnerNumberModel) ( (JSpinner) metawidget.getComponent( 28 ) ).getModel() ).getMinimum() );
		assertTrue( 2048 == (Float) ( (SpinnerNumberModel) ( (JSpinner) metawidget.getComponent( 28 ) ).getModel() ).getMaximum() );
		spinner = (JSpinner) metawidget.getComponent( 28 );
		assertTrue( 0 == ( (JSpinner.DefaultEditor) spinner.getEditor() ).getTextField().getColumns() );
		spinner.setValue( spinner.getModel().getNextValue() );

		assertEquals( "nullInBundle:", ( (JLabel) metawidget.getComponent( 29 ) ).getText() );
		assertTrue( metawidget.getComponent( 30 ) instanceof JTextField );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 30 ) ).gridx );
		assertEquals( "4.3", metawidget.getValue( "floatObject" ) );
		( (JTextField) metawidget.getComponent( 30 ) ).setText( "5.4" );

		assertEquals( "Double:", ( (JLabel) metawidget.getComponent( 31 ) ).getText() );
		assertTrue( metawidget.getComponent( 32 ) instanceof JSpinner );
		assertTrue( 3 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 32 ) ).gridx );
		assertTrue( 42.2d == (Double) metawidget.getValue( "double" ) );
		assertTrue( -8 == (Double) ( (SpinnerNumberModel) ( (JSpinner) metawidget.getComponent( 32 ) ).getModel() ).getMinimum() );
		assertTrue( Double.MAX_VALUE == (Double) ( (SpinnerNumberModel) ( (JSpinner) metawidget.getComponent( 32 ) ).getModel() ).getMaximum() );
		spinner = (JSpinner) metawidget.getComponent( 32 );
		assertTrue( 0 == ( (JSpinner.DefaultEditor) spinner.getEditor() ).getTextField().getColumns() );
		spinner.setValue( spinner.getModel().getNextValue() );

		assertTrue( metawidget.getComponent( 33 ) instanceof JTextField );
		assertTrue( 0 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 33 ) ).gridx );
		assertTrue( 2 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 33 ) ).gridwidth );
		assertEquals( "43.3", metawidget.getValue( "doubleObject" ) );
		( (JTextField) metawidget.getComponent( 33 ) ).setText( "54.4" );

		assertEquals( "Char:", ( (JLabel) metawidget.getComponent( 34 ) ).getText() );
		assertTrue( metawidget.getComponent( 35 ) instanceof JTextField );
		assertTrue( 3 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 35 ) ).gridx );
		assertEquals( "A", metawidget.getValue( "char" ) );
		( (JTextField) metawidget.getComponent( 35 ) ).setText( "Z" );

		assertEquals( "Boolean:", ( (JLabel) metawidget.getComponent( 36 ) ).getText() );
		assertTrue( metawidget.getComponent( 37 ) instanceof JCheckBox );
		assertTrue( false == (Boolean) metawidget.getValue( "boolean" ) );
		( (JCheckBox) metawidget.getComponent( 37 ) ).setSelected( true );

		assertEquals( "Boolean object:", ( (JLabel) metawidget.getComponent( 38 ) ).getText() );
		assertTrue( metawidget.getComponent( 39 ) instanceof JComboBox );
		assertTrue( 3 == ( (JComboBox) metawidget.getComponent( 39 ) ).getItemCount() );
		assertEquals( Boolean.TRUE, metawidget.getValue( "booleanObject" ) );
		( (JComboBox) metawidget.getComponent( 39 ) ).setSelectedItem( Boolean.FALSE );

		assertEquals( "Dropdown:", ( (JLabel) metawidget.getComponent( 40 ) ).getText() );
		assertTrue( metawidget.getComponent( 41 ) instanceof JComboBox );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 41 ) ).gridx );
		assertTrue( 4 == ( (JComboBox) metawidget.getComponent( 41 ) ).getItemCount() );
		assertEquals( "dropdown1", metawidget.getValue( "dropdown" ) );
		( (JComboBox) metawidget.getComponent( 41 ) ).setSelectedItem( "foo1" );

		assertEquals( "Dropdown with labels:", ( (JLabel) metawidget.getComponent( 42 ) ).getText() );
		assertTrue( metawidget.getComponent( 43 ) instanceof JComboBox );
		assertTrue( 3 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 43 ) ).gridx );

		JComboBox combo = (JComboBox) metawidget.getComponent( 43 );
		assertTrue( 5 == combo.getItemCount() );
		assertEquals( "Foo #2", ( (JLabel) combo.getRenderer().getListCellRendererComponent( new JList(), "foo2", 1, false, false ) ).getText() );
		assertEquals( "Dropdown #2", ( (JLabel) combo.getRenderer().getListCellRendererComponent( new JList(), "dropdown2", 1, false, false ) ).getText() );
		assertEquals( "Bar #2", ( (JLabel) combo.getRenderer().getListCellRendererComponent( new JList(), "bar2", 1, false, false ) ).getText() );
		assertEquals( "Baz #2", ( (JLabel) combo.getRenderer().getListCellRendererComponent( new JList(), "baz2", 1, false, false ) ).getText() );
		assertEquals( "dropdown2", metawidget.getValue( "dropdownWithLabels" ) );
		( (JComboBox) metawidget.getComponent( 43 ) ).setSelectedItem( "bar2" );

		assertEquals( "Not null dropdown:", ( (JLabel) metawidget.getComponent( 44 ) ).getText() );
		assertTrue( metawidget.getComponent( 45 ) instanceof JComboBox );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 45 ) ).gridx );
		assertTrue( 3 == ( (JComboBox) metawidget.getComponent( 45 ) ).getItemCount() );
		assertTrue( 0 == (Byte) metawidget.getValue( "notNullDropdown" ) );
		( (JComboBox) metawidget.getComponent( 45 ) ).setSelectedItem( (byte) 1 );

		assertEquals( "Not null object dropdown*:", ( (JLabel) metawidget.getComponent( 46 ) ).getText() );
		assertTrue( metawidget.getComponent( 47 ) instanceof JComboBox );
		assertTrue( 3 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 47 ) ).gridx );
		assertTrue( 6 == ( (JComboBox) metawidget.getComponent( 47 ) ).getItemCount() );
		assertEquals( "dropdown3", metawidget.getValue( "notNullObjectDropdown" ) );
		( (JComboBox) metawidget.getComponent( 47 ) ).setSelectedIndex( 0 );

		assertEquals( "Nested widgets:", ( (JLabel) metawidget.getComponent( 48 ) ).getText() );
		assertTrue( metawidget.getComponent( 49 ) instanceof SwingMetawidget );

		SwingMetawidget metawidgetNested = (SwingMetawidget) metawidget.getComponent( 49 );

		assertEquals( "Further nested widgets:", ( (JLabel) metawidgetNested.getComponent( 0 ) ).getText() );
		assertTrue( 1 == ( (GridBagLayout) metawidgetNested.getLayout() ).getConstraints( metawidgetNested.getComponent( 1 ) ).gridx );

		SwingMetawidget metawidgetFurtherNested = (SwingMetawidget) metawidgetNested.getComponent( 1 );
		assertEquals( "Further nested widgets:", ( (JLabel) metawidgetFurtherNested.getComponent( 0 ) ).getText() );
		assertTrue( metawidgetFurtherNested.getComponent( 1 ) instanceof SwingMetawidget );
		assertTrue( ( (JPanel) ( (SwingMetawidget) metawidgetFurtherNested.getComponent( 1 ) ).getComponent( 0 ) ).getComponentCount() == 0 );

		assertEquals( "Nested textbox 1:", ( (JLabel) metawidgetFurtherNested.getComponent( 2 ) ).getText() );
		assertTrue( metawidgetFurtherNested.getComponent( 3 ) instanceof JTextField );
		assertTrue( 1 == ( (GridBagLayout) metawidgetFurtherNested.getLayout() ).getConstraints( metawidgetFurtherNested.getComponent( 3 ) ).gridx );
		assertEquals( "Nested Textbox 1", metawidget.getValue( "nestedWidgets", "furtherNestedWidgets", "nestedTextbox1" ) );
		( (JTextField) metawidgetFurtherNested.getComponent( 3 ) ).setText( "Nested Textbox 1.1 (further)" );

		assertEquals( "Nested textbox 2:", ( (JLabel) metawidgetFurtherNested.getComponent( 4 ) ).getText() );
		assertTrue( metawidgetFurtherNested.getComponent( 5 ) instanceof JTextField );

		// (should be 1, as in next row, if getEffectiveNumberOfColumns is working)

		assertTrue( 1 == ( (GridBagLayout) metawidgetFurtherNested.getLayout() ).getConstraints( metawidgetFurtherNested.getComponent( 5 ) ).gridx );
		assertEquals( "Nested Textbox 2", metawidget.getValue( "nestedWidgets", "furtherNestedWidgets", "nestedTextbox2" ) );
		( (JTextField) metawidgetFurtherNested.getComponent( 5 ) ).setText( "Nested Textbox 2.2 (further)" );

		assertEquals( "Nested textbox 1:", ( (JLabel) metawidgetNested.getComponent( 2 ) ).getText() );
		assertTrue( metawidgetNested.getComponent( 3 ) instanceof JTextField );
		assertTrue( 1 == ( (GridBagLayout) metawidgetNested.getLayout() ).getConstraints( metawidgetNested.getComponent( 3 ) ).gridx );
		assertEquals( "Nested Textbox 1", metawidget.getValue( "nestedWidgets", "nestedTextbox1" ) );
		( (JTextField) metawidgetNested.getComponent( 3 ) ).setText( "Nested Textbox 1.1" );

		assertEquals( "Nested textbox 2:", ( (JLabel) metawidgetNested.getComponent( 4 ) ).getText() );
		assertTrue( metawidgetNested.getComponent( 5 ) instanceof JTextField );
		assertTrue( 1 == ( (GridBagLayout) metawidgetNested.getLayout() ).getConstraints( metawidgetNested.getComponent( 5 ) ).gridx );
		assertEquals( "Nested Textbox 2", metawidget.getValue( "nestedWidgets", "nestedTextbox2" ) );
		( (JTextField) metawidgetNested.getComponent( 5 ) ).setText( "Nested Textbox 2.2" );

		assertEquals( "Read only nested widgets:", ( (JLabel) metawidget.getComponent( 50 ) ).getText() );
		assertTrue( metawidget.getComponent( 51 ) instanceof SwingMetawidget );

		metawidgetNested = (SwingMetawidget) metawidget.getComponent( 51 );
		assertEquals( "Nested textbox 1:", ( (JLabel) metawidgetNested.getComponent( 2 ) ).getText() );
		assertTrue( metawidgetNested.getComponent( 3 ) instanceof JLabel );
		assertTrue( 1 == ( (GridBagLayout) metawidgetNested.getLayout() ).getConstraints( metawidgetNested.getComponent( 3 ) ).gridx );
		assertEquals( "Nested Textbox 1", metawidget.getValue( "readOnlyNestedWidgets", "nestedTextbox1" ) );

		assertEquals( "Nested textbox 2:", ( (JLabel) metawidgetNested.getComponent( 4 ) ).getText() );
		assertTrue( metawidgetNested.getComponent( 5 ) instanceof JLabel );
		assertTrue( 1 == ( (GridBagLayout) metawidgetNested.getLayout() ).getConstraints( metawidgetNested.getComponent( 5 ) ).gridx );
		assertEquals( "Nested Textbox 2", metawidget.getValue( "readOnlyNestedWidgets", "nestedTextbox2" ) );

		assertEquals( "Nested widgets dont expand:", ( (JLabel) metawidget.getComponent( 52 ) ).getText() );
		assertEquals( "Nested Textbox 1, Nested Textbox 2", ( (JTextField) metawidget.getComponent( 53 ) ).getText() );
		( (JTextField) metawidget.getComponent( 53 ) ).setText( "Nested Textbox 1.01, Nested Textbox 2.02" );

		assertEquals( "Read only nested widgets dont expand:", ( (JLabel) metawidget.getComponent( 54 ) ).getText() );
		assertEquals( "Nested Textbox 1, Nested Textbox 2", ( (JLabel) metawidget.getComponent( 55 ) ).getText() );

		assertEquals( "Date:", ( (JLabel) metawidget.getComponent( 56 ) ).getText() );
		assertTrue( metawidget.getComponent( 57 ) instanceof JTextField );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 57 ) ).gridx );

		DateFormat dateFormat = new SimpleDateFormat( DATE_FORMAT );
		assertEquals( dateFormat.format( allWidgets.getDate() ), metawidget.getValue( "date" ) );
		( (JTextField) metawidget.getComponent( 57 ) ).setText( "bad date" );

		JPanel separatorPanel = (JPanel) metawidget.getComponent( 58 );
		assertEquals( "Section Break", ((JLabel) separatorPanel.getComponent( 0 )).getText() );
		assertTrue( GridBagConstraints.REMAINDER == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 58 ) ).gridwidth );
		assertTrue( separatorPanel.getComponent( 1 ) instanceof JSeparator );

		assertEquals( "Read only:", ( (JLabel) metawidget.getComponent( 59 ) ).getText() );
		assertTrue( metawidget.getComponent( 60 ) instanceof JLabel );
		assertEquals( "Read Only", metawidget.getValue( "readOnly" ) );

		assertTrue( metawidget.getComponent( 61 ) instanceof JButton );
		assertTrue( 3 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 61 ) ).gridx );
		assertTrue( GridBagConstraints.NONE == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 61 ) ).fill );
		JButton button = ( (JButton) metawidget.getComponent( 61 ) );
		assertEquals( "Do action", button.getText() );
		try
		{
			button.doClick();
			assertTrue( false );
		}
		catch ( Exception e )
		{
			assertEquals( "doAction called", e.getCause().getCause().getMessage() );
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
			assertEquals( "Could not parse 'bad date'", e.getCause().getCause().getMessage() );
		}

		// Check saving

		String now = dateFormat.format( new GregorianCalendar().getTime() );
		( (JTextField) metawidget.getComponent( 57 ) ).setText( now );
		processor.getClass().getMethod( "save", SwingMetawidget.class ).invoke( processor, metawidget );

		// Check read-only

		metawidget.setReadOnly( true );

		assertEquals( "Textbox:", ( (JLabel) metawidget.getComponent( 0 ) ).getText() );
		assertEquals( "Textbox1", ( (JLabel) metawidget.getComponent( 1 ) ).getText() );
		assertEquals( "Limited textbox:", ( (JLabel) metawidget.getComponent( 2 ) ).getText() );
		assertEquals( "Limited Textbox1", ( (JLabel) metawidget.getComponent( 3 ) ).getText() );
		assertEquals( "Textarea:", ( (JLabel) metawidget.getComponent( 4 ) ).getText() );
		assertEquals( "Textarea1", ( (JTextArea) ( (JScrollPane) metawidget.getComponent( 5 ) ).getViewport().getView() ).getText() );
		assertEquals( "Password:", ( (JLabel) metawidget.getComponent( 6 ) ).getText() );
		assertTrue( metawidget.getComponent( 7 ) instanceof JPanel );
		assertEquals( "Byte:", ( (JLabel) metawidget.getComponent( 8 ) ).getText() );
		assertEquals( "126", ( (JLabel) metawidget.getComponent( 9 ) ).getText() );
		assertEquals( "Byte object:", ( (JLabel) metawidget.getComponent( 10 ) ).getText() );
		assertEquals( "-127", ( (JLabel) metawidget.getComponent( 11 ) ).getText() );
		assertEquals( "Short:", ( (JLabel) metawidget.getComponent( 12 ) ).getText() );
		assertEquals( "32766", ( (JLabel) metawidget.getComponent( 13 ) ).getText() );
		assertEquals( "Short object:", ( (JLabel) metawidget.getComponent( 14 ) ).getText() );
		assertEquals( "-32767", ( (JLabel) metawidget.getComponent( 15 ) ).getText() );
		assertEquals( "Int:", ( (JLabel) metawidget.getComponent( 16 ) ).getText() );
		assertEquals( "2147483646", ( (JLabel) metawidget.getComponent( 17 ) ).getText() );
		assertEquals( "Integer object:", ( (JLabel) metawidget.getComponent( 18 ) ).getText() );
		assertEquals( "-2147483647", ( (JLabel) metawidget.getComponent( 19 ) ).getText() );
		assertEquals( "Ranged int:", ( (JLabel) metawidget.getComponent( 20 ) ).getText() );
		assertEquals( "33", ( (JLabel) metawidget.getComponent( 21 ) ).getText() );
		assertEquals( "Ranged integer:", ( (JLabel) metawidget.getComponent( 22 ) ).getText() );
		assertEquals( "34", ( (JLabel) metawidget.getComponent( 23 ) ).getText() );
		assertEquals( "Long:", ( (JLabel) metawidget.getComponent( 24 ) ).getText() );
		assertEquals( "43", ( (JLabel) metawidget.getComponent( 25 ) ).getText() );
		assertEquals( "44", ( (JLabel) metawidget.getComponent( 26 ) ).getText() );
		assertEquals( "Float:", ( (JLabel) metawidget.getComponent( 27 ) ).getText() );
		assertTrue( ( (JLabel) metawidget.getComponent( 28 ) ).getText().startsWith( "4.3" ) || ( (JLabel) metawidget.getComponent( 28 ) ).getText().startsWith( "4.299" ) );
		assertEquals( "nullInBundle:", ( (JLabel) metawidget.getComponent( 29 ) ).getText() );
		assertEquals( "5.4", ( (JLabel) metawidget.getComponent( 30 ) ).getText() );
		assertEquals( "Double:", ( (JLabel) metawidget.getComponent( 31 ) ).getText() );
		assertTrue( ( (JLabel) metawidget.getComponent( 32 ) ).getText().startsWith( "42.3" ) || ( (JLabel) metawidget.getComponent( 32 ) ).getText().startsWith( "42.299" ) );
		assertEquals( "54.4", ( (JLabel) metawidget.getComponent( 33 ) ).getText() );
		assertEquals( "Char:", ( (JLabel) metawidget.getComponent( 34 ) ).getText() );
		assertEquals( "Z", ( (JLabel) metawidget.getComponent( 35 ) ).getText() );
		assertEquals( "Boolean:", ( (JLabel) metawidget.getComponent( 36 ) ).getText() );
		assertEquals( "true", ( (JLabel) metawidget.getComponent( 37 ) ).getText() );
		assertEquals( "Boolean object:", ( (JLabel) metawidget.getComponent( 38 ) ).getText() );
		assertEquals( "No", ( (JLabel) metawidget.getComponent( 39 ) ).getText() );
		assertEquals( "Dropdown:", ( (JLabel) metawidget.getComponent( 40 ) ).getText() );
		assertEquals( "foo1", ( (JLabel) metawidget.getComponent( 41 ) ).getText() );
		assertEquals( "Dropdown with labels:", ( (JLabel) metawidget.getComponent( 42 ) ).getText() );
		assertEquals( "Bar #2", ( (JLabel) metawidget.getComponent( 43 ) ).getText() );
		assertEquals( "Not null dropdown:", ( (JLabel) metawidget.getComponent( 44 ) ).getText() );
		assertEquals( "1", ( (JLabel) metawidget.getComponent( 45 ) ).getText() );
		assertEquals( "Not null object dropdown:", ( (JLabel) metawidget.getComponent( 46 ) ).getText() );
		assertEquals( "foo3", ( (JLabel) metawidget.getComponent( 47 ) ).getText() );
		assertEquals( "Nested widgets:", ( (JLabel) metawidget.getComponent( 48 ) ).getText() );
		assertEquals( "Further nested widgets:", ( (JLabel) ( (SwingMetawidget) metawidget.getComponent( 49 ) ).getComponent( 0 ) ).getText() );
		assertEquals( "Further nested widgets:", ( (JLabel) ( (SwingMetawidget) ( (SwingMetawidget) metawidget.getComponent( 49 ) ).getComponent( 1 ) ).getComponent( 0 ) ).getText() );
		assertEquals( "Nested textbox 1:", ( (JLabel) ( (SwingMetawidget) ( (SwingMetawidget) metawidget.getComponent( 49 ) ).getComponent( 1 ) ).getComponent( 2 ) ).getText() );
		assertEquals( "Nested Textbox 1.1 (further)", ( (JLabel) ( (SwingMetawidget) ( (SwingMetawidget) metawidget.getComponent( 49 ) ).getComponent( 1 ) ).getComponent( 3 ) ).getText() );
		assertEquals( "Nested textbox 2:", ( (JLabel) ( (SwingMetawidget) ( (SwingMetawidget) metawidget.getComponent( 49 ) ).getComponent( 1 ) ).getComponent( 4 ) ).getText() );
		assertEquals( "Nested Textbox 2.2 (further)", ( (JLabel) ( (SwingMetawidget) ( (SwingMetawidget) metawidget.getComponent( 49 ) ).getComponent( 1 ) ).getComponent( 5 ) ).getText() );
		assertEquals( "Nested textbox 1:", ( (JLabel) ( (SwingMetawidget) metawidget.getComponent( 49 ) ).getComponent( 2 ) ).getText() );
		assertEquals( "Nested Textbox 1.1", ( (JLabel) ( (SwingMetawidget) metawidget.getComponent( 49 ) ).getComponent( 3 ) ).getText() );
		assertEquals( "Nested textbox 2:", ( (JLabel) ( (SwingMetawidget) metawidget.getComponent( 49 ) ).getComponent( 4 ) ).getText() );
		assertEquals( "Nested Textbox 2.2", ( (JLabel) ( (SwingMetawidget) metawidget.getComponent( 49 ) ).getComponent( 5 ) ).getText() );
		assertEquals( "Read only nested widgets:", ( (JLabel) metawidget.getComponent( 50 ) ).getText() );
		assertEquals( "Further nested widgets:", ( (JLabel) ( (SwingMetawidget) metawidget.getComponent( 51 ) ).getComponent( 0 ) ).getText() );
		assertEquals( "Nested textbox 1:", ( (JLabel) ( (SwingMetawidget) metawidget.getComponent( 51 ) ).getComponent( 2 ) ).getText() );
		assertEquals( "Nested Textbox 1", ( (JLabel) ( (SwingMetawidget) metawidget.getComponent( 51 ) ).getComponent( 3 ) ).getText() );
		assertEquals( "Nested textbox 2:", ( (JLabel) ( (SwingMetawidget) metawidget.getComponent( 51 ) ).getComponent( 4 ) ).getText() );
		assertEquals( "Nested Textbox 2", ( (JLabel) ( (SwingMetawidget) metawidget.getComponent( 51 ) ).getComponent( 5 ) ).getText() );
		assertEquals( "Nested widgets dont expand:", ( (JLabel) metawidget.getComponent( 52 ) ).getText() );
		assertEquals( "Nested Textbox 1.01, Nested Textbox 2.02", ( (JLabel) metawidget.getComponent( 53 ) ).getText() );
		assertEquals( "Read only nested widgets dont expand:", ( (JLabel) metawidget.getComponent( 54 ) ).getText() );
		assertEquals( "Nested Textbox 1, Nested Textbox 2", ( (JLabel) metawidget.getComponent( 55 ) ).getText() );
		assertEquals( "Date:", ( (JLabel) metawidget.getComponent( 56 ) ).getText() );
		assertEquals( now, ( (JLabel) metawidget.getComponent( 57 ) ).getText() );
		assertEquals( "Section Break", ( (JLabel) ( (JPanel) metawidget.getComponent( 58 ) ).getComponent( 0 ) ).getText() );
		assertEquals( "Read only:", ( (JLabel) metawidget.getComponent( 59 ) ).getText() );
		assertEquals( "Read Only", ( (JLabel) metawidget.getComponent( 60 ) ).getText() );

		assertTrue( metawidget.getComponentCount() == 61 );

		// Test Binding.onStartBuild clears the state

		assertTrue( null != metawidget.getClientProperty( processor.getClass() ) );
		processor.onStartBuild( metawidget );
		assertTrue( null == metawidget.getClientProperty( processor.getClass() ) );
	}
}
