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

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import junit.framework.TestCase;

import org.metawidget.inspector.annotation.UiSection;
import org.metawidget.layout.delegate.DelegateLayoutTest;
import org.metawidget.swing.SwingMetawidget;

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

		// superclass

		DelegateLayoutTest.testConfig( new SeparatorSectionLayoutConfig(), new SeparatorSectionLayoutConfig(), new org.metawidget.swing.layout.GridBagLayout() );
	}

	public void testTabPlacement()
	{
		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setMetawidgetLayout( new TabbedPaneSectionLayout( new TabbedPaneSectionLayoutConfig().setLayout( new org.metawidget.swing.layout.GridBagLayout() )) );
		metawidget.setToInspect( new Foo() );

		JTabbedPane tabbedPane = (JTabbedPane) metawidget.getComponent( 0 );
		assertTrue( "Section".equals( tabbedPane.getTitleAt( 0 )));
		assertTrue( SwingConstants.TOP == tabbedPane.getTabPlacement() );
		JPanel panel = (JPanel) tabbedPane.getComponent( 0 );
		assertTrue( "Bar:".equals( ((JLabel) panel.getComponent( 0 )).getText() ));
		assertTrue( panel.getComponent( 1 ) instanceof JTextField );
		assertTrue( panel.getComponent( 2 ) instanceof JPanel );
		assertTrue( 3 == panel.getComponentCount() );

		metawidget.setMetawidgetLayout( new TabbedPaneSectionLayout( new TabbedPaneSectionLayoutConfig().setTabPlacement( SwingConstants.BOTTOM ).setLayout( new org.metawidget.swing.layout.GridBagLayout() )) );
		tabbedPane = (JTabbedPane) metawidget.getComponent( 0 );
		assertTrue( "Section".equals( tabbedPane.getTitleAt( 0 )));
		assertTrue( SwingConstants.BOTTOM == tabbedPane.getTabPlacement() );
		panel = (JPanel) tabbedPane.getComponent( 0 );
		assertTrue( "Bar:".equals( ((JLabel) panel.getComponent( 0 )).getText() ));
		assertTrue( panel.getComponent( 1 ) instanceof JTextField );
		assertTrue( panel.getComponent( 2 ) instanceof JPanel );
		assertTrue( 3 == panel.getComponentCount() );
	}

	//
	// Inner class
	//

	static class Foo
	{
		@UiSection( "Section" )
		public String bar;
	}
}
