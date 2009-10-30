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

package org.metawidget.gwt.client.widgetbuilder.impl;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;
import java.util.Set;

import org.metawidget.gwt.client.ui.GwtMetawidget;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.Widget;

/**
 * WidgetBuilder for overridden widgets in GWT environments.
 * <p>
 * Locates overridden widgets based on their <code>getName</code>.
 *
 * @author Richard Kennard
 */

public class OverriddenWidgetBuilder
	implements WidgetBuilder<Widget, GwtMetawidget>
{
	//
	// Public methods
	//

	@Override
	public Widget buildWidget( String elementName, Map<String, String> attributes, GwtMetawidget metawidget )
	{
		String name = attributes.get( NAME );

		if ( name == null )
			return null;

		Widget widget = null;
		Set<Widget> existingUnusedWidgets = metawidget.fetchExistingUnusedWidgets();

		for ( Widget widgetExisting : existingUnusedWidgets )
		{
			if ( !( widgetExisting instanceof HasName ) )
				continue;

			if ( name.equals( ( (HasName) widgetExisting ).getName() ) )
			{
				widget = widgetExisting;
				break;
			}
		}

		if ( widget != null )
			existingUnusedWidgets.remove( widget );

		return widget;
	}
}
