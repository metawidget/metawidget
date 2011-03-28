// Metawidget
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
 * @author Richard Kennard
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

	public void testIsEmptyStub()
		throws Exception {

		OutputTextLayoutDecorator decorator = new OutputTextLayoutDecorator( new OutputTextLayoutDecoratorConfig().setLayout( new SimpleLayout() ) );
		Method isEmptyStub = UIComponentFlatSectionLayoutDecorator.class.getDeclaredMethod( "isEmptyStub", UIComponent.class );
		isEmptyStub.setAccessible( true );

		assertEquals( false, isEmptyStub.invoke( decorator, (Object) null ));
		assertEquals( false, isEmptyStub.invoke( decorator, new HtmlInputText() ));
		UIStub stub = new UIStub();
		assertEquals( true, isEmptyStub.invoke( decorator, stub ));
		stub.getChildren().add( new HtmlInputText() );
		assertEquals( false, isEmptyStub.invoke( decorator, stub ));
		assertEquals( true, isEmptyStub.invoke( decorator, new HtmlInputHidden() ));
	}
}
