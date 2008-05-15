package org.metawidget.test.example.gwt.addressbook.client;

import org.metawidget.example.gwt.addressbook.client.ui.AddressBook;
import org.metawidget.example.shared.addressbook.model.ContactSearch;
import org.metawidget.example.shared.addressbook.model.ContactType;
import org.metawidget.gwt.client.ui.Facet;
import org.metawidget.gwt.client.ui.GwtMetawidget;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;

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

	public void testAddressBook()
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
				Button buttonSearch = (Button) ((HorizontalPanel) ((Facet) flexTable.getWidget( 3, 0 )).getWidget()).getWidget( 0 );
				assertTrue( "Search".equals( buttonSearch.getText() ) );

				// Manually fire 'buttonSearch.onClick'

				((ContactSearch) metawidgetSearch.getToInspect()).setFirstnames( (String) metawidgetSearch.getValue( "firstnames" ) );
				((ContactSearch) metawidgetSearch.getToInspect()).setSurname( (String) metawidgetSearch.getValue( "surname" ) );
				((ContactSearch) metawidgetSearch.getToInspect()).setType( ContactType.valueOf( (String) metawidgetSearch.getValue( "type" ) ));

				addressBook.reloadContacts();

				Timer timerResults = new Timer()
				{
					@Override
					public void run()
					{
						assertTrue( contacts.getRowCount() == 3 );
						finishTest();
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
}
