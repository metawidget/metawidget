// Metawidget (licensed under LGPL)
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

	/**
	 * Gets the type of the property.
	 * <p>
	 * Type is returned as a String, so that it can express something other than a
	 * <code>java.lang.Class</code> (eg. <code>javassist.CtClass</code> or
	 * <code>org.jboss.forge.parser.java.JavaClass</code>).
	 */

	String getType();

	boolean isReadable();

	/**
	 * Read the property for the given object.
	 * <p>
	 * Used by PropertyInspector to determine subtypes, and by BaseObjectInspector to traverse the
	 * object graph.
	 */

	Object read( Object obj );

	boolean isWritable();

	/**
	 * Write the property for the given object.
	 * <p>
	 * Used by BeanUtilsBindingProcessor, and is also nicely symmetrical with <code>read</code>.
	 */

	void write( Object obj, Object value );

	/**
	 * Gets the generic type of the property, or null if the type is not parameterized.
	 * <p>
	 * Type is returned as a String, so that it can express something other than a
	 * <code>java.lang.Class</code> (eg. <code>javassist.CtClass</code> or
	 * <code>org.jboss.forge.parser.java.JavaClass</code>).
	 */

	String getGenericType();
}
