package org.metawidget.integrationtest.js.angular;

import org.metawidget.util.AngularScenarioRunnerTestCase;

public class AddressBookAngularTest
	extends AngularScenarioRunnerTestCase {

	//
	// Public methods
	//

	public void testRest() {

		runScenarioRunner( "http://localhost:8180/addressbook-angularjs/test-runner.html" );
	}
}