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
import org.metawidget.example.shared.addressbook.model.Communication;
import org.metawidget.example.shared.addressbook.model.Contact;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Dialog for editing Communications.
 *
 * @author Richard Kennard
 */

public class CommunicationDialog
	extends AlertDialog
	implements DialogInterface.OnClickListener {

	//
	// Protected members
	//

	protected Contact							mContact;

	protected Communication						mCommunication;

	protected DialogInterface.OnClickListener	mListener;

	//
	// Private members
	//

	private Activity							mActivity;

	private AndroidMetawidget					mMetawidget;

	//
	// Contructor
	//

	protected CommunicationDialog( Activity activity, Contact contact, Communication communication, DialogInterface.OnClickListener listener ) {

		super( activity );

		mActivity = activity;
		mContact = contact;
		mCommunication = communication;
		mListener = listener;

		mMetawidget = new AndroidMetawidget( activity );
		mMetawidget.setConfig( R.raw.metawidget );
		Context context = getContext();

		if ( mCommunication == null ) {
			setTitle( context.getString( R.string.addCommunication ) );
			mMetawidget.setToInspect( new Communication() );

			setButton( context.getString( R.string.add ), this );
			setButton2( context.getString( R.string.cancel ), this );
		} else {
			setTitle( context.getString( R.string.editCommunication ) );
			mMetawidget.setToInspect( mCommunication );

			// Manual mapping

			mMetawidget.setValue( mCommunication.getType(), "type" );
			mMetawidget.setValue( mCommunication.getValue(), "value" );

			setButton( context.getString( R.string.save ), this );
			setButton2( context.getString( R.string.delete ), this );
		}

		mMetawidget.setPadding( 5, 5, 5, 5 );

		setView( mMetawidget );
		setCancelable( true );
	}

	public void onClick( DialogInterface dialog, int button ) {

		if ( button == DialogInterface.BUTTON1 ) {
			// Manual mapping

			if ( mCommunication == null ) {
				mCommunication = new Communication();
			}

			mCommunication.setType( (String) mMetawidget.getValue( "type" ) );
			mCommunication.setValue( (String) mMetawidget.getValue( "value" ) );

			try {
				mContact.addCommunication( mCommunication );
				mListener.onClick( dialog, button );
			} catch ( Exception e ) {
				Context context = getContext();

				new AlertDialog.Builder( getCurrentFocus().getContext() ).setTitle( context.getString( R.string.addError ) ).setMessage( e.getMessage() ).setPositiveButton( context.getString( R.string.ok ), null ).show();
			}
		} else if ( button == DialogInterface.BUTTON2 && mCommunication != null ) {
			Context context = getContext();
			ConfirmDialog.show( mActivity, context.getString( R.string.deleteCommunication ), context.getString( R.string.confirmDeleteCommunication ), new DialogInterface.OnClickListener() {

				public void onClick( DialogInterface confirmDialog, int confirmButton ) {

					if ( confirmButton == DialogInterface.BUTTON1 ) {
						mContact.removeCommunication( mCommunication );
						mListener.onClick( confirmDialog, confirmButton );
					}
				}
			} );
		}
	}
}
