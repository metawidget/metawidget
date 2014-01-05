// Metawidget
//
// For historical reasons, this file is licensed under the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html).
//
// Most other files in Metawidget are licensed under both the
// LGPL/EPL and a commercial license. See http://metawidget.org
// for details.

package org.metawidget.statically.html.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Date;
import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.html.StaticHtmlMetawidget;
import org.metawidget.statically.html.widgetbuilder.HtmlTag;
import org.metawidget.util.CollectionUtils;

/**
 * @author Ryan Bradley
 */

public class CssStyleProcessorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testWidgetProcessor() {

		CssStyleProcessor processor = new CssStyleProcessor();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		StaticXmlWidget tag = new HtmlTag( "input" );

		// No style

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		tag = processor.processWidget( tag, PROPERTY, attributes, metawidget );
		assertEquals( "<input/>", tag.toString() );

		// Simple styles and classes

		metawidget.putAttribute( "style", "foo" );
		metawidget.putAttribute( "class", "bar" );
		tag = processor.processWidget( tag, PROPERTY, attributes, metawidget );
		assertEquals( "<input class=\"bar\" style=\"foo\"/>", tag.toString() );

		// Compound styles and classes

		metawidget.putAttribute( "style", "foo2" );
		metawidget.putAttribute( "class", "bar2" );
		tag = processor.processWidget( tag, PROPERTY, attributes, metawidget );
		assertEquals( "<input class=\"bar bar2\" style=\"foo foo2\"/>", tag.toString() );
	}

	public void testSimpleType() {

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		metawidget.putAttribute( "class", "classy" );
		metawidget.putAttribute( "style", "stylin" );
		metawidget.setId( "foo" );
		metawidget.setPath( Foo.class.getName() );

		String result = "<table id=\"foo\"><tbody>";
		result += "<tr><th><label for=\"foo-bar\">Bar:</label></th><td><input class=\"classy\" id=\"foo-bar\" name=\"fooBar\" style=\"stylin\" type=\"date\"/></td><td/></tr>";
		result += "<tr><th><label for=\"foo-baz\">Baz:</label></th><td><input class=\"classy\" id=\"foo-baz\" name=\"fooBaz\" style=\"stylin\" type=\"number\"/></td><td/></tr>";
		result += "</tbody></table>";
		assertEquals( result, metawidget.toString() );

	}

	//
	// Inner class
	//

	public static class Foo {

		public Date getBar() {

			return null;
		}

		public void setBar( @SuppressWarnings( "unused" ) Date bar ) {

			// Do nothing
		}

		public int getBaz() {

			return 0;
		}

		public void setBaz( @SuppressWarnings( "unused" ) int baz ) {

			// Do nothing
		}
	}
}
