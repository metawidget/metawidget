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

import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.statically.faces.component.html.StaticHtmlMetawidget;
import org.metawidget.statically.faces.component.html.widgetbuilder.HtmlInputText;
import org.metawidget.util.CollectionUtils;

/**
 * @author Richard Kennard
 */

public class StandardBindingProcessorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testWidgetProcessor()
		throws Exception {

		StandardBindingProcessor processor = new StandardBindingProcessor();

		// Normal

		HtmlInputText htmlInputText = new HtmlInputText();
		Map<String, String> attributes = CollectionUtils.newHashMap();
		attributes.put( NAME, "bar" );
		StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
		metawidget.setValue( "#{foo}" );
		htmlInputText.putAttribute( "value", "#{foo.bar}" );
		processor.processWidget( htmlInputText, PROPERTY, attributes, metawidget );
		assertEquals( "<h:inputText value=\"#{foo.bar}\"/>", htmlInputText.toString() );

		// Do not overwrite existing

		attributes.put( NAME, "baz" );
		processor.processWidget( htmlInputText, PROPERTY, attributes, metawidget );
		assertEquals( "<h:inputText value=\"#{foo.bar}\"/>", htmlInputText.toString() );

		// Capitalized

		attributes.put( NAME, "Abc" );
		htmlInputText = new HtmlInputText();
		processor.processWidget( htmlInputText, PROPERTY, attributes, metawidget );
		assertEquals( "<h:inputText value=\"#{foo.abc}\"/>", htmlInputText.toString() );

		attributes.put( NAME, "URL" );
		htmlInputText = new HtmlInputText();
		processor.processWidget( htmlInputText, PROPERTY, attributes, metawidget );
		assertEquals( "<h:inputText value=\"#{foo.URL}\"/>", htmlInputText.toString() );
	}
}
