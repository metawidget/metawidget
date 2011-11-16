package org.metawidget.statically.faces.component.html.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.faces.StaticFacesInspectionResultConstants.*;

import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.statically.StaticWidget;
import org.metawidget.statically.faces.component.html.StaticHtmlMetawidget;
import org.metawidget.util.CollectionUtils;

public class ReadOnlyWidgetBuilderTest
	extends TestCase {

	//
	// Public methods
	//

	public void testReadOnlyWidgetBuilder() {

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		metawidget.setReadOnly( true );
		metawidget.setValueExpression( "value", "#{foo}" );
		metawidget.setPath( NestedFoo.class.getName() );

		String result = "<h:panelGrid columns=\"3\">\r\n" +
				"\t<h:outputLabel for=\"fooAbc\" value=\"Abc:\"/>\r\n" +
				"\t<h:outputText id=\"fooAbc\" value=\"#{foo.abc}\"/>\r\n" +
				"\t<h:outputText/>\r\n" +
				"\t<h:outputLabel for=\"fooNestedFoo\" value=\"Nested foo:\"/>\r\n" +
				"\t<h:panelGrid columns=\"3\" id=\"fooNestedFoo\">\r\n" +
				"\t\t<h:outputLabel for=\"fooNestedFooBar\" value=\"Bar:\"/>\r\n" +
				"\t\t<h:outputText id=\"fooNestedFooBar\" value=\"#{foo.nestedFoo.bar}\"/>\r\n" +
				"\t\t<h:outputText/>\r\n" +
				"\t\t<h:outputLabel for=\"fooNestedFooBaz\" value=\"Baz:\"/>\r\n" +
				"\t\t<h:outputText id=\"fooNestedFooBaz\" value=\"#{foo.nestedFoo.baz}\"/>\r\n" +
				"\t\t<h:outputText/>\r\n" +
				"\t</h:panelGrid>\r\n" +
				"\t<h:outputText/>\r\n" +
				"</h:panelGrid>\r\n";

		assertEquals( result, metawidget.toString() );
	}

	public void testReadOnlyFacesLookup()
		throws Exception {

		ReadOnlyWidgetBuilder widgetBuilder = new ReadOnlyWidgetBuilder();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( READ_ONLY, TRUE );
		StaticWidget widget = widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( "<h:outputText/>", widget.toString() );

		attributes.put( FACES_LOOKUP, "#{foo}" );
		widget = widgetBuilder.buildWidget( PROPERTY, attributes, null );
		assertEquals( "<h:outputText/>", widget.toString() );
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
}
