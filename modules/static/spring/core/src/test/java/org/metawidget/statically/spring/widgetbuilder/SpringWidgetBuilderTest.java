package org.metawidget.statically.spring.widgetbuilder;


import static org.metawidget.inspector.InspectionResultConstants.*;
import static org.metawidget.inspector.spring.SpringInspectionResultConstants.*;

import java.util.Map;
import java.util.Set;

import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.spring.widgetbuilder.SpringWidgetBuilder;
import org.metawidget.util.CollectionUtils;

import junit.framework.TestCase;

/**
 * JUnit test for the SpringWidgetBuilder, a WidgetBuilder for the StaticSpringMetawidget
 * 
 * @author Ryan Bradley
 */

public class SpringWidgetBuilderTest
    extends TestCase {
    
    //
    // Public methods
    //
    
    public void testSpringLookup()
        throws Exception {
        
        // Without 'required'
        
        SpringWidgetBuilder widgetBuilder = new SpringWidgetBuilder();
        Map<String, String> attributes = CollectionUtils.newHashMap();
        attributes.put( SPRING_LOOKUP, "#{foo.bar}" );
        StaticXmlWidget widget = widgetBuilder.buildWidget( PROPERTY, attributes, null);
        assertEquals( "<form:select><form:option/><form:options items=\"#{foo.bar}\"/></form:select>", widget.toString());
        
        // With 'required'
        
        attributes.put( REQUIRED, TRUE );
        widget = widgetBuilder.buildWidget( PROPERTY, attributes, null );
        assertEquals( "<form:select><form:options items=\"#{foo.bar}\"/></form:select>", widget.toString() );
    }
    
    public void testCollection() 
        throws Exception {
        
        // All Collections unsupported by SpringWidgetBuilder as of now
        
        SpringWidgetBuilder widgetBuilder = new SpringWidgetBuilder();
        Map<String, String> attributes = CollectionUtils.newHashMap();
        attributes.put( TYPE, Set.class.getName() );
        StaticXmlWidget widget = widgetBuilder.buildWidget( PROPERTY, attributes, null);
        assertEquals( "<stub/>", widget.toString() );
    }
}
