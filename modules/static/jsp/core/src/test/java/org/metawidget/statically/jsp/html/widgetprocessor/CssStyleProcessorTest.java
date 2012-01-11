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

/**
 * @author Ryan Bradley
 */

public class CssStyleProcessorTest
    extends TestCase {
    
    //
    // Public methods
    //
    
    public void testWidgetProcessor() {
        
        CssStyleProcessor processor = new CssStyleProcessor();
        Map<String, String> attributes = CollectionUtils.newHashMap();
        StaticXmlWidget tag = new HtmlTag( "input" );
        
        // No style
        
        StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
        tag = processor.processWidget( tag, PROPERTY, attributes, metawidget );
        assertEquals( "<input/>", tag.toString() );
        
        // Simple styles and classes
        
        metawidget.putAttribute( "style", "foo" );
        metawidget.putAttribute( "styleClass", "bar" );
        tag = processor.processWidget( tag, PROPERTY, attributes, metawidget );
        assertEquals( "<input style=\"foo\" styleClass=\"bar\"/>", tag.toString() );
        
        // Compound styles and classes
        
        metawidget.putAttribute( "style", "foo2" );
        metawidget.putAttribute( "styleClass", "bar2" );
        tag = processor.processWidget( tag, PROPERTY, attributes, metawidget );
        assertEquals( "<input style=\"foo foo2\" styleClass=\"bar bar2\"/>", tag.toString() );
    }
    
    public void testSimpleType() {
        
        StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
        metawidget.putAttribute( "style", "stylin" );
        metawidget.putAttribute( "styleClass", "styleClassin" );
        metawidget.setValue( "${foo}" );
        metawidget.setPath( Foo.class.getName() );
        
        String result = "<table>" +
            "<input name=\"fooBar\" style=\"stylin\" styleClass=\"styleClassin\" type=\"text\" value=\"${foo.bar}\"/>" +
            "<input name=\"fooBaz\" style=\"stylin\" styleClass=\"styleClassin\" type=\"text\" value=\"${foo.baz}\"/>" +
            "</table>";
        assertEquals( result, metawidget.toString() );
        
    }
    
    //
    // Inner class
    //
    
    public static class Foo {
        
        public String bar;
        
        public int baz;
    }

}
