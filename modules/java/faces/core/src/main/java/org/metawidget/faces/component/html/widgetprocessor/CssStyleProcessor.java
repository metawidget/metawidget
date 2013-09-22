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

package org.metawidget.faces.component.html.widgetprocessor;

import java.util.Map;

import javax.faces.component.UIComponent;

import org.metawidget.faces.FacesUtils;
import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.html.HtmlMetawidget;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

/**
 * WidgetProcessor that adds CSS styles to a UIComponent, based on the styles of the parent
 * Metawidget.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class CssStyleProcessor
	implements WidgetProcessor<UIComponent, UIMetawidget> {

	//
	// Public methods
	//

	public UIComponent processWidget( UIComponent component, String elementName, Map<String, String> attributes, UIMetawidget metawidget ) {

		// Note: this only applies the styles to UIStubs at the top-level. In practice, this seemed
		// to give more 'expected' behaviour than drilling into the UIStubs and applying the styles
		// to all their subcomponents too

		HtmlMetawidget htmlMetawidget = (HtmlMetawidget) metawidget;
		FacesUtils.setStyleAndStyleClass( component, htmlMetawidget.getStyle(), htmlMetawidget.getStyleClass() );

		return component;
	}
}
