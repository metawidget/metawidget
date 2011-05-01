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
 * Utilities for working with Classes.
 * <p>
 * In this context, 'simple' means 'with minimal class dependencies, suitable to be compiled into
 * JavaScript' (eg. for GWT).
 *
 * @author Richard Kennard
 */

public final class SimpleClassUtils {

	//
	// Public statics
	//

	/**
	 * Gets the 'simple' name of the class.
	 * <p>
	 * Essentially a simplified version of <code>Class.getSimpleName</code>, which is JDK
	 * 1.5-specific.
	 */

	public static String getSimpleName( Class<?> clazz ) {

		String className = clazz.getName();

		int lastIndexOf = className.lastIndexOf( StringUtils.SEPARATOR_DOT_CHAR );

		if ( lastIndexOf != -1 ) {
			className = className.substring( lastIndexOf + 1 );
		}

		return className;
	}

	//
	// Private constructor
	//

	private SimpleClassUtils() {

		// Can never be called
	}
}
