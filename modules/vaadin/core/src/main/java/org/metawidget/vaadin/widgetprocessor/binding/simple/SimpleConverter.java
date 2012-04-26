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

package org.metawidget.vaadin.widgetprocessor.binding.simple;



/**
 * Built-in Converter to convert Strings to primitive types.
 *
 * @author Richard Kennard
 */

public class SimpleConverter
	extends BaseConverter<Object> {

	//
	// Public methods
	//

	public Object convertFromString( String value, Class<Object> type ) {

		// Primitives

		if ( byte.class.equals( type ) ) {
			return Byte.parseByte( value );
		}

		if ( short.class.equals( type ) ) {
			return Short.parseShort( value );
		}

		if ( int.class.equals( type ) ) {
			return Integer.parseInt( value );
		}

		if ( long.class.equals( type ) ) {
			return Long.parseLong( value );
		}

		if ( float.class.equals( type ) ) {
			return Float.parseFloat( value );
		}

		if ( double.class.equals( type ) ) {
			return Double.parseDouble( value );
		}

		if ( boolean.class.equals( type ) ) {
			return Boolean.parseBoolean( value );
		}

		if ( char.class.equals( type ) ) {
			return ( value ).charAt( 0 );
		}

		// Primitive wrappers

		if ( Byte.class.equals( type ) ) {
			return Byte.valueOf( value );
		}

		if ( Short.class.equals( type ) ) {
			return Short.valueOf( value );
		}

		if ( Integer.class.equals( type ) ) {
			return Integer.valueOf( value );
		}

		if ( Long.class.equals( type ) ) {
			return Long.valueOf( value );
		}

		if ( Float.class.equals( type ) ) {
			return Float.valueOf( value );
		}

		if ( Double.class.equals( type ) ) {
			return Double.valueOf( value );
		}

		if ( Boolean.class.equals( type ) ) {
			return Boolean.valueOf( value );
		}

		if ( Character.class.equals( type ) ) {
			return Character.valueOf( value.charAt( 0 ) );
		}

		// Unknown

		throw new RuntimeException( "Don't know how to convert a String to a " + type.getName() );
	}
}
