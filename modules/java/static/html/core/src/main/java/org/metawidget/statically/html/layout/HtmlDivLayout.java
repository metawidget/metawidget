// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

package org.metawidget.statically.html.layout;

import java.util.Map;

import org.metawidget.layout.iface.LayoutException;
import org.metawidget.statically.StaticXmlStub;
import org.metawidget.statically.StaticXmlWidget;
import org.metawidget.statically.html.StaticHtmlMetawidget;
import org.metawidget.statically.html.widgetbuilder.HtmlDiv;

/**
 * Layout to arrange widgets using DIVs
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class HtmlDivLayout
	extends HtmlLayout {

	//
	// Public methods
	//

	public void layoutWidget( StaticXmlWidget widget, String elementName, Map<String, String> attributes, StaticXmlWidget container, StaticHtmlMetawidget metawidget ) {

		try {
			// Ignore stubs

			if ( widget instanceof StaticXmlStub && widget.getChildren().isEmpty() ) {
				return;
			}

			HtmlDiv div = new HtmlDiv();
			layoutLabel( div, widget, elementName, attributes, metawidget );
			div.getChildren().add( widget );

			container.getChildren().add( div );

		} catch ( Exception e ) {
			throw LayoutException.newException( e );
		}
	}
}
