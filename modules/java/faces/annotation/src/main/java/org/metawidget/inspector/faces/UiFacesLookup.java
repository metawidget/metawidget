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
 * Annotates the value in the field should belong to the set returned by the given EL expression.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.FIELD, ElementType.METHOD } )
public @interface UiFacesLookup {

	/**
	 * An EL expression for the lookup, of the form <code>#{...}</code>.
	 */

	String value();

	/**
	 * Name of the EL var to be referenced in <code>itemValue</code> and <code>itemLabel</code> (JSF
	 * 2 only).
	 */

	String var() default "";

	/**
	 * An EL expression for the item value, of the form <code>#{...}</code> (JSF 2 only). Typically
	 * this will make reference to the <code>var</code> attribute, for example
	 * <code>#{_item.value}</code>.
	 */

	String itemValue() default "";

	/**
	 * An EL expression for the item value, of the form <code>#{...}</code> (JSF 2 only). Typically
	 * this will make reference to the <code>var</code> attribute, for example
	 * <code>#{_item.label}</code>.
	 */

	String itemLabel() default "";
}
