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
 * Annotates the field comes after the other given field(s) in the UI.
 * <p>
 * Controlling field ordering by annotating fields is an alternative to using one of the XML-based
 * <code>Inspectors</code> (XML nodes are inherently ordered), or using
 * <code>JavassistPropertyStyle</code>.
 *
 * @author Richard Kennard
 */

@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.FIELD, ElementType.METHOD } )
public @interface UiComesAfter
{
	/**
	 * Array of property names which the annotated property must come after.
	 * <p>
	 * Specifying multiple names can be useful if the annotated property is intermingled with other
	 * properties in subclasses.
	 * <p>
	 * If no names are specified, the annotated property will come after all other properties.
	 */

	String[] value() default {};
}
