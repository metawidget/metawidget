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

package org.metawidget.faces.quirks.model;

import org.metawidget.inspector.annotation.UiComesAfter;
import org.metawidget.inspector.annotation.UiHidden;
import org.metawidget.inspector.annotation.UiLabel;

/**
 * Models an entity that tests HtmlDataTableQuirks-specific quirks.
 *
 * @author Richard Kennard
 */

public class HtmlDataTableQuirks {

	//
	// Private members
	//

	private int		mId;

	private String	mName;

	private String	mDescription;

	//
	// Constructor
	//

	public HtmlDataTableQuirks( int id, String name, String description ) {

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
