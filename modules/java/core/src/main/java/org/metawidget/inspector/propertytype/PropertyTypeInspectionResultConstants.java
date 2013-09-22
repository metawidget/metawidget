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
