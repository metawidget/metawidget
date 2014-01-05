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

package org.metawidget.iface;

/**
 * Any exception that occurs during Metawidget operation.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class MetawidgetException
	extends RuntimeException {

	//
	// Public statics
	//

	/**
	 * Static constructor.
	 * <p>
	 * Using static constructor methods prevents unnecessarily nesting MetawidgetExceptions within
	 * MetawidgetExceptions.
	 */

	public static MetawidgetException newException( Throwable cause ) {

		if ( cause instanceof MetawidgetException ) {
			return (MetawidgetException) cause;
		}

		return new MetawidgetException( cause );
	}

	/**
	 * Static constructor.
	 * <p>
	 * For consistency with <code>MetawidgetException.newException( Throwable )</code>.
	 */

	public static MetawidgetException newException( String message ) {

		return new MetawidgetException( message );
	}

	/**
	 * Static constructor.
	 * <p>
	 * For consistency with <code>MetawidgetException.newException( Throwable )</code>.
	 */

	public static MetawidgetException newException( String message, Throwable cause ) {

		return new MetawidgetException( message, cause );
	}

	//
	// Constructor
	//

	protected MetawidgetException( String message ) {

		super( message );
	}

	protected MetawidgetException( Throwable cause ) {

		super( cause );
	}

	protected MetawidgetException( String message, Throwable cause ) {

		super( message, cause );
	}
}
