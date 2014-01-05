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

package org.metawidget.android.widget.layout;

import java.util.Map;

import org.metawidget.android.widget.AndroidMetawidget;
import org.metawidget.layout.iface.Layout;

import android.view.View;
import android.view.ViewGroup;

/**
 * Layout to simply output components one after another, with no labels and no structure.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class SimpleLayout
	implements Layout<View, ViewGroup, AndroidMetawidget> {

	public void layoutWidget( View view, String elementName, Map<String, String> attributes, ViewGroup container, AndroidMetawidget metawidget ) {

		container.addView( view );
	}
}
