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
	 * Initialise the given container, using the given Metawidget to access additional services if needed (such as state saving)
	 * <p>
	 * Event called at the start of the widget building process, before the
	 * <code>WidgetBuilder</code> is called. <code>Layout</code>s may wish to act on this event to
	 * initialize themselves ready for processing.
	 *
	 * @param container
	 *            the container to layout. This is often the same as the given Metawidget
	 * @param metawidget
	 *            the parent Metawidget. Never null
	 */

	void startLayout( W container, M metawidget );

	/**
	 * Layout the given widget within the given container, using the given elementName and attributes
	 * as a guide and the given Metawidget to access additional services if needed (such as state saving)
	 *
	 * @param widget
	 *            the widget to layout. Never null
	 * @param elementName
	 *            XML node name of the business field. Typically 'entity', 'property' or 'action'.
	 *            Never null
	 * @param attributes
	 *            attributes of the widget to layout. Never null
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
	// 2. Attributes for the components had to be attached somehow (ie. putClientProperty, or wrapped in a Stub)
	// 3. elementNames for the components had to be attached somehow
	// 4. It 'felt' weird having a WidgetProcessor doing Layout stuff
	//
	// We finally settled on having a container W and a DelegateLayout
	//
	void layoutWidget( W widget, String elementName, Map<String, String> attributes, W container, M metawidget );

	/**
	 * Finish the given container, using the given Metawidget to access additional services if needed (such as state saving)
	 *
	 * @param container
	 *            the container to layout. This is often the same as the given Metawidget
	 * @param metawidget
	 *            the Metawidget to use to access additional services. Never null
	 */

	void endLayout( W container, M metawidget );
}
