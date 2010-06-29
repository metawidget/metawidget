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

package android.widget;

import java.util.List;

import org.metawidget.util.CollectionUtils;

import android.content.Context;
import android.view.View;

/**
 * Dummy implementation for unit testing.
 *
 * @author Richard Kennard
 */

public class TabHost
	extends FrameLayout {

	//
	// Private members
	//

	private List<TabSpec>	mTabs;

	//
	// Constructors
	//

	public TabHost( Context context ) {

		super( context );
	}

	//
	// Public methods
	//

	public TabHost.TabSpec newTabSpec( String name ) {

		return new TabSpec();
	}

	public void addTab( TabSpec tabSpec ) {

		mTabs.add( tabSpec );
	}

	public TabSpec getTabSpec( int index ) {

		return mTabs.get( index );
	}

	public void setup() {

		// Enforce must call setup() before addTab()

		mTabs = CollectionUtils.newArrayList();
	}

	//
	// Inner class
	//

	public static interface TabContentFactory {

		View createTabContent( String tag );
	}

	public static class TabSpec {

		//
		// Private members
		//

		private CharSequence		mIndicator;

		private TabContentFactory	mContent;

		//
		// Public methods
		//

		public TabSpec setIndicator( CharSequence indicator ) {

			mIndicator = indicator;

			return this;
		}

		public CharSequence getIndicator() {

			return mIndicator;
		}

		public TabSpec setContent( TabContentFactory content ) {

			mContent = content;

			return this;
		}

		public TabContentFactory getContent() {

			return mContent;
		}
	}
}
