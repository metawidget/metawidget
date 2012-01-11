package org.metawidget.statically.spring.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Date;

import org.metawidget.statically.StaticXmlWidget;
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
        StaticXmlWidget springInput = new FormInputTag();
        
        // No style
        
        StaticSpringMetawidget metawidget = new StaticSpringMetawidget();
        springInput = processor.processWidget( springInput, PROPERTY, null, metawidget );
        assertEquals( "<form:input/>", springInput.toString() );
        
        // Simple styles and classes
        
        metawidget.putAttribute( "cssStyle" , "foo");
        metawidget.putAttribute( "cssClass", "bar");
        springInput = processor.processWidget( springInput, PROPERTY, null, metawidget );
        assertEquals( "<form:input cssClass=\"bar\" cssStyle=\"foo\"/>", springInput.toString() );
        
        // Compound styles and compound classes
        
        metawidget.putAttribute( "cssStyle", "foo2" );
        metawidget.putAttribute( "cssClass", "bar2" );
        springInput = processor.processWidget( springInput, PROPERTY, null, metawidget );
        assertEquals( "<form:input cssClass=\"bar bar2\" cssStyle=\"foo foo2\"/>", springInput.toString() );
    }
    
    public void testSimpleType() {
        
        StaticSpringMetawidget metawidget = new StaticSpringMetawidget();
        metawidget.putAttribute( "cssStyle", "stylin" );
        metawidget.putAttribute( "cssClass", "styleClassin" );
        metawidget.setValue( "#{foo}" );
        metawidget.setPath( Foo.class.getName() );
        
        String result = "<table>" +
                "<form:input cssClass=\"styleClassin\" cssStyle=\"stylin\" path=\"bar\"/>" +
                "<form:input cssClass=\"styleClassin\" cssStyle=\"stylin\" path=\"baz\"/>" +
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
