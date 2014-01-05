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

package android.widget;

import android.content.Context;

/**
 * Dummy implementation for unit testing.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class ListView
	extends AdapterView<Adapter> {

	//
	// Constructor
	//

	public ListView( Context context ) {

		super( context );
	}

	//
	// Public methods
	//

	/**
	 * @param onItemClickListener
	 */

	public void setOnItemClickListener( OnItemClickListener onItemClickListener ) {

		// Do nothing
	}
}
