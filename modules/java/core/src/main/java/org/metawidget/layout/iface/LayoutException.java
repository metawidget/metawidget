// Metawidget (licensed under LGPL)
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

package org.metawidget.layout.iface;

import org.metawidget.iface.MetawidgetException;

/**
 * Any exception that occurs during layout.
 */

public class LayoutException
	extends MetawidgetException {

	//
	// Public statics
	//

	/**
	 * Static constructor.
	 * <p>
	 * Using static constructor methods prevents unnecessarily nesting LayoutExceptions within
	 * LayoutExceptions.
	 */

	public static LayoutException newException( Throwable cause ) {

		if ( cause instanceof LayoutException ) {
			return (LayoutException) cause;
		}

		return new LayoutException( cause );
	}

	/**
	 * Static constructor.
	 * <p>
	 * For consistency with <code>LayoutException.newException( Throwable )</code>.
	 */

	public static LayoutException newException( String message ) {

		return new LayoutException( message );
	}

	/**
	 * Static constructor.
	 * <p>
	 * For consistency with <code>LayoutException.newException( Throwable )</code>.
	 */

	public static LayoutException newException( String message, Throwable cause ) {

		return new LayoutException( message, cause );
	}

	//
	// Constructor
	//

	private LayoutException( String message ) {

		super( message );
	}

	private LayoutException( Throwable cause ) {

		super( cause );
	}

	private LayoutException( String message, Throwable cause ) {

		super( message, cause );
	}
}
