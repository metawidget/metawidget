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

package org.metawidget.test.example.gwt.addressbook.client;

import org.metawidget.example.gwt.addressbook.client.rpc.ContactsServiceAsync;
import org.metawidget.example.gwt.addressbook.client.ui.AddressBookModule;
import org.metawidget.example.gwt.addressbook.client.ui.ContactDialog;
import org.metawidget.example.shared.addressbook.model.BusinessContact;
import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.example.shared.addressbook.model.ContactType;
import org.metawidget.gwt.client.ui.Facet;
import org.metawidget.gwt.client.ui.GwtMetawidget;
import org.metawidget.gwt.client.ui.Stub;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;

/**
 * @author Richard Kennard
 */

public class GwtAddressBookTest
	extends GWTTestCase
{
	//
	// Private statics
	//

	/**
	 * Time to execute the /contacts service.
	 */

	private final static int	CONTACTS_SERVICE_DELAY	= 1000;

	private final static int	TEST_FINISH_DELAY		= 500 * CONTACTS_SERVICE_DELAY;

	//
	// Public methods
	//

	@Override
	public String getModuleName()
	{
		return "org.metawidget.test.example.gwt.addressbook.GwtAddressBookTest";
	}

	public void testPersonalContact()
		throws Exception
	{
		// Start app

		prepareBundle();
		FlowPanel panel = new FlowPanel();
		final AddressBookModule addressBookModule = new AddressBookModule( panel );
		addressBookModule.onModuleLoad();

		// Check searching

		final GwtMetawidget metawidgetSearch = (GwtMetawidget) panel.getWidget( 0 );
		final FlexTable contacts = (FlexTable) panel.getWidget( 1 );
		final FlexCellFormatter cellFormatter = contacts.getFlexCellFormatter();

		executeAfterBuildWidgets( metawidgetSearch, new Timer()
		{
			@Override
			public void run()
			{
				metawidgetSearch.setValue( "Simpson", "surname" );
				metawidgetSearch.setValue( ContactType.PERSONAL.name(), "type" );

				FlexTable flexTable = (FlexTable) metawidgetSearch.getWidget( 0 );

				assertTrue( flexTable.getRowCount() == 4 );
				final Button buttonSearch = (Button) ( (HorizontalPanel) ( (Facet) flexTable.getWidget( 3, 0 ) ).getWidget() ).getWidget( 0 );
				assertTrue( "Search".equals( buttonSearch.getText() ) );
				fireClickListeners( buttonSearch );

				Timer timerResults = new Timer()
				{
					@Override
					public void run()
					{
						assertTrue( contacts.getRowCount() == 3 );
						assertTrue( "Name".equals( contacts.getText( 0, 0 ) ) );
						assertTrue( "header".equals( cellFormatter.getStyleName( 0, 0 ) ) );
						assertTrue( "Contact".equals( contacts.getText( 0, 1 ) ) );
						assertTrue( "header".equals( cellFormatter.getStyleName( 0, 1 ) ) );
						assertTrue( "&nbsp;".equals( contacts.getHTML( 0, 2 ) ) );
						assertTrue( "header".equals( cellFormatter.getStyleName( 0, 2 ) ) );
						assertTrue( "Mr Homer Simpson".equals( contacts.getText( 1, 0 ) ) );
						assertTrue( "Telephone: (939) 555-0113".equals( contacts.getText( 1, 1 ) ) );
						assertTrue( ( (Image) contacts.getWidget( 1, 2 ) ).getUrl().endsWith( "media/personal-small.gif" ) );

						// Open dialog for Personal Contact

						final ContactsServiceAsync contactsService = addressBookModule.getContactsService();

						contactsService.load( 1, new AsyncCallback<Contact>()
						{
							public void onFailure( Throwable caught )
							{
								throw new RuntimeException( caught );
							}

							public void onSuccess( final Contact personalContact )
							{
								final ContactDialog dialog = new ContactDialog( addressBookModule, personalContact );
								final GwtMetawidget contactMetawidget = (GwtMetawidget) ( (Grid) dialog.getWidget() ).getWidget( 0, 1 );

								executeAfterBuildWidgets( contactMetawidget, new Timer()
								{
									@Override
									public void run()
									{
										assertTrue( contactMetawidget.getWidget( "firstname" ) instanceof Label );
										assertTrue( "Homer".equals( contactMetawidget.getValue( "firstname" ) ) );

										FlexTable contactFlexTable = (FlexTable) contactMetawidget.getWidget( 0 );
										assertTrue( "Date of Birth:".equals( contactFlexTable.getText( 3, 0 ) ) );
										assertTrue( "5/12/56".equals( contactMetawidget.getValue( "dateOfBirth" ) ) );
										assertTrue( "Contact Details".equals( contactFlexTable.getText( 5, 0 ) ) );
										assertTrue( 2 == contactFlexTable.getFlexCellFormatter().getColSpan( 5, 0 ) );
										assertTrue( "section-heading".equals( contactFlexTable.getFlexCellFormatter().getStyleName( 5, 0 ) ) );
										assertTrue( "Address:".equals( contactFlexTable.getText( 6, 0 ) ) );

										try
										{
											contactMetawidget.getValue( "bad-value" );
											assertTrue( false );
										}
										catch ( Exception e )
										{
											// Should throw Exception
										}

										final FlexTable communications = (FlexTable) ( (Stub) contactMetawidget.getWidget( "communications" ) ).getWidget();
										assertTrue( communications.getRowCount() == 3 );
										assertTrue( "Type".equals( communications.getText( 0, 0 ) ) );
										assertTrue( "Value".equals( communications.getText( 0, 1 ) ) );
										assertTrue( "&nbsp;".equals( communications.getHTML( 0, 2 ) ) );
										assertTrue( 3 == communications.getCellCount( 0 ) );
										assertTrue( "Telephone".equals( communications.getText( 1, 0 ) ) );
										assertTrue( "(939) 555-0113".equals( communications.getText( 1, 1 ) ) );
										assertTrue( 2 == communications.getCellCount( 1 ) );

										// Check editing

										assertTrue( ( (FlexTable) contactMetawidget.getWidget( 0 ) ).getRowCount() == 11 );

										Button backButton = (Button) ( (HorizontalPanel) ( (Facet) ( (FlexTable) contactMetawidget.getWidget( 0 ) ).getWidget( 10, 0 ) ).getWidget() ).getWidget( 3 );
										assertTrue( "Back".equals( backButton.getText() ) );
										assertTrue( backButton.isVisible() );

										final Button editButton = (Button) ( (HorizontalPanel) ( (Facet) ( (FlexTable) contactMetawidget.getWidget( 0 ) ).getWidget( 10, 0 ) ).getWidget() ).getWidget( 2 );
										assertTrue( "Edit".equals( editButton.getText() ) );
										assertTrue( editButton.isVisible() );
										fireClickListeners( editButton );

										executeAfterBuildWidgets( contactMetawidget, new Timer()
										{
											@Override
											public void run()
											{
												assertTrue( !editButton.isVisible() );
												assertTrue( contactMetawidget.getWidget( "title" ) instanceof ListBox );
												assertTrue( ( (ListBox) contactMetawidget.getWidget( "title" ) ).getItemCount() == 5 );

												assertTrue( contactMetawidget.getWidget( "firstname" ) instanceof TextBox );
												assertTrue( "Homer".equals( contactMetawidget.getValue( "firstname" ) ) );
												assertTrue( "5/12/56".equals( contactMetawidget.getValue( "dateOfBirth" ) ) );
												final Button deleteCommunication = (Button) communications.getWidget( 1, 2 );
												assertTrue( deleteCommunication.isVisible() );
												assertTrue( "Delete".equals( deleteCommunication.getText() ) );
												assertTrue( "table-buttons".equals( communications.getCellFormatter().getStyleName( 1, 2 ) ) );

												contactMetawidget.setValue( "Sapien", "surname" );

												// Check saving

												contactMetawidget.setValue( "foo", "dateOfBirth" );

												try
												{
													contactMetawidget.save();
													assertTrue( false );
												}
												catch ( IllegalArgumentException e )
												{
													assertTrue( "foo".equals( e.getMessage() ) );
												}

												contactMetawidget.setValue( "12/05/57", "dateOfBirth" );

												// Check deleting a Communication

												assertTrue( communications.getRowCount() == 3 );
												fireClickListeners( deleteCommunication );
												assertTrue( communications.getRowCount() == 2 );

												// Save again

												GwtMetawidget addressMetawidget = (GwtMetawidget) contactMetawidget.getWidget( "address" );

												executeAfterBuildWidgets( addressMetawidget, new Timer()
												{
													@Override
													public void run()
													{
														assertTrue( "742 Evergreen Terrace".equals( contactMetawidget.getValue( "address", "street" ) ) );
														assertTrue( "Anytown".equals( contactMetawidget.getValue( "address", "state" ) ) );

														contactMetawidget.setValue( "743 Evergreen Terrace", "address", "street" );
														( (ListBox) contactMetawidget.getWidget( "address", "state" ) ).setSelectedIndex( 3 );

														Button cancelButton = (Button) ( (HorizontalPanel) ( (Facet) ( (FlexTable) contactMetawidget.getWidget( 0 ) ).getWidget( 10, 0 ) ).getWidget() ).getWidget( 3 );
														assertTrue( "Cancel".equals( cancelButton.getText() ) );
														assertTrue( cancelButton.isVisible() );

														Button saveButton = (Button) ( (HorizontalPanel) ( (Facet) ( (FlexTable) contactMetawidget.getWidget( 0 ) ).getWidget( 10, 0 ) ).getWidget() ).getWidget( 0 );
														assertTrue( "Save".equals( saveButton.getText() ) );
														assertTrue( saveButton.isVisible() );
														fireClickListeners( saveButton );

														Timer timerReloadContacts = new Timer()
														{
															@Override
															public void run()
															{
																assertTrue( "Mrs Marjorie Simpson".equals( contacts.getText( 1, 0 ) ) );

																// Check surname change

																metawidgetSearch.setValue( "", "surname" );
																fireClickListeners( buttonSearch );

																Timer timerReloadContactsAgain = new Timer()
																{
																	@Override
																	public void run()
																	{
																		assertTrue( "Mr Homer Sapien".equals( contacts.getText( 1, 0 ) ) );
																		assertTrue( "".equals( contacts.getText( 1, 1 ) ) );

																		// Check address change

																		assertTrue( "743 Evergreen Terrace".equals( personalContact.getAddress().getStreet() ) );
																		assertTrue( "Lostville".equals( personalContact.getAddress().getState() ) );
																		finish();
																	}
																};

																timerReloadContactsAgain.schedule( CONTACTS_SERVICE_DELAY );
															}
														};

														timerReloadContacts.schedule( CONTACTS_SERVICE_DELAY );
													}
												} );
											}
										} );
									}
								} );
							}
						} );
					}
				};

				// Fetch the results after giving /contacts time to finish

				assertTrue( contacts.getRowCount() == 7 );
				timerResults.schedule( CONTACTS_SERVICE_DELAY );
			}
		} );

		// Test runs asynchronously

		delayTestFinish( TEST_FINISH_DELAY );
	}

	public void testBusinessContact()
		throws Exception
	{
		// Start app

		prepareBundle();
		final FlowPanel panel = new FlowPanel();
		final AddressBookModule addressBookModule = new AddressBookModule( panel );
		addressBookModule.onModuleLoad();

		final FlexTable contacts = (FlexTable) panel.getWidget( 1 );

		// Open dialog for Business Contact

		final ContactsServiceAsync contactsService = addressBookModule.getContactsService();

		contactsService.load( 5, new AsyncCallback<Contact>()
		{
			public void onFailure( Throwable caught )
			{
				throw new RuntimeException( caught );
			}

			public void onSuccess( final Contact businessContact )
			{
				final ContactDialog dialog = new ContactDialog( addressBookModule, businessContact );
				final GwtMetawidget contactMetawidget = (GwtMetawidget) ( (Grid) dialog.getWidget() ).getWidget( 0, 1 );

				executeAfterBuildWidgets( contactMetawidget, new Timer()
				{
					@Override
					public void run()
					{
						assertTrue( "Charles Montgomery".equals( contactMetawidget.getValue( "firstname" ) ) );
						assertTrue( "0".equals( contactMetawidget.getValue( "numberOfStaff" ) ) );

						Button editButton = (Button) ( (HorizontalPanel) ( (Facet) ( (FlexTable) contactMetawidget.getWidget( 0 ) ).getWidget( 11, 0 ) ).getWidget() ).getWidget( 2 );
						assertTrue( "Edit".equals( editButton.getText() ) );
						assertTrue( editButton.isVisible() );
						fireClickListeners( editButton );

						executeAfterBuildWidgets( contactMetawidget, new Timer()
						{
							@Override
							public void run()
							{
								contactMetawidget.setValue( 2, "numberOfStaff" );
								contactMetawidget.setValue( "A Company", "company" );

								// Check adding a Communication

								final FlexTable communications = (FlexTable) ( (Stub) contactMetawidget.getWidget( "communications" ) ).getWidget();
								assertTrue( communications.getRowCount() == 2 );

								final GwtMetawidget typeMetawidget = (GwtMetawidget) communications.getWidget( 1, 0 );

								executeAfterBuildWidgets( typeMetawidget, new Timer()
								{
									@Override
									public void run()
									{
										assertTrue( typeMetawidget.getWidget( "type" ) instanceof ListBox );
										typeMetawidget.setValue( "Mobile", "type" );
										final GwtMetawidget valueMetawidget = (GwtMetawidget) communications.getWidget( 1, 1 );

										executeAfterBuildWidgets( valueMetawidget, new Timer()
										{
											@Override
											public void run()
											{
												valueMetawidget.setValue( "(0402) 123 456", "value" );
												Button addButton = (Button) communications.getWidget( 1, 2 );
												assertTrue( "Add".equals( addButton.getText() ) );
												assertTrue( addButton.isVisible() );
												fireClickListeners( addButton );
												assertTrue( communications.getRowCount() == 3 );
												assertTrue( "Mobile".equals( communications.getText( 1, 0 ) ) );
												assertTrue( "(0402) 123 456".equals( communications.getText( 1, 1 ) ) );
												Button communicationsDeleteButton = (Button) communications.getWidget( 1, 2 );
												assertTrue( "Delete".equals( communicationsDeleteButton.getText() ) );
												assertTrue( communicationsDeleteButton.isVisible() );
												assertTrue( "table-buttons".equals( communications.getCellFormatter().getStyleName( 1, 2 ) ) );
												assertTrue( addButton.isVisible() );

												GwtMetawidget addressMetawidget = (GwtMetawidget) contactMetawidget.getWidget( "address" );

												executeAfterBuildWidgets( addressMetawidget, new Timer()
												{
													@Override
													public void run()
													{
														Button saveButton = (Button) ( (HorizontalPanel) ( (Facet) ( (FlexTable) contactMetawidget.getWidget( 0 ) ).getWidget( 11, 0 ) ).getWidget() ).getWidget( 0 );
														assertTrue( "Save".equals( saveButton.getText() ) );
														assertTrue( saveButton.isVisible() );
														fireClickListeners( saveButton );

														Timer timerReloadContacts = new Timer()
														{
															@Override
															public void run()
															{
																assertTrue( "Mr Charles Montgomery Burns".equals( contacts.getText( 1, 0 ) ) );
																assertTrue( "Mobile: (0402) 123 456".equals( contacts.getText( 1, 1 ) ) );
																assertTrue( ( (Image) contacts.getWidget( 1, 2 ) ).getUrl().endsWith( "media/business-small.gif" ) );

																finish();
															}
														};

														timerReloadContacts.schedule( CONTACTS_SERVICE_DELAY );
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

	public void testAddBusinessContact()
		throws Exception
	{
		// Start app

		prepareBundle();
		final FlowPanel panel = new FlowPanel();
		final AddressBookModule addressBookModule = new AddressBookModule( panel );
		addressBookModule.onModuleLoad();

		final FlexTable contacts = (FlexTable) panel.getWidget( 1 );

		// Open dialog for new Business Contact

		final Contact contact = new BusinessContact();
		final ContactDialog dialog = new ContactDialog( addressBookModule, contact );
		final GwtMetawidget contactMetawidget = (GwtMetawidget) ( (Grid) dialog.getWidget() ).getWidget( 0, 1 );

		executeAfterBuildWidgets( contactMetawidget, new Timer()
		{
			@Override
			public void run()
			{
				ListBox titleListBox = ((ListBox) contactMetawidget.getWidget( "title" ));
				assertTrue( "Mr".equals( titleListBox.getItemText( 0 )));
				assertTrue( "".equals( contactMetawidget.getValue( "firstname" ) ) );
				assertTrue( "0".equals( contactMetawidget.getValue( "numberOfStaff" ) ) );

				Button cancelButton = (Button) ( (HorizontalPanel) ( (Facet) ( (FlexTable) contactMetawidget.getWidget( 0 ) ).getWidget( 11, 0 ) ).getWidget() ).getWidget( 3 );
				assertTrue( "Cancel".equals( cancelButton.getText() ) );
				assertTrue( cancelButton.isVisible() );

				contactMetawidget.setValue( "Miss", "title" );
				contactMetawidget.setValue( "Business", "firstname" );
				contactMetawidget.setValue( "Contact", "surname" );

				// Check adding a Communication

				final FlexTable communications = (FlexTable) ( (Stub) contactMetawidget.getWidget( "communications" ) ).getWidget();
				assertTrue( communications.getRowCount() == 2 );

				final GwtMetawidget typeMetawidget = (GwtMetawidget) communications.getWidget( 1, 0 );

				executeAfterBuildWidgets( typeMetawidget, new Timer()
				{
					@Override
					public void run()
					{
						assertTrue( typeMetawidget.getWidget( "type" ) instanceof ListBox );
						typeMetawidget.setValue( "Mobile", "type" );
						final GwtMetawidget valueMetawidget = (GwtMetawidget) communications.getWidget( 1, 1 );

						executeAfterBuildWidgets( valueMetawidget, new Timer()
						{
							@Override
							public void run()
							{
								valueMetawidget.setValue( "(0402) 456 123", "value" );
								Button addButton = (Button) communications.getWidget( 1, 2 );
								assertTrue( "Add".equals( addButton.getText() ) );
								assertTrue( addButton.isVisible() );
								fireClickListeners( addButton );
								assertTrue( communications.getRowCount() == 3 );
								assertTrue( "Mobile".equals( communications.getText( 1, 0 ) ) );
								assertTrue( "(0402) 456 123".equals( communications.getText( 1, 1 ) ) );

								GwtMetawidget addressMetawidget = (GwtMetawidget) contactMetawidget.getWidget( "address" );

								executeAfterBuildWidgets( addressMetawidget, new Timer()
								{
									@Override
									public void run()
									{
										Button saveButton = (Button) ( (HorizontalPanel) ( (Facet) ( (FlexTable) contactMetawidget.getWidget( 0 ) ).getWidget( 11, 0 ) ).getWidget() ).getWidget( 0 );
										assertTrue( "Save".equals( saveButton.getText() ) );
										assertTrue( saveButton.isVisible() );
										fireClickListeners( saveButton );

										// Check appears after saving

										Timer timerReloadContacts = new Timer()
										{
											@Override
											public void run()
											{
												assertTrue( contacts.getRowCount() == 8 );

												assertTrue( "Miss Business Contact".equals( contacts.getText( 1, 0 ) ) );
												assertTrue( "Mobile: (0402) 456 123".equals( contacts.getText( 1, 1 ) ) );
												assertTrue( ( (Image) contacts.getWidget( 1, 2 ) ).getUrl().endsWith( "media/business-small.gif" ) );

												final ContactsServiceAsync contactsService = addressBookModule.getContactsService();

												// Check loading (new contact should have been assigned an id of 7)

												contactsService.load( 7, new AsyncCallback<Contact>()
												{
													public void onFailure( Throwable caught )
													{
														throw new RuntimeException( caught );
													}

													public void onSuccess( final Contact savedContact )
													{
														final ContactDialog dialogDelete = new ContactDialog( addressBookModule, savedContact );
														final GwtMetawidget deleteContactMetawidget = (GwtMetawidget) ( (Grid) dialogDelete.getWidget() ).getWidget( 0, 1 );

														executeAfterBuildWidgets( deleteContactMetawidget, new Timer()
														{
															@Override
															public void run()
															{
																assertTrue( "Business".equals( deleteContactMetawidget.getValue( "firstname" ) ) );
																assertTrue( "Contact".equals( deleteContactMetawidget.getValue( "surname" ) ) );

																// Check editing

																final Button editButton = (Button) ( (HorizontalPanel) ( (Facet) ( (FlexTable) deleteContactMetawidget.getWidget( 0 ) ).getWidget( 11, 0 ) ).getWidget() ).getWidget( 2 );
																assertTrue( "Edit".equals( editButton.getText() ) );
																assertTrue( editButton.isVisible() );
																fireClickListeners( editButton );

																executeAfterBuildWidgets( deleteContactMetawidget, new Timer()
																{
																	@Override
																	public void run()
																	{
																		assertTrue( !editButton.isVisible() );
																		ListBox deleteTitleListBox = ((ListBox) deleteContactMetawidget.getWidget( "title" ));
																		assertTrue( "Miss".equals( deleteTitleListBox.getItemText( deleteTitleListBox.getSelectedIndex() )));

																		// Check deleting

																		Button deleteButton = (Button) ( (HorizontalPanel) ( (Facet) ( (FlexTable) deleteContactMetawidget.getWidget( 0 ) ).getWidget( 11, 0 ) ).getWidget() ).getWidget( 1 );
																		assertTrue( "Delete".equals( deleteButton.getText() ) );
																		assertTrue( deleteButton.isVisible() );
																		fireClickListeners( deleteButton );

																		Timer timerReloadContactsAgain = new Timer()
																		{
																			@Override
																			public void run()
																			{
																				assertTrue( contacts.getRowCount() == 7 );
																				finish();
																			}
																		};

																		timerReloadContactsAgain.schedule( CONTACTS_SERVICE_DELAY );
																	}
																} );
															}
														} );
													}
												} );
											}
										};

										timerReloadContacts.schedule( CONTACTS_SERVICE_DELAY );
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
	// Protected methods
	//

	/**
	 * Wrapped to avoid 'synthetic access' warning
	 */

	void finish()
	{
		super.finishTest();
	}

	//
	// Native methods
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
			"dateOfBirth": "Date of Birth",
			"other": "Other",
			"save": "Save",
			"communications": "Communications",
			"street": "Street",
			"state": "State",
			"surname": "Surname",
			"cancel": "Cancel",
			"type": "Type",
			"add": "Add",
			"businessContact": "Business Contact",
			"firstname": "Firstname",
			"city": "City",
			"title": "Title",
			"search": "Search",
			"numberOfStaff": "Number of Staff",
			"delete": "Delete",
			"gender": "Gender",
			"contactDetails": "Contact Details",
			"addPersonal": "Add Personal Contact",
			"addBusiness": "Add Business Contact",
			"edit": "Edit",
			"back": "Back",
			"postcode": "Postcode",
			"address": "Address",
			"personalContact": "Personal Contact",
			"company": "Company",
			"notes": "Notes"
		};
	}-*/;
}
