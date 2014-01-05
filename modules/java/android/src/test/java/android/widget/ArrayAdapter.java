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

import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;

/**
 * Dummy implementation for unit testing.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class ArrayAdapter<T>
	implements SpinnerAdapter {

	//
	// Private members
	//

	private List<T>	mValues;

	//
	// Constructors
	//

	/**
	 * @param context
	 */

	public ArrayAdapter( Context context ) {

		// Ignore context
	}

	/**
	 * @param context
	 * @param textViewResourceId
	 * @param values
	 */

	public ArrayAdapter( Context context, int textViewResourceId, List<T> values ) {

		mValues = values;
	}

	//
	// Supported public methods
	//

	public View getView( int position, View convertView, ViewGroup parentView ) {

		return null;
	}

	/**
	 * @param position
	 * @param convertView
	 * @param parentView
	 */

	public View getDropDownView( int position, View convertView, ViewGroup parentView ) {

		return null;
	}

	public T getItem( int position ) {

		return mValues.get( position );
	}

	public int getPosition( Object value ) {

		return mValues.indexOf( value );
	}

	public int getCount() {

		return mValues.size();
	}

	public Context getContext() {

		return null;
	}

	/**
	 * @param resource
	 */

	public void setDropDownViewResource( int resource ) {

		// Do nothing
	}

	//
	// Unsupported public methods
	//

	public long getItemId( int arg0 ) {

		throw new UnsupportedOperationException();
	}

	public int getItemViewType( int arg0 ) {

		throw new UnsupportedOperationException();
	}

	public int getViewTypeCount() {

		throw new UnsupportedOperationException();
	}

	public boolean hasStableIds() {

		throw new UnsupportedOperationException();
	}

	public boolean isEmpty() {

		throw new UnsupportedOperationException();
	}

	public void registerDataSetObserver( DataSetObserver arg0 ) {

		throw new UnsupportedOperationException();
	}

	public void unregisterDataSetObserver( DataSetObserver arg0 ) {

		throw new UnsupportedOperationException();
	}
}
