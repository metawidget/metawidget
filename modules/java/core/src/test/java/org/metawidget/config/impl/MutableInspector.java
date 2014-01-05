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

package org.metawidget.config.impl;

import org.metawidget.inspector.iface.Inspector;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class MutableInspector
	implements Inspector {

	//
	// Public methods
	//

	/**
	 * @param foo
	 *            ignored
	 */

	public void setFoo( String foo ) {

		// Do nothing
	}

	public String inspect( Object toInspect, String type, String... names ) {

		return null;
	}
}
