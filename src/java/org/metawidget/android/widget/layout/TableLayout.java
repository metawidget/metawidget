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

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.android.AndroidUtils;
import org.metawidget.android.widget.AndroidMetawidget;
import org.metawidget.android.widget.Stub;
import org.metawidget.layout.iface.Layout;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Layout to arrange widgets in a table, with one column for labels and another for the widget.
 *
 * @author Richard Kennard
 */

public class TableLayout
	implements Layout<View, AndroidMetawidget>
{
	//
	// Private statics
	//

	private final static int	LABEL_AND_WIDGET	= 2;

	//
	// Private members
	//

	private int					mLabelStyle;

	private int					mSectionStyle;

	//
	// Constructor
	//

	public TableLayout()
	{
		this( new TableLayoutConfig() );
	}

	public TableLayout( TableLayoutConfig config )
	{
		mLabelStyle = config.getLabelStyle();
		mSectionStyle = config.getSectionStyle();
	}

	//
	// Public methods
	//

	public void onStartBuild( AndroidMetawidget metawidget )
	{
		metawidget.putClientProperty( TableLayout.class, null );
	}

	public void layoutChild( View view, String elementName, Map<String, String> attributes, AndroidMetawidget metawidget )
	{
		// Ignore empty Stubs

		if ( view instanceof Stub && ( (Stub) view ).getChildCount() == 0 )
			return;

		TableRow tableRow = new TableRow( metawidget.getContext() );

		String label = null;

		// Section headings

		if ( attributes != null )
		{
			State state = getState( metawidget );
			String section = attributes.get( SECTION );

			if ( section != null && !section.equals( state.currentSection ) )
			{
				state.currentSection = section;
				layoutSection( section, metawidget );
			}

			// Labels

			label = metawidget.getLabelString( attributes );

			if ( label != null )
			{
				TextView textView = new TextView( metawidget.getContext() );

				if ( !"".equals( label ) )
					textView.setText( label + ": " );

				AndroidUtils.applyStyle( textView, mLabelStyle, metawidget );

				tableRow.addView( textView );
			}
		}

		// View

		View viewToAdd = view;

		// Hack for sizing ListViews

		if ( view instanceof ListView )
		{
			FrameLayout frameLayout = new FrameLayout( metawidget.getContext() );

			if ( view.getLayoutParams() == null )
				view.setLayoutParams( new FrameLayout.LayoutParams( 325, 100 ) );

			frameLayout.addView( view );

			viewToAdd = frameLayout;
		}

		// (always override LayoutParams, just in case)

		TableRow.LayoutParams params = new TableRow.LayoutParams();

		if ( label == null )
			params.span = LABEL_AND_WIDGET;

		tableRow.addView( viewToAdd, params );

		// Add it to our layout

		getLayout( metawidget ).addView( tableRow, new android.widget.TableLayout.LayoutParams() );
	}

	public void onEndBuild( AndroidMetawidget metawidget )
	{
		View viewButtons = metawidget.getFacet( "buttons" );

		if ( viewButtons != null )
		{
			getLayout( metawidget ).addView( viewButtons, new android.widget.TableLayout.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT ) );
		}

		// If the Layout was never used, just put an empty space

		if ( metawidget.getChildCount() == 0 )
			metawidget.addView( new TextView( metawidget.getContext() ), new android.widget.LinearLayout.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT ) );
	}

	//
	// Protected methods
	//

	protected void layoutSection( String section, AndroidMetawidget metawidget )
	{
		if ( "".equals( section ) )
			return;

		// Section name (possibly localized)

		TextView textView = new TextView( metawidget.getContext() );

		String localizedSection = metawidget.getLocalizedKey( section );

		if ( localizedSection != null )
			textView.setText( localizedSection );
		else
			textView.setText( section );

		// Apply style (if any)

		AndroidUtils.applyStyle( textView, mSectionStyle, metawidget );

		// Add it as a separate layout, to save horizontal space where possible
		// (eg. the labels in this new section might be smaller than the old section)

		getLayout( metawidget ).addView( textView, new android.widget.TableLayout.LayoutParams() );

		// Start a new layout

		startNewLayout( metawidget );
	}

	//
	// Private methods
	//

	/**
	 * Initialize the TableLayout.
	 * <p>
	 * We don't initialize the TableLayout unless we find we have something to put into it, because
	 * Android doesn't like empty TableLayouts.
	 */

	private android.widget.TableLayout getLayout( AndroidMetawidget metawidget )
	{
		if ( metawidget.getChildCount() == 0 )
			startNewLayout( metawidget );

		return (android.widget.TableLayout) metawidget.getChildAt( metawidget.getChildCount() - 1 );
	}

	private void startNewLayout( AndroidMetawidget metawidget )
	{
		android.widget.TableLayout layout = new android.widget.TableLayout( metawidget.getContext() );
		layout.setOrientation( LinearLayout.VERTICAL );
		layout.setColumnStretchable( 1, true );

		metawidget.addView( layout, new android.widget.LinearLayout.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT ) );
	}

	private State getState( AndroidMetawidget metawidget )
	{
		State state = (State) metawidget.getClientProperty( TableLayout.class );

		if ( state == null )
		{
			state = new State();
			metawidget.putClientProperty( TableLayout.class, state );
		}

		return state;
	}

	//
	// Inner class
	//

	/**
	 * Simple, lightweight structure for saving state.
	 */

	/* package private */class State
	{
		/* package private */String	currentSection;
	}
}
