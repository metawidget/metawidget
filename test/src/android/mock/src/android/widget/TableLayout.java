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

import android.content.Context;
import android.util.AttributeSet;

/**
 * Dummy implementation for unit testing.
 *
 * @author Richard Kennard
 */

public class TableLayout
	extends LinearLayout {

	//
	// Constructor
	//

	public TableLayout( Context context ) {

		super( context );
	}

	public TableLayout( Context context, AttributeSet attributeSet ) {

		super( context, attributeSet );
	}

	public TableLayout( Context context, AttributeSet attributeSet, int index ) {

		super( context, attributeSet, index );
	}

	//
	// Public methods
	//

	public void setColumnStretchable( int column, boolean stretchable ) {

		// Do nothing
	}

	//
	// Inner class
	//

	public static class LayoutParams
		extends android.widget.LinearLayout.LayoutParams {
		// Do nothing
	}
}
