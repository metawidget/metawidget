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

package org.metawidget.integrationtest.android.allwidgets.test;

import org.metawidget.integrationtest.android.allwidgets.AllWidgetsActivity;

import android.test.ActivityInstrumentationTestCase2;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class AllWidgetsActivityTest
	extends ActivityInstrumentationTestCase2<AllWidgetsActivity> {

	//
	// Constructor
	//
	
	public AllWidgetsActivityTest() {

		super( AllWidgetsActivity.class.getPackage().getName(), AllWidgetsActivity.class );
	}

	//
	// Public methods
	//
	
	public void testActivity() {

		AllWidgetsActivity activity = getActivity();
		assertNotNull( activity );
	}
}
