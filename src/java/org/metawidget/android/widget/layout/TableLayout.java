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

import org.metawidget.android.widget.AndroidMetawidget;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Layout to arrange widgets in a table, with one column for labels and another for the widget.
 *
 * @author Richard Kennard
 */

public class TableLayout
	extends LinearLayout
{
	//
	// Private statics
	//

	private final static int	LABEL_AND_WIDGET	= 2;

	//
	// Constructor
	//

	public TableLayout()
	{
		this( new LinearLayoutConfig() );
	}

	public TableLayout( LinearLayoutConfig config )
	{
		super( config );
	}

	//
	// Public methods
	//

	@Override
	public void endLayout( View container, AndroidMetawidget metawidget )
	{
		super.endLayout( container, metawidget );

		// If the TableLayout was never used, just put an empty space

		ViewGroup viewGroup = (ViewGroup) container;

		if ( viewGroup.getChildCount() == 0 || !( viewGroup.getChildAt( metawidget.getChildCount() - 1 ) instanceof android.widget.TableLayout ))
			viewGroup.addView( new TextView( metawidget.getContext() ), new android.widget.LinearLayout.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT ) );
	}

	//
	// Protected methods
	//

	@Override
	protected void layoutView( View view, ViewGroup tableRow, View container, AndroidMetawidget metawidget, boolean needsLabel )
	{
		// View

		View viewToAdd = view;

		// Hack for sizing ListViews

		if ( view instanceof ListView )
		{
			FrameLayout frameLayout = new FrameLayout( tableRow.getContext() );

			if ( view.getLayoutParams() == null )
				view.setLayoutParams( new FrameLayout.LayoutParams( 325, 100 ) );

			frameLayout.addView( view );

			viewToAdd = frameLayout;
		}

		// (always override LayoutParams, just in case)

		TableRow.LayoutParams params = new TableRow.LayoutParams();

		if ( !needsLabel )
			params.span = LABEL_AND_WIDGET;

		// Add it to our layout

		tableRow.addView( viewToAdd, params );
		getLayout( (ViewGroup) container ).addView( tableRow, new android.widget.TableLayout.LayoutParams() );
	}

	@Override
	protected ViewGroup getViewToAddTo( ViewGroup container )
	{
		// TODO: could be neater?
		return new TableRow( container.getContext() );
	}

	/**
	 * Initialize the TableLayout.
	 * <p>
	 * We don't initialize the TableLayout unless we find we have something to put into it, because
	 * Android doesn't like empty TableLayouts.
	 */

	@Override
	protected android.widget.TableLayout getLayout( ViewGroup container )
	{
		if ( container.getChildCount() == 0 || !( container.getChildAt( container.getChildCount() - 1 ) instanceof android.widget.TableLayout ))
			startNewLayout( container );

		return (android.widget.TableLayout) container.getChildAt( container.getChildCount() - 1 );
	}

	@Override
	protected void startNewLayout( ViewGroup container )
	{
		android.widget.TableLayout layout = new android.widget.TableLayout( container.getContext() );
		layout.setOrientation( android.widget.LinearLayout.VERTICAL );
		layout.setColumnStretchable( 1, true );

		container.addView( layout, new android.widget.LinearLayout.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT ) );
	}
}
