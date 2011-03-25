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

package org.metawidget.widgetbuilder.iface;

import java.util.Map;

import org.metawidget.iface.Immutable;

/**
 * Common interface implemented by all WidgetBuilders. WidgetBuilders decouple the process of
 * choosing widgets based on inspection results.
 * <p>
 * WidgetBuilders must be immutable (or, at least, appear that way to clients. They can have caches
 * or configuration settings internally, as long as they are threadsafe).
 * <p>
 * Note: the name Widget<em>Builder</em> refers to the Builder design pattern.
 *
 * @param <W>
 *            base class of widgets that this WidgetBuilder builds
 * @param <M>
 *            Metawidget that supports this WidgetBuilder
 * @author Richard Kennard
 */

public interface WidgetBuilder<W, M extends W>
	extends Immutable {

	//
	// Methods
	//

	/**
	 * Builds the most appropriate widget for this business field.
	 *
	 * @param elementName
	 *            XML node name of the business field. Typically 'entity', 'property' or 'action'.
	 *            Never null
	 * @param attributes
	 *            attributes of the business field to build a widget for. Never null. This Map is
	 *            modifiable - changes will be passed to subsequent WidgetBuilders, WidgetProcessors
	 *            and Layouts
	 * @param metawidget
	 *            the parent Metawidget. Never null
	 * @return the built widget. To suppress a field, return a Stub. To defer to the next
	 *         WidgetBuilder in the pipeline, return null. If there are no more WidgetBuilders in
	 *         the pipeline, will create a nested Metawidget. This approach (return Stub for no
	 *         field, null for nested Metawidget) as opposed to the other way around (return null
	 *         for no field, return Metawidget for nested Metawidget) works better for those UI
	 *         frameworks that cannot instatiate widgets without adding them to containers (eg. SWT)
	 */

	W buildWidget( String elementName, Map<String, String> attributes, M metawidget );
}
