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

package org.metawidget.faces.component.html.layout.richfaces;

import junit.framework.TestCase;

/**
 * @author Richard Kennard
 */

public class RichFacesLayoutTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testConfig()
	{
		RichFacesLayoutConfig config1 = new RichFacesLayoutConfig();
		RichFacesLayoutConfig config2 = new RichFacesLayoutConfig();

		assertTrue( !config1.equals( "foo" ) );
		assertTrue( config1.equals( config2 ) );
		assertTrue( config1.hashCode() == config2.hashCode() );

		// sectionStyle

		config1.setSectionStyle( RichFacesLayoutConfig.SECTION_AS_TAB );
		assertTrue( RichFacesLayoutConfig.SECTION_AS_TAB == config1.getSectionStyle() );
		assertTrue( !config1.equals( config2 ) );
		assertTrue( config1.hashCode() != config2.hashCode() );

		config2.setSectionStyle( RichFacesLayoutConfig.SECTION_AS_TAB );
		assertTrue( config1.equals( config2 ) );
		assertTrue( config1.hashCode() == config2.hashCode() );
	}
}
