// Metawidget (licensed under LGPL)
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
