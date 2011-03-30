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

package org.metawidget.example.android.addressbook;

import org.metawidget.android.widget.AndroidMetawidget;
import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.example.shared.addressbook.model.ContactSearch;
import org.metawidget.example.shared.addressbook.model.ContactType;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * @author Richard Kennard
 */

public class AddressBookActivity
	extends Activity {

	//
	// Private statics
	//

	private static final int	MENU_GROUP_ID	= 0;

	//
	// Protected members
	//

	protected ContactSearch		mContactSearch;

	//
	// Public methods
	//

	@Override
	public void onCreate( Bundle bundle ) {

		super.onCreate( bundle );

		mContactSearch = new ContactSearch();
		final AddressBookApplication application = (AddressBookApplication) getApplication();

		// Layout from resource

		setContentView( R.layout.main );

		// Metawidget

		final AndroidMetawidget metawidget = (AndroidMetawidget) findViewById( R.id.metawidget );
		metawidget.setToInspect( mContactSearch );

		// Contacts List

		final ListView contactsView = (ListView) findViewById( R.id.contacts );
		contactsView.setOnItemClickListener( new ListView.OnItemClickListener() {

			public void onItemClick( AdapterView<?> viewAdapter, View view, int position, long id ) {

				Intent intent = new Intent();
				intent.setClass( AddressBookActivity.this, ContactActivity.class );

				Contact contact = (Contact) viewAdapter.getAdapter().getItem( position );

				if ( contact == null ) {
					return;
				}

				intent.putExtra( "contactId", contact.getId() );

				startActivityForResult( intent, 0 );
			}
		} );
		contactsView.setAdapter( new ArrayAdapter<Contact>( this, android.R.layout.simple_list_item_1, application.getContactsController().getAllByExample( mContactSearch ) ) );
	}

	@Override
	public boolean onCreateOptionsMenu( Menu menu ) {

		super.onCreateOptionsMenu( menu );

		menu.add( MENU_GROUP_ID, R.string.search, 0, R.string.search );
		menu.add( MENU_GROUP_ID, R.string.addPersonal, 1, R.string.addPersonal );
		menu.add( MENU_GROUP_ID, R.string.addBusiness, 2, R.string.addBusiness );

		return true;
	}

	@Override
	public boolean onOptionsItemSelected( MenuItem item ) {

		AddressBookApplication application = (AddressBookApplication) getApplication();
		ListView contacts = (ListView) findViewById( R.id.contacts );
		AndroidMetawidget metawidget = (AndroidMetawidget) findViewById( R.id.metawidget );

		switch ( item.getItemId() ) {
			case R.string.search: {
				// Manual mapping

				mContactSearch.setFirstname( (String) metawidget.getValue( "firstname" ) );
				mContactSearch.setSurname( (String) metawidget.getValue( "surname" ) );

				String type = metawidget.getValue( "type" );

				if ( type == null || "".equals( type ) ) {
					mContactSearch.setType( null );
				} else {
					mContactSearch.setType( ContactType.valueOf( type ) );
				}

				contacts.setAdapter( new ArrayAdapter<Contact>( AddressBookActivity.this, android.R.layout.simple_list_item_1, application.getContactsController().getAllByExample( mContactSearch ) ) );
				break;
			}

			case R.string.addPersonal: {
				Intent intent = new Intent();
				intent.setClass( AddressBookActivity.this, ContactActivity.class );
				intent.putExtra( "contactType", "personal" );

				startActivityForResult( intent, 0 );
				break;
			}

			case R.string.addBusiness: {
				Intent intent = new Intent();
				intent.setClass( AddressBookActivity.this, ContactActivity.class );
				intent.putExtra( "contactType", "business" );

				startActivityForResult( intent, 0 );
				break;
			}
		}

		return true;
	}

	//
	// Protected methods
	//

	@Override
	protected void onActivityResult( int requestCode, int resultCode, Intent data ) {

		super.onActivityResult( requestCode, resultCode, data );

		ListView contacts = (ListView) findViewById( R.id.contacts );
		AddressBookApplication application = (AddressBookApplication) getApplication();
		contacts.setAdapter( new ArrayAdapter<Contact>( AddressBookActivity.this, android.R.layout.simple_list_item_1, application.getContactsController().getAllByExample( mContactSearch ) ) );
	}
}
