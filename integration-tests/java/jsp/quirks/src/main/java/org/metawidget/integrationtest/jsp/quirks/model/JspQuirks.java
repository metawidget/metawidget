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

package org.metawidget.integrationtest.jsp.quirks.model;

import java.util.Date;

import org.metawidget.inspector.annotation.UiLabel;

/**
 * Models an entity that tests some JSP-specific quirks.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
