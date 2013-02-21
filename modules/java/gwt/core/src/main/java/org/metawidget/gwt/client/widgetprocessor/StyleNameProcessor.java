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

package org.metawidget.gwt.client.widgetprocessor;

import java.util.Map;

import org.metawidget.gwt.client.ui.GwtMetawidget;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

import com.google.gwt.user.client.ui.Widget;

/**
 * WidgetProcessor that calls <code>addStyleName</code> on a Widget, based on
 * the <code>getStyleName</code> of the parent Metawidget.
 *
 * @author Richard Kennard
 */

public class StyleNameProcessor
	implements WidgetProcessor<Widget, GwtMetawidget> {

	//
	// Public methods
	//

	public Widget processWidget( Widget widget, String elementName, Map<String, String> attributes, GwtMetawidget metawidget ) {

		// Note: this only applies the styles to Stubs at the top-level. In practice, this seemed
		// to give more 'expected' behaviour than drilling into the Stubs and applying the styles
		// to all their subcomponents too

		String styleName = metawidget.getStyleName();

		if ( styleName != null && styleName.length() != 0 ) {
			widget.addStyleName( styleName );
		}

		return widget;
	}
}
