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

package org.metawidget.inspector.impl.actionstyle;

import java.util.Map;

import org.metawidget.iface.Immutable;

/**
 * Abstraction layer for retrieving actions from types.
 * <p>
 * Different environments have different approaches to defining what constitutes an 'action'. For
 * example, Metawidget supplies a <code>UiAction</code> annotation and the Spring AppFramework
 * supplies an <code>Action</code> annotation.
 * <p>
 * <code>ActionStyle</code>s must be immutable (or, at least, appear that way to clients. They can
 * have caches or configuration settings internally, as long as they are threadsafe).
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public interface ActionStyle
	extends Immutable {

	//
	// Methods
	//

	/**
	 * Gets the Actions for the given type.
	 * <p>
	 * Actions must be returned using a consistent ordering, so that both unit tests and
	 * <code>CompositeInspector</code> merging is consistent. If the underlying technology does not
	 * define an ordering, one must be imposed (eg. sorted alphabetically by name), even though this
	 * may later be overridden by other mechanisms (eg.
	 * <code>ComesAfterInspectionResultProcessor</code> sorts by <code>comes-after</code>).
	 *
	 * @return the actions for the given type. Never null.
	 */

	Map<String, Action> getActions( String type );
}
