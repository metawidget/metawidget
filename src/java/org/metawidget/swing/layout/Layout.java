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

package org.metawidget.swing.layout;

import java.awt.Component;
import java.util.Map;

/**
 * Interface for Swing-based layouts.
 * <p>
 * Layouts must be threadsafe and immutable (or, at least, appear that way to clients. They can have
 * caches or configuration settings internally). If they need to store state, they should use the
 * Metawidget passed to each method and <code>SwingMetawidget.putClientProperty</code>.
 *
 * @author Richard Kennard
 */

public interface Layout
{
	//
	// Methods
	//

	void layoutBegin();

	void layoutChild( Component component, Map<String, String> attributes );

	void layoutEnd();
}
