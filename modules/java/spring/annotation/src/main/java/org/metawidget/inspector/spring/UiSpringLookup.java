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

package org.metawidget.inspector.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates the value returned by the field should belong to the set returned by the given EL
 * expression.
 * <p>
 * Because Spring's <code>s:options</code> tag takes a JSP EL expression, not a 'Spring beans aware'
 * expression, this annotation is best used in conjunction with
 * <code>InternalResourceViewResolver.setExposeContextBeansAsAttributes</code> (available in Spring
 * 2.5+). You may also need to set <code>springJspExpressionSupport</code> to <code>true</code> in
 * your web.xml.
 * 
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.FIELD, ElementType.METHOD } )
public @interface UiSpringLookup {

	/**
	 * Value of the lookup. Equivalent to
	 * <code>org.springframework.web.servlet.tags.form.OptionsTag.setItems</code>
	 */

	String value();

	/**
	 * Name of the property mapped to the label (inner text) of the 'option' tag. Equivalent to
	 * <code>org.springframework.web.servlet.tags.form.OptionsTag.setItemValue</code>
	 */

	String itemValue() default "";

	/**
	 * Name of the property mapped to the 'value' attribute of the 'option' tag. Equivalent to
	 * <code>org.springframework.web.servlet.tags.form.OptionsTag.setItemLabel</code>
	 */

	String itemLabel() default "";
}
