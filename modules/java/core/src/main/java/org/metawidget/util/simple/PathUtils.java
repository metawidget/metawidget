// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

package org.metawidget.util.simple;

/**
 * Utilities for working with <code>type/name</code>-formatted Strings
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public final class PathUtils {

	//
	// Public statics
	//

	public static TypeAndNames parsePath( String path ) {

		return new TypeAndNames( path, StringUtils.SEPARATOR_FORWARD_SLASH_CHAR );
	}

	public static TypeAndNames parsePath( String path, char separator ) {

		return new TypeAndNames( path, separator );
	}

	/**
	 * Tuple for returning a <code>type</code> and an array of <code>names</code>.
	 * <p>
	 * Uses lazy initialization to save on regex calls.
	 */

	public static class TypeAndNames {

		//
		// Private methods
		//

		private String		mPath;

		private char		mSeparator;

		private String		mParsedType;

		private String		mParsedNames;

		private String[]	mParsedNamesAsArray;

		//
		// Constructor
		//

		public TypeAndNames( String path, char separator ) {

			mPath = path.trim();
			mSeparator = separator;
		}

		//
		// Public methods
		//

		public String getType() {

			if ( mParsedType == null ) {
				int indexOfTypeEnd = mPath.indexOf( mSeparator );

				// Just type?

				if ( indexOfTypeEnd == -1 ) {
					mParsedType = mPath;
					mParsedNames = "";
				} else {
					mParsedType = mPath.substring( 0, indexOfTypeEnd );

					// Ends in separator?

					if ( indexOfTypeEnd == mPath.length() - 1 ) {
						mParsedNames = "";
					} else {
						mParsedNames = mPath.substring( indexOfTypeEnd + 1 );
					}
				}

			}

			return mParsedType;
		}

		public String getNames() {

			if ( mParsedNames == null ) {
				getType();
			}

			return mParsedNames;
		}

		public String[] getNamesAsArray() {

			if ( mParsedNamesAsArray == null ) {
				String names = getNames();

				if ( "".equals( names ) ) {
					mParsedNamesAsArray = EMPTY_STRING_ARRAY;
				} else {
					mParsedNamesAsArray = getNames().split( "\\" + mSeparator );
				}
			}

			return mParsedNamesAsArray;
		}
	}

	//
	// Private statics
	//

	static final String[]	EMPTY_STRING_ARRAY	= new String[0];

	//
	// Private constructor
	//

	private PathUtils() {

		// Can never be called
	}
}
