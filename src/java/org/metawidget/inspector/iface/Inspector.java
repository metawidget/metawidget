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

package org.metawidget.inspector.iface;

import org.metawidget.iface.Immutable;

/**
 * Common interface implemented by all Inspectors. Inspectors decouple the process of generating
 * inspection results out of back-end metadata.
 * <p>
 * Inspectors must be immutable (or, at least, appear that way to clients. They can have caches or
 * configuration settings internally, as long as they are threadsafe).
 * <p>
 * <h3>Note to Implementors</h3>
 * <p>
 * This interface is designed to work for the <em>majority</em> of use cases, but not <em>every</em>
 * use case. Inevitably there are tradeoffs. For example, the <code>toInspect</code> parameter is
 * redundant for XML-based Inspectors (note, however, that the <code>type</code> parameter is
 * <em>not</em> redundant for Object-based Inspectors).
 * <p>
 * In particular, this interface does not pass an <code>M metawidget</code> or any other kind of
 * 'helper context' as the other interfaces do. This is because Inspectors are meant to exist independent
 * of any particular UI framework. Indeed, they can exist on back-end tiers where no UI framework is
 * available at all. This seems to work for most use cases encountered so far. Theoretically, though, if you
 * find yourself needing access to a context, consider:
 * <ul>
 * <li>passing it in a Thread local (<code>JspAnnotationInspector</code> does this); or</li>
 * <li>passing an <code>Object[]</code> containing the object to be inspected <em>and</em> the context to <code>toInspect</code>, and
 * build something like <code>CompositeInspector</code> to strip that context back out before calling the regular <code>Inspectors</code>; or</li>
 * <li>use an <code>InspectionResultProcessor</code>.</li>
 * </ul>
 *
 * @author Richard Kennard
 */

public interface Inspector
	extends Immutable
{
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
