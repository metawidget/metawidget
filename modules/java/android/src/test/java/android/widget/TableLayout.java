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

/**
 * Dummy implementation for unit testing.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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

	/**
	 * @param column
	 * @param stretchable
	 */

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
