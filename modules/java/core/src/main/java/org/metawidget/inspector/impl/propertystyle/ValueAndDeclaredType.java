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

package org.metawidget.inspector.impl.propertystyle;

import org.metawidget.iface.Immutable;

/**
 * Simple immutable structure to store a value and its declared type.
 *
 * @author Richard Kennard
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
