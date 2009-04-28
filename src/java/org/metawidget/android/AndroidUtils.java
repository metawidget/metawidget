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

package org.metawidget.android;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.metawidget.MetawidgetException;
import org.metawidget.util.simple.StringUtils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Utilities for working with Android.
 *
 * @author Richard Kennard
 */

public final class AndroidUtils
{
	//
	// Public statics
	//

	/**
	 * An ArrayAdapter that creates its own TextView to render items, rather than relying on an
	 * external, application-supplied Resource.
	 */

	public static class ResourcelessArrayAdapter<T>
		extends ArrayAdapter<T>
	{
		//
		// Private statics
		//

		private final static int	NO_RESOURCE	= 0;

		//
		// Private members
		//

		private List<String>		mLabels;

		//
		// Constructor
		//

		public ResourcelessArrayAdapter( Context context, Collection<T> values )
		{
			this( context, new ArrayList<T>( values ), null );
		}

		/**
		 * @param labels
		 *            List of human-readable labels. May be null.
		 */

		public ResourcelessArrayAdapter( Context context, List<T> values, List<String> labels )
		{
			super( context, NO_RESOURCE, values );

			if ( labels != null && !labels.isEmpty() )
			{
				if ( labels.size() != values.size() )
					throw MetawidgetException.newException( "Labels list must be same size as values list" );

				mLabels = labels;
			}
		}

		//
		// Public methods
		//

		@Override
		public View getView( int position, View convertView, ViewGroup parentView )
		{
			TextView viewToReturn = (TextView) convertView;

			// (reuse the given convertView if possible)

			if ( viewToReturn == null )
				viewToReturn = new TextView( getContext() );

			if ( mLabels != null )
				viewToReturn.setText( StringUtils.quietValueOf( mLabels.get( position ) ) );
			else
				viewToReturn.setText( StringUtils.quietValueOf( getItem( position ) ) );

			return viewToReturn;
		}

		@Override
		public View getDropDownView( int position, View convertView, ViewGroup parentView )
		{
			return getView( position, convertView, parentView );
		}
	}

	//
	// Private constructor
	//

	private AndroidUtils()
	{
		// Can never be called
	}
}
