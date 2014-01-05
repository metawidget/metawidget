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

package org.metawidget.android.widget.widgetprocessor;

import static org.metawidget.inspector.InspectionResultConstants.DISABLED;
import static org.metawidget.inspector.InspectionResultConstants.TRUE;

import java.util.Map;

import org.metawidget.android.widget.AndroidMetawidget;
import org.metawidget.widgetprocessor.iface.WidgetProcessor;

import android.view.View;

/**
 * WidgetProcessor that sets the <code>disabled</code> attribute.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class DisabledAttributeProcessor
	implements WidgetProcessor<View, AndroidMetawidget> {

	//
	// Public methods
	//

	public View processWidget( View view, String elementName, Map<String, String> attributes, AndroidMetawidget metawidget ) {

		if ( TRUE.equals( attributes.get( DISABLED ) ) ) {
			view.setEnabled( false );
		}
		
		return view;
	}
}
