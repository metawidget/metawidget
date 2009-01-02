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

package org.metawidget.gwt.client.propertybinding;

import java.util.Map;

import com.google.gwt.user.client.ui.Widget;

/**
 * Interface for automatic, two-way binding of properties.
 *
 * @author Richard Kennard
 */

public interface PropertyBinding
{
	//
	// Methods
	//

	/**
	 * Bind the given Widget to the given 'path of names' within the source Object.
	 *
	 * @param widget
	 *            the widget to bind to
	 * @param attributes
	 *            metadata of the property being bound
	 * @param path
	 *            path to bind to (can be parsed using PathUtils.parsePath)
	 */

	void bindProperty( Widget widget, Map<String, String> attributes, String path );

	/**
	 * Update bound values in the Widgets from the source Object.
	 */

	void rebindProperties();

	/**
	 * Save bound values from the Widgets back to the source Object.
	 */

	void saveProperties();

	/**
	 * Unbind and release all resources.
	 */

	void unbindProperties();
}
