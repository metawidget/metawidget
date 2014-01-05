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

package org.metawidget.statically.html.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.awt.Color;
import java.util.Date;
import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.statically.StaticWidget;
import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.html.StaticHtmlMetawidget;
import org.metawidget.statically.html.layout.HtmlTableLayout;
import org.metawidget.statically.layout.SimpleLayout;
import org.metawidget.util.CollectionUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class ReadOnlyWidgetBuilderTest
	extends TestCase {

	//
	// Public methods
	//

	public void testReadOnlyWidgetBuilder() {

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		ReadOnlyWidgetBuilder widgetBuilder = new ReadOnlyWidgetBuilder();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( READ_ONLY, TRUE );

		// Masked

		attributes.put( MASKED, TRUE );
		StaticXmlWidget widget = widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( "<output></output>", widget.toString() );
		attributes.remove( MASKED );

		// Date

		attributes.put( TYPE, Date.class.getName() );
		widget = widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( "<output></output>", widget.toString() );

		// Color

		attributes.put( TYPE, Color.class.getName() );
		widget = widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( "<output></output>", widget.toString() );

		// Number

		attributes.put( TYPE, int.class.getName() );
		widget = widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( "<output></output>", widget.toString() );

		// From Metawidget

		metawidget.setReadOnly( true );
		metawidget.setId( "foo" );
		metawidget.setPath( NestedFoo.class.getName() );

		String result = "<table id=\"foo\"><tbody>";
		result += "<tr><th><label for=\"foo-abc\">Abc:</label></th><td><output id=\"foo-abc\" name=\"fooAbc\"></output></td><td/></tr>";
		result += "<tr><th><label for=\"foo-nestedFoo\">Nested Foo:</label></th><td><table id=\"foo-nestedFoo\"><tbody>";
		result += "<tr><th><label for=\"foo-nestedFoo-bar\">Bar:</label></th><td><output id=\"foo-nestedFoo-bar\" name=\"fooNestedFooBar\"></output></td><td/></tr>";
		result += "<tr><th><label for=\"foo-nestedFoo-baz\">Baz:</label></th><td><output id=\"foo-nestedFoo-baz\" name=\"fooNestedFooBaz\"></output></td><td/></tr>";
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
		assertEquals( "<output></output>", widget.toString() );

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		metawidget.setLayout( new SimpleLayout() );
		attributes = CollectionUtils.newHashMap();
		attributes.put( TYPE, Foo.class.getName() );
		attributes.put( READ_ONLY, TRUE );
		widget = widgetBuilder.buildWidget( PROPERTY, attributes, metawidget );
		assertEquals( "<output></output>", widget.toString() );

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
