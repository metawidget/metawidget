// Metawidget
//
// This library is dual licensed under both LGPL and a commercial
// license.
//
// LGPL: this library is free software; you can redistribute it and/or
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
//
// Commercial License: See http://metawidget.org for details

package org.metawidget.inspector.impl.propertystyle;

import java.util.Map;

import org.metawidget.iface.Immutable;

/**
 * Abstraction layer for retrieving properties from types.
 * <p>
 * Different environments have different approaches to defining what constitutes a 'property'. For
 * example, JavaBean-properties are convention-based, whereas Groovy has explicit property support.
 * Equally, some environments may have framework-specific, base class properties that should be
 * filtered out and excluded from the list of 'real' business model properties (eg. Struts
 * <code>ActionForm</code>s). Finally, some environments may define properties against something
 * other than a <code>java.lang.Class</code> (eg. <code>javassist.CtClass</code> or
 * <code>org.jboss.forge.parser.java.JavaClass</code>).
 * <p>
 * <code>PropertyStyle</code>s must be immutable (or, at least, appear that way to clients. They can
 * have caches or configuration settings internally, as long as they are threadsafe).
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public interface PropertyStyle
	extends Immutable {

	//
	// Methods
	//

	/**
	 * Gets the Properties for the given type.
	 * <p>
	 * Properties must be returned using a consistent ordering, so that both unit tests and
	 * <code>CompositeInspector</code> merging is consistent. If the underlying technology does not
	 * define an ordering, one must be imposed (eg. sorted alphabetically by name), even though this
	 * may later be overridden by other mechanisms (eg.
	 * <code>ComesAfterInspectionResultProcessor</code> sorts by <code>comes-after</code>).
	 *
	 * @return the properties for the given type. Never null.
	 */

	Map<String, Property> getProperties( String type );

	/**
	 * Traverses the given Object heirarchy using properties of the given names.
	 *
	 * @return the Object (may be null) and its declared type (not actual type). Never null.
	 *         If the declared type within the ValueAndDeclaredType is null, inspection will be
	 *         aborted
	 */

	ValueAndDeclaredType traverse( Object toTraverse, String type, boolean onlyToParent, String... names );
}
