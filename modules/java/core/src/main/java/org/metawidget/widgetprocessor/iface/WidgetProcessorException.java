// Metawidget
//
// This library is dual licensed under both LGPL and a commercial
// license.
//
// LGPL: this library is free software; you can redistribute it and/or
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
//
// Commercial License: See http://metawidget.org for details

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
