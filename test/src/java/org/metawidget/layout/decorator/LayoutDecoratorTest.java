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

package org.metawidget.layout.decorator;

import javax.swing.JComponent;

import junit.framework.TestCase;

import org.metawidget.layout.iface.Layout;
import org.metawidget.layout.iface.LayoutException;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.swing.layout.GridBagLayout;

/**
 * @author Richard Kennard
 */

public class LayoutDecoratorTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testConfig()
	{
		testConfig( new LayoutDecoratorConfig<JComponent, JComponent, SwingMetawidget>(), new LayoutDecoratorConfig<JComponent, JComponent, SwingMetawidget>(), new GridBagLayout() );
	}

	public static <W, C extends W, M extends C> void testConfig( LayoutDecoratorConfig<W, C, M> config1, LayoutDecoratorConfig<W,C, M> config2, Layout<W, C, M> delegate )
	{
		assertTrue( !config1.equals( "foo" ) );
		assertTrue( config1.equals( config1 ) );
		assertTrue( config1.equals( config2 ) );
		assertTrue( config1.hashCode() == config2.hashCode() );

		// layout

		config1.setLayout( delegate );
		assertTrue( delegate == config1.getLayout() );
		assertTrue( !config1.equals( config2 ) );

		config2.setLayout( delegate );
		assertTrue( config1.equals( config2 ) );
		assertTrue( config1.hashCode() == config2.hashCode() );
	}

	public void testLayoutDecorator()
		throws Exception
	{
		try
		{
			new LayoutDecorator<JComponent, JComponent, SwingMetawidget>( new LayoutDecoratorConfig<JComponent, JComponent, SwingMetawidget>() )
			{
				// Just a LayoutDecorator
			};
		}
		catch ( LayoutException e )
		{
			assertTrue( "org.metawidget.layout.decorator.LayoutDecoratorTest$1 needs a Layout to decorate (use org.metawidget.layout.decorator.LayoutDecoratorConfig.setLayout)".equals( e.getMessage() ) );
		}
	}
}