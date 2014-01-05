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

package org.metawidget.integrationtest.struts.quirks.form;

import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiHidden;
import org.metawidget.inspector.annotation.UiLabel;

/**
 * Models an entity that tests DisplayTag-specific quirks.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class DisplayTagQuirks {

	//
	// Private members
	//

	private int		mId;

	private String	mName;

	private String	mDescription;

	//
	// Constructor
	//

	public DisplayTagQuirks( int id, String name, String description ) {

		mId = id;
		mName = name;
		mDescription = description;
	}

	//
	// Public methods
	//

	@UiHidden
	public int getId() {

		return mId;
	}

	@UiLabel( "Name label" )
	public String getName() {

		return mName;
	}

	@UiComesAfter( "name" )
	public String getDescription() {

		return mDescription;
	}
}
