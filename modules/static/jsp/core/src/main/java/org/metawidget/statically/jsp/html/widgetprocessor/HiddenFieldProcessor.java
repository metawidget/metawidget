package org.metawidget.statically.jsp.html.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.jsp.html.StaticHtmlMetawidget;
import org.metawidget.statically.jsp.html.widgetbuilder.HtmlTag;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

/**
 * WidgetProcessor that adds HTML <code>&lt;input type="hidden"&gt;</code> tags to hidden and
 * read-only values, so that they POST back.
 *
 * @author Ryan Bradley
 */

public class HiddenFieldProcessor implements WidgetProcessor<StaticXmlWidget, StaticHtmlMetawidget> {
    
    //
    // Public methods
    //
    
    public StaticXmlWidget processWidget( StaticXmlWidget widget, String elementName, Map<String, String> attributes, StaticHtmlMetawidget metawidget ) {
        
        // Not hidden?
        
        if ( !TRUE.equals( attributes.get( HIDDEN ) ) ) {
            return widget;
        }
        
        String value = widget.toString();
        
        // Add a hidden input as a child of the metawidget
        
        HtmlTag tag = new HtmlTag("input");
        tag.putAttribute( "type", "hidden" );
        metawidget.getChildren().add( widget );
        
        // Not sure on the implementation of the <span/> tag as a child of metawidget or as a child of widget?
        
        if ( !TRUE.equals( attributes.get( HIDDEN ) ) && "".equals( value ) ) {
            metawidget.getChildren().add( new HtmlTag( "span" ) );
        }
        
        // Return the hidden input HtmlTag which has just been created
        
        return tag;
    }

}
