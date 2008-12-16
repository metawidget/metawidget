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

package org.metawidget.swing.actionbinding;

import java.awt.Component;
import java.util.Map;

/**
 * Interface for automatic binding of actions.
 * <p>
 * Swing already defines <code>Action</code> classes (such as <code>AbstractAction</code>) for
 * binding UI buttons to concrete Java objects. Metawidget makes this pluggable for those wanting to
 * bind UI buttons to other targets (such as RPC calls).
 * <p>
 * Implementations need not be Thread-safe.
 *
 * @author Richard Kennard
 */

public interface ActionBinding
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

	void bind( Component component, Map<String, String> attributes, String path );
}
