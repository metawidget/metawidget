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
import android.content.res.ColorStateList;
import android.text.InputFilter;
import android.text.method.KeyListener;
import android.text.method.TransformationMethod;
import android.view.View;

/**
 * Dummy implementation for unit testing.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class TextView
	extends View {

	//
	// Private members
	//

	private CharSequence			mText;

	private int						mMinLines;

	private InputFilter[]			mInputFilters;

	private KeyListener				mKeyListener;

	private TransformationMethod	mTransformationMethod;

	//
	// Constructor
	//

	public TextView( Context context ) {

		super( context );
	}

	//
	// Public methods
	//

	public CharSequence getText() {

		return mText;
	}

	public void setText( CharSequence text ) {

		mText = text;
	}

	/**
	 * @param text
	 * @param bufferType
	 */

	public void setText( CharSequence text, BufferType bufferType ) {

		mText = text;
	}

	/**
	 * @param textSize
	 */

	public void setTextSize( int textSize ) {

		// Ignore
	}

	/**
	 * @param gravity
	 */

	public void setGravity( int gravity ) {

		// Ignore
	}

	/**
	 * @param colors
	 */

	public void setTextColor( ColorStateList colors ) {

		// Ignore
	}

	public void setTransformationMethod( TransformationMethod transformationMethod ) {

		mTransformationMethod = transformationMethod;
	}

	public TransformationMethod getTransformationMethod() {

		return mTransformationMethod;
	}

	public void setMinLines( int minLines ) {

		mMinLines = minLines;
	}

	public void setFilters( InputFilter[] inputFilters ) {

		mInputFilters = inputFilters;
	}

	public InputFilter[] getFilters() {

		return mInputFilters;
	}

	public void setKeyListener( KeyListener keyListener ) {

		mKeyListener = keyListener;
	}

	public KeyListener getKeyListener() {

		return mKeyListener;
	}

	public int getMinLines() {

		return mMinLines;
	}

	//
	// Inner class
	//

	public static enum BufferType {
		SPANNABLE
	}
}
