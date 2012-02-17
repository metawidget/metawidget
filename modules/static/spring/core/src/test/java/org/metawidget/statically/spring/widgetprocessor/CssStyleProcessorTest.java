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

package org.metawidget.statically.spring.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Date;

import junit.framework.TestCase;

import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.spring.StaticSpringMetawidget;
import org.metawidget.statically.spring.widgetbuilder.FormInputTag;

/**
 * JUnit test for the CSS Style WidgetProcessor
 *
 * @author Ryan Bradley
 */

public class CssStyleProcessorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testWidgetProcessor()
		throws Exception {

		CssStyleProcessor processor = new CssStyleProcessor();
		StaticXmlWidget springInput = new FormInputTag();

		// No style

		StaticSpringMetawidget metawidget = new StaticSpringMetawidget();
		springInput = processor.processWidget( springInput, PROPERTY, null, metawidget );
		assertEquals( "<form:input/>", springInput.toString() );

		// Simple styles and classes

		metawidget.putAttribute( "cssStyle", "foo" );
		metawidget.putAttribute( "cssClass", "bar" );
		springInput = processor.processWidget( springInput, PROPERTY, null, metawidget );
		assertEquals( "<form:input cssClass=\"bar\" cssStyle=\"foo\"/>", springInput.toString() );

		// Compound styles and compound classes

		metawidget.putAttribute( "cssStyle", "foo2" );
		metawidget.putAttribute( "cssClass", "bar2" );
		springInput = processor.processWidget( springInput, PROPERTY, null, metawidget );
		assertEquals( "<form:input cssClass=\"bar bar2\" cssStyle=\"foo foo2\"/>", springInput.toString() );
	}

	public void testSimpleType() {

		StaticSpringMetawidget metawidget = new StaticSpringMetawidget();
		metawidget.putAttribute( "cssStyle", "stylin" );
		metawidget.putAttribute( "cssClass", "styleClassin" );
		metawidget.setPath( Foo.class.getName() );

		String result = "<table>" +
				"<tbody><tr><th><form:label path=\"bar\">Bar:</form:label></th><td><form:input cssClass=\"styleClassin\" cssStyle=\"stylin\" path=\"bar\"/></td><td/></tr>" +
				"<tr><th><form:label path=\"baz\">Baz:</form:label></th><td><form:input cssClass=\"styleClassin\" cssStyle=\"stylin\" path=\"baz\"/></td><td/></tr>" +
				"</tbody></table>";

		assertEquals( result, metawidget.toString() );
	}

	//
	// Inner class
	//

	public static class Foo {

		public Date getBar() {

			return null;
		}

		public void setBar( @SuppressWarnings( "unused" ) Date bar ) {

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
