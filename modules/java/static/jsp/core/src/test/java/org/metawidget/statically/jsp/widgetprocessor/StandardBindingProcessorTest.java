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

package org.metawidget.statically.jsp.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import junit.framework.TestCase;

import org.metawidget.statically.html.widgetbuilder.HtmlInput;
import org.metawidget.statically.jsp.StaticJspMetawidget;
import org.metawidget.util.CollectionUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class StandardBindingProcessorTest
    extends TestCase {

    //
    // Public methods
    //

    public void testWidgetProcessor() {

        StandardBindingProcessor processor = new StandardBindingProcessor();

        HtmlInput input = new HtmlInput();
        Map<String, String> attributes = CollectionUtils.newHashMap();

        // Normal

        attributes.put( NAME, "bar" );
        StaticJspMetawidget metawidget = new StaticJspMetawidget();
        metawidget.setValue( "${foo}" );
        input.putAttribute( "value", "${foo.bar}" );
        processor.processWidget( input, PROPERTY, attributes, metawidget );
        assertEquals( "<input value=\"${foo.bar}\"/>", input.toString() );

        // Do not overwrite existing

        attributes.put( NAME, "baz" );
        processor.processWidget( input, PROPERTY, attributes, metawidget );
        assertEquals( "<input value=\"${foo.bar}\"/>", input.toString() );
    }

}
