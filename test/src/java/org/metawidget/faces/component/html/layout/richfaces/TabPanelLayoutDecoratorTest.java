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

import org.metawidget.faces.component.layout.SimpleLayout;
import org.metawidget.layout.decorator.LayoutDecoratorTest;

/**
 * @author Richard Kennard
 */

public class TabPanelLayoutDecoratorTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testConfig()
	{
		TabPanelLayoutDecoratorConfig config1 = new TabPanelLayoutDecoratorConfig();
		TabPanelLayoutDecoratorConfig config2 = new TabPanelLayoutDecoratorConfig();

		assertTrue( !config1.equals( "foo" ) );
		assertTrue( config1.equals( config1 ) );
		assertTrue( config1.equals( config2 ) );
		assertTrue( config1.hashCode() == config2.hashCode() );

		// tabPlacement

		config1.setHeaderAlignment( "right" );
		assertTrue( "right".equals( config1.getHeaderAlignment() ));
		assertTrue( !config1.equals( config2 ) );
		assertTrue( config1.hashCode() != config2.hashCode() );

		config2.setHeaderAlignment( "right" );
		assertTrue( config1.equals( config2 ) );
		assertTrue( config1.hashCode() == config2.hashCode() );

		// superclass

		LayoutDecoratorTest.testConfig( config1, config2, new SimpleLayout() );
	}
}
