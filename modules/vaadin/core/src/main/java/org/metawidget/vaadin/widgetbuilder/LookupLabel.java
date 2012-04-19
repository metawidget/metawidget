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

package org.metawidget.vaadin.widgetbuilder;

import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/*
 * Label whose values use a lookup.
 */

public class LookupLabel
	extends com.vaadin.ui.Label {

	//
	// Private statics
	//

	private static final long	serialVersionUID	= 1l;

	//
	// Private members
	//

	private Map<String, String>	mLookup;

	private ResourceBundle		mBundle;

	//
	// Constructor
	//

	public LookupLabel( Map<String, String> lookup, ResourceBundle bundle ) {

		if ( lookup == null ) {
			throw new NullPointerException( "lookup" );
		}

		mLookup = lookup;
		mBundle = bundle;
	}

	//
	// Public methods
	//

	@Override
	public void setValue( Object text ) {

		String lookup = "";

		if ( text != null ) {
			lookup = ( text instanceof Enum<?> ) ? ( (Enum<?>) text ).name() : text.toString();
		}

		if ( lookup != null && mLookup != null ) {
			lookup = mLookup.get( lookup );
		}

		if ( mBundle != null && lookup != null ) {
			try {
				lookup = mBundle.getString( lookup );
			} catch ( MissingResourceException e ) {
				// Use default lookup
			}
		}

		super.setValue( lookup );
	}
}
