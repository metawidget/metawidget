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

package org.metawidget.layout.composite;

import java.util.Map;

import org.metawidget.inspector.iface.InspectorException;
import org.metawidget.layout.iface.Layout;

/**
 * Delegates layout to one or more sub-Layouts.
 * <p>
 * Each sub-Layout in the list is invoked, in order, calling its <code>layoutChild</code>
 * method.
 * <p>
 * Note: the name <em>Composite</em>Layout refers to the Composite design pattern.
 *
 * @author Richard Kennard
 */

public class CompositeLayout<W, M extends W>
	implements Layout<W, M>
{
	//
	// Private members
	//

	/* package private */Layout<W, M>[]	mLayouts;

	//
	// Constructor
	//

	@SuppressWarnings( "unchecked" )
	public CompositeLayout( CompositeLayoutConfig<W, M> config )
	{
		Layout<W, M>[] Layouts = config.getLayouts();

		// Must have at least two Layouts

		if ( Layouts == null || Layouts.length == 0 )
			throw InspectorException.newException( "CompositeLayout needs at least two Layouts" );

		// Defensive copy

		mLayouts = new Layout[Layouts.length];
		System.arraycopy( Layouts, 0, mLayouts, 0, Layouts.length );
	}

	//
	// Public methods
	//

	@Override
	public void onStartBuild( M metawidget )
	{
		for ( Layout<W, M> layout : mLayouts )
		{
			layout.onStartBuild( metawidget );
		}
	}

	@Override
	public W layoutChild( W widget, String elementName, Map<String, String> attributes, M metawidget )
	{
		W widgetToLayout = widget;

		for ( Layout<W, M> layout : mLayouts )
		{
			widgetToLayout = layout.layoutChild( widgetToLayout, elementName, attributes, metawidget );

			if ( widgetToLayout == null )
				return null;
		}

		return widgetToLayout;
	}

	@Override
	public void onEndBuild( M metawidget )
	{
		for ( Layout<W, M> layout : mLayouts )
		{
			layout.onEndBuild( metawidget );
		}
	}
}
