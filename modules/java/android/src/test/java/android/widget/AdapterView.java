// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

package android.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Dummy implementation for unit testing.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
