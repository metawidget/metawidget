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

package org.metawidget.gwt.client.binding.simple;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Built-in Converter to convert primitive types.
 *
 * @author Richard Kennard
 */

public class SimpleConverter
	extends Converter<Object>
{
	//
	//
	// Public methods
	//
	//

	@Override
	public Object convertFromWidget( Widget widget, Object value, Class<?> type )
	{
		// Primitives

		if ( byte.class.equals( type ) )
			return Byte.parseByte( (String) value );

		if ( short.class.equals( type ) )
			return Short.parseShort( (String) value );

		if ( int.class.equals( type ) )
			return Integer.parseInt( (String) value );

		if ( long.class.equals( type ) )
			return Long.parseLong( (String) value );

		if ( float.class.equals( type ) )
			return Float.parseFloat( (String) value );

		if ( double.class.equals( type ) )
			return Double.parseDouble( (String) value );

		if ( boolean.class.equals( type ) )
		{
			if ( widget instanceof CheckBox )
				return value;

			return Boolean.parseBoolean( (String) value );
		}

		if ( char.class.equals( type ) )
			return ( (String) value ).charAt( 0 );

		// Primitive wrappers

		if ( value == null || "".equals( value ) )
			return null;

		if ( Byte.class.equals( type ) )
			return Byte.valueOf( (String) value );

		if ( Short.class.equals( type ) )
			return Short.valueOf( (String) value );

		if ( Integer.class.equals( type ) )
			return Integer.valueOf( (String) value );

		if ( Long.class.equals( type ) )
			return Long.valueOf( (String) value );

		if ( Float.class.equals( type ) )
			return Float.valueOf( (String) value );

		if ( Double.class.equals( type ) )
			return Double.valueOf( (String) value );

		if ( Boolean.class.equals( type ) )
			return Boolean.valueOf( (String) value );

		if ( Character.class.equals( type ) )
			return Character.valueOf( ( (String) value ).charAt( 0 ) );

		// Unknown

		throw new RuntimeException( "Don't know how to convert a " + value.getClass() );
	}

	@Override
	public Object convertForWidget( Widget widget, Object value )
	{
		if ( widget instanceof CheckBox )
			return value;

		return super.convertForWidget( widget, value );
	}
}
