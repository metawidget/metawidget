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

package org.metawidget.statically.spring;

import java.io.StringWriter;

import junit.framework.TestCase;

public class StaticSpringMetawidgetTest
	extends TestCase {

	//
	// Public methods
	//

	public void testSimpleType() {

		StaticSpringMetawidget metawidget = new StaticSpringMetawidget();
		metawidget.setValue( "foo" );
		metawidget.setPath( Foo.class.getName() );

		String result = "<table>\r\n" +
				"\t<form:input path=\"bar\"/>\r\n" +
				"\t<form:input path=\"baz\"/>\r\n" +
				"</table>\r\n";

		StringWriter writer = new StringWriter();
		metawidget.write( writer, 0 );
		assertEquals( result, writer.toString() );
	}

	public void testNestedType() {

		StaticSpringMetawidget metawidget = new StaticSpringMetawidget();
		metawidget.setValue( "foo" );
		metawidget.setPath( NestedFoo.class.getName() );

		String result = "<table>\r\n" +
				"\t<form:input path=\"abc\"/>\r\n" +
				"\t<table>\r\n" +
				"\t\t<form:input path=\"nestedFoo.bar\"/>\r\n" +
				"\t\t<form:input path=\"nestedFoo.baz\"/>\r\n" +
				"\t</table>\r\n" +
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
