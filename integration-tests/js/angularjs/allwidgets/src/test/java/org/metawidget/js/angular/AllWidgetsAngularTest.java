package org.metawidget.js.angular;

import org.metawidget.util.JavaScriptTestCase;

public class AllWidgetsAngularTest
	extends JavaScriptTestCase {

	//
	// Public methods
	//

	public void testAllWidgets()
		throws Exception {

		run( "src/test/js/allwidgets-angular-tests.js" );
	}

	//
	// Protected methods
	//

	@Override
	protected void setUp() {

		super.setUp();

		evaluateJavaScript( "target/allwidgets-angularjs/lib/angular/angular.min.js" );
		evaluateJavaScript( "target/allwidgets-angularjs/lib/metawidget/core/metawidget.js" );
		evaluateJavaScript( "target/allwidgets-angularjs/lib/metawidget/core/metawidget-inspectors.js" );
		evaluateJavaScript( "target/allwidgets-angularjs/lib/metawidget/core/metawidget-widgetbuilders.js" );
		evaluateJavaScript( "target/allwidgets-angularjs/lib/metawidget/core/metawidget-widgetprocessors.js" );
		evaluateJavaScript( "target/allwidgets-angularjs/lib/metawidget/core/metawidget-layouts.js" );
		evaluateJavaScript( "target/allwidgets-angularjs/lib/metawidget/core/metawidget-utils.js" );
		evaluateJavaScript( "target/allwidgets-angularjs/lib/metawidget/angular/metawidget-angular.js" );

		evaluateHtml( "target/allwidgets-angularjs/index.html" );
	}
}