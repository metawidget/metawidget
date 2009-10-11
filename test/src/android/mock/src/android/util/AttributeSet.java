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

package android.util;

import java.util.Iterator;
import java.util.LinkedHashMap;


/**
 * Dummy implementation for unit testing.
 *
 * @author Richard Kennard
 */

public class AttributeSet
{
	//
	// Private members
	//

	private	LinkedHashMap<String, String> mAttributes = new LinkedHashMap<String, String>();

	//
	// Public methods
	//

	public void setAttributeValue( String key, String value )
	{
		mAttributes.put( key, value );
	}

	public String getAttributeValue( Object object, String key )
	{
		return mAttributes.get( key );
	}

	public int getAttributeCount()
	{
		return mAttributes.size();
	}

	public String getAttributeValue( int index )
	{
		Iterator<String> iterator = mAttributes.values().iterator();

		for( int loop = 0; loop < index - 1; loop++ )
		{
			iterator.next();
		}

		return iterator.next();
	}

	public String getAttributeName( int index )
	{
		Iterator<String> iterator = mAttributes.keySet().iterator();

		for( int loop = 0; loop < index - 1; loop++ )
		{
			iterator.next();
		}

		return iterator.next();
	}

	public int getAttributeResourceValue( Object object, String string, int i )
	{
		return 0;
	}
}
