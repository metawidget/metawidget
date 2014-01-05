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

package org.metawidget.inspector.iface;

import org.metawidget.iface.MetawidgetException;

/**
 * Any exception that occurs during inspection.
 * 
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public final class InspectorException
	extends MetawidgetException {

	//
	// Public statics
	//

	/**
	 * Static constructor.
	 * <p>
	 * Using static constructor methods prevents unnecessarily nesting InspectorExceptions within
	 * InspectorExceptions.
	 */

	public static InspectorException newException( Throwable cause ) {

		if ( cause instanceof InspectorException ) {
			return (InspectorException) cause;
		}

		return new InspectorException( cause );
	}

	/**
	 * Static constructor.
	 * <p>
	 * For consistency with <code>InspectorException.newException( Throwable )</code>.
	 */

	public static InspectorException newException( String message ) {

		return new InspectorException( message );
	}

	/**
	 * Static constructor.
	 * <p>
	 * For consistency with <code>InspectorException.newException( Throwable )</code>.
	 */

	public static InspectorException newException( String message, Throwable cause ) {

		return new InspectorException( message, cause );
	}

	//
	// Constructor
	//

	private InspectorException( String message ) {

		super( message );
	}

	private InspectorException( Throwable cause ) {

		super( cause );
	}

	private InspectorException( String message, Throwable cause ) {

		super( message, cause );
	}
}
