// Metawidget (licensed under LGPL)
//
// This library is free software; you can redistribute it and/or
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

		initializeEnvJs();

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