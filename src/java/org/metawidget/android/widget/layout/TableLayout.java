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

import org.metawidget.MetawidgetException;
import org.metawidget.android.widget.AndroidMetawidget;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Layout to arrange widgets in a table, with one column for labels and another for the
 * widget.
 *
 * @author Richard Kennard
 */

public class TableLayout
	extends Layout
{
	//
	//
	// Private statics
	//
	//

	private final static int			LABEL_AND_WIDGET	= 2;

	//
	//
	// Private members
	//
	//

	private android.widget.TableLayout	mLayout;

	private String						mCurrentSection;

	@SuppressWarnings( "unused" )
	private int							mNumberOfColumns	= 1;

	private int							mLabelStyle;

	private int							mSectionStyle;

	//
	//
	// Constructor
	//
	//

	public TableLayout( AndroidMetawidget metawidget )
	{
		super( metawidget );

		// Number of columns

		// TODO: test 'numberOfColumns' in HTML Metawidget

		Object numberOfColumns = metawidget.getParameter( "numberOfColumns" );

		if ( numberOfColumns != null )
		{
			if ( numberOfColumns instanceof String )
				mNumberOfColumns = Integer.parseInt( (String) numberOfColumns );
			else
				mNumberOfColumns = (Integer) numberOfColumns;

			if ( mNumberOfColumns < 1 )
				throw MetawidgetException.newException( "numberOfColumns must be >= 1. Use LinearLayout for zero columns" );
		}

		// Label style

		Object labelStyle = metawidget.getParameter( "labelStyle" );

		if ( labelStyle != null )
			mLabelStyle = (Integer) labelStyle;

		// Section style

		Object sectionStyle = metawidget.getParameter( "sectionStyle" );

		if ( sectionStyle != null )
			mSectionStyle = (Integer) sectionStyle;
	}

	//
	//
	// Public methods
	//
	//

	@Override
	public void layoutChild( View view, Map<String, String> attributes )
	{
		initLayout();

		AndroidMetawidget metawidget = getMetawidget();
		TableRow tableRow = new TableRow( metawidget.getContext() );

		String label = null;

		// Section headings

		if ( attributes != null )
		{
			String section = attributes.get( SECTION );

			if ( section != null && !section.equals( mCurrentSection ) )
			{
				mCurrentSection = section;
				layoutSection( section );
			}

			// Labels

			label = getMetawidget().getLabelString( attributes );

			if ( label != null )
			{
				TextView textView = new TextView( metawidget.getContext() );

				if ( !"".equals( label ))
					textView.setText( label + ": " );

				applyStyle( textView, mLabelStyle );

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

	@Override
	public void layoutEnd()
	{
		View viewButtons = getMetawidget().getFacet( "buttons" );

		if ( viewButtons != null )
		{
			initLayout();
			mLayout.addView( viewButtons, new android.widget.TableLayout.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT ) );
		}

		// If the Layout was never used, just put an empty space

		if ( mLayout == null )
			getMetawidget().addView( new TextView( getMetawidget().getContext() ), new android.widget.LinearLayout.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT ) );
	}

	//
	//
	// Protected methods
	//
	//

	protected void layoutSection( String section )
	{
		if ( "".equals( section ) )
			return;

		// Section name (possibly localized)

		AndroidMetawidget metawidget = getMetawidget();
		TextView textView = new TextView( metawidget.getContext() );

		String localizedSection = getMetawidget().getLocalizedKey( section );

		if ( localizedSection != null )
			textView.setText( localizedSection );
		else
			textView.setText( section );

		// Apply style (if any)

		applyStyle( textView, mSectionStyle );

		// Add it as a separate layout, to save horizontal space where possible
		// (eg. the labels in this new section might be smaller than the old section)

		mLayout.addView( textView, new android.widget.TableLayout.LayoutParams() );

		// Start a new layout

		mLayout = new android.widget.TableLayout( metawidget.getContext() );
		mLayout.setOrientation( LinearLayout.VERTICAL );
		mLayout.setColumnStretchable( 1, true );

		getMetawidget().addView( mLayout, new android.widget.LinearLayout.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT ) );
	}

	//
	//
	// Private methods
	//
	//

	/**
	 * Initialize the TableLayout.
	 * <p>
	 * We don't initialize the TableLayout unless we find we have something
	 * to put into it, because Android doesn't like empty TableLayouts.
	 */

	private void initLayout()
	{
		if ( mLayout != null )
			return;

		AndroidMetawidget metawidget = getMetawidget();
		mLayout = new android.widget.TableLayout( metawidget.getContext() );
		mLayout.setOrientation( LinearLayout.VERTICAL );
		mLayout.setColumnStretchable( 1, true );

		metawidget.addView( mLayout, new android.widget.LinearLayout.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT ) );
	}
}
