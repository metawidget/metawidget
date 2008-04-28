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

import org.metawidget.android.widget.AndroidMetawidget;
import org.metawidget.util.StringUtils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Layout to arrange components vertically, with one row for the label and
 * the next for the component.
 *
 * @author Richard Kennard
 */

public class LinearLayout
	extends Layout
{
	//
	//
	// Private members
	//
	//

	private String						mCurrentSection;

	private int							mLabelStyle;

	private int							mSectionStyle;

	//
	//
	// Constructor
	//
	//

	public LinearLayout( AndroidMetawidget metawidget )
	{
		super( metawidget );

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
		AndroidMetawidget metawidget = getMetawidget();

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

			if ( label != null && !"".equals( label.trim() ) )
			{
				TextView textView = new TextView( metawidget.getContext() );
				textView.setText( label + ":" );
				applyStyle( textView, mLabelStyle );

				getMetawidget().addView( textView, new android.widget.LinearLayout.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT ) );
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

		getMetawidget().addView( view, params );
	}

	@Override
	public void layoutEnd()
	{
		AndroidMetawidget metawidget = getMetawidget();
		View viewButtons = metawidget.getFacet( "buttons" );

		if ( viewButtons != null )
		{
			metawidget.addView( viewButtons, new android.widget.TableLayout.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT ) );
		}
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

		String localizedSection = getMetawidget().getLocalizedKey( StringUtils.camelCase( section ));

		if ( localizedSection != null )
			textView.setText( localizedSection, TextView.BufferType.SPANNABLE );
		else
			textView.setText( section, TextView.BufferType.SPANNABLE );

		// Apply style (if any)

		applyStyle( textView, mSectionStyle );

		// Add it to our layout

		getMetawidget().addView( textView, new android.widget.LinearLayout.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT ) );
	}
}
