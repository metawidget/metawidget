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

package org.metawidget.inspector.iface;

/**
 * Inspects the given Object and type and returns the result as a DOM Element.
 * <p>
 * <code>DomInspector</code> is an <em>optional</em> interface that enables an optimization. Like
 * most optimizations it unfortunately adds some complexity. The basic idea is that, whilst XML is a
 * great lowest-common-denominator for the <code>Inspector</code> interface (perfect for passing
 * between disparate technologies and tiers in order to allow maximum flexibility in what can be
 * inspected) serializing to and from XML strings is expensive.
 * <p>
 * Most Inspectors maintain their inspection results internally in a DOM. This interface allows them
 * to expose that DOM directly, rather than serializing it to a String, whereupon either the
 * Metawidget or a <code>CompositeInspector</code> must typically deserialize it back again.
 * <p>
 * If your <code>Inspector</code> extends <code>BaseObjectInspector</code> or
 * <code>BaseXmlInspector</code>, this optimization is implemented for you.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public interface DomInspector<E>
	extends Inspector {

	//
	// Methods
	//

	/**
	 * Optimized verison of <code>inspect</code> that avoids DOM serialization/deserialization.
	 */

	E inspectAsDom( Object toInspect, String type, String... names );
}
