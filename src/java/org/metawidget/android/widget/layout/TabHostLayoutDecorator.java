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
import org.metawidget.layout.decorator.LayoutDecoratorConfig;
import org.metawidget.util.simple.StringUtils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TabHost.TabContentFactory;

/**
 * Layout to decorate widgets from different sections using a TabHost.
 *
 * @author Richard Kennard
 */

public class TabHostLayoutDecorator
	extends AndroidNestedSectionLayoutDecorator
{
	//
	// Constructor
	//

	public TabHostLayoutDecorator( LayoutDecoratorConfig<View, AndroidMetawidget> config )
	{
		super( config );
	}

	//
	// Protected methods
	//

	@Override
	protected View createSectionWidget( View previousSectionView, Map<String, String> attributes, View container, AndroidMetawidget metawidget )
	{
		// Whole new tab host?

		TabHost tabHost;

		if ( previousSectionView == null )
		{
			tabHost = new TabHost( metawidget.getContext() );
			tabHost.setPadding( 0, 20, 0, 0 );

			// TabWidget for the tab strip

			View tabWidget = new TabWidget( metawidget.getContext() );
			tabWidget.setId( android.R.id.tabs );
			tabHost.addView( tabWidget );

			// FrameLayout for the tab contents

			FrameLayout frameLayout = new FrameLayout( metawidget.getContext() );
			frameLayout.setId( android.R.id.tabcontent );

			// (hack in some padding. Would be better to use a LinearLayout around TabWidget and
			// FrameLayout, but then the whole thing seemed to disappear?)

			frameLayout.setPadding( 0, 75, 0, 0 );
			tabHost.addView( frameLayout );

			tabHost.setup();

			// TODO: calling super calls back to itself? That was the problem?

			// Add to parent container

			((ViewGroup) container).addView( tabHost );
		}
		else
		{
			tabHost = (TabHost) previousSectionView.getParent().getParent();
		}

		// New tab

		final ViewGroup newLayout = new android.widget.LinearLayout( metawidget.getContext() );

		// (non-selected tabs must be invisible by default)

		newLayout.setVisibility( View.INVISIBLE );

		// (add to FrameLayout in advance, so that AndroidMetawidget.setValue can find it)

		((FrameLayout) tabHost.getChildAt( 1 )).addView( newLayout );

		TabContentFactory tabContentFactory = new TabHost.TabContentFactory()
		{
			public View createTabContent( String tag )
			{
				return newLayout;
			}
		};

		// Section name (possibly localized)

		String section = getState( container, metawidget ).currentSection;
		String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection == null )
			localizedSection = section;

		tabHost.addTab( tabHost.newTabSpec( localizedSection ).setIndicator( localizedSection ).setContent( tabContentFactory ) );

		return newLayout;
	}
}
