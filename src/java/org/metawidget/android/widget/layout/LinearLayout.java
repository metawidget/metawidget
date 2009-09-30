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
	// Public methods
	//


	public void onStartBuild( AndroidMetawidget metawidget )
	{
		metawidget.putClientProperty( LinearLayout.class, null );
		State state = getState( metawidget );

		// Label style

		Object labelStyle = metawidget.getParameter( "labelStyle" );

		if ( labelStyle != null )
			state.labelStyle = (Integer) labelStyle;

		// Section style

		Object sectionStyle = metawidget.getParameter( "sectionStyle" );

		if ( sectionStyle != null )
			state.sectionStyle = (Integer) sectionStyle;
	}

	@Override
	public void layoutChild( View view, String elementName, Map<String, String> attributes, AndroidMetawidget metawidget )
	{
		// Ignore empty Stubs

		if ( view instanceof Stub && ( (Stub) view ).getChildCount() == 0 )
			return;

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

			if ( label != null && !"".equals( label.trim() ) )
			{
				TextView textView = new TextView( metawidget.getContext() );
				textView.setText( label + ":" );
				AndroidUtils.applyStyle( textView, state.labelStyle, metawidget );

				metawidget.addView( textView, new android.widget.LinearLayout.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT ) );
			}
		}

		// View

		android.view.ViewGroup.LayoutParams params = view.getLayoutParams();

		if ( params == null )
		{
			// Hack for sizing ListViews

			if ( view instanceof ListView )
				params = new android.widget.LinearLayout.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, 100 );
			else
				params = new android.widget.LinearLayout.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
		}

		metawidget.addView( view, params );
	}

	@Override
	public void onEndBuild( AndroidMetawidget metawidget )
	{
		View viewButtons = metawidget.getFacet( "buttons" );

		if ( viewButtons != null )
		{
			metawidget.addView( viewButtons, new android.widget.TableLayout.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT ) );
		}
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

		String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection != null )
			textView.setText( localizedSection, TextView.BufferType.SPANNABLE );
		else
			textView.setText( section, TextView.BufferType.SPANNABLE );

		// Apply style (if any)

		State state = getState( metawidget );
		AndroidUtils.applyStyle( textView, state.sectionStyle, metawidget );

		// Add it to our layout

		metawidget.addView( textView, new android.widget.LinearLayout.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT ) );
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

		/* package private */int	labelStyle;

		/* package private */int	sectionStyle;
	}
}
