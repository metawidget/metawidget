package org.metawidget.statically.jsp.html.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.statically.jsp.html.StaticHtmlMetawidget;
import org.metawidget.statically.jsp.html.widgetbuilder.HtmlTag;
import org.metawidget.util.CollectionUtils;

import junit.framework.TestCase;

public class NameProcessorTest
    extends TestCase {
    
    //
    // Public methods
    //
    
    public void testWidgetProcessor() {
        
        StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
        Map<String, String> attributes = CollectionUtils.newHashMap();
        NameProcessor processor = new NameProcessor();
        HtmlTag tag = new HtmlTag( "input" );
        
        // Null metawidget value, no name
        
        processor.processWidget( tag, PROPERTY, attributes, metawidget );
        assertEquals( "<input/>", tag.toString() );
        
        // Null metawidget value, name
        attributes.put( NAME, "foo" );
        processor.processWidget( tag, PROPERTY, attributes, metawidget );
        assertEquals( "<input name=\"foo\"/>", tag.toString() );
        
        // Metawidget value
        metawidget.setValue( "FooBar" );
        processor.processWidget( tag, PROPERTY, attributes, metawidget );
        assertEquals( "<input name=\"fooBarFoo\"/>", tag.toString() );
        
    }
    
    public void testSimpleType() {
        
        StaticHtmlMetawidget metawidget = new StaticHtmlMetawidget();
        metawidget.setValue( "name.processor.example" );
        metawidget.setPath( Foo.class.getName() );
        
        String result = "<table><input name=\"nameProcessorExampleBar\" type=\"text\"/>" +
            "<input name=\"nameProcessorExampleBaz\" type=\"text\"/></table>";
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
