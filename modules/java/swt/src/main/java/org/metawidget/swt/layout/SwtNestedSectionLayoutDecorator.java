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

package org.metawidget.swt.layout;

import java.util.Map;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.metawidget.layout.decorator.LayoutDecoratorConfig;
import org.metawidget.swt.Stub;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.util.LayoutUtils;

/**
 * Convenience base class for LayoutDecorators wishing to decorate widgets based on changing
 * sections within SWT Layouts.
 *
 * @author Richard Kennard
 */

public abstract class SwtNestedSectionLayoutDecorator
	extends org.metawidget.layout.decorator.NestedSectionLayoutDecorator<Control, Composite, SwtMetawidget>
	implements SwtLayoutDecorator {

	//
	// Constructor
	//

	protected SwtNestedSectionLayoutDecorator( LayoutDecoratorConfig<Control, Composite, SwtMetawidget> config ) {

		super( config );
	}

	//
	// Public methods
	//

	@Override
	public void startContainerLayout( Composite container, SwtMetawidget metawidget ) {

		super.startContainerLayout( container, metawidget );
		container.setData( getClass().getName(), null );
	}

	public Composite startBuildWidget( String elementName, Map<String, String> attributes, Composite container, SwtMetawidget metawidget ) {

		String section = stripSection( attributes );
		State<Composite> state = getState( container, metawidget );

		// Stay where we are?

		if ( section == null || section.equals( state.currentSection ) ) {
			if ( state.currentSectionWidget == null ) {
				return delegateStartBuildWidget( elementName, attributes, container, metawidget );
			}

			return delegateStartBuildWidget( elementName, attributes, state.currentSectionWidget, metawidget );
		}

		state.currentSection = section;

		Composite previousSectionWidget = state.currentSectionWidget;

		// End current section

		if ( state.currentSectionWidget != null ) {
			super.endContainerLayout( state.currentSectionWidget, metawidget );
		}

		state.currentSectionWidget = null;

		// No new section?

		if ( "".equals( section ) ) {
			return delegateStartBuildWidget( elementName, attributes, container, metawidget );
		}

		state.currentSectionWidget = createSectionWidget( previousSectionWidget, section, attributes, container, metawidget );
		super.startContainerLayout( state.currentSectionWidget, metawidget );

		return delegateStartBuildWidget( elementName, attributes, state.currentSectionWidget, metawidget );
	}

	@Override
	public void layoutWidget( Control widget, String elementName, Map<String, String> attributes, Composite container, SwtMetawidget metawidget ) {

		State<Composite> state = getState( container, metawidget );

		if ( state.currentSectionWidget == null ) {
			getDelegate().layoutWidget( widget, elementName, attributes, container, metawidget );
		} else {
			getDelegate().layoutWidget( widget, elementName, attributes, state.currentSectionWidget, metawidget );
		}
	}

	//
	// Protected methods
	//

	@Override
	protected String stripSection( Map<String, String> attributes ) {

		return LayoutUtils.stripSection( attributes );
	}

	@Override
	protected State<Composite> getState( Composite container, SwtMetawidget metawidget ) {

		@SuppressWarnings( "unchecked" )
		State<Composite> state = (State<Composite>) container.getData( getClass().getName() );

		if ( state == null ) {
			state = new State<Composite>();
			container.setData( getClass().getName(), state );
		}

		return state;
	}

	@Override
	protected boolean isIgnored( Control control ) {

		return ( control instanceof Stub && ( (Stub) control ).getChildren().length == 0 );
	}

	//
	// Private methods
	//

	private Composite delegateStartBuildWidget( String elementName, Map<String, String> attributes, Composite container, SwtMetawidget metawidget ) {

		if ( getDelegate() instanceof SwtLayoutDecorator ) {
			return ( (SwtLayoutDecorator) getDelegate() ).startBuildWidget( elementName, attributes, container, metawidget );
		}

		return container;
	}
}
