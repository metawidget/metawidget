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

package org.metawidget.inspector.impl.propertystyle;

import java.util.Map;

import org.metawidget.iface.Immutable;

/**
 * Abstraction layer for retrieving properties from Classes.
 * <p>
 * Different environments have different approaches to defining what constitutes a 'property'. For
 * example, JavaBean-properties are convention-based, whereas Groovy has explicit property support.
 * Equally, some environments may have framework-specific, base class properties that should be
 * filtered out and excluded from the list of 'real' business model properties (eg. Struts
 * <code>ActionForm</code>s).
 * <p>
 * <code>PropertyStyle</code>s must be immutable (or, at least, appear that way to clients. They can
 * have caches or configuration settings internally, as long as they are threadsafe).
 *
 * @author Richard Kennard
 */

public interface PropertyStyle
	extends Immutable
{
	//
	// Methods
	//

	/**
	 * Gets the Properties for the given Class.
	 * <p>
	 * Properties must be returned using a consistent ordering, so that both unit tests and
	 * <code>CompositeInspector</code> merging is consistent. If the underlying platform does not
	 * define an ordering, one must be imposed (eg. sorted alphabetically by name), even though this
	 * may later be overridden by other mechanisms (eg. <code>PropertyTypeInspector</code> sorts by
	 * <code>UiComesAfter</code>).
	 *
	 * @return the properties for the given Class. Never null.
	 */

	Map<String, Property> getProperties( Class<?> clazz );
}
