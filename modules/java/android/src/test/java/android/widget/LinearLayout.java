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
