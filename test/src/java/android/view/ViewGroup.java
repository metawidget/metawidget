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

package android.view;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Dummy implementation for unit testing.
 *
 * @author Richard Kennard
 */

public class ViewGroup
	extends View
{
	//
	// Constructor
	//

	public ViewGroup( Context context )
	{
		super( context );
	}

	public ViewGroup( Context context, AttributeSet attributeSet )
	{
		super( context, attributeSet );
	}

	public ViewGroup( Context context, AttributeSet attributeSet, int index )
	{
		super( context, attributeSet, index );
	}

	//
	// Public methods
	//

	public void addView( View child )
	{
		// Do nothing
	}

	public void addView( View child, ViewGroup.LayoutParams params )
	{
		// Do nothing
	}

	public void removeView( View view )
	{
		// Do nothing
	}

	public void removeAllViews()
	{
		// Do nothing
	}

	public View getChildAt( int childLoop )
	{
		return null;
	}

	public int getChildCount()
	{
		return 0;
	}

	//
	// Inner class
	//

	public static class LayoutParams
	{
		//
		// Public statics
		//

		public static final String	FILL_PARENT		= null;

		public static final String	WRAP_CONTENT	= null;

		//
		// Constructor
		//

		public LayoutParams()
		{
			// Ignore
		}

		public LayoutParams( Context context, AttributeSet attributeSet )
		{
			// Ignore
		}

		public LayoutParams( String string1, String string2 )
		{
			// Ignore
		}

		public LayoutParams( String string1, int int1 )
		{
			// Ignore
		}
	}
}
