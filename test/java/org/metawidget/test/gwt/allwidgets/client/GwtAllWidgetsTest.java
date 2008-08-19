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

package org.metawidget.test.gwt.allwidgets.client;

import java.util.Date;

import org.metawidget.gwt.client.ui.Facet;
import org.metawidget.gwt.client.ui.GwtMetawidget;
import org.metawidget.gwt.client.ui.GwtUtils;
import org.metawidget.test.gwt.allwidgets.client.converter.DateConverter;
import org.metawidget.test.gwt.allwidgets.client.ui.AllWidgetsModule;
import org.metawidget.test.shared.allwidgets.model.AllWidgets;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author Richard Kennard
 */

public class GwtAllWidgetsTest
	extends GWTTestCase
{
	//
	//
	// Private statics
	//
	//

	private final static int	TEST_FINISH_DELAY	= 50 * 5000;

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
		prepareBundle();

		// Start app

		final FlowPanel panel = new FlowPanel();
		final AllWidgetsModule allWidgetsModule = new AllWidgetsModule( panel );
		allWidgetsModule.onModuleLoad();

		final GwtMetawidget metawidget = (GwtMetawidget) panel.getWidget( 0 );

		executeAfterBuildWidgets( metawidget, new Timer()
		{
			@Override
			public void run()
			{
				// Test fields

				final FlexTable flexTable = (FlexTable) metawidget.getWidget( 0 );

				// Check what created, and edit it

				assertTrue( "Textbox:".equals( flexTable.getText( 0, 0 ) ) );
				assertTrue( flexTable.getWidget( 0, 1 ) instanceof TextBox );
				assertTrue( "Textbox".equals( metawidget.getValue( "textbox" ) ) );
				( (TextBox) flexTable.getWidget( 0, 1 ) ).setText( "Textbox1" );

				assertTrue( "Limited textbox:".equals( flexTable.getText( 1, 0 ) ) );
				assertTrue( flexTable.getWidget( 1, 1 ) instanceof TextBox );
				assertTrue( 20 == ( (TextBox) flexTable.getWidget( 1, 1 ) ).getMaxLength() );
				assertTrue( "Limited Textbox".equals( metawidget.getValue( "limitedTextbox" ) ) );
				( (TextBox) flexTable.getWidget( 1, 1 ) ).setText( "Limited Textbox1" );

				assertTrue( "Textarea:".equals( flexTable.getText( 2, 0 ) ) );
				assertTrue( flexTable.getWidget( 2, 1 ) instanceof TextArea );
				assertTrue( "Textarea".equals( metawidget.getValue( "textarea" ) ) );
				( (TextArea) flexTable.getWidget( 2, 1 ) ).setText( "Textarea1" );

				assertTrue( "Password:".equals( flexTable.getText( 3, 0 ) ) );
				assertTrue( flexTable.getWidget( 3, 1 ) instanceof PasswordTextBox );
				assertTrue( "Password".equals( metawidget.getValue( "password" ) ) );
				( (PasswordTextBox) flexTable.getWidget( 3, 1 ) ).setText( "Password1" );

				// Primitives

				assertTrue( "Byte:".equals( flexTable.getText( 4, 0 ) ) );
				assertTrue( flexTable.getWidget( 4, 1 ) instanceof TextBox );
				assertTrue( String.valueOf( Byte.MAX_VALUE ).equals( metawidget.getValue( "byte" ) ) );
				( (TextBox) flexTable.getWidget( 4, 1 ) ).setText( String.valueOf( Byte.MAX_VALUE - 1 ) );

				assertTrue( "Byte object:".equals( flexTable.getText( 5, 0 ) ) );
				assertTrue( flexTable.getWidget( 5, 1 ) instanceof TextBox );
				assertTrue( String.valueOf( Byte.MIN_VALUE ).equals( metawidget.getValue( "byteObject" ) ) );
				( (TextBox) flexTable.getWidget( 5, 1 ) ).setText( String.valueOf( Byte.MIN_VALUE + 1 ) );

				assertTrue( "Short:".equals( flexTable.getText( 6, 0 ) ) );
				assertTrue( flexTable.getWidget( 6, 1 ) instanceof TextBox );
				assertTrue( String.valueOf( Short.MAX_VALUE ).equals( metawidget.getValue( "short" ) ) );
				( (TextBox) flexTable.getWidget( 6, 1 ) ).setText( String.valueOf( Short.MAX_VALUE - 1 ) );

				assertTrue( "Short object:".equals( flexTable.getText( 7, 0 ) ) );
				assertTrue( flexTable.getWidget( 7, 1 ) instanceof TextBox );
				assertTrue( String.valueOf( Short.MIN_VALUE ).equals( metawidget.getValue( "shortObject" ) ) );
				( (TextBox) flexTable.getWidget( 7, 1 ) ).setText( String.valueOf( Short.MIN_VALUE + 1 ) );

				assertTrue( "Int:".equals( flexTable.getText( 8, 0 ) ) );
				assertTrue( flexTable.getWidget( 8, 1 ) instanceof TextBox );
				assertTrue( String.valueOf( Integer.MAX_VALUE ).equals( metawidget.getValue( "int" ) ) );
				( (TextBox) flexTable.getWidget( 8, 1 ) ).setText( String.valueOf( Integer.MAX_VALUE - 1 ) );

				assertTrue( "Integer object:".equals( flexTable.getText( 9, 0 ) ) );
				assertTrue( flexTable.getWidget( 9, 1 ) instanceof TextBox );
				assertTrue( String.valueOf( Integer.MIN_VALUE ).equals( metawidget.getValue( "integerObject" ) ) );
				( (TextBox) flexTable.getWidget( 9, 1 ) ).setText( String.valueOf( Integer.MIN_VALUE + 1 ) );

				assertTrue( "Ranged int:".equals( flexTable.getText( 10, 0 ) ) );
				assertTrue( flexTable.getWidget( 10, 1 ) instanceof TextBox );
				assertTrue( "32".equals( metawidget.getValue( "rangedInt" ) ) );
				( (TextBox) flexTable.getWidget( 10, 1 ) ).setText( "33" );

				assertTrue( "Ranged integer:".equals( flexTable.getText( 11, 0 ) ) );
				assertTrue( flexTable.getWidget( 11, 1 ) instanceof TextBox );
				assertTrue( "33".equals( metawidget.getValue( "rangedInteger" ) ) );
				( (TextBox) flexTable.getWidget( 11, 1 ) ).setText( "34" );

				assertTrue( "Long:".equals( flexTable.getText( 12, 0 ) ) );
				assertTrue( flexTable.getWidget( 12, 1 ) instanceof TextBox );
				assertTrue( "42".equals( metawidget.getValue( "long" ) ) );
				( (TextBox) flexTable.getWidget( 12, 1 ) ).setText( "43" );

				assertTrue( "".equals( flexTable.getText( 13, 0 ) ) );
				assertTrue( flexTable.getWidget( 13, 1 ) instanceof TextBox );
				assertTrue( "43".equals( metawidget.getValue( "longObject" ) ) );
				( (TextBox) flexTable.getWidget( 13, 1 ) ).setText( "44" );

				assertTrue( "Float:".equals( flexTable.getText( 14, 0 ) ) );
				assertTrue( flexTable.getWidget( 14, 1 ) instanceof TextBox );
				assertTrue( "4.2".equals( metawidget.getValue( "float" ) ) );
				( (TextBox) flexTable.getWidget( 14, 1 ) ).setText( "5.3" );

				assertTrue( "nullInBundle:".equals( flexTable.getText( 15, 0 ) ) );
				assertTrue( flexTable.getWidget( 15, 1 ) instanceof TextBox );
				assertTrue( "4.3".equals( metawidget.getValue( "floatObject" ) ) );
				( (TextBox) flexTable.getWidget( 15, 1 ) ).setText( "5.4" );

				assertTrue( "Double:".equals( flexTable.getText( 16, 0 ) ) );
				assertTrue( flexTable.getWidget( 16, 1 ) instanceof TextBox );
				assertTrue( "42.2".equals( metawidget.getValue( "double" ) ) );
				( (TextBox) flexTable.getWidget( 16, 1 ) ).setText( "53.3" );

				assertTrue( flexTable.getWidget( 17, 0 ) instanceof TextBox );
				assertTrue( 2 == flexTable.getFlexCellFormatter().getColSpan( 17, 0 ) );
				assertTrue( "43.3".equals( metawidget.getValue( "doubleObject" ) ) );
				( (TextBox) flexTable.getWidget( 17, 0 ) ).setText( "54.4" );

				assertTrue( "Char:".equals( flexTable.getText( 18, 0 ) ) );
				assertTrue( flexTable.getWidget( 18, 1 ) instanceof TextBox );
				assertTrue( "A".equals( metawidget.getValue( "char" ) ) );
				assertTrue( 1 == ( (TextBox) flexTable.getWidget( 18, 1 ) ).getMaxLength() );
				( (TextBox) flexTable.getWidget( 18, 1 ) ).setText( "Z" );

				assertTrue( "Boolean:".equals( flexTable.getText( 19, 0 ) ) );
				assertTrue( flexTable.getWidget( 19, 1 ) instanceof CheckBox );
				assertTrue( false == (Boolean) metawidget.getValue( "boolean" ) );
				( (CheckBox) flexTable.getWidget( 19, 1 ) ).setChecked( true );

				assertTrue( "Boolean object:".equals( flexTable.getText( 20, 0 ) ) );
				assertTrue( flexTable.getWidget( 20, 1 ) instanceof ListBox );
				assertTrue( 3 == ( (ListBox) flexTable.getWidget( 20, 1 ) ).getItemCount() );
				assertTrue( Boolean.TRUE.toString().equals( metawidget.getValue( "booleanObject" ) ) );
				GwtUtils.setListBoxSelectedItem( ( (ListBox) flexTable.getWidget( 20, 1 ) ), Boolean.FALSE.toString() );

				assertTrue( "Dropdown:".equals( flexTable.getText( 21, 0 ) ) );
				assertTrue( flexTable.getWidget( 21, 1 ) instanceof ListBox );
				assertTrue( 4 == ( (ListBox) flexTable.getWidget( 21, 1 ) ).getItemCount() );
				assertTrue( "dropdown1".equals( metawidget.getValue( "dropdown" ) ) );
				GwtUtils.setListBoxSelectedItem( ( (ListBox) flexTable.getWidget( 21, 1 ) ), "foo1" );

				assertTrue( "Dropdown with labels:".equals( flexTable.getText( 22, 0 ) ) );
				assertTrue( flexTable.getWidget( 22, 1 ) instanceof ListBox );
				ListBox listbox = (ListBox) flexTable.getWidget( 22, 1 );
				assertTrue( 5 == listbox.getItemCount() );
				assertTrue( "foo2".equals( listbox.getValue( 1 ) ) );
				assertTrue( "Foo #2".equals( listbox.getItemText( 1 ) ) );
				assertTrue( "dropdown2".equals( listbox.getValue( 2 ) ) );
				assertTrue( "Dropdown #2".equals( listbox.getItemText( 2 ) ) );
				assertTrue( "bar2".equals( listbox.getValue( 3 ) ) );
				assertTrue( "Bar #2".equals( listbox.getItemText( 3 ) ) );
				assertTrue( "baz2".equals( listbox.getValue( 4 ) ) );
				assertTrue( "Baz #2".equals( listbox.getItemText( 4 ) ) );
				assertTrue( "dropdown2".equals( metawidget.getValue( "dropdownWithLabels" ) ) );
				GwtUtils.setListBoxSelectedItem( ( (ListBox) flexTable.getWidget( 22, 1 ) ), "bar2" );

				assertTrue( "Not null dropdown:".equals( flexTable.getText( 23, 0 ) ) );
				assertTrue( flexTable.getWidget( 23, 1 ) instanceof ListBox );
				assertTrue( 3 == ( (ListBox) flexTable.getWidget( 23, 1 ) ).getItemCount() );
				assertTrue( "0".equals( metawidget.getValue( "notNullDropdown" ) ) );
				GwtUtils.setListBoxSelectedItem( ( (ListBox) flexTable.getWidget( 23, 1 ) ), "1" );

				assertTrue( "Not null object dropdown:".equals( flexTable.getText( 24, 0 ) ) );
				assertTrue( flexTable.getWidget( 24, 1 ) instanceof ListBox );
				assertTrue( 6 == ( (ListBox) flexTable.getWidget( 24, 1 ) ).getItemCount() );
				assertTrue( "dropdown3".equals( metawidget.getValue( "notNullObjectDropdown" ) ) );
				((ListBox) flexTable.getWidget( 24, 1 ) ).setSelectedIndex( 0 );

				assertTrue( "Nested widgets:".equals( flexTable.getText( 25, 0 ) ) );
				assertTrue( flexTable.getWidget( 25, 1 ) instanceof GwtMetawidget );

				final GwtMetawidget metawidgetNested = (GwtMetawidget) metawidget.getWidget( "nestedWidgets" );

				executeAfterBuildWidgets( metawidgetNested, new Timer()
				{
					@Override
					public void run()
					{
						final FlexTable flexTableNested = (FlexTable) metawidgetNested.getWidget( 0 );

						assertTrue( "Further nested widgets:".equals( flexTableNested.getText( 0, 0 ) ) );
						final GwtMetawidget metawidgetFurtherNested = (GwtMetawidget) metawidgetNested.getWidget( "furtherNestedWidgets" );

						executeAfterBuildWidgets( metawidgetFurtherNested, new Timer()
						{
							@Override
							public void run()
							{
								final FlexTable flexTableFurtherNested = (FlexTable) metawidgetFurtherNested.getWidget( 0 );

								assertTrue( "Further nested widgets:".equals( flexTableFurtherNested.getText( 0, 0 ) ) );
								final GwtMetawidget metawidgetFurtherFurtherNested = (GwtMetawidget) metawidgetFurtherNested.getWidget( "furtherNestedWidgets" );

								executeAfterBuildWidgets( metawidgetFurtherFurtherNested, new Timer()
								{
									@Override
									public void run()
									{
										final FlexTable flexTableFurtherFurtherNested = (FlexTable) metawidgetFurtherFurtherNested.getWidget( 0 );
										assertTrue( 0 == flexTableFurtherFurtherNested.getRowCount() );

										assertTrue( "Nested textbox 1:".equals( flexTableFurtherNested.getText( 1, 0 ) ) );
										assertTrue( flexTableFurtherNested.getWidget( 1, 1 ) instanceof TextBox );
										assertTrue( "Nested Textbox 1".equals( metawidget.getValue( "nestedWidgets", "furtherNestedWidgets", "nestedTextbox1" ) ) );
										( (TextBox) flexTableFurtherNested.getWidget( 1, 1 ) ).setText( "Nested Textbox 1.1 (further)" );

										assertTrue( "Nested textbox 2:".equals( flexTableFurtherNested.getText( 2, 0 ) ) );
										assertTrue( flexTableFurtherNested.getWidget( 2, 1 ) instanceof TextBox );
										assertTrue( "Nested Textbox 2".equals( metawidget.getValue( "nestedWidgets", "furtherNestedWidgets", "nestedTextbox2" ) ) );
										( (TextBox) flexTableFurtherNested.getWidget( 2, 1 ) ).setText( "Nested Textbox 2.2 (further)" );

										assertTrue( "Nested textbox 1:".equals( flexTableNested.getText( 1, 0 ) ) );
										assertTrue( flexTableNested.getWidget( 1, 1 ) instanceof TextBox );
										assertTrue( "Nested Textbox 1".equals( metawidget.getValue( "nestedWidgets", "nestedTextbox1" ) ) );
										( (TextBox) flexTableNested.getWidget( 1, 1 ) ).setText( "Nested Textbox 1.1" );

										assertTrue( "Nested textbox 2:".equals( flexTableNested.getText( 2, 0 ) ) );
										assertTrue( flexTableNested.getWidget( 2, 1 ) instanceof TextBox );
										assertTrue( "Nested Textbox 2".equals( metawidget.getValue( "nestedWidgets", "nestedTextbox2" ) ) );
										( (TextBox) flexTableNested.getWidget( 2, 1 ) ).setText( "Nested Textbox 2.2" );

										assertTrue( "Read only nested widgets:".equals( flexTable.getText( 26, 0 ) ) );
										assertTrue( flexTable.getWidget( 26, 1 ) instanceof GwtMetawidget );

										final GwtMetawidget metawidgetReadOnlyNested = (GwtMetawidget) metawidget.getWidget( "readOnlyNestedWidgets" );

										executeAfterBuildWidgets( metawidgetReadOnlyNested, new Timer()
										{
											@Override
											public void run()
											{
												final FlexTable flexTableReadOnlyNested = (FlexTable) metawidgetReadOnlyNested.getWidget( 0 );

												assertTrue( "Further nested widgets:".equals( flexTableReadOnlyNested.getText( 0, 0 ) ) );
												final GwtMetawidget metawidgetReadOnlyFurtherNested = (GwtMetawidget) metawidgetReadOnlyNested.getWidget( "furtherNestedWidgets" );

												executeAfterBuildWidgets( metawidgetReadOnlyFurtherNested, new Timer()
												{
													@Override
													public void run()
													{
														final FlexTable flexTableReadOnlyFurtherNested = (FlexTable) metawidgetReadOnlyFurtherNested.getWidget( 0 );
														assertTrue( 0 == flexTableReadOnlyFurtherNested.getRowCount() );

														assertTrue( "Nested textbox 1:".equals( flexTableReadOnlyNested.getText( 1, 0 ) ) );
														assertTrue( flexTableReadOnlyNested.getWidget( 1, 1 ) instanceof Label );
														assertTrue( "Nested Textbox 1".equals( metawidget.getValue( "readOnlyNestedWidgets", "nestedTextbox1" ) ) );

														assertTrue( "Nested textbox 2:".equals( flexTableReadOnlyNested.getText( 2, 0 ) ) );
														assertTrue( flexTableReadOnlyNested.getWidget( 2, 1 ) instanceof Label );
														assertTrue( "Nested Textbox 2".equals( metawidget.getValue( "readOnlyNestedWidgets", "nestedTextbox2" ) ) );

														assertTrue( "Nested widgets dont expand:".equals( flexTable.getText( 27, 0 ) ) );
														assertTrue( flexTable.getWidget( 27, 1 ) instanceof TextBox );
														assertTrue( "Nested Textbox 1, Nested Textbox 2".equals( ( (TextBox) flexTable.getWidget( 27, 1 ) ).getText() ) );
														( (TextBox) flexTable.getWidget( 27, 1 ) ).setText( "Nested Textbox 1.01, Nested Textbox 2.02" );

														assertTrue( "Read only nested widgets dont expand:".equals( flexTable.getText( 28, 0 ) ) );
														assertTrue( flexTable.getWidget( 28, 1 ) instanceof Label );
														assertTrue( "Nested Textbox 1, Nested Textbox 2".equals( ( (Label) flexTable.getWidget( 28, 1 ) ).getText() ) );

														assertTrue( "Date:".equals( flexTable.getText( 29, 0 ) ) );
														assertTrue( flexTable.getWidget( 29, 1 ) instanceof TextBox );
														assertTrue( new DateConverter().convertForWidget( null, ( (AllWidgets) metawidget.getToInspect() ).getDate() ).equals( metawidget.getValue( "date" ) ) );
														( (TextBox) flexTable.getWidget( 29, 1 ) ).setText( "bad date" );

														assertTrue( "Read only:".equals( flexTable.getText( 30, 0 ) ) );
														assertTrue( flexTable.getWidget( 30, 1 ) instanceof Label );
														assertTrue( "Read Only".equals( metawidget.getValue( "readOnly" ) ) );

														assertTrue( 32 == flexTable.getRowCount() );

														// Check IllegalArgumentException

														Button saveButton = (Button) ( (Facet) flexTable.getWidget( flexTable.getRowCount() - 1, 0 ) ).getWidget();
														assertTrue( "Save".equals( saveButton.getText() ) );

														try
														{
															fireClickListeners( saveButton );
														}
														catch ( IllegalArgumentException e )
														{
															assertTrue( "bad date".equals( e.getMessage() ) );
														}

														// Check saving

														final String now = (String) new DateConverter().convertForWidget( null, new Date() );
														( (TextBox) flexTable.getWidget( 29, 1 ) ).setText( now );
														fireClickListeners( saveButton );

														executeAfterBuildWidgets( metawidget, new Timer()
														{
															@Override
															public void run()
															{
																final FlexTable readOnlyFlexTable = (FlexTable) metawidget.getWidget( 0 );

																assertTrue( "Textbox (i18n):".equals( readOnlyFlexTable.getText( 0, 0 ) ) );
																assertTrue( "Textbox1".equals( ( (Label) readOnlyFlexTable.getWidget( 0, 1 ) ).getText() ) );
																assertTrue( "Limited textbox (i18n):".equals( readOnlyFlexTable.getText( 1, 0 ) ) );
																assertTrue( "Limited Textbox1".equals( ( (Label) readOnlyFlexTable.getWidget( 1, 1 ) ).getText() ) );
																assertTrue( "Textarea (i18n):".equals( readOnlyFlexTable.getText( 2, 0 ) ) );
																assertTrue( "Textarea1".equals( ( (Label) readOnlyFlexTable.getWidget( 2, 1 ) ).getText() ) );
																assertTrue( "Password (i18n):".equals( readOnlyFlexTable.getText( 3, 0 ) ) );
																assertTrue( readOnlyFlexTable.getWidget( 3, 1 ) instanceof SimplePanel );
																assertTrue( "Byte (i18n):".equals( readOnlyFlexTable.getText( 4, 0 ) ) );
																assertTrue( "126".equals( ( (Label) readOnlyFlexTable.getWidget( 4, 1 ) ).getText() ) );
																assertTrue( "Byte object (i18n):".equals( readOnlyFlexTable.getText( 5, 0 ) ) );
																assertTrue( "-127".equals( ( (Label) readOnlyFlexTable.getWidget( 5, 1 ) ).getText() ) );
																assertTrue( "Short (i18n):".equals( readOnlyFlexTable.getText( 6, 0 ) ) );
																assertTrue( "32766".equals( ( (Label) readOnlyFlexTable.getWidget( 6, 1 ) ).getText() ) );
																assertTrue( "Short object (i18n):".equals( readOnlyFlexTable.getText( 7, 0 ) ) );
																assertTrue( "-32767".equals( ( (Label) readOnlyFlexTable.getWidget( 7, 1 ) ).getText() ) );
																assertTrue( "Int (i18n):".equals( readOnlyFlexTable.getText( 8, 0 ) ) );
																assertTrue( "2147483646".equals( ( (Label) readOnlyFlexTable.getWidget( 8, 1 ) ).getText() ) );
																assertTrue( "Integer object (i18n):".equals( readOnlyFlexTable.getText( 9, 0 ) ) );
																assertTrue( "-2147483647".equals( ( (Label) readOnlyFlexTable.getWidget( 9, 1 ) ).getText() ) );
																assertTrue( "Ranged int (i18n):".equals( readOnlyFlexTable.getText( 10, 0 ) ) );
																assertTrue( "33".equals( ( (Label) readOnlyFlexTable.getWidget( 10, 1 ) ).getText() ) );
																assertTrue( "Ranged integer (i18n):".equals( readOnlyFlexTable.getText( 11, 0 ) ) );
																assertTrue( "34".equals( ( (Label) readOnlyFlexTable.getWidget( 11, 1 ) ).getText() ) );
																assertTrue( "Long (i18n):".equals( readOnlyFlexTable.getText( 12, 0 ) ) );
																assertTrue( "43".equals( ( (Label) readOnlyFlexTable.getWidget( 12, 1 ) ).getText() ) );
																assertTrue( "??????:".equals( readOnlyFlexTable.getText( 13, 0 ) ) );
																assertTrue( "44".equals( ( (Label) readOnlyFlexTable.getWidget( 13, 1 ) ).getText() ) );
																assertTrue( "Float (i18n):".equals( readOnlyFlexTable.getText( 14, 0 ) ) );
																assertTrue( "5.3".equals( ( (Label) readOnlyFlexTable.getWidget( 14, 1 ) ).getText() ) );
																assertTrue( "".equals( readOnlyFlexTable.getText( 15, 0 ) ) );
																assertTrue( "5.4".equals( ( (Label) readOnlyFlexTable.getWidget( 15, 1 ) ).getText() ) );
																assertTrue( "Double (i18n):".equals( readOnlyFlexTable.getText( 16, 0 ) ) );
																assertTrue( "53.3".equals( ( (Label) readOnlyFlexTable.getWidget( 16, 1 ) ).getText() ) );
																assertTrue( "54.4".equals( ( (Label) readOnlyFlexTable.getWidget( 17, 0 ) ).getText() ) );
																assertTrue( 2 == readOnlyFlexTable.getFlexCellFormatter().getColSpan( 17, 0 ) );
																assertTrue( "Char (i18n):".equals( readOnlyFlexTable.getText( 18, 0 ) ) );
																assertTrue( "Z".equals( ( (Label) readOnlyFlexTable.getWidget( 18, 1 ) ).getText() ) );
																assertTrue( "Boolean (i18n):".equals( readOnlyFlexTable.getText( 19, 0 ) ) );
																assertTrue( "true".equals( ( (Label) readOnlyFlexTable.getWidget( 19, 1 ) ).getText() ) );
																assertTrue( "Boolean object (i18n):".equals( readOnlyFlexTable.getText( 20, 0 ) ) );
																assertTrue( "false".equals( ( (Label) readOnlyFlexTable.getWidget( 20, 1 ) ).getText() ) );
																assertTrue( "Dropdown (i18n):".equals( readOnlyFlexTable.getText( 21, 0 ) ) );
																assertTrue( "foo1".equals( ( (Label) readOnlyFlexTable.getWidget( 21, 1 ) ).getText() ) );
																assertTrue( "Dropdown with Labels (i18n):".equals( readOnlyFlexTable.getText( 22, 0 ) ) );
																assertTrue( "bar2".equals( ( (Label) readOnlyFlexTable.getWidget( 22, 1 ) ).getText() ) );
																assertTrue( "Not-null Dropdown (i18n):".equals( readOnlyFlexTable.getText( 23, 0 ) ) );
																assertTrue( "1".equals( ( (Label) readOnlyFlexTable.getWidget( 23, 1 ) ).getText() ) );
																assertTrue( "Not-null Object Dropdown (i18n):".equals( readOnlyFlexTable.getText( 24, 0 ) ) );
																assertTrue( "foo3".equals( ( (Label) readOnlyFlexTable.getWidget( 24, 1 ) ).getText() ) );
																assertTrue( "Nested Widgets (i18n):".equals( readOnlyFlexTable.getText( 25, 0 ) ) );

																final GwtMetawidget readOnlyMetawidgetNested = (GwtMetawidget) readOnlyFlexTable.getWidget( 25, 1 );

																executeAfterBuildWidgets( readOnlyMetawidgetNested, new Timer()
																{
																	@Override
																	public void run()
																	{
																		final FlexTable readOnlyFlexTableNested = (FlexTable) readOnlyMetawidgetNested.getWidget( 0 );

																		assertTrue( "Further Nested Widgets (i18n):".equals( readOnlyFlexTableNested.getText( 0, 0 ) ) );
																		final GwtMetawidget readOnlyMetawidgetFurtherNested = (GwtMetawidget) readOnlyFlexTableNested.getWidget( 0, 1 );

																		executeAfterBuildWidgets( readOnlyMetawidgetFurtherNested, new Timer()
																		{
																			@Override
																			public void run()
																			{
																				final FlexTable readOnlyFlexTableFurtherNested = (FlexTable) readOnlyMetawidgetFurtherNested.getWidget( 0 );
																				assertTrue( "Nested Textbox 1.1 (further)".equals( ( (Label) readOnlyFlexTableFurtherNested.getWidget( 1, 1 ) ).getText() ) );
																				assertTrue( "Nested Textbox 2.2 (further)".equals( ( (Label) readOnlyFlexTableFurtherNested.getWidget( 2, 1 ) ).getText() ) );

																				assertTrue( "???nestedTextbox1???:".equals( readOnlyFlexTableNested.getText( 1, 0 ) ) );
																				assertTrue( "Nested Textbox 1.1".equals( ( (Label) readOnlyFlexTableNested.getWidget( 1, 1 ) ).getText() ) );
																				assertTrue( "Nested Textbox 2 (i18n):".equals( readOnlyFlexTableNested.getText( 2, 0 ) ) );
																				assertTrue( "Nested Textbox 2.2".equals( ( (Label) readOnlyFlexTableNested.getWidget( 2, 1 ) ).getText() ) );

																				final GwtMetawidget readOnlyMetawidgetNested2 = (GwtMetawidget) readOnlyFlexTable.getWidget( 26, 1 );

																				executeAfterBuildWidgets( readOnlyMetawidgetNested2, new Timer()
																				{
																					@Override
																					public void run()
																					{
																						FlexTable readOnlyFlexTableNested2 = (FlexTable) readOnlyMetawidgetNested2.getWidget( 0 );
																						assertTrue( "???nestedTextbox1???:".equals( readOnlyFlexTableNested2.getText( 1, 0 ) ) );
																						assertTrue( "Nested Textbox 1".equals( ( (Label) readOnlyFlexTableNested2.getWidget( 1, 1 ) ).getText() ) );
																						assertTrue( "Nested Textbox 2 (i18n):".equals( readOnlyFlexTableNested2.getText( 2, 0 ) ) );
																						assertTrue( "Nested Textbox 2".equals( ( (Label) readOnlyFlexTableNested2.getWidget( 2, 1 ) ).getText() ) );

																						assertTrue( "Nested Widgets (don't expand) (i18n):".equals( readOnlyFlexTable.getText( 27, 0 ) ) );
																						assertTrue( "Nested Textbox 1.01, Nested Textbox 2.02".equals( ( (Label) readOnlyFlexTable.getWidget( 27, 1 ) ).getText() ) );

																						assertTrue( "Read only Nested Widgets (don't expand) (i18n):".equals( readOnlyFlexTable.getText( 28, 0 ) ) );
																						assertTrue( "Nested Textbox 1, Nested Textbox 2".equals( ( (Label) readOnlyFlexTable.getWidget( 28, 1 ) ).getText() ) );

																						assertTrue( "Date (i18n):".equals( readOnlyFlexTable.getText( 29, 0 ) ) );
																						assertTrue( now.equals( ( (Label) readOnlyFlexTable.getWidget( 29, 1 ) ).getText() ) );

																						assertTrue( "Read only (i18n):".equals( readOnlyFlexTable.getText( 30, 0 ) ) );
																						assertTrue( "Read Only".equals( ( (Label) readOnlyFlexTable.getWidget( 30, 1 ) ).getText() ) );

																						assertTrue( 32 == flexTable.getRowCount() );

																						// Test
																						// maximum
																						// inspection
																						// depth

																						metawidget.setMaximumInspectionDepth( 0 );

																						executeAfterBuildWidgets( metawidget, new Timer()
																						{
																							@Override
																							public void run()
																							{
																								assertTrue( null == metawidget.getWidget( "nestedWidgets" ) );
																								assertTrue( null == metawidget.getWidget( "readOnlyNestedWidgets" ) );
																								assertTrue( metawidget.getWidget( "readOnlyNestedWidgetsDontExpand" ) instanceof Label );

																								finish();
																							}
																						} );
																					}
																				} );
																			}
																		} );
																	}
																} );
															}
														} );
													}
												} );
											}
										} );
									}
								} );
							}
						} );
					}
				} );
			}
		} );

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

	native void executeAfterBuildWidgets( GwtMetawidget metawidget, Timer timer )
	/*-{
		metawidget.@org.metawidget.gwt.client.ui.GwtMetawidget::mExecuteAfterBuildWidgets = timer;
		metawidget.@org.metawidget.gwt.client.ui.GwtMetawidget::buildWidgets()();
	}-*/;

	private native void prepareBundle()
	/*-{
		$wnd[ "bundle" ] = {
			"textbox": "Textbox (i18n)",
			"limitedTextbox": "Limited textbox (i18n)",
			"textarea": "Textarea (i18n)",
			"password": "Password (i18n)",
			"byte": "Byte (i18n)",
			"byteObject": "Byte object (i18n)",
			"short": "Short (i18n)",
			"shortObject": "Short object (i18n)",
			"int": "Int (i18n)",
			"integerObject": "Integer object (i18n)",
			"rangedInt": "Ranged int (i18n)",
			"rangedInteger": "Ranged integer (i18n)",
			"long": "Long (i18n)",
			"longObject": "Long object (i18n)",
			"float": "Float (i18n)",
			"nullInBundle": "",
			"double": "Double (i18n)",
			"char": "Char (i18n)",
			"boolean": "Boolean (i18n)",
			"booleanObject": "Boolean object (i18n)",
			"dropdown": "Dropdown (i18n)",
			"dropdownWithLabels": "Dropdown with Labels (i18n)",
			"notNullDropdown": "Not-null Dropdown (i18n)",
			"notNullObjectDropdown": "Not-null Object Dropdown (i18n)",
			"nestedWidgets": "Nested Widgets (i18n)",
			"furtherNestedWidgets": "Further Nested Widgets (i18n)",
			"nestedTextbox2": "Nested Textbox 2 (i18n)",
			"readOnlyNestedWidgets": "Read only Nested Widgets (i18n)",
			"nestedWidgetsDontExpand": "Nested Widgets (don't expand) (i18n)",
			"readOnlyNestedWidgetsDontExpand": "Read only Nested Widgets (don't expand) (i18n)",
			"date": "Date (i18n)",
			"readOnly": "Read only (i18n)",
			"collection": "Collection (i18n)"
		};
	}-*/;
}