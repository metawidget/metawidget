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

package org.metawidget.test.spring.quirks.model;

import java.util.List;

import org.metawidget.util.CollectionUtils;

/**
 * @author Richard Kennard
 */

public class LookupBean
{
	//
	// Private members
	//

	private List<LookupItem>	mItems;

	//
	// Constructor
	//

	public LookupBean()
	{
		mItems = CollectionUtils.newArrayList();
		mItems.add( new LookupItem( "value1", "label1" ));
		mItems.add( new LookupItem( "value2", "label2" ));
		mItems.add( new LookupItem( "value3", "label3" ));
	}

	//
	// Public methods
	//

	public List<LookupItem> getItems()
	{
		return mItems;
	}

	//
	// Inner class
	//

	public static class LookupItem
	{
		//
		// Private members
		//

		private String mValue;

		private String mLabel;

		//
		// Constructor
		//

		public LookupItem( String value, String label )
		{
			mValue = value;
			mLabel = label;
		}

		//
		// Public methods
		//

		public String getValue()
		{
			return mValue;
		}

		public String getLabel()
		{
			return mLabel;
		}
	}
}
