// Metawidget (licensed under LGPL)
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

package org.metawidget.statically.faces.component.widgetprocessor;

import junit.framework.TestCase;

import org.metawidget.statically.faces.component.html.widgetbuilder.HtmlInputText;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class ReadableIdProcessorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testWidgetProcessor()
		throws Exception {

		ReadableIdProcessor processor = new ReadableIdProcessor();

		// Null value

		HtmlInputText htmlInputText = new HtmlInputText();
		processor.processWidget( htmlInputText, null, null, null );
		assertEquals( "<h:inputText/>", htmlInputText.toString() );

		// Blank value (should not become id="")

		htmlInputText = new HtmlInputText();
		htmlInputText.putAttribute( "value", "" );
		processor.processWidget( htmlInputText, null, null, null );
		assertEquals( "<h:inputText value=\"\"/>", htmlInputText.toString() );

		// Normal

		htmlInputText = new HtmlInputText();
		htmlInputText.putAttribute( "value", "#{foo.bar}" );
		processor.processWidget( htmlInputText, null, null, null );
		assertEquals( "<h:inputText id=\"fooBar\" value=\"#{foo.bar}\"/>", htmlInputText.toString() );

		// Do not overwrite existing

		htmlInputText.putAttribute( "value", "#{foo.baz}" );
		processor.processWidget( htmlInputText, null, null, null );
		assertEquals( "<h:inputText id=\"fooBar\" value=\"#{foo.baz}\"/>", htmlInputText.toString() );
	}
}
