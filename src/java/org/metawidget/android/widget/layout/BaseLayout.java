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

	private final static int	VIEW_PADDING		= 0x0000000d;

	private final static int	VIEW_PADDING_BOTTOM	= 0x00000011;

	private final static int	VIEW_PADDING_LEFT	= 0x0000000e;

	private final static int	VIEW_PADDING_RIGHT	= 0x00000010;

	private final static int	VIEW_PADDING_TOP	= 0x0000000f;

	private final static int[]	VIEW				= new int[] { VIEW_PADDING, VIEW_PADDING_BOTTOM, VIEW_PADDING_LEFT, VIEW_PADDING_RIGHT, VIEW_PADDING_TOP };

	private final static int	TEXTVIEW_COLOR		= 0x00000005;

	private final static int	TEXTVIEW_SIZE		= 0x00000002;

	private final static int[]	TEXTVIEW			= new int[] { TEXTVIEW_COLOR, TEXTVIEW_SIZE };

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

	// It isn't clear in 1.0_r1 how one is meant to generically and programmatically apply styles
	// from their ids. In particular:
	//
	// 1. AssetManager.applyStyle is package-protected; and
	// 2. Theme.obtainStyledAttributes( AttributeSet, int, int, int ) has an explicit cast to
	// XmlBlock$Parser (not just AttributeSet)
	//
	// So, this method is hard-coded for now. It is enough to convey the general idea though (eg.
	// being able to set paramLabelStyle and paramSectionStyle externally).
	//
	protected void applyStyle( View view, int style )
	{
		// No style?

		if ( style == 0 )
			return;

		// Construct StyledAttributes

		TypedArray attributes = getMetawidget().getContext().obtainStyledAttributes( style, VIEW );

		// Padding

		int padding = attributes.getDimensionPixelSize( VIEW_PADDING, -1 );
		int leftPadding;
		int topPadding;
		int rightPadding;
		int bottomPadding;

		if ( padding >= 0 )
		{
			leftPadding = padding;
			topPadding = padding;
			rightPadding = padding;
			bottomPadding = padding;
		}
		else
		{
			leftPadding = attributes.getDimensionPixelSize( VIEW_PADDING_LEFT, 0 );
			topPadding = attributes.getDimensionPixelSize( VIEW_PADDING_TOP, 0 );
			rightPadding = attributes.getDimensionPixelSize( VIEW_PADDING_RIGHT, 0 );
			bottomPadding = attributes.getDimensionPixelSize( VIEW_PADDING_BOTTOM, 0 );
		}

		view.setPadding( leftPadding, topPadding, rightPadding, bottomPadding );

		if ( view instanceof TextView )
		{
			attributes = getMetawidget().getContext().obtainStyledAttributes( style, TEXTVIEW );
			TextView textView = (TextView) view;

			// Color

			ColorStateList colors = attributes.getColorStateList( TEXTVIEW_COLOR );

			if ( colors != null )
				textView.setTextColor( colors );

			// Size

			textView.setTextSize( attributes.getDimensionPixelSize( TEXTVIEW_SIZE, 15 ) );
		}
	}
}
