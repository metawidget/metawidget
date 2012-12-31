package org.metawidget.js.jqueryui;

import org.metawidget.util.JavaScriptTestCase;

public class JQueryUIMetawidgetTest
	extends JavaScriptTestCase {

	//
	// Public methods
	//

	public void testMetawidget()
		throws Exception {

		run( "src/test/js/metawidget-jqueryui-tests.js" );
	}

	//
	// Protected methods
	//

	@Override
	protected void setUp() {

		super.setUp();

		initializeJQuery();

		evaluateResource( "/js/jquery-ui-1.9.2.custom.min.js" );
		evaluateJavaScript( "target/metawidget-jqueryui/lib/metawidget/core/metawidget.js" );
		evaluateJavaScript( "target/metawidget-jqueryui/lib/metawidget/core/metawidget-inspectors.js" );
		evaluateJavaScript( "target/metawidget-jqueryui/lib/metawidget/core/metawidget-widgetbuilders.js" );
		evaluateJavaScript( "target/metawidget-jqueryui/lib/metawidget/core/metawidget-widgetprocessors.js" );
		evaluateJavaScript( "target/metawidget-jqueryui/lib/metawidget/core/metawidget-layouts.js" );
		evaluateJavaScript( "target/metawidget-jqueryui/lib/metawidget/core/metawidget-utils.js" );
		evaluateJavaScript( "src/main/webapp/lib/metawidget/jquery-ui/metawidget-jqueryui.js" );
	}
}