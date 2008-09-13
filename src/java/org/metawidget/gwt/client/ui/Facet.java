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

package org.metawidget.gwt.client.ui;

import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Facet for GWT environments.
 * <p>
 * Facets differ from Stubs in that Stubs override widget creation, whereas Facets are
 * 'decorations' (such as button bars) to be recognized and arranged at the discretion of the
 * Layout.
 *
 * @author Richard Kennard
 */

public class Facet
	extends SimplePanel
	implements HasName
{
	//
	// Private members
	//

	private String	mName;

	//
	// Public methods
	//

	public String getName()
	{
		return mName;
	}

	public void setName( String name )
	{
		mName = name;
	}
}
