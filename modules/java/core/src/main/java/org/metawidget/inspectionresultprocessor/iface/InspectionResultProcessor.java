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
 * domain object properties based on a defined ordering, or choose only certain properties based
 * on the UI screen being displayed. In more advanced cases, you could 'unpack' an inspection result
 * that contained nested entities so that you could, say, iterate over a Collection.
 * <p>
 * Note: InspectionResultProcessors are an example of the Strategy design pattern.
 *
 * @param <M>
 *            Metawidget that supports this InspectionResultProcessor
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
	 * @param metawidget
	 *            the parent Metawidget. Never null. May be useful to help processing
	 * @param toInspect
	 *            the Object being inspected. May be useful to help processing
	 * @param type
	 *            the type being inspected. May be useful to help processing
	 * @param names
	 *            the names being inspected. May be useful to help processing
	 * @return the processed inspection result, as XML conforming to inspection-result-1.0.xsd. Can
	 *         be null if the InspectionResultProcessor wishes to cancel the inspection
	 */

	String processInspectionResult( String inspectionResult, M metawidget, Object toInspect, String type, String... names );
}
