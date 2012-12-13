package org.metawidget.js.angular;

import org.metawidget.util.JavascriptTestCase;

public class AngularMetawidgetTest
	extends JavascriptTestCase {

	//
	// Public methods
	//

	public void testMetawidget()
		throws Exception {

		run( "src/test/js/metawidget-angular-tests.js" );
	}

	//
	// Protected methods
	//

	@Override
	protected void setUp() {

		super.setUp();

		evaluateFile( "src/test/js/angular.min.js" );
		evaluateFile( "target/metawidget-angularjs/lib/metawidget/core/metawidget-inspectors.js" );
		evaluateFile( "target/metawidget-angularjs/lib/metawidget/core/metawidget-widgetbuilders.js" );
		evaluateFile( "target/metawidget-angularjs/lib/metawidget/core/metawidget-widgetprocessors.js" );
		evaluateFile( "target/metawidget-angularjs/lib/metawidget/core/metawidget-layouts.js" );
		evaluateFile( "target/metawidget-angularjs/lib/metawidget/core/metawidget-utils.js" );
		evaluateFile( "target/metawidget-angularjs/lib/metawidget/angular/metawidget-angular.js" );
	}
}