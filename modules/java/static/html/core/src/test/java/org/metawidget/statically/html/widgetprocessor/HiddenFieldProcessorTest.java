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
