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
