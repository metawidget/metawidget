// Metawidget
//
// For historical reasons, this file is licensed under the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html).
//
// Most other files in Metawidget are licensed under both the
// LGPL/EPL and a commercial license. See http://metawidget.org
// for details.

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

		String result = "<table>\r\n" +
				"\t<tbody>\r\n" +
				"\t\t<tr>\r\n" +
				"\t\t\t<th>\r\n" +
				"\t\t\t\t<form:label path=\"bar\">Bar:</form:label>\r\n" +
				"\t\t\t</th>\r\n" +
				"\t\t\t<td>\r\n" +
				"\t\t\t\t<form:input path=\"bar\"/>\r\n" +
				"\t\t\t</td>\r\n" +
				"\t\t\t<td/>\r\n" +
				"\t\t</tr>\r\n" +
				"\t\t<tr>\r\n" +
				"\t\t\t<th>\r\n" +
				"\t\t\t\t<form:label path=\"baz\">Baz:</form:label>\r\n" +
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

		String result = "<table>\r\n" +
				"\t<tbody>\r\n" +
				"\t\t<tr>\r\n" +
				"\t\t\t<th>\r\n" +
				"\t\t\t\t<form:label path=\"abc\">Abc:</form:label>\r\n" +
				"\t\t\t</th>\r\n" +
				"\t\t\t<td>\r\n" +
				"\t\t\t\t<form:input path=\"abc\"/>\r\n" +
				"\t\t\t</td>\r\n" +
				"\t\t\t<td/>\r\n" +
				"\t\t</tr>\r\n" +
				"\t\t<tr>\r\n" +
				"\t\t\t<th>\r\n" +
				"\t\t\t\t<form:label path=\"nestedFoo\">Nested Foo:</form:label>\r\n" +
				"\t\t\t</th>\r\n" +
				"\t\t\t<td>\r\n" +
				"\t\t\t\t<table id=\"nestedFoo\">\r\n" +
				"\t\t\t\t\t<tbody>\r\n" +
				"\t\t\t\t\t\t<tr>\r\n" +
				"\t\t\t\t\t\t\t<th>\r\n" +
				"\t\t\t\t\t\t\t\t<form:label path=\"nestedFoo.bar\">Bar:</form:label>\r\n" +
				"\t\t\t\t\t\t\t</th>\r\n" +
				"\t\t\t\t\t\t\t<td>\r\n" +
				"\t\t\t\t\t\t\t\t<form:input path=\"nestedFoo.bar\"/>\r\n" +
				"\t\t\t\t\t\t\t</td>\r\n" +
				"\t\t\t\t\t\t\t<td/>\r\n" +
				"\t\t\t\t\t\t</tr>\r\n" +
				"\t\t\t\t\t\t<tr>\r\n" +
				"\t\t\t\t\t\t\t<th>\r\n" +
				"\t\t\t\t\t\t\t\t<form:label path=\"nestedFoo.baz\">Baz:</form:label>\r\n" +
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
