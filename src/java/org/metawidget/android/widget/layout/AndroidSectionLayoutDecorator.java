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

package org.metawidget.android.widget.layout;

import java.util.Map;

import org.metawidget.android.widget.AndroidMetawidget;
import org.metawidget.android.widget.Stub;
import org.metawidget.layout.decorator.LayoutDecoratorConfig;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.LayoutUtils;

import android.view.View;

/**
 * Convenience base class for LayoutDecorators wishing to decorate widgets based on changing
 * sections within Android Layouts.
 *
 * @author Richard Kennard
 */

public abstract class AndroidSectionLayoutDecorator
	extends org.metawidget.layout.decorator.NestedSectionLayoutDecorator<View, AndroidMetawidget>
{
	//
	// Constructor
	//

	protected AndroidSectionLayoutDecorator( LayoutDecoratorConfig<View, AndroidMetawidget> config )
	{
		super( config );
	}

	//
	// Protected methods
	//

	@Override
	protected String stripSection( Map<String, String> attributes )
	{
		return LayoutUtils.stripSection( attributes );
	}

	@Override
	protected State<View> getState( View view, AndroidMetawidget metawidget )
	{
		@SuppressWarnings( "unchecked" )
		Map<View, State> stateMap = (Map<View, State>) metawidget.getClientProperty( getClass() );

		if ( stateMap == null )
		{
			stateMap = CollectionUtils.newHashMap();
			metawidget.putClientProperty( getClass(), stateMap );
		}

		@SuppressWarnings( "unchecked" )
		State<View> state = stateMap.get( view );

		if ( state == null )
		{
			state = new State<View>();
			stateMap.put( view, state );
		}

		return state;
	}

	@Override
	protected boolean isEmptyStub( View view )
	{
		return ( view instanceof Stub && ((Stub) view).getChildCount() == 0 );
	}
}
