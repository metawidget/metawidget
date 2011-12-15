package org.metawidget.statically.jsp.html.widgetprocessor;

import java.util.Map;

import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.jsp.html.StaticHtmlMetawidget;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

/**
 * WidgetProcessor that adds CSS styles to a StaticXmlWidget, based on the styles of the parent
 * StaticHtmlMetawidget.
 *
 * @author Ryan Bradley
 */

public class CssStyleProcessor 
    implements WidgetProcessor<StaticXmlWidget, StaticHtmlMetawidget> {

    public StaticXmlWidget processWidget( StaticXmlWidget widget, String elementName, Map<String, String> attributes, StaticHtmlMetawidget metawidget ) {

        String style = metawidget.getAttribute( "style" );
        
        if ( style != null ) {
            String existingStyle = widget.getAttribute( "style" );
            
            if ( existingStyle == null || "".equals( existingStyle ) ) {
                widget.putAttribute( "style" , style );
            }
            else {
                widget.putAttribute( "style" , existingStyle + " " + style );
            }
        }
        
        String styleClass = metawidget.getAttribute( "styleClass" );
        
        if ( styleClass != null ) {
            String existingStyleClass = widget.getAttribute( "styleClass" );
            
            if ( existingStyleClass == null || "".equals( existingStyleClass ) ) {
                widget.putAttribute( "styleClass" , styleClass );
            }
            else {
                widget.putAttribute( "styleClass" , existingStyleClass + " " + styleClass );
            }
        }
        
        return widget;
    }

}
