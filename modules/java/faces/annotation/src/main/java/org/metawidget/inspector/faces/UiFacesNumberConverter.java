// Metawidget
//
// This library is dual licensed under both LGPL and a commercial
// license.
//
// LGPL: this library is free software; you can redistribute it and/or
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
//
// Commercial License: See http://metawidget.org for details

package org.metawidget.inspector.faces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates the field should use the standard Faces <code>NumberConverter</code>.
 * <p>
 * Note: the <code>NumberConverter</code> property <code>integerOnly</code> is not specified using
 * this annotation, as it can be inferred from the property's type.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.FIELD, ElementType.METHOD } )
public @interface UiFacesNumberConverter {

	String currencyCode() default "";

	String currencySymbol() default "";

	boolean groupingUsed() default true;

	int maxFractionDigits() default -1;

	int maxIntegerDigits() default -1;

	int minFractionDigits() default -1;

	int minIntegerDigits() default -1;

	String locale() default "";

	String pattern() default "";

	String type() default "";
}
