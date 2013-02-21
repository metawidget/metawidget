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

package org.metawidget.statically.faces.component.html.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Date;

import junit.framework.TestCase;

import org.metawidget.statically.faces.component.html.StaticHtmlMetawidget;
import org.metawidget.statically.faces.component.html.widgetbuilder.HtmlInputText;

/**
 * @author Richard Kennard
 */

public class CssStyleProcessorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testWidgetProcessor()
		throws Exception {

		CssStyleProcessor processor = new CssStyleProcessor();
		HtmlInputText htmlInputText = new HtmlInputText();

		// No style

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		processor.processWidget( htmlInputText, PROPERTY, null, metawidget );
		assertEquals( "<h:inputText/>", htmlInputText.toString() );

		// Simple styles and styleClasses

		metawidget.setStyle( "foo" );
		metawidget.setStyleClass( "bar" );
		processor.processWidget( htmlInputText, PROPERTY, null, metawidget );
		assertEquals( "<h:inputText style=\"foo\" styleClass=\"bar\"/>", htmlInputText.toString() );

		// Compound styles and styleClasses

		metawidget.setStyle( "foo2" );
		metawidget.setStyleClass( "bar2" );
		processor.processWidget( htmlInputText, PROPERTY, null, metawidget );
		assertEquals( "<h:inputText style=\"foo foo2\" styleClass=\"bar bar2\"/>", htmlInputText.toString() );

	}

	public void testSimpleType() {

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		metawidget.setStyle( "stylin" );
		metawidget.setStyleClass( "styleClassin" );
		metawidget.setValue( "#{foo}" );
		metawidget.setPath( Foo.class.getName() );

		String result = "<h:panelGrid columns=\"3\">" +
				"<h:outputLabel for=\"fooBar\" value=\"Bar:\"/>" +
				"<h:panelGroup>" +
				"<h:inputText id=\"fooBar\" style=\"stylin\" styleClass=\"styleClassin\" value=\"#{foo.bar}\">" +
				"<f:convertDateTime/>" +
				"</h:inputText>" +
				"<h:message for=\"fooBar\"/>" +
				"</h:panelGroup>" +
				"<h:outputText/>" +
				"<h:outputLabel for=\"fooBaz\" value=\"Baz:\"/>" +
				"<h:panelGroup>" +
				"<h:inputText id=\"fooBaz\" style=\"stylin\" styleClass=\"styleClassin\" value=\"#{foo.baz}\"/>" +
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
