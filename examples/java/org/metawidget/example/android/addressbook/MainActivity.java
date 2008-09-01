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

import org.metawidget.android.AndroidUtils.ResourcelessArrayAdapter;
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
import android.widget.ListView;

/**
 * @author Richard Kennard
 */

public class MainActivity
	extends Activity
{
	//
	//
	// Protected members
	//
	//

	protected ContactSearch		mContactSearch;

	//
	//
	// Public methods
	//
	//

	@Override
	public void onCreate( Bundle bundle )
	{
		super.onCreate( bundle );

		mContactSearch = new ContactSearch();
		final AddressBookApplication application = (AddressBookApplication) getApplication();

		// Layout from resource

		setContentView( R.layout.main );

		// Metawidget

		final AndroidMetawidget metawidget = (AndroidMetawidget) findViewById( R.id.metawidget );
		metawidget.setToInspect( mContactSearch );

		// Contacts List

		final ListView contacts = (ListView) findViewById( R.id.contacts );
		contacts.setOnItemClickListener( new ListView.OnItemClickListener()
		{
			@SuppressWarnings( "unchecked" )
			public void onItemClick( AdapterView viewAdapter, View view, int position, long id )
			{
				Intent intent = new Intent();
				intent.setClass( MainActivity.this, ContactActivity.class );

				Contact contact = (Contact) viewAdapter.getAdapter().getItem( position );

				if ( contact == null )
					return;

				intent.putExtra( "contactId", contact.getId() );

				startActivity( intent );
			}
		} );
		contacts.setAdapter( new ResourcelessArrayAdapter<Contact>( this, application.getContactsController().getAllByExample( mContactSearch ), null ) );
	}

	@Override
	public boolean onCreateOptionsMenu( Menu menu )
	{
		super.onCreateOptionsMenu( menu );

		menu.add( R.string.search );
		menu.add( R.string.addPersonal );
		menu.add( R.string.addBusiness );

		return true;
	}

	@Override
	public boolean onOptionsItemSelected( MenuItem item )
	{
		AddressBookApplication application = (AddressBookApplication) getApplication();
		ListView contacts = (ListView) findViewById( R.id.contacts );
		AndroidMetawidget metawidget = (AndroidMetawidget) findViewById( R.id.metawidget );

		switch ( item.getItemId() )
		{
			case 0:
			{
				// Manual mapping

				mContactSearch.setFirstname( (String) metawidget.getValue( "firstname" ) );
				mContactSearch.setSurname( (String) metawidget.getValue( "surname" ) );

				String type = (String) metawidget.getValue( "type" );

				if ( type == null || "".equals( type ) )
					mContactSearch.setType( null );
				else
					mContactSearch.setType( ContactType.valueOf( type ) );

				contacts.setAdapter( new ResourcelessArrayAdapter<Contact>( MainActivity.this, application.getContactsController().getAllByExample( mContactSearch ), null ) );
				break;
			}

			case 1:
			{
				Intent intent = new Intent();
				intent.setClass( MainActivity.this, ContactActivity.class );
				intent.putExtra( "contactType", "personal" );

				startActivity( intent );
				break;
			}

			case 2:
			{
				Intent intent = new Intent();
				intent.setClass( MainActivity.this, ContactActivity.class );
				intent.putExtra( "contactType", "business" );

				startActivity( intent );
				break;
			}
		}

		return true;
	}

	//
	//
	// Protected methods
	//
	//

	@Override
	protected void onActivityResult( int requestCode, int resultCode, Intent data )
	{
		super.onActivityResult( requestCode, resultCode, data );

		ListView contacts = (ListView) findViewById( R.id.contacts );
		AddressBookApplication application = (AddressBookApplication) getApplication();
		contacts.setAdapter( new ResourcelessArrayAdapter<Contact>( MainActivity.this, application.getContactsController().getAllByExample( mContactSearch ), null ) );
	}
}
