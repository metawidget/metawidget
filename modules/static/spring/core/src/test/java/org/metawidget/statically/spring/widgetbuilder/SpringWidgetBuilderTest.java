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
    
    public void testLookup() {
        
        SpringWidgetBuilder widgetBuilder = new SpringWidgetBuilder();
        Map<String, String> attributes = CollectionUtils.newHashMap();
        attributes.put( LOOKUP, "${foo.bar}" );
        attributes.put( LOOKUP_LABELS, "foo.bar" );
        
        // Without 'required'
        
        StaticXmlWidget widget = widgetBuilder.buildWidget( PROPERTY, attributes, null );
        assertEquals( "<form:select><form:option/><form:option label=\"foo.bar\" value=\"${foo.bar}\"/></form:select>", widget.toString() );
        
        // With 'required
        
        attributes.put( REQUIRED, TRUE );
        
        widget = widgetBuilder.buildWidget( PROPERTY, attributes, null);
        assertEquals( "<form:select><form:option label=\"foo.bar\" value=\"${foo.bar}\"/></form:select>", widget.toString() );
    }
    
    public void testSpringLookup() {
        
        // Without 'required'
        
        SpringWidgetBuilder widgetBuilder = new SpringWidgetBuilder();
        Map<String, String> attributes = CollectionUtils.newHashMap();
        attributes.put( SPRING_LOOKUP, "${foo.bar}" );
        StaticXmlWidget widget = widgetBuilder.buildWidget( PROPERTY, attributes, null);
        assertEquals( "<form:select><form:option/><form:options items=\"${foo.bar}\"/></form:select>", widget.toString());
        
        // With 'required'
        
        attributes.put( REQUIRED, TRUE );
        widget = widgetBuilder.buildWidget( PROPERTY, attributes, null );
        assertEquals( "<form:select><form:options items=\"${foo.bar}\"/></form:select>", widget.toString() );
    }
    
    public void testCollection() {
        
        // SpringWidgetBuilder returns stubs for all collections at the moment.
        
        SpringWidgetBuilder widgetBuilder = new SpringWidgetBuilder();
        Map<String, String> attributes = CollectionUtils.newHashMap();
        attributes.put( TYPE, Set.class.getName() );
        StaticXmlWidget widget = widgetBuilder.buildWidget( PROPERTY, attributes, null );
        assertNull( widget );
    }

}
