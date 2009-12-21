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

package org.metawidget.gwt.client.ui.layout;

import org.metawidget.gwt.client.ui.Stub;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author Richard Kennard
 */

public class GwtFlatSectionLayoutDecoratorTest
	extends GWTTestCase
{
	//
	// Public methods
	//

	@Override
	public String getModuleName()
	{
		return "org.metawidget.gwt.GwtMetawidgetTest";
	}

	public void testEmptyStub()
	{
		LabelLayoutDecorator layoutDecorator = new LabelLayoutDecorator( new LabelLayoutDecoratorConfig().setLayout( new FlexTableLayout() ));
		assertTrue( false == layoutDecorator.isEmptyStub( null ) );
		assertTrue( false == layoutDecorator.isEmptyStub( new CheckBox() ) );

		Stub stub = new Stub();
		assertTrue( true == layoutDecorator.isEmptyStub( stub ) );

		stub.add( new TextBox() );
		assertTrue( false == layoutDecorator.isEmptyStub( stub ) );
	}
}
