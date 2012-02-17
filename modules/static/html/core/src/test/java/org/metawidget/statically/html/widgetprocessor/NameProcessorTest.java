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

package org.metawidget.statically.html.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.statically.html.StaticHtmlMetawidget;
import org.metawidget.statically.html.widgetbuilder.HtmlInput;
import org.metawidget.util.CollectionUtils;

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
