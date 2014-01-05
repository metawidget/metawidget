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

package org.metawidget.faces.component.html.layout;

import java.lang.reflect.Method;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.component.html.HtmlInputText;

import junit.framework.TestCase;

import org.metawidget.faces.component.UIStub;
import org.metawidget.faces.component.layout.SimpleLayout;
import org.metawidget.faces.component.layout.UIComponentFlatSectionLayoutDecorator;
import org.metawidget.util.MetawidgetTestUtils;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class OutputTextLayoutDecoratorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testConfig() {

		MetawidgetTestUtils.testEqualsAndHashcode( OutputTextLayoutDecoratorConfig.class, new OutputTextLayoutDecoratorConfig() {
			// Subclass
		} );
	}

	public void testIgnored()
		throws Exception {

		OutputTextLayoutDecorator decorator = new OutputTextLayoutDecorator( new OutputTextLayoutDecoratorConfig().setLayout( new SimpleLayout() ) );
		Method isIgnored = UIComponentFlatSectionLayoutDecorator.class.getDeclaredMethod( "isIgnored", UIComponent.class );
		isIgnored.setAccessible( true );

		assertEquals( false, isIgnored.invoke( decorator, (Object) null ));
		assertEquals( false, isIgnored.invoke( decorator, new HtmlInputText() ));
		UIStub stub = new UIStub();
		assertEquals( true, isIgnored.invoke( decorator, stub ));
		stub.getChildren().add( new HtmlInputText() );
		assertEquals( false, isIgnored.invoke( decorator, stub ));
		assertEquals( true, isIgnored.invoke( decorator, new HtmlInputHidden() ));
	}
}
