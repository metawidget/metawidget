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

package org.metawidget.inspector.spring;

/**
 * Spring-specific element and attribute names appearing in DOMs conforming to
 * inspection-result-1.0.xsd.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public final class SpringInspectionResultConstants {

	//
	// Public statics
	//

	public static final String	SPRING_LOOKUP				= "spring-lookup";

	public static final String	SPRING_LOOKUP_ITEM_VALUE	= "spring-lookup-item-value";

	public static final String	SPRING_LOOKUP_ITEM_LABEL	= "spring-lookup-item-label";

	//
	// Private constructor
	//

	private SpringInspectionResultConstants() {

		// Can never be called
	}
}
