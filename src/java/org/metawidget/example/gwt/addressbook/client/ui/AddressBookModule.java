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
import org.metawidget.gwt.client.ui.Facet;
import org.metawidget.gwt.client.ui.GwtMetawidget;
import org.metawidget.gwt.client.ui.GwtUtils;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.ColumnFormatter;

/**
 * @author Richard Kennard
 */

public class AddressBookModule
	implements EntryPoint
{
	//
	//
	// Private members
	//
	//

	Panel					mPanel;

	ContactSearch			mContactSearch;

	FlexTable				mContacts;

	ContactsServiceAsync	mContactsService;

	//
	//
	// Constructor
	//
	//

	public AddressBookModule()
	{
		this( RootPanel.get() );
	}

	public AddressBookModule( Panel panel )
	{
		mPanel = panel;
	}

	//
	//
	// Public methods
	//
	//

	public void onModuleLoad()
	{
		// Model

		mContactsService = (ContactsServiceAsync) GWT.create( ContactsService.class );
		mContactSearch = new ContactSearch();

		// Results table

		// TODO: header style

		mContacts = new FlexTable();
		mContacts.setStyleName( "data-table" );

		ColumnFormatter formatter = mContacts.getColumnFormatter();
		formatter.setStyleName( 0, "column-half" );
		formatter.setStyleName( 1, "column-half" );

		reloadContacts();
		mContacts.addTableListener( new TableListener()
		{
			public void onCellClicked( SourcesTableEvents sender, final int row, int cell )
			{
				// Re-fetch the contacts and map it to the row...

				mContactsService.getAllByExample( mContactSearch, new AsyncCallback<List<Contact>>()
				{
					public void onFailure( Throwable caught )
					{
						Window.alert( caught.getMessage() );
					}

					public void onSuccess( List<Contact> contacts )
					{
						long contactId = contacts.get( row - 1 ).getId();

						// ...then load that row

						mContactsService.load( contactId, new AsyncCallback<Contact>()
						{
							public void onFailure( Throwable caught )
							{
								Window.alert( caught.getMessage() );
							}

							public void onSuccess( Contact contact )
							{
								new ContactDialog( AddressBookModule.this, contact ).show();
							}
						} );
					}
				} );
			}
		} );

		// Metawidget

		final GwtMetawidget metawidget = new GwtMetawidget();
		metawidget.setDictionaryName( "bundle" );
		metawidget.setParameter( "tableStyleName", "table-form" );
		metawidget.setParameter( "columnStyleNames", "table-label-column,table-component-column" );
		metawidget.setParameter( "buttonsStyleName", "buttons" );
		metawidget.setToInspect( mContactSearch );

		// Embedded buttons

		Facet buttonsFacet = new Facet();
		buttonsFacet.setName( "buttons" );
		metawidget.add( buttonsFacet );

		HorizontalPanel panel = new HorizontalPanel();
		buttonsFacet.add( panel );

		Dictionary dictionary = Dictionary.getDictionary( "bundle" );

		Button searchButton = new Button( dictionary.get( "search" ));
		searchButton.addClickListener( new ClickListener()
		{
			public void onClick( Widget sender )
			{
				// Example of manual mapping. See ContactDialog for an example of using automatic
				// Bindings

				mContactSearch.setFirstnames( (String) metawidget.getValue( "firstnames" ) );
				mContactSearch.setSurname( (String) metawidget.getValue( "surname" ) );

				String type = (String) metawidget.getValue( "type" );

				if ( type == null || "".equals( type ) )
					mContactSearch.setType( null );
				else
					mContactSearch.setType( ContactType.valueOf( type ) );

				reloadContacts();
			}
		} );
		panel.add( searchButton );

		Button addPersonalButton = new Button( dictionary.get( "addPersonal" ));
		addPersonalButton.addClickListener( new ClickListener()
		{
			public void onClick( Widget sender )
			{
				Contact contact = new PersonalContact();
				new ContactDialog( AddressBookModule.this, contact ).show();
			}
		} );
		panel.add( addPersonalButton );

		Button addBusinessButton = new Button( dictionary.get( "addBusiness" ));
		addBusinessButton.addClickListener( new ClickListener()
		{
			public void onClick( Widget sender )
			{
				Contact contact = new BusinessContact();
				new ContactDialog( AddressBookModule.this, contact ).show();
			}
		} );
		panel.add( addBusinessButton );

		// Add to either RootPanel or the given Panel (for unit tests)

		if ( mPanel instanceof RootPanel )
		{
			RootPanel.get( "metawidget" ).add( metawidget );
			RootPanel.get( "contacts" ).add( mContacts );
		}
		else
		{
			mPanel.add( metawidget );
			mPanel.add( mContacts );
		}
	}

	public Panel getPanel()
	{
		return mPanel;
	}

	public ContactsServiceAsync getContactsService()
	{
		return mContactsService;
	}

	public void reloadContacts()
	{
		// Header

		mContacts.setText( 0, 0, "Name" );
		mContacts.setText( 0, 1, "Contact" );

		// Contacts

		mContactsService.getAllByExample( mContactSearch, new AsyncCallback<List<Contact>>()
		{
			public void onFailure( Throwable caught )
			{
				Window.alert( caught.getMessage() );
			}

			public void onSuccess( List<Contact> contacts )
			{
				int row = 1;

				// Add the given contacts

				for ( Contact contact : contacts )
				{
					mContacts.setText( row, 0, contact.getFullname() );
					mContacts.setText( row, 1, GwtUtils.toString( contact.getCommunications(), ',' ) );

					Image image = new Image();

					if ( contact instanceof BusinessContact )
						image.setUrl( "media/business-small.gif" );
					else
						image.setUrl( "media/personal-small.gif" );

					mContacts.setWidget( row, 2, image );

					row++;
				}

				// Cleanup any extra rows

				while ( mContacts.getRowCount() > row )
				{
					mContacts.removeRow( row );
				}
			}
		} );
	}
}
