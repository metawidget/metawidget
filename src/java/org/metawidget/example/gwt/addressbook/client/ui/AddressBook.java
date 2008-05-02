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

package org.metawidget.example.gwt.addressbook.client.ui;

import java.util.List;

import org.metawidget.example.gwt.addressbook.client.rpc.ContactsService;
import org.metawidget.example.gwt.addressbook.client.rpc.ContactsServiceAsync;
import org.metawidget.example.shared.addressbook.model.BusinessContact;
import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.example.shared.addressbook.model.ContactSearch;
import org.metawidget.example.shared.addressbook.model.ContactType;
import org.metawidget.example.shared.addressbook.model.PersonalContact;
import org.metawidget.gwt.client.ui.GwtMetawidget;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Richard Kennard
 */

public class AddressBook
	implements EntryPoint
{
	//
	//
	// Private members
	//
	//

	ContactSearch			mContactSearch;

	FlexTable				mContacts;

	ContactsServiceAsync	mContactsService;

	//
	//
	// Public methods
	//
	//

	public void onModuleLoad()
	{
		// Model

		mContactsService = (ContactsServiceAsync) GWT.create( ContactsService.class );
		( (ServiceDefTarget) mContactsService ).setServiceEntryPoint( GWT.getModuleBaseURL() + "contacts" );

		mContactSearch = new ContactSearch();

		// Results table

		mContacts = new FlexTable();
		RootPanel.get( "contacts" ).add( mContacts );
		loadContacts();

		mContacts.addTableListener( new TableListener()
		{
			public void onCellClicked( SourcesTableEvents sender, int row, int cell )
			{
				mContactsService.load( 1, new AsyncCallback<Contact>()
				{
					public void onFailure( Throwable caught )
					{
						Window.alert( caught.getMessage() );
					}

					public void onSuccess( Contact contact )
					{
						new ContactDialog( mContactsService, contact ).show();
					}
				} );
			}
		} );

		// Metawidget

		final GwtMetawidget metawidget = new GwtMetawidget();
		metawidget.setToInspect( mContactSearch );
		metawidget.buildWidgets();
		RootPanel.get( "metawidget" ).add( metawidget );

		// Embedded buttons

		Button search = new Button( "Search" );
		search.addClickListener( new ClickListener()
		{
			public void onClick( Widget sender )
			{
				// Example of manual mapping. See ContactDialog for an example of using automatic
				// Bindings

				mContactSearch.setFirstnames( (String) metawidget.getValue( "firstnames" ) );
				mContactSearch.setSurname( (String) metawidget.getValue( "surname" ) );

				String type = (String) metawidget.getValue( "type" );

				if ( type == null )
					mContactSearch.setType( null );
				else
					mContactSearch.setType( ContactType.valueOf( type ) );

				loadContacts();
			}
		} );
		RootPanel.get( "search" ).add( search );

		Button addPersonal = new Button( "Add Personal" );
		addPersonal.addClickListener( new ClickListener()
		{
			public void onClick( Widget sender )
			{
				new ContactDialog( mContactsService, new PersonalContact() ).show();
			}
		} );
		RootPanel.get( "addPersonal" ).add( addPersonal );

		Button addBusiness = new Button( "Add Business" );
		addBusiness.addClickListener( new ClickListener()
		{
			public void onClick( Widget sender )
			{
				new ContactDialog( mContactsService, new BusinessContact() ).show();
			}
		} );
		RootPanel.get( "addBusiness" ).add( addBusiness );
	}

	//
	//
	// Package-level methods
	//
	//

	void loadContacts()
	{
		mContactsService.getAllByExample( mContactSearch, new AsyncCallback<List<Contact>>()
		{
			public void onFailure( Throwable caught )
			{
				Window.alert( caught.getMessage() );
			}

			public void onSuccess( List<Contact> contacts )
			{
				// Add the given contacts

				int numberOfContacts = contacts.size();

				for ( int loop = 0; loop < numberOfContacts; loop++ )
				{
					mContacts.setText( loop, 0, contacts.get( loop ).getFullname() );
				}

				// Remove any additional existing rows

				while( mContacts.getRowCount() > numberOfContacts )
				{
					mContacts.removeRow( numberOfContacts );
				}
			}
		} );
	}
}
