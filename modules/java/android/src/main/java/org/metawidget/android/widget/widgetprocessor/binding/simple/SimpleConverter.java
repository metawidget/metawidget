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

package org.metawidget.android.widget.widgetprocessor.binding.simple;

import android.view.View;
import android.widget.CheckBox;

/**
 * Built-in Converter to convert primitive types.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class SimpleConverter
	extends BaseConverter<Object> {

	//
	// Public methods
	//

	public Object convertFromView( View widget, Object value, Class<?> type ) {

		if ( value instanceof String ) {
			String stringValue = (String) value;

			// Primitives

			if ( byte.class.equals( type ) ) {
				return Byte.parseByte( stringValue );
			}

			if ( short.class.equals( type ) ) {
				return Short.parseShort( stringValue );
			}

			if ( int.class.equals( type ) ) {
				return Integer.parseInt( stringValue );
			}

			if ( long.class.equals( type ) ) {
				return Long.parseLong( stringValue );
			}

			if ( float.class.equals( type ) ) {
				return Float.parseFloat( stringValue );
			}

			if ( double.class.equals( type ) ) {
				return Double.parseDouble( stringValue );
			}

			if ( boolean.class.equals( type ) ) {
				return Boolean.parseBoolean( stringValue );
			}

			if ( char.class.equals( type ) ) {
				return ( stringValue ).charAt( 0 );
			}

			// Primitive wrappers

			if ( Byte.class.equals( type ) ) {
				return Byte.valueOf( stringValue );
			}

			if ( Short.class.equals( type ) ) {
				return Short.valueOf( stringValue );
			}

			if ( Integer.class.equals( type ) ) {
				return Integer.valueOf( stringValue );
			}

			if ( Long.class.equals( type ) ) {
				return Long.valueOf( stringValue );
			}

			if ( Float.class.equals( type ) ) {
				return Float.valueOf( stringValue );
			}

			if ( Double.class.equals( type ) ) {
				return Double.valueOf( stringValue );
			}

			if ( Boolean.class.equals( type ) ) {
				return Boolean.valueOf( stringValue );
			}

			if ( Character.class.equals( type ) ) {
				return Character.valueOf( stringValue.charAt( 0 ) );
			}

			// Unknown

			throw new RuntimeException( "Don't know how to convert a String to a " + type.getName() );
		}

		// Not a String conversion

		return value;
	}

	@Override
	public Object convertForView( View view, Object value ) {

		if ( view instanceof CheckBox ) {
			return value;
		}

		return super.convertForView( view, value );
	}
}
