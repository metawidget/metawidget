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
import org.metawidget.layout.delegate.LayoutDecoratorTest;

import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * @author Richard Kennard
 */

public class HeadingSectionLayoutTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testConfig()
	{
		HeadingSectionLayoutConfig config1 = new HeadingSectionLayoutConfig();
		HeadingSectionLayoutConfig config2 = new HeadingSectionLayoutConfig();

		assertTrue( !config1.equals( "foo" ) );
		assertTrue( config1.equals( config2 ) );
		assertTrue( config1.hashCode() == config2.hashCode() );

		// sectionStyle

		config1.setSectionStyle( 100 );
		assertTrue( 100 == config1.getSectionStyle() );
		assertTrue( !config1.equals( config2 ) );
		assertTrue( config1.hashCode() != config2.hashCode() );

		config2.setSectionStyle( 100 );
		assertTrue( config1.equals( config2 ) );
		assertTrue( config1.hashCode() == config2.hashCode() );

		// superclass

		LayoutDecoratorTest.testConfig( config1, config2, new TableLayout() );
	}

	public void testHeadingSectionLayout()
	{
		AndroidMetawidget androidMetawidget = new AndroidMetawidget( null );
		androidMetawidget.setLayout( new HeadingSectionLayout( new HeadingSectionLayoutConfig().setLayout( new TableLayout() )));
		androidMetawidget.setToInspect( new Foo() );

		Facet facet = new Facet( null );
		facet.setName( "buttons" );
		androidMetawidget.addView( facet );

		android.widget.TableLayout tableLayout = (android.widget.TableLayout) androidMetawidget.getChildAt( 0 );
		TableRow tableRow = (TableRow) tableLayout.getChildAt( 0 );
		assertTrue( "Bar: ".equals( ( (TextView) tableRow.getChildAt( 0 ) ).getText() ) );
		assertTrue( tableRow.getChildAt( 1 ) instanceof EditText );

		// Heading #1

		assertTrue( "heading1".equals( ((TextView) androidMetawidget.getChildAt( 1 ) ).getText() ));
		android.widget.LinearLayout linearLayout = (android.widget.LinearLayout) androidMetawidget.getChildAt( 2 );
		tableLayout = (android.widget.TableLayout) linearLayout.getChildAt( 0 );
		tableRow = (TableRow) tableLayout.getChildAt( 0 );
		assertTrue( "Baz: ".equals( ( (TextView) tableRow.getChildAt( 0 ) ).getText() ) );
		assertTrue( tableRow.getChildAt( 1 ) instanceof CheckBox );
		tableRow = (TableRow) tableLayout.getChildAt( 1 );
		assertTrue( "Abc: ".equals( ( (TextView) tableRow.getChildAt( 0 ) ).getText() ) );
		assertTrue( tableRow.getChildAt( 1 ) instanceof EditText );
		assertTrue( 2 == tableLayout.getChildCount() );

		// Heading  #2

		assertTrue( "heading2".equals( ((TextView) androidMetawidget.getChildAt( 3 ) ).getText() ));
		linearLayout = (android.widget.LinearLayout) androidMetawidget.getChildAt( 4 );
		tableLayout = (android.widget.TableLayout) linearLayout.getChildAt( 0 );
		tableRow = (TableRow) tableLayout.getChildAt( 0 );
		assertTrue( "Def: ".equals( ( (TextView) tableRow.getChildAt( 0 ) ).getText() ) );
		assertTrue( tableRow.getChildAt( 1 ) instanceof EditText );

		// Separate component

		tableLayout = (android.widget.TableLayout) androidMetawidget.getChildAt( 5 );
		tableRow = (TableRow) tableLayout.getChildAt( 0 );
		assertTrue( "Ghi: ".equals( ( (TextView) tableRow.getChildAt( 0 ) ).getText() ) );
		assertTrue( tableRow.getChildAt( 1 ) instanceof Spinner );
		assertTrue( 1 == tableLayout.getChildCount() );

		// Heading #3

		assertTrue( "heading3".equals( ((TextView) androidMetawidget.getChildAt( 6 ) ).getText() ));
		linearLayout = (android.widget.LinearLayout) androidMetawidget.getChildAt( 7 );
		tableLayout = (android.widget.TableLayout) linearLayout.getChildAt( 0 );
		tableRow = (TableRow) tableLayout.getChildAt( 0 );
		assertTrue( "Jkl: ".equals( ( (TextView) tableRow.getChildAt( 0 ) ).getText() ) );
		assertTrue( tableRow.getChildAt( 1 ) instanceof EditText );
		assertTrue( 1 == tableLayout.getChildCount() );

		assertTrue( facet == androidMetawidget.getChildAt( 8 ));
		assertTrue( 9 == androidMetawidget.getChildCount() );
	}

	//
	// Inner class
	//

	public static class Foo
	{
		public String	bar;

		@UiComesAfter( "bar" )
		@UiSection( "heading1" )
		public boolean	baz;

		@UiComesAfter( "baz" )
		public String	abc;

		@UiSection( "heading2" )
		public String	def;

		@UiSection( "" )
		@UiLookup( { "foo", "bar" } )
		public String	ghi;

		@UiSection( "heading3" )
		public String	jkl;
	}
}
