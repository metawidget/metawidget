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

import java.util.Date;
import java.util.List;

/**
 * @author Richard Kennard
 */

public class BadInspectorConfig
{
	//
	// Private members
	//

	private List<String>	mList;

	private int				mInt;

	//
	// Public methods
	//

	public void setList( List<String> list )
	{
		mList = list;
	}

	public List<String> getList()
	{
		return mList;
	}
	
	public void setInt( int anInt )
	{
		mInt = anInt;
	}

	public int getInt()
	{
		return mInt;
	}
	
	public void setDate( Date date )
	{
		// Test unsupported types
	}

	public void setNoParameters()
	{
		// Test method with no parameters
	}

	public void setMultipleParameters( String foo, String bar )
	{
		// Test method with multiple parameters
	}
}
