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
 * Identifies a method as being an executable action.
 * <p>
 * The method must be public, and must not take any parameters in its signature.
 * <p>
 * Note: Metawidget is designed to use <em>existing</em> metadata as much as possible. Clients
 * should use something like <code>org.jdesktop.application.Action</code> or JBoss jBPM in
 * preference to <code>UiAction</code>.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.METHOD )
public @interface UiAction {
	// Just a marker annotation
}
