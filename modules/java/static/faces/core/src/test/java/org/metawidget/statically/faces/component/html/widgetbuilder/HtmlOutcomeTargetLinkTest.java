// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

package org.metawidget.statically.faces.component.html.widgetbuilder;

import java.io.StringWriter;
import java.io.Writer;

import junit.framework.TestCase;

import org.metawidget.statically.StaticUtils.IndentedWriter;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class HtmlOutcomeTargetLinkTest
	extends TestCase {

	//
	// Public methods
	//

	public void testHtmlOutcomeTargetLink()
		throws Exception {

		HtmlOutcomeTargetLink widget = new HtmlOutcomeTargetLink();
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
