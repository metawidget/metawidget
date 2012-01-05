package org.metawidget.statically.spring.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.statically.StaticWidget;
import org.metawidget.statically.StaticXmlStub;
import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.spring.StaticSpringMetawidget;
import org.metawidget.statically.spring.widgetbuilder.FormHiddenTag;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

/**
 * WidgetProcessor that sets <code>HiddenTag.setDisabled( false )</code> on all
 * <code>HiddenTag</code>s, so that they POST back.
 * <p>
 * Note: passing values via hidden tags is a potential security risk: they can be modified by
 * malicious clients before being returned to the server.
 *
 * @author Ryan Bradley
 */

public class HiddenFieldProcessor implements WidgetProcessor<StaticXmlWidget, StaticSpringMetawidget> {

    //
    // Public methods
    //
    
    public StaticXmlWidget processWidget( StaticXmlWidget widget, String elementName, Map<String, String> attributes, StaticSpringMetawidget metawidget ) {
        
        // Not hidden?
        
        if ( !TRUE.equals( attributes.get( HIDDEN ) ) ) {
            return widget;
        }
        
        for( StaticWidget child : widget.getChildren() ) {
            
            ( (StaticXmlWidget) child).putAttribute( HIDDEN, TRUE );
        }
               
        String name = attributes.get( NAME );
        
        String value = metawidget.getValue();
        
        if ( value != null ) {

            // Take the LHS minus the first path (if any), as we assume that will
            // be supplied by the form            
            
            int firstIndexOf = value.indexOf( StringUtils.SEPARATOR_DOT_CHAR );
            
            if ( firstIndexOf != -1 ) {
                name = value.substring( firstIndexOf + 1 ) + StringUtils.SEPARATOR_DOT + name;
            }
        }
        
        widget = new FormHiddenTag();
        widget.putAttribute( "path", name );

        if ( !TRUE.equals( attributes.get( HIDDEN ) ) && "".equals( attributes.get( HIDDEN ) ) ) {
            
            // Add a child stub to the widget.
                       
            widget = new StaticXmlStub();
            metawidget.getChildren().add( widget );
            return widget;
            
            // Or should it be an HTML <span/> tag?
//          metawidget.getChildren().add( new HtmlTag( "span" );
        }        
        
        return widget;
    }

}
