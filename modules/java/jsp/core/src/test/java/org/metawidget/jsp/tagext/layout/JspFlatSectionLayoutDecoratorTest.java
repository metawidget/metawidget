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

package org.metawidget.jsp.tagext.layout;

import java.lang.reflect.Field;

import junit.framework.TestCase;

import org.metawidget.jsp.tagext.StubTag;
import org.metawidget.jsp.tagext.html.HtmlMetawidgetTag;
import org.metawidget.jsp.tagext.html.HtmlStubTag;
import org.metawidget.jsp.tagext.html.layout.HeadingTagLayoutDecorator;
import org.metawidget.jsp.tagext.html.layout.HeadingTagLayoutDecoratorConfig;
import org.metawidget.jsp.tagext.html.layout.HtmlTableLayout;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class JspFlatSectionLayoutDecoratorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testEmptyStub()
		throws Exception {

		HeadingTagLayoutDecorator layoutDecorator = new HeadingTagLayoutDecorator( new HeadingTagLayoutDecoratorConfig().setLayout( new HtmlTableLayout() ) );
		assertEquals( false, layoutDecorator.isIgnored( null ) );
		assertEquals( false, layoutDecorator.isIgnored( new HtmlMetawidgetTag() ) );

		StubTag stub = new HtmlStubTag();
		assertEquals( true, layoutDecorator.isIgnored( stub ) );

		Field field = StubTag.class.getDeclaredField( "mSavedBodyContent" );
		field.setAccessible( true );
		field.set( stub, "Foo" );

		assertEquals( false, layoutDecorator.isIgnored( stub ) );
	}
}
