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

import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.html.StaticHtmlMetawidget;
import org.metawidget.statically.html.widgetbuilder.HtmlTag;
import org.metawidget.util.CollectionUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class HiddenFieldProcessorTest
    extends TestCase {

    public void testWidgetProcessor() {

        HiddenFieldProcessor processor = new HiddenFieldProcessor();
        StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
        Map<String, String> attributes = CollectionUtils.newHashMap();
        StaticXmlWidget tag = new HtmlTag( "input" );

        // Not hidden

        attributes.put( HIDDEN, FALSE );
        tag = processor.processWidget( tag, PROPERTY, attributes, metawidget );
        assertEquals( "<input/>", tag.toString() );

        // Read only

        attributes.put( READ_ONLY, TRUE );
        tag = processor.processWidget( new HtmlTag( "output" ), PROPERTY, attributes, metawidget );
        assertEquals( "<output><input type=\"hidden\"/></output>", tag.toString() );

        // Hidden

        attributes.put( HIDDEN, TRUE );
        tag = processor.processWidget( tag, PROPERTY, attributes, metawidget );
        assertEquals( "<input type=\"hidden\"/>", tag.toString() );
    }
}
