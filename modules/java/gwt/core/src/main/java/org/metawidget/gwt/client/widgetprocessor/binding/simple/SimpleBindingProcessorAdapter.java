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

package org.metawidget.gwt.client.widgetprocessor.binding.simple;

/**
 * Adapter around domain objects to support <code>SimpleBindingProcessor</code>.
 * <p>
 * All domain objects used by <code>SimpleBindingProcessor</code> must be wrapped by a
 * <code>SimpleBindingProcessorAdapter</code>. The supplied
 * <code>SimpleBindingProcessorAdapterGenerator</code> automates the creation of adapters.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public interface SimpleBindingProcessorAdapter<T> {

	//
	// Methods
	//

	Object getProperty( T object, String... property );

	Class<?> getPropertyType( T object, String... property );

	void setProperty( T object, Object value, String... property );

	void invokeAction( T object, String... action );
}
