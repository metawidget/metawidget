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

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.statically.faces.component.html.StaticHtmlMetawidget;
import org.metawidget.statically.faces.component.html.widgetbuilder.HtmlInputText;
import org.metawidget.util.CollectionUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class StandardBindingProcessorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testWidgetProcessor()
		throws Exception {

		StandardBindingProcessor processor = new StandardBindingProcessor();

		// Normal

		HtmlInputText htmlInputText = new HtmlInputText();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( NAME, "bar" );
		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		metawidget.setValue( "#{foo}" );
		htmlInputText.putAttribute( "value", "#{foo.bar}" );
		processor.processWidget( htmlInputText, PROPERTY, attributes, metawidget );
		assertEquals( "<h:inputText value=\"#{foo.bar}\"/>", htmlInputText.toString() );

		// Do not overwrite existing

		attributes.put( NAME, "baz" );
		processor.processWidget( htmlInputText, PROPERTY, attributes, metawidget );
		assertEquals( "<h:inputText value=\"#{foo.bar}\"/>", htmlInputText.toString() );

		// Capitalized

		attributes.put( NAME, "Abc" );
		htmlInputText = new HtmlInputText();
		processor.processWidget( htmlInputText, PROPERTY, attributes, metawidget );
		assertEquals( "<h:inputText value=\"#{foo.abc}\"/>", htmlInputText.toString() );

		attributes.put( NAME, "URL" );
		htmlInputText = new HtmlInputText();
		processor.processWidget( htmlInputText, PROPERTY, attributes, metawidget );
		assertEquals( "<h:inputText value=\"#{foo.URL}\"/>", htmlInputText.toString() );
	}
}
