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
