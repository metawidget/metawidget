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

package org.metawidget.faces;

import javax.faces.component.html.HtmlInputText;

import junit.framework.TestCase;

/**
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

public class FacesUtilsTest
	extends TestCase {

	//
	// Public methods
	//

	public void testWrapping()
		throws Exception {

		assertEquals( true, FacesUtils.isExpression( "#{foo}" ) );
		assertEquals( false, FacesUtils.isExpression( "foo" ) );

		assertEquals( "foo", FacesUtils.unwrapExpression( "#{foo}" ) );
		assertEquals( "foo", FacesUtils.unwrapExpression( "foo" ) );
		assertEquals( "#{foo", FacesUtils.unwrapExpression( "#{foo" ) );
		assertEquals( "{foo}", FacesUtils.unwrapExpression( "{foo}" ) );
		assertEquals( "foo}", FacesUtils.unwrapExpression( "foo}" ) );

		assertEquals( "#{foo}", FacesUtils.wrapExpression( "foo" ) );
		assertEquals( "#{foo}", FacesUtils.wrapExpression( "#{foo}" ) );
		assertEquals( "#{#{foo}", FacesUtils.wrapExpression( "#{foo" ) );
		assertEquals( "#{{foo}}", FacesUtils.wrapExpression( "{foo}" ) );
		assertEquals( "#{foo}}", FacesUtils.wrapExpression( "foo}" ) );
	}

	public void testSetStyleAndStyleClass()
		throws Exception {

		HtmlInputText component = new HtmlInputText();
		FacesUtils.setStyleAndStyleClass( component, "style1", "styleClass1" );
		assertEquals( "style1", component.getStyle() );
		assertEquals( "styleClass1", component.getStyleClass() );

		FacesUtils.setStyleAndStyleClass( component, "style2", "styleClass2" );
		assertEquals( "style1 style2", component.getStyle() );
		assertEquals( "styleClass1 styleClass2", component.getStyleClass() );

		FacesUtils.setStyleAndStyleClass( component, "style3", "styleClass3" );
		assertEquals( "style1 style2 style3", component.getStyle() );
		assertEquals( "styleClass1 styleClass2 styleClass3", component.getStyleClass() );

		FacesUtils.setStyleAndStyleClass( component, "style2", "styleClass2" );
		assertEquals( "style1 style2 style3", component.getStyle() );
		assertEquals( "styleClass1 styleClass2 styleClass3", component.getStyleClass() );
	}
}
