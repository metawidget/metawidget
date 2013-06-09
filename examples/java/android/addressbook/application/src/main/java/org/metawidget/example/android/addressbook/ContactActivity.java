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

import java.text.DateFormat;
import java.util.Set;

import org.metawidget.android.widget.AndroidMetawidget;
import org.metawidget.android.widget.widgetprocessor.binding.simple.SimpleBindingProcessor;
import org.metawidget.example.shared.addressbook.model.BusinessContact;
import org.metawidget.example.shared.addressbook.model.Communication;
import org.metawidget.example.shared.addressbook.model.Contact;
import org.metawidget.example.shared.addressbook.model.PersonalContact;
import org.metawidget.util.CollectionUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Activity for Address Book Contacts.
 * <p>
 * Note: for brevity, this example is not optimized to reuse the Metawidget. For an example showing
 * reuse, see <code>org.metawidget.example.gwt.addressbook.client.ui.ContactDialog</code>.
 *
 * @author Richard Kennard
 */

public class ContactActivity
	extends Activity {

	//
	// Private statics
	//

	private static final int		MENU_GROUP_ID	= 0;

	private static final DateFormat	FORMAT			= DateFormat.getDateInstance( DateFormat.SHORT );

	static {
		FORMAT.setLenient( false );
	}

	//
	// Protected members
	//

	protected Contact				mContact;

	//
	// Public methods
	//

	@Override
	public void onCreate( Bundle bundle ) {

		super.onCreate( bundle );

		// Layout from resource

		setContentView( R.layout.contact );

		final AddressBookApplication application = (AddressBookApplication) getApplication();
		StringBuilder builderTitle = new StringBuilder();

		// Load contact

		Intent intent = getIntent();

		long id = intent.getLongExtra( "contactId", -1 );

		if ( id != -1 ) {
			mContact = application.getContactsController().load( id );
			builderTitle.append( mContact.getFullname() );
			builderTitle.append( " - " );
		}

		// Create contact

		else {
			String contactType = intent.getStringExtra( "contactType" );

			if ( "business".equals( contactType ) ) {
				mContact = new BusinessContact();
			} else {
				mContact = new PersonalContact();
			}
		}

		// Set title

		if ( mContact instanceof PersonalContact ) {
			builderTitle.append( getString( R.string.personalContact ) );
		} else {
			builderTitle.append( getString( R.string.businessContact ) );
		}

		setTitle( builderTitle );

		// Communications

		final ListView communicationsView = (ListView) findViewById( R.id.communications );
		Set<Communication> communications = mContact.getCommunications();

		if ( communications != null ) {
			communicationsView.setAdapter( new ArrayAdapter<Communication>( this, android.R.layout.simple_list_item_1, CollectionUtils.newArrayList( communications ) ) );
		}

		communicationsView.setOnItemClickListener( new ListView.OnItemClickListener() {

			public void onItemClick( AdapterView<?> viewAdapter, View view, int position, long itemId ) {

				Communication communication = (Communication) viewAdapter.getAdapter().getItem( position );

				new CommunicationDialog( ContactActivity.this, mContact, communication, new DialogInterface.OnClickListener() {

					public void onClick( DialogInterface dialog, int button ) {

						communicationsView.setAdapter( new ArrayAdapter<Communication>( ContactActivity.this, android.R.layout.simple_list_item_1, CollectionUtils.newArrayList( mContact.getCommunications() ) ) );
					}
				} ).show();
			}
		} );

		// Metawidget

		final AndroidMetawidget metawidget = (AndroidMetawidget) findViewById( R.id.metawidget );
		metawidget.setToInspect( mContact );

		if ( id == -1 ) {
			metawidget.setReadOnly( false );
		}

		if ( !metawidget.isReadOnly() ) {
			metawidget.buildWidgets();
			View view = metawidget.findViewWithTag( "title" );
			view.setFocusable( true );
			view.setFocusableInTouchMode( true );
			view.requestFocus();
		}
	}

	@Override
	public boolean onPrepareOptionsMenu( Menu menu ) {

		super.onPrepareOptionsMenu( menu );
		menu.clear();

		AndroidMetawidget metawidget = (AndroidMetawidget) findViewById( R.id.metawidget );

		if ( metawidget.isReadOnly() ) {
			menu.add( MENU_GROUP_ID, R.string.edit, 0, R.string.edit );
		} else {
			menu.add( MENU_GROUP_ID, R.string.addCommunication, 0, R.string.addCommunication );
			menu.add( MENU_GROUP_ID, R.string.save, 1, R.string.save );

			if ( mContact.getId() != -1 ) {
				menu.add( MENU_GROUP_ID, R.string.delete, 2, R.string.delete );
			}
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected( MenuItem item ) {

		final AddressBookApplication application = (AddressBookApplication) getApplication();
		final AndroidMetawidget metawidget = (AndroidMetawidget) findViewById( R.id.metawidget );

		switch ( item.getItemId() ) {
			case R.string.edit:
				metawidget.setReadOnly( false );
				metawidget.buildWidgets();
				View view = metawidget.findViewWithTag( "title" );
				view.setFocusable( true );
				view.setFocusableInTouchMode( true );
				view.requestFocus();
				break;

			case R.string.save:
				metawidget.getWidgetProcessor( SimpleBindingProcessor.class ).save( metawidget );

				try {
					application.getContactsController().save( mContact );
					finish();
				} catch ( Exception e ) {
					new AlertDialog.Builder( getCurrentFocus().getContext() ).setTitle( getString( R.string.saveError ) ).setMessage( e.getMessage() ).setPositiveButton( getString( R.string.ok ), null ).show();
				}
				break;

			case R.string.delete:
				ConfirmDialog.show( ContactActivity.this, getString( R.string.deleteContact ), getString( R.string.confirmDeleteContact ), new DialogInterface.OnClickListener() {

					public void onClick( DialogInterface dialog, int button ) {

						if ( button == DialogInterface.BUTTON1 ) {
							application.getContactsController().delete( mContact );
							finish();
						}
					}
				} );
				break;

			case R.string.addCommunication:
				new CommunicationDialog( ContactActivity.this, mContact, null, new DialogInterface.OnClickListener() {

					public void onClick( DialogInterface dialog, int button ) {

						ListView communicationsView = (ListView) findViewById( R.id.communications );
						communicationsView.setAdapter( new ArrayAdapter<Communication>( ContactActivity.this, android.R.layout.simple_list_item_1, CollectionUtils.newArrayList( mContact.getCommunications() ) ) );
					}
				} ).show();
				break;
		}

		return false;
	}
}