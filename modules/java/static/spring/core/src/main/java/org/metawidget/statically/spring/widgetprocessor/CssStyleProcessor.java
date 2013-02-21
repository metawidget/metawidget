// Metawidget (licensed under LGPL)
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
