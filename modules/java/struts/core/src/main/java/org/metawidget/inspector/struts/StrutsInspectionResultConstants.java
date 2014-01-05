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

package org.metawidget.inspector.struts;

/**
 * Struts-specific element and attribute names appearing in DOMs conforming to
 * inspection-result-1.0.xsd.
 *
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public final class StrutsInspectionResultConstants {

	//
	// Public statics
	//

	public static final String	STRUTS_LOOKUP_NAME				= "struts-lookup-name";

	public static final String	STRUTS_LOOKUP_PROPERTY			= "struts-lookup-property";

	public static final String	STRUTS_LOOKUP_LABEL_NAME		= "struts-lookup-label-name";

	public static final String	STRUTS_LOOKUP_LABEL_PROPERTY	= "struts-lookup-label-property";

	//
	// Private constructor
	//

	private StrutsInspectionResultConstants() {

		// Can never be called
	}
}
