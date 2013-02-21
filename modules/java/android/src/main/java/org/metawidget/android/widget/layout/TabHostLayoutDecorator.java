// Metawidget (licensed under LGPL)
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
import org.metawidget.layout.decorator.LayoutDecoratorConfig;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.StringUtils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabWidget;

/**
 * Layout to decorate widgets from different sections using a TabHost.
 *
 * @author Richard Kennard
 */

public class TabHostLayoutDecorator
	extends AndroidNestedSectionLayoutDecorator {

	//
	// Constructor
	//

	public TabHostLayoutDecorator( LayoutDecoratorConfig<View, ViewGroup, AndroidMetawidget> config ) {

		super( config );
	}

	//
	// Protected methods
	//

	@Override
	protected ViewGroup createSectionWidget( ViewGroup previousSectionView, String section, Map<String, String> attributes, ViewGroup container, AndroidMetawidget metawidget ) {

		// Whole new tab host?

		TabHost tabHost;

		if ( previousSectionView == null ) {

			// Use the 'AttributeSet' version of the constructor, as it has a different codepath
			// which initializes mTabLayoutId. This is required for 'addTab' to work

			tabHost = new TabHost( metawidget.getContext(), null );
			tabHost.setPadding( 0, 20, 0, 0 );

			// LinearLayout to separate TabWidget and FrameLayout (as per
			// http://developer.android.com/guide/tutorials/views/hello-tabwidget.html)

			android.widget.LinearLayout tabHostLayout = new android.widget.LinearLayout( metawidget.getContext() );
			tabHostLayout.setOrientation( LinearLayout.VERTICAL );
			tabHost.addView( tabHostLayout );

			// TabWidget for the tab strip

			View tabWidget = new TabWidget( metawidget.getContext() );
			tabWidget.setId( android.R.id.tabs );
			tabHostLayout.addView( tabWidget );

			// FrameLayout for the tab contents

			FrameLayout frameLayout = new FrameLayout( metawidget.getContext() );
			frameLayout.setId( android.R.id.tabcontent );
			frameLayout.setPadding( 0, 5, 0, 0 );
			tabHostLayout.addView( frameLayout );

			tabHost.setup();

			Map<String, String> tabHostAttributes = CollectionUtils.newHashMap();
			tabHostAttributes.put( LABEL, "" );
			tabHostAttributes.put( LARGE, TRUE );
			getDelegate().layoutWidget( tabHost, PROPERTY, tabHostAttributes, container, metawidget );
		} else {
			tabHost = (TabHost) previousSectionView.getParent().getParent().getParent();
		}

		// New tab

		final android.widget.LinearLayout newLayout = new android.widget.LinearLayout( metawidget.getContext() );
		newLayout.setOrientation( LinearLayout.VERTICAL );

		// (non-selected tabs must be invisible by default)

		newLayout.setVisibility( View.INVISIBLE );

		// (add to FrameLayout in advance, so that AndroidMetawidget.setValue can find it)

		( (FrameLayout) ( (android.widget.LinearLayout) tabHost.getChildAt( 0 ) ).getChildAt( 1 ) ).addView( newLayout );

		TabContentFactory tabContentFactory = new TabHost.TabContentFactory() {

			public View createTabContent( String tag ) {

				return newLayout;
			}
		};

		// Section name (possibly localized)

		String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection == null ) {
			localizedSection = section;
		}

		tabHost.addTab( tabHost.newTabSpec( localizedSection ).setIndicator( localizedSection ).setContent( tabContentFactory ) );

		return newLayout;
	}
}
