package org.metawidget.statically.faces.component.html;

import java.io.StringWriter;

import junit.framework.TestCase;

import org.metawidget.inspector.annotation.UiSection;

public class StaticHtmlMetawidgetTest
	extends TestCase {

	//
	// Public methods
	//

	public void testNullPath() {

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		StringWriter writer = new StringWriter();
		metawidget.write( writer );
		assertEquals( "<h:panelGrid columns=\"2\">\r\n</h:panelGrid>\r\n", writer.toString() );
	}

	public void testInitialIndent() {

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		StringWriter writer = new StringWriter();
		metawidget.write( writer, 0 );
		assertEquals( "<h:panelGrid columns=\"2\">\r\n</h:panelGrid>\r\n", writer.toString() );

		writer = new StringWriter();
		metawidget.write( writer, 1 );
		assertEquals( "\t<h:panelGrid columns=\"2\">\r\n\t</h:panelGrid>\r\n", writer.toString() );

		writer = new StringWriter();
		metawidget.write( writer, 5 );
		assertEquals( "\t\t\t\t\t<h:panelGrid columns=\"2\">\r\n\t\t\t\t\t</h:panelGrid>\r\n", writer.toString() );

		writer = new StringWriter();
		metawidget.write( writer, 5 );
		assertEquals( "<h:panelGrid columns=\"2\">\r\n\t\t\t\t\t</h:panelGrid>", writer.toString().trim() );
	}

	public void testSimpleType() {

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		metawidget.setValueExpression( "value", "#{foo}" );
		metawidget.setPath( Foo.class.getName() );

		StringWriter writer = new StringWriter();
		metawidget.write( writer );

		String result = "<h:panelGrid columns=\"2\">\r\n" +
				"\t<h:outputLabel for=\"fooBar\" value=\"Bar:\"/>\r\n" +
				"\t<h:inputText id=\"fooBar\" value=\"#{foo.bar}\"/>\r\n" +
				"\t<h:outputLabel for=\"fooBaz\" value=\"Baz:\"/>\r\n" +
				"\t<h:inputText id=\"fooBaz\" value=\"#{foo.baz}\"/>\r\n" +
				"</h:panelGrid>\r\n";

		assertEquals( result, writer.toString() );
	}

	public void testNestedType() {

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		metawidget.setValueExpression( "value", "#{foo}" );
		metawidget.setPath( NestedFoo.class.getName() );

		StringWriter writer = new StringWriter();
		metawidget.write( writer );

		String result = "<h:panelGrid columns=\"2\">\r\n";
		result += "\t<h:outputLabel for=\"fooAbc\" value=\"Abc:\"/>\r\n";
		result += "\t<h:inputText id=\"fooAbc\" value=\"#{foo.abc}\"/>\r\n";
		result += "\t<h:outputLabel for=\"fooNestedFoo\" value=\"Nested foo:\"/>\r\n";
		result += "\t<h:panelGrid columns=\"2\" id=\"fooNestedFoo\">\r\n";
		result += "\t\t<h:outputLabel for=\"fooNestedFooBar\" value=\"Bar:\"/>\r\n";
		result += "\t\t<h:inputText id=\"fooNestedFooBar\" value=\"#{foo.nestedFoo.bar}\"/>\r\n";
		result += "\t\t<h:outputLabel for=\"fooNestedFooBaz\" value=\"Baz:\"/>\r\n";
		result += "\t\t<h:inputText id=\"fooNestedFooBaz\" value=\"#{foo.nestedFoo.baz}\"/>\r\n";
		result += "\t</h:panelGrid>\r\n";
		result += "</h:panelGrid>\r\n";

		assertEquals( result, writer.toString() );
	}

	public void testOutputTextLayoutDecorator() {

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		metawidget.setValueExpression( "value", "#{foo}" );
		metawidget.setPath( Sections.class.getName() );

		StringWriter writer = new StringWriter();
		metawidget.write( writer );

		String result = "<h:panelGrid columns=\"2\">\r\n" +
				"\t<h:outputText value=\"Section #1\"/>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel for=\"fooAbc\" value=\"Abc:\"/>\r\n" +
				"\t<h:inputText id=\"fooAbc\" value=\"#{foo.abc}\"/>\r\n" +
				"\t<h:outputText value=\"Section #2\"/>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel for=\"fooDef\" value=\"Def:\"/>\r\n" +
				"\t<h:inputText id=\"fooDef\" value=\"#{foo.def}\"/>\r\n" +
				"</h:panelGrid>\r\n";

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
