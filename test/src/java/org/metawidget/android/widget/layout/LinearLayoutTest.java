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
import android.widget.TextView;

/**
 * @author Richard Kennard
 */

public class LinearLayoutTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testLinearLayout()
	{
		AndroidMetawidget androidMetawidget = new AndroidMetawidget( null );
		androidMetawidget.setLayout( new HeadingSectionLayoutDecorator( new HeadingSectionLayoutDecoratorConfig().setLayout( new LinearLayout() )));

		Stub stub = new Stub( null );
		stub.setTag( "stubMe" );
		androidMetawidget.addView( stub );

		Facet buttonsFacet = new Facet( null );
		buttonsFacet.setName( "buttons" );
		androidMetawidget.addView( buttonsFacet );

		androidMetawidget.setToInspect( new Foo() );

		assertTrue( "Bar: ".equals( ((TextView) androidMetawidget.getChildAt( 0 )).getText() ));
		assertTrue( androidMetawidget.getChildAt( 1 ) instanceof EditText );
		assertTrue( "Baz: ".equals( ((TextView) androidMetawidget.getChildAt( 2 )).getText() ));
		assertTrue( androidMetawidget.getChildAt( 3 ) instanceof CheckBox );
		assertTrue( "Foo Section".equals( ((TextView) androidMetawidget.getChildAt( 4 )).getText() ));

		android.widget.LinearLayout linearLayout = (android.widget.LinearLayout) androidMetawidget.getChildAt( 5 );
		assertTrue( "Abc: ".equals( ((TextView) linearLayout.getChildAt( 0 )).getText() ));
		assertTrue( linearLayout.getChildAt( 1 ) instanceof Spinner );
		assertTrue( buttonsFacet == androidMetawidget.getChildAt( 6 ) );

		assertTrue( androidMetawidget.getChildCount() == 7 );
	}

	public void testConfig()
	{
		LinearLayoutConfig config1 = new LinearLayoutConfig();
		LinearLayoutConfig config2 = new LinearLayoutConfig();

		assertTrue( !config1.equals( "foo" ) );
		assertTrue( config1.equals( config2 ) );
		assertTrue( config1.hashCode() == config2.hashCode() );

		// labelStyle

		config1.setLabelStyle( 100 );
		assertTrue( 100 == config1.getLabelStyle() );
		assertTrue( !config1.equals( config2 ) );
		assertTrue( config1.hashCode() != config2.hashCode() );

		config2.setLabelStyle( 100 );
		assertTrue( config1.equals( config2 ) );
		assertTrue( config1.hashCode() == config2.hashCode() );
	}

	//
	// Inner class
	//

	public static class Foo
	{
		public String bar;

		@UiComesAfter( "bar" )
		public boolean baz;

		@UiComesAfter( "baz" )
		@UiSection( "Foo Section" )
		@UiLookup( "one, two, three" )
		public String abc;

		public String stubMe;
	}
}
