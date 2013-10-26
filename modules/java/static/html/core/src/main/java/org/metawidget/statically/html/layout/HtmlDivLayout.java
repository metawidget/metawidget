// Metawidget
//
// This library is dual licensed under both LGPL and a commercial
// license.
//
// LGPL: this library is free software; you can redistribute it and/or
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
//
// Commercial License: See http://metawidget.org for details

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
