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

package org.metawidget.inspector.propertytype;

/**
 * PropertyType-specific element and attribute names appearing in DOMs conforming to
 * inspection-result-1.0.xsd.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public final class PropertyTypeInspectionResultConstants {

	//
	// Public statics
	//

	public static final String	NO_GETTER		= "no-getter";

	/**
	 * Actual class of the property's value.
	 * <p>
	 * This attribute will only appear if the actual class differs from the declared class (eg. is a
	 * subclass).
	 * <p>
	 * Note we don't do this the other way around (eg. return the actual class as TYPE and have a,
	 * say, DECLARED_CLASS attribute) because the type must be consistent between Object and
	 * XML-based inspectors. In particular, we don't want to use a proxied class as the 'type'.
	 */

	public static final String	ACTUAL_CLASS	= "actual-class";

	//
	// Private constructor
	//

	private PropertyTypeInspectionResultConstants() {

		// Can never be called
	}
}
