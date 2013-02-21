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

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Date;
import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.inspector.annotation.UiRequired;
import org.metawidget.statically.faces.component.html.StaticHtmlMetawidget;
import org.metawidget.statically.faces.component.html.widgetbuilder.HtmlInputText;
import org.metawidget.util.CollectionUtils;

/**
 * @author Richard Kennard
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
