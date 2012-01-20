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
import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.statically.spring.widgetbuilder.FormSelectTag;

/**
 * Basic JUnit Tests for the StaticSpringMetawidget - a Static Metawidget for Spring environments.
 *
 * @author Ryan Bradley
 */

public class StaticSpringMetawidgetTest
	extends TestCase {

	//
	// Public methods
	//

	public static void testNoNamespace() {

		assertNull( new StaticSpringMetawidget().getNamespaceURI() );
	}

	public void testNullPath() {

		StaticSpringMetawidget metawidget = new StaticSpringMetawidget();
		assertEquals( "<table><tbody/></table>", metawidget.toString() );

		Map<String, String> namespaces = metawidget.getNamespaces();

		// Namespaces should be empty as the only child is a <table/> element.
		assertEquals( true, namespaces.isEmpty() );
	}

	public void testAdditionalNamespaces() {

		FormSelectTag select = new FormSelectTag();
		select.putAdditionalNamespaceURI( "foo", "http://foo.bar" );
		StaticSpringMetawidget metawidget = new StaticSpringMetawidget();
		metawidget.getChildren().add( select );

		Map<String, String> namespaces = metawidget.getNamespaces();
		assertEquals( 2, namespaces.size() );
		assertEquals( "http://www.springframework.org/tags/form", namespaces.get( "form" ) );
		assertEquals( "http://foo.bar", namespaces.get( "foo" ) );
	}

	public void testSimpleType() {

		StaticSpringMetawidget metawidget = new StaticSpringMetawidget();
		metawidget.setValue( "foo" );
		metawidget.setPath( Foo.class.getName() );

		String result = "<table id=\"table-orgMetawidgetStaticallySpringStaticSpringMetawidgetTestFoo\">\r\n" +
				"\t<tbody>\r\n" +
				"\t\t<tr>\r\n" +
				"\t\t\t<th>\r\n" +
				"\t\t\t\t<form:label path=\"bar\">Bar</form:label>\r\n" +
				"\t\t\t</th>\r\n" +
				"\t\t\t<td>\r\n" +
				"\t\t\t\t<form:input path=\"bar\"/>\r\n" +
				"\t\t\t</td>\r\n" +
				"\t\t\t<td/>\r\n" +
				"\t\t</tr>\r\n" +
				"\t\t<tr>\r\n" +
				"\t\t\t<th>\r\n" +
				"\t\t\t\t<form:label path=\"baz\">Baz</form:label>\r\n" +
				"\t\t\t</th>\r\n" +
				"\t\t\t<td>\r\n" +
				"\t\t\t\t<form:input path=\"baz\"/>\r\n" +
				"\t\t\t</td>\r\n" +
				"\t\t\t<td/>\r\n" +
				"\t\t</tr>\r\n" +
				"\t</tbody>\r\n" +
				"</table>\r\n";

		StringWriter writer = new StringWriter();
		metawidget.write( writer, 0 );
		assertEquals( result, writer.toString() );

		Map<String, String> namespaces = metawidget.getNamespaces();
		assertEquals( 1, namespaces.size() );
		assertEquals( "http://www.springframework.org/tags/form", namespaces.get( "form" ) );
	}

	public void testNestedType() {

		StaticSpringMetawidget metawidget = new StaticSpringMetawidget();
		metawidget.setValue( "foo" );
		metawidget.setPath( NestedFoo.class.getName() );

		String result = "<table id=\"table-orgMetawidgetStaticallySpringStaticSpringMetawidgetTestNestedFoo\">\r\n" +
				"\t<tbody>\r\n" +
				"\t\t<tr>\r\n" +
				"\t\t\t<th>\r\n" +
				"\t\t\t\t<form:label path=\"abc\">Abc</form:label>\r\n" +
				"\t\t\t</th>\r\n" +
				"\t\t\t<td>\r\n" +
				"\t\t\t\t<form:input path=\"abc\"/>\r\n" +
				"\t\t\t</td>\r\n" +
				"\t\t\t<td/>\r\n" +
				"\t\t</tr>\r\n" +
				"\t\t<tr>\r\n" +
				"\t\t\t<th>\r\n" +
				"\t\t\t\t<form:label path=\"nestedFoo\">Nested Foo</form:label>\r\n" +
				"\t\t\t</th>\r\n" +
				"\t\t\t<td>\r\n" +
				"\t\t\t\t<table id=\"table-orgMetawidgetStaticallySpringStaticSpringMetawidgetTestNestedFooNestedFoo\">\r\n" +
				"\t\t\t\t\t<tbody>\r\n" +
				"\t\t\t\t\t\t<tr>\r\n" +
				"\t\t\t\t\t\t\t<th>\r\n" +
				"\t\t\t\t\t\t\t\t<form:label path=\"nestedFoo.bar\">Bar</form:label>\r\n" +
				"\t\t\t\t\t\t\t</th>\r\n" +
				"\t\t\t\t\t\t\t<td>\r\n" +
				"\t\t\t\t\t\t\t\t<form:input path=\"nestedFoo.bar\"/>\r\n" +
				"\t\t\t\t\t\t\t</td>\r\n" +
				"\t\t\t\t\t\t\t<td/>\r\n" +
				"\t\t\t\t\t\t</tr>\r\n" +
				"\t\t\t\t\t\t<tr>\r\n" +
				"\t\t\t\t\t\t\t<th>\r\n" +
				"\t\t\t\t\t\t\t\t<form:label path=\"nestedFoo.baz\">Baz</form:label>\r\n" +
				"\t\t\t\t\t\t\t</th>\r\n" +
				"\t\t\t\t\t\t\t<td>\r\n" +
				"\t\t\t\t\t\t\t\t<form:input path=\"nestedFoo.baz\"/>\r\n" +
				"\t\t\t\t\t\t\t</td>\r\n" +
				"\t\t\t\t\t\t\t<td/>\r\n" +
				"\t\t\t\t\t\t</tr>\r\n" +
				"\t\t\t\t\t</tbody>\r\n" +
				"\t\t\t\t</table>\r\n" +
				"\t\t\t</td>\r\n" +
				"\t\t\t<td/>\r\n" +
				"\t\t</tr>\r\n" +
				"\t</tbody>\r\n" +
				"</table>\r\n" +
				"";

		StringWriter writer = new StringWriter();
		metawidget.write( writer, 0 );
		assertEquals( result, writer.toString() );

		Map<String, String> namespaces = metawidget.getNamespaces();
		assertEquals( 1, namespaces.size() );
		assertEquals( "http://www.springframework.org/tags/form", namespaces.get( "form" ) );
	}

	//
	// Inner classes
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
