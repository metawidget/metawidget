package org.metawidget.test.example.gwt.addressbook.client;

import org.metawidget.example.gwt.addressbook.client.rpc.ContactsServiceAsync;
import org.metawidget.example.gwt.addressbook.client.ui.AddressBook;
import org.metawidget.example.gwt.addressbook.client.ui.ContactDialog;
import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.example.shared.addressbook.model.ContactSearch;
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
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

@SuppressWarnings( "synthetic-access" )
public class GwtAddressBookTest
	extends GWTTestCase
{
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

		FlowPanel panel = new FlowPanel();
		final AddressBook addressBook = new AddressBook( panel );
		addressBook.onModuleLoad();

		// Check searching

		final GwtMetawidget metawidgetSearch = (GwtMetawidget) panel.getWidget( 0 );
		final FlexTable contacts = (FlexTable) panel.getWidget( 1 );

		Timer timerSearch = new Timer()
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

				// Manually fire 'buttonSearch.onClick'

				final ContactSearch contactSearch = (ContactSearch) metawidgetSearch.getToInspect();
				contactSearch.setFirstnames( (String) metawidgetSearch.getValue( "firstnames" ) );
				contactSearch.setSurname( (String) metawidgetSearch.getValue( "surname" ) );
				contactSearch.setType( ContactType.valueOf( (String) metawidgetSearch.getValue( "type" ) ) );

				addressBook.reloadContacts();

				Timer timerResults = new Timer()
				{
					@Override
					public void run()
					{
						assertTrue( contacts.getRowCount() == 3 );

						// Open dialog for Personal Contact

						final ContactsServiceAsync contactsService = addressBook.getContactsService();

						contactsService.load( 1, new AsyncCallback<Contact>()
						{
							public void onFailure( Throwable caught )
							{
								throw new RuntimeException( caught );
							}

							public void onSuccess( final Contact personalContact )
							{
								ContactDialog dialog = new ContactDialog( addressBook, personalContact );
								final GwtMetawidget contactMetawidget = (GwtMetawidget) ( (Grid) dialog.getWidget() ).getWidget( 0, 1 );

								Timer timerPersonalContact = new Timer()
								{
									@Override
									public void run()
									{
										assertTrue( contactMetawidget.findWidget( "firstnames" ) instanceof Label );
										assertTrue( "Homer".equals( contactMetawidget.getValue( "firstnames" ) ) );
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

										// Manually fire 'editButton.onClick'

										contactMetawidget.setReadOnly( false );
										contactMetawidget.buildWidgets();

										Timer timerEditPersonalContact = new Timer()
										{
											@Override
											public void run()
											{
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
												Button saveButton = (Button) ( (HorizontalPanel) ( (Facet) ( (FlexTable) contactMetawidget.getWidget( 0 ) ).getWidget( 8, 0 ) ).getWidget() ).getWidget( 0 );
												assertTrue( "Save".equals( saveButton.getText() ) );

												// Save

												contactMetawidget.save();

												contactsService.save( personalContact, new AsyncCallback<Object>()
												{
													public void onFailure( Throwable caught )
													{
														throw new RuntimeException( caught );
													}

													public void onSuccess( Object result )
													{
														finishTest();
													}
												} );
											}
										};

										timerEditPersonalContact.schedule( 1000 );
									}
								};

								timerPersonalContact.schedule( 1000 );
							}
						} );
					}
				};

				// Fetch the results after giving /contacts time to finish

				assertTrue( contacts.getRowCount() == 7 );
				timerResults.schedule( 1000 );
			}
		};

		// Run the search after giving /metawidget-inspector time to finish

		timerSearch.schedule( 1000 );

		// Test runs asynchronously

		delayTestFinish( 50000 );
	}

	public void testBusinessContact()
		throws Exception
	{
		// Start app

		FlowPanel panel = new FlowPanel();
		final AddressBook addressBook = new AddressBook( panel );
		addressBook.onModuleLoad();

		// Open dialog for Business Contact

		final ContactsServiceAsync contactsService = addressBook.getContactsService();

		contactsService.load( 6, new AsyncCallback<Contact>()
		{
			public void onFailure( Throwable caught )
			{
				throw new RuntimeException( caught );
			}

			public void onSuccess( final Contact personalContact )
			{
				ContactDialog dialog = new ContactDialog( addressBook, personalContact );
				final GwtMetawidget contactMetawidget = (GwtMetawidget) ( (Grid) dialog.getWidget() ).getWidget( 0, 1 );

				Timer timerBusinessContact = new Timer()
				{
					@Override
					public void run()
					{
						assertTrue( "Charles Montgomery".equals( contactMetawidget.getValue( "firstnames" ) ) );
						assertTrue( "0".equals( contactMetawidget.getValue( "numberOfStaff" ) ) );

						// Manually fire 'editButton.onClick'

						contactMetawidget.setReadOnly( false );
						contactMetawidget.buildWidgets();

						Timer timerEditBusinessContact = new Timer()
						{
							@Override
							public void run()
							{
								// Check saving

								contactMetawidget.setValue( 2, "numberOfStaff" );
								contactMetawidget.setValue( "A Company", "company" );

								assertTrue( ( (FlexTable) contactMetawidget.getWidget( 0 ) ).getRowCount() == 10 );
								contactMetawidget.save();

								// Check deleting

								contactsService.delete( (Contact) contactMetawidget.getToInspect(), new AsyncCallback<Boolean>()
								{
									public void onFailure( Throwable caught )
									{
										throw new RuntimeException( caught );
									}

									public void onSuccess( Boolean result )
									{
										finishTest();
									}
								} );
							}
						};

						timerEditBusinessContact.schedule( 1000 );
					}
				};

				timerBusinessContact.schedule( 1000 );
			}
		} );

		// Test runs asynchronously

		delayTestFinish( 50000 );
	}
}
