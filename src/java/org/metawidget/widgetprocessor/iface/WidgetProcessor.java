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

package org.metawidget.widgetprocessor.iface;

import java.util.Map;

/**
 * Common interface implemented by all WidgetProcessors. WidgetProcessors allow arbitrary processing
 * of a widget at key points in its lifecycle.
 * <p>
 * WidgetProcessors must be threadsafe and immutable (or, at least, appear that way to clients. They
 * can have caches or configuration settings internally). If they need to store state, they should
 * use the Metawidget passed to each method.
 *
 * @author Richard Kennard
 */

public interface WidgetProcessor<W, M extends W>
{
	//
	// Methods
	//

	/**
	 * Called at the start of the widget building process, before any widgets have been built.
	 */

	void onStartBuild( M metawidget );

	/**
	 * Called after a widget has been built by the <code>WidgetBuilder</code>, and it is being added
	 * to the Metawidget.
	 *
	 * @return generally the same widget as was passed in to the first argument. Can be a different
	 *         widget if the WidgetProcessor wishes to do a substitution
	 */

	W onAdd( W widget, String elementName, Map<String, String> attributes, M metawidget );

	/**
	 * Called at the end of widget building, after all widgets have been built and added to the
	 * Metawidget.
	 */

	void onEndBuild( M metawidget );
}
