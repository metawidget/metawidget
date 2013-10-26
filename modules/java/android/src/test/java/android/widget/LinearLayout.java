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

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Dummy implementation for unit testing.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class LinearLayout
	extends ViewGroup {

	//
	// Public statics
	//

	public static final int	HORIZONTAL	= 0;

	public static final int	VERTICAL	= 1;

	//
	// Private members
	//

	private int				mOrientation;

	//
	// Constructor
	//

	public LinearLayout( Context context ) {

		super( context );
	}

	public LinearLayout( Context context, AttributeSet attributeSet ) {

		super( context, attributeSet );
	}

	public LinearLayout( Context context, AttributeSet attributeSet, int index ) {

		super( context, attributeSet, index );
	}

	//
	// Public methods
	//

	public void setOrientation( int orientation ) {

		mOrientation = orientation;
	}

	public int getOrientation() {

		return mOrientation;
	}

	//
	// Inner class
	//

	public static class LayoutParams
		extends MarginLayoutParams {

		//
		// Constructor
		//

		public LayoutParams() {

			// Default constructor
		}

		public LayoutParams( int int1, int int2 ) {

			super( int1, int2 );
		}

		public LayoutParams( MarginLayoutParams toCopy ) {

			super( toCopy );
		}
	}
}
