// Metawidget
//
// For historical reasons, this file is licensed under the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html).
//
// Most other files in Metawidget are licensed under both the
// LGPL/EPL and a commercial license. See http://metawidget.org
// for details.

package org.metawidget.statically.html.widgetprocessor;

import java.util.Map;

import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.html.StaticHtmlMetawidget;
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

        String styleClass = metawidget.getAttribute( "class" );

        if ( styleClass != null ) {
            String existingStyleClass = widget.getAttribute( "class" );

            if ( existingStyleClass == null || "".equals( existingStyleClass ) ) {
                widget.putAttribute( "class" , styleClass );
            }
            else {
                widget.putAttribute( "class" , existingStyleClass + " " + styleClass );
            }
        }

        return widget;
    }

}
