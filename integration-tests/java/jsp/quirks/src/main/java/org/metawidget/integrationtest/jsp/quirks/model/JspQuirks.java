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

package org.metawidget.integrationtest.jsp.quirks.model;

import java.util.Date;

import org.metawidget.inspector.annotation.UiLabel;

/**
 * Models an entity that tests some JSP-specific quirks.
 *
 * @author Richard Kennard
 */

public class JspQuirks {

	//
	// Private members
	//

	private boolean	mBooleanPrimitive;

	//
	// Public methods
	//

	@UiLabel( "${40+2} boolean" )
	public boolean isBooleanPrimitive() {

		return mBooleanPrimitive;
	}

	public void setBooleanPrimitive( boolean booleanPrimitive ) {

		mBooleanPrimitive = booleanPrimitive;
	}
	
	public String getIgnoredName() {
		
		return null;
	}

	public Date getIgnoredType() {
		
		return null;
	}
}
