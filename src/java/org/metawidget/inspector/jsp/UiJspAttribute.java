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

package org.metawidget.inspector.jsp;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates an arbitrary attribute for the UI, based on a Java Server Pages EL expression.
 * <p>
 * When using this annotation, <code>JspAnnotationInspector</code> must be able access to
 * JSP 2.0 <code>FacesContext</code>. In practice this usually happens automatically, but in some cases
 * it may be necessary to 'combine remote inspections' (see the Reference Documentation).
 *
 * @author Richard Kennard
 */

@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.FIELD, ElementType.METHOD } )
public @interface UiJspAttribute
{
	String name();

	/**
	 * Value to set the attribute to.
	 * <p>
	 * Can be a String or an EL expression (in which case it must be of the form <code>#{...}</code>)
	 */

	String value();

	/**
	 * Optional EL condition with which to restrict the setting of the attribute, unless the
	 * condition evaluates to true.
	 * <p>
	 * Must be of the form <code>#{...}</code>.
	 */

	String condition() default "";
}
