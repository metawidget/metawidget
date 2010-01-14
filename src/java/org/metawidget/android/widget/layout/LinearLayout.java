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

import org.metawidget.android.AndroidUtils;
import org.metawidget.android.widget.AndroidMetawidget;
import org.metawidget.android.widget.Stub;
import org.metawidget.layout.iface.AdvancedLayout;
import org.metawidget.util.simple.SimpleLayoutUtils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Layout to arrange widgets vertically, with one row for the label and the next for the widget.
 *
 * @author Richard Kennard
 */

public class LinearLayout
	implements AdvancedLayout<View, ViewGroup, AndroidMetawidget>
{
	//
	// Private members
	//

	private int					mLabelStyle;

	//
	// Constructor
	//

	public LinearLayout()
	{
		this( new LinearLayoutConfig() );
	}

	public LinearLayout( LinearLayoutConfig config )
	{
		mLabelStyle = config.getLabelStyle();
	}

	//
	// Public methods
	//

	@Override
	public void startLayout( ViewGroup container, AndroidMetawidget metawidget )
	{
		// Do nothing
	}

	public void layoutWidget( View view, String elementName, Map<String, String> attributes, ViewGroup container, AndroidMetawidget metawidget )
	{
		// Ignore empty Stubs

		if ( view instanceof Stub && ( (Stub) view ).getChildCount() == 0 )
			return;

		ViewGroup viewToAddTo = newViewToAddTo( container );

		String labelText = metawidget.getLabelString( attributes );
		boolean needsLabel = SimpleLayoutUtils.needsLabel( labelText, elementName );

		// Labels

		if ( needsLabel )
		{
			TextView textView = new TextView( metawidget.getContext() );
			textView.setText( labelText + ": " );

			AndroidUtils.applyStyle( textView, mLabelStyle, metawidget );

			viewToAddTo.addView( textView );
		}

		// View

		layoutWidget( view, viewToAddTo, container, metawidget, needsLabel );
	}

	@Override
	public void endLayout( ViewGroup container, AndroidMetawidget metawidget )
	{
		if ( container.equals( metawidget ))
		{
			View viewButtons = metawidget.getFacet( "buttons" );

			if ( viewButtons != null )
			{
				container.addView( viewButtons, new android.widget.LinearLayout.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT ) );
			}
		}
	}

	//
	// Protected methods
	//

	protected void layoutWidget( View view, ViewGroup viewToAddTo, ViewGroup container, AndroidMetawidget metawidget, boolean needsLabel )
	{
		android.view.ViewGroup.LayoutParams params = view.getLayoutParams();

		if ( params == null )
		{
			// Hack for sizing ListViews

			if ( view instanceof ListView )
				params = new android.widget.LinearLayout.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, 100 );
			else
				params = new android.widget.LinearLayout.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
		}

		getLayout( container ).addView( view, params );
	}

	//
	// Protected methods
	//

	protected ViewGroup newViewToAddTo( ViewGroup metawidget )
	{
		// AndroidMetawidget is already a LinearLayout

		return metawidget;
	}

	protected ViewGroup getLayout( ViewGroup container )
	{
		// AndroidMetawidget is already a LinearLayout

		return container;
	}
}
