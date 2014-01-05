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

import java.util.List;

import org.metawidget.util.CollectionUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout.LayoutParams;

/**
 * Dummy implementation for unit testing.
 * 
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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

	private boolean			mEnabled = true;

	private OnClickListener	mOnClickListener;

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

	public void setOnClickListener( OnClickListener onClickListener ) {

		mOnClickListener = onClickListener;
	}

	public OnClickListener getOnClickListener() {

		return mOnClickListener;
	}

	public void setEnabled( boolean enabled ) {
		
		mEnabled = enabled;
	}
	
	public boolean isEnabled() {
		
		return mEnabled;
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

	//
	// Inner class
	//

	public interface OnClickListener {

		void onClick( View view );
	}
}
