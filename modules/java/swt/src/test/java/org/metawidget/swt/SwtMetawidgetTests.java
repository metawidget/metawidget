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

package org.metawidget.swt;

import org.eclipse.swt.widgets.Display;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class SwtMetawidgetTests {

	//
	// Public statics
	//

	/**
	 * Need a single Display within the whole Thread of JUnit tests, else get a
	 * 'org.eclipse.swt.SWTException: Invalid thread access'
	 */

	public static final Display	TEST_DISPLAY	= new Display();
}