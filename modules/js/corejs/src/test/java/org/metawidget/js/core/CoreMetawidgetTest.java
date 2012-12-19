package org.metawidget.js.core;

import org.metawidget.util.JavaScriptTestCase;

public class CoreMetawidgetTest
	extends JavaScriptTestCase {

	//
	// Public methods
	//

	public void testMetawidget()
		throws Exception {

		run( "src/test/js/metawidget-core-inspector-tests.js" );
		run( "src/test/js/metawidget-core-layout-tests.js" );
		run( "src/test/js/metawidget-core-tests.js" );
		run( "src/test/js/metawidget-core-util-tests.js" );
		run( "src/test/js/metawidget-core-widgetbuilder-tests.js" );
		run( "src/test/js/metawidget-core-widgetprocessor-tests.js" );
	}

	//
	// Protected methods
	//

	@Override
	protected void setUp() {

		super.setUp();

		evaluateJavaScript( "src/main/webapp/lib/metawidget/core/metawidget-inspectors.js" );
		evaluateJavaScript( "src/main/webapp/lib/metawidget/core/metawidget-widgetbuilders.js" );
		evaluateJavaScript( "src/main/webapp/lib/metawidget/core/metawidget-widgetprocessors.js" );
		evaluateJavaScript( "src/main/webapp/lib/metawidget/core/metawidget-layouts.js" );
		evaluateJavaScript( "src/main/webapp/lib/metawidget/core/metawidget-utils.js" );
		evaluateJavaScript( "src/main/webapp/lib/metawidget/core/metawidget.js" );
	}
}