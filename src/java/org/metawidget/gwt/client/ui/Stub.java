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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Stub for GWT environments.
 * <p>
 * Stubs are used to 'stub out' what Metawidget would normally create - either to suppress widget
 * creation entirely or to create child widgets with a different name. They differ from Facets in
 * that Facets are simply 'decorations' (such as button bars) to be recognized and arranged at the
 * discretion of the Layout.
 *
 * @author Richard Kennard
 */

public class Stub
	extends SimplePanel
{
	//
	//
	// Private members
	//
	//

	private String				mName;

	private Map<String, String>	mAttributes;

	//
	//
	// Public methods
	//
	//

	public String getName()
	{
		return mName;
	}

	public void setName( String name )
	{
		mName = name;
	}

	public void setAttribute( String name, String value )
	{
		if ( mAttributes == null )
			mAttributes = new HashMap<String, String>();

		mAttributes.put( name, value );
	}

	public Map<String, String> getAttributes()
	{
		if ( mAttributes == null )
			return Collections.emptyMap();

		return mAttributes;
	}
}
