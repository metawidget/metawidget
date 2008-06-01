// Metawidget
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

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
 * <code>ResourceBundle</code>, and have the <code>ResourceBundle</code> return an empty
 * String.
 *
 * @author Richard Kennard
 */

@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.FIELD, ElementType.METHOD } )
public @interface UiLabel
{
	String value();
}
