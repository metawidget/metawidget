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

package org.metawidget.inspectionresultprocessor.iface;

import org.metawidget.iface.Immutable;

/**
 * Common interface implemented by all InspectionResultProcessors. InspectionResultProcessors allow
 * arbitrary processing of the inspection result before it is given to the WidgetBuilder.
 * <p>
 * InspectionResultProcessors must be immutable (or, at least, appear that way to clients. They can
 * have caches or configuration settings internally, as long as they are threadsafe). If they need
 * to store state, they should use the Metawidget passed to each method.
 * <p>
 * Processing an inspection result can be useful in a variety of cases. For example, you can sort
 * business object properties based on a defined ordering, or choose only certain properties based
 * on the UI screen being displayed. In more advanced cases, you could 'unpack' an inspection result
 * that contained nested entities so that you could, say, iterate over a Collection.
 * <p>
 * Note: InspectionResultProcessors are an example of the Strategy design pattern.
 *
 * @param <M>
 *            Metawidget that supports this InspectionResultProcessor
 * @author Richard Kennard
 */

public interface InspectionResultProcessor<M>
	extends Immutable {

	//
	// Methods
	//

	/**
	 * Process the given inspection result in context of the given Metawidget.
	 *
	 * @param inspectionResult
	 *            the inspection result to process, as XML conforming to inspection-result-1.0.xsd.
	 *            Never null
	 * @return the processed inspection result, as XML conforming to inspection-result-1.0.xsd. Can
	 *         be null if the InspectionResultProcessor wishes to cancel the inspection
	 */

	String processInspectionResult( String inspectionResult, M metawidget );
}
