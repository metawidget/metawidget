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

import org.metawidget.layout.iface.LayoutException;
import org.metawidget.util.TestUtils;

import com.google.gwt.junit.client.GWTTestCase;

/**
 * @author Richard Kennard
 */

public class FlexTableLayoutTest
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

	public void testConfig()
	{
		TestUtils.testEqualsAndHashcode( FlexTableLayoutConfig.class );
	}

	public void testNumberOfColumns()
	{
		FlexTableLayoutConfig config = new FlexTableLayoutConfig();

		try
		{
			config.setNumberOfColumns( -1 );
			assertTrue( false );
		}
		catch( LayoutException e )
		{
			assertTrue( "numberOfColumns must be >= 0".equals( e.getMessage() ));
		}
	}
}
