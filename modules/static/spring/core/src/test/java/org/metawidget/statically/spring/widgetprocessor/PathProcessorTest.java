package org.metawidget.statically.spring.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Date;
import java.util.Map;

import org.metawidget.statically.StaticXmlWidget;
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
        StaticXmlWidget springInput = new FormInputTag();
        StaticSpringMetawidget metawidget = new StaticSpringMetawidget();
        Map<String, String> attributes = CollectionUtils.newHashMap();
                
        // Null value
        
        attributes.put( NAME, "bar" );
        springInput = processor.processWidget( springInput, PROPERTY, attributes, metawidget );
        assertEquals( "<form:input path=\"bar\"/>", springInput.toString() );
        
        // Non-null value, no dot separator
        
        metawidget.setValue( "foo" );
        springInput = processor.processWidget( springInput, PROPERTY, attributes, metawidget );
        assertEquals( "<form:input path=\"bar\"/>", springInput.toString() );
        
        // Non-null value, dot separator
        
        metawidget.setValue( "org.foo" );
        springInput = processor.processWidget( springInput, PROPERTY, attributes, metawidget );
        assertEquals( "<form:input path=\"foo.bar\"/>", springInput.toString() );
    }
    
    public void testSimpleType() {
        
        StaticSpringMetawidget metawidget = new StaticSpringMetawidget();
        
        // Name is meaningless because it will take the name of the inner class members.
        metawidget.putAttribute( NAME, "meaningless" );
        metawidget.setValue( "${foo}" );
        metawidget.setPath( Foo.class.getName() );
        
        String result = "<form:form commandName=\"foo\"><table id=\"table-org.metawidget.statically.spring.widgetprocessor.PathProcessorTest$Foo\">" +
        "<tbody><tr><td><form:label path=\"bar\">Bar</form:label></td><td><form:input path=\"bar\"/></td><td>Not Required</td></tr>" +
        "<tr><td><form:label path=\"baz\">Baz</form:label></td><td><form:input path=\"baz\"/></td><td>Not Required</td></tr>" +
        "</tbody></table></form:form>";
        
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
