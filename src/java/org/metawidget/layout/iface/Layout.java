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

package org.metawidget.layout.iface;

import java.util.Map;

/**
 * Interface for Layouts.
 * <p>
 * Layouts must be immutable (or, at least, appear that way to clients. They can have caches or
 * configuration settings internally, as long as they are threadsafe). If they need to store state,
 * they should use the Metawidget passed to each method.
 *
 * @author Richard Kennard
 */

public interface Layout<W, M extends W>
{
	//
	// Methods
	//

	/**
	 * Event called at the start of the widget building process, before the
	 * <code>WidgetBuilder</code> is called. <code>Layout</code>s may wish to act on this event to
	 * initialize themselves ready for processing.
	 *
	 * @param metawidget
	 *            the parent Metawidget. Never null
	 */

	void onStartBuild( M metawidget );

	/**
	 * @param widget
	 *            the widget to layout. Never null
	 * @param elementName
	 *            XML node name of the business field. Typically 'entity', 'property' or 'action'.
	 *            Never null
	 * @param attributes
	 *            attributes of the widget to layout. Never null
	 * @param metawidget
	 *            the parent Metawidget. Never null
	 */

	void layoutChild( W widget, String elementName, Map<String, String> attributes, M metawidget );

	/**
	 * Event called at the end of widget building, after all widgets have been built and added to
	 * the <code>Layout</code>. <code>Layout</code>s may wish to act on this event to clean
	 * themselves up after processing.
	 *
	 * @param metawidget
	 *            the parent Metawidget. Never null
	 */

	void onEndBuild( M metawidget );
}
