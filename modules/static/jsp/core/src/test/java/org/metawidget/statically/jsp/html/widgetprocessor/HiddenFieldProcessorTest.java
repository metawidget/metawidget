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

package org.metawidget.statically.jsp.html.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.jsp.html.StaticHtmlMetawidget;
import org.metawidget.statically.jsp.html.widgetbuilder.HtmlTag;
import org.metawidget.util.CollectionUtils;

import junit.framework.TestCase;

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
        
        // Hidden
        
        attributes.put( HIDDEN, TRUE );
        tag = processor.processWidget( tag, PROPERTY, attributes, metawidget );
        assertEquals( "<input type=\"hidden\"/>", tag.toString() );
    }
    
    // We do not support hiding the entire Metawidget, only certain members of the Metawidget, so we don't test a simple type.
    
}
