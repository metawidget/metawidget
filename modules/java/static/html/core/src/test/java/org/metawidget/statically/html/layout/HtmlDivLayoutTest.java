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
