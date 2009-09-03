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
import org.metawidget.layout.iface.LayoutException;
import org.metawidget.swing.layout.GroupLayout;

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

	private final static int			LABEL_AND_WIDGET	= 2;

	//
	// Private members
	//

	private android.widget.TableLayout	mLayout;

	//
	// Constructor
	//

	public void layoutBegin( AndroidMetawidget metawidget )
	{
		metawidget.putClientProperty( GroupLayout.class, null );
		State state = getState( metawidget );

		// Number of columns

		Object numberOfColumns = metawidget.getParameter( "numberOfColumns" );

		if ( numberOfColumns != null )
		{
			if ( numberOfColumns instanceof String )
				state.mNumberOfColumns = Integer.parseInt( (String) numberOfColumns );
			else
				state.mNumberOfColumns = (Integer) numberOfColumns;

			if ( state.mNumberOfColumns < 1 )
				throw LayoutException.newException( "numberOfColumns must be >= 1. Use LinearLayout for zero columns" );
		}

		// Label style

		Object labelStyle = metawidget.getParameter( "labelStyle" );

		if ( labelStyle != null )
			state.mLabelStyle = (Integer) labelStyle;

		// Section style

		Object sectionStyle = metawidget.getParameter( "sectionStyle" );

		if ( sectionStyle != null )
			state.mSectionStyle = (Integer) sectionStyle;
	}

	public void layoutChild( View view, Map<String, String> attributes, AndroidMetawidget metawidget )
	{
		// Ignore empty Stubs

		if ( view instanceof Stub && ( (Stub) view ).getChildCount() == 0 )
			return;

		initLayout( metawidget );

		TableRow tableRow = new TableRow( metawidget.getContext() );

		String label = null;

		// Section headings

		if ( attributes != null )
		{
			State state = getState( metawidget );
			String section = attributes.get( SECTION );

			if ( section != null && !section.equals( state.mCurrentSection ) )
			{
				state.mCurrentSection = section;
				layoutSection( section, metawidget );
			}

			// Labels

			label = metawidget.getLabelString( attributes );

			if ( label != null )
			{
				TextView textView = new TextView( metawidget.getContext() );

				if ( !"".equals( label ) )
					textView.setText( label + ": " );

				AndroidUtils.applyStyle( textView, state.mLabelStyle, metawidget );

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

		mLayout.addView( tableRow, new android.widget.TableLayout.LayoutParams() );
	}

	public void layoutEnd( AndroidMetawidget metawidget )
	{
		View viewButtons = metawidget.getFacet( "buttons" );

		if ( viewButtons != null )
		{
			initLayout( metawidget );
			mLayout.addView( viewButtons, new android.widget.TableLayout.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT ) );
		}

		// If the Layout was never used, just put an empty space

		if ( mLayout == null )
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

		State state = getState( metawidget );
		AndroidUtils.applyStyle( textView, state.mSectionStyle, metawidget );

		// Add it as a separate layout, to save horizontal space where possible
		// (eg. the labels in this new section might be smaller than the old section)

		mLayout.addView( textView, new android.widget.TableLayout.LayoutParams() );

		// Start a new layout

		mLayout = new android.widget.TableLayout( metawidget.getContext() );
		mLayout.setOrientation( LinearLayout.VERTICAL );
		mLayout.setColumnStretchable( 1, true );

		metawidget.addView( mLayout, new android.widget.LinearLayout.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT ) );
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

	private void initLayout( AndroidMetawidget metawidget )
	{
		if ( mLayout != null )
			return;

		mLayout = new android.widget.TableLayout( metawidget.getContext() );
		mLayout.setOrientation( LinearLayout.VERTICAL );
		mLayout.setColumnStretchable( 1, true );

		metawidget.addView( mLayout, new android.widget.LinearLayout.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT ) );
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
		/* package private */String	mCurrentSection;

		/* package private */int	mNumberOfColumns	= 1;

		/* package private */int	mLabelStyle;

		/* package private */int	mSectionStyle;
	}
}
