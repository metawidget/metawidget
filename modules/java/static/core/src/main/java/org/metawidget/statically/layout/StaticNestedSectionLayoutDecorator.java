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

package org.metawidget.statically.layout;

import java.util.Map;

import org.metawidget.layout.decorator.LayoutDecoratorConfig;
import org.metawidget.statically.StaticMetawidget;
import org.metawidget.statically.StaticWidget;
import org.metawidget.util.LayoutUtils;

/**
 * Convenience base class for LayoutDecorators wishing to decorate widgets based on changing
 * sections within Swing Layouts.
 *
 * @author Richard Kennard
 */

public abstract class StaticNestedSectionLayoutDecorator
	extends org.metawidget.layout.decorator.NestedSectionLayoutDecorator<StaticWidget, StaticWidget, StaticMetawidget> {

	//
	// Constructor
	//

	protected StaticNestedSectionLayoutDecorator( LayoutDecoratorConfig<StaticWidget, StaticWidget, StaticMetawidget> config ) {

		super( config );
	}

	//
	// Public methods
	//

	@Override
	public void startContainerLayout( StaticWidget container, StaticMetawidget metawidget ) {

		super.startContainerLayout( container, metawidget );
		container.putClientProperty( getClass(), null );
	}

	//
	// Protected methods
	//

	@Override
	protected String stripSection( Map<String, String> attributes ) {

		return LayoutUtils.stripSection( attributes );
	}

	@Override
	protected State<StaticWidget> getState( StaticWidget container, StaticMetawidget metawidget ) {

		@SuppressWarnings( "unchecked" )
		State<StaticWidget> state = (State<StaticWidget>) container.getClientProperty( getClass() );

		if ( state == null ) {
			state = new State<StaticWidget>();
			container.putClientProperty( getClass(), state );
		}

		return state;
	}

	@Override
	protected boolean isIgnored( StaticWidget component ) {

		return false;
	}
}
