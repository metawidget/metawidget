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

package org.metawidget.swing;

import java.awt.Component;

import javax.swing.JComponent;

import org.metawidget.MetawidgetException;
import org.metawidget.util.ArrayUtils;

/**
 * Utilities for working with Swing.
 *
 * @author Richard Kennard
 */

public final class SwingUtils
{
	//
	//
	// Public statics
	//
	//

	/**
	 * Finds the Component with the given name.
	 */

	public static Component findComponentNamed( JComponent component, String... names )
	{
		if ( names == null || names.length == 0 )
			return null;

		Component topComponent = component;

		for( int loop = 0, length = names.length; loop < length; loop++ )
		{
			String name = names[loop];

			// Try to find a component...

			Component[] children = ((JComponent) topComponent).getComponents();
			topComponent = null;

			for ( Component childComponent : children )
			{
				// ...with the name we're interested in

				if ( name.equals( childComponent.getName() ))
				{
					topComponent = childComponent;
					break;
				}
			}

			if ( loop == length - 1 )
				return topComponent;

			if ( topComponent == null )
				throw MetawidgetException.newException( "No such component '" + name + "' of '" + ArrayUtils.toString( names, "', '" ) + "'" );

			if ( !( topComponent instanceof JComponent ))
				throw MetawidgetException.newException( "'" + name + "' is not a JComponent" );
		}

		return topComponent;
	}

	//
	//
	// Private constructor
	//
	//

	private SwingUtils()
	{
		// Can never be called
	}
}
