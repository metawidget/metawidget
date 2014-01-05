// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

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
