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

package org.metawidget.example.gwt.clientside.client.ui;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * @author Richard Kennard
 */

public class ClientSideModule
	implements EntryPoint
{
	//
	// Package-level members
	//

	Panel					mPanel;

	//
	// Constructor
	//

	public ClientSideModule()
	{
		this( RootPanel.get() );
	}

	public ClientSideModule( Panel panel )
	{
		mPanel = panel;
	}

	//
	// Public methods
	//

	public void onModuleLoad()
	{
		// TODO: clientside example!
	}

	public Panel getPanel()
	{
		return mPanel;
	}
}
