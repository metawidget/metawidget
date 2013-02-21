// Metawidget (licensed under LGPL)
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

package android.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Dummy implementation for unit testing.
 *
 * @author Richard Kennard
 */

public class AdapterView<T extends Adapter>
	extends ViewGroup {

	//
	// Private members
	//

	private T	mAdapter;

	private int	mPosition;

	//
	// Constructor
	//

	public AdapterView( Context context ) {

		super( context );
	}

	//
	// Public methods
	//

	public T getAdapter() {

		return mAdapter;
	}

	public void setAdapter( T adapter ) {

		mAdapter = adapter;
	}

	public void setSelection( int position ) {

		mPosition = position;
	}

	public Object getSelectedItem() {

		return ( mAdapter ).getItem( mPosition );
	}

	//
	// Inner class
	//

	public static interface OnItemClickListener {

		//
		// Methods
		//

		void onItemClick( AdapterView<?> parent, View view, int position, long id );
	}
}
