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

package org.metawidget.statically.layout;

import java.util.Map;

import org.metawidget.layout.iface.Layout;
import org.metawidget.layout.iface.LayoutException;
import org.metawidget.statically.StaticMetawidget;
import org.metawidget.statically.StaticWidget;

/**
 * @author Richard Kennard
 */

public class SimpleLayout
	implements Layout<StaticWidget, StaticWidget, StaticMetawidget> {

	//
	// Public methods
	//

	public void layoutWidget( StaticWidget component, String elementName, Map<String, String> attributes, StaticWidget container, StaticMetawidget metawidget ) {

		try {
			component.write( metawidget.getWriter() );
		} catch ( Exception e ) {
			throw LayoutException.newException( e );
		}
	}
}
