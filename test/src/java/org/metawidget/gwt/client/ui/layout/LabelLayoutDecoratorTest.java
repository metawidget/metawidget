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

import com.google.gwt.junit.client.GWTTestCase;

/**
 * @author Richard Kennard
 */

public class LabelLayoutDecoratorTest
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
		LabelLayoutDecoratorConfig config1 = new LabelLayoutDecoratorConfig();
		LabelLayoutDecoratorConfig config2 = new LabelLayoutDecoratorConfig();

		assertTrue( !config1.equals( "foo" ) );
		assertTrue( config1.equals( config2 ) );
		assertTrue( config1.hashCode() == config2.hashCode() );

		// styleName

		config1.setStyleName( "section-style-name" );
		assertTrue( "section-style-name".equals( config1.getStyleName() ) );
		assertTrue( !config1.equals( config2 ) );
		assertTrue( config1.hashCode() != config2.hashCode() );

		config2.setStyleName( "section-style-name" );
		assertTrue( config1.equals( config2 ) );
		assertTrue( config1.hashCode() == config2.hashCode() );

		// superclass

		assertTrue( !config1.equals( "foo" ) );
		assertTrue( config1.equals( config2 ) );
		assertTrue( config1.hashCode() == config2.hashCode() );

		// layout

		FlexTableLayout delegate = new FlexTableLayout();

		config1.setLayout( delegate );
		assertTrue( delegate == config1.getLayout() );
		assertTrue( !config1.equals( config2 ) );
		assertTrue( config1.hashCode() != config2.hashCode() );

		config2.setLayout( delegate );
		assertTrue( config1.equals( config2 ) );
		assertTrue( config1.hashCode() == config2.hashCode() );
	}
}
