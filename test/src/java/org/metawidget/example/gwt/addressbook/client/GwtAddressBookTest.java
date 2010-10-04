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

package org.metawidget.example.gwt.addressbook.client;

import org.metawidget.example.gwt.addressbook.client.rpc.ContactsServiceAsync;
import org.metawidget.example.gwt.addressbook.client.ui.AddressBookModule;
import org.metawidget.example.gwt.addressbook.client.ui.ContactDialog;
import org.metawidget.example.shared.addressbook.model.BusinessContact;
import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.example.shared.addressbook.model.ContactType;
import org.metawidget.example.shared.addressbook.model.PersonalContact;
import org.metawidget.gwt.client.ui.Facet;
import org.metawidget.gwt.client.ui.GwtMetawidget;
import org.metawidget.gwt.client.ui.Stub;
import org.metawidget.gwt.client.widgetprocessor.binding.simple.SimpleBindingProcessor;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;

/**
 * @author Richard Kennard
 */

public class GwtAddressBookTest
	extends GWTTestCase {

	//
	// Private statics
	//

	/**
	 * Time to execute the /contacts service.
	 */

	private static final int	CONTACTS_SERVICE_DELAY	= 1000;

	private static final int	TEST_FINISH_DELAY		= 500 * CONTACTS_SERVICE_DELAY;

	//
	// Public methods
	//

	@Override
	public String getModuleName() {

		return "org.metawidget.example.gwt.addressbook.GwtAddressBookTest";
	}

	public void testPersonalContact()
		throws Exception {

		// Start app

		prepareBundle();
		FlowPanel panel = new FlowPanel();
		final AddressBookModule addressBookModule = new AddressBookModule( panel );
		addressBookModule.onModuleLoad();

		// Check searching

		final GwtMetawidget metawidgetSearch = (GwtMetawidget) panel.getWidget( 0 );
		final FlexTable contacts = (FlexTable) panel.getWidget( 1 );

		Timer timerAllContacts = new Timer() {

			@Override
			public void run() {

				assertTrue( contacts.getRowCount() == 7 );
				final FlexCellFormatter cellFormatter = contacts.getFlexCellFormatter();

				executeAfterBuildWidgets( metawidgetSearch, new Timer() {

					@Override
					public void run() {

						metawidgetSearch.setValue( "Simpson", "surname" );
						metawidgetSearch.setValue( ContactType.PERSONAL.name(), "type" );

						FlexTable flexTable = (FlexTable) metawidgetSearch.getWidget( 0 );

						assertTrue( flexTable.getRowCount() == 4 );
						final Button buttonSearch = (Button) ( (FlowPanel) ( (Facet) flexTable.getWidget( 3, 0 ) ).getWidget() ).getWidget( 0 );
						assertEquals( "Search", buttonSearch.getText() );
						fireClickEvent( buttonSearch );

						Timer timerPersonalResults = new Timer() {

							@Override
							public void run() {

								assertTrue( contacts.getRowCount() == 3 );
								assertEquals( "Name", contacts.getText( 0, 0 ) );
								assertEquals( "header", cellFormatter.getStyleName( 0, 0 ) );
								assertEquals( "Contact", contacts.getText( 0, 1 ) );
								assertEquals( "header", cellFormatter.getStyleName( 0, 1 ) );
								assertEquals( "&nbsp;", contacts.getHTML( 0, 2 ) );
								assertEquals( "header", cellFormatter.getStyleName( 0, 2 ) );
								assertEquals( "Mr Homer Simpson", contacts.getText( 1, 0 ) );
								assertEquals( "Telephone: (939) 555-0113", contacts.getText( 1, 1 ) );
								assertTrue( ( (Image) contacts.getWidget( 1, 2 ) ).getUrl().endsWith( "media/personal-small.gif" ) );

								// Open dialog for Personal Contact

								final ContactsServiceAsync contactsService = addressBookModule.getContactsService();

								contactsService.load( 1, new AsyncCallback<Contact>() {

									public void onFailure( Throwable caught ) {

										throw new RuntimeException( caught );
									}

									public void onSuccess( final Contact personalContact ) {

										final ContactDialog dialog = new ContactDialog( addressBookModule, personalContact );
										final GwtMetawidget contactMetawidget = (GwtMetawidget) ( (Grid) dialog.getWidget() ).getWidget( 0, 1 );

										executeAfterBuildWidgets( contactMetawidget, new Timer() {

											@Override
											public void run() {

												assertTrue( contactMetawidget.getWidget( "firstname" ) instanceof Label );
												assertEquals( "Homer", contactMetawidget.getValue( "firstname" ) );

												FlexTable contactFlexTable = (FlexTable) contactMetawidget.getWidget( 0 );
												assertEquals( "Date of Birth:", contactFlexTable.getText( 3, 0 ) );
												assertEquals( "5/12/56", contactMetawidget.getValue( "dateOfBirth" ) );
												assertEquals( "Contact Details", contactFlexTable.getText( 5, 0 ) );
												assertTrue( 2 == contactFlexTable.getFlexCellFormatter().getColSpan( 5, 0 ) );
												assertEquals( "section-heading", ( (Label) contactFlexTable.getWidget( 5, 0 ) ).getStyleName() );
												assertEquals( "Address:", contactFlexTable.getText( 6, 0 ) );

												try {
													contactMetawidget.getValue( "bad-value" );
													assertTrue( false );
												} catch ( Exception e ) {
													// Should throw Exception
												}

												final FlexTable communications = (FlexTable) ( (Stub) contactMetawidget.getWidget( "communications" ) ).getWidget( 0 );
												assertTrue( communications.getRowCount() == 3 );
												assertEquals( "Type", communications.getText( 0, 0 ) );
												assertEquals( "Value", communications.getText( 0, 1 ) );
												assertEquals( "&nbsp;", communications.getHTML( 0, 2 ) );
												assertTrue( 3 == communications.getCellCount( 0 ) );
												assertEquals( "Telephone", communications.getText( 1, 0 ) );
												assertEquals( "(939) 555-0113", communications.getText( 1, 1 ) );
												assertTrue( 2 == communications.getCellCount( 1 ) );

												// Check editing

												assertTrue( ( (FlexTable) contactMetawidget.getWidget( 0 ) ).getRowCount() == 11 );

												Button backButton = (Button) ( (FlowPanel) ( (Facet) ( (FlexTable) contactMetawidget.getWidget( 0 ) ).getWidget( 10, 0 ) ).getWidget() ).getWidget( 3 );
												assertEquals( "Back", backButton.getText() );
												assertTrue( backButton.isVisible() );

												final Button editButton = (Button) ( (FlowPanel) ( (Facet) ( (FlexTable) contactMetawidget.getWidget( 0 ) ).getWidget( 10, 0 ) ).getWidget() ).getWidget( 2 );
												assertEquals( "Edit", editButton.getText() );
												assertTrue( editButton.isVisible() );
												fireClickEvent( editButton );

												executeAfterBuildWidgets( contactMetawidget, new Timer() {

													@Override
													public void run() {

														assertFalse( editButton.isVisible() );
														assertTrue( contactMetawidget.getWidget( "title" ) instanceof ListBox );
														assertTrue( ( (ListBox) contactMetawidget.getWidget( "title" ) ).getItemCount() == 5 );

														assertTrue( contactMetawidget.getWidget( "firstname" ) instanceof TextBox );
														assertEquals( "Homer", contactMetawidget.getValue( "firstname" ) );
														assertEquals( "5/12/56", contactMetawidget.getValue( "dateOfBirth" ) );
														final Button deleteCommunication = (Button) communications.getWidget( 1, 2 );
														assertTrue( deleteCommunication.isVisible() );
														assertEquals( "Delete", deleteCommunication.getText() );
														assertEquals( "table-buttons", communications.getCellFormatter().getStyleName( 1, 2 ) );

														contactMetawidget.setValue( "Sapien", "surname" );

														// Check saving

														contactMetawidget.setValue( "foo", "dateOfBirth" );

														try {
															contactMetawidget.getWidgetProcessor( SimpleBindingProcessor.class ).save( contactMetawidget );
															assertTrue( false );
														} catch ( IllegalArgumentException e ) {
															assertEquals( "foo", e.getMessage() );
														}

														contactMetawidget.setValue( "12/05/57", "dateOfBirth" );

														// Check deleting a Communication

														assertTrue( communications.getRowCount() == 3 );
														fireClickEvent( deleteCommunication );
														assertTrue( communications.getRowCount() == 2 );

														// Save again

														GwtMetawidget addressMetawidget = contactMetawidget.getWidget( "address" );

														executeAfterBuildWidgets( addressMetawidget, new Timer() {

															@Override
															public void run() {

																assertEquals( "742 Evergreen Terrace", contactMetawidget.getValue( "address", "street" ) );
																assertEquals( "Anytown", contactMetawidget.getValue( "address", "state" ) );

																contactMetawidget.setValue( "743 Evergreen Terrace", "address", "street" );
																( (ListBox) contactMetawidget.getWidget( "address", "state" ) ).setSelectedIndex( 3 );

																Button cancelButton = (Button) ( (FlowPanel) ( (Facet) ( (FlexTable) contactMetawidget.getWidget( 0 ) ).getWidget( 10, 0 ) ).getWidget() ).getWidget( 3 );
																assertEquals( "Cancel", cancelButton.getText() );
																assertTrue( cancelButton.isVisible() );

																Button saveButton = (Button) ( (FlowPanel) ( (Facet) ( (FlexTable) contactMetawidget.getWidget( 0 ) ).getWidget( 10, 0 ) ).getWidget() ).getWidget( 0 );
																assertEquals( "Save", saveButton.getText() );
																assertTrue( saveButton.isVisible() );
																fireClickEvent( saveButton );

																Timer timerReloadContacts = new Timer() {

																	@Override
																	public void run() {

																		assertEquals( "Mrs Marjorie Simpson", contacts.getText( 1, 0 ) );

																		// Check surname change

																		metawidgetSearch.setValue( "", "surname" );
																		fireClickEvent( buttonSearch );

																		Timer timerReloadContactsAgain = new Timer() {

																			@Override
																			public void run() {

																				assertEquals( "Mr Homer Sapien", contacts.getText( 1, 0 ) );

																				// Check
																				// communication was
																				// deleted

																				assertEquals( "", contacts.getText( 1, 1 ) );

																				// Check address was
																				// changed (ie.
																				// exercises the
																				// nested Metawidgets
																				// in
																				// SimpleBindingProcessor.save)

																				assertEquals( "743 Evergreen Terrace", personalContact.getAddress().getStreet() );
																				assertEquals( "Lostville", personalContact.getAddress().getState() );

																				final ContactDialog newDialog = new ContactDialog( addressBookModule, personalContact );
																				final GwtMetawidget newContactMetawidget = (GwtMetawidget) ( (Grid) newDialog.getWidget() ).getWidget( 0, 1 );

																				executeAfterBuildWidgets( newContactMetawidget, new Timer() {

																					@Override
																					public void run() {

																						final GwtMetawidget newAddressMetawidget = (GwtMetawidget) ( (FlexTable) newContactMetawidget.getWidget( 0 ) ).getWidget( 6, 1 );

																						executeAfterBuildWidgets( newAddressMetawidget, new Timer() {

																							@Override
																							public void run() {

																								assertEquals( "743 Evergreen Terrace", newContactMetawidget.getValue( "address", "street" ) );
																								Label streetLabel = (Label) ( (FlexTable) newAddressMetawidget.getWidget( 0 ) ).getWidget( 0, 1 );
																								newContactMetawidget.setValue( "Foo", "address", "street" );
																								assertEquals( "Foo", newContactMetawidget.getValue( "address", "street" ) );
																								assertEquals( "Foo", streetLabel.getText() );

																								// Check
																								// rebinding
																								// works
																								// (ie.
																								// exercises
																								// the
																								// nested
																								// Metawidgets
																								// in
																								// SimpleBindingProcessor.rebind)

																								PersonalContact reboundPersonalContact = new PersonalContact();
																								reboundPersonalContact.getAddress().setStreet( "Bar" );

																								newContactMetawidget.getWidgetProcessor( SimpleBindingProcessor.class ).rebind( reboundPersonalContact, newContactMetawidget );
																								assertEquals( "Bar", newContactMetawidget.getValue( "address", "street" ) );
																								assertEquals( "Bar", streetLabel.getText() );
																								assertEquals( reboundPersonalContact, newContactMetawidget.getToInspect() );

																								finish();
																							}
																						} );
																					}
																				} );
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

						timerPersonalResults.schedule( CONTACTS_SERVICE_DELAY );
					}
				} );
			}
		};

		timerAllContacts.schedule( CONTACTS_SERVICE_DELAY );

		// Test runs asynchronously

		delayTestFinish( TEST_FINISH_DELAY );
	}

	public void testBusinessContact()
		throws Exception {

		// Start app

		prepareBundle();
		final FlowPanel panel = new FlowPanel();
		final AddressBookModule addressBookModule = new AddressBookModule( panel );
		addressBookModule.onModuleLoad();

		final FlexTable contacts = (FlexTable) panel.getWidget( 1 );

		// Open dialog for Business Contact

		final ContactsServiceAsync contactsService = addressBookModule.getContactsService();

		contactsService.load( 5, new AsyncCallback<Contact>() {

			public void onFailure( Throwable caught ) {

				throw new RuntimeException( caught );
			}

			public void onSuccess( final Contact businessContact ) {

				final ContactDialog dialog = new ContactDialog( addressBookModule, businessContact );
				final GwtMetawidget contactMetawidget = (GwtMetawidget) ( (Grid) dialog.getWidget() ).getWidget( 0, 1 );

				executeAfterBuildWidgets( contactMetawidget, new Timer() {

					@Override
					public void run() {

						assertEquals( "Charles Montgomery", contactMetawidget.getValue( "firstname" ) );
						assertEquals( "0", contactMetawidget.getValue( "numberOfStaff" ) );

						Button editButton = (Button) ( (FlowPanel) ( (Facet) ( (FlexTable) contactMetawidget.getWidget( 0 ) ).getWidget( 11, 0 ) ).getWidget() ).getWidget( 2 );
						assertEquals( "Edit", editButton.getText() );
						assertTrue( editButton.isVisible() );
						fireClickEvent( editButton );

						executeAfterBuildWidgets( contactMetawidget, new Timer() {

							@Override
							public void run() {

								FlexTable flexTable = (FlexTable) contactMetawidget.getWidget( 0 );
								assertEquals( "table-form", flexTable.getStyleName() );
								assertEquals( "*", flexTable.getText( 0, 2 ) );
								assertEquals( "table-label-column", flexTable.getCellFormatter().getStyleName( 0, 0 ) );
								assertEquals( "table-component-column", flexTable.getCellFormatter().getStyleName( 0, 1 ) );
								assertEquals( "table-required-column", flexTable.getCellFormatter().getStyleName( 0, 2 ) );
								assertEquals( "*", flexTable.getText( 1, 2 ) );
								assertEquals( "*", flexTable.getText( 2, 2 ) );
								assertEquals( "", flexTable.getText( 3, 2 ) );
								assertEquals( "<DIV></DIV>", flexTable.getHTML( 3, 2 ) );
								assertEquals( "table-label-column", flexTable.getCellFormatter().getStyleName( 3, 0 ) );
								assertEquals( "table-component-column", flexTable.getCellFormatter().getStyleName( 3, 1 ) );
								assertEquals( "table-required-column", flexTable.getCellFormatter().getStyleName( 3, 2 ) );
								contactMetawidget.setValue( 2, "numberOfStaff" );
								contactMetawidget.setValue( "A Company", "company" );

								// Check adding a Communication

								final FlexTable communications = (FlexTable) ( (Stub) contactMetawidget.getWidget( "communications" ) ).getWidget( 0 );
								assertTrue( communications.getRowCount() == 2 );

								final GwtMetawidget typeMetawidget = (GwtMetawidget) communications.getWidget( 1, 0 );

								executeAfterBuildWidgets( typeMetawidget, new Timer() {

									@Override
									public void run() {

										assertTrue( typeMetawidget.getWidget( "type" ) instanceof ListBox );
										typeMetawidget.setValue( "Mobile", "type" );
										final GwtMetawidget valueMetawidget = (GwtMetawidget) communications.getWidget( 1, 1 );

										executeAfterBuildWidgets( valueMetawidget, new Timer() {

											@Override
											public void run() {

												valueMetawidget.setValue( "(0402) 123 456", "value" );
												Button addButton = (Button) communications.getWidget( 1, 2 );
												assertEquals( "Add", addButton.getText() );
												assertTrue( addButton.isVisible() );
												fireClickEvent( addButton );
												assertTrue( communications.getRowCount() == 3 );
												assertEquals( "Mobile", communications.getText( 1, 0 ) );
												assertEquals( "(0402) 123 456", communications.getText( 1, 1 ) );
												Button communicationsDeleteButton = (Button) communications.getWidget( 1, 2 );
												assertEquals( "Delete", communicationsDeleteButton.getText() );
												assertTrue( communicationsDeleteButton.isVisible() );
												assertEquals( "table-buttons", communications.getCellFormatter().getStyleName( 1, 2 ) );
												assertTrue( addButton.isVisible() );

												GwtMetawidget addressMetawidget = contactMetawidget.getWidget( "address" );

												executeAfterBuildWidgets( addressMetawidget, new Timer() {

													@Override
													public void run() {

														Button saveButton = (Button) ( (FlowPanel) ( (Facet) ( (FlexTable) contactMetawidget.getWidget( 0 ) ).getWidget( 11, 0 ) ).getWidget() ).getWidget( 0 );
														assertEquals( "Save", saveButton.getText() );
														assertTrue( saveButton.isVisible() );
														fireClickEvent( saveButton );

														Timer timerReloadContacts = new Timer() {

															@Override
															public void run() {

																assertEquals( "Mr Charles Montgomery Burns", contacts.getText( 1, 0 ) );
																assertEquals( "Mobile: (0402) 123 456", contacts.getText( 1, 1 ) );
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
		throws Exception {

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

		executeAfterBuildWidgets( contactMetawidget, new Timer() {

			@Override
			public void run() {

				ListBox titleListBox = ( (ListBox) contactMetawidget.getWidget( "title" ) );
				assertEquals( "Mr", titleListBox.getItemText( 0 ) );
				assertEquals( "", contactMetawidget.getValue( "firstname" ) );
				assertEquals( "0", contactMetawidget.getValue( "numberOfStaff" ) );

				Button cancelButton = (Button) ( (FlowPanel) ( (Facet) ( (FlexTable) contactMetawidget.getWidget( 0 ) ).getWidget( 11, 0 ) ).getWidget() ).getWidget( 3 );
				assertEquals( "Cancel", cancelButton.getText() );
				assertTrue( cancelButton.isVisible() );

				contactMetawidget.setValue( "Miss", "title" );
				contactMetawidget.setValue( "Business", "firstname" );
				contactMetawidget.setValue( "Contact", "surname" );

				// Check adding a Communication

				final FlexTable communications = (FlexTable) ( (Stub) contactMetawidget.getWidget( "communications" ) ).getWidget( 0 );
				assertTrue( communications.getRowCount() == 2 );

				final GwtMetawidget typeMetawidget = (GwtMetawidget) communications.getWidget( 1, 0 );

				executeAfterBuildWidgets( typeMetawidget, new Timer() {

					@Override
					public void run() {

						assertTrue( typeMetawidget.getWidget( "type" ) instanceof ListBox );
						typeMetawidget.setValue( "Mobile", "type" );
						final GwtMetawidget valueMetawidget = (GwtMetawidget) communications.getWidget( 1, 1 );

						executeAfterBuildWidgets( valueMetawidget, new Timer() {

							@Override
							public void run() {

								valueMetawidget.setValue( "(0402) 456 123", "value" );
								Button addButton = (Button) communications.getWidget( 1, 2 );
								assertEquals( "Add", addButton.getText() );
								assertTrue( addButton.isVisible() );
								fireClickEvent( addButton );
								assertTrue( communications.getRowCount() == 3 );
								assertEquals( "Mobile", communications.getText( 1, 0 ) );
								assertEquals( "(0402) 456 123", communications.getText( 1, 1 ) );

								GwtMetawidget addressMetawidget = contactMetawidget.getWidget( "address" );

								executeAfterBuildWidgets( addressMetawidget, new Timer() {

									@Override
									public void run() {

										Button saveButton = (Button) ( (FlowPanel) ( (Facet) ( (FlexTable) contactMetawidget.getWidget( 0 ) ).getWidget( 11, 0 ) ).getWidget() ).getWidget( 0 );
										assertEquals( "Save", saveButton.getText() );
										assertTrue( saveButton.isVisible() );
										fireClickEvent( saveButton );

										// Check appears after saving

										Timer timerReloadContacts = new Timer() {

											@Override
											public void run() {

												assertTrue( contacts.getRowCount() == 8 );

												assertEquals( "Miss Business Contact", contacts.getText( 1, 0 ) );
												assertEquals( "Mobile: (0402) 456 123", contacts.getText( 1, 1 ) );
												assertTrue( ( (Image) contacts.getWidget( 1, 2 ) ).getUrl().endsWith( "media/business-small.gif" ) );

												final ContactsServiceAsync contactsService = addressBookModule.getContactsService();

												// Check loading (new contact should have been
												// assigned an id of 7)

												contactsService.load( 7, new AsyncCallback<Contact>() {

													public void onFailure( Throwable caught ) {

														throw new RuntimeException( caught );
													}

													public void onSuccess( final Contact savedContact ) {

														final ContactDialog dialogDelete = new ContactDialog( addressBookModule, savedContact );
														final GwtMetawidget deleteContactMetawidget = (GwtMetawidget) ( (Grid) dialogDelete.getWidget() ).getWidget( 0, 1 );

														executeAfterBuildWidgets( deleteContactMetawidget, new Timer() {

															@Override
															public void run() {

																assertEquals( "Business", deleteContactMetawidget.getValue( "firstname" ) );
																assertEquals( "Contact", deleteContactMetawidget.getValue( "surname" ) );

																// Check editing

																final Button editButton = (Button) ( (FlowPanel) ( (Facet) ( (FlexTable) deleteContactMetawidget.getWidget( 0 ) ).getWidget( 11, 0 ) ).getWidget() ).getWidget( 2 );
																assertEquals( "Edit", editButton.getText() );
																assertTrue( editButton.isVisible() );
																fireClickEvent( editButton );

																executeAfterBuildWidgets( deleteContactMetawidget, new Timer() {

																	@Override
																	public void run() {

																		assertFalse( editButton.isVisible() );
																		ListBox deleteTitleListBox = ( (ListBox) deleteContactMetawidget.getWidget( "title" ) );
																		assertEquals( "Miss", deleteTitleListBox.getItemText( deleteTitleListBox.getSelectedIndex() ) );

																		// Check deleting

																		Button deleteButton = (Button) ( (FlowPanel) ( (Facet) ( (FlexTable) deleteContactMetawidget.getWidget( 0 ) ).getWidget( 11, 0 ) ).getWidget() ).getWidget( 1 );
																		assertEquals( "Delete", deleteButton.getText() );
																		assertTrue( deleteButton.isVisible() );
																		fireClickEvent( deleteButton );

																		Timer timerReloadContactsAgain = new Timer() {

																			@Override
																			public void run() {

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
	// Private methods
	//

	/**
	 * Wrapped to avoid 'synthetic access' warning
	 */

	/* package private */void finish() {

		super.finishTest();
	}

	/* package private */void fireClickEvent( HasHandlers widget ) {

		Document document = Document.get();
		NativeEvent nativeEvent = document.createClickEvent( 0, 0, 0, 0, 0, false, false, false, false );
		DomEvent.fireNativeEvent( nativeEvent, widget );
	}

	//
	// Native methods
	//

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
