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
	implements DialogInterface.OnClickListener
{
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

	protected CommunicationDialog( Activity activity, Contact contact, Communication communication, DialogInterface.OnClickListener listener )
	{
		super( activity );

		mActivity = activity;
		mContact = contact;
		mCommunication = communication;
		mListener = listener;

		mMetawidget = new AndroidMetawidget( activity );
		mMetawidget.setConfig( R.raw.inspector_config );
		Context context = getContext();

		if ( mCommunication == null )
		{
			setTitle( context.getString( R.string.addCommunication ));
			mMetawidget.setToInspect( new Communication() );

			setButton( context.getString( R.string.add ), this );
			setButton2( context.getString( R.string.cancel ), this );
		}
		else
		{
			setTitle( context.getString( R.string.editCommunication ));
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

	public void onClick( DialogInterface dialog, int button )
	{
		if ( button == DialogInterface.BUTTON1 )
		{
			// Manual mapping

			if ( mCommunication == null )
				mCommunication = new Communication();

			mCommunication.setType( (String) mMetawidget.getValue( "type" ) );
			mCommunication.setValue( (String) mMetawidget.getValue( "value" ) );

			try
			{
				mContact.addCommunication( mCommunication );
				mListener.onClick( dialog, button );
			}
			catch( Exception e )
			{
				Context context = getContext();

				new AlertDialog.Builder( getCurrentFocus().getContext() )
					.setTitle( context.getString( R.string.addError ) )
					.setMessage( e.getMessage() )
					.setPositiveButton( context.getString( R.string.ok ), null )
					.show();
			}
		}
		else if ( button == DialogInterface.BUTTON2 && mCommunication != null )
		{
			Context context = getContext();
			ConfirmDialog.show( mActivity, context.getString( R.string.deleteCommunication ), context.getString( R.string.confirmDeleteCommunication ), new DialogInterface.OnClickListener()
			{
				public void onClick( DialogInterface confirmDialog, int confirmButton )
				{
					if ( confirmButton == DialogInterface.BUTTON1 )
					{
						mContact.removeCommunication( mCommunication );
						mListener.onClick( confirmDialog, confirmButton );
					}
				}
			} );
		}
	}
}
