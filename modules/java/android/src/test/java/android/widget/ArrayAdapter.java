// Metawidget
//
// This library is dual licensed under both LGPL and a commercial
// license.
//
// LGPL: this library is free software; you can redistribute it and/or
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
//
// Commercial License: See http://metawidget.org for details

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
