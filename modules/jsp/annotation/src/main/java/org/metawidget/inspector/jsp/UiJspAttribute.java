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
 * Annotates an arbitrary Metawidget attribute, based on a Java Server Pages EL expression.
 * <p>
 * This annotation can only be used with JSP 2.0. When using this annotation, the
 * <code>PageContext</code> must be injected before each inspection using
 * <code>JspAnnotationInspector.setThreadLocalPageContext</code>.
 * <code>org.metawidget.jsp.tagext.MetawidgetTag</code> does this automatically.
 *
 * @author Richard Kennard
 */

@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.FIELD, ElementType.METHOD } )
public @interface UiJspAttribute {

	/**
	 * Attribute to set.
	 * <p>
	 * Multiple attributes can be specified if you need to set multiple attributes to the same
	 * expression.
	 */

	String[] name();

	/**
	 * Value to set the attribute to. Must be an EL expression of the form <code>${...}</code>.
	 * <p>
	 * Note EL expressions can include conditions. For example:
	 * <p>
	 * <code>${foo.bar ? 'baz' : null}</code>.
	 */

	String expression();
}
