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

package org.metawidget.android;

import org.metawidget.android.widget.AndroidMetawidget;

import android.R;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.view.View;
import android.widget.TextView;

/**
 * Utilities for working with Android.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>, Tomasz Bartczak
 */

public final class AndroidUtils {

	//
	// Public statics
	//

	/**
	 * Applies the given style id (defined in <code>res/values/styles.xml</code>) to the given View.
	 */

	// It isn't clear in SDK 1.0 R2 how one is meant to generically and programmatically apply
	// styles. It seems like the 'new View( Context, AttributeSet, styleId )' constructor would do
	// the trick, but how to create the AttributeSet for the styleId? This comment...
	//
	// http://markmail.org/message/ogskv4frewsxghlp
	//
	// ...seems to suggest only the container can do this at present, but that this will be enhanced
	// in future?
	//
	public static void applyStyle( View view, int style, AndroidMetawidget metawidget ) {

		// No style?

		if ( style == 0 ) {
			return;
		}

		// Construct StyledAttributes

		TypedArray attributes = metawidget.getContext().obtainStyledAttributes( style, VIEW_ATTRIBUTES );

		// Padding

		int padding = attributes.getDimensionPixelSize( 0, BOGUS_DEFAULT );
		int leftPadding;
		int topPadding;
		int rightPadding;
		int bottomPadding;

		if ( padding == BOGUS_DEFAULT ) {
			leftPadding = attributes.getDimensionPixelSize( 1, 0 );
			topPadding = attributes.getDimensionPixelSize( 2, 0 );
			rightPadding = attributes.getDimensionPixelSize( 3, 0 );
			bottomPadding = attributes.getDimensionPixelSize( 4, 0 );
		} else {
			leftPadding = padding;
			topPadding = padding;
			rightPadding = padding;
			bottomPadding = padding;
		}

		view.setPadding( leftPadding, topPadding, rightPadding, bottomPadding );

		if ( view instanceof TextView ) {
			attributes = metawidget.getContext().obtainStyledAttributes( style, TEXTVIEW_ATTRIBUTES );
			TextView textView = (TextView) view;

			// Color

			ColorStateList colors = attributes.getColorStateList( 0 );

			if ( colors != null ) {
				textView.setTextColor( colors );
			}

			// Gravity

			int gravity = attributes.getInteger( 1, BOGUS_DEFAULT );

			if ( gravity != BOGUS_DEFAULT ) {
				textView.setGravity( gravity );
			}

			// Size
			//
			// (Tomasz discovered you have to re-call obtainStyledAttributes here. Why?)

			attributes = metawidget.getContext().obtainStyledAttributes( style, TEXTVIEW_ATTRIBUTES_SIZE );
			int textSize = attributes.getDimensionPixelSize( 0, BOGUS_DEFAULT );

			if ( textSize != BOGUS_DEFAULT ) {
				textView.setTextSize( textSize );
			}
		}
	}

	//
	// Private statics
	//

	/**
	 * Array of View style attributes.
	 */

	private static final int[]	VIEW_ATTRIBUTES				= new int[] { R.attr.padding, R.attr.paddingLeft, R.attr.paddingTop, R.attr.paddingRight, R.attr.paddingBottom };

	/**
	 * Array of TextView style attributes.
	 */

	private static final int[]	TEXTVIEW_ATTRIBUTES			= new int[] { R.attr.textColor, R.attr.gravity };

	/**
	 * For some reason you must call textSize separately to textColor, as discovered by kretes:
	 * https://sourceforge.net/projects/metawidget/forums/forum/747623/topic/3448466
	 */

	private static final int[]	TEXTVIEW_ATTRIBUTES_SIZE	= new int[] { R.attr.textSize };

	private static final int	BOGUS_DEFAULT				= -1;

	//
	// Private constructor
	//

	private AndroidUtils() {

		// Can never be called
	}
}
