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

import java.util.Map;

import org.metawidget.gwt.client.ui.Facet;
import org.metawidget.gwt.client.ui.GwtMetawidget;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Richard Kennard
 */

public class FlexTableLayout
	extends Layout
{
	//
	//
	// Private members
	//
	//

	private FlexTable	mLayout;

	//
	//
	// Constructor
	//
	//

	public FlexTableLayout( GwtMetawidget metawidget )
	{
		super( metawidget );
	}

	//
	//
	// Public methods
	//
	//

	@Override
	public void layoutBegin()
	{
		mLayout = new FlexTable();
		getMetawidget().add( mLayout );
	}

	@Override
	public void layoutChild( Widget widget, Map<String, String> attributes )
	{
		int row = mLayout.getRowCount();

		// Label

		String name = attributes.get( "name" );
		Label label = new Label( name + ":" );
		mLayout.setWidget( row, 0, label );

		// Widget

		mLayout.setWidget( row, 1, widget );
	}

	@Override
	public void layoutEnd()
	{
		Facet facet = getMetawidget().getFacet( "buttons" );

		if ( facet != null )
		{
			int row = mLayout.getRowCount();
			mLayout.setWidget( row, 0, facet );
			mLayout.getFlexCellFormatter().setColSpan( row, 0, 2 );
		}
	}
}
