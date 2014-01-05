// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

package android.widget;

import java.util.List;

import org.metawidget.util.CollectionUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Dummy implementation for unit testing.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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

	/**
	 * This form of the Constructor (as opposed to the one that doesn't take an AttributeSet) is
	 * needed to initialize mTabLayoutId. This is required for 'addTab' to work.
	 *
	 * @param attrs
	 *            needed to initialize mTabLayoutId. Can be null
	 */

	public TabHost( Context context, AttributeSet attrs ) {

		super( context );
	}

	//
	// Public methods
	//

	/**
	 * @param name
	 */

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
