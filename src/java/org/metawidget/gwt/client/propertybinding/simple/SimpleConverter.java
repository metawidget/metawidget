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

package org.metawidget.gwt.client.propertybinding.simple;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Built-in Converter to convert primitive types.
 *
 * @author Richard Kennard
 */

public class SimpleConverter
	extends BaseConverter<Object>
{
	//
	// Public methods
	//

	public Object convertFromWidget( Widget widget, Object value, Class<?> type )
	{
		if ( value instanceof String )
		{
			String stringValue = (String) value;

			// Primitives

			if ( byte.class.equals( type ) )
				return Byte.parseByte( stringValue );

			if ( short.class.equals( type ) )
				return Short.parseShort( stringValue );

			if ( int.class.equals( type ) )
				return Integer.parseInt( stringValue );

			if ( long.class.equals( type ) )
				return Long.parseLong( stringValue );

			if ( float.class.equals( type ) )
				return Float.parseFloat( stringValue );

			if ( double.class.equals( type ) )
				return Double.parseDouble( stringValue );

			if ( boolean.class.equals( type ) )
				return Boolean.parseBoolean( stringValue );

			if ( char.class.equals( type ) )
				return ( stringValue ).charAt( 0 );

			// Primitive wrappers

			if ( Byte.class.equals( type ) )
				return Byte.valueOf( stringValue );

			if ( Short.class.equals( type ) )
				return Short.valueOf( stringValue );

			if ( Integer.class.equals( type ) )
				return Integer.valueOf( stringValue );

			if ( Long.class.equals( type ) )
				return Long.valueOf( stringValue );

			if ( Float.class.equals( type ) )
				return Float.valueOf( stringValue );

			if ( Double.class.equals( type ) )
				return Double.valueOf( stringValue );

			if ( Boolean.class.equals( type ) )
				return Boolean.valueOf( stringValue );

			if ( Character.class.equals( type ) )
				return Character.valueOf( stringValue.charAt( 0 ) );

			// Unknown

			throw new RuntimeException( "Don't know how to convert a String to a " + type );
		}

		// Not a String conversion

		return value;
	}

	@Override
	public Object convertForWidget( Widget widget, Object value )
	{
		if ( widget instanceof CheckBox )
			return value;

		return super.convertForWidget( widget, value );
	}
}
