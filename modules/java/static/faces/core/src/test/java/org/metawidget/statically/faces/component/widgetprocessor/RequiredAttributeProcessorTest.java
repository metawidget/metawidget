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

import java.util.Date;
import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.inspector.annotation.UiRequired;
import org.metawidget.statically.faces.component.html.StaticHtmlMetawidget;
import org.metawidget.statically.faces.component.html.widgetbuilder.HtmlInputText;
import org.metawidget.util.CollectionUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class RequiredAttributeProcessorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testWidgetProcessor()
		throws Exception {

		RequiredAttributeProcessor processor = new RequiredAttributeProcessor();

		HtmlInputText htmlInputText = new HtmlInputText();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		processor.processWidget( htmlInputText, PROPERTY, attributes, null );
		assertEquals( "<h:inputText/>", htmlInputText.toString() );

		attributes.put( REQUIRED, TRUE );
		processor.processWidget( htmlInputText, PROPERTY, attributes, null );
		assertEquals( "<h:inputText required=\"true\"/>", htmlInputText.toString() );
	}

	public void testSimpleType() {

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		metawidget.setValue( "#{foo}" );
		metawidget.setPath( Foo.class.getName() );

		String result = "<h:panelGrid columns=\"3\">" +
				"<h:outputLabel for=\"fooBar\" value=\"Bar:\"/>" +
				"<h:panelGroup>" +
				"<h:inputText id=\"fooBar\" required=\"true\" value=\"#{foo.bar}\">" +
				"<f:convertDateTime/>" +
				"</h:inputText>" +
				"<h:message for=\"fooBar\"/>" +
				"</h:panelGroup>" +
				"<h:outputText value=\"*\"/>" +
				"<h:outputLabel for=\"fooBaz\" value=\"Baz:\"/>" +
				"<h:panelGroup>" +
				"<h:inputText id=\"fooBaz\" value=\"#{foo.baz}\"/>" +
				"<h:message for=\"fooBaz\"/>" +
				"</h:panelGroup>" +
				"<h:outputText/>" +
				"</h:panelGrid>";

		assertEquals( result, metawidget.toString() );
	}

	//
	// Inner class
	//

	public static class Foo {

		@UiRequired
		public Date getBar() {

			return null;
		}

		public void setBar( @SuppressWarnings( "unused" ) Date date ) {

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
