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
