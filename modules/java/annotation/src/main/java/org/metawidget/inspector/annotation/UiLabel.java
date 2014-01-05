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
 * Annotates the field should have the given label in the UI.
 * <p>
 * If no UI <code>ResourceBundle</code> is in use, the label will be output 'as is'. If a resource
 * bundle <em>has</em> been specified, the 'camel-cased' version of the label will be looked up in
 * the bundle. This means developers can initially build their UIs without worrying about resource
 * bundles, then turn on localization support later.
 * <p>
 * To remove the label entirely (including its column) specify an empty String. To render a blank
 * label (preserving its column) specify a value that gets looked up in a
 * <code>ResourceBundle</code>, and have the <code>ResourceBundle</code> return an empty String.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.FIELD, ElementType.METHOD } )
public @interface UiLabel {

	String value();
}
