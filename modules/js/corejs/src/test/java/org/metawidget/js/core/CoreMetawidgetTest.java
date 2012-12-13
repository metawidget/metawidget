package org.metawidget.js.core;

import org.metawidget.util.JavascriptTestCase;

public class CoreMetawidgetTest
	extends JavascriptTestCase {

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

		evaluateFile( "target/metawidget-corejs/lib/metawidget/core/metawidget.js" );
		evaluateFile( "target/metawidget-corejs/lib/metawidget/core/metawidget-inspectors.js" );
		evaluateFile( "target/metawidget-corejs/lib/metawidget/core/metawidget-widgetbuilders.js" );
		evaluateFile( "target/metawidget-corejs/lib/metawidget/core/metawidget-widgetprocessors.js" );
		evaluateFile( "target/metawidget-corejs/lib/metawidget/core/metawidget-layouts.js" );
		evaluateFile( "target/metawidget-corejs/lib/metawidget/core/metawidget-utils.js" );
	}
}