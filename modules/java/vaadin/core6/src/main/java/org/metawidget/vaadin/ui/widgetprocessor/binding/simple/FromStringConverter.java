// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

package org.metawidget.vaadin.ui.widgetprocessor.binding.simple;

/**
 * Built-in Converter to convert Strings to primitive types.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
