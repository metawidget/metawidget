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

package org.metawidget.iface;

/**
 * Interface to indicate a class (ie. an <code>Inspector</code>, a <code>Layout</code> etc) should
 * be considered immutable.
 * <p>
 * Immutable classes should:
 * <ul>
 * <li>not provide any setter methods or mutators
 * <li>mark all fields final and private
 * </ul>
 * <p>
 * Note: this interface is not an annotation, in order to support JDK 1.4.
 * <p>
 * <strong>This is an internal API and is subject to change. Clients should use one of its
 * subinterfaces (ie. <code>Inspector</code>, <code>Layout</code> etc) instead.</strong>
 *
 * @author Richard Kennard
 */

public interface Immutable {
	// Just a marker interface
}
