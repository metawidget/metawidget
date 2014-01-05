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

package org.metawidget.inspector.impl.propertystyle;

import org.metawidget.inspector.impl.BaseTrait;

/**
 * Convenience implementation for Properties.
 * <p>
 * Handles construction, and returning names and types.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public abstract class BaseProperty
	extends BaseTrait
	implements Property {

	//
	// Private methods
	//

	private String	mType;

	//
	// Constructor
	//

	public BaseProperty( String name, String type ) {

		super( name );
		mType = type;
	}

	//
	// Public methods
	//

	public String getType() {

		return mType;
	}
}
