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

/**
 * Processes the given inspection result as a DOM Element.
 * <p>
 * <code>DomInspectionResultProcessor</code> is an <em>optional</em> interface that enables an
 * optimization. Like most optimizations it unfortunately adds some complexity. The basic idea is
 * that, whilst XML is a great lowest-common-denominator for the
 * <code>InspectionResultProcessor</code> interface (perfect for platform-neutral implementations)
 * serializing to and from XML strings is expensive.
 * <p>
 * Most InspectionResultProcessors maintain their inspection results internally in a DOM. This
 * interface allows them to expose that DOM directly, rather than serializing it to a String,
 * whereupon the Metawidget must typically deserialize it back again.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public interface DomInspectionResultProcessor<E, M>
	extends InspectionResultProcessor<M> {

	//
	// Methods
	//

	/**
	 * Optimized verison of <code>processInspectionResult</code> that avoids DOM
	 * serialization/deserialization.
	 */

	E processInspectionResultAsDom( E inspectionResult, M metawidget, Object toInspect, String type, String... names );
}
