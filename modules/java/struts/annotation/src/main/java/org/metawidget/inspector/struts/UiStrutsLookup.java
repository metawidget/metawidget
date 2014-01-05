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

package org.metawidget.inspector.struts;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates the value returned by the field should belong to the set returned by the named JSP bean
 * and property (as used by Struts' <code>html:options</code>).
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.FIELD, ElementType.METHOD } )
public @interface UiStrutsLookup {

	/**
	 * Name of the bean containing the lookup. Equivalent to
	 * <code>org.apache.struts.taglib.OptionsTag.setName</code>
	 */

	String name();

	/**
	 * Name of the property to use to build the values collection. Equivalent to
	 * <code>org.apache.struts.taglib.OptionsTag.setProperty</code>
	 */

	String property();

	/**
	 * Name of the bean containing the labels lookup. Equivalent to
	 * <code>org.apache.struts.taglib.OptionsTag.setLabelName</code>
	 */

	String labelName() default "";

	/**
	 * Name of the property to use to build the labels collection. Equivalent to
	 * <code>org.apache.struts.taglib.OptionsTag.setLabelProperty</code>
	 */

	String labelProperty() default "";
}
