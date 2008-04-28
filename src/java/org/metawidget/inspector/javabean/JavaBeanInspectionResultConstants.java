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

package org.metawidget.inspector.javabean;

/**
 * JavaBean-specific element and attribute names appearing in DOMs conforming to inspection-result-1.0.xsd.
 *
 * @author Richard Kennard
 */

public final class JavaBeanInspectionResultConstants
{
	//
	//
	// Public statics
	//
	//

	/**
	 * Whether the JavaBean property has no setter method.
	 * <p>
	 * All properties without setters are also <code>READ_ONLY</code>, but
	 * not all <code>READ_ONLY</code> properties have <code>NO_SETTER</code>.
	 */

	public final static String	NO_SETTER		= "no-setter";

	public final static String	NO_GETTER		= "no-getter";

	/**
	 * Declared class attribute.
	 * <p>
	 * The fully-qualified class name declared by the property. This attribute
	 * will only appear if the declared class differs from the value's actual
	 * class (eg. the object is a subclass)
	 */

	public final static String	DECLARED_CLASS	= "declared-class";

	//
	//
	// Private constructor
	//
	//

	private JavaBeanInspectionResultConstants()
	{
		// Can never be called
	}
}
