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

package org.metawidget.test.swing.allwidgets.converter.beansbinding;

import org.jdesktop.beansbinding.Converter;
import org.metawidget.test.shared.allwidgets.model.AllWidgets.NestedWidgets;
import org.metawidget.util.ArrayUtils;
import org.metawidget.util.simple.StringUtils;

/**
 * @author Richard Kennard
 */

public class NestedWidgetsConverter
	extends Converter<NestedWidgets, String>
{
	//
	// Public methods
	//

	@Override
	public String convertForward( NestedWidgets value )
	{
		return StringUtils.quietValueOf( value );
	}

	@Override
	public NestedWidgets convertReverse( String value )
	{
		String[] values = ArrayUtils.fromString( value );

		if ( values.length == 0 )
			return null;

		NestedWidgets nestedWidgets = new NestedWidgets();
		nestedWidgets.setNestedTextbox1( values[0] );

		if ( values.length > 1 )
			nestedWidgets.setNestedTextbox2( values[1] );

		return nestedWidgets;
	}
}
