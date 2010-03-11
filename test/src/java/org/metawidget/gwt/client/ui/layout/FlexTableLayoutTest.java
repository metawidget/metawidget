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
		FlexTableLayoutConfig config1 = new FlexTableLayoutConfig();
		FlexTableLayoutConfig config2 = new FlexTableLayoutConfig();

		assertFalse( config1.equals( "foo" ));
		assertTrue( config1.equals( config1 ) );
		assertEquals( config1, config2 );
		assertTrue( config1.hashCode() == config2.hashCode() );

		// numberOfColumns

		config1.setNumberOfColumns( 2 );
		assertTrue( 2 == config1.getNumberOfColumns() );
		assertFalse( config1.equals( config2 ));

		config2.setNumberOfColumns( 2 );
		assertEquals( config1, config2 );
		assertTrue( config1.hashCode() == config2.hashCode() );

		// tableStyleName

		config1.setTableStyleName( "table-style-name" );
		assertEquals( "table-style-name", config1.getTableStyleName() );
		assertFalse( config1.equals( config2 ));

		config2.setTableStyleName( "table-style-name" );
		assertEquals( config1, config2 );
		assertTrue( config1.hashCode() == config2.hashCode() );

		// columnStyleNames

		config1.setColumnStyleNames( "column-style-name1", "column-style-name2" );
		assertEquals( "column-style-name1", config1.getColumnStyleNames()[0] );
		assertEquals( "column-style-name2", config1.getColumnStyleNames()[1] );
		assertFalse( config1.equals( config2 ));

		config2.setColumnStyleNames( "column-style-name1", "column-style-name2" );
		assertEquals( config1, config2 );
		assertTrue( config1.hashCode() == config2.hashCode() );

		// footerStyleName

		config1.setFooterStyleName( "footer-style-name" );
		assertEquals( "footer-style-name", config1.getFooterStyleName() );
		assertFalse( config1.equals( config2 ));

		config2.setFooterStyleName( "footer-style-name" );
		assertEquals( config1, config2 );
		assertTrue( config1.hashCode() == config2.hashCode() );
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
			assertEquals( "numberOfColumns must be >= 0", e.getMessage() );
		}
	}
}
