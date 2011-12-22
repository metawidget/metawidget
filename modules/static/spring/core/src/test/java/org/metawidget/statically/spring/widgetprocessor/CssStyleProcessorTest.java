package org.metawidget.statically.spring.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Date;

import org.metawidget.statically.spring.widgetprocessor.CssStyleProcessor;
import org.metawidget.statically.spring.StaticSpringMetawidget;
import org.metawidget.statically.spring.widgetbuilder.FormInputTag;

import junit.framework.TestCase;

/**
 * JUnit test for the CSS Style WidgetProcessor
 * 
 * @author Ryan Bradley
 */

public class CssStyleProcessorTest
    extends TestCase {

    //
    // Public methods
    //
    
    public void testWidgetProcessor() 
        throws Exception {
        
        CssStyleProcessor processor = new CssStyleProcessor();
        FormInputTag springInput = new FormInputTag();
        
        // No style
        
        StaticSpringMetawidget metawidget = new StaticSpringMetawidget();
        processor.processWidget( springInput, PROPERTY, null, metawidget );
        assertEquals( "<form:input/>", springInput.toString() );
        
        // Simple styles and classes
        
        metawidget.putAttribute( "style" , "foo");
        metawidget.putAttribute( "styleClass", "bar");
        processor.processWidget( springInput, PROPERTY, null, metawidget );
        assertEquals( "<form:input style=\"foo\" styleClass=\"bar\"/>", springInput.toString() );
        
        // Compound styles and compound classes
        
        metawidget.putAttribute( "style", "foo2" );
        metawidget.putAttribute( "styleClass", "bar2" );
        processor.processWidget( springInput, PROPERTY, null, metawidget );
        assertEquals( "<form:input style=\"foo foo2\" styleClass=\"bar bar2\"/>", springInput.toString() );
    }
    
    public void testSimpleType() {
        
        StaticSpringMetawidget metawidget = new StaticSpringMetawidget();
        metawidget.putAttribute( "style", "stylin" );
        metawidget.putAttribute( "styleClass", "styleClassin" );
        metawidget.setValue( "#{foo}" );
        metawidget.setPath( Foo.class.getName() );
        
        String actual = metawidget.toString();
        
        String result = "<table>" +
                "<form:input path=\"bar\" style=\"stylin\" styleClass=\"styleClassin\"/>" +
                "<form:input path=\"baz\" style=\"stylin\" styleClass=\"styleClassin\"/>" +
                "</table>";
        
        assertEquals( result, actual );
    }
    
    //
    // Inner class
    //
    
    public static class Foo {
        
        public Date bar;
        
        public int baz;
    }
    
}
