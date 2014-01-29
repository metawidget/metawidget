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

package org.metawidget.android.widget.layout;

import java.util.Map;

import org.metawidget.android.widget.AndroidMetawidget;
import org.metawidget.android.widget.Stub;
import org.metawidget.layout.decorator.LayoutDecoratorConfig;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.LayoutUtils;

import android.view.View;
import android.view.ViewGroup;

/**
 * Convenience base class for LayoutDecorators wishing to decorate widgets based on changing
 * sections within Android Layouts.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public abstract class AndroidNestedSectionLayoutDecorator
	extends org.metawidget.layout.decorator.NestedSectionLayoutDecorator<View, ViewGroup, AndroidMetawidget> {

	//
	// Constructor
	//

	protected AndroidNestedSectionLayoutDecorator( LayoutDecoratorConfig<View, ViewGroup, AndroidMetawidget> config ) {

		super( config );
	}

	//
	// Protected methods
	//

	@Override
	protected String stripSection( Map<String, String> attributes ) {

		return LayoutUtils.stripSection( attributes );
	}

	@Override
	protected State<ViewGroup> getState( ViewGroup view, AndroidMetawidget metawidget ) {

		@SuppressWarnings( "unchecked" )
		Map<View, State<ViewGroup>> stateMap = (Map<View, State<ViewGroup>>) metawidget.getClientProperty( getClass() );

		if ( stateMap == null ) {
			stateMap = CollectionUtils.newHashMap();
			metawidget.putClientProperty( getClass(), stateMap );
		}

		State<ViewGroup> state = stateMap.get( view );

		if ( state == null ) {
			state = new State<ViewGroup>();
			stateMap.put( view, state );
		}

		return state;
	}

	@Override
	protected boolean isIgnored( View view ) {

		return view instanceof Stub && ( (Stub) view ).getChildCount() == 0;
	}
}
