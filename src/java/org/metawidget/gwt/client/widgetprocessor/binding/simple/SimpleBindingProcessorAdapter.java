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

package org.metawidget.gwt.client.widgetprocessor.binding.simple;

/**
 * Adapter around business objects to support <code>SimpleBindingProcessor</code>.
 * <p>
 * All business objects used by <code>SimpleBindingProcessor</code> must be wrapped by a
 * <code>SimpleBindingProcessorAdapter</code>. The supplied <code>SimpleBindingProcessorAdapterGenerator</code>
 * automates the creation of adapters.
 *
 * @author Richard Kennard
 */

public interface SimpleBindingProcessorAdapter<T>
{
	//
	// Methods
	//

	Object getProperty( T object, String... property );

	Class<?> getPropertyType( T object, String... property );

	void setProperty( T object, Object value, String... property );

	void invokeAction( T object, String... action );
}
