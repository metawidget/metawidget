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
 * Annotates the field should use the given Faces converter in the UI.
 * <p>
 * This annotation uses the converter <em>id</em>, not its class. Alternatively, this annotation can
 * be an EL expression that evaluates to a <code>javax.faces.convert.Converter</code> instance.
 * 
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.FIELD, ElementType.METHOD } )
public @interface UiFacesConverter {

	/**
	 * The id of the converter, must match that defined in <code>faces-config.xml</code>'s
	 * <code>&lt;converter-id&gt;</code>. This can also be an expression of the form
	 * <code>#{...}</code> that evaluates to a <code>javax.faces.convert.Converter</code> instance.
	 */

	String value();
}
