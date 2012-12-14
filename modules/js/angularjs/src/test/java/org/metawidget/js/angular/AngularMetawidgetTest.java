package org.metawidget.js.angular;

import org.metawidget.util.JavaScriptTestCase;

public class AngularMetawidgetTest
	extends JavaScriptTestCase {

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

		evaluateJavaScript( "src/test/js/angular.min.js" );
		evaluateJavaScript( "target/metawidget-angularjs/lib/metawidget/core/metawidget-inspectors.js" );
		evaluateJavaScript( "target/metawidget-angularjs/lib/metawidget/core/metawidget-widgetbuilders.js" );
		evaluateJavaScript( "target/metawidget-angularjs/lib/metawidget/core/metawidget-widgetprocessors.js" );
		evaluateJavaScript( "target/metawidget-angularjs/lib/metawidget/core/metawidget-layouts.js" );
		evaluateJavaScript( "target/metawidget-angularjs/lib/metawidget/core/metawidget-utils.js" );
		evaluateJavaScript( "target/metawidget-angularjs/lib/metawidget/angular/metawidget-angular.js" );
	}
}