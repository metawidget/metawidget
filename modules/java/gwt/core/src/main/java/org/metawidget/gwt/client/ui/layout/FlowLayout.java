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

package org.metawidget.gwt.client.ui.layout;

import java.util.Map;

import org.metawidget.gwt.client.ui.GwtMetawidget;
import org.metawidget.layout.iface.Layout;

import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Layout to simply output components one after another, with no labels and no structure.
 * <p>
 * This Layout is suited to rendering single components, or for rendering components whose layout
 * relies entirely on CSS.
 *
 * @author Richard Kennard
 */

public class FlowLayout
	implements Layout<Widget, Panel, GwtMetawidget> {

	//
	// Public methods
	//

	public void layoutWidget( Widget widget, String elementName, Map<String, String> attributes, Panel container, GwtMetawidget metawidget ) {

		metawidget.add( widget );
	}
}
