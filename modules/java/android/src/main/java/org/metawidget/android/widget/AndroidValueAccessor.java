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

package org.metawidget.android.widget;

import android.view.View;

/**
 * Additional interface implemented by Android WidgetBuilders.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public interface AndroidValueAccessor {

	//
	// Methods
	//

	Object getValue( View view );

	/**
	 * Sets the given View to the specified value.
	 *
	 * @return true if the View was recognised and its value set
	 */

	boolean setValue( Object value, View view );
}
