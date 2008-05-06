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

package org.metawidget.gwt.client.binding;

/**
 * GWT interface for binding to domain objects.
 * <p>
 * Because of its Java-to-JavaScript compiler, GWT places some limitations on dynamic reflection and
 * instantiation of types. Therefore, GwtMetawidget requires clients to supply an explicit interface
 * through which to execute binding calls. In most cases, clients can use <code>BindingAdapterGenerator</code>
 * to automatically generate this as a secondary class.
 *
 * @author Richard Kennard
 */

public interface Binding
{
	//
	//
	// Methods
	//
	//

	Object getProperty( String property );
}
