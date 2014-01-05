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

package org.metawidget.inspectionresultprocessor.iface;

import org.metawidget.iface.MetawidgetException;

/**
 * Any exception that occurs during inspection result processing.
 * 
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public final class InspectionResultProcessorException
	extends MetawidgetException {

	//
	// Public statics
	//

	/**
	 * Static constructor.
	 * <p>
	 * Using static constructor methods prevents unnecessarily nesting
	 * InspectionResultProcessorExceptions within InspectionResultProcessorExceptions.
	 */

	public static InspectionResultProcessorException newException( Throwable cause ) {

		if ( cause instanceof InspectionResultProcessorException ) {
			return (InspectionResultProcessorException) cause;
		}

		return new InspectionResultProcessorException( cause );
	}

	/**
	 * Static constructor.
	 * <p>
	 * For consistency with
	 * <code>InspectionResultProcessorException.newException( Throwable )</code>.
	 */

	public static InspectionResultProcessorException newException( String message ) {

		return new InspectionResultProcessorException( message );
	}

	/**
	 * Static constructor.
	 * <p>
	 * For consistency with
	 * <code>InspectionResultProcessorException.newException( Throwable )</code>.
	 */

	public static InspectionResultProcessorException newException( String message, Throwable cause ) {

		return new InspectionResultProcessorException( message, cause );
	}

	//
	// Constructor
	//

	private InspectionResultProcessorException( String message ) {

		super( message );
	}

	private InspectionResultProcessorException( Throwable cause ) {

		super( cause );
	}

	private InspectionResultProcessorException( String message, Throwable cause ) {

		super( message, cause );
	}
}
