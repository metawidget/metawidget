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

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import junit.framework.TestCase;

import org.metawidget.inspector.annotation.UiLarge;
import org.metawidget.inspector.annotation.UiSection;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.MetawidgetTestUtils;

/**
 * @author Richard Kennard
 */

public class TabbedPaneLayoutDecoratorTest
	extends TestCase {

	//
	// Public methods
	//

	public void testConfig() {

		MetawidgetTestUtils.testEqualsAndHashcode( TabbedPaneLayoutDecoratorConfig.class, new TabbedPaneLayoutDecoratorConfig() {
			// Subclass
		} );
	}

	public void testTabPlacement() {

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setMetawidgetLayout( new TabbedPaneLayoutDecorator( new TabbedPaneLayoutDecoratorConfig().setLayout( new org.metawidget.swing.layout.GridBagLayout() ) ) );
		metawidget.setToInspect( new Foo() );

		JTabbedPane tabbedPane = (JTabbedPane) metawidget.getComponent( 0 );
		assertEquals( "Section", tabbedPane.getTitleAt( 0 ) );
		assertTrue( SwingConstants.TOP == tabbedPane.getTabPlacement() );
		JPanel panel = (JPanel) tabbedPane.getComponent( 0 );
		assertEquals( "Bar:", ( (JLabel) panel.getComponent( 0 ) ).getText() );
		assertTrue( panel.getComponent( 1 ) instanceof JTextField );
		assertTrue( panel.getComponent( 2 ) instanceof JPanel );
		assertTrue( 3 == panel.getComponentCount() );

		metawidget.setMetawidgetLayout( new TabbedPaneLayoutDecorator( new TabbedPaneLayoutDecoratorConfig().setTabPlacement( SwingConstants.BOTTOM ).setLayout( new org.metawidget.swing.layout.GridBagLayout() ) ) );
		tabbedPane = (JTabbedPane) metawidget.getComponent( 0 );
		assertEquals( "Section", tabbedPane.getTitleAt( 0 ) );
		assertTrue( SwingConstants.BOTTOM == tabbedPane.getTabPlacement() );
		panel = (JPanel) tabbedPane.getComponent( 0 );
		assertEquals( "Bar:", ( (JLabel) panel.getComponent( 0 ) ).getText() );
		assertTrue( panel.getComponent( 1 ) instanceof JTextField );
		assertTrue( panel.getComponent( 2 ) instanceof JPanel );
		assertTrue( 3 == panel.getComponentCount() );
	}

	public void testNestedTabs() {

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setMetawidgetLayout( new TabbedPaneLayoutDecorator( new TabbedPaneLayoutDecoratorConfig().setLayout( new TabbedPaneLayoutDecorator( new TabbedPaneLayoutDecoratorConfig().setLayout( new org.metawidget.swing.layout.GridBagLayout() ) ) ) ) );
		metawidget.setToInspect( new Bar() );

		assertEquals( "Abc:", ( (JLabel) metawidget.getComponent( 0 ) ).getText() );
		assertTrue( metawidget.getComponent( 1 ) instanceof JTextField );

		JTabbedPane outerTabbedPane = (JTabbedPane) metawidget.getComponent( 2 );
		assertEquals( "Foo", outerTabbedPane.getTitleAt( 0 ) );
		JPanel outerPanel = (JPanel) outerTabbedPane.getComponent( 0 );
		assertTrue( 4 == outerPanel.getComponentCount() );

		JTabbedPane innerTabbedPane = (JTabbedPane) outerPanel.getComponent( 0 );
		assertEquals( "Bar", innerTabbedPane.getTitleAt( 0 ) );
		JPanel barPanel = (JPanel) innerTabbedPane.getComponent( 0 );
		assertEquals( "Def:", ( (JLabel) barPanel.getComponent( 0 ) ).getText() );
		assertTrue( barPanel.getComponent( 1 ) instanceof JCheckBox );
		assertEquals( "Ghi:", ( (JLabel) barPanel.getComponent( 2 ) ).getText() );
		assertTrue( barPanel.getComponent( 3 ) instanceof JScrollPane );
		assertTrue( 4 == barPanel.getComponentCount() );

		assertEquals( "Baz", innerTabbedPane.getTitleAt( 1 ) );
		JPanel bazPanel = (JPanel) innerTabbedPane.getComponent( 1 );
		assertEquals( "Jkl:", ( (JLabel) bazPanel.getComponent( 0 ) ).getText() );
		assertTrue( bazPanel.getComponent( 1 ) instanceof JTextField );
		assertTrue( bazPanel.getComponent( 2 ) instanceof JPanel );
		assertTrue( 3 == bazPanel.getComponentCount() );

		assertEquals( "Mno:", ( (JLabel) outerPanel.getComponent( 1 ) ).getText() );
		assertTrue( outerPanel.getComponent( 2 ) instanceof JCheckBox );

		innerTabbedPane = (JTabbedPane) outerPanel.getComponent( 3 );
		assertEquals( "Moo", innerTabbedPane.getTitleAt( 0 ) );
		JPanel mooPanel = (JPanel) innerTabbedPane.getComponent( 0 );
		assertEquals( "Pqr:", ( (JLabel) mooPanel.getComponent( 0 ) ).getText() );
		assertTrue( mooPanel.getComponent( 1 ) instanceof JTextField );
		assertTrue( mooPanel.getComponent( 2 ) instanceof JPanel );
		assertTrue( 3 == mooPanel.getComponentCount() );

		assertEquals( "Stu:", ( (JLabel) metawidget.getComponent( 3 ) ).getText() );
		assertTrue( metawidget.getComponent( 4 ) instanceof JTextField );
		assertTrue( 5 == metawidget.getComponentCount() );

		// Test components within nested tabs still accessible by name

		assertTrue( metawidget.getComponent( 1 ) == metawidget.getComponent( "abc" ) );
		assertTrue( barPanel.getComponent( 1 ) == metawidget.getComponent( "def" ) );
		assertTrue( barPanel.getComponent( 3 ) == metawidget.getComponent( "ghi" ) );
		assertTrue( bazPanel.getComponent( 1 ) == metawidget.getComponent( "jkl" ) );
		assertTrue( outerPanel.getComponent( 2 ) == metawidget.getComponent( "mno" ) );
		assertTrue( mooPanel.getComponent( 1 ) == metawidget.getComponent( "pqr" ) );
		assertTrue( metawidget.getComponent( 4 ) == metawidget.getComponent( "stu" ) );
	}

	public static void main( String[] args ) {

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setMetawidgetLayout( new SeparatorLayoutDecorator( new SeparatorLayoutDecoratorConfig().setLayout( new TabbedPaneLayoutDecorator( new TabbedPaneLayoutDecoratorConfig().setLayout( new org.metawidget.swing.layout.GridBagLayout() ) ) ) ) );
		metawidget.setToInspect( new Baz() );

		JFrame frame = new JFrame( "Metawidget Tutorial" );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.getContentPane().add( metawidget );
		frame.setSize( 400, 210 );
		frame.setVisible( true );
	}

	//
	// Inner class
	//

	static class Foo {

		@UiSection( "Section" )
		public String	bar;
	}

	static class Bar {

		public String	abc;

		@UiSection( { "Foo", "Bar" } )
		public boolean	def;

		@UiLarge
		public String	ghi;

		@UiSection( { "Foo", "Baz" } )
		public String	jkl;

		@UiSection( { "Foo", "" } )
		public boolean	mno;

		@UiSection( { "Foo", "Moo" } )
		public String	pqr;

		@UiSection( "" )
		public String	stu;
	}

	static class Baz {

		@UiSection( { "Foo", "Bar" } )
		public String	abc;

		@UiSection( { "Baz" } )
		public boolean	def;
	}
}
