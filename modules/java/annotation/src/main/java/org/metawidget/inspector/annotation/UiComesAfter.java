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

package org.metawidget.inspector.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates the field comes after the other given field(s) in the UI.
 * <p>
 * Controlling field ordering by annotating fields is an alternative to using one of the XML-based
 * <code>Inspectors</code> (XML nodes are inherently ordered), or using
 * <code>JavassistPropertyStyle</code>.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.FIELD, ElementType.METHOD } )
public @interface UiComesAfter {

	/**
	 * Array of property names which the annotated property must come after.
	 * <p>
	 * Specifying multiple names can be useful if the annotated property is intermingled with other
	 * properties in subclasses.
	 * <p>
	 * If no names are specified, the annotated property will come after all other properties.
	 */

	String[] value() default {};
}
