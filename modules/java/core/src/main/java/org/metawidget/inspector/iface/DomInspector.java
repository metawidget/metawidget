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
