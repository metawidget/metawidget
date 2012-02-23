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

package org.metawidget.statically.html.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Date;
import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.statically.html.StaticHtmlMetawidget;
import org.metawidget.statically.html.widgetbuilder.HtmlInput;
import org.metawidget.util.CollectionUtils;

public class IdProcessorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testWidgetProcessor() {

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		IdProcessor processor = new IdProcessor();
		HtmlInput tag = new HtmlInput();

		// Null metawidget value, no name

		processor.processWidget( tag, PROPERTY, attributes, metawidget );
		assertEquals( "<input/>", tag.toString() );

		// Null metawidget value, name
		attributes.put( NAME, "foo" );
		processor.processWidget( tag, PROPERTY, attributes, metawidget );
		assertEquals( "<input id=\"foo\"/>", tag.toString() );

		// Metawidget value
		metawidget.setId( "FooBar" );
		processor.processWidget( tag, PROPERTY, attributes, metawidget );
		assertEquals( "<input id=\"FooBar-foo\"/>", tag.toString() );

	}

	public void testSimpleType() {

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		metawidget.setId( "name-processor-example" );
		metawidget.setPath( Foo.class.getName() );

		String result = "<table id=\"name-processor-example\"><tbody><tr><th>" +
				"<label for=\"name-processor-example-bar\">Bar:</label></th><td><input id=\"name-processor-example-bar\" name=\"nameProcessorExampleBar\" type=\"date\"/></td>" +
				"<td/>" +
				"</tr><tr>" +
				"<th><label for=\"name-processor-example-baz\">Baz:</label></th>" +
				"<td><input id=\"name-processor-example-baz\" name=\"nameProcessorExampleBaz\" type=\"number\"/></td>" +
				"<td/></tr></tbody></table>";

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
