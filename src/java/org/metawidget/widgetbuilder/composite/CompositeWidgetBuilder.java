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

import java.util.Map;

import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

/**
 * Delegates widget building to one or more sub-WidgetBuilders.
 * <p>
 * Each sub-WidgetBuilder in the list is invoked, in order, calling its <code>buildWidget</code>
 * method. The first non-null result is returned. If all sub-WidgetBuilders return null, null is
 * returned (the parent Metawidget will generally instantiate a nested Metawidget in this case).
 * <p>
 * Note: the name <em>Composite</em>WidgetBuilder refers to the Composite design pattern.
 *
 * @author Richard Kennard
 */

public class CompositeWidgetBuilder<W, M extends W>
	implements WidgetBuilder<W, M>
{
	//
	// Private members
	//

	/* package private */WidgetBuilder<W, M>[]	mWidgetBuilders;

	//
	// Constructor
	//

	@SuppressWarnings( "unchecked" )
	public CompositeWidgetBuilder( CompositeWidgetBuilderConfig<W, M> config )
	{
		WidgetBuilder<W, M>[] widgetBuilders = config.getWidgetBuilders();

		// Must have at least two WidgetBuilders

		if ( widgetBuilders == null || widgetBuilders.length == 0 )
			throw InspectorException.newException( "CompositeWidgetBuilder needs at least two WidgetBuilders" );

		// Defensive copy

		mWidgetBuilders = new WidgetBuilder[widgetBuilders.length];
		System.arraycopy( widgetBuilders, 0, mWidgetBuilders, 0, widgetBuilders.length );
	}

	//
	// Public methods
	//

	public W buildWidget( String elementName, Map<String, String> attributes, M metawidget )
	{
		for ( WidgetBuilder<W, M> widgetBuilder : mWidgetBuilders )
		{
			W widget = widgetBuilder.buildWidget( elementName, attributes, metawidget );

			if ( widget != null )
				return widget;
		}

		return null;
	}

	/**
	 * Exposed for <code>getValue</code> calls.
	 */

	public WidgetBuilder<W, M>[] getWidgetBuilders()
	{
		// Defensive copy

		@SuppressWarnings( "unchecked" )
		WidgetBuilder<W, M>[] widgetBuilders = new WidgetBuilder[mWidgetBuilders.length];
		System.arraycopy( mWidgetBuilders, 0, widgetBuilders, 0, mWidgetBuilders.length );

		return widgetBuilders;
	}
}
