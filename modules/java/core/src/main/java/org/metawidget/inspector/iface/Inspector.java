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

package org.metawidget.inspector.iface;

import org.metawidget.iface.Immutable;

/**
 * Common interface implemented by all Inspectors. Inspectors decouple the process of generating
 * inspection results out of back-end metadata.
 * <p>
 * Inspectors must be immutable (or, at least, appear that way to clients. They can have caches or
 * configuration settings internally, as long as they are threadsafe).
 * <p>
 * This interface does not pass an <code>M metawidget</code> or any other kind of 'helper context'
 * as the other interfaces do. This is because Inspectors need to exist independent of any
 * particular UI framework. Indeed, they can exist on back-end tiers where no UI framework is
 * available at all. If you find yourself needing access to a context, consider using an
 * <code>InspectionResultProcessor</code>.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public interface Inspector
	extends Immutable {

	//
	// Methods
	//

	/**
	 * Inspect the given Object according to the given path, and return the result as a String
	 * conforming to inspection-result-1.0.xsd.
	 * <p>
	 * Note: the method returns a String, rather than a DOM, to support the use of hetergenous
	 * technologies between the Inspectors and the Metawidgets. For example, GwtMetawidget is
	 * written in JavaScript but its Inspectors are written in Java.
	 *
	 * @param toInspect
	 *            runtime object to inspect. May be null
	 * @param type
	 *            match type attribute in inspection-result.xml
	 * @param names
	 *            match name attributes under type
	 * @return XML conforming to inspection-result-1.0.xsd
	 */

	String inspect( Object toInspect, String type, String... names );
}
