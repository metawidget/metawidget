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

package org.metawidget.gwt.client.ui.layout;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.metawidget.gwt.client.ui.GwtMetawidget;
import org.metawidget.gwt.client.ui.GwtUtils;
import org.metawidget.gwt.client.ui.Stub;
import org.metawidget.layout.decorator.LayoutDecoratorConfig;

import com.google.gwt.user.client.ui.Widget;

/**
 * Convenience base class for LayoutDecorators wishing to decorate widgets based on changing
 * sections within GWT Layouts.
 *
 * @author Richard Kennard
 */

public abstract class GwtFlatSectionLayoutDecorator
	extends org.metawidget.layout.decorator.FlatSectionLayoutDecorator<Widget, GwtMetawidget>
{
	//
	// Constructor
	//

	protected GwtFlatSectionLayoutDecorator( LayoutDecoratorConfig<Widget, GwtMetawidget> config )
	{
		super( config );
	}

	//
	// Public methods
	//

	@Override
	public void startLayout( Widget container, GwtMetawidget metawidget )
	{
		super.startLayout( container, metawidget );
		metawidget.putClientProperty( getClass(), null );
	}

	//
	// Protected methods
	//

	@Override
	protected String[] getSections( Map<String, String> attributes )
	{
		return GwtUtils.fromString( attributes.get( SECTION ), ',' ).toArray( new String[0] );
	}

	@Override
	protected State getState( Widget container, GwtMetawidget metawidget )
	{
		State state = (State) metawidget.getClientProperty( getClass() );

		if ( state == null )
		{
			state = new State();
			metawidget.putClientProperty( getClass(), state );
		}

		return state;
	}

	@Override
	protected boolean isEmptyStub( Widget widget )
	{
		return ( widget instanceof Stub && ((Stub) widget).getWidgetCount() == 0 );
	}
}
