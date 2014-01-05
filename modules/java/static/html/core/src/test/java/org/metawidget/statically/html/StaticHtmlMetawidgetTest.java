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

package org.metawidget.statically.html;

import java.io.StringWriter;

import junit.framework.TestCase;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class StaticHtmlMetawidgetTest
	extends TestCase {

	//
	// Public methods
	//

	public void testSimpleType() {

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		metawidget.setId( "foo" );
		metawidget.setPath( Foo.class.getName() );

		String result = "<table id=\"foo\">\r\n" +
				"\t<tbody>\r\n" +
				"\t\t<tr>\r\n" +
				"\t\t\t<th>\r\n" +
				"\t\t\t\t<label for=\"foo-bar\">Bar:</label>\r\n" +
				"\t\t\t</th>\r\n" +
				"\t\t\t<td>\r\n" +
				"\t\t\t\t<input id=\"foo-bar\" name=\"fooBar\" type=\"text\"/>\r\n" +
				"\t\t\t</td>\r\n" +
				"\t\t\t<td/>\r\n" +
				"\t\t</tr>\r\n" +
				"\t\t<tr>\r\n" +
				"\t\t\t<th>\r\n" +
				"\t\t\t\t<label for=\"foo-baz\">Baz:</label>\r\n" +
				"\t\t\t</th>\r\n" +
				"\t\t\t<td>\r\n" +
				"\t\t\t\t<input id=\"foo-baz\" name=\"fooBaz\" type=\"text\"/>\r\n" +
				"\t\t\t</td>\r\n" +
				"\t\t\t<td/>\r\n" +
				"\t\t</tr>\r\n" +
				"\t</tbody>\r\n" +
				"</table>\r\n";

		StringWriter writer = new StringWriter();
		metawidget.write( writer, 0 );
		assertEquals( result, writer.toString() );
	}

	public void testNestedType() {

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		metawidget.setId( "foo" );
		metawidget.setPath( NestedFoo.class.getName() );

		String result = "<table id=\"foo\">\r\n" +
				"\t<tbody>\r\n" +
				"\t\t<tr>\r\n" +
				"\t\t\t<th>\r\n" +
				"\t\t\t\t<label for=\"foo-abc\">Abc:</label>\r\n" +
				"\t\t\t</th>\r\n" +
				"\t\t\t<td>\r\n" +
				"\t\t\t\t<input id=\"foo-abc\" name=\"fooAbc\" type=\"text\"/>\r\n" +
				"\t\t\t</td>\r\n" +
				"\t\t\t<td/>\r\n" +
				"\t\t</tr>\r\n" +
				"\t\t<tr>\r\n" +
				"\t\t\t<th>\r\n" +
				"\t\t\t\t<label for=\"foo-nestedFoo\">Nested Foo:</label>\r\n" +
				"\t\t\t</th>\r\n" +
				"\t\t\t<td>\r\n" +
				"\t\t\t\t<table id=\"foo-nestedFoo\">\r\n" +
				"\t\t\t\t\t<tbody>\r\n" +
				"\t\t\t\t\t\t<tr>\r\n" +
				"\t\t\t\t\t\t\t<th>\r\n" +
				"\t\t\t\t\t\t\t\t<label for=\"foo-nestedFoo-bar\">Bar:</label>\r\n" +
				"\t\t\t\t\t\t\t</th>\r\n" +
				"\t\t\t\t\t\t\t<td>\r\n" +
				"\t\t\t\t\t\t\t\t<input id=\"foo-nestedFoo-bar\" name=\"fooNestedFooBar\" type=\"text\"/>\r\n" +
				"\t\t\t\t\t\t\t</td>\r\n" +
				"\t\t\t\t\t\t\t<td/>\r\n" +
				"\t\t\t\t\t\t</tr>\r\n" +
				"\t\t\t\t\t\t<tr>\r\n" +
				"\t\t\t\t\t\t\t<th>\r\n" +
				"\t\t\t\t\t\t\t\t<label for=\"foo-nestedFoo-baz\">Baz:</label>\r\n" +
				"\t\t\t\t\t\t\t</th>\r\n" +
				"\t\t\t\t\t\t\t<td>\r\n" +
				"\t\t\t\t\t\t\t\t<input id=\"foo-nestedFoo-baz\" name=\"fooNestedFooBaz\" type=\"text\"/>\r\n" +
				"\t\t\t\t\t\t\t</td>\r\n" +
				"\t\t\t\t\t\t\t<td/>\r\n" +
				"\t\t\t\t\t\t</tr>\r\n" +
				"\t\t\t\t\t</tbody>\r\n" +
				"\t\t\t\t</table>\r\n" +
				"\t\t\t</td>\r\n" +
				"\t\t\t<td/>\r\n" +
				"\t\t</tr>\r\n" +
				"\t</tbody>\r\n" +
				"</table>\r\n";

		StringWriter writer = new StringWriter();
		metawidget.write( writer, 0 );
		assertEquals( result, writer.toString() );
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
