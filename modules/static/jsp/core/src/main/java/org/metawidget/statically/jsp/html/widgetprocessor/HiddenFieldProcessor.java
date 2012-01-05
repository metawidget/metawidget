// Metawidget
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

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
        
        metawidget.getChildren().remove( widget );
        widget = new HtmlTag("input");
        widget.putAttribute( "type", "hidden" );
        metawidget.getChildren().add( widget );
        
        if ( !TRUE.equals( attributes.get( HIDDEN ) ) && "".equals( value ) ) {
            metawidget.getChildren().remove( widget );
            widget = new HtmlTag( "span" );
            metawidget.getChildren().add( widget );
            return widget;
        }
        
        // Return the hidden input HtmlTag which has just been created
        
        return widget;
    }

}
