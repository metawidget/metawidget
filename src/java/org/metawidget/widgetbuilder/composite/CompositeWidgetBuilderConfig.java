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

package org.metawidget.widgetbuilder.composite;

import java.util.List;

import org.metawidget.widgetbuilder.iface.WidgetBuilder;

/**
 * Configures a CompositeWidgetBuilder prior to use. Once instantiated, WidgetBuilders are
 * immutable.
 *
 * @author Richard Kennard
 */

public class CompositeWidgetBuilderConfig<W, M>
{
	//
	// Private members
	//

	private WidgetBuilder<W, M>[]	mWidgetBuilders;

	//
	// Public methods
	//

	public WidgetBuilder<W, M>[] getWidgetBuilders()
	{
		return mWidgetBuilders;
	}

	/**
	 * Sets the sub-WidgetBuilders the CompositeWidgetBuilder will call.
	 * <p>
	 * WidgetBuilders will be called in order.
	 *
	 * @return this, as part of a fluent interface
	 */

	public CompositeWidgetBuilderConfig<W, M> setWidgetBuilders( WidgetBuilder<W, M>... widgetBuilders )
	{
		mWidgetBuilders = widgetBuilders;

		// Fluent interface

		return this;
	}

	/**
	 * Sets the sub-Inspectors the CompositeInspector will call.
	 * <p>
	 * Inspectors will be called in order. CompositeInspector's merging algorithm preserves the
	 * element ordering of the first DOMs as new DOMs are merged in.
	 * <p>
	 * This overloaded form of the setter is useful for <code>metawidget.xml</code>.
	 *
	 * @return this, as part of a fluent interface
	 */

	@SuppressWarnings( "unchecked" )
	public CompositeWidgetBuilderConfig<W, M> setWidgetBuilders( List<WidgetBuilder<W, M>> widgetBuilders )
	{
		WidgetBuilder<W, M>[] widgetBuildersArray = new WidgetBuilder[widgetBuilders.size()];
		return setWidgetBuilders( widgetBuilders.toArray( widgetBuildersArray ) );
	}
}
