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

import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.spring.StaticSpringMetawidget;
import org.metawidget.statically.spring.widgetbuilder.FormInputTag;
import org.metawidget.util.CollectionUtils;

/**
 * JUnit test for the Hidden Field Widget Processor
 *
 * @author Ryan Bradley
 */

public class HiddenFieldProcessorTest
    extends TestCase {

    //
    // Public methods
    //

    public void testWidgetProcessor() {

        HiddenFieldProcessor processor = new HiddenFieldProcessor();
        StaticXmlWidget springInput = new FormInputTag();
        StaticSpringMetawidget metawidget = new StaticSpringMetawidget();
        Map<String, String> attributes = CollectionUtils.newHashMap();

        // Not hidden

        attributes.put( HIDDEN, FALSE );
        springInput = processor.processWidget( springInput, PROPERTY, attributes, metawidget );
        assertEquals( "<form:input/>", springInput.toString() );

        // Null value

        attributes.put( NAME, "foo" );
        attributes.put( HIDDEN, TRUE );
        springInput = processor.processWidget( springInput, PROPERTY, attributes, metawidget );
        assertEquals( "<form:hidden path=\"foo\"/>", springInput.toString() );

        // Simple value (i.e. no '.' characters used as separators)

        springInput = new FormInputTag();
        attributes.put( NAME, "foo" );
        metawidget.setValue( "bar" );
        springInput = processor.processWidget( springInput, PROPERTY, attributes, metawidget );
        assertEquals( "<form:hidden path=\"foo\"/>", springInput.toString() );

        // Complex metawidget value (i.e. using '.' separators)

        springInput = new FormInputTag();
        attributes.put( NAME, "spring" );
        metawidget.setValue( "test.org.metawidget.statically" );
        springInput = processor.processWidget( springInput, PROPERTY, attributes, metawidget );
        assertEquals( "<form:hidden path=\"org.metawidget.statically.spring\"/>", springInput.toString() );

        // Value wrapped in a JSP EL

        springInput = new FormInputTag();
        attributes.put(NAME, "spring");
        metawidget.setValue("${spring.static}");
        springInput = processor.processWidget(springInput, PROPERTY, attributes, metawidget);
        assertEquals("<form:hidden path=\"static.spring\"/>", springInput.toString());
    }
}
