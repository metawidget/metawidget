package org.metwaidget.statically.spring.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Date;

import org.junit.Ignore;
import org.metawidget.statically.jsp.html.StaticHtmlMetawidgetTest.Foo;
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
    
    public void testWidgetBuilder() 
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
    
    // Need to look at this test some more in org.metawidget.statically.faces.component.html.widgetprocessor.CssStyleProcessorTest.java
    
    @Ignore
    public void testSimpleType() {
        
        StaticSpringMetawidget metawidget = new StaticSpringMetawidget();
        metawidget.putAttribute( "style", "stylin" );
        metawidget.putAttribute( "styleClass", "styleClassin" );
        metawidget.setValue( "#{foo}" );
        metawidget.setPath( Foo.class.getName() );
        
        String result = "<table>\r\n" +
                "\t<form:input path=\"bar\"/>";
        
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
