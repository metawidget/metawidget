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

package org.metawidget.swing.layout;

import javax.swing.SwingConstants;

import junit.framework.TestCase;

/**
 * @author Richard Kennard
 */

public class TabbedPaneSectionLayoutTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testConfig()
	{
		TabbedPaneSectionLayoutConfig config1 = new TabbedPaneSectionLayoutConfig();
		TabbedPaneSectionLayoutConfig config2 = new TabbedPaneSectionLayoutConfig();

		assertTrue( !config1.equals( "foo" ) );
		assertTrue( config1.equals( config2 ) );
		assertTrue( config1.hashCode() == config2.hashCode() );

		// tabPlacement

		config1.setTabPlacement( SwingConstants.TOP );
		assertTrue( SwingConstants.TOP == config1.getTabPlacement() );
		assertTrue( !config1.equals( config2 ) );
		assertTrue( config1.hashCode() != config2.hashCode() );

		config2.setTabPlacement( SwingConstants.TOP );
		assertTrue( config1.equals( config2 ) );
		assertTrue( config1.hashCode() == config2.hashCode() );
	}
}
