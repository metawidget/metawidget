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

package org.metawidget.statically;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * @author Richard Kennard
 */

public interface StaticWidget {

	//
	// Methods
	//

	/**
	 * @return	the list of this widget's children. Never null
	 */

	List<StaticWidget> getChildren();

	void write( Writer writer )
		throws IOException;

	/**
	 * General-purpose storage area, rather like <code>JComponent.putClientProperty</code>.
	 */

	void putClientProperty( Object key, Object value );

	/**
	 * General-purpose storage area, rather like <code>JComponent.getClientProperty</code>.
	 */

	public <T> T getClientProperty( Object key );
}
