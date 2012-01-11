package org.metawidget.statically.spring.widgetprocessor;

import static org.metawidget.inspector.spring.SpringInspectionResultConstants.*;

import java.util.Map;

import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.spring.StaticSpringMetawidget;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

/**
 * WidgetProcessor that adds CSS styles to a StaticXmlWidget, based on the styles of the parent
 * StaticSpringMetawidget.
 *
 * @author Ryan Bradley
 */

public class CssStyleProcessor 
    implements WidgetProcessor<StaticXmlWidget, StaticSpringMetawidget> {
    
    //
    // Public methods
    //
    
    public StaticXmlWidget processWidget(StaticXmlWidget widget, String elementName, Map<String, String> attributes, StaticSpringMetawidget metawidget ) {

        String style = metawidget.getAttribute( SPRING_CSS_STYLE );
        
        if ( style != null ) {
            String existingStyle = widget.getAttribute( SPRING_CSS_STYLE );
            
            if( existingStyle == null || "".equals(existingStyle) ) {
                widget.putAttribute( SPRING_CSS_STYLE, style );
            }
            else {
                widget.putAttribute( SPRING_CSS_STYLE, existingStyle + " " + style );
            }
        }
        
        String styleClass = metawidget.getAttribute( SPRING_CSS_CLASS );
        
        if ( styleClass != null ) {
            String existingStyleClass = widget.getAttribute( SPRING_CSS_CLASS );
            
            if ( existingStyleClass == null || "".equals( existingStyleClass) ) {
                widget.putAttribute( SPRING_CSS_CLASS, styleClass );
            }
            else {
                widget.putAttribute( SPRING_CSS_CLASS, existingStyleClass + " " + styleClass );
            }
        }
        
        return widget;
    }

}
