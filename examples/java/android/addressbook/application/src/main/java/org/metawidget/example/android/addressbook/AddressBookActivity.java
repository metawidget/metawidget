// Metawidget Examples (licensed under BSD License)
//
// Copyright (c) Richard Kennard
// All rights reserved
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
//
// * Redistributions of source code must retain the above copyright notice,
//   this list of conditions and the following disclaimer.
// * Redistributions in binary form must reproduce the above copyright notice,
//   this list of conditions and the following disclaimer in the documentation
//   and/or other materials provided with the distribution.
// * Neither the name of Richard Kennard nor the names of its contributors may
//   be used to endorse or promote products derived from this software without
//   specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
// OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
// OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
// OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
// OF THE POSSIBILITY OF SUCH DAMAGE.

package org.metawidget.example.android.addressbook;

import org.metawidget.android.widget.AndroidMetawidget;
import org.metawidget.android.widget.widgetprocessor.binding.simple.SimpleBindingProcessor;
import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.example.shared.addressbook.model.ContactSearch;

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
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
				metawidget.getWidgetProcessor( SimpleBindingProcessor.class ).save( metawidget );
				contacts.setAdapter( new ArrayAdapter<Contact>( AddressBookActivity.this, android.R.layout.simple_list_item_1, application.getContactsController().getAllByExample( mContactSearch ) ) );
			}
			break;

			case R.string.addPersonal: {
				Intent intent = new Intent();
				intent.setClass( AddressBookActivity.this, ContactActivity.class );
				intent.putExtra( "contactType", "personal" );

				startActivityForResult( intent, 0 );
			}
			break;

			case R.string.addBusiness: {
				Intent intent = new Intent();
				intent.setClass( AddressBookActivity.this, ContactActivity.class );
				intent.putExtra( "contactType", "business" );

				startActivityForResult( intent, 0 );
			}
			break;
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
