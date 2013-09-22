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
