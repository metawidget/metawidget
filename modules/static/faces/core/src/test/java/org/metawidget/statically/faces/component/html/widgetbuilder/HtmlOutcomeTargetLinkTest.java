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
