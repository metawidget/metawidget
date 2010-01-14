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
 *
 * @param <E>
 *            Element class at the root of the inspection result (typically org.w3d.com.Element)
 * @param <M>
 *            Metawidget that supports this InspectionResultProcessor
 * @author Richard Kennard
 */

public interface InspectionResultProcessor<E, M>
	extends Immutable
{
	//
	// Methods
	//

	/**
	 * @param inspectionResult
	 *            the inspection result to process. Never null
	 * @return generally the original inspection result (as passed in to the first argument). Can be
	 *         a different inspection result if the InspectionResultProcessor wishes to substitute
	 *         the original inspection result for another. Can be null if the
	 *         InspectionResultProcessor wishes to cancel the inspection
	 */

	E processInspectionResult( E inspectionResult, M metawidget );
}
