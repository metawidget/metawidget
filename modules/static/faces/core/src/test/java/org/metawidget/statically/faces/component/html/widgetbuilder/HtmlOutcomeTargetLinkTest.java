package org.metawidget.statically.faces.component.html.widgetbuilder;

import java.io.StringWriter;
import java.io.Writer;

import junit.framework.TestCase;

import org.metawidget.statically.StaticUtils.IndentedWriter;

public class HtmlOutcomeTargetLinkTest
	extends TestCase {

	//
	// Public methods
	//

	public void testHtmlOutcomeTargetLink()
		throws Exception {

		// Hide 'value' and 'converter'

		HtmlOutcomeTargetLink widget = new HtmlOutcomeTargetLink();
		widget.setValue( "foo" );
		widget.setConverter( "bar" );
		widget.putAttribute( "baz", "abc" );

		assertEquals( "<h:link baz=\"abc\"/>", widget.toString() );

		// With text content

		widget.setTextContent( "fooBar" );
		assertEquals( "<h:link baz=\"abc\">fooBar</h:link>", widget.toString() );

		// With non-indented text content

		Writer writer = new IndentedWriter( new StringWriter(), 0 );
		widget.write( writer );
		widget.setTextContent( "fooBar" );
		assertEquals( "<h:link baz=\"abc\">fooBar</h:link>\r\n", writer.toString() );

		// With indented text content

		Param param = new Param();
		param.putAttribute( "name", "id" );
		param.putAttribute( "value", "foo" );
		widget.getChildren().add( param );

		writer = new IndentedWriter( new StringWriter(), 0 );
		widget.write( writer );
		widget.setTextContent( "fooBar" );
		assertEquals( "<h:link baz=\"abc\">\r\n\t<f:param name=\"id\" value=\"foo\"/>\r\n\tfooBar\r\n</h:link>\r\n", writer.toString() );
	}
}
