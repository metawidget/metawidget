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

import java.lang.reflect.Type;

import org.metawidget.inspector.impl.Trait;

/**
 * Interface over getter/setter-based, field-based, or Groovy-based properties, so that
 * <code>Inspectors</code> can treat them all the same.
 *
 * @author Richard Kennard
 */

public interface Property
	extends Trait {

	//
	// Methods
	//

	Class<?> getType();

	boolean isReadable();

	/**
	 * Read the given property for the given object.
	 * <p>
	 * Used by PropertyInspector to determine subtypes, and by BaseObjectInspector to traverse the
	 * object graph.
	 */

	Object read( Object obj );

	boolean isWritable();

	Type getGenericType();
}
