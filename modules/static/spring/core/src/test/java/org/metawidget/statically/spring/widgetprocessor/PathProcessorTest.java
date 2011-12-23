package org.metawidget.statically.spring.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Date;
import java.util.Map;

import org.metawidget.statically.spring.StaticSpringMetawidget;
import org.metawidget.statically.spring.widgetbuilder.FormInputTag;
import org.metawidget.util.CollectionUtils;

import junit.framework.TestCase;

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
        FormInputTag springInput = new FormInputTag();
        StaticSpringMetawidget metawidget = new StaticSpringMetawidget();
        Map<String, String> attributes = CollectionUtils.newHashMap();
                
        // Null value
        
        attributes.put( NAME, "bar" );
        processor.processWidget( springInput, PROPERTY, attributes, metawidget );
        assertEquals( "<form:input path=\"bar\"/>", springInput.toString() );
        
        // Non-null value, no dot separator
        
        metawidget.setValue( "foo" );
        processor.processWidget( springInput, PROPERTY, attributes, metawidget );
        assertEquals( "<form:input path=\"bar\"/>", springInput.toString() );
        
        // Non-null value, dot separator
        
        metawidget.setValue( "org.foo" );
        processor.processWidget( springInput, PROPERTY, attributes, metawidget );
        assertEquals( "<form:input path=\"foo.bar\"/>", springInput.toString() );
    }
    
    public void testSimpleType() {
        
        StaticSpringMetawidget metawidget = new StaticSpringMetawidget();
        
        // Name is meaningless because it will take the name of the inner class members.
        metawidget.putAttribute( NAME, "meaningless" );
        metawidget.setValue( "org.foo" );
        metawidget.setPath( Foo.class.getName() );
        
        String result = "<table>" +
        "<form:input path=\"foo.bar\"/>" +
        "<form:input path=\"foo.baz\"/>" +
        "</table>";
        
        assertEquals( result, metawidget.toString() );
    }
    
    //
    // Inner class
    //
    
    public static class Foo {
        
        public Date bar;
        
        public int baz;
    }
}
