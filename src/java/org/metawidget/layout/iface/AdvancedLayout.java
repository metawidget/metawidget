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

/**
 * Common interface implemented by all AdvancedLayouts. AdvancedLayouts can hook into additional
 * lifecycle events.
 * <p>
 * There are advantages to keeping the vanilla <code>Layout</code> interface as a single-method
 * interface: First, it alleviates the need for a <code>BaseLayout</code> or
 * <code>LayoutAdapter</code> class (to provide default implementations for subclasses who don't
 * override all the methods); second, it makes <code>Layout</code> amenable to automatic function
 * objects (closures) in Java 7. This latter point is more of a concession to consistency with
 * <code>WidgetProcessor</code> and the other interfaces in the pipeline, but may still prove
 * useful.
 *
 * @author Richard Kennard
 */

public interface AdvancedLayout<W, C extends W, M extends C>
	extends Layout<W, C, M>
{
	//
	// Methods
	//

	/**
	 * Initialise the given container, using the given Metawidget to access additional services if
	 * needed (such as state saving).
	 *
	 * @param container
	 *            the container to layout. This is often the same as the given Metawidget
	 * @param metawidget
	 *            the parent Metawidget. Never null
	 */

	void startLayout( C container, M metawidget );

	/**
	 * Finish the given container, using the given Metawidget to access additional services if
	 * needed (such as state saving).
	 *
	 * @param container
	 *            the container to layout. This is often the same as the given Metawidget
	 * @param metawidget
	 *            the Metawidget to use to access additional services. Never null
	 */

	void endLayout( C container, M metawidget );
}
