// Metawidget
//
// For historical reasons, this file is licensed under the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html).
//
// Most other files in Metawidget are licensed under both the
// LGPL/EPL and a commercial license. See http://metawidget.org
// for details.

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
