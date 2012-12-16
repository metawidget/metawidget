package org.metawidget.js.core;

import org.metawidget.util.JavaScriptTestCase;

public class CoreMetawidgetTest
	extends JavaScriptTestCase {

	//
	// Public methods
	//

	public void testMetawidget()
		throws Exception {

		run( "src/test/js/metawidget-core-tests.js" );
	}

	//
	// Protected methods
	//

	@Override
	protected void setUp() {

		super.setUp();

		evaluateJavaScript( "src/main/webapp/lib/metawidget/core/metawidget.js" );
		evaluateJavaScript( "src/main/webapp/lib/metawidget/core/metawidget-inspectors.js" );
		evaluateJavaScript( "src/main/webapp/lib/metawidget/core/metawidget-widgetbuilders.js" );
		evaluateJavaScript( "src/main/webapp/lib/metawidget/core/metawidget-widgetprocessors.js" );
		evaluateJavaScript( "src/main/webapp/lib/metawidget/core/metawidget-layouts.js" );
		evaluateJavaScript( "src/main/webapp/lib/metawidget/core/metawidget-utils.js" );
	}
}