// Metawidget
//
// This library is dual licensed under both LGPL and a commercial
// license.
//
// LGPL: this library is free software; you can redistribute it and/or
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
//
// Commercial License: See http://metawidget.org for details

package org.metawidget.gwt.client.ui.layout;

import java.util.HashMap;
import java.util.Map;

import org.metawidget.gwt.client.ui.GwtMetawidget;
import org.metawidget.gwt.client.ui.GwtUtils;
import org.metawidget.gwt.client.ui.Stub;
import org.metawidget.layout.decorator.LayoutDecoratorConfig;

import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Convenience base class for LayoutDecorators wishing to decorate widgets based on changing
 * sections within Android Layouts.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public abstract class GwtNestedSectionLayoutDecorator
	extends org.metawidget.layout.decorator.NestedSectionLayoutDecorator<Widget, Panel, GwtMetawidget> {

	//
	// Constructor
	//

	protected GwtNestedSectionLayoutDecorator( LayoutDecoratorConfig<Widget, Panel, GwtMetawidget> config ) {

		super( config );
	}

	//
	// Protected methods
	//

	@Override
	protected String stripSection( Map<String, String> attributes ) {

		return GwtUtils.stripSection( attributes );
	}

	@Override
	protected State<Panel> getState( Panel container, GwtMetawidget metawidget ) {

		@SuppressWarnings( "unchecked" )
		Map<Widget, State<Panel>> stateMap = (Map<Widget, State<Panel>>) metawidget.getClientProperty( getClass() );

		if ( stateMap == null ) {
			stateMap = new HashMap<Widget, State<Panel>>();
			metawidget.putClientProperty( getClass(), stateMap );
		}

		State<Panel> state = stateMap.get( container );

		if ( state == null ) {
			state = new State<Panel>();
			stateMap.put( container, state );
		}

		return state;
	}

	@Override
	protected boolean isIgnored( Widget widget ) {

		return ( widget instanceof Stub && ( (Stub) widget ).getWidgetCount() == 0 );
	}
}
