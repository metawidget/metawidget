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

import java.util.List;

import org.metawidget.util.CollectionUtils;

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
	// Private members
	//

	private List<View>	mChildren	= CollectionUtils.newArrayList();

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
		mChildren.add( child );
	}

	public void addView( View child, LayoutParams params )
	{
		mChildren.add( child );
	}

	public void removeView( View view )
	{
		mChildren.remove( view );
	}

	public void removeAllViews()
	{
		mChildren.clear();
	}

	public View getChildAt( int childIndex )
	{
		return mChildren.get( childIndex );
	}

	public int getChildCount()
	{
		return mChildren.size();
	}

	//
	// Inner class
	//

	public static class LayoutParams
	{
		//
		// Public statics
		//

		public static final int	FILL_PARENT		= 0;

		public static final int	WRAP_CONTENT	= 0;

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

		public LayoutParams( int int1, int int2 )
		{
			// Ignore
		}

		public LayoutParams( LayoutParams toCopy )
		{
			// Ignore
		}
	}

	public static class MarginLayoutParams
		extends LayoutParams
	{
		//
		// Constructor
		//

		public MarginLayoutParams()
		{
			// Default constructor
		}

		public MarginLayoutParams( int int1, int int2 )
		{
			super( int1, int2 );
		}

		public MarginLayoutParams( MarginLayoutParams toCopy )
		{
			super( toCopy );
		}
	}
}
