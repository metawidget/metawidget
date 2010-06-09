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

package org.metawidget.swing.allwidgets.converter.beanutils;

import org.apache.commons.beanutils.Converter;
import org.metawidget.shared.allwidgets.model.AllWidgets.NestedWidgets;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * @author Richard Kennard
 */

public class NestedWidgetsConverter
	implements Converter
{
	//
	// Public methods
	//

	@SuppressWarnings( "unchecked" )
	public Object convert( Class clazz, Object value )
	{
		// To String

		if ( clazz.equals( String.class ))
		{
			return StringUtils.quietValueOf( value );
		}

		// To NestedWidgets

		if ( clazz.equals( NestedWidgets.class ))
		{
			String[] values = ArrayUtils.fromString( (String) value );

			if ( values.length == 0 )
			{
				return null;
			}

			NestedWidgets nestedWidgets = new NestedWidgets();
			nestedWidgets.setNestedTextbox1( values[0] );

			if ( values.length > 1 )
			{
				nestedWidgets.setNestedTextbox2( values[1] );
			}

			return nestedWidgets;
		}

		throw new UnsupportedOperationException( "Don't know how to convert '" + value + "' to a " + clazz );
	}
}
