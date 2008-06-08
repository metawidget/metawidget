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

/**
 * Abstraction layer for retrieving properties from Classes.
 * <p>
 * Different environments have different approaches to defining what constitutes a 'property'. For
 * example, JavaBean-properties are convention-based, whereas Groovy has explicit property support.
 *
 * @author Richard Kennard
 */

public interface PropertyStyle
{
	//
	//
	// Methods
	//
	//

	/**
	 * Gets the Properties for the given Class.
	 * <p>
	 * Properties must be returned using a consistent ordering, so that both unit tests and
	 * <code>CompositeInspector</code> merging is consistent. If the underlying platform does not
	 * define an ordering, one must be imposed (eg. sorted alphabetically by name).
	 *
	 * @return the properties for the given Class. Never null.
	 */

	Map<String, Property> getProperties( Class<?> clazz );
}
