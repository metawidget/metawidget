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

package org.metawidget.android.widget.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;
import java.util.Set;

import org.metawidget.android.widget.AndroidMetawidget;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

import android.view.View;

/**
 * WidgetBuilder for overridden widgets in Swing environments.
 * <p>
 * Locates overridden widgets based on their <code>tag</code>.
 *
 * @author Richard Kennard
 */

public class OverriddenWidgetBuilder
	implements WidgetBuilder<View, AndroidMetawidget>
{
	//
	// Public methods
	//

	@Override
	public View buildWidget( String elementName, Map<String, String> attributes, AndroidMetawidget metawidget )
	{
		View view = null;
		String name = attributes.get( NAME );

		if ( name == null )
			return null;

		Set<View> existingUnusedViews = metawidget.fetchExistingUnusedViews();

		for ( View viewExisting : existingUnusedViews )
		{
			if ( name.equals( viewExisting.getTag() ) )
			{
				view = viewExisting;
				break;
			}
		}

		if ( view != null )
			existingUnusedViews.remove( view );

		return view;
	}
}
