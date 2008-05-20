package org.metawidget.test.gwt.allwidgets.client;

import org.metawidget.gwt.client.ui.Facet;
import org.metawidget.gwt.client.ui.GwtMetawidget;
import org.metawidget.test.gwt.allwidgets.client.ui.AllWidgetsModule;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

public class GwtAllWidgetsTest
	extends GWTTestCase
{
	//
	//
	// Private statics
	//
	//

	private final static int	TIMER_SCHEDULE_DELAY	= 2000;

	private final static int	TEST_FINISH_DELAY		= 50 * TIMER_SCHEDULE_DELAY;

	//
	//
	// Public methods
	//
	//

	@Override
	public String getModuleName()
	{
		return "org.metawidget.test.gwt.allwidgets.GwtAllWidgetsTest";
	}

	public void testAllWidgets()
		throws Exception
	{
		// Start app

		final FlowPanel panel = new FlowPanel();
		final AllWidgetsModule allWidgetsModule = new AllWidgetsModule( panel );
		allWidgetsModule.onModuleLoad();

		Timer timerLoad = new Timer()
		{
			@Override
			public void run()
			{
				// Test fields

				final GwtMetawidget metawidget = (GwtMetawidget) panel.getWidget( 0 );
				FlexTable flexTable = (FlexTable) metawidget.getWidget( 0 );

				// Check what created, and edit it

				//assertTrue( "Textbox:".equals( flexTable.getText( 0, 0 ) ));
				assertTrue( flexTable.getWidget( 0, 1 ) instanceof TextBox );
				assertTrue( "Textbox".equals( metawidget.getValue( "textbox" ) ) );
				((TextBox) flexTable.getWidget( 0, 1 )).setText( "Textbox1" );

				//assertTrue( "Limited textbox:".equals( flexTable.getText( 1, 0 ) ));
				assertTrue( flexTable.getWidget( 1, 1 ) instanceof TextBox );
				assertTrue( 20 == ((TextBox) flexTable.getWidget( 1, 1 )).getMaxLength() );
				assertTrue( "Limited Textbox".equals( metawidget.getValue( "limitedTextbox" ) ) );
				((TextBox) flexTable.getWidget( 1, 1 )).setText( "Limited Textbox1" );

				//assertTrue( "Textarea:".equals( flexTable.getText( 2, 0 ) ));
				assertTrue( flexTable.getWidget( 2, 1 ) instanceof TextArea );
				assertTrue( "Textarea".equals( metawidget.getValue( "textarea" ) ) );
				((TextArea) flexTable.getWidget( 2, 1 )).setText( "Textarea1" );

				//assertTrue( "Password:".equals( flexTable.getText( 3, 0 ) ));
				assertTrue( flexTable.getWidget( 3, 1 ) instanceof PasswordTextBox );
				assertTrue( "Password".equals( metawidget.getValue( "password" ) ) );
				((PasswordTextBox) flexTable.getWidget( 3, 1 )).setText( "Password1" );

				// Primitives

				//assertTrue( "Byte:".equals( flexTable.getText( 4, 0 ) ));
				assertTrue( flexTable.getWidget( 4, 1 ) instanceof TextBox );
				assertTrue( String.valueOf( Byte.MAX_VALUE ).equals( metawidget.getValue( "byte" ) ) );
				((TextBox) flexTable.getWidget( 4, 1 )).setText( String.valueOf( Byte.MAX_VALUE - 1 ) );

				//assertTrue( "Byte object:".equals( flexTable.getText( 5, 0 ) ));
				assertTrue( flexTable.getWidget( 5, 1 ) instanceof TextBox );
				assertTrue( String.valueOf( Byte.MIN_VALUE ).equals( metawidget.getValue( "byteObject" ) ) );
				((TextBox) flexTable.getWidget( 5, 1 )).setText( String.valueOf( Byte.MIN_VALUE + 1 ) );

				//assertTrue( "Short:".equals( flexTable.getText( 6, 0 ) ));
				assertTrue( flexTable.getWidget( 6, 1 ) instanceof TextBox );
				assertTrue( String.valueOf( Short.MAX_VALUE ).equals( metawidget.getValue( "short" ) ) );
				((TextBox) flexTable.getWidget( 6, 1 )).setText( String.valueOf( Short.MAX_VALUE - 1 ) );

				//assertTrue( "Short object:".equals( flexTable.getText( 7, 0 ) ));
				assertTrue( flexTable.getWidget( 7, 1 ) instanceof TextBox );
				assertTrue( String.valueOf( Short.MIN_VALUE ).equals( metawidget.getValue( "shortObject" ) ) );
				((TextBox) flexTable.getWidget( 7, 1 )).setText( String.valueOf( Short.MIN_VALUE + 1 ) );

				//assertTrue( "Int:".equals( flexTable.getText( 8, 0 ) ));
				assertTrue( flexTable.getWidget( 8, 1 ) instanceof TextBox );
				assertTrue( String.valueOf( Integer.MAX_VALUE ).equals( metawidget.getValue( "int" ) ) );
				((TextBox) flexTable.getWidget( 8, 1 )).setText( String.valueOf( Integer.MAX_VALUE - 1 ) );

				//assertTrue( "Integer object:".equals( flexTable.getText( 9, 0 ) ));
				assertTrue( flexTable.getWidget( 9, 1 ) instanceof TextBox );
				assertTrue( String.valueOf( Integer.MIN_VALUE ).equals( metawidget.getValue( "integerObject" ) ) );
				((TextBox) flexTable.getWidget( 9, 1 )).setText( String.valueOf( Integer.MIN_VALUE + 1 ) );

				//assertTrue( "Ranged int:".equals( flexTable.getText( 10, 0 ) ));
				assertTrue( flexTable.getWidget( 10, 1 ) instanceof TextBox );
				assertTrue( "32".equals( metawidget.getValue( "rangedInt" ) ) );
				((TextBox) flexTable.getWidget( 10, 1 )).setText( "33" );

				//assertTrue( "Ranged integer:".equals( flexTable.getText( 11, 0 ) ));
				assertTrue( flexTable.getWidget( 11, 1 ) instanceof TextBox );
				assertTrue( "33".equals( metawidget.getValue( "rangedInteger" ) ) );
				((TextBox) flexTable.getWidget( 11, 1 )).setText( "34" );

				//assertTrue( "Long:".equals( flexTable.getText( 12, 0 ) ));
				assertTrue( flexTable.getWidget( 12, 1 ) instanceof TextBox );
				assertTrue( "42".equals( metawidget.getValue( "long" ) ) );
				((TextBox) flexTable.getWidget( 12, 1 )).setText( "43" );

				//assertTrue( "Long object:".equals( flexTable.getText( 13, 0 ) ));
				assertTrue( flexTable.getWidget( 13, 1 ) instanceof TextBox );
				assertTrue( "43".equals( metawidget.getValue( "longObject" ) ) );
				((TextBox) flexTable.getWidget( 13, 1 )).setText( "44" );

				//assertTrue( "Float:".equals( flexTable.getText( 14, 0 ) ));
				assertTrue( flexTable.getWidget( 14, 1 ) instanceof TextBox );
				assertTrue( "4.2".equals( metawidget.getValue( "float" ) ) );
				((TextBox) flexTable.getWidget( 14, 1 )).setText( "5.3" );

				//assertTrue( "Float object:".equals( flexTable.getText( 15, 0 ) ));
				assertTrue( flexTable.getWidget( 15, 1 ) instanceof TextBox );
				assertTrue( "4.3".equals( metawidget.getValue( "floatObject" ) ) );
				((TextBox) flexTable.getWidget( 15, 1 )).setText( "5.4" );

				//assertTrue( "Double:".equals( flexTable.getText( 16, 0 ) ));
				assertTrue( flexTable.getWidget( 16, 1 ) instanceof TextBox );
				assertTrue( "42.2".equals( metawidget.getValue( "double" ) ) );
				((TextBox) flexTable.getWidget( 16, 1 )).setText( "53.3" );

				//assertTrue( "Double object:".equals( flexTable.getText( 17, 0 ) ));
				assertTrue( flexTable.getWidget( 17, 1 ) instanceof TextBox );
				assertTrue( "43.3".equals( metawidget.getValue( "doubleObject" ) ) );
				((TextBox) flexTable.getWidget( 17, 1 )).setText( "54.4" );

				//assertTrue( "Char:".equals( flexTable.getText( 18, 0 ) ));
				assertTrue( flexTable.getWidget( 18, 1 ) instanceof TextBox );
				assertTrue( "A".equals( metawidget.getValue( "char" ) ) );
				assertTrue( 1 == ((TextBox) flexTable.getWidget( 18, 1 )).getMaxLength() );
				((TextBox) flexTable.getWidget( 18, 1 )).setText( "Z" );

				/*
					// Primitives

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
					assertTrue( "dropdown".equals( metawidget.getValue( "dropdown" ) ) );
					( (JComboBox) metawidget.getComponent( 41 ) ).setSelectedItem( "foo" );

					assertTrue( "Dropdown with labels:".equals( ( (JLabel) metawidget.getComponent( 42 ) ).getText() ) );
					assertTrue( metawidget.getComponent( 43 ) instanceof JComboBox );
					assertTrue( 3 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 43 ) ).gridx );
					assertTrue( 4 == ( (JComboBox) metawidget.getComponent( 43 ) ).getItemCount() );
					assertTrue( "dropdown".equals( metawidget.getValue( "dropdownWithLabels" ) ) );
					( (JComboBox) metawidget.getComponent( 43 ) ).setSelectedItem( "bar" );

					assertTrue( "Not null dropdown:".equals( ( (JLabel) metawidget.getComponent( 44 ) ).getText() ) );
					assertTrue( metawidget.getComponent( 45 ) instanceof JComboBox );
					assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 45 ) ).gridx );
					assertTrue( 3 == ( (JComboBox) metawidget.getComponent( 45 ) ).getItemCount() );
					assertTrue( 0 == (Integer) metawidget.getValue( "notNullDropdown" ) );
					( (JComboBox) metawidget.getComponent( 45 ) ).setSelectedItem( 1 );

					assertTrue( "Nested widgets:".equals( ( (JLabel) metawidget.getComponent( 46 ) ).getText() ) );
					assertTrue( metawidget.getComponent( 47 ) instanceof SwingMetawidget );

					SwingMetawidget metawidgetNested = (SwingMetawidget) metawidget.getComponent( 47 );
					assertTrue( "Nested textbox 1:".equals( ( (JLabel) metawidgetNested.getComponent( 0 ) ).getText() ) );
					assertTrue( metawidgetNested.getComponent( 1 ) instanceof JTextField );
					assertTrue( 1 == ( (GridBagLayout) metawidgetNested.getLayout() ).getConstraints( metawidgetNested.getComponent( 1 ) ).gridx );
					assertTrue( "Nested Textbox 1".equals( metawidget.getValue( "nestedWidgets", "nestedTextbox1" ) ) );
					( (JTextField) metawidgetNested.getComponent( 1 ) ).setText( "Nested Textbox 1.1" );

					assertTrue( "Nested textbox 2:".equals( ( (JLabel) metawidgetNested.getComponent( 2 ) ).getText() ) );
					assertTrue( metawidgetNested.getComponent( 3 ) instanceof JTextField );
					assertTrue( 1 == ( (GridBagLayout) metawidgetNested.getLayout() ).getConstraints( metawidgetNested.getComponent( 3 ) ).gridx );
					assertTrue( "Nested Textbox 2".equals( metawidget.getValue( "nestedWidgets", "nestedTextbox2" ) ) );
					( (JTextField) metawidgetNested.getComponent( 3 ) ).setText( "Nested Textbox 2.2" );

					assertTrue( "Read only nested widgets:".equals( ( (JLabel) metawidget.getComponent( 48 ) ).getText() ) );
					assertTrue( metawidget.getComponent( 49 ) instanceof SwingMetawidget );

					metawidgetNested = (SwingMetawidget) metawidget.getComponent( 49 );
					assertTrue( "Nested textbox 1:".equals( ( (JLabel) metawidgetNested.getComponent( 0 ) ).getText() ) );
					assertTrue( metawidgetNested.getComponent( 1 ) instanceof JLabel );
					assertTrue( 1 == ( (GridBagLayout) metawidgetNested.getLayout() ).getConstraints( metawidgetNested.getComponent( 1 ) ).gridx );
					assertTrue( "Nested Textbox 1".equals( metawidget.getValue( "readOnlyNestedWidgets", "nestedTextbox1" ) ) );

					assertTrue( "Nested textbox 2:".equals( ( (JLabel) metawidgetNested.getComponent( 2 ) ).getText() ) );
					assertTrue( metawidgetNested.getComponent( 3 ) instanceof JLabel );
					assertTrue( 1 == ( (GridBagLayout) metawidgetNested.getLayout() ).getConstraints( metawidgetNested.getComponent( 3 ) ).gridx );
					assertTrue( "Nested Textbox 2".equals( metawidget.getValue( "readOnlyNestedWidgets", "nestedTextbox2" ) ) );

					assertTrue( "Read only nested widgets dont expand:".equals( ( (JLabel) metawidget.getComponent( 50 ) ).getText() ) );
					assertTrue( "Nested Textbox 1, Nested Textbox 2".equals( ((JLabel) metawidget.getComponent( 51 )).getText() ));

					assertTrue( "Date:".equals( ( (JLabel) metawidget.getComponent( 52 ) ).getText() ) );
					assertTrue( metawidget.getComponent( 53 ) instanceof JTextField );
					assertTrue( 3 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 53 ) ).gridx );
					assertTrue( converterDate.convert( String.class, allWidgets.getDate() ).equals( metawidget.getValue( "date" ) ) );
					( (JTextField) metawidget.getComponent( 53 ) ).setText( "bad date" );

					assertTrue( "Read only:".equals( ( (JLabel) metawidget.getComponent( 54 ) ).getText() ) );
					assertTrue( metawidget.getComponent( 55 ) instanceof JLabel );
					assertTrue( 1 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 55 ) ).gridx );

					assertTrue( "Mystery:".equals( ( (JLabel) metawidget.getComponent( 56 ) ).getText() ) );
					assertTrue( metawidget.getComponent( 57 ) instanceof JTextField );
					assertTrue( 3 == ( (GridBagLayout) metawidget.getLayout() ).getConstraints( metawidget.getComponent( 57 ) ).gridx );
					( (JTextField) metawidget.getComponent( 57 ) ).setText( "mystery" );

					assertTrue( "Collection:".equals( ( (JLabel) metawidget.getComponent( 58 ) ).getText() ) );
					assertTrue( metawidget.getComponent( 59 ) instanceof JScrollPane );
					assertTrue( ((JScrollPane) metawidget.getComponent( 59 )).getViewport().getView() instanceof JTable );

					assertTrue( metawidget.getComponentCount() == 60 );
				 */

				// Check saving

				Button saveButton = (Button) ( (Facet) flexTable.getWidget( flexTable.getRowCount() - 1 , 0 ) ).getWidget();
				assertTrue( "Save".equals( saveButton.getText() ) );
				fireClickListeners( saveButton );

				Timer timerSave = new Timer()
				{
					@Override
					public void run()
					{
						FlexTable readOnlyFlexTable = (FlexTable) metawidget.getWidget( 0 );

						assertTrue( "textbox:".equals( readOnlyFlexTable.getText( 0, 0 )));
						assertTrue( "Textbox1".equals( readOnlyFlexTable.getText( 0, 1 )));

						finish();
					}
				};

				timerSave.schedule( TIMER_SCHEDULE_DELAY );
			}
		};

		timerLoad.schedule( TIMER_SCHEDULE_DELAY );

		// Test runs asynchronously

		delayTestFinish( TEST_FINISH_DELAY );

	}

	//
	//
	// Protected methods
	//
	//

	/**
	 * Wrapped to avoid 'synthetic access' warning
	 */

	void finish()
	{
		super.finishTest();
	}

	//
	//
	// Native methods
	//
	//

	native void fireClickListeners( FocusWidget focusWidget )
	/*-{
		focusWidget.@com.google.gwt.user.client.ui.FocusWidget::fireClickListeners()();
	}-*/;
}
