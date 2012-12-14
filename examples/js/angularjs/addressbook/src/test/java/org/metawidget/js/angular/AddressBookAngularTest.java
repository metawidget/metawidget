package org.metawidget.js.angular;

import org.metawidget.util.JavaScriptTestCase;

public class AddressBookAngularTest
	extends JavaScriptTestCase {

	//
	// Public methods
	//

	public void testAddressBook()
		throws Exception {

		run( "src/test/js/addressbook-angular-tests.js" );
	}

	//
	// Protected methods
	//

	@Override
	protected void setUp() {

		super.setUp();

		evaluateJavaScript( "target/addressbook-angularjs/lib/angular/angular.min.js" );
		evaluateJavaScript( "target/addressbook-angularjs/lib/metawidget/core/metawidget.js" );
		evaluateJavaScript( "target/addressbook-angularjs/lib/metawidget/core/metawidget-inspectors.js" );
		evaluateJavaScript( "target/addressbook-angularjs/lib/metawidget/core/metawidget-widgetbuilders.js" );
		evaluateJavaScript( "target/addressbook-angularjs/lib/metawidget/core/metawidget-widgetprocessors.js" );
		evaluateJavaScript( "target/addressbook-angularjs/lib/metawidget/core/metawidget-layouts.js" );
		evaluateJavaScript( "target/addressbook-angularjs/lib/metawidget/core/metawidget-utils.js" );
		evaluateJavaScript( "target/addressbook-angularjs/lib/metawidget/angular/metawidget-angular.js" );
		evaluateJavaScript( "target/addressbook-angularjs/js/app.js" );
		evaluateJavaScript( "target/addressbook-angularjs/js/controllers.js" );
		evaluateJavaScript( "target/addressbook-angularjs/js/services.js" );
		
		evaluateHtml( "target/addressbook-angularjs/index.html" );
	}
}