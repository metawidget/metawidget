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

package org.metawidget.gwt.client.widgetbuilder;

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
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class OverriddenWidgetBuilder
	implements WidgetBuilder<Widget, GwtMetawidget> {

	//
	// Public methods
	//

	public Widget buildWidget( String elementName, Map<String, String> attributes, GwtMetawidget metawidget ) {

		String name = attributes.get( NAME );

		if ( name == null ) {
			return null;
		}

		Widget widget = null;
		Set<Widget> existingUnusedWidgets = metawidget.fetchExistingUnusedWidgets();

		for ( Widget widgetExisting : existingUnusedWidgets ) {
			if ( !( widgetExisting instanceof HasName ) ) {
				continue;
			}

			if ( name.equals( ( (HasName) widgetExisting ).getName() ) ) {
				widget = widgetExisting;
				break;
			}
		}

		if ( widget != null ) {
			existingUnusedWidgets.remove( widget );
		}

		return widget;
	}
}
