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

package org.metawidget.widgetprocessor.iface;

import org.metawidget.iface.MetawidgetException;

/**
 * Any exception that occurs during widget processing.
 * 
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public final class WidgetProcessorException
	extends MetawidgetException {

	//
	// Public statics
	//

	/**
	 * Static constructor.
	 * <p>
	 * Using static constructor methods prevents unnecessarily nesting WidgetProcessorExceptions
	 * within WidgetProcessorExceptions.
	 */

	public static WidgetProcessorException newException( Throwable cause ) {

		if ( cause instanceof WidgetProcessorException ) {
			return (WidgetProcessorException) cause;
		}

		return new WidgetProcessorException( cause );
	}

	/**
	 * Static constructor.
	 * <p>
	 * For consistency with <code>WidgetProcessorException.newException( Throwable )</code>.
	 */

	public static WidgetProcessorException newException( String message ) {

		return new WidgetProcessorException( message );
	}

	/**
	 * Static constructor.
	 * <p>
	 * For consistency with <code>WidgetProcessorException.newException( Throwable )</code>.
	 */

	public static WidgetProcessorException newException( String message, Throwable cause ) {

		return new WidgetProcessorException( message, cause );
	}

	//
	// Constructor
	//

	private WidgetProcessorException( String message ) {

		super( message );
	}

	private WidgetProcessorException( Throwable cause ) {

		super( cause );
	}

	private WidgetProcessorException( String message, Throwable cause ) {

		super( message, cause );
	}
}
