package org.metawidget.statically.spring.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Date;
import java.util.Map;

import org.metawidget.statically.spring.StaticSpringMetawidget;
import org.metawidget.statically.spring.widgetbuilder.FormInputTag;
import org.metawidget.util.CollectionUtils;

import junit.framework.TestCase;

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
        
        HiddenFieldProcessorDeprecated processor = new HiddenFieldProcessorDeprecated();
        FormInputTag springInput = new FormInputTag();
        StaticSpringMetawidget metawidget = new StaticSpringMetawidget();        
        Map<String, String> attributes = CollectionUtils.newHashMap();
        
        // Not hidden
        
        attributes.put( HiddenFieldProcessorDeprecated.ATTRIBUTE_NEEDS_HIDDEN_FIELD, FALSE );
        processor.processWidget( springInput, PROPERTY, attributes, metawidget );
        assertEquals( "<form:input/>", springInput.toString() );
        
        // Null value
        
        attributes.put( NAME, "foo" );
        attributes.put( HiddenFieldProcessorDeprecated.ATTRIBUTE_NEEDS_HIDDEN_FIELD, TRUE );
        processor.processWidget( springInput, PROPERTY, attributes, metawidget );
        assertEquals( "<form:input><form:hidden path=\"foo\"/></form:input>", springInput.toString() );
        
        // Simple value (i.e. no '.' characters used as separators)
        
        springInput = new FormInputTag();
        attributes.put( NAME, "foo" );
        metawidget.setValue( "bar" );
        processor.processWidget( springInput, PROPERTY, attributes, metawidget );
        assertEquals( "<form:input><form:hidden path=\"foo\"/></form:input>", springInput.toString() );
        
        // Complex metawidget value (i.e. using '.' separators)
        
        springInput = new FormInputTag();
        attributes.put( NAME, "spring" );
        metawidget.setValue( "test.org.metawidget.statically" );
        processor.processWidget( springInput, PROPERTY, attributes, metawidget );
        assertEquals( "<form:input><form:hidden path=\"org.metawidget.statically.spring\"/></form:input>", springInput.toString() );
    }
    
    public void testSimpleType() {
        
        StaticSpringMetawidget metawidget = new StaticSpringMetawidget();
        metawidget.putAttribute( HiddenFieldProcessorDeprecated.ATTRIBUTE_NEEDS_HIDDEN_FIELD, TRUE );
        metawidget.putAttribute( NAME, "bar");
        metawidget.setValue( "org.foo" );
        metawidget.setPath( Foo.class.getName() );
        
        String result = "<table>" +
        "<form:input path=\"foo.bar\"/>" +
//        "<form:hidden path=\"foo.bar\"/>" +
        "<form:input path=\"foo.baz\"/>" +
//        "<form:hidden path=\"foo.baz\"/>" +       
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
