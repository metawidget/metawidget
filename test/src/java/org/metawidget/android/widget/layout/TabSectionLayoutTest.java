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

import junit.framework.TestCase;

import org.metawidget.android.widget.AndroidMetawidget;
import org.metawidget.android.widget.Facet;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiLookup;
import org.metawidget.inspector.annotation.UiSection;
import org.metawidget.layout.delegate.DelegateLayoutConfig;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * @author Richard Kennard
 */

public class TabSectionLayoutTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testTabSectionLayout()
	{
		AndroidMetawidget androidMetawidget = new AndroidMetawidget( null );
		androidMetawidget.setLayout( new TabSectionLayout( new DelegateLayoutConfig<View,AndroidMetawidget>().setLayout( new TableLayout() )));
		androidMetawidget.setToInspect( new Foo() );

		Facet facet = new Facet( null );
		facet.setName( "buttons" );
		androidMetawidget.addView( facet );

		android.widget.TableLayout tableLayout = (android.widget.TableLayout) androidMetawidget.getChildAt( 0 );
		TableRow tableRow = (TableRow) tableLayout.getChildAt( 0 );
		assertTrue( "Bar: ".equals( ( (TextView) tableRow.getChildAt( 0 ) ).getText() ) );
		assertTrue( tableRow.getChildAt( 1 ) instanceof EditText );

		// Tab Host #1

		TabHost tabHost = (TabHost) androidMetawidget.getChildAt( 1 );
		assertTrue( tabHost.getChildAt( 0 ) instanceof TabWidget );
		assertTrue( tabHost.getChildAt( 1 ) instanceof FrameLayout );

		// Tab 1

		assertTrue( "tab1".equals( tabHost.getTabSpec( 0 ).getIndicator() ));
		android.widget.LinearLayout tab = (android.widget.LinearLayout) tabHost.getTabSpec( 0 ).getContent().createTabContent( null );
		tableLayout = (android.widget.TableLayout) tab.getChildAt( 0 );
		tableRow = (TableRow) tableLayout.getChildAt( 0 );
		assertTrue( "Baz: ".equals( ( (TextView) tableRow.getChildAt( 0 ) ).getText() ) );
		assertTrue( tableRow.getChildAt( 1 ) instanceof CheckBox );
		tableRow = (TableRow) tableLayout.getChildAt( 1 );
		assertTrue( "Abc: ".equals( ( (TextView) tableRow.getChildAt( 0 ) ).getText() ) );
		assertTrue( tableRow.getChildAt( 1 ) instanceof EditText );
		assertTrue( 2 == tableLayout.getChildCount() );

		// Tab 2

		assertTrue( "tab2".equals( tabHost.getTabSpec( 1 ).getIndicator() ));
		tab = (android.widget.LinearLayout) tabHost.getTabSpec( 1 ).getContent().createTabContent( null );
		tableLayout = (android.widget.TableLayout) tab.getChildAt( 0 );
		tableRow = (TableRow) tableLayout.getChildAt( 0 );
		assertTrue( "Def: ".equals( ( (TextView) tableRow.getChildAt( 0 ) ).getText() ) );
		assertTrue( tableRow.getChildAt( 1 ) instanceof EditText );
		assertTrue( 1 == tableLayout.getChildCount() );

		// Separate component

		tableLayout = (android.widget.TableLayout) androidMetawidget.getChildAt( 2 );
		tableRow = (TableRow) tableLayout.getChildAt( 0 );
		assertTrue( "Ghi: ".equals( ( (TextView) tableRow.getChildAt( 0 ) ).getText() ) );
		assertTrue( tableRow.getChildAt( 1 ) instanceof Spinner );

		// Tab Host #2

		tabHost = (TabHost) androidMetawidget.getChildAt( 3 );
		assertTrue( tabHost.getChildAt( 0 ) instanceof TabWidget );
		assertTrue( tabHost.getChildAt( 1 ) instanceof FrameLayout );

		// Tab A

		assertTrue( "tabA".equals( tabHost.getTabSpec( 0 ).getIndicator() ));
		tab = (android.widget.LinearLayout) tabHost.getTabSpec( 0 ).getContent().createTabContent( null );
		tableLayout = (android.widget.TableLayout) tab.getChildAt( 0 );
		tableRow = (TableRow) tableLayout.getChildAt( 0 );
		assertTrue( "Jkl: ".equals( ( (TextView) tableRow.getChildAt( 0 ) ).getText() ) );
		assertTrue( tableRow.getChildAt( 1 ) instanceof EditText );
		assertTrue( 1 == tableLayout.getChildCount() );

		assertTrue( facet == androidMetawidget.getChildAt( 4 ));
		assertTrue( 5 == androidMetawidget.getChildCount() );
	}

	//
	// Inner class
	//

	public static class Foo
	{
		public String	bar;

		@UiComesAfter( "bar" )
		@UiSection( "tab1" )
		public boolean	baz;

		@UiComesAfter( "baz" )
		public String	abc;

		@UiSection( "tab2" )
		public String	def;

		@UiSection( "" )
		@UiLookup( { "foo", "bar" } )
		public String	ghi;

		@UiSection( "tabA" )
		public String	jkl;
	}
}
