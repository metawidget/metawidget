// Metawidget
//
// This library is dual licensed under both LGPL and a commercial
// license.
//
// LGPL: this library is free software; you can redistribute it and/or
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
//
// Commercial License: See http://metawidget.org for details

package org.metawidget.vaadin.ui.widgetbuilder;

import java.util.Map;

/**
 * Label whose values display using a lookup.
 * 
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class LookupLabel
	extends com.vaadin.ui.Label {

	//
	// Private members
	//

	private Map<String, String>	mLookup;

	//
	// Constructor
	//

	public LookupLabel( Map<String, String> lookup ) {

		if ( lookup == null ) {
			throw new NullPointerException( "lookup" );
		}

		mLookup = lookup;
	}

	//
	// Public methods
	//

	/**
	 * Overridden to get the value using our lookup.
	 */

	@Override
	public String getValue() {

		String value = (String) super.getValue();
		String lookup = mLookup.get( value );

		if ( lookup != null ) {
			return lookup;
		}

		return value;
	}
}
