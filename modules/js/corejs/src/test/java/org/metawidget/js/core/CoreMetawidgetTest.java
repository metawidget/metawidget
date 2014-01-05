// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

package org.metawidget.js.core;

import org.metawidget.util.JavaScriptTestCase;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

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