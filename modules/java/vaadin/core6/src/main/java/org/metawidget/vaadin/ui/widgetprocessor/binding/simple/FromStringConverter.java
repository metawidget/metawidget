// Metawidget (licensed under LGPL)
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

package org.metawidget.vaadin.ui.widgetprocessor.binding.simple;

/**
 * Built-in Converter to convert Strings to primitive types.
 *
 * @author Richard Kennard
 */

public class FromStringConverter
	implements Converter<String, Object> {

	//
	// Public methods
	//

	@SuppressWarnings( { "unchecked", "rawtypes" } )
	public Object convert( String value, Class<? extends Object> actualType ) {

		// Primitives

		if ( byte.class.equals( actualType ) ) {
			return Byte.parseByte( value );
		}

		if ( short.class.equals( actualType ) ) {
			return Short.parseShort( value );
		}

		if ( int.class.equals( actualType ) ) {
			return Integer.parseInt( value );
		}

		if ( long.class.equals( actualType ) ) {
			return Long.parseLong( value );
		}

		if ( float.class.equals( actualType ) ) {
			return Float.parseFloat( value );
		}

		if ( double.class.equals( actualType ) ) {
			return Double.parseDouble( value );
		}

		if ( boolean.class.equals( actualType ) ) {
			return Boolean.parseBoolean( value );
		}

		if ( char.class.equals( actualType ) ) {
			return ( value ).charAt( 0 );
		}

		// Primitive wrappers

		if ( Byte.class.equals( actualType ) ) {
			return Byte.valueOf( value );
		}

		if ( Short.class.equals( actualType ) ) {
			return Short.valueOf( value );
		}

		if ( Integer.class.equals( actualType ) ) {
			return Integer.valueOf( value );
		}

		if ( Long.class.equals( actualType ) ) {
			return Long.valueOf( value );
		}

		if ( Float.class.equals( actualType ) ) {
			return Float.valueOf( value );
		}

		if ( Double.class.equals( actualType ) ) {
			return Double.valueOf( value );
		}

		if ( Boolean.class.equals( actualType ) ) {
			return Boolean.valueOf( value );
		}

		if ( Character.class.equals( actualType ) ) {
			return Character.valueOf( value.charAt( 0 ) );
		}

		// Enums

		if ( actualType.isEnum() ) {
			return Enum.valueOf( (Class<? extends Enum>) actualType, value );
		}

		// Strings/Objects

		if ( String.class.equals( actualType ) || Object.class.equals( actualType ) ) {

			// Convert empty Strings back to null

			if ( "".equals( value )) {
				return null;
			}

			return value;
		}

		// Unknown

		throw new RuntimeException( "Don't know how to convert a String to a " + actualType.getName() );
	}
}
