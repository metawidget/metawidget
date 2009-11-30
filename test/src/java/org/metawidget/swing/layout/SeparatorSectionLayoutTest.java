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

import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import junit.framework.TestCase;

import org.metawidget.inspector.annotation.UiSection;
import org.metawidget.layout.delegate.DelegateLayoutTest;
import org.metawidget.swing.SwingMetawidget;

/**
 * @author Richard Kennard
 */

public class SeparatorSectionLayoutTest
	extends TestCase
{
	//
	// Public methods
	//

	public void testConfig()
	{
		SeparatorSectionLayoutConfig config1 = new SeparatorSectionLayoutConfig();
		SeparatorSectionLayoutConfig config2 = new SeparatorSectionLayoutConfig();

		assertTrue( !config1.equals( "foo" ) );
		assertTrue( config1.equals( config2 ) );
		assertTrue( config1.hashCode() == config2.hashCode() );

		// tabPlacement

		config1.setAlignment( SwingConstants.RIGHT );
		assertTrue( SwingConstants.RIGHT == config1.getAlignment() );
		assertTrue( !config1.equals( config2 ) );
		assertTrue( config1.hashCode() != config2.hashCode() );

		config2.setAlignment( SwingConstants.RIGHT );
		assertTrue( config1.equals( config2 ) );
		assertTrue( config1.hashCode() == config2.hashCode() );

		// superclass

		DelegateLayoutTest.testConfig( new SeparatorSectionLayoutConfig(), new SeparatorSectionLayoutConfig(), new org.metawidget.swing.layout.GridBagLayout() );
	}

	public void testAlignment()
	{
		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setToInspect( new Foo() );

		JPanel panel = (JPanel) metawidget.getComponent( 0 );
		assertTrue( "Section".equals( ((JLabel) panel.getComponent( 0 )).getText() ));
		assertTrue( 0 == ( (GridBagLayout) panel.getLayout() ).getConstraints( panel.getComponent( 0 ) ).insets.left );
		assertTrue( 5 == ( (GridBagLayout) panel.getLayout() ).getConstraints( panel.getComponent( 0 ) ).insets.right );
		assertTrue( panel.getComponent( 1 ) instanceof JSeparator );
		assertTrue( "Bar:".equals( ((JLabel) metawidget.getComponent( 1 )).getText() ));
		assertTrue( metawidget.getComponent( 2 ) instanceof JTextField );
		assertTrue( metawidget.getComponent( 3 ) instanceof JPanel );
		assertTrue( 4 == metawidget.getComponentCount() );

		metawidget.setMetawidgetLayout( new SeparatorSectionLayout( new SeparatorSectionLayoutConfig().setAlignment( SwingConstants.RIGHT ).setLayout( new org.metawidget.swing.layout.GridBagLayout() )) );
		panel = (JPanel) metawidget.getComponent( 0 );
		assertTrue( "Section".equals( ((JLabel) panel.getComponent( 0 )).getText() ));
		assertTrue( 1 == ( (GridBagLayout) panel.getLayout() ).getConstraints( panel.getComponent( 0 ) ).gridx );
		assertTrue( 5 == ( (GridBagLayout) panel.getLayout() ).getConstraints( panel.getComponent( 0 ) ).insets.left );
		assertTrue( 0 == ( (GridBagLayout) panel.getLayout() ).getConstraints( panel.getComponent( 0 ) ).insets.right );
		assertTrue( panel.getComponent( 1 ) instanceof JSeparator );
		assertTrue( 0 == ( (GridBagLayout) panel.getLayout() ).getConstraints( panel.getComponent( 1 ) ).gridx );
		assertTrue( "Bar:".equals( ((JLabel) metawidget.getComponent( 1 )).getText() ));
		assertTrue( metawidget.getComponent( 2 ) instanceof JTextField );
		assertTrue( metawidget.getComponent( 3 ) instanceof JPanel );
		assertTrue( 4 == metawidget.getComponentCount() );
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
