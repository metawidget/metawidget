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

package org.metawidget.gwt.client.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.gwt.client.ui.GwtMetawidget;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.Widget;

/**
 * Processor to set the name of <code>HasName</code> widgets to be the business field name.
 *
 * @author Richard Kennard
 */

public class HasNameProcessor
	implements WidgetProcessor<Widget, GwtMetawidget> {

	//
	// Public methods
	//

	@Override
	public Widget processWidget( Widget widget, String elementName, Map<String, String> attributes, final GwtMetawidget metawidget ) {

		if ( widget instanceof HasName ) {
			( (HasName) widget ).setName( attributes.get( NAME ) );
		}

		return widget;
	}
}
