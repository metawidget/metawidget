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

import org.metawidget.iface.Immutable;

/**
 * Simple immutable structure to store a value and its declared type.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class ValueAndDeclaredType
	implements Immutable {

	//
	// Private members
	//

	private Object	mValue;

	private String	mDeclaredType;

	//
	// Constructor
	//

	public ValueAndDeclaredType( Object value, String declaredType ) {

		mValue = value;
		mDeclaredType = declaredType;
	}

	//
	// Public methods
	//

	public Object getValue() {

		return mValue;
	}

	public String getDeclaredType() {

		return mDeclaredType;
	}
}
