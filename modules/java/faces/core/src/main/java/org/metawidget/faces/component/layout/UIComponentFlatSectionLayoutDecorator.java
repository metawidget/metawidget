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

package org.metawidget.faces.component.layout;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputHidden;

import org.metawidget.faces.component.UIMetawidget;
import org.metawidget.faces.component.UIStub;
import org.metawidget.layout.decorator.LayoutDecoratorConfig;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.LayoutUtils;

/**
 * Convenience base class for LayoutDecorators wishing to decorate widgets based on changing
 * sections within JSF Layouts.
 *
 * @author Richard Kennard
 */

public abstract class UIComponentFlatSectionLayoutDecorator
	extends org.metawidget.layout.decorator.FlatSectionLayoutDecorator<UIComponent, UIComponent, UIMetawidget> {

	//
	// Constructor
	//

	protected UIComponentFlatSectionLayoutDecorator( LayoutDecoratorConfig<UIComponent, UIComponent, UIMetawidget> config ) {

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
	protected State getState( UIComponent container, UIMetawidget metawidget ) {

		State state = (State) metawidget.getClientProperty( getClass() );

		if ( state == null ) {
			state = new State();
			metawidget.putClientProperty( getClass(), state );
		}

		return state;
	}

	@Override
	protected boolean isIgnored( UIComponent component ) {

		if ( component instanceof UIStub && component.getChildren().isEmpty() ) {
			return true;
		}

		// Ignore HtmlInputHidden, so that we don't create section headings for it

		if ( component instanceof HtmlInputHidden ) {
			return true;
		}

		return false;
	}
}
