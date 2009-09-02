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

import org.metawidget.gwt.client.ui.GwtMetawidget;

import com.google.gwt.user.client.ui.Widget;

/**
 * Layout to simply output components one after another, with no labels and no structure.
 *
 * @author Richard Kennard
 */

public class FlowLayout
	implements Layout
{
	//
	// Public methods
	//

	@Override
	public void layoutBegin( GwtMetawidget metawidget )
	{
		// Do nothing
	}

	@Override
	public void layoutChild( Widget widget, Map<String, String> attributes, GwtMetawidget metawidget )
	{
		metawidget.add( widget );
	}

	@Override
	public void layoutEnd( GwtMetawidget metawidget )
	{
		// Do nothing
	}
}
