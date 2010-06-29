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

package org.metawidget.example.shared.addressbook.controller;

/**
 * @author Richard Kennard
 */

public class CommunicationsController {

	//
	// Protected members
	//

	protected final static String[]	ALL_COMMUNICATION_TYPES	= { "Telephone", "Mobile", "Fax", "E-mail" };

	//
	// Public methods
	//

	/**
	 * A real-world implementation would likely use a database lookup.
	 */

	public String[] getAll() {

		return ALL_COMMUNICATION_TYPES;
	}
}
