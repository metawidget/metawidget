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

package org.metawidget.android.widget.layout;

import java.util.Map;

import org.metawidget.android.widget.AndroidMetawidget;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.view.View;
import android.widget.TextView;

/**
 * Convenience implementation.
 *
 * @author Richard Kennard
 */

public abstract class BaseLayout
	implements Layout
{
	//
	// Private statics
	//

	private final static int	VIEW_PADDING		= 0x010100d5;

	private final static int	VIEW_PADDING_BOTTOM	= 0x010100d9;

	private final static int	VIEW_PADDING_LEFT	= 0x010100d6;

	private final static int	VIEW_PADDING_RIGHT	= 0x010100d8;

	private final static int	VIEW_PADDING_TOP	= 0x010100d7;

	/**
	 * Array of View style attributes.
	 * <p>
	 * These values are from, say,
	 * http://code.google.com/android/reference/android/R.attr.html#textSize. Note: as of SDK 1.0
	 * R2, this technique is frowned upon. Google have deprecated
	 * <code>andorid.R.styleable.View</code> because these numbers will change in the future.
	 * However, they haven't provided a good alternative (see below).
	 */

	private final static int[]	VIEW				= new int[] { VIEW_PADDING, VIEW_PADDING_LEFT, VIEW_PADDING_TOP, VIEW_PADDING_RIGHT, VIEW_PADDING_BOTTOM };

	private final static int	TEXTVIEW_COLOR		= 0x01010098;

	private final static int	TEXTVIEW_SIZE		= 0x01010095;

	/**
	 * Array of TextView style attributes.
	 * <p>
	 * These values are from, say,
	 * http://code.google.com/android/reference/android/R.attr.html#textSize. Note: as of SDK 1.0
	 * R2, this technique is frowned upon. Google have deprecated
	 * <code>andorid.R.styleable.View</code> because these numbers will change in the future.
	 * However, they haven't provided a good alternative (see below).
	 */

	private final static int[]	TEXTVIEW			= new int[] { TEXTVIEW_COLOR, TEXTVIEW_SIZE };

	private final static int	BOGUS_DEFAULT		= -1;

	//
	// Private members
	//

	private AndroidMetawidget	mMetawidget;

	//
	// Constructor
	//

	public BaseLayout( AndroidMetawidget metawidget )
	{
		mMetawidget = metawidget;
	}

	//
	// Public methods
	//

	public void layoutBegin()
	{
		// Do nothing by default
	}

	public void layoutChild( View view, Map<String, String> attributes )
	{
		// Do nothing by default
	}

	public void layoutEnd()
	{
		// Do nothing by default
	}

	//
	// Protected methods
	//

	protected AndroidMetawidget getMetawidget()
	{
		return mMetawidget;
	}

	/**
	 * Applies the given style id (defined in <code>res/values/styles.xml</code>) to the given
	 * View.
	 */

	// It isn't clear in SDK 1.0 R2 how one is meant to generically and programmatically apply
	// styles. It seems like the 'new View( Context, AttributeSet, styleId )' constructor would do the
	// trick, but how to create the AttributeSet for the styleId? This comment...
	//
	// http://markmail.org/message/ogskv4frewsxghlp
	//
	// ...seems to suggest only the container can do this at present, but that this will be cleaned
	// up in future
	//
	protected void applyStyle( View view, int style )
	{
		// No style?

		if ( style == 0 )
			return;

		// Construct StyledAttributes

		TypedArray attributes = getMetawidget().getContext().obtainStyledAttributes( style, VIEW );

		// Padding

		int padding = attributes.getDimensionPixelSize( 0, BOGUS_DEFAULT );
		int leftPadding;
		int topPadding;
		int rightPadding;
		int bottomPadding;

		if ( padding == BOGUS_DEFAULT )
		{
			leftPadding = attributes.getDimensionPixelSize( 1, 0 );
			topPadding = attributes.getDimensionPixelSize( 2, 0 );
			rightPadding = attributes.getDimensionPixelSize( 3, 0 );
			bottomPadding = attributes.getDimensionPixelSize( 4, 0 );
		}
		else
		{
			leftPadding = padding;
			topPadding = padding;
			rightPadding = padding;
			bottomPadding = padding;
		}

		view.setPadding( leftPadding, topPadding, rightPadding, bottomPadding );

		if ( view instanceof TextView )
		{
			attributes = getMetawidget().getContext().obtainStyledAttributes( style, TEXTVIEW );
			TextView textView = (TextView) view;

			System.out.println( "View paddingTop is " + topPadding );
			System.out.println( "View paddingBottom is " + bottomPadding );
			System.out.println( "TextView colors are " + attributes.getColorStateList( 0 ) );
			System.out.println( "TextView size is " + attributes.getDimension( 1, -1 ) );

			// Color

			ColorStateList colors = attributes.getColorStateList( 0 );

			if ( colors != null )
				textView.setTextColor( colors );

			// Size

			int textSize = attributes.getDimensionPixelSize( 1, BOGUS_DEFAULT );

			if ( textSize != BOGUS_DEFAULT )
				textView.setTextSize( textSize );
		}
	}
}
