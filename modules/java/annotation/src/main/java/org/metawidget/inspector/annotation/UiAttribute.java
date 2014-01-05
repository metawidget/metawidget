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
 * Annotates an arbitrary Metawidget attribute.
 * <p>
 * This annotation can be used when no other Inspector is available for the given attribute, and as
 * an alternative to using XmlInspector.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.FIELD, ElementType.METHOD } )
public @interface UiAttribute {

	/**
	 * Attribute to set.
	 * <p>
	 * Multiple attributes can be specified if you need to set multiple attributes to the same
	 * value.
	 */

	String[] name();

	/**
	 * Value to set the attribute to.
	 * <p>
	 * This can be an EL expression if using an <code>InspectionResultProcessor</code> such as
	 * <code>FacesInspectionResultProcessor</code> or <code>JexlInspectionResultProcessor</code>.
	 */

	String value();
}
