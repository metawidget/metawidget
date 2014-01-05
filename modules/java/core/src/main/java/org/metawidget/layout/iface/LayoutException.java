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

package org.metawidget.layout.iface;

import org.metawidget.iface.MetawidgetException;

/**
 * Any exception that occurs during layout.
 * 
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public final class LayoutException
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
