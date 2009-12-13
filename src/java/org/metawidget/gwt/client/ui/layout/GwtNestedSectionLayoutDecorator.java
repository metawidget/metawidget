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

package org.metawidget.gwt.client.ui.layout;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.metawidget.gwt.client.ui.GwtMetawidget;
import org.metawidget.gwt.client.ui.GwtUtils;
import org.metawidget.gwt.client.ui.Stub;
import org.metawidget.layout.decorator.LayoutDecoratorConfig;

import com.google.gwt.user.client.ui.Widget;

/**
 * Convenience base class for LayoutDecorators wishing to decorate widgets based on changing
 * sections within Android Layouts.
 *
 * @author Richard Kennard
 */

public abstract class GwtNestedSectionLayoutDecorator
	extends org.metawidget.layout.decorator.NestedSectionLayoutDecorator<Widget, GwtMetawidget>
{
	//
	// Constructor
	//

	protected GwtNestedSectionLayoutDecorator( LayoutDecoratorConfig<Widget, GwtMetawidget> config )
	{
		super( config );
	}

	//
	// Protected methods
	//

	@Override
	protected String stripSection( Map<String, String> attributes )
	{
		String sections = attributes.remove( SECTION );

		// (null means 'no change to current section')

		if ( sections == null )
			return null;

		List<String> sectionAsArray = GwtUtils.fromString( sections, ',' );

		switch ( sectionAsArray.size() )
		{
			// (empty String means 'end current section')

			case 0:
				return "";

			case 1:
				return sectionAsArray.get(0);

			default:
				String section = sectionAsArray.remove( 0 );
				attributes.put( SECTION, GwtUtils.toString( sectionAsArray, ',' ) );
				return section;
		}
	}

	@Override
	protected State<Widget> getState( Widget widget, GwtMetawidget metawidget )
	{
		@SuppressWarnings( "unchecked" )
		Map<Widget, State<Widget>> stateMap = (Map<Widget, State<Widget>>) metawidget.getClientProperty( getClass() );

		if ( stateMap == null )
		{
			stateMap = new HashMap<Widget, State<Widget>>();
			metawidget.putClientProperty( getClass(), stateMap );
		}

		State<Widget> state = stateMap.get( widget );

		if ( state == null )
		{
			state = new State<Widget>();
			stateMap.put( widget, state );
		}

		return state;
	}

	@Override
	protected boolean isEmptyStub( Widget widget )
	{
		return ( widget instanceof Stub && ((Stub) widget).getWidgetCount() == 0 );
	}
}
