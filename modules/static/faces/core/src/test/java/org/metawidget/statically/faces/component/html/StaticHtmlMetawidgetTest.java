package org.metawidget.statically.faces.component.html;

import java.io.StringWriter;

import junit.framework.TestCase;

public class StaticHtmlMetawidgetTest
	extends TestCase {

	//
	// Public methods
	//

	public void testNullPath() {

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		StringWriter writer = new StringWriter();
		metawidget.write( writer );
		assertTrue( writer.toString().length() == 0 );
	}

	public void testSimpleType() {

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		metawidget.setValueExpression( "#{foo}" );
		metawidget.setPath( Foo.class.getName() );

		StringWriter writer = new StringWriter();
		metawidget.write( writer );
		assertEquals( "<h:inputText id=\"bar\" value=\"#{foo.bar}\"/><h:inputText id=\"baz\" value=\"#{foo.baz}\"/>", writer.toString() );
	}

	public void testNestedType() {

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		metawidget.setValueExpression( "#{nestedFoo}" );
		metawidget.setPath( NestedFoo.class.getName() );

		StringWriter writer = new StringWriter();
		metawidget.write( writer );
		// TODO: assertEquals( "yay", writer.toString() );
	}

	//
	// Inner class
	//

	static class Foo {

		public String bar;

		public String baz;
	}

	static class NestedFoo {

		public String abc;

		public Foo nestedFoo;
	}
}
