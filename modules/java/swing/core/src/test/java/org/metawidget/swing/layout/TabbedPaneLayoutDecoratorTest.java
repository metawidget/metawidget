// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

package org.metawidget.swing.layout;

import java.awt.Component;

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
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
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
		assertEquals( SwingConstants.TOP, tabbedPane.getTabPlacement() );
		JPanel panel = (JPanel) tabbedPane.getComponent( 0 );
		assertEquals( "Bar:", ( (JLabel) panel.getComponent( 0 ) ).getText() );
		assertTrue( panel.getComponent( 1 ) instanceof JTextField );
		assertTrue( panel.getComponent( 2 ) instanceof JPanel );
		assertEquals( 3, panel.getComponentCount() );

		metawidget.setMetawidgetLayout( new TabbedPaneLayoutDecorator( new TabbedPaneLayoutDecoratorConfig().setTabPlacement( SwingConstants.BOTTOM ).setLayout( new org.metawidget.swing.layout.GridBagLayout() ) ) );
		tabbedPane = (JTabbedPane) metawidget.getComponent( 0 );
		assertEquals( "Section", tabbedPane.getTitleAt( 0 ) );
		assertEquals( SwingConstants.BOTTOM, tabbedPane.getTabPlacement() );
		panel = (JPanel) tabbedPane.getComponent( 0 );
		assertEquals( "Bar:", ( (JLabel) panel.getComponent( 0 ) ).getText() );
		assertTrue( panel.getComponent( 1 ) instanceof JTextField );
		assertTrue( panel.getComponent( 2 ) instanceof JPanel );
		assertEquals( 3, panel.getComponentCount() );
	}

	@SuppressWarnings( "cast" )
	public void testNestedTabs() {

		SwingMetawidget metawidget = new SwingMetawidget();
		metawidget.setMetawidgetLayout( new TabbedPaneLayoutDecorator( new TabbedPaneLayoutDecoratorConfig().setLayout( new TabbedPaneLayoutDecorator( new TabbedPaneLayoutDecoratorConfig().setLayout( new org.metawidget.swing.layout.GridBagLayout() ) ) ) ) );
		metawidget.setToInspect( new Bar() );

		assertEquals( "Abc:", ( (JLabel) metawidget.getComponent( 0 ) ).getText() );
		assertTrue( metawidget.getComponent( 1 ) instanceof JTextField );

		JTabbedPane outerTabbedPane = (JTabbedPane) metawidget.getComponent( 2 );
		assertEquals( "Foo", outerTabbedPane.getTitleAt( 0 ) );
		JPanel outerPanel = (JPanel) outerTabbedPane.getComponent( 0 );
		assertEquals( 4, outerPanel.getComponentCount() );

		JTabbedPane innerTabbedPane = (JTabbedPane) outerPanel.getComponent( 0 );
		assertEquals( "Bar", innerTabbedPane.getTitleAt( 0 ) );
		JPanel barPanel = (JPanel) innerTabbedPane.getComponent( 0 );
		assertEquals( "Def:", ( (JLabel) barPanel.getComponent( 0 ) ).getText() );
		assertTrue( barPanel.getComponent( 1 ) instanceof JCheckBox );
		assertEquals( "Ghi:", ( (JLabel) barPanel.getComponent( 2 ) ).getText() );
		assertTrue( barPanel.getComponent( 3 ) instanceof JScrollPane );
		assertEquals( 4, barPanel.getComponentCount() );

		assertEquals( "Baz", innerTabbedPane.getTitleAt( 1 ) );
		JPanel bazPanel = (JPanel) innerTabbedPane.getComponent( 1 );
		assertEquals( "Jkl:", ( (JLabel) bazPanel.getComponent( 0 ) ).getText() );
		assertTrue( bazPanel.getComponent( 1 ) instanceof JTextField );
		assertTrue( bazPanel.getComponent( 2 ) instanceof JPanel );
		assertEquals( 3, bazPanel.getComponentCount() );

		assertEquals( "Mno:", ( (JLabel) outerPanel.getComponent( 1 ) ).getText() );
		assertTrue( outerPanel.getComponent( 2 ) instanceof JCheckBox );

		innerTabbedPane = (JTabbedPane) outerPanel.getComponent( 3 );
		assertEquals( "Moo", innerTabbedPane.getTitleAt( 0 ) );
		JPanel mooPanel = (JPanel) innerTabbedPane.getComponent( 0 );
		assertEquals( "Pqr:", ( (JLabel) mooPanel.getComponent( 0 ) ).getText() );
		assertTrue( mooPanel.getComponent( 1 ) instanceof JTextField );
		assertTrue( mooPanel.getComponent( 2 ) instanceof JPanel );
		assertEquals( 3, mooPanel.getComponentCount() );

		assertEquals( "Stu:", ( (JLabel) metawidget.getComponent( 3 ) ).getText() );
		assertTrue( metawidget.getComponent( 4 ) instanceof JTextField );
		assertEquals( 5, metawidget.getComponentCount() );

		// Test components within nested tabs still accessible by name

		assertEquals( (Component) metawidget.getComponent( 1 ), (Component) metawidget.getComponent( "abc" ) );
		assertEquals( (Component) barPanel.getComponent( 1 ), (Component) metawidget.getComponent( "def" ) );
		assertEquals( (Component) barPanel.getComponent( 3 ), (Component) metawidget.getComponent( "ghi" ) );
		assertEquals( (Component) bazPanel.getComponent( 1 ), (Component) metawidget.getComponent( "jkl" ) );
		assertEquals( (Component) outerPanel.getComponent( 2 ), (Component) metawidget.getComponent( "mno" ) );
		assertEquals( (Component) mooPanel.getComponent( 1 ), (Component) metawidget.getComponent( "pqr" ) );
		assertEquals( (Component) metawidget.getComponent( 4 ), (Component) metawidget.getComponent( "stu" ) );
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
		public String getBar() {

			return null;
		}

		public void setBar( @SuppressWarnings( "unused" ) String bar ) {

			// Do nothing
		}
	}

	static class Bar {

		public String getAbc() {

			return null;
		}

		public void setAbc( @SuppressWarnings( "unused" ) String abc ) {

			// Do nothing
		}

		@UiSection( { "Foo", "Bar" } )
		public boolean isDef() {

			return false;
		}

		public void setDef( @SuppressWarnings( "unused" ) boolean def ) {

			// Do nothing
		}

		@UiLarge
		public String getGhi() {

			return null;
		}

		public void setGhi( @SuppressWarnings( "unused" ) String ghi ) {

			// Do nothing
		}

		@UiSection( { "Foo", "Baz" } )
		public String getJkl() {

			return null;
		}

		public void setJkl( @SuppressWarnings( "unused" ) String jkl ) {

			// Do nothing
		}

		@UiSection( { "Foo", "" } )
		public boolean isMno() {

			return false;
		}

		public void setMno( @SuppressWarnings( "unused" ) boolean mno ) {

			// Do nothing
		}

		@UiSection( { "Foo", "Moo" } )
		public String getPqr() {

			return null;
		}

		public void setPqr( @SuppressWarnings( "unused" ) String pqr ) {

			// Do nothing
		}

		@UiSection( "" )
		public String getStu() {

			return null;
		}

		public void setStu( @SuppressWarnings( "unused" ) String stu ) {

			// Do nothing
		}
	}

	static class Baz {

		@UiSection( { "Foo", "Bar" } )
		public String getAbc() {

			return null;
		}

		public void setAbc( @SuppressWarnings( "unused" ) String abc ) {

			// Do nothing
		}

		@UiSection( { "Baz" } )
		public boolean getDef() {

			return false;
		}

		public void setDef( @SuppressWarnings( "unused" ) String def ) {

			// Do nothing
		}
	}
}
