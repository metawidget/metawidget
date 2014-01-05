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

package org.metawidget.widgetbuilder.iface;

import org.metawidget.iface.MetawidgetException;

/**
 * Any exception that occurs during widget building.
 * 
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public final class WidgetBuilderException
	extends MetawidgetException {

	//
	// Public statics
	//

	/**
	 * Static constructor.
	 * <p>
	 * Using static constructor methods prevents unnecessarily nesting WidgetBuilderExceptions
	 * within WidgetBuilderExceptions.
	 */

	public static WidgetBuilderException newException( Throwable cause ) {

		if ( cause instanceof WidgetBuilderException ) {
			return (WidgetBuilderException) cause;
		}

		return new WidgetBuilderException( cause );
	}

	/**
	 * Static constructor.
	 * <p>
	 * For consistency with <code>WidgetBuilderException.newException( Throwable )</code>.
	 */

	public static WidgetBuilderException newException( String message ) {

		return new WidgetBuilderException( message );
	}

	/**
	 * Static constructor.
	 * <p>
	 * For consistency with <code>WidgetBuilderException.newException( Throwable )</code>.
	 */

	public static WidgetBuilderException newException( String message, Throwable cause ) {

		return new WidgetBuilderException( message, cause );
	}

	//
	// Constructor
	//

	private WidgetBuilderException( String message ) {

		super( message );
	}

	private WidgetBuilderException( Throwable cause ) {

		super( cause );
	}

	private WidgetBuilderException( String message, Throwable cause ) {

		super( message, cause );
	}
}
