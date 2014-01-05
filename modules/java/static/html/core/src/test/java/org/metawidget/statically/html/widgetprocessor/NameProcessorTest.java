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

package org.metawidget.statically.html.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.statically.html.StaticHtmlMetawidget;
import org.metawidget.statically.html.widgetbuilder.HtmlInput;
import org.metawidget.util.CollectionUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class NameProcessorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testWidgetProcessor() {

		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		NameProcessor processor = new NameProcessor();
		HtmlInput tag = new HtmlInput();

		// Null metawidget value, no name

		processor.processWidget( tag, PROPERTY, attributes, metawidget );
		assertEquals( "<input/>", tag.toString() );

		// Null metawidget value, name
		attributes.put( NAME, "foo" );
		processor.processWidget( tag, PROPERTY, attributes, metawidget );
		assertEquals( "<input name=\"foo\"/>", tag.toString() );

		// Metawidget value
		metawidget.setId( "FooBar" );
		processor.processWidget( tag, PROPERTY, attributes, metawidget );
		assertEquals( "<input name=\"fooBarFoo\"/>", tag.toString() );
	}
}
