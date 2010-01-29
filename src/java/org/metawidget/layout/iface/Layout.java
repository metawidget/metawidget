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

import org.metawidget.iface.Immutable;

/**
 * Interface for Layouts.
 * <p>
 * Layouts must be immutable (or, at least, appear that way to clients. They can have caches or
 * configuration settings internally, as long as they are threadsafe). If they need to store state,
 * they should use the Metawidget passed to each method.
 * <p>
 * Note: WidgetProcessors are an example of the Strategy design pattern.
 *
 * @param <W>
 *            base class of widgets that this Layout lays out
 * @param <C>
 *            base class of container widgets. Many UI frameworks make a distinction between
 *            'container widgets' (ie. Panels) and widgets that cannot contain child controls (ie.
 *            TextBox). For frameworks that don't make such a distinction, W and C can be the same
 * @param <M>
 *            Metawidget that supports this Layout
 * @author Richard Kennard
 */

public interface Layout<W, C extends W, M extends C>
	extends Immutable
{
	//
	// Methods
	//

	/**
	 * Layout the given widget within the given container, using the given elementName and
	 * attributes as a guide and the given Metawidget to access additional services if needed (such
	 * as state saving)
	 *
	 * @param widget
	 *            the widget to layout. Never null
	 * @param elementName
	 *            XML node name of the business field. Typically 'entity', 'property' or 'action'.
	 *            Never null
	 * @param attributes
	 *            attributes of the widget to layout. Never null. This Map is modifiable - changes
	 *            will be passed to subsequent WidgetProcessors and Layouts
	 * @param container
	 *            the container to add to. This is often the same as the given Metawidget
	 * @param metawidget
	 *            the Metawidget to use to access additional services. Never null
	 */

	// Note: we explored having layoutChild return W (see SVN), and then having a CompositeLayout
	// class which could combine multiple Layouts such as a TabbedPaneLayout and a GridBagLayout.
	// This was problematic because:
	//
	// 1. Layouts tend to have side effects (ie. they add widgets to themselves) so it wasn't
	// clear what should happen if someone tries to combine, say, a GridBagLayout with a MigLayout.
	// 2. each Layout generally expects itself to be the 'end point' of the pipeline.
	// 3. returning W makes the Layout interface identical to the WidgetProcessor interface.
	//
	// We tried instead making TabbedPaneLayout into TabbedPaneProcessor (see SVN). This was also
	// problematic because:
	//
	// 1. Nested sections had to be handled as nested, partially-initalised Metawidgets which could
	// then use their chosen Layout (eg. GridBagLayout)
	// 2. Attributes for the components had to be attached somehow (ie. putClientProperty, or
	// wrapped in a Stub)
	// 3. elementNames for the components had to be attached somehow
	// 4. It 'felt' weird having a WidgetProcessor doing Layout stuff
	//
	// We finally settled on having a container W and a LayoutDecorator
	//
	void layoutWidget( W widget, String elementName, Map<String, String> attributes, C container, M metawidget );
}
