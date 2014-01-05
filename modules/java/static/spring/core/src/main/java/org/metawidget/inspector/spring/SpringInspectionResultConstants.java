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
