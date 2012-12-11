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
import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.spring.StaticSpringMetawidget;
import org.metawidget.statically.spring.widgetbuilder.FormInputTag;
import org.metawidget.util.CollectionUtils;

/**
 * JUnit test for the Path WidgetProcessor
 *
 * @author Ryan Bradley
 */

public class PathProcessorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testWidgetProcessor() {

		PathProcessor processor = new PathProcessor();
		StaticXmlWidget springInput = new FormInputTag();
		StaticSpringMetawidget metawidget = new StaticSpringMetawidget();
		Map<String, String> attributes = CollectionUtils.newHashMap();

		// Null value

		attributes.put( NAME, "bar" );
		springInput = processor.processWidget( springInput, PROPERTY, attributes, metawidget );
		assertEquals( "<form:input path=\"bar\"/>", springInput.toString() );

		// Non-null value, no dot separator

		metawidget.setValue( "foo" );
		springInput = processor.processWidget( springInput, PROPERTY, attributes, metawidget );
		assertEquals( "<form:input path=\"bar\"/>", springInput.toString() );

		// Non-null value, dot separator

		metawidget.setValue( "org.foo" );
		springInput = processor.processWidget( springInput, PROPERTY, attributes, metawidget );
		assertEquals( "<form:input path=\"foo.bar\"/>", springInput.toString() );
	}

	public void testSimpleType() {

		StaticSpringMetawidget metawidget = new StaticSpringMetawidget();

		// Name is meaningless because it will take the name of the inner class members.
		metawidget.putAttribute( NAME, "meaningless" );
		metawidget.setValue( "${foo}" );
		metawidget.setPath( Foo.class.getName() );

		String result = "<table>" +
				"<tbody><tr><th><form:label path=\"bar\">Bar:</form:label></th><td><form:input path=\"bar\"/></td><td/></tr>" +
				"<tr><th><form:label path=\"baz\">Baz:</form:label></th><td><form:input path=\"baz\"/></td><td/></tr>" +
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
