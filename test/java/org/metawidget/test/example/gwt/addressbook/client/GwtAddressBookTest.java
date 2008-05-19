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

public class GwtAddressBookTest
	extends GWTTestCase
{
	//
	//
	// Private statics
	//
	//

	private final static int	TIMER_SCHEDULE_DELAY	= 1000;

	private final static int	TEST_FINISH_DELAY		= 50 * TIMER_SCHEDULE_DELAY;

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
		final AddressBookModule addressBookModule = new AddressBookModule( panel );
		addressBookModule.onModuleLoad();

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
										fireClickListeners( editButton );

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
										};

										timerEditPersonalContact.schedule( TIMER_SCHEDULE_DELAY );
									}
								};

								timerPersonalContact.schedule( TIMER_SCHEDULE_DELAY );
							}
						} );
					}
				};

				// Fetch the results after giving /contacts time to finish

				assertTrue( contacts.getRowCount() == 7 );
				timerResults.schedule( TIMER_SCHEDULE_DELAY );
			}
		};

		// Run the search after giving /metawidget-inspector time to finish

		timerSearch.schedule( TIMER_SCHEDULE_DELAY );

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

				Timer timerBusinessContact = new Timer()
				{
					@Override
					public void run()
					{
						assertTrue( "Charles Montgomery".equals( contactMetawidget.getValue( "firstnames" ) ) );
						assertTrue( "0".equals( contactMetawidget.getValue( "numberOfStaff" ) ) );

						Button editButton = (Button) ( (HorizontalPanel) ( (Facet) ( (FlexTable) contactMetawidget.getWidget( 0 ) ).getWidget( 9, 0 ) ).getWidget() ).getWidget( 2 );
						assertTrue( "Edit".equals( editButton.getText() ) );
						fireClickListeners( editButton );

						Timer timerEditBusinessContact = new Timer()
						{
							@Override
							public void run()
							{
								contactMetawidget.setValue( 2, "numberOfStaff" );
								contactMetawidget.setValue( "A Company", "company" );

								// Check adding a Communication

								final FlexTable communications = (FlexTable) ( (Stub) contactMetawidget.findWidget( "communications" ) ).getWidget();
								assertTrue( communications.getRowCount() == 2 );
								assertTrue( ( (GwtMetawidget) communications.getWidget( 1, 0 ) ).findWidget( "type" ) instanceof ListBox );
								GwtMetawidget typeMetawidget = (GwtMetawidget) communications.getWidget( 1, 0 );
								typeMetawidget.setValue( "Mobile", "type" );
								GwtMetawidget valueMetawidget = (GwtMetawidget) communications.getWidget( 1, 1 );
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

										Timer timerBusinessContactDelete = new Timer()
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

												timerReloadContactsAgain.schedule( TIMER_SCHEDULE_DELAY );
											}
										};

										timerBusinessContactDelete.schedule( TIMER_SCHEDULE_DELAY );
									}
								};

								timerReloadContacts.schedule( TIMER_SCHEDULE_DELAY );
							}
						};

						timerEditBusinessContact.schedule( TIMER_SCHEDULE_DELAY );
					}
				};

				timerBusinessContact.schedule( TIMER_SCHEDULE_DELAY );
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
}
