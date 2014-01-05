// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

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
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
