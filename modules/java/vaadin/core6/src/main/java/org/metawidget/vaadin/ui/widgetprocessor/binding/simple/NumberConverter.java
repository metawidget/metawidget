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
 * Built-in Converter to convert Numbers to primitive types.
 * <p>
 * This Converter is necessary because some Vaadin components (such as Slider) expect to be given
 * Doubles, but we may want them to load/save to Integer fields.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class NumberConverter
	implements Converter<Number, Number> {

	//
	// Public methods
	//

	public Number convert( Number value, Class<? extends Number> actualType ) {

		// Primitives

		if ( byte.class.equals( actualType ) || Byte.class.equals( actualType ) ) {
			return value.byteValue();
		}

		if ( short.class.equals( actualType ) || Short.class.equals( actualType ) ) {
			return value.shortValue();
		}

		if ( int.class.equals( actualType ) || Integer.class.equals( actualType ) ) {
			return value.intValue();
		}

		if ( long.class.equals( actualType ) || Long.class.equals( actualType ) ) {
			return value.longValue();
		}

		if ( float.class.equals( actualType ) || Float.class.equals( actualType ) ) {
			return value.floatValue();
		}

		if ( double.class.equals( actualType ) || Double.class.equals( actualType ) ) {
			return value.doubleValue();
		}

		// Unknown

		throw new RuntimeException( "Don't know how to convert a Number to a " + actualType.getName() );
	}
}
