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

package org.metawidget.test.inspector;

import java.util.List;

import org.metawidget.inspector.Inspector;
import org.metawidget.inspector.InspectorException;
import org.w3c.dom.Document;

/**
 * @author Richard Kennard
 */

public class BadInspector
	implements Inspector
{
	//
	//
	// Private members
	//
	//

	private List<String>	mList;

	private int				mInt;

	//
	//
	// Constructor
	//
	//

	public BadInspector( BadInspectorConfig config )
	{
		mList = config.getList();
		mInt = config.getInt();
	}

	//
	//
	// Public methods
	//
	//

	public Document inspect( Object toInspect, String type, String... names )
		throws InspectorException
	{
		return null;
	}
	
	public List<String> getList()
	{
		return mList;
	}
	
	public int getInt()
	{
		return mInt;
	}
}
