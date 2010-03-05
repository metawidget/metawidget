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

package org.metawidget.swt.layout;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.metawidget.layout.decorator.LayoutDecoratorConfig;
import org.metawidget.swt.Stub;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.LayoutUtils;

/**
 * Convenience base class for LayoutDecorators wishing to decorate widgets based on changing
 * sections within SWT Layouts.
 *
 * @author Richard Kennard
 */

public abstract class SwtFlatSectionLayoutDecorator
	extends org.metawidget.layout.decorator.FlatSectionLayoutDecorator<Control, Composite, SwtMetawidget>
{
	//
	// Constructor
	//

	protected SwtFlatSectionLayoutDecorator( LayoutDecoratorConfig<Control, Composite, SwtMetawidget> config )
	{
		super( config );
	}

	//
	// Public methods
	//

	@Override
	public void startContainerLayout( Composite container, SwtMetawidget metawidget )
	{
		super.startContainerLayout( container, metawidget );
		container.setData( getClass().getName(), null );
	}

	//
	// Protected methods
	//

	@Override
	protected String stripSection( Map<String, String> attributes )
	{
		return LayoutUtils.stripSection( attributes );
	}

	@Override
	protected String[] getSections( Map<String, String> attributes )
	{
		return ArrayUtils.fromString( attributes.get( SECTION ) );
	}

	@Override
	protected State getState( Composite container, SwtMetawidget metawidget )
	{
		State state = (State) container.getData( getClass().getName() );

		if ( state == null )
		{
			state = new State();
			container.setData( getClass().getName(), state );
		}

		return state;
	}

	@Override
	protected boolean isEmptyStub( Control control )
	{
		return ( control instanceof Stub && ((Stub) control).getChildren().length == 0 );
	}
}
