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
