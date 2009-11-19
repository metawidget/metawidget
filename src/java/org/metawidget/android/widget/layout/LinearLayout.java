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
import org.metawidget.layout.impl.LayoutUtils;
import org.metawidget.util.simple.StringUtils;

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
	implements Layout<View, AndroidMetawidget>
{
	//
	// Private members
	//

	private int					mLabelStyle;

	private int					mSectionStyle;

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
		mSectionStyle = config.getSectionStyle();
	}

	//
	// Public methods
	//

	public void startLayout( View container, AndroidMetawidget metawidget )
	{
		metawidget.putClientProperty( LinearLayout.class, null );
	}

	public void layoutWidget( View view, String elementName, Map<String, String> attributes, View container, AndroidMetawidget metawidget )
	{
		// Ignore empty Stubs

		if ( view instanceof Stub && ( (Stub) view ).getChildCount() == 0 )
			return;

		ViewGroup viewToAddTo = getViewToAddTo( metawidget );

		String labelText = metawidget.getLabelString( attributes );
		boolean needsLabel = LayoutUtils.needsLabel( labelText, elementName );

		if ( attributes != null )
		{
			// Section headings

			State state = getState( metawidget );
			String section = attributes.get( SECTION );

			if ( section != null && !section.equals( state.currentSection ) )
			{
				state.currentSection = section;
				layoutSection( section, metawidget );
			}
		}

		// Labels

		if ( needsLabel )
		{
			TextView textView = new TextView( metawidget.getContext() );
			textView.setText( labelText + ": " );

			AndroidUtils.applyStyle( textView, mLabelStyle, metawidget );

			viewToAddTo.addView( textView );
		}

		// View

		layoutView( view, viewToAddTo, metawidget, needsLabel );
	}

	public void endLayout( View container, AndroidMetawidget metawidget )
	{
		View viewButtons = metawidget.getFacet( "buttons" );

		if ( viewButtons != null )
		{
			getLayout( metawidget ).addView( viewButtons, new android.widget.LinearLayout.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT ) );
		}
	}

	//
	// Protected methods
	//

	protected void layoutView( View view, ViewGroup viewToAddTo, AndroidMetawidget metawidget, boolean needsLabel )
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

		viewToAddTo.addView( view, params );
	}

	protected void layoutSection( String section, AndroidMetawidget metawidget )
	{
		if ( "".equals( section ) )
			return;

		// Section name (possibly localized)

		TextView textView = new TextView( metawidget.getContext() );

		String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection != null )
			textView.setText( localizedSection, TextView.BufferType.SPANNABLE );
		else
			textView.setText( section, TextView.BufferType.SPANNABLE );

		// Apply style (if any)

		AndroidUtils.applyStyle( textView, mSectionStyle, metawidget );

		// Start a new layout, to save horizontal space where possible
		// (eg. the labels in this new section might be smaller than the old section)

		startNewLayout( metawidget );

		// Add it to the Layout

		getLayout( metawidget ).addView( textView, new android.widget.LinearLayout.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT ) );
	}

	//
	// Protected methods
	//

	protected ViewGroup getViewToAddTo( AndroidMetawidget metawidget )
	{
		// AndroidMetawidget is already a LinearLayout

		return metawidget;
	}

	protected android.widget.LinearLayout getLayout( AndroidMetawidget metawidget )
	{
		// AndroidMetawidget is already a LinearLayout

		return metawidget;
	}

	protected void startNewLayout( AndroidMetawidget metawidget )
	{
		// Do nothing
	}

	//
	// Private methods
	//

	private State getState( AndroidMetawidget metawidget )
	{
		State state = (State) metawidget.getClientProperty( LinearLayout.class );

		if ( state == null )
		{
			state = new State();
			metawidget.putClientProperty( LinearLayout.class, state );
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
