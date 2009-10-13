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

package android.widget;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Dummy implementation for unit testing.
 *
 * @author Richard Kennard
 */

public class ArrayAdapter<T>
{
	//
	// Private members
	//

	private List<T>	mValues;

	//
	// Constructors
	//

	public ArrayAdapter( Context context )
	{
		// Ignore context
	}

	public ArrayAdapter( Context context, int noResource, List<T> values )
	{
		mValues = values;
	}

	//
	// Public methods
	//

	public View getView( int position, View convertView, ViewGroup parentView )
	{
		return null;
	}

	public View getDropDownView( int position, View convertView, ViewGroup parentView )
	{
		return null;
	}

	public T getItem( int position )
	{
		return mValues.get( position );
	}

	public int getPosition( T value )
	{
		return mValues.indexOf( value );
	}

	public Context getContext()
	{
		return null;
	}
}
