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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author Richard Kennard
 */

public class GwtAddressBookTest
	extends GWTTestCase
{
	//
	//
	// Private statics
	//
	//

	/**
	 * Time to execute the /contacts service.
	 */

	private final static int	CONTACTS_SERVICE_DELAY	= 1000;

	private final static int	TEST_FINISH_DELAY		= 500 * CONTACTS_SERVICE_DELAY;

	//
	//
	// Public methods
	//
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

		executeAfterBuildWidgets( metawidgetSearch, new Timer()
		{
			@Override
			public void run()
			{
				metawidgetSearch.setValue( "Simpson", "surname" );
				metawidgetSearch.setValue( ContactType.PERSONAL.name(), "type" );

				assertTrue( metawidgetSearch.getWidgetCount() == 1 );
				FlexTable flexTable = (FlexTable) metawidgetSearch.getWidget( 0 );

				assertTrue( flexTable.getRowCount() == 4 );
				Button buttonSearch = (Button) ( (HorizontalPanel) ( (Facet) flexTable.getWidget( 3, 0 ) ).getWidget() ).getWidget( 0 );
				assertTrue( "Search".equals( buttonSearch.getText() ) );
				fireClickListeners( buttonSearch );

				Timer timerResults = new Timer()
				{
					@Override
					public void run()
					{
						assertTrue( contacts.getRowCount() == 3 );

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
										assertTrue( contactMetawidget.findWidget( "firstnames" ) instanceof Label );
										assertTrue( "Homer".equals( contactMetawidget.getValue( "firstnames" ) ) );

										// TODO: check 'dateOfBirth' says 'Date of Birth'
										assertTrue( "5/12/56".equals( contactMetawidget.getValue( "dateOfBirth" ) ) );

										try
										{
											contactMetawidget.getValue( "bad-value" );
											assertTrue( false );
										}
										catch ( Exception e )
										{
											// Should throw Exception
										}

										assertTrue( ( (FlexTable) ( (Stub) contactMetawidget.findWidget( "communications" ) ).getWidget() ).getRowCount() == 3 );

										// Check editing

										assertTrue( ( (FlexTable) contactMetawidget.getWidget( 0 ) ).getRowCount() == 9 );
										Button editButton = (Button) ( (HorizontalPanel) ( (Facet) ( (FlexTable) contactMetawidget.getWidget( 0 ) ).getWidget( 8, 0 ) ).getWidget() ).getWidget( 2 );
										assertTrue( "Edit".equals( editButton.getText() ) );
										fireClickListeners( editButton );

										executeAfterBuildWidgets( contactMetawidget, new Timer()
										{
											@Override
											public void run()
											{
												assertTrue( contactMetawidget.findWidget( "title" ) instanceof ListBox );
												assertTrue( ((ListBox) contactMetawidget.findWidget( "title" )).getItemCount() == 6 );

												assertTrue( contactMetawidget.findWidget( "firstnames" ) instanceof TextBox );
												assertTrue( "Homer".equals( contactMetawidget.getValue( "firstnames" ) ) );
												assertTrue( "5/12/56".equals( contactMetawidget.getValue( "dateOfBirth" ) ) );

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

												final FlexTable communications = (FlexTable) ((Stub) contactMetawidget.findWidget( "communications" )).getWidget();
												fireClickListeners( (Button) communications.getWidget( 1, 2 ));
												assertTrue( communications.getRowCount() == 2 );

												// Save again

												Button saveButton = (Button) ( (HorizontalPanel) ( (Facet) ( (FlexTable) contactMetawidget.getWidget( 0 ) ).getWidget( 8, 0 ) ).getWidget() ).getWidget( 0 );
												assertTrue( "Save".equals( saveButton.getText() ) );
												fireClickListeners( saveButton );
												finish();
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

		final FlowPanel panel = new FlowPanel();
		final AddressBookModule addressBookModule = new AddressBookModule( panel );
		addressBookModule.onModuleLoad();

		final FlexTable contacts = (FlexTable) panel.getWidget( 1 );

		// Open dialog for Business Contact

		final ContactsServiceAsync contactsService = addressBookModule.getContactsService();

		contactsService.load( 6, new AsyncCallback<Contact>()
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
						assertTrue( "Charles Montgomery".equals( contactMetawidget.getValue( "firstnames" ) ) );
						assertTrue( "0".equals( contactMetawidget.getValue( "numberOfStaff" ) ) );

						Button editButton = (Button) ( (HorizontalPanel) ( (Facet) ( (FlexTable) contactMetawidget.getWidget( 0 ) ).getWidget( 9, 0 ) ).getWidget() ).getWidget( 2 );
						assertTrue( "Edit".equals( editButton.getText() ) );
						fireClickListeners( editButton );

						executeAfterBuildWidgets( contactMetawidget, new Timer()
						{
							@Override
							public void run()
							{
								contactMetawidget.setValue( 2, "numberOfStaff" );
								contactMetawidget.setValue( "A Company", "company" );

								// Check adding a Communication

								final FlexTable communications = (FlexTable) ( (Stub) contactMetawidget.findWidget( "communications" ) ).getWidget();
								assertTrue( communications.getRowCount() == 2 );

								final GwtMetawidget typeMetawidget = (GwtMetawidget) communications.getWidget( 1, 0 );

								executeAfterBuildWidgets( typeMetawidget, new Timer()
								{
									@Override
									public void run()
									{
										assertTrue( typeMetawidget.findWidget( "type" ) instanceof ListBox );
										typeMetawidget.setValue( "Mobile", "type" );
										final GwtMetawidget valueMetawidget = (GwtMetawidget) communications.getWidget( 1, 1 );

										executeAfterBuildWidgets( valueMetawidget, new Timer()
										{
											@Override
											public void run()
											{
												valueMetawidget.setValue( "(0402) 123 456", "value" );
												fireClickListeners( (Button) communications.getWidget( 1, 2 ) );
												assertTrue( communications.getRowCount() == 3 );

												Button saveButton = (Button) ( (HorizontalPanel) ( (Facet) ( (FlexTable) contactMetawidget.getWidget( 0 ) ).getWidget( 9, 0 ) ).getWidget() ).getWidget( 0 );
												assertTrue( "Save".equals( saveButton.getText() ) );
												fireClickListeners( saveButton );

												Timer timerReloadContacts = new Timer()
												{
													@Override
													public void run()
													{
														assertTrue( "Mobile: (0402) 123 456".equals( contacts.getText( 1, 1 )));

														final ContactDialog dialogDelete = new ContactDialog( addressBookModule, businessContact );
														final GwtMetawidget deleteContactMetawidget = (GwtMetawidget) ( (Grid) dialogDelete.getWidget() ).getWidget( 0, 1 );

														executeAfterBuildWidgets( deleteContactMetawidget, new Timer()
														{
															@Override
															public void run()
															{
																Button deleteButton = (Button) ( (HorizontalPanel) ( (Facet) ( (FlexTable) deleteContactMetawidget.getWidget( 0 ) ).getWidget( 9, 0 ) ).getWidget() ).getWidget( 1 );
																assertTrue( "Delete".equals( deleteButton.getText() ) );
																fireClickListeners( deleteButton );

																// Check deleting

																Timer timerReloadContactsAgain = new Timer()
																{
																	@Override
																	public void run()
																	{
																		assertTrue( contacts.getRowCount() == 6 );
																		finish();
																	}
																};

																timerReloadContactsAgain.schedule( CONTACTS_SERVICE_DELAY );
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
			"firstnames": "Firstnames",
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
			"postcode": "Postcode",
			"address": "Address",
			"personalContact": "Personal Contact",
			"company": "Company",
			"notes": "Notes"
		};
	}-*/;
}
