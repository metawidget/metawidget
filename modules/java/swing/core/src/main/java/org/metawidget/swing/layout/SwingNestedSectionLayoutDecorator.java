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

package org.metawidget.swing.layout;

import java.util.Map;

import javax.swing.JComponent;

import org.metawidget.layout.decorator.LayoutDecoratorConfig;
import org.metawidget.swing.Stub;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.LayoutUtils;

/**
 * Convenience base class for LayoutDecorators wishing to decorate widgets based on changing
 * sections within Swing Layouts.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public abstract class SwingNestedSectionLayoutDecorator
	extends org.metawidget.layout.decorator.NestedSectionLayoutDecorator<JComponent, JComponent, SwingMetawidget> {

	//
	// Constructor
	//

	protected SwingNestedSectionLayoutDecorator( LayoutDecoratorConfig<JComponent, JComponent, SwingMetawidget> config ) {

		super( config );
	}

	//
	// Public methods
	//

	@Override
	public void startContainerLayout( JComponent container, SwingMetawidget metawidget ) {

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
	protected State<JComponent> getState( JComponent container, SwingMetawidget metawidget ) {

		@SuppressWarnings( "unchecked" )
		State<JComponent> state = (State<JComponent>) container.getClientProperty( getClass() );

		if ( state == null ) {
			state = new State<JComponent>();
			container.putClientProperty( getClass(), state );
		}

		return state;
	}

	@Override
	protected boolean isIgnored( JComponent component ) {

		return ( component instanceof Stub && component.getComponentCount() == 0 );
	}
}
