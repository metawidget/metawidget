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
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
