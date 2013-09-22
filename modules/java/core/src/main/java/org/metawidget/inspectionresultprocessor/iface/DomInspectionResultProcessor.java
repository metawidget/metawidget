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
