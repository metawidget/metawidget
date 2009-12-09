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

package org.metawidget.faces.component.layout;

import java.util.Map;

import javax.faces.component.UIComponent;

import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.UIStub;
import org.metawidget.layout.decorator.LayoutDecoratorConfig;
import org.metawidget.util.CollectionUtils;

/**
 * Convenience base class for LayoutDecorators wishing to decorate widgets based on changing
 * sections within JSF Layouts.
 *
 * @author Richard Kennard
 */

public abstract class UIComponentNestedSectionLayoutDecorator
	extends org.metawidget.layout.decorator.NestedSectionLayoutDecorator<UIComponent, UIMetawidget>
{
	//
	// Constructor
	//

	protected UIComponentNestedSectionLayoutDecorator( LayoutDecoratorConfig<UIComponent, UIMetawidget> config )
	{
		super( config );
	}

	//
	// Protected methods
	//

	@Override
	protected State<UIComponent> getState( UIComponent container, UIMetawidget metawidget )
	{
		@SuppressWarnings( "unchecked" )
		Map<UIComponent, State> stateMap = (Map<UIComponent, State>) metawidget.getClientProperty( getClass() );

		if ( stateMap == null )
		{
			stateMap = CollectionUtils.newHashMap();
			metawidget.putClientProperty( getClass(), stateMap );
		}

		@SuppressWarnings( "unchecked" )
		State<UIComponent> state = stateMap.get( container );

		if ( state == null )
		{
			state = new State<UIComponent>();
			stateMap.put( container, state );
		}

		return state;
	}

	@Override
	protected boolean isEmptyStub( UIComponent component )
	{
		return ( component instanceof UIStub && component.getChildren().isEmpty() );
	}
}
