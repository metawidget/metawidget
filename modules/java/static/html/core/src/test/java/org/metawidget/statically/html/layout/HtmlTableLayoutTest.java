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

package org.metawidget.statically.html.layout;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.statically.StaticXmlStub;
import org.metawidget.statically.html.StaticHtmlMetawidget;
import org.metawidget.statically.html.widgetbuilder.HtmlDiv;
import org.metawidget.statically.html.widgetbuilder.HtmlInput;
import org.metawidget.util.CollectionUtils;

public class HtmlTableLayoutTest
	extends TestCase {

	//
	// Public methods
	//

	public void testHtmlTableLayout()
		throws Exception {

		HtmlTableLayoutConfig config = new HtmlTableLayoutConfig();
		config.setTableStyle( "tableStyle" );
		config.setTableStyleClass( "tableStyleClass" );
		config.setLabelColumnStyleClass( "labelColumnStyleClass" );
		config.setComponentColumnStyleClass( "componentColumnStyleClass" );
		config.setRequiredColumnStyleClass( "requiredColumnStyleClass" );
		HtmlTableLayout layout = new HtmlTableLayout( config );

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		HtmlDiv container = new HtmlDiv();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( NAME, "foo" );
		HtmlInput htmlInput = new HtmlInput();
		htmlInput.putAttribute( "id", "foo-bar" );
		layout.startContainerLayout( container, metawidget );
		layout.layoutWidget( htmlInput, PROPERTY, attributes, container, metawidget );
		layout.endContainerLayout( container, metawidget );

		StringBuilder builder = new StringBuilder();
		builder.append( "<div><table class=\"tableStyleClass\" style=\"tableStyle\"><tbody><tr>" );
		builder.append( "<th class=\"labelColumnStyleClass\"><label for=\"foo-bar\">Foo:</label></th>" );
		builder.append( "<td class=\"componentColumnStyleClass\"><input id=\"foo-bar\"/></td>" );
		builder.append( "<td class=\"requiredColumnStyleClass\"/>" );
		builder.append( "</tr></tbody></table></div>" );
		assertEquals( builder.toString(), container.toString() );
	}

	public void testStubs()
		throws Exception {

		HtmlTableLayout layout = new HtmlTableLayout();
		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( NAME, "foo" );

		// Empty stub

		StaticXmlStub stub = new StaticXmlStub();
		HtmlDiv container = new HtmlDiv();
		layout.startContainerLayout( container, metawidget );
		layout.layoutWidget( stub, PROPERTY, attributes, container, metawidget );
		layout.endContainerLayout( container, metawidget );

		assertEquals( "<div><table><tbody/></table></div>", container.toString() );

		// Stub with children

		HtmlInput htmlInput = new HtmlInput();
		htmlInput.putAttribute( "id", "foo-bar" );
		stub.getChildren().add( htmlInput );

		container = new HtmlDiv();
		layout.startContainerLayout( container, metawidget );
		layout.layoutWidget( stub, PROPERTY, attributes, container, metawidget );
		layout.endContainerLayout( container, metawidget );

		StringBuilder builder = new StringBuilder();
		builder.append( "<div><table><tbody><tr>" );
		builder.append( "<th><label for=\"foo-bar\">Foo:</label></th>" );
		builder.append( "<td><input id=\"foo-bar\"/></td>" );
		builder.append( "<td/>" );
		builder.append( "</tr></tbody></table></div>" );
		assertEquals( builder.toString(), container.toString() );
	}
}
