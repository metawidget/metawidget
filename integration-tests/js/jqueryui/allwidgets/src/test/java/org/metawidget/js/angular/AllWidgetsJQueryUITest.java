package org.metawidget.js.angular;

import org.metawidget.util.JavaScriptTestCase;

public class AllWidgetsJQueryUITest
	extends JavaScriptTestCase {

	//
	// Public methods
	//

	public void testAllWidgets()
		throws Exception {

		evaluateHtml( "target/allwidgets-jqueryui/index.html" );
		run( "src/test/js/allwidgets-jqueryui-tests.js" );
	}
}