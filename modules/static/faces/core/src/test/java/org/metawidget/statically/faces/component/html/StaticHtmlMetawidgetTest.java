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

package org.metawidget.statically.faces.component.html;

import java.io.StringWriter;
import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.inspector.annotation.UiSection;
import org.metawidget.statically.faces.component.html.layout.HtmlPanelGrid;

public class StaticHtmlMetawidgetTest
	extends TestCase {

	//
	// Public methods
	//

	public static void testNoNamespace() {

		assertEquals( null, new StaticHtmlMetawidget().getNamespaceURI() );
	}

	public void testNullPath() {

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		assertEquals( "<h:panelGrid columns=\"3\"/>", metawidget.toString() );

		Map<String, String> namespaces = metawidget.getNamespaces();
		assertEquals( "http://java.sun.com/jsf/html", namespaces.get( "h" ) );
		assertEquals( 1, namespaces.size() );
	}

	public void testAdditionalNamespaces() {

		HtmlPanelGrid panelGrid = new HtmlPanelGrid();
		panelGrid.putAdditionalNamespaceURI( "foo", "http://foo.bar" );
		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		metawidget.getChildren().add( panelGrid );

		Map<String, String> namespaces = metawidget.getNamespaces();
		assertEquals( "http://java.sun.com/jsf/html", namespaces.get( "h" ) );
		assertEquals( "http://foo.bar", namespaces.get( "foo" ) );
		assertEquals( 2, namespaces.size() );
	}

	public void testInitialIndent() {

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		metawidget.setPath( Foo.class.getName() );
		StringWriter writer = new StringWriter();
		metawidget.write( writer, 0 );

		String result = "<h:panelGrid columns=\"3\">\r\n" +
				"\t<h:outputLabel value=\"Bar:\"/>\r\n" +
				"\t<h:panelGroup>\r\n" +
				"\t\t<h:inputText/>\r\n" +
				"\t\t<h:message/>\r\n" +
				"\t</h:panelGroup>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel value=\"Baz:\"/>\r\n" +
				"\t<h:panelGroup>\r\n" +
				"\t\t<h:inputText/>\r\n" +
				"\t\t<h:message/>\r\n" +
				"\t</h:panelGroup>\r\n" +
				"\t<h:outputText/>\r\n" +
				"</h:panelGrid>\r\n";
		assertEquals( result, writer.toString() );

		writer = new StringWriter();
		metawidget.write( writer, 1 );

		result = "\t<h:panelGrid columns=\"3\">\r\n" +
				"\t\t<h:outputLabel value=\"Bar:\"/>\r\n" +
				"\t\t<h:panelGroup>\r\n" +
				"\t\t\t<h:inputText/>\r\n" +
				"\t\t\t<h:message/>\r\n" +
				"\t\t</h:panelGroup>\r\n" +
				"\t\t<h:outputText/>\r\n" +
				"\t\t<h:outputLabel value=\"Baz:\"/>\r\n" +
				"\t\t<h:panelGroup>\r\n" +
				"\t\t\t<h:inputText/>\r\n" +
				"\t\t\t<h:message/>\r\n" +
				"\t\t</h:panelGroup>\r\n" +
				"\t\t<h:outputText/>\r\n" +
				"\t</h:panelGrid>\r\n";

		assertEquals( result, writer.toString() );

		writer = new StringWriter();
		metawidget.write( writer, 5 );

		result = "\t\t\t\t\t<h:panelGrid columns=\"3\">\r\n" +
				"\t\t\t\t\t\t<h:outputLabel value=\"Bar:\"/>\r\n" +
				"\t\t\t\t\t\t<h:panelGroup>\r\n" +
				"\t\t\t\t\t\t\t<h:inputText/>\r\n" +
				"\t\t\t\t\t\t\t<h:message/>\r\n" +
				"\t\t\t\t\t\t</h:panelGroup>\r\n" +
				"\t\t\t\t\t\t<h:outputText/>\r\n" +
				"\t\t\t\t\t\t<h:outputLabel value=\"Baz:\"/>\r\n" +
				"\t\t\t\t\t\t<h:panelGroup>\r\n" +
				"\t\t\t\t\t\t\t<h:inputText/>\r\n" +
				"\t\t\t\t\t\t\t<h:message/>\r\n" +
				"\t\t\t\t\t\t</h:panelGroup>\r\n" +
				"\t\t\t\t\t\t<h:outputText/>\r\n" +
				"\t\t\t\t\t</h:panelGrid>\r\n";

		assertEquals( result, writer.toString() );

		writer = new StringWriter();
		metawidget.write( writer, 5 );

		result = "<h:panelGrid columns=\"3\">\r\n" +
				"\t\t\t\t\t\t<h:outputLabel value=\"Bar:\"/>\r\n" +
				"\t\t\t\t\t\t<h:panelGroup>\r\n" +
				"\t\t\t\t\t\t\t<h:inputText/>\r\n" +
				"\t\t\t\t\t\t\t<h:message/>\r\n" +
				"\t\t\t\t\t\t</h:panelGroup>\r\n" +
				"\t\t\t\t\t\t<h:outputText/>\r\n" +
				"\t\t\t\t\t\t<h:outputLabel value=\"Baz:\"/>\r\n" +
				"\t\t\t\t\t\t<h:panelGroup>\r\n" +
				"\t\t\t\t\t\t\t<h:inputText/>\r\n" +
				"\t\t\t\t\t\t\t<h:message/>\r\n" +
				"\t\t\t\t\t\t</h:panelGroup>\r\n" +
				"\t\t\t\t\t\t<h:outputText/>\r\n" +
				"\t\t\t\t\t</h:panelGrid>";

		assertEquals( result, writer.toString().trim() );
	}

	public void testSimpleType() {

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		metawidget.setValue( "#{foo}" );
		metawidget.setPath( Foo.class.getName() );

		String result = "<h:panelGrid columns=\"3\">\r\n" +
				"\t<h:outputLabel for=\"fooBar\" value=\"Bar:\"/>\r\n" +
				"\t<h:panelGroup>\r\n" +
				"\t\t<h:inputText id=\"fooBar\" value=\"#{foo.bar}\"/>\r\n" +
				"\t\t<h:message for=\"fooBar\"/>\r\n" +
				"\t</h:panelGroup>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel for=\"fooBaz\" value=\"Baz:\"/>\r\n" +
				"\t<h:panelGroup>\r\n" +
				"\t\t<h:inputText id=\"fooBaz\" value=\"#{foo.baz}\"/>\r\n" +
				"\t\t<h:message for=\"fooBaz\"/>\r\n" +
				"\t</h:panelGroup>\r\n" +
				"\t<h:outputText/>\r\n" +
				"</h:panelGrid>\r\n";

		StringWriter writer = new StringWriter();
		metawidget.write( writer, 0 );
		assertEquals( result, writer.toString() );

		Map<String, String> namespaces = metawidget.getNamespaces();
		assertEquals( "http://java.sun.com/jsf/html", namespaces.get( "h" ) );
		assertEquals( 1, namespaces.size() );
	}

	public void testNestedType() {

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		metawidget.setValue( "#{foo}" );
		metawidget.setPath( NestedFoo.class.getName() );

		String result = "<h:panelGrid columns=\"3\">\r\n" +
				"\t<h:outputLabel for=\"fooAbc\" value=\"Abc:\"/>\r\n" +
				"\t<h:panelGroup>\r\n" +
				"\t\t<h:inputText id=\"fooAbc\" value=\"#{foo.abc}\"/>\r\n" +
				"\t\t<h:message for=\"fooAbc\"/>\r\n" +
				"\t</h:panelGroup>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel for=\"fooNestedFoo\" value=\"Nested Foo:\"/>\r\n" +
				"\t<h:panelGrid columns=\"3\" id=\"fooNestedFoo\">\r\n" +
				"\t\t<h:outputLabel for=\"fooNestedFooBar\" value=\"Bar:\"/>\r\n" +
				"\t\t<h:panelGroup>\r\n" +
				"\t\t\t<h:inputText id=\"fooNestedFooBar\" value=\"#{foo.nestedFoo.bar}\"/>\r\n" +
				"\t\t\t<h:message for=\"fooNestedFooBar\"/>\r\n" +
				"\t\t</h:panelGroup>\r\n" +
				"\t\t<h:outputText/>\r\n" +
				"\t\t<h:outputLabel for=\"fooNestedFooBaz\" value=\"Baz:\"/>\r\n" +
				"\t\t<h:panelGroup>\r\n" +
				"\t\t\t<h:inputText id=\"fooNestedFooBaz\" value=\"#{foo.nestedFoo.baz}\"/>\r\n" +
				"\t\t\t<h:message for=\"fooNestedFooBaz\"/>\r\n" +
				"\t\t</h:panelGroup>\r\n" +
				"\t\t<h:outputText/>\r\n" +
				"\t</h:panelGrid>\r\n" +
				"\t<h:outputText/>\r\n" +
				"</h:panelGrid>\r\n";

		StringWriter writer = new StringWriter();
		metawidget.write( writer, 0 );
		assertEquals( result, writer.toString() );

		Map<String, String> namespaces = metawidget.getNamespaces();
		assertEquals( "http://java.sun.com/jsf/html", namespaces.get( "h" ) );
		assertEquals( 1, namespaces.size() );
	}

	public void testOutputTextLayoutDecorator() {

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		metawidget.setValue( "#{foo}" );
		metawidget.setPath( Sections.class.getName() );

		String result = "<h:panelGrid columns=\"3\">\r\n" +
				"\t<h:outputText value=\"Section #1\"/>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel for=\"fooAbc\" value=\"Abc:\"/>\r\n" +
				"\t<h:panelGroup>\r\n" +
				"\t\t<h:inputText id=\"fooAbc\" value=\"#{foo.abc}\"/>\r\n" +
				"\t\t<h:message for=\"fooAbc\"/>\r\n" +
				"\t</h:panelGroup>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputText value=\"Section #2\"/>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel for=\"fooDef\" value=\"Def:\"/>\r\n" +
				"\t<h:panelGroup>\r\n" +
				"\t\t<h:inputText id=\"fooDef\" value=\"#{foo.def}\"/>\r\n" +
				"\t\t<h:message for=\"fooDef\"/>\r\n" +
				"\t</h:panelGroup>\r\n" +
				"\t<h:outputText/>\r\n" +
				"</h:panelGrid>\r\n";

		StringWriter writer = new StringWriter();
		metawidget.write( writer, 0 );
		assertEquals( result, writer.toString() );
	}

	//
	// Inner class
	//

	public static class Foo {

		public String	bar;

		public String	baz;
	}

	public static class NestedFoo {

		public String	abc;

		public Foo		nestedFoo;
	}

	public static class Sections {

		@UiSection( "Section #1" )
		public String	abc;

		@UiSection( { "Section #1", "Section #2" } )
		public String	def;
	}
}
