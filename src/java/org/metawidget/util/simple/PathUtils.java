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

package org.metawidget.util.simple;

import java.util.ArrayList;
import java.util.List;

/**
 * Utilities for working with <code>type/name/name</code>-formatted Strings
 *
 * @author Richard Kennard
 */

public class PathUtils
{
	//
	//
	// Public statics
	//
	//

	public static TypeAndNames parsePath( String path )
	{
		return parsePath( path, StringUtils.SEPARATOR_SLASH_CHAR );
	}

	public static TypeAndNames parsePath( String path, char separator )
	{
		int indexOfTypeEnd = path.indexOf( separator );

		// Just type?

		if ( indexOfTypeEnd == -1 )
			return new TypeAndNames( path, null );

		String type = path.substring( 0, indexOfTypeEnd );

		// Parse names

		int indexOfNameEnd = indexOfTypeEnd;

		List<String> names = new ArrayList<String>();

		while ( true )
		{
			int indexOfNameStart = indexOfNameEnd + 1;
			indexOfNameEnd = path.indexOf( separator, indexOfNameStart );

			if ( indexOfNameEnd == -1 )
			{
				names.add( path.substring( indexOfNameStart ) );
				break;
			}

			names.add( path.substring( indexOfNameStart, indexOfNameEnd ) );
		}

		if ( names.isEmpty() )
			return new TypeAndNames( type, null );

		return new TypeAndNames( type, names.toArray( new String[names.size()] ) );
	}

	/**
	 * Tuple for returning a <code>type</code> and an array of <code>names</code>.
	 */

	public static class TypeAndNames
	{
		//
		//
		// Private methods
		//
		//

		private String		mType;

		private String[]	mNames;

		//
		//
		// Constructor
		//
		//

		public TypeAndNames( String type, String[] names )
		{
			mType = type;
			mNames = names;
		}

		//
		//
		// Public methods
		//
		//

		public String getType()
		{
			return mType;
		}

		public String[] getNames()
		{
			return mNames;
		}
	}

	//
	//
	// Private constructor
	//
	//

	private PathUtils()
	{
		// Can never be called
	}
}
