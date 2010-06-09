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

/**
 * Utilities for working with <code>type/name</code>-formatted Strings
 *
 * @author Richard Kennard
 */

public class PathUtils
{
	//
	// Public statics
	//

	public static TypeAndNames parsePath( String path )
	{
		return new TypeAndNames( path, StringUtils.SEPARATOR_FORWARD_SLASH_CHAR );
	}

	public static TypeAndNames parsePath( String path, char separator )
	{
		return new TypeAndNames( path, separator );
	}

	/**
	 * Tuple for returning a <code>type</code> and an array of <code>names</code>.
	 * <p>
	 * Uses lazy initialization to save on regex calls.
	 */

	public static class TypeAndNames
	{
		//
		//
		// Private methods
		//
		//

		private String		mPath;

		private char		mSeparator;

		private String		mParsedType;

		private String		mParsedNames;

		private String[]	mParsedNamesAsArray;

		//
		//
		// Constructor
		//
		//

		public TypeAndNames( String path, char separator )
		{
			mPath = path.trim();
			mSeparator = separator;
		}

		//
		//
		// Public methods
		//
		//

		public String getType()
		{
			if ( mParsedType == null )
			{
				int indexOfTypeEnd = mPath.indexOf( mSeparator );

				// Just type?

				if ( indexOfTypeEnd == -1 )
				{
					mParsedType = mPath;
					mParsedNames = "";
				}
				else
				{
					mParsedType = mPath.substring( 0, indexOfTypeEnd );

					// Ends in separator?

					if ( indexOfTypeEnd == mPath.length() - 1 )
					{
						mParsedNames = "";
					}
					else
					{
						mParsedNames = mPath.substring( indexOfTypeEnd + 1 );
					}
				}


			}

			return mParsedType;
		}

		public String getNames()
		{
			if ( mParsedNames == null )
			{
				getType();
			}

			return mParsedNames;
		}

		public String[] getNamesAsArray()
		{
			if ( mParsedNamesAsArray == null )
			{
				String names = getNames();

				if ( "".equals( names ))
				{
					mParsedNamesAsArray = EMPTY_STRING_ARRAY;
				}
				else
				{
					mParsedNamesAsArray = getNames().split( "\\" + mSeparator );
				}
			}

			return mParsedNamesAsArray;
		}
	}

	//
	// Private statics
	//

	final static String[]	EMPTY_STRING_ARRAY	= new String[0];

	//
	// Private constructor
	//

	private PathUtils()
	{
		// Can never be called
	}
}
