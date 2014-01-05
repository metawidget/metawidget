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

package org.metawidget.integrationtest.faces.quirks.model.icefaces;

import org.metawidget.inspector.annotation.UiSection;

/**
 * Models an entity that tests some ICEfaces PanelTabSet-specific quirks.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class IceFacesPanelTabSetQuirks {

	//
	// Private members
	//

	private String	mString1;

	private String	mString2;

	//
	// Public methods
	//

	@UiSection( "Tab #1" )
	public String getString1() {

		return mString1;
	}

	public void setString1( String string1 ) {

		mString1 = string1;
	}

	@UiSection( "Tab #2" )
	public String getString2() {

		return mString2;
	}

	public void setString2( String string2 ) {

		mString2 = string2;
	}
}
