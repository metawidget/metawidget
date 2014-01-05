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

package org.metawidget.integrationtest.spring.quirks.model;

import org.metawidget.inspector.spring.UiSpringLookup;

/**
 * Models an entity that tests some Spring-specific quirks.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class SpringQuirks {

	//
	// Private members
	//

	private String	mLookup;

	//
	// Public methods
	//

	@UiSpringLookup( value = "${lookup.items}", itemValue = "${'va'}lue", itemLabel = "label" )
	public String getLookup() {

		return mLookup;
	}

	public void setLookup( String lookup ) {

		mLookup = lookup;
	}
}
