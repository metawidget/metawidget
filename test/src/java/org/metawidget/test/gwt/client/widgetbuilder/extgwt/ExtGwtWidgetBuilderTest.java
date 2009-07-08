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

package org.metawidget.test.gwt.client.widgetbuilder.extgwt;

import java.util.Date;

import org.metawidget.gwt.client.widgetbuilder.extgwt.ExtGwtWidgetBuilder;

import com.extjs.gxt.ui.client.widget.form.DateField;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author Richard Kennard
 */

public class ExtGwtWidgetBuilderTest
	extends GWTTestCase
{
	//
	// Public methods
	//

	@Override
	public String getModuleName()
	{
		return "org.metawidget.test.gwt.GwtMetawidgetTest";
	}

	public void testWidgetBuilder()
		throws Exception
	{
		ExtGwtWidgetBuilder widgetBuilder = new ExtGwtWidgetBuilder();

		assertTrue( false == widgetBuilder.setValue( new TextBox(), null ));

		DateField dateField = new DateField();
		Date date = new Date();
		assertTrue( true == widgetBuilder.setValue( dateField, date ));
		assertTrue( date == dateField.getValue() );
	}
}
