// Metawidget (licensed under LGPL)
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
