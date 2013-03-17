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

package org.metawidget.inspectionresultprocessor.iface;

import org.metawidget.iface.MetawidgetException;

/**
 * Any exception that occurs during inspection result processing.
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
