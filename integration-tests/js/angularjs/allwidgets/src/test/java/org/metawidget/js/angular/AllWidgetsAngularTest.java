package org.metawidget.js.angular;

import org.metawidget.util.JavaScriptTestCase;

public class AllWidgetsAngularTest
	extends JavaScriptTestCase {

	//
	// Public methods
	//

	public void testAllWidgets()
		throws Exception {

		evaluateHtml( "target/allwidgets-angularjs/index.html" );
		run( "src/test/js/allwidgets-angular-tests.js" );
	}

	//
	// Protected methods
	//

	@Override
	protected void setUp() {

		super.setUp();

		initializeEnvJs();
		evaluateResource( "/js/angular-scenario.js" );
	}
}