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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Convenience class for Yes/No confirmations.
 *
 * @author Richard Kennard
 */

public class ConfirmDialog
	extends AlertDialog
{
	//
	//
	// Public statics
	//
	//

	public static void show( Context context, CharSequence title, CharSequence message, DialogInterface.OnClickListener listener )
	{
		Dialog dialog = new ConfirmDialog( context, title, message, listener );
		dialog.show();
	}

	//
	//
	// Contructor
	//
	//

	protected ConfirmDialog( Context context, CharSequence title, CharSequence message, DialogInterface.OnClickListener listener )
	{
		super( context );

		setTitle( title );
		setMessage( message );
		setCancelable( true );

		setButton( context.getString( R.string.yes ), listener );
		setButton2( context.getString( R.string.no ), listener );
	}
}
