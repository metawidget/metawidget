package org.metawidget.statically.faces.component.html.widgetbuilder;

import junit.framework.TestCase;

public class HtmlOutcomeTargetLinkTest
	extends TestCase {

	//
	// Public methods
	//

	public void testHtmlOutcomeTargetLink()
		throws Exception {

		// Hide 'value' and 'converter'

		HtmlOutcomeTargetLink widget = new HtmlOutcomeTargetLink();
		widget.setValue( "foo" );
		widget.setConverter( "bar" );
		widget.putAttribute( "baz", "abc" );

		assertEquals( "<h:link baz=\"abc\"/>", widget.toString() );
	}
}
