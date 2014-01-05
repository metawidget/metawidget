// Metawidget
//
// For historical reasons, this file is licensed under the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html).
//
// Most other files in Metawidget are licensed under both the
// LGPL/EPL and a commercial license. See http://metawidget.org
// for details.

package org.metawidget.vaadin.ui.layout;

import java.util.HashMap;
import java.util.Map;

import org.metawidget.layout.decorator.LayoutDecoratorConfig;
import org.metawidget.util.LayoutUtils;
import org.metawidget.vaadin.ui.Stub;
import org.metawidget.vaadin.ui.VaadinMetawidget;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;

/**
 * Convenience base class for LayoutDecorators
 *
 * @author Loghman Barari
 */

public abstract class VaadinNestedSectionLayoutDecorator
	extends org.metawidget.layout.decorator.NestedSectionLayoutDecorator<Component, ComponentContainer, VaadinMetawidget> {

	//
	// Constructor
	//

	protected VaadinNestedSectionLayoutDecorator( LayoutDecoratorConfig<Component, ComponentContainer, VaadinMetawidget> config ) {

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
	protected State<ComponentContainer> getState( ComponentContainer container, VaadinMetawidget metawidget ) {

		@SuppressWarnings( "unchecked" )
		Map<Component, State<ComponentContainer>> stateMap = (Map<Component, State<ComponentContainer>>) metawidget.getClientProperty( getClass() );

		if ( stateMap == null ) {
			stateMap = new HashMap<Component, State<ComponentContainer>>();
			metawidget.putClientProperty( getClass(), stateMap );
		}

		State<ComponentContainer> state = stateMap.get( container );

		if ( state == null ) {
			state = new State<ComponentContainer>();
			stateMap.put( container, state );
		}

		return state;
	}

	@Override
	protected boolean isIgnored( Component component ) {

		return ( component instanceof Stub && !( (Stub) component ).getComponentIterator().hasNext() );
	}
}
