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

		String value = super.getValue();
		String lookup = mLookup.get( value );

		if ( lookup != null ) {
			return lookup;
		}

		return value;
	}
}
