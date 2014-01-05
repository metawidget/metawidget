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

package org.metawidget.android.widget.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;
import java.util.Set;

import org.metawidget.android.widget.AndroidMetawidget;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

import android.view.View;

/**
 * WidgetBuilder for overridden widgets in Swing environments.
 * <p>
 * Locates overridden widgets based on their <code>tag</code>.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class OverriddenWidgetBuilder
	implements WidgetBuilder<View, AndroidMetawidget> {

	//
	// Public methods
	//

	public View buildWidget( String elementName, Map<String, String> attributes, AndroidMetawidget metawidget ) {

		View view = null;
		String name = attributes.get( NAME );

		if ( name == null ) {
			return null;
		}

		Set<View> existingUnusedViews = metawidget.fetchExistingUnusedViews();

		for ( View viewExisting : existingUnusedViews ) {
			if ( name.equals( viewExisting.getTag() ) ) {
				view = viewExisting;
				break;
			}
		}

		if ( view != null ) {
			existingUnusedViews.remove( view );
		}

		return view;
	}
}
