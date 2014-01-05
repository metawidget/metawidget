// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

package org.metawidget.inspector.impl.propertystyle;

import org.metawidget.inspector.impl.Trait;

/**
 * Interface over getter/setter-based, field-based, or Groovy-based properties, so that
 * <code>Inspectors</code> can treat them all the same.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
