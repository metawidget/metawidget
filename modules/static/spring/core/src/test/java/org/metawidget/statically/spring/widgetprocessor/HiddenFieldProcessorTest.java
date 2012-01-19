package org.metawidget.statically.spring.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.statically.StaticXmlWidget;
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
        
        HiddenFieldProcessor processor = new HiddenFieldProcessor();
        StaticXmlWidget springInput = new FormInputTag();
        StaticSpringMetawidget metawidget = new StaticSpringMetawidget();        
        Map<String, String> attributes = CollectionUtils.newHashMap();
        
        // Not hidden
        
        attributes.put( HIDDEN, FALSE );
        springInput = processor.processWidget( springInput, PROPERTY, attributes, metawidget );
        assertEquals( "<form:input/>", springInput.toString() );
        
        // Null value
        
        attributes.put( NAME, "foo" );
        attributes.put( HIDDEN, TRUE );
        springInput = processor.processWidget( springInput, PROPERTY, attributes, metawidget );
        assertEquals( "<form:hidden path=\"foo\"/>", springInput.toString() );
        
        // Simple value (i.e. no '.' characters used as separators)
        
        springInput = new FormInputTag();
        attributes.put( NAME, "foo" );
        metawidget.setValue( "bar" );
        springInput = processor.processWidget( springInput, PROPERTY, attributes, metawidget );
        assertEquals( "<form:hidden path=\"foo\"/>", springInput.toString() );
        
        // Complex metawidget value (i.e. using '.' separators)
        
        springInput = new FormInputTag();
        attributes.put( NAME, "spring" );
        metawidget.setValue( "test.org.metawidget.statically" );
        springInput = processor.processWidget( springInput, PROPERTY, attributes, metawidget );
        assertEquals( "<form:hidden path=\"org.metawidget.statically.spring\"/>", springInput.toString() );
    }
    
    // We do not test a simple type as Metawidget does not support hiding the entire Metawidget, only specific members.
    
}
