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

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.layout.decorator.LayoutDecoratorConfig;
import org.metawidget.statically.StaticMetawidget;
import org.metawidget.statically.StaticWidget;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.LayoutUtils;

/**
 * Convenience base class for LayoutDecorators wishing to decorate widgets based on changing
 * sections within JSP-based Layouts.
 * <p>
 * Note: it is not clear it is possible to implement a <code>JspNestedSectionLayoutDecorator</code>,
 * because the JSP component model does not allow adding children to tags.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public abstract class StaticFlatSectionLayoutDecorator
	extends org.metawidget.layout.decorator.FlatSectionLayoutDecorator<StaticWidget, StaticWidget, StaticMetawidget> {

	//
	// Constructor
	//

	protected StaticFlatSectionLayoutDecorator( LayoutDecoratorConfig<StaticWidget, StaticWidget, StaticMetawidget> config ) {

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
	protected State getState( StaticWidget containerTag, StaticMetawidget metawidgetTag ) {

		State state = (State) metawidgetTag.getClientProperty( getClass() );

		if ( state == null ) {
			state = new State();
			metawidgetTag.putClientProperty( getClass(), state );
		}

		return state;
	}

	@Override
	protected boolean isIgnored( StaticWidget tag ) {

		return false;
	}
}
