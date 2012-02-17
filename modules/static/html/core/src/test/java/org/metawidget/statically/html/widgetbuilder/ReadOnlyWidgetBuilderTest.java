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

package org.metawidget.statically.html.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.statically.StaticWidget;
import org.metawidget.statically.html.StaticHtmlMetawidget;
import org.metawidget.statically.html.layout.HtmlTableLayout;
import org.metawidget.statically.layout.SimpleLayout;
import org.metawidget.util.CollectionUtils;

public class ReadOnlyWidgetBuilderTest
	extends TestCase {

	//
	// Public methods
	//

	public void testReadOnlyWidgetBuilder() {

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		metawidget.setReadOnly( true );
		metawidget.setId( "foo" );
		metawidget.setPath( NestedFoo.class.getName() );

		String result = "<table id=\"foo\"><tbody>";
		result += "<tr><th><label for=\"foo-abc\">Abc:</label></th><td><div id=\"foo-abc\"/></td><td/></tr>";
		result += "<tr><th><label for=\"foo-nestedFoo\">Nested Foo:</label></th><td><table id=\"foo-nestedFoo\"><tbody>";
		result += "<tr><th><label for=\"foo-nestedFoo-bar\">Bar:</label></th><td><div id=\"foo-nestedFoo-bar\"/></td><td/></tr>";
		result += "<tr><th><label for=\"foo-nestedFoo-baz\">Baz:</label></th><td><div id=\"foo-nestedFoo-baz\"/></td><td/></tr>";
		result += "</tbody></table></td><td/></tr></tbody></table>";

		assertEquals( result, metawidget.toString() );
	}

	public void testReadOnlyDontExpand() {

		ReadOnlyWidgetBuilder widgetBuilder = new ReadOnlyWidgetBuilder();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( TYPE, Foo.class.getName() );
		attributes.put( READ_ONLY, TRUE );
		attributes.put( DONT_EXPAND, TRUE );
		StaticWidget widget = widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( "<div/>", widget.toString() );

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		metawidget.setLayout( new SimpleLayout() );
		attributes = CollectionUtils.newHashMap();
		attributes.put( TYPE, Foo.class.getName() );
		attributes.put( READ_ONLY, TRUE );
		widget = widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( "<div/>", widget.toString() );

		metawidget.setLayout( new HtmlTableLayout() );
		assertEquals( null, widgetBuilder.buildWidget( PROPERTY, attributes, metawidget ));
	}

	//
	// Inner class
	//

	public static class Foo {

		public String getBar() {

			return null;
		}

		public void setBar( @SuppressWarnings( "unused" ) String bar ) {

			// Do nothing
		}

		public String getBaz() {

			return null;
		}

		public void setBaz( @SuppressWarnings( "unused" ) String baz ) {

			// Do nothing
		}
	}

	public static class NestedFoo {

		public String getAbc() {

			return null;
		}

		public void setAbc( @SuppressWarnings( "unused" ) String abc ) {

			// Do nothing
		}

		public Foo getNestedFoo() {

			return null;
		}

		public void setNestedFoo( @SuppressWarnings( "unused" ) Foo nestedFoo ) {

			// Do nothing
		}
	}
}
