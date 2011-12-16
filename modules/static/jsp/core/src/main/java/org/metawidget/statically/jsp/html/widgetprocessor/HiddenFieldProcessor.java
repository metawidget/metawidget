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
 * <p>
 * Note: passing values via hidden tags is a potential security risk: they can be modified by
 * malicious clients before being returned to the server.
 *
 * @author Ryan Bradley
 */

public class HiddenFieldProcessor implements WidgetProcessor<StaticXmlWidget, StaticHtmlMetawidget> {

    public static final String                          ATTRIBUTE_NEEDS_HIDDEN_FIELD    = "metawidget-needs-hidden-field";    
    
    //
    // Public methods
    //
    
    public StaticXmlWidget processWidget( StaticXmlWidget widget, String elementName, Map<String, String> attributes, StaticHtmlMetawidget metawidget ) {
        
        // Not hidden?
        
        if ( !TRUE.equals( attributes.get( ATTRIBUTE_NEEDS_HIDDEN_FIELD ) ) ) {
            return widget;
        }
        
        String value = widget.toString();
        
        // Add a hidden input as a child of the widget
        
        HtmlTag tag = new HtmlTag("input");
        tag.putAttribute( "type", "hidden" );
        metawidget.getChildren().add( widget );
        
        // Not sure on the implementation of the <span/> tag as a child of metawidget or as a child of widget?
        
        if ( !TRUE.equals( attributes.get( HIDDEN ) ) && "".equals( value ) ) {
            metawidget.getChildren().add( new HtmlTag( "span" ) );
        }
        
        // What exactly should we be returning?  We do not process the passed in widget, but rather the metawidget.
        
        return widget;
    }

}
