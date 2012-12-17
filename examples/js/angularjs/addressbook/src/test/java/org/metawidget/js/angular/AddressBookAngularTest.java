package org.metawidget.js.angular;

import org.metawidget.util.JavaScriptTestCase;

public class AddressBookAngularTest
	extends JavaScriptTestCase {

	//
	// Public methods
	//

	public void testAddressBook()
		throws Exception {

		evaluateHtml( "target/addressbook-angularjs/index.html" );
		run( "src/test/js/addressbook-angular-tests.js" );
	}
}