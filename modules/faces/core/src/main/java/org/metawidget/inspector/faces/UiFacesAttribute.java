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

package org.metawidget.inspector.faces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates an arbitrary Metawidget attribute, based on a Java Server Faces EL expression.
 * <p>
 * Unlike <code>UiFacesLookup</code>, which fits into a well-defined place within the JSF framework
 * (ie. <code>f:selectItems</code>), the <code>UiFacesAttribute</code> expression is evaluated by
 * the <code>Inspector</code>, not by the <code>Metawidget</code>. This means the
 * <code>Inspector</code> must be able access to <code>FacesContext</code>. In practice this usually
 * happens automatically, but in some cases it may be necessary to 'combine remote inspections' (see
 * the Reference Documentation).
 * <p>
 * Note in the event <code>UiFacesAttribute</code> uses an attribute name overlapping with another
 * <code>UiFacesxxx</code> annotation (eg. <code>UiFacesLookup</code>) the latter will get
 * precedence. For example...
 * <p>
 * <code>
 *
 * @UiFacesLookup( "#{foo}" )<br/>
 * @UiFacesAttribute( name = FacesInspectionResultConstants.FACES_LOOKUP, expression =
 *                    "#{!empty bar ? '' : null}" ) </code>
 *                    <p>
 *                    ...in general, you should avoid such ambiguous situations. For example, you
 *                    could instead use...
 *                    <p>
 *                    <code>
 * @UiFacesAttribute( name = FacesInspectionResultConstants.FACES_LOOKUP, expression =
 *                    "#{!empty bar ? '' : '#{foo}'}" ) </code>
 *                    <p>
 *                    Also consider using <code>FacesInspectorConfig.setInjectThis</code>.
 *                    </p>
 * @author Richard Kennard
 */

@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.FIELD, ElementType.METHOD } )
public @interface UiFacesAttribute {

	/**
	 * Attribute to set.
	 * <p>
	 * Multiple attributes can be specified if you need to set multiple attributes to the same
	 * expression.
	 */

	String[] name();

	/**
	 * Value to set the attribute to. Must be an EL expression of the form <code>#{...}</code>.
	 * <p>
	 * Note EL expressions can include conditions. For example:
	 * <p>
	 * <code>#{foo.bar ? 'baz' : null}</code>.
	 * <p>
	 * Also consider using <code>FacesInspectorConfig.setInjectThis</code>.
	 */

	String expression();
}
