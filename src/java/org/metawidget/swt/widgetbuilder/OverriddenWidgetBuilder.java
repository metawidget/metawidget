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

package org.metawidget.swt.widgetbuilder;

import static org.metawidget.inspector.InspectionResultConstants.*;

import java.util.Map;

import org.eclipse.swt.widgets.Control;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.widgetbuilder.iface.WidgetBuilder;

/**
 * WidgetBuilder for overridden widgets in SWT environments.
 * <p>
 * Locates overridden widgets based on a <code>name</code> attribute in their <code>setData</code>.
 *
 * @author Richard Kennard
 */

public class OverriddenWidgetBuilder
	implements WidgetBuilder<Control, SwtMetawidget>
{
	//
	// Public methods
	//

	@Override
	public Control buildWidget( String elementName, Map<String, String> attributes, SwtMetawidget metawidget )
	{
		String name = attributes.get( NAME );

		if ( name == null )
			return null;

		Control component = null;

		for ( Control componentExisting : metawidget.getChildren() )
		{
			if ( name.equals( componentExisting.getData( NAME ) ) )
				return component;
		}

		return null;
	}
}
