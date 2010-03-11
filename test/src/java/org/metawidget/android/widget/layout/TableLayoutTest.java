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

import android.widget.AdapterView;
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
		assertEquals( "Bar: ", ( (TextView) tableRow.getChildAt( 0 ) ).getText() );
		assertTrue( tableRow.getChildAt( 1 ) instanceof EditText );
		tableRow = (TableRow) tableLayout.getChildAt( 1 );
		assertEquals( "Baz: ", ( (TextView) tableRow.getChildAt( 0 ) ).getText() );
		assertTrue( tableRow.getChildAt( 1 ) instanceof CheckBox );

		tableRow = (TableRow) tableLayout.getChildAt( 2 );
		assertEquals( "Foo Section", ( (TextView) tableRow.getChildAt( 0 ) ).getText() );
		tableRow = (TableRow) tableLayout.getChildAt( 3 );
		android.widget.LinearLayout linearLayout = (android.widget.LinearLayout) tableRow.getChildAt( 0 );
		tableLayout = (android.widget.TableLayout) linearLayout.getChildAt( 0 );
		tableRow = (TableRow) tableLayout.getChildAt( 0 );
		assertEquals( "Abc: ", ( (TextView) tableRow.getChildAt( 0 ) ).getText() );
		assertTrue( tableRow.getChildAt( 1 ) instanceof Spinner );
		tableRow = (TableRow) tableLayout.getChildAt( 1 );
		assertEquals( "Nested foo: ", ( (TextView) tableRow.getChildAt( 0 ) ).getText() );
		assertTrue( buttonsFacet == androidMetawidget.getChildAt( 1 ) );
		assertTrue( 2 == androidMetawidget.getChildCount() );

		// nestedFoo

		AndroidMetawidget nestedMetawidget = (AndroidMetawidget) tableRow.getChildAt( 1 );
		tableLayout = (android.widget.TableLayout) nestedMetawidget.getChildAt( 0 );
		tableRow = (TableRow) tableLayout.getChildAt( 0 );
		assertEquals( "Bar: ", ( (TextView) tableRow.getChildAt( 0 ) ).getText() );
		assertTrue( tableRow.getChildAt( 1 ) instanceof EditText );
		tableRow = (TableRow) tableLayout.getChildAt( 1 );
		assertEquals( "Baz: ", ( (TextView) tableRow.getChildAt( 0 ) ).getText() );
		assertTrue( tableRow.getChildAt( 1 ) instanceof CheckBox );

		tableRow = (TableRow) tableLayout.getChildAt( 2 );
		assertEquals( "Foo Section", ( (TextView) tableRow.getChildAt( 0 ) ).getText() );
		tableRow = (TableRow) tableLayout.getChildAt( 3 );
		linearLayout = (android.widget.LinearLayout) tableRow.getChildAt( 0 );
		tableLayout = (android.widget.TableLayout) linearLayout.getChildAt( 0 );
		tableRow = (TableRow) tableLayout.getChildAt( 0 );
		assertEquals( "Abc: ", ( (TextView) tableRow.getChildAt( 0 ) ).getText() );
		AdapterView<?> adapterView = (Spinner) tableRow.getChildAt( 1 );
		assertTrue( tableRow.getChildAt( 1 ) instanceof Spinner );
		tableRow = (TableRow) tableLayout.getChildAt( 1 );
		assertEquals( "Stub me: ", ( (TextView) tableRow.getChildAt( 0 ) ).getText() );
		assertTrue( tableRow.getChildAt( 1 ) instanceof EditText );
		tableRow = (TableRow) tableLayout.getChildAt( 2 );
		assertEquals( "Nested foo: ", ( (TextView) tableRow.getChildAt( 0 ) ).getText() );
		assertTrue( tableRow.getChildAt( 1 ) instanceof AndroidMetawidget );
		assertTrue( 3 == tableLayout.getChildCount() );

		AndroidMetawidget nestedNestedMetawidget = (AndroidMetawidget) tableRow.getChildAt( 1 );
		assertTrue( 1 == nestedNestedMetawidget.getChildCount() );

		// (spacer)

		assertTrue( null == ((TextView) nestedNestedMetawidget.getChildAt( 0 )).getText() );
		assertTrue( 1 == nestedNestedMetawidget.getChildCount() );

		// Get/set nested value

		assertTrue( null == adapterView.getSelectedItem() );
		assertTrue( null == adapterView.getAdapter().getItem( 0 ) );
		assertEquals( "one", adapterView.getAdapter().getItem( 1 ) );
		assertEquals( "two", adapterView.getAdapter().getItem( 2 ) );
		assertEquals( "three", adapterView.getAdapter().getItem( 3 ) );
		androidMetawidget.setValue( "two", "nestedFoo", "abc" );
		assertEquals( "two", adapterView.getSelectedItem() );
		assertEquals( "two", androidMetawidget.getValue( "nestedFoo", "abc" ) );
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
