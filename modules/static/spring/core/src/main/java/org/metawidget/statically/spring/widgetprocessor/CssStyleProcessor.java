package org.metawidget.statically.spring.widgetprocessor;

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

        String cssStyle = metawidget.getAttribute( "cssStyle" );
        
        if ( cssStyle != null ) {
            String existingCssStyle = widget.getAttribute( "cssStyle" );
            
            if( existingCssStyle == null || "".equals(existingCssStyle) ) {
                widget.putAttribute( "cssStyle", cssStyle );
            }
            else {
                widget.putAttribute( "cssStyle", existingCssStyle + " " + cssStyle );
            }
        }
        
        String cssClass = metawidget.getAttribute( "cssClass" );
        
        if ( cssClass != null ) {
            String existingCssClass = widget.getAttribute( "cssClass" );
            
            if ( existingCssClass == null || "".equals( existingCssClass) ) {
                widget.putAttribute( "cssClass", cssClass );
            }
            else {
                widget.putAttribute( "cssClass", existingCssClass + " " + cssClass );
            }
        }
        
        return widget;
    }

}
