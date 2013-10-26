// Metawidget
//
// This library is dual licensed under both LGPL and a commercial
// license.
//
// LGPL: this library is free software; you can redistribute it and/or
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
//
// Commercial License: See http://metawidget.org for details

package org.metawidget.statically.html.layout;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.statically.StaticXmlStub;
import org.metawidget.statically.html.StaticHtmlMetawidget;
import org.metawidget.statically.html.widgetbuilder.HtmlDiv;
import org.metawidget.statically.html.widgetbuilder.HtmlInput;
import org.metawidget.util.CollectionUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class HtmlDivLayoutTest
	extends TestCase {

	//
	// Public methods
	//

	public void testHtmlDivLayout()
		throws Exception {

		HtmlDivLayout layout = new HtmlDivLayout();

		// Normal

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		HtmlDiv container = new HtmlDiv();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( NAME, "foo" );
		HtmlInput htmlInput = new HtmlInput();
		htmlInput.putAttribute( "id", "foo-bar" );
		layout.layoutWidget( htmlInput, PROPERTY, attributes, container, metawidget );

		assertEquals( "<div><div><label for=\"foo-bar\">Foo:</label><input id=\"foo-bar\"/></div></div>", container.toString() );
	}

	public void testStub()
		throws Exception {

		HtmlDivLayout layout = new HtmlDivLayout();

		// Empty stub

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		HtmlDiv container = new HtmlDiv();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( NAME, "foo" );
		layout.layoutWidget( new StaticXmlStub(), PROPERTY, attributes, container, metawidget );
		layout.layoutWidget( new HtmlInput(), PROPERTY, attributes, container, metawidget );
		layout.layoutWidget( new StaticXmlStub(), PROPERTY, attributes, container, metawidget );

		assertEquals( "<div><div><label>Foo:</label><input/></div></div>", container.toString() );

		// Stub with children

		container = new HtmlDiv();
		StaticXmlStub stub = new StaticXmlStub();
		stub.getChildren().add( new HtmlInput() );
		layout.layoutWidget( stub, PROPERTY, attributes, container, metawidget );

		assertEquals( "<div><div><label>Foo:</label><input/></div></div>", container.toString() );
	}

	public void testNestedId()
		throws Exception {

		HtmlDivLayout layout = new HtmlDivLayout();

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		HtmlDiv container = new HtmlDiv();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( NAME, "foo" );

		HtmlDiv div = new HtmlDiv();
		HtmlInput HtmlInput = new HtmlInput();
		HtmlInput.putAttribute( "id", "fooBeanCurrent" );
		div.getChildren().add( HtmlInput );

		layout.layoutWidget( div, PROPERTY, attributes, container, metawidget );

		assertEquals( "<div><div><label for=\"fooBeanCurrent\">Foo:</label><div><input id=\"fooBeanCurrent\"/></div></div></div>", container.toString() );
	}
}
