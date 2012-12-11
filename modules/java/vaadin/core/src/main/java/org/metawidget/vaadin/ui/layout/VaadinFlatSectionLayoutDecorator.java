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

package org.metawidget.vaadin.ui.layout;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.layout.decorator.FlatSectionLayoutDecorator;
import org.metawidget.layout.decorator.LayoutDecoratorConfig;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.LayoutUtils;
import org.metawidget.vaadin.ui.Stub;
import org.metawidget.vaadin.ui.VaadinMetawidget;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;

/**
 * Convenience base class for LayoutDecorators .
 *
 * @author Loghman Barari
 */

public abstract class VaadinFlatSectionLayoutDecorator
	extends FlatSectionLayoutDecorator<Component, ComponentContainer, VaadinMetawidget> {

	//
	// Constructor
	//

	protected VaadinFlatSectionLayoutDecorator( LayoutDecoratorConfig<Component, ComponentContainer, VaadinMetawidget> config ) {

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
	protected String[] getSections( Map<String, String> attributes ) {

		return ArrayUtils.fromString( attributes.get( SECTION ) );
	}

	@Override
	protected State getState( ComponentContainer container, VaadinMetawidget metawidget ) {

		State state = (State) metawidget.getClientProperty( getClass() );

		if ( state == null ) {
			state = new State();
			metawidget.putClientProperty( getClass(), state );
		}

		return state;
	}

	@Override
	protected boolean isIgnored( Component component ) {

		return ( component instanceof Stub && !( (Stub) component ).getComponentIterator().hasNext() );
	}
}
