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

package org.metawidget.gwt.client.inspector;

import java.io.Serializable;

/**
 * Common interface implemented by all GWT Inspectors.
 * <p>
 * GWT-flavoured version of the <code>Inspector</code> interface, that returns a String rather
 * than a <code>org.w3c.dom.Document</code> (because
 * <code>com.google.gwt.xml.client.Document</code> is not Serializable).
 * <p>
 * Implementors must be threadsafe, and must appear to clients to be immutable. Internally, however,
 * they can have state (such as caches or configuration settings).
 *
 * @author Richard Kennard
 */

public interface GwtInspector
{
	/**
	 * Inspect the given Object according to the given path, and return the result as a DOM
	 * conforming to inspection-result-1.0.xsd.
	 * <p>
	 *
	 * @param toInspect
	 *            runtime object to inspect. May be null
	 * @param type
	 *            match type attribute in inspection-result.xml
	 * @param names
	 *            match name attributes under type
	 * @return a DOM conforming to inspection-result-1.0.xsd
	 */

	String inspect( Serializable toInspect, String type, String[] names )
		throws Exception;
}
