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
import org.metawidget.widgetbuilder.WidgetBuilder;

/**
 * @author Richard Kennard
 */

public class CompositeWidgetBuilder<W,M>
	implements WidgetBuilder<W,M>
{
	//
	// Private members
	//

	private WidgetBuilder<W,M>[]	mWidgetBuilders;

	//
	// Constructor
	//

	@SuppressWarnings("unchecked")
	public CompositeWidgetBuilder( CompositeWidgetBuilderConfig<W,M> config )
	{
		WidgetBuilder<W,M>[] widgetBuilders = config.getWidgetBuilders();

		// Must have at least one WidgetBuilder. At least two, really, but one can be useful
		// if we want to validate what the sub-WidgetBuilder is returning

		if ( widgetBuilders == null || widgetBuilders.length == 0 )
			throw InspectorException.newException( "CompositeWidgetBuilder needs at least one WidgetBuilder" );

		// Defensive copy

		mWidgetBuilders = new WidgetBuilder[widgetBuilders.length];
		System.arraycopy( widgetBuilders, 0, mWidgetBuilders, 0, widgetBuilders.length );
	}

	//
	// Public methods
	//

	@Override
	public W buildWidget( String elementName, Map<String, String> attributes, M metawidget )
		throws Exception
	{
		for( WidgetBuilder<W,M> widgetBuilder: mWidgetBuilders )
		{
			W widget = widgetBuilder.buildWidget( elementName, attributes, metawidget );

			if ( widget != null )
				return widget;
		}

		return null;
	}
}
