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

package org.metawidget.widgetbuilder.iface;

/**
 * Common interface implemented by all AdvancedWidgetBuilders. AdvancedWidgetBuilders can hook
 * into additional lifecycle events.
 * <p>
 * There are advantages to keeping the vanilla <code>WidgetBuilder</code> interface as a
 * single-method interface: First, it alleviates the need for a <code>BaseWidgetBuilder</code> or
 * <code>WidgetBuilderAdapter</code> class (to provide default implementations for subclasses who
 * don't override all the methods); second, it makes <code>WidgetBuilder</code> an example of the
 * Strategy pattern; third, it makes <code>WidgetBuilder</code> amenable to automatic function
 * objects (part of closures in Java 7).
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public interface AdvancedWidgetBuilder<W, M extends W>
	extends WidgetBuilder<W, M> {

	//
	// Methods
	//

	/**
	 * Event called at the start of the widget building process, before any
	 * <code>WidgetBuilder</code>s are called. <code>WidgetBuilder</code>s may wish to act on this
	 * event to initialize themselves ready for processing. This event is only called once per
	 * inspection, not once per widget built.
	 *
	 * @param metawidget
	 *            the parent Metawidget. Never null
	 */

	void onStartBuild( M metawidget );

	/**
	 * Event called at the end of widget building, after all widgets have been built and added to
	 * the <code>Layout</code>. <code>WidgetBuilders</code>s may wish to act on this event to clean
	 * themselves up after processing. This event is only called once per inspection, not once per
	 * widget built.
	 *
	 * @param metawidget
	 *            the parent Metawidget. Never null
	 */

	void onEndBuild( M metawidget );
}
