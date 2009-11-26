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
import org.metawidget.layout.delegate.DelegateLayout;
import org.metawidget.util.simple.StringUtils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author Richard Kennard
 */

public class HeadingSectionLayout
	extends DelegateLayout<View, AndroidMetawidget>
{
	//
	// Private members
	//

	private int	mSectionStyle;

	//
	// Constructor
	//

	public HeadingSectionLayout( HeadingSectionLayoutConfig config )
	{
		super( config );

		mSectionStyle = config.getSectionStyle();
	}

	//
	// Public methods
	//

	@Override
	public void startLayout( View container, AndroidMetawidget metawidget )
	{
		super.startLayout( container, metawidget );
		metawidget.putClientProperty( HeadingSectionLayout.class, null );
	}

	@Override
	public void layoutWidget( View view, String elementName, Map<String, String> attributes, View container, AndroidMetawidget metawidget )
	{
		String section = attributes.get( SECTION );

		State state = getState( metawidget );

		// Stay where we are?

		if ( section == null || section.equals( state.currentSection ) )
		{
			super.layoutWidget( view, elementName, attributes, container, metawidget );
			return;
		}

		// End current section
		// TODO: unit test this

		if ( state.currentSection != null )
			super.endLayout( container, metawidget );

		state.currentSection = section;

		// No new section?

		if ( "".equals( section ) )
		{
			super.layoutWidget( view, elementName, attributes, container, metawidget );
			return;
		}

		// Section name (possibly localized)

		String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection == null )
			localizedSection = section;

		TextView textView = new TextView( metawidget.getContext() );
		textView.setText( localizedSection, TextView.BufferType.SPANNABLE );

		// Apply style (if any)

		AndroidUtils.applyStyle( textView, mSectionStyle, metawidget );

		// Add to parent container

		((ViewGroup) container).addView( textView, new android.widget.LinearLayout.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT ) );

		// New section

		super.startLayout( container, metawidget );
		super.layoutWidget( view, elementName, attributes, container, metawidget );
	}

	//
	// Private methods
	//

	private State getState( AndroidMetawidget metawidget )
	{
		State state = (State) metawidget.getClientProperty( HeadingSectionLayout.class );

		if ( state == null )
		{
			state = new State();
			metawidget.putClientProperty( HeadingSectionLayout.class, state );
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
