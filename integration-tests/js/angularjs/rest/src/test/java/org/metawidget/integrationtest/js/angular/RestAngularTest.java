package org.metawidget.integrationtest.js.angular;

import org.metawidget.util.AngularScenarioRunnerTestCase;

public class RestAngularTest
	extends AngularScenarioRunnerTestCase {

	//
	// Public methods
	//

	public void testRest() {

		runScenarioRunner( "http://localhost:8180/rest-angularjs/test-runner.html" );
	}
}