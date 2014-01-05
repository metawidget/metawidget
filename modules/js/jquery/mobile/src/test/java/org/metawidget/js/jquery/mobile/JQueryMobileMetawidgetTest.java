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

package org.metawidget.js.jquery.mobile;

import org.metawidget.util.JavaScriptTestCase;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class JQueryMobileMetawidgetTest
	extends JavaScriptTestCase {

	//
	// Public methods
	//

	public void testMetawidget()
		throws Exception {

		run( "src/test/js/metawidget-jquerymobile-tests.js" );
	}

	//
	// Protected methods
	//

	@Override
	protected void setUp() {

		super.setUp();

		initializeEnvJs();

		evaluateResource( "/js/jquery.mobile-1.3.2.min.js" );
		evaluateJavaScript( "target/metawidget-jquerymobile/lib/metawidget/core/metawidget.js" );
		evaluateJavaScript( "target/metawidget-jquerymobile/lib/metawidget/core/metawidget-inspectors.js" );
		evaluateJavaScript( "target/metawidget-jquerymobile/lib/metawidget/core/metawidget-widgetbuilders.js" );
		evaluateJavaScript( "target/metawidget-jquerymobile/lib/metawidget/core/metawidget-widgetprocessors.js" );
		evaluateJavaScript( "target/metawidget-jquerymobile/lib/metawidget/core/metawidget-layouts.js" );
		evaluateJavaScript( "target/metawidget-jquerymobile/lib/metawidget/core/metawidget-utils.js" );
		evaluateJavaScript( "src/main/webapp/lib/metawidget/jquery.mobile/metawidget-jquerymobile.js" );
	}
}