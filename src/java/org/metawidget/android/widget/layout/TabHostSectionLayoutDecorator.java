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
import org.metawidget.layout.decorator.LayoutDecorator;
import org.metawidget.layout.decorator.LayoutDecoratorConfig;
import org.metawidget.util.LayoutUtils;
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

public class TabHostSectionLayoutDecorator
	extends LayoutDecorator<View, AndroidMetawidget>
{
	//
	// Constructor
	//

	public TabHostSectionLayoutDecorator( LayoutDecoratorConfig<View, AndroidMetawidget> config )
	{
		super( config );
	}

	//
	// Public methods
	//

	@Override
	public void startLayout( View container, AndroidMetawidget metawidget )
	{
		super.startLayout( container, metawidget );
		metawidget.putClientProperty( HeadingSectionLayoutDecorator.class, null );
	}

	@Override
	public void layoutWidget( View view, String elementName, Map<String, String> attributes, View container, AndroidMetawidget metawidget )
	{
		String section = LayoutUtils.stripSection( attributes );
		State state = getState( metawidget );

		// Stay where we are?

		if ( section == null || section.equals( state.currentSection ) )
		{
			if ( state.currentLayout != null )
				super.layoutWidget( view, elementName, attributes, state.currentLayout, metawidget );
			else
				super.layoutWidget( view, elementName, attributes, container, metawidget );

			return;
		}

		state.currentSection = section;

		// End current section

		if ( state.currentLayout != null )
			super.endLayout( state.currentLayout, metawidget );

		// No new section?

		if ( "".equals( section ) )
		{
			state.tabHost = null;
			super.layoutWidget( view, elementName, attributes, container, metawidget );
			return;
		}

		// Section name (possibly localized)

		String localizedSection = metawidget.getLocalizedKey( StringUtils.camelCase( section ) );

		if ( localizedSection == null )
			localizedSection = section;

		// Whole new tab host?

		FrameLayout frameLayout;

		if ( state.tabHost == null )
		{
			state.tabHost = new TabHost( metawidget.getContext() );
			state.tabHost.setPadding( 0, 20, 0, 0 );

			// TabWidget for the tab strip

			View tabWidget = new TabWidget( metawidget.getContext() );
			tabWidget.setId( android.R.id.tabs );
			state.tabHost.addView( tabWidget );

			// FrameLayout for the tab contents

			frameLayout = new FrameLayout( metawidget.getContext() );
			frameLayout.setId( android.R.id.tabcontent );

			// (hack in some padding. Would be better to use a LinearLayout around TabWidget and
			// FrameLayout, but then the whole thing seemed to disappear?)

			frameLayout.setPadding( 0, 75, 0, 0 );
			state.tabHost.addView( frameLayout );

			state.tabHost.setup();

			( (ViewGroup) container ).addView( state.tabHost, new android.widget.LinearLayout.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT ) );
		}
		else
		{
			frameLayout = (FrameLayout) state.tabHost.getChildAt( 1 );
		}

		final ViewGroup newLayout = new android.widget.LinearLayout( metawidget.getContext() );

		// (non-selected tabs must be invisible by default)

		newLayout.setVisibility( View.INVISIBLE );
		state.currentLayout = newLayout;

		// (add to FrameLayout in advance, so that AndroidMetawidget.setValue can find it)

		frameLayout.addView( newLayout );

		TabContentFactory tabContentFactory = new TabHost.TabContentFactory()
		{
			public View createTabContent( String tag )
			{
				return newLayout;
			}
		};

		state.tabHost.addTab( state.tabHost.newTabSpec( localizedSection ).setIndicator( localizedSection ).setContent( tabContentFactory ) );

		// Add view to new tab

		super.startLayout( state.currentLayout, metawidget );
		super.layoutWidget( view, elementName, attributes, state.currentLayout, metawidget );
	}

	//
	// Private methods
	//

	private State getState( AndroidMetawidget metawidget )
	{
		State state = (State) metawidget.getClientProperty( HeadingSectionLayoutDecorator.class );

		if ( state == null )
		{
			state = new State();
			metawidget.putClientProperty( HeadingSectionLayoutDecorator.class, state );
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
		/* package private */String		currentSection;

		/* package private */TabHost	tabHost;

		/* package private */ViewGroup	currentLayout;
	}
}
