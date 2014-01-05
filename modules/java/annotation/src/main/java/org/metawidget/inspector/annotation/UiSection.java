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
 * Annotates the field should be preceding by a section heading.
 * <p>
 * The 'camel-cased' version of the section name will first be looked up in any relevant UI
 * <code>ResourceBundle</code>. If no match is found, the section name will be output 'as is'.
 * <p>
 * Once a section heading has been declared, subsequent fields are assumed to belong to the same
 * section until a different section heading is encountered. Sections can be cancelled using a
 * section heading with an empty String. Sections can be nested by specifying multiple section
 * names.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.FIELD, ElementType.METHOD } )
public @interface UiSection {

	String[] value();
}
