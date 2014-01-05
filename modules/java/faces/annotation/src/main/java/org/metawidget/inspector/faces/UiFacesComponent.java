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
 * Annotates the field should be rendered using the given Faces <em>component type</em> in the UI.
 * <p>
 * Use of this annotation does not bind the business class to the UI quite as tightly as it may
 * appear, because JSF has a loosely coupled relationship between
 * <code>&lt;component-type&gt;</code> and <code>&lt;component-class&gt;</code>, and a further loose
 * coupling between <code>&lt;component&gt;</code> and <code>&lt;render-kit&gt;</code> - as defined
 * in <code>faces-config.xml</code>.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.FIELD, ElementType.METHOD } )
public @interface UiFacesComponent {

	String value();
}
