package org.metawidget.example.js.angular.addressbook;

import org.metawidget.util.JavascriptTestCase;

public class AngularAddressBookTest
	extends JavascriptTestCase {

	//
	// Public methods
	//

	public void testMetawidget()
		throws Exception {

		run( "src/test/js/addressbook-angular-tests.js" );
	}

	//
	// Protected methods
	//

	@Override
	protected void setUp() {

		super.setUp();

		evaluateFile( "target/addressbook-angularjs/lib/angular/angular.min.js" );
		evaluateFile( "target/addressbook-angularjs/lib/metawidget/core/metawidget-inspectors.js" );
		evaluateFile( "target/addressbook-angularjs/lib/metawidget/core/metawidget-widgetbuilders.js" );
		evaluateFile( "target/addressbook-angularjs/lib/metawidget/core/metawidget-widgetprocessors.js" );
		evaluateFile( "target/addressbook-angularjs/lib/metawidget/core/metawidget-layouts.js" );
		evaluateFile( "target/addressbook-angularjs/lib/metawidget/core/metawidget-utils.js" );
		evaluateFile( "target/addressbook-angularjs/lib/metawidget/angular/metawidget-angular.js" );
		evaluateFile( "target/addressbook-angularjs/js/app.js" );
		evaluateFile( "target/addressbook-angularjs/js/controllers.js" );
		evaluateFile( "target/addressbook-angularjs/js/services.js" );
	}
}