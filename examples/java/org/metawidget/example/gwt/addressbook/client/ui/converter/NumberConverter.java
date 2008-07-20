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

package org.metawidget.example.gwt.addressbook.client.ui.converter;

import org.metawidget.gwt.client.binding.simple.Converter;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author Richard Kennard
 */

// TODO: move this into GWT core?

public class NumberConverter
	extends Converter<Number>
{
	//
	//
	// Public methods
	//
	//

	@Override
	public Number convertFromWidget( Widget widget, Object value, Class<?> type )
	{
		if ( value == null || "".equals( value ))
			return null;

		if ( Integer.class.equals( type ))
			return Integer.valueOf( (String) value );

		if ( int.class.equals( type ))
			return Integer.valueOf( (String) value );

		throw new RuntimeException( "Don't know how to convert a " + value.getClass() );
	}
}
