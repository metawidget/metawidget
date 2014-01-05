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
