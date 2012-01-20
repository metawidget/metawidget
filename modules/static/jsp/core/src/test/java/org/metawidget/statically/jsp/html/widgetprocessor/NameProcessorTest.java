package org.metawidget.statically.jsp.html.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Date;
import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.statically.jsp.html.StaticHtmlMetawidget;
import org.metawidget.statically.jsp.html.widgetbuilder.HtmlInput;
import org.metawidget.util.CollectionUtils;

public class NameProcessorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testWidgetProcessor() {

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		NameProcessor processor = new NameProcessor();
		HtmlInput tag = new HtmlInput();

		// Null metawidget value, no name

		processor.processWidget( tag, PROPERTY, attributes, metawidget );
		assertEquals( "<input/>", tag.toString() );

		// Null metawidget value, name
		attributes.put( NAME, "foo" );
		processor.processWidget( tag, PROPERTY, attributes, metawidget );
		assertEquals( "<input name=\"foo\"/>", tag.toString() );

		// Metawidget value
		metawidget.setValue( "FooBar" );
		processor.processWidget( tag, PROPERTY, attributes, metawidget );
		assertEquals( "<input name=\"fooBarFoo\"/>", tag.toString() );

	}

	public void testSimpleType() {

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		metawidget.setValue( "name.processor.example" );
		metawidget.setPath( Foo.class.getName() );

		String result = "<table id=\"table-orgMetawidgetStaticallyJspHtmlWidgetprocessorNameProcessorTestFoo\"><tbody><tr><th>" +
				"<label>Bar</label></th><td><input name=\"nameProcessorExampleBar\" type=\"text\" value=\"${name.processor.example.bar}\"/></td>" +
				"<td/>" +
				"</tr><tr>" +
				"<th><label>Baz</label></th>" +
				"<td><input name=\"nameProcessorExampleBaz\" type=\"text\" value=\"${name.processor.example.baz}\"/></td>" +
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
