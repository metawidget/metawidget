// Metawidget (licensed under LGPL)
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
import android.widget.FrameLayout.LayoutParams;

/**
 * Dummy implementation for unit testing.
 *
 * @author Richard Kennard
 */

public class View {

	//
	// Public statics
	//

	public static final int	VISIBLE		= 0;

	public static final int	INVISIBLE	= 4;

	public static final int	FOCUS_UP	= 33;

	//
	// Protected members
	//

	protected List<View>	mChildren	= CollectionUtils.newArrayList();

	//
	// Private members
	//

	private ViewParent		mParent;

	private Object			mTag;

	private int				mVisibility	= View.VISIBLE;

	//
	// Constructor
	//

	/**
	 * @param context
	 */

	public View( Context context ) {

		// Ignore context
	}

	/**
	 * @param context
	 * @param attributeSet
	 */

	public View( Context context, AttributeSet attributeSet ) {

		// Ignore context
	}

	/**
	 * @param context
	 * @param attributeSet
	 * @param index
	 */

	public View( Context context, AttributeSet attributeSet, int index ) {

		// Ignore context
	}

	//
	// Public methods
	//

	public Context getContext() {

		return null;
	}

	public ViewParent getParent() {

		return mParent;
	}

	public void setParent( ViewParent parent ) {

		mParent = parent;
	}

	public ViewGroup.LayoutParams getLayoutParams() {

		return null;
	}

	/**
	 * @param id
	 */

	public void setId( int id ) {

		// Do nothing
	}

	public Object getTag() {

		return mTag;
	}

	public void setTag( Object tag ) {

		mTag = tag;
	}

	/**
	 * @param layoutParams
	 */

	public void setLayoutParams( LayoutParams layoutParams ) {

		// Do nothing
	}

	public void setVisibility( int visibility ) {

		mVisibility = visibility;
	}

	public int getVisibility() {

		return mVisibility;
	}

	/**
	 * @param tag
	 */

	public final View findViewWithTag( Object tag ) {

		for ( View view : mChildren ) {

			if ( tag.equals( view.getTag() ) ) {
				return view;
			}
		}

		return null;
	}

	/**
	 * @param focusable
	 */

	public void setFocusable( boolean focusable ) {

		// Do nothing
	}

	/**
	 * @param focusable
	 */

	public void setFocusableInTouchMode( boolean focusable ) {

		// Do nothing
	}

	public void requestFocus() {

		// Do nothing
	}

	/**
	 * @param leftPadding
	 * @param topPadding
	 * @param rightPadding
	 * @param bottomPadding
	 */

	public void setPadding( int leftPadding, int topPadding, int rightPadding, int bottomPadding ) {

		// Do nothing
	}

	//
	// Protected methods
	//

	/**
	 * @param widthMeasureSpec
	 * @param heightMeasureSpec
	 */

	protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec ) {

		// Do nothing
	}

	protected void postInvalidate() {

		// Do nothing
	}
}
