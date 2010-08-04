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
import android.content.res.ColorStateList;
import android.text.InputFilter;
import android.text.method.KeyListener;
import android.text.method.TransformationMethod;
import android.view.View;

/**
 * Dummy implementation for unit testing.
 *
 * @author Richard Kennard
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
