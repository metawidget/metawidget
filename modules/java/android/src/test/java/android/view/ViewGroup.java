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

package android.view;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;

/**
 * Dummy implementation for unit testing.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class ViewGroup
	extends View
	implements ViewParent {

	//
	// Constructor
	//

	public ViewGroup( Context context ) {

		super( context );
	}

	public ViewGroup( Context context, AttributeSet attributeSet ) {

		super( context, attributeSet );
	}

	public ViewGroup( Context context, AttributeSet attributeSet, int index ) {

		super( context, attributeSet, index );
	}

	//
	// Public methods
	//

	public void addView( View child ) {

		addView( child, null );
	}

	/**
	 * @param params
	 */

	public void addView( View child, LayoutParams params ) {

		child.setParent( this );
		mChildren.add( child );
	}

	public void removeView( View view ) {

		mChildren.remove( view );
	}

	public void removeAllViews() {

		mChildren.clear();
	}

	public View getChildAt( int childIndex ) {

		return mChildren.get( childIndex );
	}

	public int getChildCount() {

		return mChildren.size();
	}

	//
	// Inner class
	//

	public static class LayoutParams {

		//
		// Public statics
		//

		public static final int	FILL_PARENT		= 0;

		public static final int	WRAP_CONTENT	= 0;

		//
		// Constructor
		//

		public LayoutParams() {

			// Ignore
		}

		/**
		 * @param context
		 * @param attributeSet
		 */

		public LayoutParams( Context context, AttributeSet attributeSet ) {

			// Ignore
		}

		/**
		 * @param int1
		 * @param int2
		 */

		public LayoutParams( int int1, int int2 ) {

			// Ignore
		}

		/**
		 * @param toCopy
		 */

		public LayoutParams( LayoutParams toCopy ) {

			// Ignore
		}
	}

	public static class MarginLayoutParams
		extends LayoutParams {

		//
		// Constructor
		//

		public MarginLayoutParams() {

			// Default constructor
		}

		public MarginLayoutParams( int int1, int int2 ) {

			super( int1, int2 );
		}

		public MarginLayoutParams( MarginLayoutParams toCopy ) {

			super( toCopy );
		}
	}

	public void bringChildToFront( View arg0 ) {

		// Do nothing
	}

	public void childDrawableStateChanged( View arg0 ) {

		// Do nothing
	}

	public void clearChildFocus( View arg0 ) {

		// Do nothing
	}

	public void createContextMenu( ContextMenu arg0 ) {

		// Do nothing
	}

	public View focusSearch( View arg0, int arg1 ) {

		return null;
	}

	public void focusableViewAvailable( View arg0 ) {

		// Do nothing
	}

	public boolean getChildVisibleRect( View arg0, Rect arg1, Point arg2 ) {

		return false;
	}

	public void invalidateChild( View arg0, Rect arg1 ) {

		// Do nothing
	}

	public ViewParent invalidateChildInParent( int[] arg0, Rect arg1 ) {

		return null;
	}

	public boolean isLayoutRequested() {

		return false;
	}

	public void recomputeViewAttributes( View arg0 ) {

		// Do nothing
	}

	public void requestChildFocus( View arg0, View arg1 ) {

		// Do nothing
	}

	public void requestDisallowInterceptTouchEvent( boolean arg0 ) {

		// Do nothing
	}

	public void requestLayout() {

		// Do nothing
	}

	public void requestTransparentRegion( View arg0 ) {

		// Do nothing
	}

	public boolean requestChildRectangleOnScreen( View arg0, Rect arg1, boolean arg2 ) {

		return false;
	}

	public boolean showContextMenuForChild( View arg0 ) {

		return false;
	}
}
