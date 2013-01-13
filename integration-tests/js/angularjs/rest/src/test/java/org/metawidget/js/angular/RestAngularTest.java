package org.metawidget.js.angular;

import org.metawidget.util.JavaScriptTestCase;

public class RestAngularTest
	extends JavaScriptTestCase {

	//
	// Public methods
	//

	public void testRest()
		throws Exception {

		evaluateHtml( "http://localhost:8180/rest-angularjs/index.html" );
		run( "src/test/js/rest-angular-tests.js" );
	}
}