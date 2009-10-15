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
import org.metawidget.android.widget.Stub;
import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiLookup;
import org.metawidget.inspector.annotation.UiSection;

import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * @author Richard Kennard
 */

public class TableLayoutTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testTableLayout()
	{
		AndroidMetawidget androidMetawidget = new AndroidMetawidget( null );
		androidMetawidget.setLayout( new TableLayout() );

		Stub stub = new Stub( null );
		stub.setTag( "stubMe" );
		androidMetawidget.addView( stub );

		Facet buttonsFacet = new Facet( null );
		buttonsFacet.setName( "buttons" );
		androidMetawidget.addView( buttonsFacet );

		Foo foo = new Foo();
		foo.nestedFoo = new Foo();
		androidMetawidget.setToInspect( foo );

		android.widget.TableLayout tableLayout = (android.widget.TableLayout) androidMetawidget.getChildAt( 0 );
		TableRow tableRow = (TableRow) tableLayout.getChildAt( 0 );
		assertTrue( "Bar: ".equals( ( (TextView) tableRow.getChildAt( 0 ) ).getText() ) );
		assertTrue( tableRow.getChildAt( 1 ) instanceof EditText );
		tableRow = (TableRow) tableLayout.getChildAt( 1 );
		assertTrue( "Baz: ".equals( ( (TextView) tableRow.getChildAt( 0 ) ).getText() ) );
		assertTrue( tableRow.getChildAt( 1 ) instanceof CheckBox );
		tableLayout = (android.widget.TableLayout) androidMetawidget.getChildAt( 1 );
		assertTrue( "Foo Section".equals( ( (TextView) tableLayout.getChildAt( 0 ) ).getText() ) );
		tableRow = (TableRow) tableLayout.getChildAt( 1 );
		assertTrue( "Abc: ".equals( ( (TextView) tableRow.getChildAt( 0 ) ).getText() ) );
		assertTrue( tableRow.getChildAt( 1 ) instanceof Spinner );
		tableRow = (TableRow) tableLayout.getChildAt( 2 );
		assertTrue( "Nested foo: ".equals( ( (TextView) tableRow.getChildAt( 0 ) ).getText() ) );
		assertTrue( buttonsFacet == tableLayout.getChildAt( 3 ) );
		assertTrue( 2 == androidMetawidget.getChildCount() );

		// nestedFoo

		AndroidMetawidget nestedMetawidget = (AndroidMetawidget) tableRow.getChildAt( 1 );
		tableLayout = (android.widget.TableLayout) nestedMetawidget.getChildAt( 0 );
		tableRow = (TableRow) tableLayout.getChildAt( 0 );
		assertTrue( "Bar: ".equals( ( (TextView) tableRow.getChildAt( 0 ) ).getText() ) );
		assertTrue( tableRow.getChildAt( 1 ) instanceof EditText );
		tableRow = (TableRow) tableLayout.getChildAt( 1 );
		assertTrue( "Baz: ".equals( ( (TextView) tableRow.getChildAt( 0 ) ).getText() ) );
		assertTrue( tableRow.getChildAt( 1 ) instanceof CheckBox );
		tableLayout = (android.widget.TableLayout) nestedMetawidget.getChildAt( 1 );
		assertTrue( "Foo Section".equals( ( (TextView) tableLayout.getChildAt( 0 ) ).getText() ) );
		tableRow = (TableRow) tableLayout.getChildAt( 1 );
		assertTrue( "Abc: ".equals( ( (TextView) tableRow.getChildAt( 0 ) ).getText() ) );
		Spinner spinner = (Spinner) tableRow.getChildAt( 1 );
		assertTrue( tableRow.getChildAt( 1 ) instanceof Spinner );
		tableRow = (TableRow) tableLayout.getChildAt( 2 );
		assertTrue( "Stub me: ".equals( ( (TextView) tableRow.getChildAt( 0 ) ).getText() ) );
		assertTrue( tableRow.getChildAt( 1 ) instanceof EditText );
		tableRow = (TableRow) tableLayout.getChildAt( 3 );
		assertTrue( "Nested foo: ".equals( ( (TextView) tableRow.getChildAt( 0 ) ).getText() ) );
		assertTrue( tableRow.getChildAt( 1 ) instanceof AndroidMetawidget );
		assertTrue( 4 == tableLayout.getChildCount() );

		AndroidMetawidget nestedNestedMetawidget = (AndroidMetawidget) tableRow.getChildAt( 1 );
		assertTrue( 1 == nestedNestedMetawidget.getChildCount() );

		// (spacer)

		assertTrue( null == ((TextView) nestedNestedMetawidget.getChildAt( 0 )).getText() );
		assertTrue( 1 == nestedNestedMetawidget.getChildCount() );

		// Get/set nested value

		assertTrue( null == spinner.getSelectedItem() );
		assertTrue( null == spinner.getAdapter().getItem( 0 ) );
		assertTrue( "one".equals( spinner.getAdapter().getItem( 1 ) ) );
		assertTrue( "two".equals( spinner.getAdapter().getItem( 2 ) ) );
		assertTrue( "three".equals( spinner.getAdapter().getItem( 3 ) ) );
		androidMetawidget.setValue( "two", "nestedFoo", "abc" );
		assertTrue( "two".equals( spinner.getSelectedItem() ) );
		assertTrue( "two".equals( androidMetawidget.getValue( "nestedFoo", "abc" ) ) );
	}

	//
	// Inner class
	//

	public static class Foo
	{
		public String	bar;

		@UiComesAfter( "bar" )
		public boolean	baz;

		@UiComesAfter( "baz" )
		@UiSection( "Foo Section" )
		@UiLookup( { "one", "two", "three" } )
		public String	abc;

		public String	stubMe;

		@UiComesAfter( { "abc", "stubMe" } )
		public Foo		nestedFoo;
	}
}
