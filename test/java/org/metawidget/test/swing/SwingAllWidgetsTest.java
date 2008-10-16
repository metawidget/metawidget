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

package org.metawidget.test.swing;

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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import junit.framework.TestCase;

import org.apache.commons.beanutils.ConvertUtils;
import org.metawidget.MetawidgetException;
import org.metawidget.swing.Stub;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.binding.Binding;
import org.metawidget.swing.binding.beanutils.BeanUtilsBinding;
import org.metawidget.test.shared.allwidgets.model.AllWidgets;
import org.metawidget.test.shared.allwidgets.model.AllWidgets.NestedWidgets;
import org.metawidget.test.shared.allwidgets.proxy.AllWidgets$$EnhancerByCGLIB$$1234;

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

	/**
	 * JUnit 3.7 constructor.
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

		ConvertUtils.register( new org.metawidget.test.swing.allwidgets.converter.beanutils.DateConverter( DATE_FORMAT ), Date.class );
		ConvertUtils.register( new org.metawidget.test.swing.allwidgets.converter.beanutils.NestedWidgetsConverter(), NestedWidgets.class );
		runTest( BeanUtilsBinding.class );
	}

	//
	// Protected methods
	//

	protected void runTest( Class<? extends Binding> bindingClass )
	{
		// Model

		AllWidgets allWidgets = new AllWidgets$$EnhancerByCGLIB$$1234();

		// App

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setBindingClass( bindingClass );
		metawidget.setInspectorConfig( "org/metawidget/test/swing/allwidgets/inspector-config.xml" );
		metawidget.setParameter( "numberOfColumns", 2 );
		metawidget.setToInspect( allWidgets );

		Stub stub = new Stub();
		stub.setName( "mystery" );
		metawidget.add( stub );

		// Test missing components

		try
		{
			metawidget.getValue( "no-such-component" );
			assertTrue( false );
		}
		catch ( MetawidgetException e1 )
		{
			assertTrue( "No component named 'no-such-component'".equals( e1.getMessage() ) );

			try
			{
				metawidget.getValue( "textbox", "no-such-component" );
				assertTrue( false );
			}
			catch ( MetawidgetException e2 )
			{
				assertTrue( "No component named 'textbox', 'no-such-component'".equals( e2.getMessage() ) );

				try
				{
					metawidget.getValue( "textbox", "no-such-component1", "no-such-component2" );
					assertTrue( false );
				}
				catch ( MetawidgetException e3 )
				{
					assertTrue( "No such component 'no-such-component1' of 'textbox', 'no-such-component1', 'no-such-component2'".equals( e3.getMessage() ) );
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
			assertTrue( "No component named 'no-such-component'".equals( e1.getMessage() ) );

			try
			{
				metawidget.setValue( null, "textbox", "no-such-component" );
				assertTrue( false );
			}
			catch ( MetawidgetException e2 )
			{
				assertTrue( "No component named 'textbox', 'no-such-component'".equals( e2.getMessage() ) );

				try
				{
					metawidget.setValue( null, "textbox", "no-such-component1", "no-such-component2" );
					assertTrue( false );
				}
				catch ( MetawidgetException e3 )
				{
					assertTrue( "No such component 'no-such-component1' of 'textbox', 'no-such-component1', 'no-such-component2'".equals( e3.getMessage() ) );
				}
			}
		}

		// Check what created, and edit it

		assertTrue( "Textbox*:".equals( ( (JLabel) metawidget.getComponent( 0 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 1 ) instanceof JTextField );
		assertTrue( "Textbox".equals( metawidget.getValue( "textbox" ) ) );
		( (JTextField) metawidget.getComponent( 1 ) ).setText( "Textbox1" );

		assertTrue( "Limited textbox:".equals( ( (JLabel) metawidget.getComponent( 2 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 3 ) instanceof JTextField );
		assertTrue( 3 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 3 ) ).gridx );
		assertTrue( "Limited Textbox".equals( metawidget.getValue( "limitedTextbox" ) ) );
		( (JTextField) metawidget.getComponent( 3 ) ).setText( "Limited Textbox1" );

		assertTrue( "Textarea:".equals( ( (JLabel) metawidget.getComponent( 4 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 5 ) instanceof JScrollPane );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 5 ) ).gridx );
		assertTrue( "Textarea".equals( metawidget.getValue( "textarea" ) ) );

		( (JTextArea) ( (JScrollPane) metawidget.getComponent( 5 ) ).getViewport().getView() ).setText( "Textarea1" );

		assertTrue( "Password:".equals( ( (JLabel) metawidget.getComponent( 6 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 7 ) instanceof JPasswordField );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 7 ) ).gridx );
		assertTrue( "Password".equals( metawidget.getValue( "password" ) ) );
		( (JPasswordField) metawidget.getComponent( 7 ) ).setText( "Password1" );

		// Primitives

		assertTrue( "Byte:".equals( ( (JLabel) metawidget.getComponent( 8 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 9 ) instanceof JSpinner );
		assertTrue( 3 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 9 ) ).gridx );
		assertTrue( Byte.MAX_VALUE == (Byte) metawidget.getValue( "byte" ) );
		assertTrue( Byte.MIN_VALUE == (Byte) ( (SpinnerNumberModel) ( (JSpinner) metawidget.getComponent( 9 ) ).getModel() ).getMinimum() );
		assertTrue( Byte.MAX_VALUE == (Byte) ( (SpinnerNumberModel) ( (JSpinner) metawidget.getComponent( 9 ) ).getModel() ).getMaximum() );
		assertTrue( 0 == ( (JSpinner.DefaultEditor) ( (JSpinner) metawidget.getComponent( 9 ) ).getEditor() ).getTextField().getColumns() );
		JSpinner spinner = (JSpinner) metawidget.getComponent( 9 );
		spinner.setValue( spinner.getModel().getPreviousValue() );

		assertTrue( "Byte object:".equals( ( (JLabel) metawidget.getComponent( 10 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 11 ) instanceof JTextField );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 11 ) ).gridx );
		assertTrue( String.valueOf( Byte.MIN_VALUE ).equals( metawidget.getValue( "byteObject" ) ) );
		( (JTextField) metawidget.getComponent( 11 ) ).setText( String.valueOf( Byte.MIN_VALUE + 1 ) );

		assertTrue( "Short:".equals( ( (JLabel) metawidget.getComponent( 12 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 13 ) instanceof JSpinner );
		assertTrue( 3 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 13 ) ).gridx );
		assertTrue( Short.MAX_VALUE == (Short) metawidget.getValue( "short" ) );
		assertTrue( Short.MIN_VALUE == (Short) ( (SpinnerNumberModel) ( (JSpinner) metawidget.getComponent( 13 ) ).getModel() ).getMinimum() );
		assertTrue( Short.MAX_VALUE == (Short) ( (SpinnerNumberModel) ( (JSpinner) metawidget.getComponent( 13 ) ).getModel() ).getMaximum() );
		spinner = (JSpinner) metawidget.getComponent( 13 );
		assertTrue( 0 == ( (JSpinner.DefaultEditor) spinner.getEditor() ).getTextField().getColumns() );
		spinner.setValue( spinner.getModel().getPreviousValue() );

		assertTrue( "Short object:".equals( ( (JLabel) metawidget.getComponent( 14 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 15 ) instanceof JTextField );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 15 ) ).gridx );
		assertTrue( String.valueOf( Short.MIN_VALUE ).equals( metawidget.getValue( "shortObject" ) ) );
		( (JTextField) metawidget.getComponent( 15 ) ).setText( String.valueOf( Short.MIN_VALUE + 1 ) );

		assertTrue( "Int:".equals( ( (JLabel) metawidget.getComponent( 16 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 17 ) instanceof JSpinner );
		assertTrue( 3 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 17 ) ).gridx );
		assertTrue( Integer.MAX_VALUE == (Integer) metawidget.getValue( "int" ) );
		assertTrue( Integer.MIN_VALUE == (Integer) ( (SpinnerNumberModel) ( (JSpinner) metawidget.getComponent( 17 ) ).getModel() ).getMinimum() );
		assertTrue( Integer.MAX_VALUE == (Integer) ( (SpinnerNumberModel) ( (JSpinner) metawidget.getComponent( 17 ) ).getModel() ).getMaximum() );
		spinner = (JSpinner) metawidget.getComponent( 17 );
		assertTrue( 0 == ( (JSpinner.DefaultEditor) spinner.getEditor() ).getTextField().getColumns() );
		spinner.setValue( spinner.getModel().getPreviousValue() );

		assertTrue( "Integer object:".equals( ( (JLabel) metawidget.getComponent( 18 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 19 ) instanceof JTextField );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 19 ) ).gridx );
		assertTrue( String.valueOf( Integer.MIN_VALUE ).equals( metawidget.getValue( "integerObject" ) ) );
		( (JTextField) metawidget.getComponent( 19 ) ).setText( String.valueOf( Integer.MIN_VALUE + 1 ) );

		assertTrue( "Ranged int:".equals( ( (JLabel) metawidget.getComponent( 20 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 21 ) instanceof JSlider );
		assertTrue( 3 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 21 ) ).gridx );
		assertTrue( 1 == ( (JSlider) metawidget.getComponent( 21 ) ).getMinimum() );
		assertTrue( 100 == ( (JSlider) metawidget.getComponent( 21 ) ).getMaximum() );
		assertTrue( 32 == (Integer) metawidget.getValue( "rangedInt" ) );
		( (JSlider) metawidget.getComponent( 21 ) ).setValue( 33 );

		assertTrue( "Ranged integer:".equals( ( (JLabel) metawidget.getComponent( 22 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 23 ) instanceof JTextField );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 23 ) ).gridx );
		assertTrue( "33".equals( metawidget.getValue( "rangedInteger" ) ) );
		( (JTextField) metawidget.getComponent( 23 ) ).setText( String.valueOf( 34 ) );

		assertTrue( "Long:".equals( ( (JLabel) metawidget.getComponent( 24 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 25 ) instanceof JSpinner );
		assertTrue( 3 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 25 ) ).gridx );
		assertTrue( 42 == (Long) metawidget.getValue( "long" ) );
		assertTrue( Long.MIN_VALUE == (Long) ( (SpinnerNumberModel) ( (JSpinner) metawidget.getComponent( 25 ) ).getModel() ).getMinimum() );
		assertTrue( Long.MAX_VALUE == (Long) ( (SpinnerNumberModel) ( (JSpinner) metawidget.getComponent( 25 ) ).getModel() ).getMaximum() );
		spinner = (JSpinner) metawidget.getComponent( 25 );
		assertTrue( 0 == ( (JSpinner.DefaultEditor) spinner.getEditor() ).getTextField().getColumns() );
		spinner.setValue( spinner.getModel().getNextValue() );

		assertTrue( metawidget.getComponent( 26 ) instanceof JTextField );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 26 ) ).gridx );
		assertTrue( "43".equals( metawidget.getValue( "longObject" ) ) );
		( (JTextField) metawidget.getComponent( 26 ) ).setText( "44" );

		assertTrue( "Float:".equals( ( (JLabel) metawidget.getComponent( 27 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 28 ) instanceof JSpinner );
		assertTrue( 3 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 28 ) ).gridx );
		assertTrue( 4.2f == (Float) metawidget.getValue( "float" ) );
		assertTrue( -Float.MAX_VALUE == (Float) ( (SpinnerNumberModel) ( (JSpinner) metawidget.getComponent( 28 ) ).getModel() ).getMinimum() );
		assertTrue( Float.MAX_VALUE == (Float) ( (SpinnerNumberModel) ( (JSpinner) metawidget.getComponent( 28 ) ).getModel() ).getMaximum() );
		spinner = (JSpinner) metawidget.getComponent( 28 );
		assertTrue( 0 == ( (JSpinner.DefaultEditor) spinner.getEditor() ).getTextField().getColumns() );
		spinner.setValue( spinner.getModel().getNextValue() );

		assertTrue( "nullInBundle:".equals( ( (JLabel) metawidget.getComponent( 29 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 30 ) instanceof JTextField );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 30 ) ).gridx );
		assertTrue( "4.3".equals( metawidget.getValue( "floatObject" ) ) );
		( (JTextField) metawidget.getComponent( 30 ) ).setText( "5.4" );

		assertTrue( "Double:".equals( ( (JLabel) metawidget.getComponent( 31 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 32 ) instanceof JSpinner );
		assertTrue( 3 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 32 ) ).gridx );
		assertTrue( 42.2d == (Double) metawidget.getValue( "double" ) );
		assertTrue( -Double.MAX_VALUE == (Double) ( (SpinnerNumberModel) ( (JSpinner) metawidget.getComponent( 32 ) ).getModel() ).getMinimum() );
		assertTrue( Double.MAX_VALUE == (Double) ( (SpinnerNumberModel) ( (JSpinner) metawidget.getComponent( 32 ) ).getModel() ).getMaximum() );
		spinner = (JSpinner) metawidget.getComponent( 32 );
		assertTrue( 0 == ( (JSpinner.DefaultEditor) spinner.getEditor() ).getTextField().getColumns() );
		spinner.setValue( spinner.getModel().getNextValue() );

		assertTrue( metawidget.getComponent( 33 ) instanceof JTextField );
		assertTrue( 0 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 33 ) ).gridx );
		assertTrue( 2 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 33 ) ).gridwidth );
		assertTrue( "43.3".equals( metawidget.getValue( "doubleObject" ) ) );
		( (JTextField) metawidget.getComponent( 33 ) ).setText( "54.4" );

		assertTrue( "Char:".equals( ( (JLabel) metawidget.getComponent( 34 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 35 ) instanceof JTextField );
		assertTrue( 3 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 35 ) ).gridx );
		assertTrue( "A".equals( metawidget.getValue( "char" ) ) );
		( (JTextField) metawidget.getComponent( 35 ) ).setText( "Z" );

		assertTrue( "Boolean:".equals( ( (JLabel) metawidget.getComponent( 36 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 37 ) instanceof JCheckBox );
		assertTrue( false == (Boolean) metawidget.getValue( "boolean" ) );
		( (JCheckBox) metawidget.getComponent( 37 ) ).setSelected( true );

		assertTrue( "Boolean object:".equals( ( (JLabel) metawidget.getComponent( 38 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 39 ) instanceof JComboBox );
		assertTrue( 3 == ( (JComboBox) metawidget.getComponent( 39 ) ).getItemCount() );
		assertTrue( Boolean.TRUE.equals( metawidget.getValue( "booleanObject" ) ) );
		( (JComboBox) metawidget.getComponent( 39 ) ).setSelectedItem( Boolean.FALSE );

		assertTrue( "Dropdown:".equals( ( (JLabel) metawidget.getComponent( 40 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 41 ) instanceof JComboBox );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 41 ) ).gridx );
		assertTrue( 4 == ( (JComboBox) metawidget.getComponent( 41 ) ).getItemCount() );
		assertTrue( "dropdown1".equals( metawidget.getValue( "dropdown" ) ) );
		( (JComboBox) metawidget.getComponent( 41 ) ).setSelectedItem( "foo1" );

		assertTrue( "Dropdown with labels:".equals( ( (JLabel) metawidget.getComponent( 42 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 43 ) instanceof JComboBox );
		assertTrue( 3 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 43 ) ).gridx );

		JComboBox combo = (JComboBox) metawidget.getComponent( 43 );
		assertTrue( 5 == combo.getItemCount() );
		assertTrue( "Foo #2".equals( ( (JLabel) combo.getRenderer().getListCellRendererComponent( new JList(), "foo2", 1, false, false ) ).getText() ) );
		assertTrue( "Dropdown #2".equals( ( (JLabel) combo.getRenderer().getListCellRendererComponent( new JList(), "dropdown2", 1, false, false ) ).getText() ) );
		assertTrue( "Bar #2".equals( ( (JLabel) combo.getRenderer().getListCellRendererComponent( new JList(), "bar2", 1, false, false ) ).getText() ) );
		assertTrue( "Baz #2".equals( ( (JLabel) combo.getRenderer().getListCellRendererComponent( new JList(), "baz2", 1, false, false ) ).getText() ) );
		assertTrue( "dropdown2".equals( metawidget.getValue( "dropdownWithLabels" ) ) );
		( (JComboBox) metawidget.getComponent( 43 ) ).setSelectedItem( "bar2" );

		assertTrue( "Not null dropdown:".equals( ( (JLabel) metawidget.getComponent( 44 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 45 ) instanceof JComboBox );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 45 ) ).gridx );
		assertTrue( 3 == ( (JComboBox) metawidget.getComponent( 45 ) ).getItemCount() );
		assertTrue( 0 == (Byte) metawidget.getValue( "notNullDropdown" ) );
		( (JComboBox) metawidget.getComponent( 45 ) ).setSelectedItem( (byte) 1 );

		assertTrue( "Not null object dropdown*:".equals( ( (JLabel) metawidget.getComponent( 46 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 47 ) instanceof JComboBox );
		assertTrue( 3 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 47 ) ).gridx );
		assertTrue( 6 == ( (JComboBox) metawidget.getComponent( 47 ) ).getItemCount() );
		assertTrue( "dropdown3".equals( metawidget.getValue( "notNullObjectDropdown" ) ) );
		( (JComboBox) metawidget.getComponent( 47 ) ).setSelectedIndex( 0 );

		assertTrue( "Nested widgets:".equals( ( (JLabel) metawidget.getComponent( 48 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 49 ) instanceof SwingMetawidget );

		SwingMetawidget metawidgetNested = (SwingMetawidget) metawidget.getComponent( 49 );

		assertTrue( "Further nested widgets:".equals( ( (JLabel) metawidgetNested.getComponent( 0 ) ).getText() ) );
		assertTrue( 1 == ( (GridBagLayout) metawidgetNested.getLayout() ).getConstraints( metawidgetNested.getComponent( 1 ) ).gridx );

		SwingMetawidget metawidgetFurtherNested = (SwingMetawidget) metawidgetNested.getComponent( 1 );
		assertTrue( "Further nested widgets:".equals( ( (JLabel) metawidgetFurtherNested.getComponent( 0 ) ).getText() ) );
		assertTrue( metawidgetFurtherNested.getComponent( 1 ) instanceof SwingMetawidget );
		assertTrue( ( (JPanel) ( (SwingMetawidget) metawidgetFurtherNested.getComponent( 1 ) ).getComponent( 0 ) ).getComponentCount() == 0 );

		assertTrue( "Nested textbox 1:".equals( ( (JLabel) metawidgetFurtherNested.getComponent( 2 ) ).getText() ) );
		assertTrue( metawidgetFurtherNested.getComponent( 3 ) instanceof JTextField );
		assertTrue( 1 == ( (GridBagLayout) metawidgetFurtherNested.getLayout() ).getConstraints( metawidgetFurtherNested.getComponent( 3 ) ).gridx );
		assertTrue( "Nested Textbox 1".equals( metawidget.getValue( "nestedWidgets", "furtherNestedWidgets", "nestedTextbox1" ) ) );
		( (JTextField) metawidgetFurtherNested.getComponent( 3 ) ).setText( "Nested Textbox 1.1 (further)" );

		assertTrue( "Nested textbox 2:".equals( ( (JLabel) metawidgetFurtherNested.getComponent( 4 ) ).getText() ) );
		assertTrue( metawidgetFurtherNested.getComponent( 5 ) instanceof JTextField );
		assertTrue( 1 == ( (GridBagLayout) metawidgetFurtherNested.getLayout() ).getConstraints( metawidgetFurtherNested.getComponent( 5 ) ).gridx );
		assertTrue( "Nested Textbox 2".equals( metawidget.getValue( "nestedWidgets", "furtherNestedWidgets", "nestedTextbox2" ) ) );
		( (JTextField) metawidgetFurtherNested.getComponent( 5 ) ).setText( "Nested Textbox 2.2 (further)" );

		assertTrue( "Nested textbox 1:".equals( ( (JLabel) metawidgetNested.getComponent( 2 ) ).getText() ) );
		assertTrue( metawidgetNested.getComponent( 3 ) instanceof JTextField );
		assertTrue( 1 == ( (GridBagLayout) metawidgetNested.getLayout() ).getConstraints( metawidgetNested.getComponent( 3 ) ).gridx );
		assertTrue( "Nested Textbox 1".equals( metawidget.getValue( "nestedWidgets", "nestedTextbox1" ) ) );
		( (JTextField) metawidgetNested.getComponent( 3 ) ).setText( "Nested Textbox 1.1" );

		assertTrue( "Nested textbox 2:".equals( ( (JLabel) metawidgetNested.getComponent( 4 ) ).getText() ) );
		assertTrue( metawidgetNested.getComponent( 5 ) instanceof JTextField );
		assertTrue( 1 == ( (GridBagLayout) metawidgetNested.getLayout() ).getConstraints( metawidgetNested.getComponent( 5 ) ).gridx );
		assertTrue( "Nested Textbox 2".equals( metawidget.getValue( "nestedWidgets", "nestedTextbox2" ) ) );
		( (JTextField) metawidgetNested.getComponent( 5 ) ).setText( "Nested Textbox 2.2" );

		assertTrue( "Read only nested widgets:".equals( ( (JLabel) metawidget.getComponent( 50 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 51 ) instanceof SwingMetawidget );

		metawidgetNested = (SwingMetawidget) metawidget.getComponent( 51 );
		assertTrue( "Nested textbox 1:".equals( ( (JLabel) metawidgetNested.getComponent( 2 ) ).getText() ) );
		assertTrue( metawidgetNested.getComponent( 3 ) instanceof JLabel );
		assertTrue( 1 == ( (GridBagLayout) metawidgetNested.getLayout() ).getConstraints( metawidgetNested.getComponent( 3 ) ).gridx );
		assertTrue( "Nested Textbox 1".equals( metawidget.getValue( "readOnlyNestedWidgets", "nestedTextbox1" ) ) );

		assertTrue( "Nested textbox 2:".equals( ( (JLabel) metawidgetNested.getComponent( 4 ) ).getText() ) );
		assertTrue( metawidgetNested.getComponent( 5 ) instanceof JLabel );
		assertTrue( 1 == ( (GridBagLayout) metawidgetNested.getLayout() ).getConstraints( metawidgetNested.getComponent( 5 ) ).gridx );
		assertTrue( "Nested Textbox 2".equals( metawidget.getValue( "readOnlyNestedWidgets", "nestedTextbox2" ) ) );

		assertTrue( "Nested widgets dont expand:".equals( ( (JLabel) metawidget.getComponent( 52 ) ).getText() ) );
		assertTrue( "Nested Textbox 1, Nested Textbox 2".equals( ( (JTextField) metawidget.getComponent( 53 ) ).getText() ) );
		( (JTextField) metawidget.getComponent( 53 ) ).setText( "Nested Textbox 1.01, Nested Textbox 2.02" );

		assertTrue( "Read only nested widgets dont expand:".equals( ( (JLabel) metawidget.getComponent( 54 ) ).getText() ) );
		assertTrue( "Nested Textbox 1, Nested Textbox 2".equals( ( (JLabel) metawidget.getComponent( 55 ) ).getText() ) );

		assertTrue( "Date:".equals( ( (JLabel) metawidget.getComponent( 56 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 57 ) instanceof JTextField );
		assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 57 ) ).gridx );

		DateFormat dateFormat = new SimpleDateFormat( DATE_FORMAT );
		assertTrue( dateFormat.format( allWidgets.getDate() ).equals( metawidget.getValue( "date" ) ) );
		( (JTextField) metawidget.getComponent( 57 ) ).setText( "bad date" );

		assertTrue( metawidget.getComponent( 58 ) instanceof JPanel );
		assertTrue( GridBagConstraints.WEST == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 58 ) ).anchor );
		assertTrue( GridBagConstraints.HORIZONTAL == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 58 ) ).fill );
		assertTrue( GridBagConstraints.REMAINDER == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 58 ) ).gridwidth );
		assertTrue( "Section Break".equals( ((JLabel) ((JPanel) metawidget.getComponent( 58 ) ).getComponent( 0 )).getText() ) );

		assertTrue( "Read only:".equals( ( (JLabel) metawidget.getComponent( 59 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 60 ) instanceof JLabel );
		assertTrue( "Read Only".equals( metawidget.getValue( "readOnly" ) ) );

		assertTrue( metawidget.getComponent( 61 ) instanceof JButton );
		assertTrue( 3 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 61 ) ).gridx );
		assertTrue( GridBagConstraints.NONE == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 61 ) ).fill );
		assertTrue( "Do action".equals( ((JButton) metawidget.getComponent( 61 )).getText() ) );

		assertTrue( 62 == metawidget.getComponentCount() );

		// Check painting

		JFrame frame = new JFrame();
		metawidget.paint( frame.getGraphics() );

		// Check MetawidgetException

		try
		{
			metawidget.save();
			assertTrue( false );
		}
		catch ( MetawidgetException e )
		{
			assertTrue( "Could not parse 'bad date'".equals( e.getMessage() ) );
		}

		// Check saving

		String now = dateFormat.format( new GregorianCalendar().getTime() );
		( (JTextField) metawidget.getComponent( 57 ) ).setText( now );
		metawidget.save();

		// Check read-only

		metawidget.setReadOnly( true );

		assertTrue( "Textbox:".equals( ( (JLabel) metawidget.getComponent( 0 ) ).getText() ) );
		assertTrue( "Textbox1".equals( ( (JLabel) metawidget.getComponent( 1 ) ).getText() ) );
		assertTrue( "Limited textbox:".equals( ( (JLabel) metawidget.getComponent( 2 ) ).getText() ) );
		assertTrue( "Limited Textbox1".equals( ( (JLabel) metawidget.getComponent( 3 ) ).getText() ) );
		assertTrue( "Textarea:".equals( ( (JLabel) metawidget.getComponent( 4 ) ).getText() ) );
		assertTrue( "Textarea1".equals( ( (JLabel) ( (JScrollPane) metawidget.getComponent( 5 ) ).getViewport().getView() ).getText() ) );
		assertTrue( "Password:".equals( ( (JLabel) metawidget.getComponent( 6 ) ).getText() ) );
		assertTrue( metawidget.getComponent( 7 ) instanceof JPanel );
		assertTrue( "Byte:".equals( ( (JLabel) metawidget.getComponent( 8 ) ).getText() ) );
		assertTrue( "126".equals( ( (JLabel) metawidget.getComponent( 9 ) ).getText() ) );
		assertTrue( "Byte object:".equals( ( (JLabel) metawidget.getComponent( 10 ) ).getText() ) );
		assertTrue( "-127".equals( ( (JLabel) metawidget.getComponent( 11 ) ).getText() ) );
		assertTrue( "Short:".equals( ( (JLabel) metawidget.getComponent( 12 ) ).getText() ) );
		assertTrue( "32766".equals( ( (JLabel) metawidget.getComponent( 13 ) ).getText() ) );
		assertTrue( "Short object:".equals( ( (JLabel) metawidget.getComponent( 14 ) ).getText() ) );
		assertTrue( "-32767".equals( ( (JLabel) metawidget.getComponent( 15 ) ).getText() ) );
		assertTrue( "Int:".equals( ( (JLabel) metawidget.getComponent( 16 ) ).getText() ) );
		assertTrue( "2147483646".equals( ( (JLabel) metawidget.getComponent( 17 ) ).getText() ) );
		assertTrue( "Integer object:".equals( ( (JLabel) metawidget.getComponent( 18 ) ).getText() ) );
		assertTrue( "-2147483647".equals( ( (JLabel) metawidget.getComponent( 19 ) ).getText() ) );
		assertTrue( "Ranged int:".equals( ( (JLabel) metawidget.getComponent( 20 ) ).getText() ) );
		assertTrue( "33".equals( ( (JLabel) metawidget.getComponent( 21 ) ).getText() ) );
		assertTrue( "Ranged integer:".equals( ( (JLabel) metawidget.getComponent( 22 ) ).getText() ) );
		assertTrue( "34".equals( ( (JLabel) metawidget.getComponent( 23 ) ).getText() ) );
		assertTrue( "Long:".equals( ( (JLabel) metawidget.getComponent( 24 ) ).getText() ) );
		assertTrue( "43".equals( ( (JLabel) metawidget.getComponent( 25 ) ).getText() ) );
		assertTrue( "44".equals( ( (JLabel) metawidget.getComponent( 26 ) ).getText() ) );
		assertTrue( "Float:".equals( ( (JLabel) metawidget.getComponent( 27 ) ).getText() ) );
		assertTrue( ( (JLabel) metawidget.getComponent( 28 ) ).getText().startsWith( "4.3" ) || ( (JLabel) metawidget.getComponent( 28 ) ).getText().startsWith( "4.299" ) );
		assertTrue( "nullInBundle:".equals( ( (JLabel) metawidget.getComponent( 29 ) ).getText() ) );
		assertTrue( "5.4".equals( ( (JLabel) metawidget.getComponent( 30 ) ).getText() ) );
		assertTrue( "Double:".equals( ( (JLabel) metawidget.getComponent( 31 ) ).getText() ) );
		assertTrue( ( (JLabel) metawidget.getComponent( 32 ) ).getText().startsWith( "42.3" ) || ( (JLabel) metawidget.getComponent( 32 ) ).getText().startsWith( "42.299" ) );
		assertTrue( "54.4".equals( ( (JLabel) metawidget.getComponent( 33 ) ).getText() ) );
		assertTrue( "Char:".equals( ( (JLabel) metawidget.getComponent( 34 ) ).getText() ) );
		assertTrue( "Z".equals( ( (JLabel) metawidget.getComponent( 35 ) ).getText() ) );
		assertTrue( "Boolean:".equals( ( (JLabel) metawidget.getComponent( 36 ) ).getText() ) );
		assertTrue( "true".equals( ( (JLabel) metawidget.getComponent( 37 ) ).getText() ) );
		assertTrue( "Boolean object:".equals( ( (JLabel) metawidget.getComponent( 38 ) ).getText() ) );
		assertTrue( "false".equals( ( (JLabel) metawidget.getComponent( 39 ) ).getText() ) );
		assertTrue( "Dropdown:".equals( ( (JLabel) metawidget.getComponent( 40 ) ).getText() ) );
		assertTrue( "foo1".equals( ( (JLabel) metawidget.getComponent( 41 ) ).getText() ) );
		assertTrue( "Dropdown with labels:".equals( ( (JLabel) metawidget.getComponent( 42 ) ).getText() ) );
		assertTrue( "Bar #2".equals( ( (JLabel) metawidget.getComponent( 43 ) ).getText() ) );
		assertTrue( "Not null dropdown:".equals( ( (JLabel) metawidget.getComponent( 44 ) ).getText() ) );
		assertTrue( "1".equals( ( (JLabel) metawidget.getComponent( 45 ) ).getText() ) );
		assertTrue( "Not null object dropdown:".equals( ( (JLabel) metawidget.getComponent( 46 ) ).getText() ) );
		assertTrue( "foo3".equals( ( (JLabel) metawidget.getComponent( 47 ) ).getText() ) );
		assertTrue( "Nested widgets:".equals( ( (JLabel) metawidget.getComponent( 48 ) ).getText() ) );
		assertTrue( "Further nested widgets:".equals( ( (JLabel) ( (SwingMetawidget) metawidget.getComponent( 49 ) ).getComponent( 0 ) ).getText() ) );
		assertTrue( "Further nested widgets:".equals( ( (JLabel) ( (SwingMetawidget) ( (SwingMetawidget) metawidget.getComponent( 49 ) ).getComponent( 1 ) ).getComponent( 0 ) ).getText() ) );
		assertTrue( "Nested textbox 1:".equals( ( (JLabel) ( (SwingMetawidget) ( (SwingMetawidget) metawidget.getComponent( 49 ) ).getComponent( 1 ) ).getComponent( 2 ) ).getText() ) );
		assertTrue( "Nested Textbox 1.1 (further)".equals( ( (JLabel) ( (SwingMetawidget) ( (SwingMetawidget) metawidget.getComponent( 49 ) ).getComponent( 1 ) ).getComponent( 3 ) ).getText() ) );
		assertTrue( "Nested textbox 2:".equals( ( (JLabel) ( (SwingMetawidget) ( (SwingMetawidget) metawidget.getComponent( 49 ) ).getComponent( 1 ) ).getComponent( 4 ) ).getText() ) );
		assertTrue( "Nested Textbox 2.2 (further)".equals( ( (JLabel) ( (SwingMetawidget) ( (SwingMetawidget) metawidget.getComponent( 49 ) ).getComponent( 1 ) ).getComponent( 5 ) ).getText() ) );
		assertTrue( "Nested textbox 1:".equals( ( (JLabel) ( (SwingMetawidget) metawidget.getComponent( 49 ) ).getComponent( 2 ) ).getText() ) );
		assertTrue( "Nested Textbox 1.1".equals( ( (JLabel) ( (SwingMetawidget) metawidget.getComponent( 49 ) ).getComponent( 3 ) ).getText() ) );
		assertTrue( "Nested textbox 2:".equals( ( (JLabel) ( (SwingMetawidget) metawidget.getComponent( 49 ) ).getComponent( 4 ) ).getText() ) );
		assertTrue( "Nested Textbox 2.2".equals( ( (JLabel) ( (SwingMetawidget) metawidget.getComponent( 49 ) ).getComponent( 5 ) ).getText() ) );
		assertTrue( "Read only nested widgets:".equals( ( (JLabel) metawidget.getComponent( 50 ) ).getText() ) );
		assertTrue( "Further nested widgets:".equals( ( (JLabel) ( (SwingMetawidget) metawidget.getComponent( 51 ) ).getComponent( 0 ) ).getText() ) );
		assertTrue( "Nested textbox 1:".equals( ( (JLabel) ( (SwingMetawidget) metawidget.getComponent( 51 ) ).getComponent( 2 ) ).getText() ) );
		assertTrue( "Nested Textbox 1".equals( ( (JLabel) ( (SwingMetawidget) metawidget.getComponent( 51 ) ).getComponent( 3 ) ).getText() ) );
		assertTrue( "Nested textbox 2:".equals( ( (JLabel) ( (SwingMetawidget) metawidget.getComponent( 51 ) ).getComponent( 4 ) ).getText() ) );
		assertTrue( "Nested Textbox 2".equals( ( (JLabel) ( (SwingMetawidget) metawidget.getComponent( 51 ) ).getComponent( 5 ) ).getText() ) );
		assertTrue( "Nested widgets dont expand:".equals( ( (JLabel) metawidget.getComponent( 52 ) ).getText() ) );
		assertTrue( "Nested Textbox 1.01, Nested Textbox 2.02".equals( ( (JLabel) metawidget.getComponent( 53 ) ).getText() ) );
		assertTrue( "Read only nested widgets dont expand:".equals( ( (JLabel) metawidget.getComponent( 54 ) ).getText() ) );
		assertTrue( "Nested Textbox 1, Nested Textbox 2".equals( ( (JLabel) metawidget.getComponent( 55 ) ).getText() ) );
		assertTrue( "Date:".equals( ( (JLabel) metawidget.getComponent( 56 ) ).getText() ) );
		assertTrue( now.equals( ( (JLabel) metawidget.getComponent( 57 ) ).getText() ) );
		assertTrue( "Section Break".equals( ( (JLabel) ((JPanel) metawidget.getComponent( 58 )).getComponent( 0 ) ).getText() ) );
		assertTrue( "Read only:".equals( ( (JLabel) metawidget.getComponent( 59 ) ).getText() ) );
		assertTrue( "Read Only".equals( ( (JLabel) metawidget.getComponent( 60 ) ).getText() ) );

		assertTrue( metawidget.getComponentCount() == 61 );

		// Test unbind

		metawidget.setBindingClass( null );
	}
}
