package org.metawidget.test.example.gwt.addressbook.client;

import java.util.List;

import org.metawidget.example.gwt.addressbook.client.rpc.ContactsService;
import org.metawidget.example.gwt.addressbook.client.rpc.ContactsServiceAsync;
import org.metawidget.example.shared.addressbook.model.Contact;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

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
		Timer timer = new Timer()
		{
			@Override
			public void run()
			{
				ContactsServiceAsync contactsService = (ContactsServiceAsync) GWT.create( ContactsService.class );
				( (ServiceDefTarget) contactsService ).setServiceEntryPoint( GWT.getModuleBaseURL() + "contacts" );

				contactsService.getAllByExample( null, new AsyncCallback<List<Contact>>()
				{
					public void onFailure( Throwable caught )
					{
						throw new RuntimeException( caught );
					}

					public void onSuccess( List<Contact> contacts )
					{
						assertTrue( contacts.size() == 6 );
						finishTest();
					}
				} );

			}
		};

		delayTestFinish( 50000 );
		timer.schedule( 100 );
	}
}
