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
        HtmlTag tag = new HtmlTag( "input" );
        
        // Not hidden

        attributes.put( HIDDEN, FALSE );        
        tag = (HtmlTag) processor.processWidget( tag, PROPERTY, attributes, metawidget );
        assertEquals( "<input/>", tag.toString() );
        
        // Hidden
        
        attributes.put( HIDDEN, TRUE );
        tag = (HtmlTag) processor.processWidget( tag, PROPERTY, attributes, metawidget );
        assertEquals( "<input type=\"hidden\"/>", tag.toString() );
    }
    
    public void testSimpleType() {
        
        StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
        metawidget.setPath( Foo.class.getName() );
        metawidget.setValue( "${foo}" );
        metawidget.putAttribute( HIDDEN, TRUE );
        
        String result = "<table><input name=\"fooBar\" type=\"hidden\"/><input name=\"fooBaz\" type=\"hidden\"/></table>";
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
