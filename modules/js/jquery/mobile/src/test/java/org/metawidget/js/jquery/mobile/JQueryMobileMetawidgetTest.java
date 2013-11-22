// Metawidget
//
// This library is dual licensed under both LGPL and a commercial
// license.
//
// LGPL: this library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
//
// Commercial License: See http://metawidget.org for details

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