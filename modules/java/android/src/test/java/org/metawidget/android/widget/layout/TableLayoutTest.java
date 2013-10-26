// Metawidget
//
// This library is dual licensed under both LGPL and a commercial
// license.
//
// LGPL: this library is free software; you can redistribute it and/or
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
//
// Commercial License: See http://metawidget.org for details

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
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class TableLayoutTest
	extends TestCase {

	//
	// Public methods
	//

	public void testTableLayout() {

		AndroidMetawidget androidMetawidget = new AndroidMetawidget( null );

		Stub stub = new Stub( null );
		stub.setTag( "stubMe" );
		androidMetawidget.addView( stub );

		Facet buttonsFacet = new Facet( null );
		buttonsFacet.setName( "buttons" );
		androidMetawidget.addView( buttonsFacet );

		Foo foo = new Foo();
		foo.setNestedFoo( new Foo() );
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
		assertEquals( "Nested Foo: ", ( (TextView) tableRow.getChildAt( 0 ) ).getText() );
		assertEquals( buttonsFacet, androidMetawidget.getChildAt( 1 ) );
		assertEquals( 2, androidMetawidget.getChildCount() );

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
		assertEquals( "Stub Me: ", ( (TextView) tableRow.getChildAt( 0 ) ).getText() );
		assertTrue( tableRow.getChildAt( 1 ) instanceof EditText );
		tableRow = (TableRow) tableLayout.getChildAt( 2 );
		assertEquals( "Nested Foo: ", ( (TextView) tableRow.getChildAt( 0 ) ).getText() );
		assertTrue( tableRow.getChildAt( 1 ) instanceof AndroidMetawidget );
		assertEquals( 3, tableLayout.getChildCount() );

		AndroidMetawidget nestedNestedMetawidget = (AndroidMetawidget) tableRow.getChildAt( 1 );
		assertEquals( 1, nestedNestedMetawidget.getChildCount() );

		// (spacer)

		assertEquals( null, ( (TextView) nestedNestedMetawidget.getChildAt( 0 ) ).getText() );
		assertEquals( 1, nestedNestedMetawidget.getChildCount() );

		// Get/set nested value

		assertEquals( null, adapterView.getSelectedItem() );
		assertEquals( null, adapterView.getAdapter().getItem( 0 ) );
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

	public static class Foo {

		private Foo	mNestedFoo;

		public String getBar() {

			return null;
		}

		public void setBar( @SuppressWarnings( "unused" ) String bar ) {

			// Do nothing
		}

		@UiComesAfter( "bar" )
		public boolean isBaz() {

			return false;
		}

		public void setBaz( @SuppressWarnings( "unused" ) boolean baz ) {

			// Do nothing
		}

		@UiComesAfter( "baz" )
		@UiSection( "Foo Section" )
		@UiLookup( { "one", "two", "three" } )
		public String getAbc() {

			return null;
		}

		public void setAbc( @SuppressWarnings( "unused" ) String abc ) {

			// Do nothing
		}

		public String getStubMe() {

			return null;
		}

		public void setStubMe( @SuppressWarnings( "unused" ) String stubMe ) {

			// Do nothing
		}

		@UiComesAfter( { "abc", "stubMe" } )
		public Foo getNestedFoo() {

			return mNestedFoo;
		}

		public void setNestedFoo( Foo nestedFoo ) {

			mNestedFoo = nestedFoo;
		}
	}
}
